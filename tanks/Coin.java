package tanks;


import java.awt.Color;
import java.awt.Graphics;


public class Coin extends Figure {

	public Coin(Field field) {
		super(field);
	}

	@Override
	public void draw() {
		Graphics g = field.getGraphics();
		g.setColor(Color.YELLOW);
		g.fillOval(field.getWidth()/4, field.getHeight()/4, field.getWidth()/2, field.getHeight()/2);
	}

	@Override
	public boolean getWaterProof() {
		return false;
	}

}
