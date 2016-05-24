import java.awt.Color;
import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;

public class Navigation {
	
	
	private JFrame current;
	private Menu m;
	private Settings s;
	//private Help h;
	//private PauseScreen p;
	//private Menu screen;
	private Renderer screen;

	//PUT THE MAZE AND PLAYER IN RENDERER
	
	private final int WWID = 1000;//window width and height
	private final int WHEI = 800;
	private final int FPS = 1000/60;


	public Navigation() throws IOException {
		startWindow();
	}
	
	private void startWindow() throws IOException{
		//display = new Display Menu();

		current = new JFrame("The Amazing Maze");
		current.setSize(WWID,WHEI);//since we have pack the size shouldn't matter 
		current.setResizable(false);
		//current.pack(); //we don't want this since everything should just work around the fact the window is constant size		
		current.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//lets us close the window        
		current.setBackground(Color.GREEN);

		current.setVisible(true);
		
		//screen = new Renderer(WWID,WHEI);
		m = new Menu(this);
		
		//current.getContentPane().add(screen);
		current.getContentPane().add(m); //This is commented out since it won't work without the required images -- Tom
	}

	
	public static void main(String[] args) throws IOException{
		Navigation gameWindow = new Navigation();
		//System.out.println(gameWindow.current.getComponent(1));
		gameWindow.current.setVisible(true);
		gameWindow.current.setFocusable(false);
		gameWindow.showMenu();
	}
	
	public void showMenu() {
		m.setVisible(true);
		setCurrentFrame(m);
	}
	
	 
//shows game
	public void showGame() {
		screen = new Renderer(WWID,WHEI);
		screen.setVisible(true);
		current.add(screen);
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
		current.add(s);
		setCurrentFrame(s);
	}
/*
	
	//Shows how to play screen
	public void runHowToPlay() {
		h = new HowToPlay(this);
        h.setVisible(true);
		setCurrentFrame(h);
	}
	
	*/
	
	/***
	 * Sets the displayed screen to the screen that is passed in
	 * @param screen
	 */
	private void setCurrentFrame(Component screen) {
		//you don't want to keep adding stuff to the jframe because you'll eventually have 29384729347 jpanels in the jframe
		//instead add all the jpanels to the jframe in the constructor and just toggle visibility --Tom
		for(Component c : current.getContentPane().getComponents()){
			c.setVisible(false);
			c.setFocusable(false);
		}

		screen.setFocusable(true);
		screen.setVisible(true);
		screen.requestFocusInWindow();
	}


	public Renderer getRenderer(){
		return screen;
	}

	

}