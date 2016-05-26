// Templete Settings class

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Settings extends JPanel {
	private JButton play;
	private JButton menu;
	private JButton easy;
	private JButton medium;
	private JButton hard;
	private JButton few;
	private JButton many;
	private JButton lots;

	private Navigation navigator;
	
	public Settings(Navigation n) throws IOException {
		// Set navigator to the one passed in
		navigator = n;
		
		// Create buttons with icons in them + hover images
        ArrayList<JButton> buttonArray = new ArrayList<JButton>();
        play = new JButton(createImage("playP.png"));
        play.setRolloverIcon(createImage("playA.png"));
        buttonArray.add(play);

        menu = new JButton(createImage("menuP.png"));
        menu.setRolloverIcon(createImage("menuA.png"));
        buttonArray.add(menu);

        easy = new JButton(createImage("Easy-P.bmp"));
        easy.setRolloverIcon(createImage("Easy-A.bmp"));
        buttonArray.add(easy);

        medium = new JButton(createImage("Medium-P.bmp"));
        medium.setRolloverIcon(createImage("Medium-A.bmp"));
        buttonArray.add(medium);

        hard = new JButton(createImage("Hard-P.bmp"));
        hard.setRolloverIcon(createImage("Hard-A.bmp"));
        buttonArray.add(hard);

        few = new JButton(createImage("Few-P.bmp"));
        few.setRolloverIcon(createImage("Few-A.bmp"));
        buttonArray.add(few);

        many = new JButton(createImage("Many-P.bmp"));
        many.setRolloverIcon(createImage("Many-A.bmp"));
        buttonArray.add(many);

        lots = new JButton(createImage("Lots-P.bmp"));
        lots.setRolloverIcon(createImage("Lots-A.bmp"));
        buttonArray.add(lots);
        
        for (JButton button: buttonArray){
        	button.setRolloverEnabled(true);
        	button.setBorderPainted(false);
        	button.setContentAreaFilled(false);
        	button.setBackground(Color.WHITE);
        	
        }

		// Set ActionListeners for settings buttons
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showGame();
			}
		});
		

		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.showMenu();
			}
		});
		
		easy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.setDiff(60);
			}
		});
		medium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.setDiff(30);
			}
		});
		hard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.setDiff(15);
			}
		});
		few.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.setNumSen(5);
			}
		});
		many.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.setNumSen(15);
			}
		});
		lots.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				navigator.setNumSen(30);
			}
		});
		
		
		//lay out jpanels for menu buttons
        JPanel boxdiff = new JPanel();
        boxdiff.setOpaque(false);
        boxdiff.setLayout(new BoxLayout(boxdiff, BoxLayout.LINE_AXIS));
        boxdiff.add(Box.createHorizontalGlue());
        boxdiff.add(easy);
        boxdiff.add(Box.createRigidArea(new Dimension(100, 0)));
        boxdiff.add(medium);
        boxdiff.add(Box.createRigidArea(new Dimension(100, 0)));
        boxdiff.add(hard);
		
        JPanel boxsens = new JPanel();
        boxsens.setOpaque(false);
        boxsens.setLayout(new BoxLayout(boxsens, BoxLayout.LINE_AXIS));
        boxsens.add(Box.createHorizontalGlue());
        boxsens.add(few);
        boxsens.add(Box.createRigidArea(new Dimension(100, 0)));
        boxsens.add(many);
        boxsens.add(Box.createRigidArea(new Dimension(100, 0)));
        boxsens.add(lots);
		
		JPanel boxButtons = new JPanel();
		boxButtons.setOpaque(false);
		boxButtons.setLayout(new BoxLayout(boxButtons, BoxLayout.LINE_AXIS));
		boxButtons.add(Box.createHorizontalGlue());
		boxButtons.add(menu);
		boxButtons.add(Box.createRigidArea(new Dimension(300, 0)));
		boxButtons.add(play);
		
		
		// Create JPanel for title
		JPanel title = new JPanel();
		ImageIcon headerIcon = createImage("title.png");
		JLabel headerLabel = new JLabel(headerIcon, JLabel.CENTER);
		title.add(headerLabel,BorderLayout.CENTER);
		title.setOpaque(false);
		
		// Place components in a box layout
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(75));
		box.add(title);
		box.add(Box.createVerticalStrut(40));
		box.add(boxdiff);
		box.add(Box.createVerticalStrut(40));
		box.add(boxsens);
		box.add(Box.createVerticalStrut(40));
		box.add(boxButtons);
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
