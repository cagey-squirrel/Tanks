package tanks;

public abstract class Figure {
	
	protected Field field;

	public Figure(Field polje) {
		super();
		this.field = polje;
	}

	public Field getField() {
		synchronized(field) {
			return field;
		}
	}
	
	public abstract boolean getWaterProof();
	
	public void moveFigure(Field f) {
		synchronized(field) {
			field = f;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Figure other = (Figure) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		return true;
	}
	
	public abstract void draw();
	
	
	
}
