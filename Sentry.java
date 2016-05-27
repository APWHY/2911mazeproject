import java.util.Random;

public class Sentry {
	
	private int row; 
	private int col;
	private int degree;
	private int rotSpeed;
	
	/**
	 * Constructor.
	 * 
	 * @param column Column index of Sentry
	 * @param row Row index of Sentry
	 */
	public Sentry(int column, int row) {
		
		this.row = row;
		this.col = column;
        
		Random rand = new Random();
        this.rotSpeed = rand.nextInt(3)-2; // Picks a random rotation speed
        
        if (this.rotSpeed >= 0) rotSpeed++;
        
        this.degree = rand.nextInt(360); 
    }

	/**
	 * Update the rotation of the Sentry.
	 * 
	 * @return New sentry position
	 */
    public int updateDegree(){
        
    	this.degree = this.degree + this.rotSpeed;
    	if (this.degree > 359) this.degree = 0;
    	
        return this.degree;
    }

	/**
	 * Get column index of Sentry.
	 * 
	 * @return Column index
	 */
	public int getColumn() {
		return this.col;
	}

	/**
	 * Get row index of Sentry.
	 * 
	 * @return Row index
	 */
	public int getRow() {
		return this.row;
	}
	
	/**
	 * Get degree of Sentry rotation.
	 * 
	 * @return Degree of Sentry rotation
	 */
	public int getDegree() {
		return this.degree;
	}
}
