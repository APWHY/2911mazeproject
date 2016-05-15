//use bytes instead of ints?

public class Sentry {
	private int[] currentPos; //int[0] is x-coordinate, int[1] is y-coordinate
	private int degree;
	
	private int[][] rangeOfPos; //list of coordinates where sentry can go, in adjacent order.
    //the sentry doesn't have to move, it just turns around
	private int counter;
	

	
	//Implement above functions...
	//Sentry constructor, getRow, getColumn by Andy
	public Sentry(int row, int column) {
		this.int[0] = row;
		this.int[1] = column;
        degree = 0;//will eventually be a random number
    }

    private void updateDegree(){
        degree++;
        return degree;
    }

    // private void makeMove(){
    //     counter++;
    //     currentPos = {/*x*/rangeOfPos[counter], /*y*/rangeOfPos[counter][counter]};
    //     updateVision(currentVision);
    // }
	
	public int getRow() {
		return this.int[0];
	}
	
	public int getColumn() {
		return this.int[1];
	}
	
}
