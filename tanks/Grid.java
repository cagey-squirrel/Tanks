package tanks;


import java.awt.Label;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;



@SuppressWarnings("serial")
public class Grid extends Panel implements Runnable {
	

	private Field[][] fields;
	private Game game;
	private int dimension;
	
	private List<Coin> coins = new ArrayList<>();
	private List<Tank> tanks = new ArrayList<>();
	private List<Bullet> bullets = new ArrayList<>();
	private Player player;
	private WhiteCoin whiteCoin;
	
	private int numOfPoints = 0;
	private int numOfCoins = 0;
	private Thread thread;
	private boolean gameOn = false;
	
	Label lPoints; 
	Label lUntouchable;
	Label lBullets;
	
	
	//Constructors:
	public Grid(int dimension, Game game) {
		this.game = game;
		this.dimension = dimension;
		fields = new Field[dimension][dimension];
		int numOfGrasses = 0;
		while(true) {
			int n = (int)(Math.random() * dimension);
			int m = (int)(Math.random() * dimension);
			if(fields[n][m] != null)
				continue;
			fields[n][m] = new Grass(this);
			numOfGrasses++;
			if(numOfGrasses >= (int)(0.8*dimension*dimension))
				break;
		}
		
		for(int i = 0; i < dimension; i++) 
			for(int j = 0; j < dimension; j++)
				if(fields[i][j] == null)
					fields[i][j] = new Wall(this);

	}
	
	public Grid(Game game) {
		this(17, game);
	}
	
	//---------------------------------------------------------------------------------------------------
	
	
	
	//Methods for field echolocation:
	public int[] findFieldPosition(Field p) {
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				if(fields[i][j] == p)
					return new int[] {i,j};
		return null;
	}
	
	public Field findDistantField(Field p, int pomx, int pomy) {
		int coordinates[] = this.findFieldPosition(p);
		if( (coordinates[0] + pomx >= dimension) || (coordinates[0] + pomx < 0) )
			return null;
		if( (coordinates[1] + pomy >= dimension) || (coordinates[1] + pomy < 0) )
			return null;
		
		return fields[coordinates[0] + pomx][coordinates[1] + pomy];
	}
	
	public void addBullet(Bullet b) {
		synchronized(bullets) {
			bullets.add(b);
		}
	}
	
	public void removeBullet(Bullet b) {
		synchronized(bullets) {
			bullets.remove(b);
		}
	}
	
	public void shootBullet() {
		if(player == null)
			return;
		
		if(player.bullets == 0)
			return;
		Bullet b = new Bullet(player.field, player.dirx, player.diry, "Player");
		synchronized(bullets) {
			bullets.add(b);
		}
		b.start();
		player.bullets--;
		lBullets.setText("Bullets: " + player.bullets); 
	}
	
	//---------------------------------------------------------------------------------------------
	
	//Methods used in "run" method:
	private void draw() {
		synchronized(coins) {
			for(Coin n : coins) 
				n.draw();
		}
		
		synchronized(bullets) {
			for(Bullet b : bullets) 
				b.draw();
		}
		
		synchronized(tanks) {
			for(Tank t : tanks) {
				t.draw();
			}
		}
		if(whiteCoin!=null)
			whiteCoin.draw();
		
		if(player!=null)
			player.draw();
	}
	
	private void update() {

		boolean stop = false;
		
		Coin toRemove = null;
		synchronized(coins) {
		for(Coin n : coins)
			if(n.getField() == player.getField()) {
				numOfPoints++;
				n.getField().repaint();
				lPoints.setText("Points " + numOfPoints);
				lPoints.revalidate();
				toRemove = n;
			}
		if(numOfPoints == numOfCoins)
			stop = true;
		if(toRemove!=null)
			coins.remove(toRemove);
		}
		
		if(player!=null && whiteCoin != null)
		if(player.getField() == whiteCoin.getField()) {
			player.setUntouchable();
			whiteCoin = null;
		}
		if(whiteCoin == null)
			while(true) {
				boolean flag = true;
				int n = (int)(Math.random() * dimension);
				int m = (int)(Math.random() * dimension);
				if(fields[n][m] instanceof Wall || fields[n][m] instanceof Water) 
					continue;
				for(Coin nov : coins)
					if(nov.getField() == fields[n][m]) { 
						flag = false;
						break;
					}
				if(!flag)
					continue;

			whiteCoin = new WhiteCoin(fields[n][m]);
			break;
					
		}
		synchronized(bullets) {
			for(Bullet b : bullets) {
				if(b.getField() == player.getField() && b.getOwner() == "Tank")
					stop = true;
			}
		}

		synchronized(tanks) {
			List<Tank> tanksToRemove = new ArrayList<>();
			List<Bullet> bulletsToRemove = new ArrayList<>();
			for(Tank t : tanks) {
				if(t.getField() == player.getField())
					if(!player.getUntouchable())
						stop = true;
				synchronized(bullets) {
					for(Bullet b : bullets)
						if(t.getField() == b.getField() && b.getOwner()=="Player") {
							tanksToRemove.add(t);
							bulletsToRemove.add(b);
						}		
				}
			}
			for(Tank t : tanksToRemove)
				synchronized(tanks) {
					t.stop();
					tanks.remove(t);
				}
			for(Bullet b : bulletsToRemove)
				synchronized(bullets) {
					b.stop();
				}
					
		}
		if(stop) {
			stop();
		}
	}
	
