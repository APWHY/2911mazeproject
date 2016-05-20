import java.util.Random;

public class Sentry {
	
	private int row; 
	private int col;
	private int degree;
	private int rotSpeed;
	
	//Sentry constructor, getRow, getColumn by Andy
	public Sentry(int column, int row) {
		
		this.row = row;
		this.col = column;
        Random rand = new Random();
        this.rotSpeed = rand.nextInt(3)-2; // Picks a random speed
        
        if (this.rotSpeed >= 0) rotSpeed++;
        
        this.degree = rand.nextInt(360); 

    }

    public int updateDegree(){
        
    	this.degree = this.degree + this.rotSpeed;
    	if (this.degree > 359) this.degree = 0;
    	
        return this.degree;
    }

	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.col;
	}
	public int getDegree() {
		return degree;
	}
}
