package tanks;

import java.awt.Canvas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public abstract class Field extends Canvas {
	
	private Grid grid;

	public Field(Grid grid) {
		super();
		this.grid = grid;
		this.setSize(10,10);
		this.addMouseListener(new MouseEventHandler());
	}
	
private class MouseEventHandler extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			grid.changeField(Field.this);
		}		
}

	public Grid getGrid() {
		return grid;
	}
	
	public int[] findPosition() {
		return grid.findFieldPosition(this);
	}
	
	
	public Field findDistantField(int pomx, int pomy) {
		return grid.findDistantField(this, pomx, pomy);
	}
	
	
	
	public abstract boolean allowed(Figure f);
	
}
