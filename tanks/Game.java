package tanks;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;





@SuppressWarnings("serial")
public class Game extends Frame {
	
	private Grid grid = new Grid(this);
	
	//Grafic components
	private Label points = new Label("Points: 0");
	private Label untouchable = new Label("Untouchable: 0");
	private TextField tf = new TextField("10", 2); 
	private Button btnStart = new Button("Start");
	private TextField tfDimension = new TextField("17", 2);
	private Button btnNewDimension = new Button("Update size");
	private Label lBullets = new Label("Bullets: 0");
	
	private MenuItem changing = new MenuItem("Changing Time", new MenuShortcut('N'));
	private MenuItem playing = new MenuItem("Play Time");
	
	private CheckboxGroup gSurface = new CheckboxGroup(); 
	private Checkbox cGrass = new Checkbox("Grass", true, gSurface);
	private Checkbox cWall = new Checkbox("Wall", false, gSurface);
	private Checkbox cWater = new Checkbox("Water", false, gSurface);

	Panel pgrid = new Panel();
	
	//Info about game
	private boolean playTime = false;
	private String fieldType = "Grass";
	private int dimension = 17;
	private int numOfCoins = 10;

	//Constructor
	public Game() throws HeadlessException {
		super("Game");
		addPanels();
		addMenu();
		addListeners();
		this.setSize(500, 400);
		this.setVisible(true);
		System.out.println("Player moves by using WASD keys as well as using space for shooting");
		System.out.println("By collecting white coins you get 5 bullets and are invulnerable for 10 moves");
		
	}
	//-------------------------------------------------------------
	
	
	
	
	
	//Methods changing the state od the game:
	private void startGame() {
		grid.stop();
		grid.initializeGrid();
	}
	
	private void play() {
		if(playTime == false) {
			cGrass.setEnabled(false);
			cWall.setEnabled(false);
			cWater.setEnabled(false);
			btnStart.setEnabled(true);
			btnNewDimension.setEnabled(false);
			playTime = true;
		}
	}
	
	private void change() {
		if(playTime == true) {
			grid.stop();
			cGrass.setEnabled(true);
			cWall.setEnabled(true);
			cWater.setEnabled(true);
			btnStart.setEnabled(false);
			btnNewDimension.setEnabled(true);
			playTime = false;
		}
	}
	
	public void updateField(Field p1, Field p2, int x, int y) {
		pgrid.remove(p1);
		p1.repaint();
		pgrid.add(p2, x*dimension + y);
		p2.repaint();
		pgrid.revalidate();
	}
	
	public void newDimension() {
		dimension = Integer.parseInt(tfDimension.getText());
		grid = new Grid(dimension, this);
		grid.setLbl(points, untouchable, lBullets);
		this.remove(pgrid);
		pgrid = new Panel();
		pgrid.setLayout(new GridLayout(dimension,dimension));
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				pgrid.add(grid.getFields()[i][j]);
		
		this.add(pgrid, BorderLayout.CENTER);
		pgrid.revalidate();
		pgrid.repaint();
	}
	
	//-----------------------------------------------------------------------------------------------
	

	//Getter and setter methods:
	public int getNumOfCoins() {
		numOfCoins = Integer.parseInt(tf.getText());
		return numOfCoins;
	}
	
	public boolean getPlayTime() {
		return this.playTime;
	}
	
	public String getFieldType() {
		return this.fieldType;
	}
	
	public void setPlayTime(boolean pt) {
		playTime = pt;
	}

	
	//--------------------------------------------------------------------------------------------
	

	//Initializing window
	private void addListeners() {
		
		changing.addActionListener(e -> {change();}); 
		playing.addActionListener(e ->{play();});
		
		btnStart.addActionListener(e -> {this.requestFocus(); startGame();});		 
		
		cGrass.addItemListener(e -> {this.fieldType = "Grass";} );  
		cWall.addItemListener(e -> {this.fieldType = "Wall";});
		cWater.addItemListener(e -> {this.fieldType = "Water";});
		
		btnNewDimension.addActionListener(e-> {newDimension();});
			
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				grid.movePlayer(0, -1); break;
			case KeyEvent.VK_D:
				grid.movePlayer(0, 1); break;
			case KeyEvent.VK_S:
				grid.movePlayer(1, 0); break;
			case KeyEvent.VK_W:
				grid.movePlayer(-1, 0); break;
			case KeyEvent.VK_SPACE:
				grid.shootBullet(); break;
			}
			}
			});	
		
		this.addWindowListener(new WindowAdapter() {  //Window disposal
			public void windowClosing(WindowEvent e) { grid.stop(); dispose(); }
		});
	}
	
	private void addPanels() {
		
		this.setLayout(new BorderLayout());
		
		grid.setLbl(points, untouchable, lBullets);
		
		pgrid.setLayout(new GridLayout(dimension,dimension));
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				pgrid.add(grid.getFields()[i][j]);
		
		Label coins = new Label("Coins:");
		coins.setAlignment(Label.CENTER);
		points.setAlignment(Label.CENTER);
		tf.setColumns(2);
		
		Panel eastTop = new Panel();
		eastTop.setLayout(new BorderLayout());
		Panel pSurface = new Panel();
		pSurface.setLayout(new GridLayout(3,0,1,1));
		cGrass.setBackground(Color.GREEN);
		cWall.setBackground(Color.LIGHT_GRAY);
		cWater.setBackground(Color.BLUE);		
		pSurface.add(cGrass);
		pSurface.add(cWall);
		pSurface.add(cWater);
		Label lSurface = new Label("Surface:");
		lSurface.setAlignment(Label.CENTER);
		eastTop.add(lSurface, BorderLayout.WEST);
		eastTop.add(pSurface, BorderLayout.EAST);
		
		Panel eastBot = new Panel();
		eastBot.setLayout(new GridLayout(4,1));

		eastBot.add(new Label("Dimension: "), 0);
		eastBot.add(tfDimension, 1);
		eastBot.add(btnNewDimension, 2);
		eastBot.add(lBullets);

		
		btnStart.setEnabled(false);
		
		Panel p = new Panel();
		p.setLayout(new GridLayout(1,7,0,0));
		coins.setAlignment(Label.CENTER);
		p.add(coins,0);
		p.add(tf,1);
		p.add(points,2);
		points.setAlignment(Label.CENTER);
		p.add(btnStart,3);
		p.add(untouchable,4);
		p.setVisible(true);
		
		Panel pEast = new Panel();
		pEast.setLayout(new GridLayout(2,1));
		pEast.add(eastTop, 0);
		pEast.add(eastBot, 1);
		
		this.add(pgrid, BorderLayout.CENTER);
		this.add(p, BorderLayout.SOUTH);
		this.add(pEast, BorderLayout.EAST);
		
	}
	
	private void addMenu() {
		MenuBar menubar = new MenuBar();
		Menu regime = new Menu("Regime");
		regime.add(changing);
		regime.addSeparator();
		regime.add(playing);
		menubar.add(regime);
		this.setMenuBar(menubar);
	}
	
	//---------------------------------------------------------------------------------------------
	
	
	public static void main(String[] arg) {
		@SuppressWarnings("unused")
		Game i = new Game();
	}
	
}