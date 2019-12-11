package dataaccess;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageDisplay {

	private JFrame frame;
	private JLabel imgLabel;
	private BufferedImage imgOne;
	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	
	public ImageDisplay(BufferedImage imgOne) {
		this.imgOne = imgOne;
	}

	public void showImg(){
		showImg("");
	}
	
	public void showImg(String title){
		// Use label to display the image
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		imgLabel = new JLabel(new ImageIcon(imgOne));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(imgLabel, c);

		frame.setTitle(title);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void updateImg() {
		imgLabel.updateUI();
	}
}
