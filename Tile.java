
/**
 * This class contains everything to do with one specific tile in the maze. Most importantly, it contains the type and the location in the sprite matrix (in renderer) where you can find the correct sprite for that tile
 *
 */
public class Tile {

	//Class Constants -- Only Empty is needed here, because the others aren't used in this class
		//Tile status definitions -- ones commented out are simply not used in the code	
			private static final int EMPTY = 0;
			//private static final int FLOOR = 1;
			//private static final int WALL  = 2;
			//private static final int START = 3;
			//private static final int EXIT  = 4;
			//private static final int SENTRY  = 5;
			//private static final int KEY = 6;
	
	//class variables
		private int row;
		private int col;
		private int type; 
		private int imgCol; //hold the type of sprite for Renderer to show --tom
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

	/**
	 * Get column index of image in Tile.
	 * 
	 * @return int column index
	 */
	public int getImgCol() {
		return imgCol;
	}


	/**
	 * Set column index for image in tile.
	 * 
	 * @param imgCol int image column
	 */
	public void setImgCol(int imgCol) {
		this.imgCol = imgCol;
	}

	/**
	 * Get row index of image in Tile.
	 * 
	 * @return int row index
	 */
	public int getImgRow() {
		return imgRow;
	}


	/**
	 * Set row index for image in tile.
	 * 
	 * @param imgRow int image row
	 */
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



	
}