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
	
	// Maze generator constants
	private static final float CYCLECHANCE = (float) 0.75;
	private static final int NUMSEN = 30; // Numbers of Sentries - Andy
	private static final int SENTRYSPACE = 3; // Space in between Sentries
	
	private Tile[][] maze;
	private ArrayList<Sentry> sentries;
	private boolean keyStatus; // Currently unused
	private int size;

	
	public Maze(int size){
		
		this.size = size;
		this.maze = new Tile[size][size];
		this.sentries = new ArrayList<Sentry>();
		
		do{
			for (int n = 0; n < size; n++){
				for (int m = 0; m < size; m++){
					this.maze[n][m] = new Tile(n,m);
				}
			}
			generateMaze(size); //should check quality of maze (no. of empty squares?) and regenerate if it sucks
		} while (!goodMaze());	
		
		addSentries(NUMSEN);// Adding sentries to the maze - Andy
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
				avoid.add(getOne(sentry.getColumn(),sentry.getRow()));
			}
			
			avoid.add(getOneType(EXIT));
			avoid.add(getOneType(START));
			
			for (Tile tile: avoid){
				if(Math.sqrt(Math.pow(tile.getRow()-r,2) + Math.pow(tile.getCol()-c,2)) < SENTRYSPACE){
					tooClose = true;
				}
			}
			
			if (this.getOne(c, r).getType() == WALL && tooClose == false) {
				//this.getOne(r, c).setType(SENTRY); we don't need to set the type to sentry since they act as walls
				Sentry s = new Sentry(c, r);
				this.sentries.add(s);
				num--;
			}
		}
	}
	
	// add timer
	
	/**
	 * 
	 */
	public void updateMaze(){
		
		for (Sentry sentry: this.sentries){
			sentry.updateDegree();
		}
		
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public ArrayList<Sentry> getSentries() {
		return this.sentries;
	}

	
	/**
	 * Maze generation function.
	 * 
	 * @param size
	 */
	private void generateMaze(int size) { // generates maze using random walk (at least that's what I think I'm doing)

		Random rand = new Random();
		
		Tile start = new Tile(rand.nextInt(size),rand.nextInt(size)); // Choose starting Tile at random
		start.setType(START);
		
		this.maze[start.getCol()][start.getRow()] = start;
		
		LinkedList<Tile> ends = new LinkedList<Tile>(); //this holds the number of paths that haven't been closed off
		LinkedList<Tile> choices = new LinkedList<Tile>();//holds the number of possible tiles that can be traveled to from the current one
		
		int branches = 0;
		int temp = 0;
		
		
		Tile cur = null;
		Tile process = null;
		
		ends.add(start);//adds our starting position to the ends list
		
		
		while (!ends.isEmpty()){ //so while we still have some paths that haven't been closed off
			
			cur = ends.remove(); //take an unfinished path off the queue
			
			if (this.getN(cur)!= null){ //and add cells that are adjacent to it and have yet to be discovered to the choices list
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

			if (!choices.isEmpty()){		//if there are some tiles to choose from ----	
	//there are two options here. The long line means that sometimes the maze will not extend an end even if the option exists
	//the large commented out section means that if an option exists at least one will be taken. It provides more complete mazes but 
	//the mazes don't look as nice and become bloody hard
				//pick a number from 0 to the number of choices, skewed towards 0 by taking the uniform distribution to the power of 1.5
				branches = (int)Math.round(Math.sqrt(rand.nextFloat()*rand.nextFloat()*rand.nextFloat()))*(choices.size()-1);
				
//				if (choices.size() == 1){//picks a number from 1 to the number of choices uniformly or skewed
//					branches = 1;
//				}else{
//					branches = rand.nextInt(choices.size()-1)+1; //uniform
//					//branches = (int)Math.round(Math.sqrt(rand.nextFloat()*rand.nextFloat()*rand.nextFloat()))*(choices.size()-2)+1;//skewed
//				}
				
				//branches is the number of possible options that WILL be explored (so usually not all available options are explored)
				while (branches >= 0 && !choices.isEmpty()){ //for each remaining branch provided for, we
					
					if (choices.size() == 1){//pick one of the choices randomly
						temp = 0;
					} else {
						temp = Math.round(rand.nextFloat())*(choices.size()-1);
					}
					 
					process = choices.remove(temp);//take it off the choices list
					process.setPrev(cur);//THIS LINE IS USELESS IM GOING TO REMOVE IT SOME TIME
					
					//the next bit of code will block the chance of cycles being discovered if they exist
					//you actually want some cycles, so CYCLECHANCE can be adjusted to change the chance we ignore the potential for a cycle
					if (rand.nextFloat() > CYCLECHANCE && getN(process)!= null && getN(process) != cur && getN(process).getType() == FLOOR){
						process.setType(WALL);
					} else if (rand.nextFloat() > CYCLECHANCE && getS(process)!= null && getS(process) != cur && getS(process).getType() == FLOOR){
						process.setType(WALL);
					}else if (rand.nextFloat() > CYCLECHANCE && getW(process)!= null && getW(process) != cur && getW(process).getType() == FLOOR){
						process.setType(WALL);
					}else if (rand.nextFloat() > CYCLECHANCE && getE(process)!= null && getE(process) != cur && getE(process).getType() == FLOOR){
						process.setType(WALL);
					}else{
						process.setType(FLOOR);		//set it to floor if it passes the cycle test(or the test just gets ignored)
						ends.add(process);//add it to the list of unfinished paths
					}

					branches--;//reduces the number of br anches provided for
				}
				
				while (choices.size() > 0){//after all the branches have been accounted for, any remaining choices get turned into walls
					process = choices.remove();
					process.setType(WALL);
				}
				
				if (getN(cur) != null && getN(cur).getType() == FLOOR){//these finish off diagonal walling logic for 2,3 or 4 way forks.
					if(getW(cur) != null &&getW(cur).getType() == FLOOR && getNW(cur).getType()!=FLOOR){
						getNW(cur).setType(WALL);//needed because these are essentially corners
					}
					
					if(getE(cur) != null &&getE(cur).getType() == FLOOR && getNE(cur).getType()!=FLOOR){
						getNE(cur).setType(WALL);
					}
				}
				
				if (getS(cur) != null && getS(cur).getType() == FLOOR){
					if(getW(cur) != null &&getW(cur).getType() == FLOOR && getSW(cur).getType()!=FLOOR){
						getSW(cur).setType(WALL);
					}
					
					if(getE(cur) != null &&getE(cur).getType() == FLOOR && getSE(cur).getType()!=FLOOR){
						getSE(cur).setType(WALL);
					}
				}
			}
		}

		cur.setType(EXIT);//picks the last visited node as the exit
	}

	/**
	 * 
	 * @return
	 */
	private boolean goodMaze(){ // makes sure there aren't too many empty squares in the maze
		
		int count = 0;
		
		for(int n = 0;n < size;n++){
			for(int m = 0;m < size;m++){
				if(maze[n][m].getType() == EMPTY){
					count++;
				}
			}
		}

		if(count > 15){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * 
	 * @param tile
	 * @return
	 */
	public Tile getN(Tile tile){
		
		if(tile.getRow() > 0){
			return maze[tile.getCol()][tile.getRow()-1];
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * 
	 * @param tile
	 * @return
	 */
	public Tile getS(Tile tile){
		
		if(tile.getRow() < size-1){
			return maze[tile.getCol()][tile.getRow()+1];
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param tile
	 * @return
	 */
	public Tile getE(Tile tile){
		if(tile.getCol() < size-1){
			return maze[tile.getCol()+1][tile.getRow()];
		}else{
			return null;
		}
	}
	public Tile getW(Tile tile){
		if(tile.getCol() > 0){
			return maze[tile.getCol()-1][tile.getRow()];
		}else{
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

	public Tile getOne(int col, int row){
		return maze[col][row];
	}
	public Tile getOneType(int type){//given a type (hopefully unique like start or end or key) will return first found instance of that type
		for(int n = 0;n < size;n++){
			for(int m = 0;m < size;m++){
				if(maze[n][m].getType() == type)return maze[n][m];
			}
		}
		return null;
	}
	
	//Irfan -renamed function for User.java
	public int tileType(int row, int col){
		return maze[col][row].getType();
	}
	
	
}