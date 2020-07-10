package tanks;

import java.awt.Color;
import java.awt.Graphics;

public class WhiteCoin extends Figure {

	public WhiteCoin(Field polje) {
		super(polje);
	}

	@Override
	public boolean getWaterProof() {
		return false;
	}

	@Override
	public void draw() {
		Graphics g = field.getGraphics();
		g.setColor(Color.WHITE);
		g.fillOval(field.getWidth()/4, field.getHeight()/4, field.getWidth()/2, field.getHeight()/2);
	}

}
