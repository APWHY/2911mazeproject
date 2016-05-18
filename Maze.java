package mazeGUI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
//use bytes instead of ints?

public class Maze {

	//Tile status definitions
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	private static final int SENTRY  = 5;	
	
	private static final float CYCLECHANCE = (float) 0.75;
	private static final int numSen = 3; // Numbers of sentries - Andy

	private Tile[][] maze;
	private ArrayList<Sentry> sentries;
	private boolean keyStatus;
	private int size;

	public Tile start; //Irfan
	
	/**
	* Constructor, creates a new Maze class.
	*/
	public Maze(int size){
		
		this.size = size;
		maze = new Tile[size][size];
		
		do{
			for (int n = 0; n < size; n++){
				for (int m = 0; m < size; m++){
					maze[n][m] = new Tile(n,m);
				}
			}
			generateMaze(size); //should check quality of maze (no. of empty squares?) and regenerate if it sucks
		}while(!goodMaze());
		
		addSentries(numSen, maze); // Adding sentries to the maze - Andy
		keyStatus = false;
	}
	
	//Adding sentries function - Andy
	/**
	* 
	* 
	* @param num Number of Sentries to be inserted.
	* @param maze 2d array of Tiles 
	*/
	public void addSentries(int num, Tile[][] maze) {
		while(num > 0) {
			Random x = new Random();
			Random y = new Random();
			int r = x.nextInt(size);
			int c = y.nextInt(size);
			if(this.getOne(r, c).getType() == WALL) {
				this.getOne(r, c).setType(SENTRY);
				Sentry s = new Sentry(r, c);
				sentries.add(s);
				num--;
			}
		}
	}
	
	// add timer
	
