// Templete Settings class

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Settings extends JPanel {

	private JButton menu;
	private JButton prevPage;
	private JButton nextPage;
	private Navigation navigator;
	
	public Settings(Navigation n) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		
		// Create buttons with icons in them + hover images
		menu = new JButton(createImage("playP.png"));
		menu.setRolloverEnabled(true);
		menu.setRolloverIcon(createImage("playA.png"));
		
		prevPage = new JButton(createImage("settingsP.png"));
		prevPage.setRolloverEnabled(true);
		prevPage.setRolloverIcon(createImage("settingsA.png"));
		
		nextPage = new JButton(createImage("howtoplayP.png"));
		nextPage.setRolloverEnabled(true);
		nextPage.setRolloverIcon(createImage("howtoplayA.png"));
		
		// Set border to false
		menu.setBorderPainted(false);
		prevPage.setBorderPainted(false);
		nextPage.setBorderPainted(false);
		
		// Set content area to be smaller/only contain image
		menu.setContentAreaFilled(false);
		prevPage.setContentAreaFilled(false);
		nextPage.setContentAreaFilled(false);
		
		// Set Background of buttons to white
		menu.setBackground(Color.white);
		prevPage.setBackground(Color.white);
		nextPage.setBackground(Color.white);

		// Set action commands
		menu.setActionCommand("Play");
		prevPage.setActionCommand("Settings");
		nextPage.setActionCommand("HowToPlay");
		
		// Set Alignment
		menu.setAlignmentX(CENTER_ALIGNMENT);
		prevPage.setAlignmentX(CENTER_ALIGNMENT);
		nextPage.setAlignmentX(CENTER_ALIGNMENT);
		
		// Create JPanel for title
		JPanel title = new JPanel();
		ImageIcon headerIcon = createImage("title.png");
		JLabel headerLabel = new JLabel(headerIcon, JLabel.CENTER);
		title.add(headerLabel,BorderLayout.CENTER);
		title.setOpaque(false);
		
		// Place components in a box layout
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(200));
		box.add(title);
		box.add(Box.createVerticalStrut(80));
        add(box); 
        
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