	//-----------------------------------------------------------------------------------------------
	
	
	
	
	//Starting and stopping:
	public void stop() {
		gameOn = false;
		if(thread != null)
			thread.interrupt();
		thread = null;
		
		synchronized(tanks) {
			for(Tank t : tanks) {
				t.stop();
			}
		}
		
		
		
		synchronized(tanks) {
			tanks.clear();
		}
		
		synchronized(bullets) {
			bullets.clear();
		}
		
		synchronized(coins) {
			coins.clear();
		}
		
		player = null;
		
	}
	
	public void initializeGrid() {
	
		if(!game.getPlayTime()) { 
			return;
		}			
		
		gameOn = true;
		thread = new Thread(this);
		
		numOfPoints = 0;
		lPoints.setText("Points: 0");
		
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				fields[i][j].repaint();
		
		int x = game.getNumOfCoins();
		
		numOfCoins = x;
		
		int i = 0;
		while(i < x) {     
			int n = (int)(Math.random() * dimension);
			int m = (int)(Math.random() * dimension);
			Coin nov = new Coin(fields[n][m]);
			if(coins.contains(nov)) 
				continue; 
			if(fields[n][m] instanceof Wall || fields[n][m] instanceof Water) 
				continue;
			coins.add(nov);
			i++;
		}
		
		i = 0;
		while(i < (int)(x/3)) {  
			int n = (int)(Math.random() * dimension);
			int m = (int)(Math.random() * dimension);
			if(!(fields[n][m] instanceof Wall  || fields[n][m] instanceof Water)) {	
			   Tank tenk = new Tank(fields[n][m]);
			   tanks.add(tenk);
			   i++;
			}
		}
		
		
		while(true) {
			boolean flag = true;
			int n = (int)(Math.random() * dimension);
			int m = (int)(Math.random() * dimension);
			if(fields[n][m] instanceof Wall || fields[n][m] instanceof Water) 
				continue;
			for(Coin nov : coins)
				if(nov.getField() == fields[n][m]) { 
					flag = false;
					break;
				}
			if(!flag)
				continue;

			whiteCoin = new WhiteCoin(fields[n][m]);
			break;
					
		}
		
		
		while(true) {
			boolean flag = true;
			int n = (int)(Math.random() * dimension);
			int m = (int)(Math.random() * dimension);
			if(fields[n][m] instanceof Wall || fields[n][m] instanceof Water) 
				continue;
			for(Coin nov : coins)
				if(nov.getField() == fields[n][m]) { 
					flag = false;
					break;
				}
			if(!flag)
				continue;
			
			for(Tank tenk : tanks)
				if(tenk.getField() == fields[n][m]) {  
					flag = false;
					break;
				}
			
			if(!flag)
				continue;
			
			player = new Player(fields[n][m]);
			player.setLabel(lUntouchable, lBullets);
			break;
					
		}
			
		
		thread.start();		
		for(Tank t: tanks) 
			t.start();
		this.requestFocus();
	}
	
	//----------------------------------------------------------------------------------
	
	//Getter and setter methods:
	public Player getPlayer() {
		return player;
	}
	
	public List<Coin> getCoins() {
		return coins;
	}

	public List<Tank> getTanks() {
		return tanks;
	}
	
	public Field[][] getFields() {
		return fields;
	}
	
	public boolean getGameOn() {
		return gameOn;
	}
	
	public void setLbl(Label l, Label untouchable, Label bullets) {
		lPoints = l;
		lUntouchable = untouchable;
		lBullets = bullets;
	}
	
	//---------------------------------------------------------------------------------------------
	
	
	public void changeField(Field p) {

		if(game.getPlayTime()) 
			return;
		

		String s = game.getFieldType();
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				if(fields[i][j] == p) {
					if(s == "Grass") 
						if(!(fields[i][j] instanceof Grass)) {
							Field oldF = fields[i][j];
							Field newF = new Grass(this);
							fields[i][j] = newF;
							fields[i][j].repaint();
							game.updateField(oldF, newF, i, j);
						}
					
					if(s == "Wall") 
						if(!(fields[i][j] instanceof Wall)) {
							Field oldF = fields[i][j];
							Field newF = new Wall(this);
							fields[i][j] = newF;
							fields[i][j].repaint();
							game.updateField(oldF, newF, i, j);
						}
					
					if(s == "Water") 
						if(!(fields[i][j] instanceof Water)) {
							Field oldF = fields[i][j];
							Field newF = new Water(this);
							fields[i][j] = newF;
							fields[i][j].repaint();
							game.updateField(oldF, newF, i, j);
						}
					break;
				}
	}
	
	public void movePlayer(int pomX, int pomY) {
		if(!gameOn)
			return;
		player.move(pomX, pomY);
	}

	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				draw();
				update();
				Thread.sleep(40);
			}
		}
		catch(InterruptedException e) {}

	}
		

}
