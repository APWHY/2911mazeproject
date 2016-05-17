//use bytes instead of ints?
package mazeGUI;

import java.util.Random;

public class Sentry {
	private int row; 
	private int col;
	private int degree;
	
	
	private final int ROTATIONSPEED = 120;
	
	//Implement above functions...
	//Sentry constructor, getRow, getColumn by Andy
	public Sentry(int row, int column) {
		this.row = row;
		this.col = column;
        Random rand = new Random();
        degree = rand.nextInt(ROTATIONSPEED);//will eventually be a random number

    }

    public int updateDegree(){
        degree++;
        if (degree > ROTATIONSPEED){
        	degree = 0;
        }
        return degree;
    }

	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return col;
	}
	
}
