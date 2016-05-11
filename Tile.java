package mazeGUI;

public class Tile {
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	
	int row;
	int col;
	Tile prev;
	int type;
	
	public Tile(int horz,int vert){
		row = horz;
		col = vert;
		prev = null;
		type = EMPTY;
		
	}



	public void setPrev(Tile prev) {
		this.prev = prev;
	}



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Tile getPrev() {
		return prev;
	}
	
	
}
