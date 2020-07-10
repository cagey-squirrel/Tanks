package tanks;

import java.awt.Graphics;

public class Tank extends Figure implements Runnable {
	
	private Thread thread;
	private int moveCount = 0;
	private int dirx = 0;
	private int diry = 0;

	
	public Tank(Field field) {
		super(field);
	}
	

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				Thread.sleep(500);
				this.moveRandomly();
				field.repaint();
				
			}
		
		}
		catch(InterruptedException e) {}
	}
	
	private void moveRandomly() {
		
		double d = Math.random();
		boolean tankMoved = false;
		synchronized(field) { 
		if(d < 0.25) {
			Field p = field.findDistantField(-1, 0);
			if (!(p == null))
				if(p.allowed(this)){
					field.repaint();
					moveFigure(p);
					tankMoved = true;
					moveCount++;
					dirx = -1;
					diry = 0;
				}

		}
		if(d < 0.50 && !tankMoved) {
			Field p = field.findDistantField(1, 0);
			if (!(p == null))
				if(p.allowed(this)){
					field.repaint();
					moveFigure(p);
					tankMoved = true;
					moveCount++;
					dirx = 1;
					diry = 0;
				}
		}
		if(d < 0.75 && !tankMoved) {
			Field p = field.findDistantField(0, -1);
			if (!(p == null))
				if(p.allowed(this)){
					field.repaint();
					moveFigure(p);
					tankMoved = true;
					moveCount++;
					dirx = 0;
					diry = -1;
				}
		}
		if (d <= 1  && !tankMoved) {
			Field p = field.findDistantField(0, 1);
			if (!(p == null))
				if(p.allowed(this)){
					field.repaint();
					moveFigure(p);
					tankMoved = true;
					moveCount++;
					dirx = 0;
					diry = 1;
				}
			}
		}
		if(moveCount == 4) {
			moveCount = 0;
			Bullet b = new Bullet(this.field, dirx, diry, "Tank");
			this.field.getGrid().addBullet(b);
			b.start();
		}
			
	}
	
	@Override
	public boolean getWaterProof() {
		return false;
	}

	
	public void stop() {
		if(thread != null)
			thread.interrupt();
		thread = null;
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void draw() { 
		if (field == null)
			return;
		synchronized(field) { 
			field.repaint();
			Graphics g = field.getGraphics();
			g.drawLine(0, 0, field.getWidth(), field.getHeight());
			g.drawLine(field.getWidth(), 0, 0, field.getHeight());
		}

	}

}
