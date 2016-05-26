import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
	BufferedImage[][] sprites;
	private int vert, horz,tick;
	private int WID,HEI,OFFSET;
	
	// Constants
	private final int RWID = 24, RHEI = 24; // Must be divisible by 4
	private final int SPRITESIZE = 16;//size of the sprites
	private final int MAZESIZE = 30;
	
	private static final int EMPTY = 0;
	private static final int FLOOR = 1;
	private static final int WALL  = 2;
	private static final int START = 3;
	private static final int EXIT  = 4;
	private static final int SENTRY  = 5;
	private static final int KEY = 6;
	
	private final int ARCDIST = (int)(RWID*1.3);
	private final int ARCWIDTH = 45;
	
	private final int FPS = 1000/60;
	private final int TICKRATE = 60; //number of frames for one second
	
	// Buttons + Navigation
	private JButton pause;
	private TimerDisplay timer;
	private JLabel timerDisplay;
	private String timerPadding = "          "; //Can't get struts to cooperate :(
	private Navigation navigator;
	
	public Renderer(Navigation n, int width, int height,int numSen, int diff){
		navigator = n;
		Color bg = new Color(112,200,160);
		Color bg2 = new Color(172,230,250);
		this.setBackground(bg2);
		this.vert = this.horz = this.tick = 0;
		this.WID = width;
		this.HEI = height;
		this.OFFSET = (int)(RWID/2); 
		this.colSet = 150;
		this.colFlag = 1;
		this.setVisible(true);
		this.setFocusable(true);
		this.maze = new Maze(this.MAZESIZE, numSen, diff);
		this.timer = new TimerDisplay();
		try {
			this.sprites = getSprites();
		} catch (IOException e1) {
			// incase the sprites aren't there for some reason idk
			e1.printStackTrace();
		}
		this.canvas = new BufferedImage(this.RWID * this.MAZESIZE, this.RHEI * this.MAZESIZE, BufferedImage.TYPE_3BYTE_BGR);
		this.player = new Player(this.maze, this.RWID, this.RHEI, this.ARCDIST, this.ARCWIDTH); //Irfan
		//Tom -- we don't pass OFFSET because we just add OFFSET to the player coordinates -- it's not relevant to the player
		//we also don't need framewidth and frameheight for the same reasons
		
		Timer fpsTimer = new Timer(FPS, this);
		fpsTimer.setRepeats(true);
		fpsTimer.start();
		
		addKeyListener(this); // ???
		addMouseListener(this); // ???
		
		
		//adding Pause button & timer counter??
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
		
		timerDisplay = new JLabel();
		timerDisplay.setText(timerPadding + this.timer.getTime());
		timerDisplay.setFont(new Font("Rockwell", Font.BOLD, 30));
		timerDisplay.setForeground(Color.DARK_GRAY);
		//timerDisplay.setBackground(Color.CYAN);
		//timerDisplay.setOpaque(true);
		timerDisplay.setVisible(true);
		//timerDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
		//timerDisplay.setLocation(100, 100);
		//timerDisplay.setBounds(200, 200, 100, 100);
		//add(timerDisplay);
		//timerDisplay.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(725));
		
		Box VerBox = Box.createVerticalBox();

		VerBox.add(Box.createVerticalStrut(10));
		//VerBox.add(Box.createHorizontalStrut(100)); //added whitespace padding instead.
		VerBox.add(timerDisplay);
		
		VerBox.add(Box.createVerticalStrut(350));
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
			
			//g.setColor(Color.BLACK);
			g.drawImage(sprites[5][0], sentry.getColumn()*(this.RWID), sentry.getRow()*(this.RHEI), (1+sentry.getColumn())*(this.RWID), (1+sentry.getRow())*(this.RHEI), 1,1, 15,15, this);
			//g.fillOval(sentry.getColumn()*(this.RWID), sentry.getRow()*(this.RHEI), this.RWID, this.RHEI);
			g.setColor(Color.ORANGE);
			
			int centX = sentry.getColumn()*(this.RWID) + this.RWID/2;
			int centY = sentry.getRow()*(this.RHEI) + this.RHEI/2;		
//			g.fillArc(centX - this.ARCDIST, centY - this.ARCDIST, this.ARCDIST*2 , this.ARCDIST*2 , sentry.getDegree(), this.ARCWIDTH);
//			g.setColor(Color.BLACK);
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
		if(tick == 0 && navigator.paused == false){
			this.timer.incrementSecond();
			this.timerDisplay.setText(timerPadding + this.timer.getTime());
			this.timerDisplay.repaint();
		}
		
		repaint();	
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
}