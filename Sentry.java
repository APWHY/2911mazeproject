//use bytes instead of ints?

public class Sentry {

	private int[] currentPos; //int[0] is x-coordinate, int[1] is y-coordinate
	private int visionCounter; // used to calculate the vision co-ordinates
	
	
	private int degree; // ???
	
	private int[][] rangeOfPos; //list of coordinates where sentry can go, in adjacent order.
    //the sentry doesn't have to move, it just turns on the spot
	private int counter;
	

	
	//Implement above functions...
	//Sentry constructor, getRow, getColumn by Andy
	public Sentry(int row, int column) {
		this.currentPos = new int[2];
		this.currentPos[0] = row;
		this.currentPos[1] = column;
		this.visionCounter = 0;
        this.degree = 0; //will eventually be a random number
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
	
	
	/**
	* Updates the tile that the sentry is observing 
	* Scans the adjacent tiles in a clockwise direction 
	* regardless of tile status
	* 
	* @return result int[2] containg the co-ordinates of the scanned tile
	**/
	public int[] updateVision(){
		
		int[2] result;
		
		// can add tile status check here
		// check: if (wall) then skip position until not wall
		
		switch(this.visionCounter){
		
			case(0): 
				result[0] = this.currentPos[0] --;
				result[1] = this.currentPos[1] ++;
				this.visionCounter ++;
				return result;
			case(1): 
				result[0] = this.currentPos[0] --;
				result[1] = this.currentPos[1];
				this.visionCounter ++;
				return result;
			case(2): 
				result[0] = this.currentPos[0] --;
				result[1] = this.currentPos[1] ++;
				this.visionCounter ++;
				return result;
			case(3): 
				result[0] = this.currentPos[0];
				result[1] = this.currentPos[1] ++;
				this.visionCounter ++;
				return result;
			case(4): 
				result[0] = this.currentPos[0] ++;
				result[1] = this.currentPos[1] ++;
				this.visionCounter ++;
				return result;
			case(5): 
				result[0] = this.currentPos[0] ++;
				result[1] = this.currentPos[1];
				this.visionCounter ++;
				return result;
			case(6): 
				result[0] = this.currentPos[0] ++;
				result[1] = this.currentPos[1] --;
				this.visionCounter ++;
				return result;
			case(7): 
				result[0] = this.currentPos[0];
				result[1] = this.currentPos[1] --;
				this.visionCounter = 0;
				return result;
			default:
				return this.currentPos;
		
		}
					
		}
	
	}
	
	
}
