package mazeGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Renderer extends JPanel{
//testing variables -- these won't be in once maze is integrated
	private int colSet;
	private final int[][] testMaze = {
			{ 0,1,0,0,0,0,0,1,1,1 },
			{ 0,1,0,0,0,0,0,1,1,1 },
			{ 0,1,0,0,0,0,0,1,1,1 },
			{ 0,1,0,0,0,0,1,0,0,0 },
			{ 0,1,0,0,0,1,0,1,0,0 },
			{ 0,0,1,0,0,0,1,0,0,0 },
			{ 0,0,1,0,0,0,0,0,0,0 },
			{ 0,0,1,0,0,0,0,0,0,0 },
			{ 1,0,1,0,0,1,0,0,0,0 },
			{ 0,0,1,0,0,0,0,0,0,0 }
	};
	private int ppx, ppy;
	
	//constants and other variables
	private int WID,HEI,offset;
	private final int RWID = 68, RHEI = 68;

	
	
	public Renderer(int width, int height){
		this.setBackground(Color.BLUE);
		WID = width;
		HEI = height;
		offset = 50;
		colSet = 100;
		ppx = WID/2;
		ppy = HEI/2;
		this.setVisible(true);
		this.setFocusable(true);
	}
	private int[][] getMaze(){
		return testMaze;
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
		int [][] maze = getMaze();
		for(int m = 0; m < maze.length; m++){
			for(int n = 0;n <  maze[0].length; n++){
				if (maze[m][n] == 0){
					g.setColor(new Color(1*(colSet),0,1*(colSet)));
				}else{
					g.setColor(new Color(1*(colSet),1*(colSet),0));
				}
				g.fillRect(offset+n*(RWID+1), offset+m*(RHEI+1), RWID,RHEI);
			}
		}
		colSet++;
		if (colSet > 255){
			colSet = 0;
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
