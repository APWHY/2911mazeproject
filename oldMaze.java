import java.util.ArrayList;
//use bytes instead of ints?

public class Maze {
	//Tile status definitions
	private static final int EMPTY = 0;
	private static final int WALL  = 1;
	private static final int START = 2;
	private static final int EXIT  = 3;


	private int[][] maze;
	private ArrayList<Sentry> sentries;
	private boolean keyStatus;
	private int[] start;
	private int[] end;
	
	// add timer
	
	private void generateMaze() {
		//TO DO
	}
	// Edit from Andy
	
	
	
	/////////// Irfan's treeToArray function below ///////////////////////////////
	//Issues:
	//- Several functions not handle going outside the bounds of the maze.
	//   e.g. adjacentCells = {maze[x][y-1], maze[x][y+1]... etc.
	
	//Requests:
	//- *Edge* cells MUST have their walls set before passing into treeToArray
	//- Other walls must be set to false; this is done by default upon initialisation, I think.
	
	private static final int UP    = 0;
	private static final int DOWN  = 1;
	private static final int LEFT  = 2;
	private static final int RIGHT = 3;
	private static final int NUM_SIDES = 4;
	
	private final int X = 0;
	private final int Y = 1;
	
	private void treeToArray(Tree tree, Cell[][] maze, int[] start, int[] previous, int numUnvisitedCells){
		if(numUnvisitedCells <= 0)
			return;
		
		int x = start[X];
		int y = start[Y];
		Cell currentCell = maze[x][y];
		addWalls(currentCell, x, y, maze);

		//int numWalls = numWalls(adjacentWalls);
		
		ArrayList<int[]> possibleMoves = possibleMoves(currentCell, x, y, previous);
		Collections.shuffle(possibleMoves); //visit a *random* adjacent cell.
		
		int numBranches = tree.children.length;
		int i;
		for(i = 0; i < numBranches && i < possibleMoves.size(); i++){
			treeToArray(tree.children[i], maze, possibleMoves.get(i), start, numUnvisitedCells);
			//                                  ^next cell            ^current cell
			numUnvisitedCells--;
		}
		
		for(; i < NUM_SIDES - 1; i++){
			//Make wall between current and unvisited adjacent cell.
			int unvisited = direction(x, y, possibleMoves.get(i));
			currentCell.walls[unvisited] = true;
		}
		
		return;
	}

	private ArrayList<int[]> possibleMoves(Cell currentCell, int x, int y, int[] previous) {
		ArrayList<int[]> adjacentCells = new ArrayList<int[]>();
		adjacentCells.add(new int[] {x, y-1}); //UP    //Order is important,
		adjacentCells.add(new int[] {x, y+1}); //DOWN  //since indexes correspond
		adjacentCells.add(new int[] {x-1, y}); //LEFT  //to direction.
		adjacentCells.add(new int[] {x+1, y}); //RIGHT
		
		//Remove cells blocked by wall.
		for(int direction = 0; direction < NUM_SIDES; direction++){
			if(currentCell.walls[direction] == true)
				adjacentCells.remove(direction); //direction used as index.
				currentCell.walls[direction] = true; //set wall.
		}
		
		//Remove the 'came from' direction.
		adjacentCells.remove(new int[] {previous[X], previous[Y]});

		return adjacentCells;
	}

	private int direction(int fromX, int fromY, int[] to) {
		if(to[X] == fromX && to[Y] == fromY - 1)
			return UP;
		else if(to[X] == fromX && to[Y] == fromY + 1)
			return DOWN;
		else if(to[X] == fromX + 1 && to[Y] == fromY)
			return RIGHT;
		else if(to[X] == fromX - 1 && to[Y] == fromY)
			return LEFT;
		else{
			System.err.println("Invalid start or previous in treeToArray");
			return 99;
		}
	}

	//TO DO: Handle array out of bounds!!!
	private void addWalls(Cell currentCell, int x, int y, Cell[][] maze) {
		Cell[] adjacentCells = {maze[x][y-1], maze[x][y+1], maze[x-1][y], maze[x+1][y]};
		//                             ^UP           ^DOWN      ^LEFT         ^RIGHT
		currentCell.walls[UP]    = adjacentCells[UP].walls[DOWN];
		currentCell.walls[DOWN]  = adjacentCells[DOWN].walls[UP];
		currentCell.walls[LEFT]  = adjacentCells[LEFT].walls[RIGHT];
		currentCell.walls[RIGHT] = adjacentCells[RIGHT].walls[LEFT];
	}
	
}
