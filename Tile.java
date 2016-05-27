
public class Tile {

	// Constants -- Might not need as type is set during maze generation.
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	
	private int row;
	private int col;
	private Tile prev; // Not currently in use
	private int type; // Not used by own class
	private int imgCol;
	private int imgRow;
	
	/**
	 * Constructor.
	 * 
	 * @param col Column index of Tile 
	 * @param row Row index of Tile
	 */
	public Tile(int col,int row){
	
		this.col = col;
		this.row = row;
		this.type = EMPTY;		
		this.imgCol = 0;
		this.imgRow = 0;

	}



	public int getImgCol() {
		return imgCol;
	}



	public void setImgCol(int imgCol) {
		this.imgCol = imgCol;
	}



	public int getImgRow() {
		return imgRow;
	}



	public void setImgRow(int imgRow) {
		this.imgRow = imgRow;
	}



	/**
	 * Set Tile type.
	 * 
	 * @param type Tile type as defined in Maze.java
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Get tile type.
	 * 
	 * @return Tile type as defined in maze.java
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Get Tile's column index.
	 * 
	 * @return Column index.
	 */
	public int getCol() {
		return this.col;
	}

	/**
	 * Get Tile's row index.
	 * 
	 * @return Row index.
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * Set a prev Tile. Can be used to make paths.
	 * 
	 * @param prev Prev Tile
	 */
	public void setPrev(Tile prev) {
		this.prev = prev;
	}

	/**
	 * Get the Prev Tile.
	 * 
	 * @return Tile
	 */
	public Tile getPrev() {
		return prev;
	}
	
}