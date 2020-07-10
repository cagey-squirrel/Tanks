package tanks;

import java.awt.Color;

public class Water extends Field {

	public Water(Grid grid) {
		super(grid);
		this.setBackground(Color.blue);
	}

	@Override
	public boolean allowed(Figure f) {
		return f.getWaterProof();
	}

}
