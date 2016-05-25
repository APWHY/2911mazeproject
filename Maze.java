import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Maze {
	
	//Tile status definitions
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	private static final int SENTRY  = 5;
	private static final int KEY = 6;
	
	// Maze generator constants
	private static final float CYCLECHANCE = (float) 0.75;
	private static final int NUMSEN = 30; // Numbers of Sentries - Andy
	private static final int SENTRYSPACE = 3; // Space in between Sentries
	
	private Tile[][] maze;
	private ArrayList<Sentry> sentries;
	private boolean keyStatus; // Currently unused
	private int size;

	/**
	 * Constructor.
	 * 
	 * @param size Maze size
	 */
	public Maze(int size){
		
		this.size = size;
		this.maze = new Tile[size][size];
		this.sentries = new ArrayList<Sentry>();
		
		do { // Initialize maze grid
			for (int n = 0; n < size; n++){
				for (int m = 0; m < size; m++){
					this.maze[n][m] = new Tile(n,m);
				}
			}
			generateMaze(size);
		} while (!goodMaze()); // Check if maze is good

		addSentries(NUMSEN);
		addKey(maze, size); // Adding the key to the maze - Andy
		keyStatus = false;
	}
	
	//Adding sentries function - Andy
	/**
	 * Adds instances of the Sentry class to a maze.
	 * 
	 * @param num
	 */
	public void addSentries(int num) {
		
		Random x = new Random();
		Random y = new Random();
		int r = 0;
		int c = 0;
		ArrayList<Tile> avoid = new ArrayList<Tile>();
		boolean tooClose = false;
		
		while (num > 0) {
			r = x.nextInt(size);
			c = y.nextInt(size);
			tooClose = false;
		
			for (Sentry sentry: this.sentries ){
				avoid.add(getTile(sentry.getColumn(),sentry.getRow()));
			}
			
			avoid.add(getTileType(EXIT));
			avoid.add(getTileType(START));
			
			for (Tile tile: avoid){
				if(Math.sqrt(Math.pow(tile.getRow()-r,2) + Math.pow(tile.getCol()-c,2)) < SENTRYSPACE){
					tooClose = true;
				}
			}
			
			if (this.getTile(c, r).getType() == WALL && tooClose == false) {
				Sentry s = new Sentry(c, r);
				this.sentries.add(s);
				num--;
			}
		}
	}
	
	// Function to add the key to the maze - Andy
	
	public void addKey(Tile[][] maze, int size) {
		int row = size;
		int col = 0;
		do {
			Random x = new Random();
			Random y = new Random();
			row = x.nextInt(size);
			col = y.nextInt(size);
			if(maze[row][col].getType() == FLOOR && isCorner(maze[row][col])) { // if the tile is a floor and a corner, add a key to it
				maze[row][col].setType(KEY);
			}
		}while(maze[row][col].getType() != KEY); // continue the loop if the tile is not a floor or a corner
	}
	
	// Function to check if a tile is a corner (one pathway only) - Andy
	
	public boolean isCorner(Tile tile) {
		int count = 0;
		if (getN(tile) != null && getN(tile).getType() == FLOOR) {
			count++;
		}
		if (getS(tile) != null && getS(tile).getType() == FLOOR) {
			count++;
		}
		if (getE(tile) != null && getE(tile).getType() == FLOOR) {
			count++;
		}
		if (getW(tile) != null && getW(tile).getType() == FLOOR) {
			count++;
		}
		if (count == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Rotates sentries in the Maze.
	 */
	public void updateMaze(){
		for (Sentry sentry : this.sentries){
			sentry.updateDegree();
		}
	}

	/**
	 * Get a list of the sentries in the maze.
	 * 
	 * @return ArrayList of Sentries
	 */
	public ArrayList<Sentry> getSentries() {
		return this.sentries;
	}

	
	/**
	 * Maze generation function using pseudo BFS on Tile grid.
	 * 
	 * @param size Maze size
	 */
	private void generateMaze(int size) { // generates maze using random walk (at least that's what I think I'm doing)

		Random rand = new Random();
		
		Tile start = new Tile(rand.nextInt(size),rand.nextInt(size)); // Choose starting Tile at random
		start.setType(START);
		
		this.maze[start.getCol()][start.getRow()] = start;
		
		LinkedList<Tile> ends = new LinkedList<Tile>(); // Queue of Tiles representing paths that have not been closed as a List 
		LinkedList<Tile> choices = new LinkedList<Tile>(); // Holds the Tiles that can be travelled to from the current Tile
		
		int branches = 0;
		int temp = 0;
		
		Tile cur = null;
		Tile process = null;
		
		ends.add(start); // Add starting Tile to ends Queue
		
		while (!ends.isEmpty()){ // While there are still open paths
			
			cur = ends.remove(); // Take an open path from the Queue
			
			if (this.getN(cur)!= null){ // Add adjacent undiscovered Tiles to the choices list
				if(this.getN(cur).getType() == EMPTY) choices.add(getN(cur));
			}
			
			if (this.getS(cur)!= null){
				if(this.getS(cur).getType() == EMPTY) choices.add(getS(cur));
			}
			
			if (this.getE(cur)!= null){
				if(this.getE(cur).getType() == EMPTY) choices.add(getE(cur));
			}
			
			if (this.getW(cur)!= null){
				if(this.getW(cur).getType() == EMPTY) choices.add(getW(cur));
			}

			if (!choices.isEmpty()){		
				
				// Choose a number from 0 to the number of choices, skewed towards 0 by taking the uniform distribution to the power of 1.5
				branches = (int)Math.round(Math.sqrt(rand.nextFloat()*rand.nextFloat()*rand.nextFloat()))*(choices.size()-1);
				
				// Branches is the number of possible options that WILL be explored (not all available options may be explored)
				while (branches >= 0 && !choices.isEmpty()){ // For each remaining branch
					
					if (choices.size() == 1){ // Pick one of the choices randomly
						temp = 0;
					} else {
						temp = Math.round(rand.nextFloat())*(choices.size()-1);
					}
					 
					process = choices.remove(temp); // Take it off the choices list
					
					// Regulate the number of cycles by randomly checking the status of each direction from the current Tile
					if (rand.nextFloat() > CYCLECHANCE && getN(process)!= null && getN(process) != cur && getN(process).getType() == FLOOR){
						process.setType(WALL);
					} else if (rand.nextFloat() > CYCLECHANCE && getS(process)!= null && getS(process) != cur && getS(process).getType() == FLOOR){
						process.setType(WALL);
					} else if (rand.nextFloat() > CYCLECHANCE && getW(process)!= null && getW(process) != cur && getW(process).getType() == FLOOR){
						process.setType(WALL);
					} else if (rand.nextFloat() > CYCLECHANCE && getE(process)!= null && getE(process) != cur && getE(process).getType() == FLOOR){
						process.setType(WALL);
					} else {
						process.setType(FLOOR); // Set Tile to FLOOR if cycle test is passed or ignored
						ends.add(process); // Add Tile to the list of unfinished paths
					}

					branches--; // reduces the number of branches 
				}
				
				while (choices.size() > 0){ // Set remaining choices as wall after all the branches have been accounted
					process = choices.remove();
					process.setType(WALL);
				}
				
				// These finish off diagonal walling logic for 2,3 or 4 way forks.
				if (getN(cur) != null && getN(cur).getType() == FLOOR){ 
					if (getW(cur) != null &&getW(cur).getType() == FLOOR && getNW(cur).getType()!=FLOOR){
						getNW(cur).setType(WALL);//needed because these are essentially corners
					}
					
					if (getE(cur) != null &&getE(cur).getType() == FLOOR && getNE(cur).getType()!=FLOOR){
						getNE(cur).setType(WALL);
					}
				}
				
				if (getS(cur) != null && getS(cur).getType() == FLOOR){
					if (getW(cur) != null &&getW(cur).getType() == FLOOR && getSW(cur).getType()!=FLOOR){
						getSW(cur).setType(WALL);
					}
					
					if (getE(cur) != null &&getE(cur).getType() == FLOOR && getSE(cur).getType()!=FLOOR){
						getSE(cur).setType(WALL);
					}
				}
			}
		}

		cur.setType(EXIT); // Select the last visited node as the exit Tile
	}

	/**
	 * Check the number of unreachable Tiles in the maze
	 * 
	 * @return Boolean result
	 */
	private boolean goodMaze(){
		
		int count = 0;
		
		for (int n = 0;n < size;n++){
			for (int m = 0;m < size;m++){
				if (maze[n][m].getType() == EMPTY){
					count++;
				}
			}
		}

		if (count > 15){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Get relative Tile above some current Tile.
	 * 
	 * @param tile Current Tile
	 * @return Tile above current Tile 
	 */
	public Tile getN(Tile tile){
		
		if (tile.getRow() > 0){
			return maze[tile.getCol()][tile.getRow()-1];
		} else {
			return null;
		}
	}
	
	/**
	 * Get relative Tile below some current Tile.
	 * 
	 * @param tile Current Tile
	 * @return Tile below current Tile
	 */
	public Tile getS(Tile tile){
		
		if (tile.getRow() < size-1){
			return maze[tile.getCol()][tile.getRow()+1];
		} else {
			return null;
		}
	}
	
	/**
	 * Get relative Tile to the right of some current Tile.
	 * 
	 * @param tile Current Tile
	 * @return Tile to the right of current Tile
	 */
	public Tile getE(Tile tile){
		if (tile.getCol() < size-1){
			return maze[tile.getCol()+1][tile.getRow()];
		} else {
			return null;
		}
	}
	
	/**
	 * Get relative Tile to the left some current Tile.
	 * 
	 * @param tile Current Tile
	 * @return Tile to the left current Tile
	 */
	public Tile getW(Tile tile){
		if (tile.getCol() > 0){
			return maze[tile.getCol()-1][tile.getRow()];
		} else {
			return null;
		}
	}
	
	
	public Tile getNW(Tile tile){
		return getW(getN(tile));
	}
	
	public Tile getNE(Tile tile){
		return getE(getN(tile));
	}
	
	public Tile getSW(Tile tile){
		return getW(getS(tile));
	}
	
	public Tile getSE(Tile tile){
		return getE(getS(tile));
	}
	
	public boolean isKeyStatus() {
		return keyStatus;
	}

	public int getSize() {
		return size;
	}

	public Tile getTile(int col, int row){
		return maze[col][row];
	}
	
	/**
	 * Gets the first instance of a Tile of the given type.
	 * 
	 * @param type Tile type
	 * @return Tile
	 */
	public Tile getTileType(int type){
		for(int n = 0;n < size;n++){
			for(int m = 0;m < size;m++){
				if(maze[n][m].getType() == type)return maze[n][m];
			}
		}
		return null;
	}
	
	//Irfan -renamed function for User.java
	/**
	 * Get the type of a Tile.
	 * 
	 * @param row 
	 * @param col
	 * @return
	 */
	public int tileType(int row, int col){
		return maze[col][row].getType();
	}
	
	
}