package mazeGUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class User{
	private final int SPEED = 10;
	
	private int xPos;
	private int yPos;
	
	//private Graphics g;
	private Maze maze;
	private int tileWidth;
	private int tileHeight;
	private int frameWidth;
	private int frameHeight;
	private int offset;
	
	private int userWidth;
	private int userHeight;
	
	JFrame test = new JFrame("Irfan's user");

	public User(/*Graphics g,*/ Maze maze, int tileWidth, int tileHeight, 
			  int frameWidth, int frameHeight, int offset){
		//this.g = g;
		this.maze = maze;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.offset = offset;

		this.xPos = maze.start.getCol() * tileWidth  + offset + 1;
		this.yPos = maze.start.getRow() * tileHeight + offset + 1;
		
		this.userWidth = tileWidth/2;
		this.userHeight = tileHeight/2;
	}
	
	public void drawPlayer(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(xPos, yPos, userWidth, userHeight);
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
	

	private final int WALL = 2;
	//Gets stuck at frame. //Possible soln: Make a frame of wall tiles.
	private boolean tileIsWall(int x, int y){
		int leftEdge = (x - offset)/tileWidth;  //Will round down
		int topEdge = (y - offset)/tileHeight; //if not perfectly divisible. :)
		int rightEdge = (x + userWidth - offset)/tileWidth ;
		int bottomEdge = (y + userHeight - offset)/tileHeight ; 
		
		if(maze.tileType(topEdge, rightEdge) != WALL
				&& maze.tileType(topEdge, leftEdge)     != WALL
				&& maze.tileType(bottomEdge, rightEdge) != WALL
				&& maze.tileType(bottomEdge, leftEdge)  != WALL) //maze has x, y, wrong way around??!!
			return false;
		
		return true;
	}


}