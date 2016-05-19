//use bytes instead of ints?
package mazeGUI;

import java.util.Random;

public class Sentry {

	private int row; 
	private int col;
	private int degree;
	private int rotSpeed;
	
	
	//Implement above functions...
	//Sentry constructor, getRow, getColumn by Andy
	public Sentry(int column, int row) {
		this.row = row;
		this.col = column;
        Random rand = new Random();
        rotSpeed = rand.nextInt(3)-2; //picks a random speed
        if (rotSpeed >= 0)rotSpeed++;
        degree = rand.nextInt(360);//will eventually be a random number

    }

    public int updateDegree(){
        degree = degree + rotSpeed;
        if (degree > 359){
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
	public int getDegree() {
		return degree;
	}
}
