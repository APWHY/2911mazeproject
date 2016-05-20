public class Tile {

	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	
	private int row;
	private int col;
	private Tile prev;
	private int type;
	
	public Tile(int horz,int vert){
		
		col = horz;
		row = vert;
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