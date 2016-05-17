package mazeGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Renderer extends JPanel{
//testing variables -- these won't be in once maze is integrated
	private int colSet,colFlag;
//	private final int[][] testMaze = {
//			{ 0,1,0,0,0,0,0,1,1,1 },
//			{ 0,1,0,0,0,0,0,1,1,1 },
//			{ 0,1,0,0,0,0,0,1,1,1 },
//			{ 0,1,0,0,0,0,1,0,0,0 },
//			{ 0,1,0,0,0,1,0,1,0,0 },
//			{ 0,0,1,0,0,0,1,0,0,0 },
//			{ 0,0,1,0,0,0,0,0,0,0 },
//			{ 0,0,1,0,0,0,0,0,0,0 },
//			{ 1,0,1,0,0,1,0,0,0,0 },
//			{ 0,0,1,0,0,0,0,0,0,0 },
//			{ 1,1,1,1,1,1,1,0,1,1 },
//	};
	private int ppx, ppy;
	private Maze maze;
	public Player player;
	
	//constants and other variables
	private int WID,HEI,offset;
	private final int RWID = 24, RHEI = 24;//make this divisible by 4
	private final int MAZESIZE = 30;
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	
	public Renderer(int width, int height){
		this.setBackground(Color.BLUE);
		WID = width;
		HEI = height;
		offset = 50; //MAKE CAPITALS? -Irfan.
		colSet = 150;
		colFlag = 1;
		ppx = WID/2;
		ppy = HEI/2;
		this.setVisible(true);
		this.setFocusable(true);
		maze = new Maze(MAZESIZE);
		player = new Player(this.maze, RWID, RHEI); //Irfan
		//Tom -- we don't pass offset because we just add offset to the player coordinates -- it's not relevant to the player
		//we also don't need framewidth and frameheight for the same reasons
	}
	private Maze getMaze(){
		return maze;
	}
	public void movePlayer(int vert, int horz){
		maze = player.move(vert,horz,maze);
	}
	public int getPX(){
		return ppx;
	}
	public int getPY(){
		return ppy;
	}
	public void setPX(int n){
		ppx += n;
	}
	public void setPY(int n){
		ppy += n;
	}
	private void drawFrame(Graphics g){
		drawMaze(g);
		drawSentries(g);
		drawPlayer(g);

	}
	private void drawMaze(Graphics g){
		Maze maze = getMaze();
		for(int m = 0; m < maze.getSize(); m++){
			for(int n = 0;n <  maze.getSize(); n++){
				if (maze.getOne(m, n).getType() == FLOOR){//grey			
					g.setColor(new Color(1*(colSet),1*(colSet),1*(colSet)));
				}else if (maze.getOne(m, n).getType() == START){//red
					g.setColor(new Color(1*(colSet),25,25));
				}else if (maze.getOne(m, n).getType() == WALL){//blue
					g.setColor(new Color(25,25,1*(colSet)));
				}else if (maze.getOne(m, n).getType() == EXIT){//cyan
					g.setColor(new Color(25,1*(colSet),1*(colSet)));
				}else if (maze.getOne(m, n).getType() == EMPTY){//pink
					g.setColor(new Color(1*(colSet),0,1*(colSet)));
				}
				//g.fillRect(offset+n*(RWID+1), offset+m*(RHEI+1), RWID,RHEI);
				g.fillRect(offset+n*(RWID), offset+m*(RHEI), RWID,RHEI); //IRfan
			}
		}
		colSet= colSet + colFlag;
		if (colSet > 200){
			colFlag = -1;
		}
		if (colSet < 150){
			colFlag = 1;
		}
	}
	
	//Edited by Irfan
	private void drawPlayer(Graphics g){//it's more OOP for this function to be in renderer
		g.setColor(Color.YELLOW);
		//Tom -- bit messy but we're putting player x and y coordinate in the center because he's a dot. If he becomes a sprite I'll change it back
		g.fillOval(player.getxPos()+offset-(player.getUserRad()), player.getyPos()+offset-(player.getUserRad()), player.getUserRad()*2, player.getUserRad()*2);

		//original one
		//g.fillOval(player.getxPos()+offset, player.getyPos()+offset, player.getUserRad(), player.getUserRad());

	}
	private void drawSentries(Graphics g){
		for(Sentry sentry: maze.getSentries()){
			g.setColor(new Color(20,20,20));
			g.fillOval(offset+sentry.getColumn()*(RWID), offset+sentry.getRow()*(RHEI), RWID,RHEI);
			
		}
	}
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		drawFrame(g);
	}
}