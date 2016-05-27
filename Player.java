import java.util.ArrayList;


/**
 * This class handles all player information AND interactions with the game (maze, sentries etc.), the most important of which are the collision checks
 *
 */
public class Player {

	//class constants
		private static final int REALLYBIGNUMBER = 36000; //it's a really big number....that happens to be divisible by 360
		private final int SPEED = 3; //this holds the maximum speed the player can travel at.
	
		//Tile status definitions -- ones commented out are simply not used in the code
			//private static final int EMPTY = 0;
			//private static final int FLOOR = 1;
			private static final int WALL  = 2;
			private static final int START = 3;
			private static final int EXIT  = 4;
			//private static final int SENTRY  = 5;
			private static final int KEY = 6;
	
	
	//class variables
		private int userRad; //collision is done assuming the player is a circle. It's not 100% perfect but it does a pretty good job
							 //This is mainly because the player used to be a circle and the collision checks were pixel perfect, but now we use a sprite
		private boolean caught;//by the sentry, see
		
		//positional variables
			private int xPos;	//holds the rounded x position of the player for renderer to use
			private int yPos;
			private float xFos; //holds the true x position of the player
			private float yFos;
			private float a; //actually pseudoconstant -- doesn't change after constructor chooses it.
			private float fx; //holds x-component of speed
			private float fy;
		
		//collision variables -- used to help with collision checks
			private int tileWidth;
			private int tileHeight;
			private int arcDist;
			private int arcWidth;


	/**
	 * Constructor. Doesn't do anything but call reset()
	 * 
	 * @param maze the maze
	 * @param tileWidth the width of a tile
	 * @param tileHeight the height of a tile
	 * @param arcDist the radius of the arcs that the sentries emit
	 * @param arcWidth the angle of the arcs that the sentries emit
	 */
	public Player(Maze maze, int tileWidth, int tileHeight, int arcDist, int arcWidth){
		reset(maze,tileWidth,tileHeight,arcDist,arcWidth);

	}
	
	/**
	 * Sends player back to the Start tile. This is called by the constructor or by the player when the player makes contact with a Sentry's vision.
	 * 
	 * @param maze the maze
	 * @param tileWidth the width of a tile
	 * @param tileHeight the height of a tile
	 * @param arcDist the radius of the arcs that the sentries emit
	 * @param arcWidth the angle of the arcs that the sentries emit
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
		fx = 0;
		fy = 0;
		this.caught = false;
	}
	
	/**
	 * Calculates the player's position relative to 0 every tick, the top left most corner. Note that the positions here are relative to the maze, not to the outside JFrame
	 * 
	 * @param vert Vertical distance from 0 
	 * @param horz Horizontal distance from 0
	 * @param maze Game maze
	 * @return Updated maze
	 */
	public Maze move(int vert, int horz, Maze maze){
		//friction calcs
		if(Math.abs(.2*(fy)/Math.abs(fy))> Math.abs(fy) && vert == 0) fy = 0;
		if(Math.abs(.2*(fx)/Math.abs(fx))> Math.abs(fx) && horz == 0) fx = 0;
		if (fy != 0) fy = (float) (fy - .2*(fy)/Math.abs(fy));	
		if (fx != 0) fx = (float) (fx - .2*(fx)/Math.abs(fx));
		
		//vector calcs
		if(vert == 1){//up pressed
			fy = fy - a;
		}
		
		if(vert == -1){//down pressed
			fy = fy + a;

		}
		
		if(horz == 1){//left pressed
			fx = fx - a;

		}
		
		if(horz == -1){//right pressed
			fx = fx + a;

		}
		//maxspeed calcs
		if(Math.abs(fy) > SPEED){
			fy = (Math.abs(fy)/fy)*SPEED;
		}
		if(Math.abs(fx) > SPEED){
			fx = (Math.abs(fx)/fx)*SPEED;
		}
		//collision checks with maze
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
		this.checkLasers(maze); //collision checks with sentries
		
		if(caught){
			reset(maze,tileWidth,tileHeight,arcDist,arcWidth);
		}
		
		return maze;
	}
	


	/**
	 * Checks if the player is within a Sentry's line of sight. This check is no longer pixel perfect because the player isn't a circle, but a rectangular sprite. Still, it's close enough for what we need.
	 * 
	 * @param maze the maze
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
				//playerArc is the angle of the arc where the chord of the arc is the player diameter. No checking of tan's boundary conditions because we know this number to be between 0 and <180
				playerArc = (float) Math.toDegrees(Math.atan((userRad/2)/(Math.sqrt(Math.pow(sentry.getRow()*tileWidth+tileWidth/2-yPos,2) + Math.pow(sentry.getColumn()*tileHeight+tileHeight/2-xPos,2)))));
				//playerSent is the angle from the sentry to the player
				playerSent = 0;
				sentLocY = sentry.getRow()*tileWidth+tileWidth/2;
				sentLocX = sentry.getColumn()*tileHeight+tileHeight/2;
				
				if(xPos-sentLocX == 0){//messing about with atan having limits and whatnot
					if (yPos - sentLocY < 0){
						playerSent = 90;
					} else {
						playerSent = -90;
					}
				} else {
					playerSent = (float)Math.toDegrees(Math.atan((float)(yPos-sentLocY)/(sentLocX-xPos)));
				}

				if (xPos-sentLocX < 0){//works around atan only doing -90 to 90
					playerSent += 180;
				}
				
				if (playerSent < 0){
					playerSent += 360;
				}
				angleLow = (REALLYBIGNUMBER + sentry.getDegree()-playerArc) % 360;//the actual value of REALLYBIGNUMBER doesn't matter as long as it's really big and divisible by 360
				angleHigh = (REALLYBIGNUMBER + sentry.getDegree() + playerArc + arcWidth) % 360;
				if(angleLow < angleHigh){//found this very neat way to compare angles online --tom
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
	 * Check if the player has obtained the key. Calls keyOff (Maze class) if the key has been found
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
	 * Checks if the player has reached the exit. Calls activateShrine(in Maze) if the exit has been found.
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
	 * Checks if coordinates will cause the player to overlap with a wall
	 * 
	 * @param maze Game maze
	 * @param x Player co-ordinates
	 * @param y Player co-ordinates
	 * @return Boolean true if the player hasn't collided with a tile, false otherwise.
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
	 * Get the player's x coordinate.
	 * 
	 * @return int the player's x coordinate
	 */
	public int getxPos() {
		return xPos;
	}

	/**
	 * Get the player's y coordinate.
	 * 
	 * @return int the player's y coordinate.
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
	 * 4 (south) is default.
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
		return 4;//8;//just in case
	}
	
	public boolean isCaught() {
		return caught;
	}
}