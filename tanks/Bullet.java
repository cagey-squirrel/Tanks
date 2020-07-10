package tanks;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Figure implements Runnable {
	
	private int dirx;
	private int diry;
	private String owner;
	private Thread thread;

	public Bullet(Field polje, int dirx, int diry, String s) {
		super(polje);
		this.dirx = dirx;
		this.diry = diry;
		this.owner = s;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public synchronized void move() {
		Field p = field.findDistantField(dirx, diry);
		if (!(p == null))
			if(p.allowed(this)){
				field.repaint();
				moveFigure(p);
				return;
			}
		this.stop();
	}

	@Override
	public boolean getWaterProof() {
		return true;
	}

	@Override
	public void draw() {
		Graphics g = field.getGraphics();
		if(g == null)
			return;
		g.setColor(Color.BLACK);
		g.fillOval(field.getWidth()/4, field.getHeight()/4, field.getWidth()/2, field.getHeight()/2);
	}
	
	public synchronized void stop() {
		if(thread != null)
			thread.interrupt();
		thread = null;
		if(this.field!=null)
		if(this.field.getGrid()!= null)
		this.field.getGrid().removeBullet(this);
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				Thread.sleep(200);
				move();
				field.repaint();
			}
		
		}
		catch(InterruptedException e) {}
	}
	}


