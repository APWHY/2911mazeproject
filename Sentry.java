//use bytes instead of ints?

public class Sentry {
	private int[] currentPos; //int[0] is x-coordinate, int[1] is y-coordinate
	private int[] currentVision;
	
	private int[][] rangeOfPos; //list of coordinates where sentry can go, in adjacent order.
	private int counter;
	
	private void makeMove(){
		counter++;
		currentPos = {/*x*/rangeOfPos[counter], /*y*/rangeOfPos[counter][counter]};
		updateVision(currentVision);
	}
	
	//Implement above functions...
	//Sentry constructor, getRow, getColumn by Andy
	public Sentry(int row, int column) {
		this.int[0] = row;
		this.int[1] = column;
	}
	
	public int getRow() {
		return this.int[0];
	}
	
	public int getColumn() {
		return this.int[1];
	}
	
}
