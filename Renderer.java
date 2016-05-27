import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This class is responsible for displaying the game. All logic is performed in other classes -- this class only handles drawing after requesting the required information from the player, maze, sentry and TimerDisplay classes
 *
 */
@SuppressWarnings("serial")
public class Renderer extends JPanel implements ActionListener, MouseListener, KeyListener {
	//Class constants
		private final int STARTSIZE = 100; //sets the size of the circle that surrounds the player when a new maze is started
		//Maze drawing related constants
			private final int RWID = 24, RHEI = 24; // Must be divisible by 4
			private final int SPRITESIZE = 16;//size of the sprites
			private final int MAZESIZE = 30;
			private int OFFSET = (int)(RWID/2);
			//Pseudo-constants, only changed in the constructor
				private int NUMSEN, DIFF; //settings obtained from Navigation
			
		//Tile status definitions -- ones commented out are simply not used in the code	
			private static final int EMPTY = 0;
			private static final int FLOOR = 1;
			private static final int WALL  = 2;
			private static final int START = 3;
			private static final int EXIT  = 4;
			//private static final int SENTRY  = 5; //-- this isn't actually used by renderer because it asks Maze where the sentries are directly
			private static final int KEY = 6;
			
		//Sentry Arc constants	
			private final int ARCDIST = (int)(RWID*1.3);
			private final int ARCWIDTH = 45;			
			
		//Timer related constants	
			private final int FPS = 1000/60;
			private final int TICKRATE = 60; //number of frames for one second
			private final int STARTTIME = 120;
	
	
	//Class variables		
		private Timer fpsTimer;
		// Game related variables
			private Maze maze;
			private Player player;
			private int vert, horz; //direction indicators to send to player class from keyEventListener
			private int tick; //ticks once a second						
			private int start, end; //determine size of transition graphics when a maze is started and ended

		// Graphics related variables
			BufferedImage canvas;
			BufferedImage[][] sprites;
			BufferedImage[][] playerSprites;

		// Buttons + Navigation
			private JButton pause;
			private TimerDisplay timer;
			private JLabel timerDisplay;
			private JLabel bonusDisplay;
			private String timerPadding = "          "; //Can't get struts to cooperate :(
			private Navigation navigator;
	
