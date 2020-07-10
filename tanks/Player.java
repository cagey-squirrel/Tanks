package tanks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;


public class Player extends Figure {
	
	private boolean untouchable = false;
	private int untouchableMoves = 0;
	private Label lUntouchable;
	private Label lBullets;
	int bullets = 0;
	int dirx = 0;
	int diry = 0;

	public Player(Field field) {
		super(field);
	}

	@Override
	public void draw() {
		synchronized(field) {
		Graphics g = field.getGraphics();
		g.setColor(Color.RED);
		if(untouchable)
			g.setColor(Color.WHITE);
		g.drawLine(field.getWidth()/2, 0, field.getWidth()/2, field.getHeight());
		g.drawLine(0, field.getHeight()/2, field.getWidth(), field.getHeight()/2);
		}
	}
	
	@Override
	public boolean getWaterProof() {
		return untouchable;
	}
	
	public void setUntouchable() {
		untouchable = true;
		untouchableMoves = 10;
		bullets += 5;
		lBullets.setText("Bullets: " + bullets);
		lUntouchable.setText("Untouchable: 10");
	}
	
	public void setLabel(Label l1, Label l2) {
		lUntouchable = l1;
		lBullets = l2;
	}
	
	public boolean getUntouchable() {
		return untouchable;
	}
	
	public void move (int pomX, int pomY) {
		synchronized(field) {
		Field p = field.findDistantField(pomX, pomY);
		if (p == null) { 
			return;
		}
		if(!p.allowed(this)) { 
			return;
		}
		dirx = pomX;
		diry = pomY;
		
		this.field.repaint();
		
		if(untouchableMoves>0) {
			untouchableMoves--;
			lUntouchable.setText("Untouchable: " + untouchableMoves);
		}
		if(untouchableMoves == 0)
			untouchable = false;
		
		moveFigure(p);
		}
		return;
		
	}

}
