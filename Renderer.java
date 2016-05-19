package mazeGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Renderer extends JPanel{
//testing variables -- these won't be in once maze is integrated
	private int colSet,colFlag;
	private int ppx, ppy;
	private Maze maze;
	private Player player;
	BufferedImage canvas;
	//constants and other variables
	private int WID,HEI,offset;
	private final int RWID = 24, RHEI = 24;//make this divisible by 4
	private final int MAZESIZE = 30;
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	private final int ARCDIST = (int)(RWID*1.3);
	private final int ARCWIDTH = 45;
	
	public Renderer(int width, int height){
		this.setBackground(Color.GREEN);
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
		canvas = new BufferedImage(RWID*MAZESIZE,RHEI*MAZESIZE,BufferedImage.TYPE_3BYTE_BGR);
		player = new Player(this.maze, RWID, RHEI,ARCDIST,ARCWIDTH); //Irfan
		//Tom -- we don't pass offset because we just add offset to the player coordinates -- it's not relevant to the player
		//we also don't need framewidth and frameheight for the same reasons
	}
	private Maze getMaze(){
		return maze;
	}
	public void updateGame(int vert, int horz, int tick){
		maze.updateMaze();
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
		Graphics cg = canvas.getGraphics();
		drawMaze(cg);
		drawSentries(cg);
		drawPlayer(cg);
		g.drawImage(canvas, offset, offset, null);

	}
	private void drawMaze(Graphics g){
		Maze maze = getMaze();
		for(int m = 0; m < maze.getSize(); m++){
			for(int n = 0;n <  maze.getSize(); n++){
				if (maze.getOne(n, m).getType() == FLOOR){//grey			
					g.setColor(Color.GRAY);
				}else if (maze.getOne(n, m).getType() == START){//red
					g.setColor(Color.RED);
				}else if (maze.getOne(n, m).getType() == WALL){//blue
					g.setColor(Color.BLUE);
				}else if (maze.getOne(n, m).getType() == EXIT){//cyan
					g.setColor(Color.CYAN);
				}else if (maze.getOne(n, m).getType() == EMPTY){//pink
					g.setColor(Color.PINK);
				}
				//g.fillRect(offset+n*(RWID+1), offset+m*(RHEI+1), RWID,RHEI);
				g.fillRect(n*(RWID), m*(RHEI), RWID,RHEI); //IRfan
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
		g.setColor(new Color(20,player.caught,50));
		//Tom -- bit messy but we're putting player x and y coordinate in the center because he's a dot. If he becomes a sprite I'll change it back
		g.fillOval(player.getxPos()-(player.getUserRad()), player.getyPos()-(player.getUserRad()), player.getUserRad()*2, player.getUserRad()*2);

		//original one
		//g.fillOval(player.getxPos()+offset, player.getyPos()+offset, player.getUserRad(), player.getUserRad());

	}
	private void drawSentries(Graphics g){
		for(Sentry sentry: maze.getSentries()){
			g.setColor(Color.BLACK);
			g.fillOval(sentry.getColumn()*(RWID), sentry.getRow()*(RHEI), RWID,RHEI);
			g.setColor(Color.ORANGE);
			int centX = sentry.getColumn()*(RWID) + RWID/2;
			int centY = sentry.getRow()*(RHEI) + RHEI/2;		
			for(int n = 0; n <= ARCDIST; n = n + 2 ){
				g.drawArc(centX-ARCDIST, centY-ARCDIST, ARCDIST*2-2*n, ARCDIST*2-2*n, sentry.getDegree(), ARCWIDTH);
				centX += 2;
				centY += 2;
			}
		}
	}
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		drawFrame(g);
	}
}