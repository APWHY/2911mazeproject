//use bytes instead of ints?

public class User {
    private int[] currentPos; //int[0] is x-coordinate, int[1] is y-coordinate
	//private boolean hasKey; //This info stored in maze only for now.
	
	private void makeMove(){
		char direction = getUserInput() //'u', 'd', 'l', 'r' (up, down, left, right).
		newPos = new int {newX(direction), newY(direction)};
				
		if (isValid(newPos)){ //false if a wall, true otherwise
			currentPos = newPos;
		}
		else{
			//do nothing
		}
	}
	
	//Implement above functions...
	
}