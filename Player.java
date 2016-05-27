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
	private final int SPEED = 3; //this is actually maxspeed
	
	private int xPos;
	private int yPos;
	private float xFos;
	private float yFos;
	
	private int tileWidth;
	private int tileHeight;
	private int arcDist;
	private int arcWidth;
	
	private float a;
	private int sx;
	private int sy;
	private float fx;
	private float fy;

	private int userRad; //Tom -- using radius instead since it's a circle
	private boolean caught;

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
		reset(maze,tileWidth,tileHeight,arcDist,arcWidth);

	}
	
	/**
	 * Sends player back to the Start tile. This is called when
	 * the player makes contact with a Sentry's vision.
	 * 
	 * @param maze Game maze
	 * @param tileWidth tile width for drawing
	 * @param tileHeight tile height for drawing
	 * @param arcDist 
	 * @param arcWidth
	 */
	private void reset(Maze maze, int tileWidth, int tileHeight, int arcDist, int arcWidth){
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.arcDist = arcDist;
		this.arcWidth = arcWidth;
		this.userRad = tileWidth/4;
		this.xPos = maze.getTileType(START).getCol() * tileWidth  + userRad*2;
		this.yPos = maze.getTileType(START).getRow() * tileHeight + userRad*2;
		this.xFos = xPos;
		yFos = yPos;
		a = (float)0.4;
		sx = 0;
		sy = 0;
		fx = 0;
		fy = 0;
		this.caught = false;
	}
	
	/**
	 * Calculates the player's position relative to 0, 
	 * the top left most corner. 
	 * 
	 * @param vert Vertical distance from 0 
	 * @param horz Horizontal distance from 0
	 * @param maze Game maze
	 * @return Updated maze
	 */
	public Maze move(int vert, int horz, Maze maze){
		//friction
		if(Math.abs(.2*(fy)/Math.abs(fy))> Math.abs(fy) && vert == 0) fy = 0;
		if(Math.abs(.2*(fx)/Math.abs(fx))> Math.abs(fx) && horz == 0) fx = 0;
		if (fy != 0) fy = (float) (fy - .2*(fy)/Math.abs(fy));	
		if (fx != 0) fx = (float) (fx - .2*(fx)/Math.abs(fx));
		
		//vector calcs
		if(vert == 1){
			fy = fy - a;
		//	this.up(maze);
//			System.out.println("up" );
		}
		
		if(vert == -1){
			fy = fy + a;
		//	this.down(maze);
//			System.out.println("down" );
		}
		
		if(horz == 1){
			fx = fx - a;
		//	this.left(maze);
//			System.out.println("left" );
		}
		
		if(horz == -1){
			fx = fx + a;
		//	this.right(maze);
//			System.out.println("right" );
		}
		//maxspeed calcs
		if(Math.abs(fy) > SPEED){
			fy = (Math.abs(fy)/fy)*SPEED;
		}
		if(Math.abs(fx) > SPEED){
			fx = (Math.abs(fx)/fx)*SPEED;
		}
		//collision checks
		for(int i = 0; i < Math.abs(fy) && !tileIsWall(maze, Math.round(xFos), Math.round(yFos)); i++)
			yFos =  (yFos + (fy)/Math.abs(fy));
		if(tileIsWall(maze, Math.round(xFos), Math.round(yFos))){
			yFos = (yFos - (fy)/Math.abs(fy));
			fy = (float) (-0.8*fy);//bounce check
		}
		for(int i = 0; i < Math.abs(fx) && !tileIsWall(maze, Math.round(xFos), Math.round(yFos)); i++)
			xFos = (xFos + (fx)/Math.abs(fx));

		if(tileIsWall(maze, Math.round(xFos), Math.round(yFos))){
			xFos =  (xFos - (fx)/Math.abs(fx));
			fx = (float) (-0.8*fx);
		}
		xPos = Math.round(xFos);
		yPos = Math.round(yFos);
		
		if(!maze.isKeyStatus()){
			this.checkKey(maze, xPos, yPos);
		}else{
			this.checkExit(maze, xPos, yPos);
		}
		this.checkLasers(maze);
		
		if(caught){
			reset(maze,tileWidth,tileHeight,arcDist,arcWidth);
		}
		
		return maze;
	}
	
	public boolean isCaught() {
		return caught;
	}

	/**
	 * Checks if the player is within a Sentry's line of sight.
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
		
		caught = false;
		
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
						caught = true;
					}
				}else{
					if(angleLow < playerSent || angleHigh > playerSent){
						caught = true;
					}
				}
			
			}
		}
	}
	
	/**
	 * Moves the player up.
	 * 
	 * @param maze Game maze
	 */
	public void up(Maze maze) {
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++)
			yPos -= 1;
		if(tileIsWall(maze, xPos, yPos)){
			yPos++;
		}
	}

	/**
	 * Moves the player down.
	 * 
	 * @param maze Game maze
	 */
	public void down(Maze maze) {
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++)
			yPos += 1;
		if(tileIsWall(maze, xPos, yPos))
			yPos--;

	}

	/**
	 * Moves the player right.
	 * 
	 * @param maze Game maze
	 */
	public void right(Maze maze) {
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++)
			xPos += 1;
		if(tileIsWall(maze, xPos, yPos))
			xPos--;

	}

	/**
	 * Moves the player left.
	 * 
	 * @param maze Game maze
	 */
	public void left(Maze maze) {
		
		for(int i = 0; i < SPEED && !tileIsWall(maze, xPos, yPos); i++) 
			xPos -= 1;
		
		if(tileIsWall(maze, xPos, yPos)) 
			xPos++;

	}
	
	/**
	 * check if the player has obtained the key.
	 * 
	 * @param maze Game maze
	 * @param x Player Co-ordinates
	 * @param y Player Co-ordinates
	 */
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
	
	/**
	 * Checks if the player has reached the exit.
	 * 
	 * @param maze Game maze
	 * @param x Player Co-ordinates
	 * @param y Player Co-ordinates
	 */
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
	
	/**
	 * Checks if the Tile is a wall.
	 * 
	 * @param maze Game maze
	 * @param x Player co-ordinates
	 * @param y Player co-ordinates
	 * @return Boolean result
	 */
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

	/**
	 * Get the player's x co-ordinate.
	 * 
	 * @return int
	 */
	public int getxPos() {
		return xPos;
	}

	/**
	 * Get the player's y coordinate.
	 * 
	 * @return int
	 */
	public int getyPos() {
		return yPos;
	}

	/**
	 * Get the player's radius.
	 * 
	 * @return int
	 */
	public int getUserRad(){
		return userRad;
	}

	/**
	 * Get the direction that the player is facing. 
	 * 
	 * @return One of 8 directions,
	 * starting from north (== 0),
	 * going clockwise.
	 * 0 indicates stationary.
	 */
	public int getDirection(){
		if(fx == 0){
			if(fy == 0)
				return 0;//8; //stationary
			else if(fy > 0)
				return 4; //south
			else if (fy < 0)
				return 0; //north
		}
		if(fx > 0){
			if(fy == 0)
				return 2; //east
			else if(fy > 0)
				return 3; //se
			else if (fy < 0)
				return 1; //ne
		}
		if(fx < 0){
			if(fy == 0)
				return 6; //west
			else if(fy > 0)
				return 5; //sw
			else if (fy < 0)
				return 7; //nw
		}
		return 0;//8;//just in case
	}
}