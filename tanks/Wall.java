package tanks;

import java.awt.Color;

@SuppressWarnings("serial")
public class Wall extends Field {

	public Wall(Grid grid) {
		super(grid);
		this.setBackground(Color.LIGHT_GRAY);
	}

	@Override
	public boolean allowed(Figure f) {
		return false;
	}

}
