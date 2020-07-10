package tanks;

import java.awt.Color;

@SuppressWarnings("serial")
public class Grass extends Field {

	public Grass(Grid grid) {
		super(grid);
		this.setBackground(Color.GREEN);
	}


	@Override
	public boolean allowed(Figure f) {
		return true;
	}

}
