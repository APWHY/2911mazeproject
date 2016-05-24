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


@SuppressWarnings("serial")
public class Menu extends JPanel{
	
	private JButton play;
	private JButton settings;
	private JButton howtoplay;
	private Navigation navigator;
	
	public Menu(Navigation n) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		
		// Create buttons with icons in them + hover images
		this.play = new JButton(createImage("playP.png"));
	    this.play.setRolloverEnabled(true);
	    this.play.setRolloverIcon(createImage("playA.png"));
	    this.settings = new JButton(createImage("settingsP.png"));
	    this.settings.setRolloverEnabled(true);
	    this.settings.setRolloverIcon(createImage("settingsA.png"));
	    this.howtoplay = new JButton(createImage("howtoplayP.png"));
	    this.howtoplay.setRolloverEnabled(true);
	    this.howtoplay.setRolloverIcon(createImage("howtoplayA.png"));
		
		// Set border to false
	    this.play.setBorderPainted(false);
	    this.settings.setBorderPainted(false);
	    this.howtoplay.setBorderPainted(false);
		
		// Set content area to be smaller/only contain image
	    this.play.setContentAreaFilled(false);
	    this.settings.setContentAreaFilled(false);
	    this.howtoplay.setContentAreaFilled(false);
		
		// Set Background of buttons to white
	    this.play.setBackground(Color.white);
	    this.settings.setBackground(Color.white);
	    this.howtoplay.setBackground(Color.white);

		// Set action commands
	    this.play.setActionCommand("Play");
	    this.settings.setActionCommand("Settings");
	    this.howtoplay.setActionCommand("HowToPlay");
		
		// Set ActionListener for play button
	    this.play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				// Call Navigator to run game
			}
		});
		
		// Set ActionListener for settings button
	    this.settings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				// Call Navigator to show settings
			}
		});
		
		// Set ActionListener for howtoplay button
	    this.howtoplay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				// Call Navigator to show how to play
			}
		});	
				
		// Set Alignment
	    this.play.setAlignmentX(CENTER_ALIGNMENT);
	    this.settings.setAlignmentX(CENTER_ALIGNMENT);
	    this.howtoplay.setAlignmentX(CENTER_ALIGNMENT);
		
		// Create JPanel for title
		JPanel title = new JPanel();
		ImageIcon headerIcon = createImage("title.png");
		JLabel headerLabel = new JLabel(headerIcon, JLabel.CENTER);
		title.add(headerLabel,BorderLayout.CENTER);
		title.setOpaque(false);
		
		// Place components in a box layout
		Box box = Box.createVerticalBox();
		box.add(title);
		box.add(Box.createVerticalStrut(40));
		box.add(play);
        box.add(Box.createVerticalStrut(20));
        box.add(settings);
        box.add(Box.createVerticalStrut(20));
        box.add(howtoplay);
        add(box); 
        
        setSize(850, 650);
        setBackground(Color.white);
        
        // Adding box containing components
        add(box, JPanel.CENTER_ALIGNMENT);
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
