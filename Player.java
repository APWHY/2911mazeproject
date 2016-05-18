package mazeGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Player{
	private final int SPEED = 1;
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	private static final int SENTRY  = 5;	
	private static final int REALLYBIGNUMBER = 36000;
	private int xPos;
	private int yPos;
	
	//private Graphics g;
	private Maze maze;
	private int tileWidth;
	private int tileHeight;
	private int arcDist;
	private int arcWidth;

	private int userRad; //Tom -- using radius instead since it's a circle
//	private int userWidth;
//	private int userHeight;
	public int caught;//just for testing get rid of this
	JFrame test = new JFrame("Irfan's user");

	public Player(/*Graphics g,*/ Maze maze, int tileWidth, int tileHeight, int arcDist, int arcWidth){
		this.maze = maze;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.arcDist = arcDist;
		this.arcWidth = arcWidth;
		this.userRad = tileWidth/4;
		this.xPos = maze.getOneType(START).getCol() * tileWidth  + userRad*2;
		this.yPos = maze.getOneType(START).getRow() * tileHeight + userRad*2;
		
		this.caught = 25;

	}
	
	public void drawPlayer(Graphics g) {

	}
	public Maze move(int vert, int horz, Maze maze){
		this.maze = maze;
		if(vert == 1){
			up();
		}
		if(vert == -1){
			down();
		}
		if(horz == 1){
			left();
		}
		if(horz == -1){
			right();
		}
		checkLasers();
		return maze;
	}
	private void checkLasers(){
		ArrayList<Sentry> sentries = maze.getSentries();
		float playerArc = 0;
		float playerSent = 0;
		int sentLocY = 0;
		int sentLocX = 0;
		float angleLow = 0;
		float angleHigh = 0;
		caught = 25;
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
					if(yPos - sentLocY < 0){
						playerSent = 90;
					}else{
						playerSent = -90;
					}
				}else{
					playerSent = (float)Math.toDegrees(Math.atan((yPos-sentLocY)/(sentLocX-xPos)));
				}
				if(xPos-sentLocX < 0){//works around tan only doing -90 to 90
				playerSent += 180;
				}
				if(playerSent < 0){
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
	public void up() {
		for(int i = 0; i < SPEED && !tileIsWall(xPos, yPos); i++)
			yPos -= 1;
		if(tileIsWall(xPos, yPos))
			yPos++;
	}

	public void down() {
		for(int i = 0; i < SPEED && !tileIsWall(xPos, yPos); i++)
			yPos += 1;
		if(tileIsWall(xPos, yPos))
			yPos--;
	}

	public void right() {
		for(int i = 0; i < SPEED && !tileIsWall(xPos, yPos); i++)
			xPos += 1;
		if(tileIsWall(xPos, yPos))
			xPos--;
	}

	public void left() {
		for(int i = 0; i < SPEED && !tileIsWall(xPos, yPos); i++)
			xPos -= 1;
		if(tileIsWall(xPos, yPos))
			xPos++;
	}
	
	
	private boolean tileIsWall(int x, int y){
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