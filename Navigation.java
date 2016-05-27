import java.awt.Color;
import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;


/**
 * This class is responsible for the window (JFrame) of the program, general JPanel management related to the window and variables passed between JPanels
 *
 */
public class Navigation {
	//class constants
		private final int WWID = 1000;//window width and height
		private final int WHEI = 800;
		
	//class variables
		private Component showing;// keeps track of which component is being shown
		private boolean paused = false;//pause timer
		//JPanels inside Navigation
			private JFrame current;
			private Menu m;
			private Settings s;
			private HowToPlay h;
			private Pause p;
			private Endscreen e; 
			private Renderer screen;
			
		//variables passed to this from settings.java -- these get passed to screen whenever a new renderer is made
			private int numSen;
			private int diff;

	
	public Navigation() throws IOException {
		numSen = 3; //just some default starting variables 
		diff = 15;
		startWindow();
	}

	

	private void startWindow() throws IOException{
		//display = new Display Menu();
		
		current = new JFrame("The Amazing Maze");
		current.setSize(WWID,WHEI);//since we have pack the size shouldn't matter 
		current.setResizable(true);
		//current.pack(); //we don't want this since everything should just work around the fact the window is constant size		
		current.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//lets us close the window        
		current.setBackground(Color.GREEN);

		current.setVisible(true); 
		

		
		//current.getContentPane().add(screen);
		//showMenu(); //This is commented out since it won't work without the required images -- Tom
	}

	
	public static void main(String[] args) throws IOException{
		Navigation gameWindow = new Navigation();
		//System.out.println(gameWindow.current.getComponent(1));
		gameWindow.current.setVisible(true);
		gameWindow.current.setFocusable(false);
		gameWindow.showMenu();
	}
	
	public void showMenu() {
		//screen = new Renderer(WWID,WHEI);
		try {
			m = new Menu(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.setVisible(true);
		setCurrentFrame(m);
	}
	
	 
//shows game
	public void showGame() {
		screen = new Renderer(this,WWID,WHEI, numSen, diff);
		screen.setVisible(true);
		this.paused = false;
		setCurrentFrame(screen);
	}
	
	//Shows the settings screen	 
	public void showSettings() {
		try {
			s = new Settings(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		s.setVisible(true);
		setCurrentFrame(s);
	}
	
	//Shows how to play screen
	public void showHowToPlay() {
		try {
			h = new HowToPlay(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		h.setVisible(true);
		setCurrentFrame(h);
	}
	
	
	/**
	 * Opens pause screen
	 * @param type
	 * @param diff
	 */
	public void showPause() {
		try {
			p = new Pause(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.setVisible(true);
		setCurrentFrame(p);
		this.paused = true;
	}
	
	/**
	 * Closes pause screen and returns to game
	 */
	public void unPause() {
		p.setVisible(false);
		screen.setVisible(true);
		setCurrentFrame(screen);
		this.paused = false;
	}
	
	/**
	 * Shows Endscreen
	 */
	public void showEndScreen(TimerDisplay timer) {
		try {
			e = new Endscreen(this, timer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.paused = true;
        e.setVisible(true);
		setCurrentFrame(e);
	}
	
	/***
	 * Sets the displayed screen to the screen that is passed in
	 * @param screen
	 */
	private void setCurrentFrame(Component view) {

		current.getContentPane().add(view);
		//you don't want to keep adding stuff to the jframe because you'll eventually have 29384729347 jpanels in the jframe
		//so remove other panels before adding them --tom
		for(Component c : current.getContentPane().getComponents()){	
			if(!view.equals(c)){
				current.remove(c);
			}
		}
		showing = view;//this keeps track of the panel that is being shown, although there should only be one panel being shown at any time--tom
		view.setFocusable(true);
		view.requestFocusInWindow();
		view.setVisible(true);
		current.revalidate();//because im removing stuff you gotta revalidate--tom
	}


	public Component getShowing() {
		return showing;
	}

	public Renderer getRenderer(){
		return screen;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public int getNumSen() {
		return numSen;
	}

	public void setNumSen(int numSen) {
		this.numSen = numSen;
		showSettings();
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
		showSettings();
	}

}