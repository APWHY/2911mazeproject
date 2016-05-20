import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game implements ActionListener, MouseListener, KeyListener{
	
	
	private JFrame mainFrame;
	private Renderer screen;
	private int vert, horz,tick;

	//PUT THE MAZE AND PLAYER IN RENDERER
	
	private final int WWID = 900;//window width and height
	private final int WHEI = 900;
	private final int FPS = 1000/60;
	private final int TICKRATE = 60; //number of frames for one second
	public Game() {
		startWindow();
	}
	private void startWindow(){
		//display = new Display Menu();
		vert = horz = tick = 0;
		mainFrame = new JFrame("wow look at this");
		mainFrame.setSize(WWID,WHEI);//since we have pack the size shouldn't matter 
		mainFrame.setResizable(false);
		//mainFrame.pack(); //we don't want this since everything should just work around the fact the window is constant size		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//lets us close the window        
		mainFrame.setBackground(Color.GREEN);

		mainFrame.setVisible(true);
		
		screen = new Renderer(WWID,WHEI);
		mainFrame.getContentPane().add(screen);
		
	}
	public void actionPerformed(ActionEvent e){
		tick = tick++ % TICKRATE;
		screen.updateGame(vert,horz,tick);
		mainFrame.getContentPane().repaint();	
	}
	
	public static void main(String[] args){
		Game gameWindow = new Game();
		//System.out.println(gameWindow.mainFrame.getComponent(1));
		gameWindow.mainFrame.setVisible(true);
		Timer fpsTimer = new Timer(gameWindow.FPS, gameWindow);
		fpsTimer.setRepeats(true);
		fpsTimer.start();
		gameWindow.mainFrame.addKeyListener(gameWindow);
		gameWindow.mainFrame.addMouseListener(gameWindow);
		gameWindow.mainFrame.setFocusable(true);
	}
	public Renderer getRenderer(){
		return screen;
	}
	@Override
	public void keyTyped(KeyEvent e) {	

	}
	
	//Edited by Irfan.
	@Override
	public void keyPressed(KeyEvent e) {	//useful
		if(e.getKeyCode() == KeyEvent.VK_UP){
			vert = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			vert = -1;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			horz = -1;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			horz = 1;
		}
	}
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
	

}