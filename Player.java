import java.util.ArrayList;
import javax.swing.JFrame;

public class Player {

	// Constants
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	private static final int SENTRY  = 5;	
	private static final int KEY = 6;
	
	private static final int REALLYBIGNUMBER = 36000;
	private final int SPEED = 1;
	
	private int xPos;
	private int yPos;
	
	private int tileWidth;
	private int tileHeight;
	private int arcDist;
	private int arcWidth;

	private int userRad; //Tom -- using radius instead since it's a circle
	public int caught; // just for testing get rid of this - Could use to trigger game over?

	/**
	 * Constructor.
	 * 
	 * @param maze
	 * @param tileWidth
	 * @param tileHeight
	 * @param arcDist
	 * @param arcWidth
	 */
	public Player(Maze maze, int tileWidth, int tileHeight, int arcDist, int arcWidth){

		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.arcDist = arcDist;
		this.arcWidth = arcWidth;
		this.userRad = tileWidth/4;
		this.xPos = maze.getTileType(START).getCol() * tileWidth  + userRad*2;
		this.yPos = maze.getTileType(START).getRow() * tileHeight + userRad*2;
		
		this.caught = 25;
	}
	
	/**
	 * 
	 * 
	 * @param vert
	 * @param horz
	 * @param maze
	 * @return
	 */
	public Maze move(int vert, int horz, Maze maze){
		
		if(vert == 1){
			this.up(maze);
		}
		
		if(vert == -1){
			this.down(maze);
		}
		
		if(horz == 1){
			this.left(maze);
		}
		
		if(horz == -1){
			this.right(maze);
		}
		if(!maze.isKeyStatus()){
			this.checkKey(maze, xPos, yPos);
		}else{
			this.checkExit(maze, xPos, yPos);
		}
		this.checkLasers(maze);
		return maze;
	}
	
	/**
	 * 
	 * 
	 * @param maze
	 */
	private void checkLasers(Maze maze){
		
		ArrayList<Sentry> sentries = maze.getSentries();
		
		float playerArc = 0;
		float playerSent = 0;
		int sentLocY = 0;
		int sentLocX = 0;
		float angleLow = 0;
		float angleHigh = 0;
		
		this.caught = 25;
		
		for(Sentry sentry: sentries){
			//first check that the user is even within range of the sentry
			if(Math.sqrt(Math.pow(sentry.getRow()*tileWidth+tileWidth/2-yPos,2) + Math.pow(sentry.getColumn()*tileHeight+tileHeight/2-xPos,2)) < arcDist+userRad){
				//then see if the user is in the beam or not
				//playerArc is the angle of the arc where the chord of the arc is the player diamater. No checking of tan's boundary conditions because we know this number to be between 0 and <180
				playerArc = (float) Math.toDegrees(Math.atan((userRad/2)/(Math.sqrt(Math.pow(sentry.getRow()*tileWidth+tileWidth/2-yPos,2) + Math.pow(sentry.getColumn()*tileHeight+tileHeight/2-xPos,2)))));
				//playerSent is the angle from the sentry to the player
				playerSent = 0;
				sentLocY = sentry.getRow()*tileWidth+tileWidth/2;
				sentLocX = sentry.getColumn()*tileHeight+tileHeight/2;
				
				if(xPos-sentLocX == 0){
					if (yPos - sentLocY < 0){
						playerSent = 90;
					} else {
						playerSent = -90;
					}
				} else {
					playerSent = (float)Math.toDegrees(Math.atan((float)(yPos-sentLocY)/(sentLocX-xPos)));
				}

				if (xPos-sentLocX < 0){//works around tan only doing -90 to 90
					playerSent += 180;
				}
				
				if (playerSent < 0){
					playerSent += 360;
				}
				angleLow = (REALLYBIGNUMBER + sentry.getDegree()-playerArc) % 360;
				angleHigh = (REALLYBIGNUMBER + sentry.getDegree() + playerArc + arcWidth) % 360;
				if(angleLow < angleHigh){
					if (angleLow < playerSent && angleHigh > playerSent){
						caught = 150;
					}
				}else{
					if(angleLow < playerSent || angleHigh > playerSent){
						caught = 255;
					}
				}
			
			}
		}
	}
	
	public void up(Maze maze) {
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++)
			yPos -= 1;
		if(tileIsWall(maze, xPos, yPos))
			yPos++;

	}

	public void down(Maze maze) {
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++)
			yPos += 1;
		if(tileIsWall(maze, xPos, yPos))
			yPos--;

	}

	public void right(Maze maze) {
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++)
			xPos += 1;
		if(tileIsWall(maze, xPos, yPos))
			xPos--;

	}

	public void left(Maze maze) {
		
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++) 
			xPos -= 1;
		
		if(tileIsWall(maze, xPos, yPos)) 
			xPos++;

	}
	private void checkKey(Maze maze, int x, int y){
		int leftEdge = (x - userRad)/tileWidth;  //Will round down
		int topEdge = (y- userRad)/tileHeight; //if not perfectly divisible. :)
		int rightEdge = (x + userRad)/tileWidth ;
		int bottomEdge = (y + userRad)/tileHeight ; 
		
		if(maze.tileType(topEdge, rightEdge) == KEY
				|| maze.tileType(topEdge, leftEdge)     == KEY
				|| maze.tileType(bottomEdge, rightEdge) == KEY
				|| maze.tileType(bottomEdge, leftEdge)  == KEY) 
			maze.keyOff();//this is pretty ugly but it's the easiest way to do it
	}
	private void checkExit(Maze maze, int x, int y){
		int leftEdge = (x - userRad)/tileWidth;  //Will round down
		int topEdge = (y- userRad)/tileHeight; //if not perfectly divisible. :)
		int rightEdge = (x + userRad)/tileWidth ;
		int bottomEdge = (y + userRad)/tileHeight ; 
		
		if(maze.tileType(topEdge, rightEdge) == EXIT
				|| maze.tileType(topEdge, leftEdge)     == EXIT
				|| maze.tileType(bottomEdge, rightEdge) == EXIT
				|| maze.tileType(bottomEdge, leftEdge)  == EXIT) 
			maze.activateShrine();//and the winner for cheesiest function name goes to....
	}
	
	private boolean tileIsWall(Maze maze, int x, int y){
		//checks that the player doesn't leave the maze
		if(x < userRad || y < userRad || x >= (maze.getSize())*tileWidth-userRad || y >= (maze.getSize())*tileHeight-userRad){
			return true;
		}
		int leftEdge = (x - userRad)/tileWidth;  //Will round down
		int topEdge = (y- userRad)/tileHeight; //if not perfectly divisible. :)
		int rightEdge = (x + userRad)/tileWidth ;
		int bottomEdge = (y + userRad)/tileHeight ; 
	
		if(maze.tileType(topEdge, rightEdge) != WALL
				&& maze.tileType(topEdge, leftEdge)     != WALL
				&& maze.tileType(bottomEdge, rightEdge) != WALL
				&& maze.tileType(bottomEdge, leftEdge)  != WALL) 
			return false;
		
		return true;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public int getUserRad(){
		return userRad;
	}


}