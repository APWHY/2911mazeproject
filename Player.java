package mazeGUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class Player{
	private final int SPEED = 1;
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	private static final int SENTRY  = 5;	
	
	private int xPos;
	private int yPos;
	
	//private Graphics g;
	private Maze maze;
	private int tileWidth;
	private int tileHeight;


	private int userRad; //Tom -- using radius instead since it's a circle
//	private int userWidth;
//	private int userHeight;
	
	JFrame test = new JFrame("Irfan's user");

	public Player(/*Graphics g,*/ Maze maze, int tileWidth, int tileHeight){
		this.maze = maze;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;

		this.userRad = tileWidth/4;
		this.xPos = maze.start.getCol() * tileWidth  + userRad*2;
		this.yPos = maze.start.getRow() * tileHeight + userRad*2;
		


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
		
		return maze;
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