	/**
	* Maze generation function.
	* 
	* 
	*
	*/
	private void generateMaze(int size) { //generates maze using random walk (at least that's what I think I'm doing)

		Random rand = new Random();
		this.start = new Tile(rand.nextInt(size),rand.nextInt(size)); // create randomly chosen start Tile
		this.start.setType(START);
		this.maze[start.getRow()][start.getCol()] = start;
		LinkedList<Tile> ends = new LinkedList<Tile>(); // LinkedList as Queue of avilable paths || this holds the number of paths that haven't been closed off
		LinkedList<Tile> choices = new LinkedList<Tile>(); // Holds the Tiles that can be travelled too
		ends.add(start); // Add starting Tile to the ends list
		int branches = 0; // Number of adjacent Tiles explored from some Tile
		int temp = 0;
		Tile cur = null
		Tile process = null;
		
		while (!ends.isEmpty()){ // While there are still available paths
		
			cur = ends.remove(); // Take an unfinished path off the Queue || Examine the next unfinished path
			
			if (this.getN(cur)!= null){//and add cells that are adjacent to it and have yet to be discovered to the choices list
				if(this.getN(cur).getType() == EMPTY) choices.add(getN(cur)); // why not and the two ifs
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

			if (!choices.isEmpty()){ // If there are Tiles to choose from
			
	//there are two options here. The long line means that sometimes the maze will not extend an end even if the option exists
	//the large commented out section means that if an option exists at least one will be taken. It provides more complete mazes but 
	//the mazes don't look as nice and become bloody hard
			
			    //pick a number from 0 to the number of choices, skewed towards 0 by taking the uniform distribution to the power of 1.5
				//
				branches = (int)Math.round(Math.sqrt(rand.nextFloat()*rand.nextFloat()*rand.nextFloat()))*(choices.size()-1);
				
//				if (choices.size() == 1){//picks a number from 1 to the number of choices uniformly or skewed
//					branches = 1;
//				}else{
//					branches = rand.nextInt(choices.size()-1)+1; //uniform
//					//branches = (int)Math.round(Math.sqrt(rand.nextFloat()*rand.nextFloat()*rand.nextFloat()))*(choices.size()-2)+1;//skewed
//				}
				
				//branches is the number of possible options that WILL be explored (so usually not all available options are explored)
				while (branches >= 0 && !choices.isEmpty()){ // For each explorable branch || for each remaining branch provided for, we
					
					if (choices.size() == 1){ // Choode a random branch || pick one of the choices randomly
						temp = 0;
					} else {
						temp = Math.round(rand.nextFloat())*(choices.size()-1);
					}
					
					process = choices.remove(temp); // Remove the next Tile on the chosen branch || take it off the choices list
					process.setPrev(cur);//THIS LINE IS USELESS IM GOING TO REMOVE IT SOME TIME
					
					// the next bit of code will block the chance of cycles being discovered if they exist
					//you actually want some cycles, so CYCLECHANCE can be adjusted to change the chance we ignore the potential for a cycle
					
					// Adjusts the number of cycles appearing in the maze
					if (rand.nextFloat() > CYCLECHANCE && getN(process)!= null && getN(process) != cur && getN(process).getType() == FLOOR){
						process.setType(WALL);
					} else if (rand.nextFloat() > CYCLECHANCE && getS(process)!= null && getS(process) != cur && getS(process).getType() == FLOOR){
						process.setType(WALL);
					} else if (rand.nextFloat() > CYCLECHANCE && getW(process)!= null && getW(process) != cur && getW(process).getType() == FLOOR){
						process.setType(WALL);
					} else if (rand.nextFloat() > CYCLECHANCE && getE(process)!= null && getE(process) != cur && getE(process).getType() == FLOOR){
					    process.setType(WALL);
					} else {
						process.setType(FLOOR);		//set it to floor if it passes the cycle test(or the test just gets ignored)
						ends.add(process);//add it to the list of unfinished paths
					}

					branches--;//reduces the number of branches provided for
				}
				
				while (choices.size() > 0){//after all the branches have been accounted for, any remaining choices get turned into walls
					process = choices.remove();
					process.setType(WALL);
				}
				
				if (getN(cur) != null && getN(cur).getType() == FLOOR){//these finish off diagonal walling logic for 2,3 or 4 way forks.
					if(getW(cur) != null &&getW(cur).getType() == FLOOR) getNW(cur).setType(WALL);//needed because these are essentially corners
					if(getE(cur) != null &&getE(cur).getType() == FLOOR) getNE(cur).setType(WALL);
				}
				
				if (getS(cur) != null && getS(cur).getType() == FLOOR){
					if(getW(cur) != null &&getW(cur).getType() == FLOOR) getSW(cur).setType(WALL);
					if(getE(cur) != null &&getE(cur).getType() == FLOOR) getSE(cur).setType(WALL);
				}
			}
		}
//		for(int n = 0;n < size;n++){
//			for(int m = 0;m < size;m++){
//				System.out.print(maze[n][m].getType());
//			}
//			System.out.println("");
//		}
		cur.setType(EXIT);//picks the last visited node as the exit
	}
	
	
	
	/**
	* 
	*/
	private boolean goodMaze(){//makes sure there aren't too many empty squares in the maze
		int count = 0;
		for(int n = 0;n < size;n++){
			for(int m = 0;m < size;m++){
				if(maze[n][m].getType() == EMPTY){
					count++;
				}
			}

		}
		System.out.println(count);
		if(count > 15){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	* 
	*/
	private Tile getN(Tile tile){
		if(tile.getRow() > 0){
			return maze[tile.getRow()-1][tile.getCol()];
		}else{
			return null;
		}
	}
	
	/**
	* 
	*/
	private Tile getS(Tile tile){
		if(tile.getRow() < size-1){
			return maze[tile.getRow()+1][tile.getCol()];
		}else{
			return null;
		}
	}
	
	/**
	* 
	*/
	private Tile getE(Tile tile){
		if(tile.getCol() < size-1){
			return maze[tile.getRow()][tile.getCol()+1];
		}else{
			return null;
		}
	}
	
	/**
	* 
	*/
	private Tile getW(Tile tile){
		if(tile.getCol() > 0){
			return maze[tile.getRow()][tile.getCol()-1];
		}else{
			return null;
		}
	}
	
	/**
	* 
	*/
	private Tile getNW(Tile tile){
		return getW(getN(tile));
	}
	
	/**
	* 
	*/
	private Tile getNE(Tile tile){
		return getE(getN(tile));
	}
	
	/**
	* 
	*/
	private Tile getSW(Tile tile){
		return getW(getS(tile));
	}
	
	/**
	* 
	*/
	private Tile getSE(Tile tile){
		return getE(getS(tile));
	}
	
	/**
	* 
	*/
	public boolean isKeyStatus() {
		return keyStatus;
	}

	/**
	* 
	*/
	public int getSize() {
		return size;
	}

	/**
	* 
	*/
	public Tile getOne(int row, int col){
		return maze[row][col];
	}
	
	//Irfan -renamed function for User.java
	/**
	* 
	*/
	public int tileType(int row, int col){
		return maze[row][col].getType();
	}
	
	
}