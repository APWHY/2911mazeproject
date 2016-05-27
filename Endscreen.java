import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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


@SuppressWarnings("serial")
public class Endscreen extends JPanel{
	
	private JButton reset;
	private JButton menu;
	private JLabel timerDisplay;
	private Navigation navigator;
	private TimerDisplay t;
	
	/**
	 * Constructor for end game screen.
	 * 
	 * @param n Central navigation screen
	 * @param timer Timer with time value to be displayed
	 * @throws IOException
	 */
	public Endscreen(Navigation n, TimerDisplay timer) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		t = timer;
		// Create buttons with icons in them + hover images
		reset = new JButton(createImage("replayP.png"));
		reset.setRolloverEnabled(true);
		reset.setRolloverIcon(createImage("replayA.png"));
		
		menu = new JButton(createImage("menuP.png"));
		menu.setRolloverEnabled(true);
		menu.setRolloverIcon(createImage("menuA.png"));
		
		//
		timerDisplay = new JLabel();
		timerDisplay.setText("Congratulations! You survived for "+ timer.getTotal() + "! Do you want to try again?");
		timerDisplay.setFont(new Font("Rockwell", Font.BOLD, 30));
		timerDisplay.setForeground(Color.DARK_GRAY);
		timerDisplay.setVisible(true);
		
		// Set border to false
		reset.setBorderPainted(false);
		menu.setBorderPainted(false);
		
		// Set content area to be smaller/only contain image
		reset.setContentAreaFilled(false);
		menu.setContentAreaFilled(false);
		
		
		// Set ActionListener for reset button
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showGame();;
			}
		});
				
		// Set ActionListener for menu button
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showMenu();
			}
		});
		
		// Set Alignment
		reset.setAlignmentX(CENTER_ALIGNMENT);
		menu.setAlignmentX(CENTER_ALIGNMENT);
		timerDisplay.setAlignmentX(CENTER_ALIGNMENT);
		
		// Create JPanel for title
		JPanel title = new JPanel();
		ImageIcon headerIcon = createImage("title.png");
		JLabel headerLabel = new JLabel(headerIcon, JLabel.CENTER);
		title.add(headerLabel,BorderLayout.CENTER);
		title.setOpaque(false);

		
		// Create JPanel for timesup
		JPanel timesup = new JPanel();
		ImageIcon timesIcon = createImage("timesup.png");
		JLabel timesLabel = new JLabel(timesIcon, JLabel.CENTER);
		timesup.add(timesLabel,BorderLayout.CENTER);
		timesup.setOpaque(false);
		
		
		// Place components in a box layout
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(20));
		box.add(title);
		box.add(Box.createVerticalStrut(40));
		box.add(timesup);
		box.add(timerDisplay);
		//box.add(Box.createVerticalStrut(40));
        //box.add(time used);					//replace time used with timer if you want time to be displayed
		box.add(Box.createVerticalStrut(40));
        box.add(reset);
        box.add(Box.createVerticalStrut(40));
        box.add(menu);
        // Adding box containing components
        add(box); 
       
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
	 * Draws the images onto the JFrame.
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