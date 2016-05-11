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
	
	//constants and other variables
	private int WID,HEI,offset;
	private final int RWID = 25, RHEI = 25;
	private final int MAZESIZE = 25;
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	
	public Renderer(int width, int height){
		this.setBackground(Color.BLUE);
		WID = width;
		HEI = height;
		offset = 50;
		colSet = 150;
		colFlag = 1;
		ppx = WID/2;
		ppy = HEI/2;
		this.setVisible(true);
		this.setFocusable(true);
		maze = new Maze(MAZESIZE);
	}
	private Maze getMaze(){
		return maze;
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
		drawPlayer(g);
	}
	private void drawMaze(Graphics g){
		Maze maze = getMaze();
		for(int m = 0; m < maze.getSize(); m++){
			for(int n = 0;n <  maze.getSize(); n++){
				if (maze.getOne(m, n).getType() == FLOOR){//pink
					g.setColor(new Color(1*(colSet),0,1*(colSet)));
				}else if (maze.getOne(m, n).getType() == START){//red
					g.setColor(new Color(1*(colSet),25,25));
				}else if (maze.getOne(m, n).getType() == WALL){//blue
					g.setColor(new Color(25,25,1*(colSet)));
				}else if (maze.getOne(m, n).getType() == EXIT){//cyan
					g.setColor(new Color(25,1*(colSet),1*(colSet)));
				}else if (maze.getOne(m, n).getType() == EMPTY){//grey
					g.setColor(new Color(1*(colSet),1*(colSet),1*(colSet)));
				}
				g.fillRect(offset+n*(RWID+1), offset+m*(RHEI+1), RWID,RHEI);
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
	
	private void drawPlayer(Graphics g){
		g.setColor(Color.DARK_GRAY);
		g.fillOval(getPX(), getPY(), RWID/2, RHEI/2);
		
		
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		drawFrame(g);
	}
}
