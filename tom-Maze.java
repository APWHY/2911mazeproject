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
	
	private static final float CYCLECHANCE = (float) 0.75;

	private Tile[][] maze;
//	private ArrayList<Sentry> sentries;
	private boolean keyStatus;
	private int size;

	public Maze(int size){
		this.size = size;
		maze = new Tile[size][size];
		for (int n = 0; n < size; n++){
			for (int m = 0; m < size; m++){
				maze[n][m] = new Tile(n,m);
			}
		}
		generateMaze(size);
		keyStatus = false;
	}
	
	// add timer
	
	private void generateMaze(int size) {


		Random rand = new Random();
		Tile start = new Tile(rand.nextInt(size),rand.nextInt(size));
		start.setType(START);
		maze[start.getRow()][start.getCol()] = start;
		int branches = 0;
		int temp = 0;
		LinkedList<Tile> ends = new LinkedList<Tile>();
		LinkedList<Tile> choices = new LinkedList<Tile>();
		ends.add(start);
		Tile cur,process;
		while(!ends.isEmpty()){
			cur = ends.remove();
			if(getN(cur)!= null){
				if(getN(cur).type == EMPTY) choices.add(getN(cur));
			}
			if(getS(cur)!= null){
				if(getS(cur).type == EMPTY) choices.add(getS(cur));
			}
			if(getE(cur)!= null){
				if(getE(cur).type == EMPTY) choices.add(getE(cur));
			}
			if(getW(cur)!= null){
				if(getW(cur).type == EMPTY) choices.add(getW(cur));
			}

			if(!choices.isEmpty()){			
	//there are two options here. The long line means that sometimes the maze will not extend an end even if the option exists
	//the large commented out section means that if an option exists at least one will be taken. It provides more complete mazes but 
	//the mazes don't look as nice and become bloody hard
				branches = (int)Math.round(Math.sqrt(rand.nextFloat()*rand.nextFloat()*rand.nextFloat()))*(choices.size()-1);

				
//				if (choices.size() == 1){
//					branches = 1;
//				}else{
//					branches = rand.nextInt(choices.size()-1)+1;
//				}
				while (branches >= 0 && !choices.isEmpty()){
					if (choices.size() == 1){
						temp = 0;
					}else{
						temp = Math.round(rand.nextFloat())*(choices.size()-1);
					}
					process = choices.remove(temp);
					process.setPrev(cur);
					
					//the next bit of code will block the chance of cycles being discovered if they exist
					//you actually want some cycles, so CYCLECHANCE can be adjusted to change the chance we ignore the potential for a cycle
					if (rand.nextFloat() > CYCLECHANCE && getN(process)!= null && getN(process) != cur && getN(process).getType() == FLOOR)process.setType(WALL);
					else if (rand.nextFloat() > CYCLECHANCE && getS(process)!= null && getS(process) != cur && getS(process).getType() == FLOOR)process.setType(WALL);
					else if (rand.nextFloat() > CYCLECHANCE && getW(process)!= null && getW(process) != cur && getW(process).getType() == FLOOR)process.setType(WALL);
					else if (rand.nextFloat() > CYCLECHANCE && getE(process)!= null && getE(process) != cur && getE(process).getType() == FLOOR)process.setType(WALL);
					else{
						process.setType(FLOOR);		
						ends.add(process);
					}

					branches--;
				}
				while (choices.size() > 0){
					process = choices.remove();
					process.setType(WALL);
				}
				if (getN(cur) != null && getN(cur).getType() == FLOOR){//these finish off diagonal walling logic for 2,3 or 4 way forks.
					if(getW(cur) != null &&getW(cur).getType() == FLOOR) getNW(cur).setType(WALL);
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
	}
	
	private Tile getN(Tile tile){
		if(tile.getRow() > 0){
			return maze[tile.getRow()-1][tile.getCol()];
		}else{
			return null;
		}
	}
	private Tile getS(Tile tile){
		if(tile.getRow() < size-1){
			return maze[tile.getRow()+1][tile.getCol()];
		}else{
			return null;
		}
	}
	private Tile getE(Tile tile){
		if(tile.getCol() < size-1){
			return maze[tile.getRow()][tile.getCol()+1];
		}else{
			return null;
		}
	}
	private Tile getW(Tile tile){
		if(tile.getCol() > 0){
			return maze[tile.getRow()][tile.getCol()-1];
		}else{
			return null;
		}
	}
	private Tile getNW(Tile tile){
		return getW(getN(tile));
	}
	private Tile getNE(Tile tile){
		return getE(getN(tile));
	}
	private Tile getSW(Tile tile){
		return getW(getS(tile));
	}
	private Tile getSE(Tile tile){
		return getE(getS(tile));
	}
	public boolean isKeyStatus() {
		return keyStatus;
	}

	public int getSize() {
		return size;
	}

	public Tile getOne(int row, int col){
		return maze[row][col];
	}
	
	
}
