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
public class Pause extends JPanel{
	
	private JButton unpause;
	private JButton reset;
	private JButton menu;
	private Navigation navigator;
	
	public Pause(Navigation n) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		// Create buttons with icons in them + hover images
		unpause = new JButton(createImage("unpauseP.png"));
		unpause.setRolloverEnabled(true);
		unpause.setRolloverIcon(createImage("unpauseA.png"));
		
		reset = new JButton(createImage("resetP.png"));
		reset.setRolloverEnabled(true);
		reset.setRolloverIcon(createImage("resetA.png"));
		
		menu = new JButton(createImage("menuP.png"));
		menu.setRolloverEnabled(true);
		menu.setRolloverIcon(createImage("menuA.png"));
		
		// Set border to false
		unpause.setBorderPainted(false);
		reset.setBorderPainted(false);
		menu.setBorderPainted(false);
		
		// Set content area to be smaller/only contain image
		unpause.setContentAreaFilled(false);
		reset.setContentAreaFilled(false);
		menu.setContentAreaFilled(false);
		
		
		// Set ActionListener for unpause button
		unpause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.unPause();
			}
		});
		
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
		unpause.setAlignmentX(CENTER_ALIGNMENT);
		reset.setAlignmentX(CENTER_ALIGNMENT);
		menu.setAlignmentX(CENTER_ALIGNMENT);
		
		// Create JPanel for title
		JPanel title = new JPanel();
		ImageIcon headerIcon = createImage("title.png");
		JLabel headerLabel = new JLabel(headerIcon, JLabel.CENTER);
		title.add(headerLabel,BorderLayout.CENTER);
		title.setOpaque(false);
		
		// Create JPanel for title
		JPanel pausetitle = new JPanel();
		ImageIcon pauseIcon = createImage("pausedtitle.png");
		JLabel pauseLabel = new JLabel(pauseIcon, JLabel.CENTER);
		pausetitle.add(pauseLabel,BorderLayout.CENTER);
		pausetitle.setOpaque(false);
		
		// Place components in a box layout
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(20));
		box.add(title);
		box.add(Box.createVerticalStrut(40));
		box.add(pausetitle);
		box.add(Box.createVerticalStrut(40));
		box.add(unpause);
		box.add(Box.createVerticalStrut(40));
        box.add(reset);
        box.add(Box.createVerticalStrut(40));
        box.add(menu);
        // Adding box containing components
        add(box); 
       
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