	public Renderer(Navigation n, int width, int height,int NUMSEN, int DIFF){
		navigator = n;
		Color bg2 = new Color(172,230,250);
		this.setBackground(bg2);
		this.vert = this.horz = this.tick = 0; 
		this.setVisible(true);
		this.setFocusable(true);
		this.NUMSEN = NUMSEN;
		this.DIFF = DIFF;
		this.maze = new Maze(this.MAZESIZE, NUMSEN, DIFF);
		this.timer = new TimerDisplay(STARTTIME);
		start = STARTSIZE;
		end = 0;
		try {
			this.sprites = getSprites();
		} catch (IOException e1) {
			// incase the sprites aren't there for some reason idk
			e1.printStackTrace();
		}
		try {
		    this.playerSprites = getPlayerSprites();
		} catch (IOException e) {
			System.err.println("Something wrong with player sprites.");
		}
		
		
		this.canvas = new BufferedImage(this.RWID * this.MAZESIZE, this.RHEI * this.MAZESIZE, BufferedImage.TYPE_3BYTE_BGR);
		this.player = new Player(this.maze, this.RWID, this.RHEI, this.ARCDIST, this.ARCWIDTH); //Irfan
		//Tom -- we don't pass OFFSET because we just add OFFSET to the player coordinates -- it's not relevant to the player
		//we also don't need framewidth and frameheight for the same reasons
		
		fpsTimer = new Timer(FPS, this);
		fpsTimer.setRepeats(true);
		fpsTimer.start();
		
		addKeyListener(this); // ???
		addMouseListener(this); // ???
		//to whoever put the ??? marks there those are needed to make sure that the game listens to key and mouse strokes --tom
		
		sideBar();

	}
	private void sideBar(){//just puts the code for the sidebar somewhere else
		//adding Pause button & timer counter
		try {
			pause = new JButton(createImage("pauseP.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pause.setRolloverEnabled(true);
		
		try {
			pause.setRolloverIcon(createImage("pauseA.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pause.setBorderPainted(false);
		pause.setContentAreaFilled(false);
		
		// Set ActionListener for pause button
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showPause();
			}
		});
		JLabel timerText = new JLabel();
		timerText.setText(timerPadding + "Time left: ");
		timerText.setFont(new Font("Rockwell", Font.BOLD, 30));
		timerText.setForeground(Color.DARK_GRAY);		
		
		timerDisplay = new JLabel();
		timerDisplay.setText(timerPadding + this.timer.getTime());
		timerDisplay.setFont(new Font("Rockwell", Font.BOLD, 30));
		timerDisplay.setForeground(Color.DARK_GRAY);
		timerDisplay.setVisible(true);
		
		JLabel bonusText = new JLabel();
		bonusText.setText(timerPadding + "Bonus left: ");
		bonusText.setFont(new Font("Rockwell", Font.BOLD, 30));
		bonusText.setForeground(Color.DARK_GRAY);
		
		bonusDisplay = new JLabel();
		bonusDisplay.setText(timerPadding  + this.timer.getBonus());
		bonusDisplay.setFont(new Font("Rockwell", Font.BOLD, 30));
		bonusDisplay.setForeground(Color.DARK_GRAY);
		bonusDisplay.setVisible(true);
			
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(725));
		
		Box VerBox = Box.createVerticalBox();

		VerBox.add(Box.createVerticalStrut(10));
		VerBox.add(timerText);
		VerBox.add(timerDisplay);
		VerBox.add(bonusText);
		VerBox.add(bonusDisplay);
		
		VerBox.add(Box.createVerticalStrut(350));
		VerBox.add(Box.createRigidArea(new Dimension(50, 0)));
		VerBox.add(pause);
	
		box.add(VerBox);
		add(box);
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
		if(maze.isExitStatus() && end <= 0){
			end = (RWID)*MAZESIZE;
			maze.setExitStatus(false);
			timer.addTime();
		}
		if(end > 0){
			cg.fillRect(0, 0, (RWID)*MAZESIZE-end, (RWID)*MAZESIZE);
			end = end - 10;
			if(end <= 0){
				maze = new Maze(this.MAZESIZE, NUMSEN, DIFF);
				player = new Player(this.maze, this.RWID, this.RHEI, this.ARCDIST, this.ARCWIDTH);
				start = STARTSIZE;
			}
		}
		g.fillRect(0, 0, (int)((RWID+1)*MAZESIZE+1), (int)((RHEI+1)*MAZESIZE)+1);
		g.drawImage(canvas, OFFSET+3, OFFSET+3, null);

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
				}else if (this.maze.getTile(n, m).getType() == KEY){//yellow
					g.setColor(Color.YELLOW);
				}
				//g.fillRect(OFFSET+n*(RWID+1), OFFSET+m*(RHEI+1), RWID,RHEI);
				//g.fillRect(n*(this.RWID), m*(this.RHEI), this.RWID, this.RHEI); //Irfan
				g.drawImage(sprites[maze.getTile(n, m).getImgCol()][maze.getTile(n, m).getImgRow()], n*(this.RWID), m*(this.RHEI), (n+1)*(this.RWID), (m+1)*(this.RHEI), 1,1, 15,15, this);
				//g.drawImage(sprites[6][2], n*(this.RWID), m*(this.RHEI), (n+1)*(this.RWID), (m+1)*(this.RHEI), 1,1, 16,16, this);
			}
		}
		

	}
	
	//Edited by Irfan
	/**
	 * 
	 * @param g
	 */
	private void drawPlayer(Graphics g){ // it's more OOP for this function to be in renderer
		//draws a circle around the player at start of maze
		g.setColor(Color.ORANGE);
		if(start > 0){
			g.fillArc(this.player.getxPos()-start, this.player.getyPos()-start, 2*start, 2*start, 0,360);
			start--;
		}
		// Tom -- bit messy but we're putting player x and y coordinate in the center because he's a dot. If he becomes a sprite I'll change it back
		g.drawImage(this.playerSprites[this.player.getDirection()][0], this.player.getxPos()-15, this.player.getyPos()-20, this.player.getxPos()+15, this.player.getyPos()+10, 1, 1, 20, 20, this);
	}
	
	/**
	 * 
	 * 
	 * @param g
	 */
	private void drawSentries(Graphics g){
		
		for(Sentry sentry: this.maze.getSentries()){
			
			g.drawImage(sprites[5][0], sentry.getColumn()*(this.RWID), sentry.getRow()*(this.RHEI), (1+sentry.getColumn())*(this.RWID), (1+sentry.getRow())*(this.RHEI), 1,1, 15,15, this);
			g.setColor(Color.ORANGE);
			
			int centX = sentry.getColumn()*(this.RWID) + this.RWID/2;
			int centY = sentry.getRow()*(this.RHEI) + this.RHEI/2;		
			for(int n = 0; n <= this.ARCDIST; n = n + 4 ){
				g.fillArc(centX - this.ARCDIST, centY - this.ARCDIST, this.ARCDIST*2 - 2*n, this.ARCDIST*2 - 2*n, sentry.getDegree(), this.ARCWIDTH);
				centX += 4;
				centY += 4;
				if (g.getColor() == Color.ORANGE){
					g.setColor(Color.RED);
				}else{
					g.setColor(Color.ORANGE);
				}
			}
		}
	}
	
	
	private BufferedImage[][] getSprites() throws IOException{
		BufferedImage bigImg = ImageIO.read(new File("fixedsprites.bmp"));
		int spriteCols = 7;
		int spriteRows = 6;
		
		BufferedImage[][] newSprites = new BufferedImage[spriteCols][spriteRows];
        for (int i = 0; i < spriteRows; i++)
        {
            for (int j = 0; j < spriteCols; j++)
            {
                newSprites[j][i] = bigImg.getSubimage(j * SPRITESIZE, i * SPRITESIZE, SPRITESIZE, SPRITESIZE);
            }
        }
		
		
		return newSprites;
	}
	
	private BufferedImage[][] getPlayerSprites() throws IOException{
		BufferedImage bigImg = ImageIO.read(new File("playerSoldier.png"));
		int spriteCols = 8;
		int spriteRows = 1;
		
		BufferedImage[][] newSprites = new BufferedImage[spriteCols][spriteRows];
        for (int i = 0; i < spriteRows; i++)
        {
            for (int j = 0; j < spriteCols; j++)
            {
                newSprites[j][i] = bigImg.getSubimage(j * 20, i * 20, 20, 20);
            }
        }
		return newSprites;
	}
	
	/**
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g){ 
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
			if (vert == 1) vert = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			if (vert == -1) vert = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if (horz == -1) horz = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if (horz == 1) horz = 0;
		}
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {

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
		if (navigator.getShowing() == this){
			tick = (tick + 1) % TICKRATE;
			updateGame(vert,horz,tick);
			if(tick == 0 && navigator.isPaused() == false){
				this.timer.incrementSecond();
				this.timerDisplay.setText(timerPadding + this.timer.getTime());
				this.timerDisplay.repaint();
				this.bonusDisplay.setText(timerPadding + this.timer.getBonus());
				this.bonusDisplay.repaint();
			}
			
			if(timer.getSeconds() <= 0){
				navigator.showEndScreen(timer);
				fpsTimer.stop();
			
			}
			
			repaint();	
		}
	}
	
	protected static ImageIcon createImage(String path) throws IOException {
		java.net.URL imgURL = Menu.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(ImageIO.read(imgURL));
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getARCWIDTH() {
		return ARCWIDTH;
	}
}