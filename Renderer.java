import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Renderer extends JPanel implements ActionListener, MouseListener, KeyListener {
	
	// Game related variables
	private Maze maze;
	private int colSet,colFlag;
	private Player player;
	
	// Graphics related variables
	BufferedImage canvas;
	private int vert, horz,tick;
	private int WID,HEI,OFFSET;
	
	// Constants
	private final int RWID = 24, RHEI = 24; // Must be divisible by 4
	private final int MAZESIZE = 30;
	
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	
	private final int ARCDIST = (int)(RWID*1.3);
	private final int ARCWIDTH = 45;
	
	private final int FPS = 1000/60;
	private final int TICKRATE = 60; //number of frames for one second
	
	public Renderer(int width, int height){
		
		this.setBackground(Color.GREEN);
		this.vert = this.horz = this.tick = 0;
		this.WID = width;
		this.HEI = height;
		this.OFFSET = 30; 
		this.colSet = 150;
		this.colFlag = 1;
		this.setVisible(true);
		this.setFocusable(true);
		this.maze = new Maze(this.MAZESIZE);
		this.canvas = new BufferedImage(this.RWID * this.MAZESIZE, this.RHEI * this.MAZESIZE, BufferedImage.TYPE_3BYTE_BGR);
		this.player = new Player(this.maze, this.RWID, this.RHEI, this.ARCDIST, this.ARCWIDTH); //Irfan
		//Tom -- we don't pass OFFSET because we just add OFFSET to the player coordinates -- it's not relevant to the player
		//we also don't need framewidth and frameheight for the same reasons
		
		Timer fpsTimer = new Timer(FPS, this);
		fpsTimer.setRepeats(true);
		fpsTimer.start();
		
		addKeyListener(this); // ???
		addMouseListener(this); // ???
	}
	
	/**
	 * 
	 * 
	 * @param vert
	 * @param horz
	 * @param tickthis.maze, 
	 */
	public void updateGame(int vert, int horz, int tick){
		maze.updateMaze();
		maze = player.move(vert , horz, this.maze);
	}
	
	/**
	 * 
	 * @return
	 */
	public Maze getMaze(){
		return maze;
	}

	/**
	 * 
	 * 
	 * @param g
	 */
	private void drawFrame(Graphics g){
		Graphics cg = canvas.getGraphics();
		drawMaze(cg);
		drawSentries(cg);
		drawPlayer(cg);
		g.drawImage(canvas, OFFSET, OFFSET, null);

	}
	
	/**
	 * 
	 * 
	 * @param g
	 */
	private void drawMaze(Graphics g){
		
		for (int m = 0; m < this.maze.getSize(); m++){
			for (int n = 0;n <  this.maze.getSize(); n++){
				if (this.maze.getTile(n, m).getType() == FLOOR){//grey			
					g.setColor(Color.GRAY);
				} else if (this.maze.getTile(n, m).getType() == START){//red
					g.setColor(Color.RED);
				} else if (this.maze.getTile(n, m).getType() == WALL){//blue
					g.setColor(Color.BLUE);
				} else if (this.maze.getTile(n, m).getType() == EXIT){//cyan
					g.setColor(Color.CYAN);
				} else if (this.maze.getTile(n, m).getType() == EMPTY){//pink
					g.setColor(Color.PINK);
				}
				//g.fillRect(OFFSET+n*(RWID+1), OFFSET+m*(RHEI+1), RWID,RHEI);
				g.fillRect(n*(this.RWID), m*(this.RHEI), this.RWID, this.RHEI); //Irfan
			}
		}
		
		this.colSet = this.colSet + this.colFlag;
		
		if (this.colSet > 200){
			this.colFlag = -1;
		}
		
		if (this.colSet < 150){
			this.colFlag = 1;
		}
	}
	
	//Edited by Irfan
	/**
	 * 
	 * @param g
	 */
	private void drawPlayer(Graphics g){ // it's more OOP for this function to be in renderer
		
		g.setColor(new Color(20, this.player.caught,50));
		// Tom -- bit messy but we're putting player x and y coordinate in the center because he's a dot. If he becomes a sprite I'll change it back
		// original
		// g.fillOval(player.getxPos()+OFFSET, player.getyPos()+OFFSET, player.getUserRad(), player.getUserRad());
		g.fillOval(this.player.getxPos()-(this.player.getUserRad()), this.player.getyPos()-(this.player.getUserRad()), this.player.getUserRad()*2, this.player.getUserRad()*2);

		
	}
	
	/**
	 * 
	 * 
	 * @param g
	 */
	private void drawSentries(Graphics g){
		
		for(Sentry sentry: this.maze.getSentries()){
			
			g.setColor(Color.BLACK);
			g.fillOval(sentry.getColumn()*(this.RWID), sentry.getRow()*(this.RHEI), this.RWID, this.RHEI);
			g.setColor(Color.ORANGE);
			
			int centX = sentry.getColumn()*(this.RWID) + this.RWID/2;
			int centY = sentry.getRow()*(this.RHEI) + this.RHEI/2;		
			
			for(int n = 0; n <= this.ARCDIST; n = n + 2 ){
				g.drawArc(centX - this.ARCDIST, centY - this.ARCDIST, this.ARCDIST*2 - 2*n, this.ARCDIST*2 - 2*n, sentry.getDegree(), this.ARCWIDTH);
				centX += 2;
				centY += 2;
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g){ // Why is this protected? private?
		super.paintComponent(g);
		this.drawFrame(g);
	}
	
	/**
	 * 
	 */
	@Override
	public void keyTyped(KeyEvent e) {	
	}
	
	//Edited by Irfan.
	/**
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent e) {	//useful
		
		if (e.getKeyCode() == KeyEvent.VK_UP){
			this.vert = 1;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN){
			this.vert = -1;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			this.horz = -1;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT){
			this.horz = 1;
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent e) {	
		if(e.getKeyCode() == KeyEvent.VK_UP){
			vert = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			vert = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			horz = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			horz = 0;
		}
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {	//useful

	}
	@Override
	public void mousePressed(MouseEvent e) {	
	}
	@Override
	public void mouseReleased(MouseEvent e) {	
	}
	@Override
	public void mouseEntered(MouseEvent e) {	
	}
	@Override
	public void mouseExited(MouseEvent e) {	
	}
	@Override
	public void actionPerformed(ActionEvent e){
		tick = (tick + 1) % TICKRATE;
		updateGame(vert,horz,tick);
		repaint();	
	}
}