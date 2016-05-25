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


public class HowToPlay extends JPanel {
	private JButton menu;
	private Navigation navigator;
	
	public HowToPlay(Navigation n) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		
		// Create buttons with icons in them + hover images
			menu = new JButton(createImage("menuP.png"));
			menu.setRolloverEnabled(true);
			menu.setRolloverIcon(createImage("menuA.png"));
			menu.setBorderPainted(false);
			menu.setContentAreaFilled(false);
			menu.setAlignmentX(CENTER_ALIGNMENT);
			
			// Create JPanel for title
			JPanel title = new JPanel();
			ImageIcon headerIcon = createImage("title.png");
			JLabel headerLabel = new JLabel(headerIcon, JLabel.CENTER);
			title.add(headerLabel,BorderLayout.CENTER);
			title.setOpaque(false);
			
			// Create set of instructions
			JPanel instructions = new JPanel();
			ImageIcon instIcon = createImage("instructions.png");
			JLabel instLabel = new JLabel(instIcon, JLabel.CENTER);
			instructions.add(instLabel,BorderLayout.CENTER);
			instructions.setOpaque(false);
			
			
			// Place components in a box layout
			Box box = Box.createVerticalBox();
			box.add(Box.createVerticalStrut(20));
			box.add(title);
			box.add(Box.createVerticalStrut(10));
			box.add(instructions);
			box.add(Box.createVerticalStrut(5));
			box.add(menu);
			// Adding box containing components
			add(box); 
			
			// Set ActionListener for menu button
			menu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					navigator.showMenu();
				}
			});
			
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
