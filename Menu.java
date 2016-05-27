import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is responsible for the display of the menu, seen when you start up the game
 *
 */
@SuppressWarnings("serial")
public class Menu extends JPanel{
	
	//class variables
	private JButton play;
	private JButton settings;
	private JButton howtoplay;
	private Navigation navigator;
	
	/**
	 * Constructor for main menu screen.
	 * 
	 * @param n Central navigation class
	 * @throws IOException
	 */
	public Menu(Navigation n) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		// Create buttons with icons in them + hover images
		play = new JButton(createImage("playP.png"));
		play.setRolloverEnabled(true);
		play.setRolloverIcon(createImage("playA.png"));
		settings = new JButton(createImage("settingsP.png"));
		settings.setRolloverEnabled(true);
		settings.setRolloverIcon(createImage("settingsA.png"));
		howtoplay = new JButton(createImage("howtoplayP.png"));
		howtoplay.setRolloverEnabled(true);
		howtoplay.setRolloverIcon(createImage("howtoplayA.png"));
		
		// Set border to false
		play.setBorderPainted(false);
		settings.setBorderPainted(false);
		howtoplay.setBorderPainted(false);
		
		// Set content area to be smaller/only contain image
		play.setContentAreaFilled(false);
		settings.setContentAreaFilled(false);
		howtoplay.setContentAreaFilled(false);
		
		// Set Background of buttons to white
		play.setBackground(Color.white);
		settings.setBackground(Color.white);
		howtoplay.setBackground(Color.white);

		/*
		// Set action commands
		play.setActionCommand("Play");
		settings.setActionCommand("Settings");
		howtoplay.setActionCommand("HowToPlay");
		*/
		
		// Set ActionListener for play button
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showGame();
			}
		});
		
		// Set ActionListener for settings button
		settings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showSettings();
			}
		});
		
		// Set Alignment
		play.setAlignmentX(CENTER_ALIGNMENT);
		settings.setAlignmentX(CENTER_ALIGNMENT);
		howtoplay.setAlignmentX(CENTER_ALIGNMENT);
		
		// Create JPanel for title
		JPanel title = new JPanel();
		ImageIcon headerIcon = createImage("title.png");
		JLabel headerLabel = new JLabel(headerIcon, JLabel.CENTER);
		title.add(headerLabel,BorderLayout.CENTER);
		title.setOpaque(false);
		
		// Place components in a box layout
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(20));
		box.add(title);
		box.add(Box.createVerticalStrut(80));
		box.add(play);
        box.add(Box.createVerticalStrut(40));
        box.add(settings);
        box.add(Box.createVerticalStrut(40));
        box.add(howtoplay);
        // Adding box containing components
        add(box); 
        
		// Set ActionListener for howtoplay button
		howtoplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showHowToPlay();
			}
		});	
				
		
	}
	
	/**
	 * Helper function to create image to be drawn on JFrame.
	 * 
	 * @param path Image file name
	 * @return Image 
	 * @throws IOException
	 */
	protected static ImageIcon createImage(String path) throws IOException {
		java.net.URL imgURL = Menu.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(ImageIO.read(imgURL));
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	/**
	 * Overridden -- Draws the images onto the JFrame.
	 */
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
		try {
			g.drawImage(createImage("background.png").getImage(), 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
}