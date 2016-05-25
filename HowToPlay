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
	private JButton nextPage;
	private JButton prevPage;
	private Navigation navigator;
	
	public HowToPlay(Navigation n) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		
		// Create buttons with icons in them + hover images
			menu = new JButton(createImage("menuP.png"));
			menu.setRolloverEnabled(true);
			menu.setRolloverIcon(createImage("menuA.png"));
			
			//prevPage = new JButton(createImage("prevP.png"));
			//prevPage.setRolloverEnabled(true);
			//prevPage.setRolloverIcon(createImage("prevA.png"));
			
			nextPage = new JButton(createImage("nextP.png"));
			nextPage.setRolloverEnabled(true);
			nextPage.setRolloverIcon(createImage("nextA.png"));
			
			// Set border to false
			menu.setBorderPainted(false);
			//prevPage.setBorderPainted(false);
			nextPage.setBorderPainted(false);
			
			// Set content area to be smaller/only contain image
			menu.setContentAreaFilled(false);
			//prevPage.setContentAreaFilled(false);
			nextPage.setContentAreaFilled(false);
			
			// Set Background of buttons to white
			menu.setBackground(Color.white);
			//prevPage.setBackground(Color.white);
			nextPage.setBackground(Color.white);

			/*
			// Set action commands
			menu.setActionCommand("Menu");
			prevPage.setActionCommand("prevPage");
			nextPage.setActionCommand("nextPage");
			*/
			
			// Set Alignment
			menu.setAlignmentX(CENTER_ALIGNMENT);
			//prevPage.setAlignmentX(CENTER_ALIGNMENT);
			nextPage.setAlignmentX(CENTER_ALIGNMENT);
			
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
			Box horBox = Box.createHorizontalBox();
			//horBox.add(prevPage);
			horBox.add(Box.createVerticalStrut(325));
			horBox.add(nextPage);
			box.add(horBox);
			box.add(Box.createVerticalStrut(80));
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
			
			/*
			// Set ActionListener for prev button
			prevPage.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			*/
			// Set ActionListener for next button
			nextPage.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					navigator.showHowToPlay2();
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
