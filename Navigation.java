import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;

import javax.swing.JFrame;


public class Navigation {

	private static JFrame current;
	private Menu m;
	//private Settings s;
	//private Help h;
	//private Game g;
	//private PauseScreen p;
	
	public Navigation () {
		current = new JFrame("The Amazing Maze");
		current.setSize(850, 650);
		current.setVisible(true);
		current.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			this.m = new Menu(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * Sets inital screen to Menu
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final Navigation n  = new Navigation();
		n.runMenu();
	}
	
	/**
	 * Shows menu screen
	 */
	public void runMenu() {
		m.setVisible(true);
		setCurrentFrame(m);
	}
	
	/*
	 
	//Shows game
	public void runGame() {
		g = new Game();
		g.setVisible(true);
		setFrameSettings(g);
	}
	
	//Shows the settings screen	 
	public void runSettings() {
		s = new Settings(this);
		s.setVisible(true);
		setCurrentFrame(s);
	}

	
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
		current.getContentPane().add(screen, BorderLayout.CENTER);
		current.setSize(850, 650);
		current.setResizable(false);
		current.setVisible(true);
		current.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
