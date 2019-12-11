package ui;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.opencv.core.Core;

import dataaccess.VideoAnalyzer;
 
public class start {
	
	static final int WIDTH = 352;
	static final int HEIGHT = 288;
	static String fullNameAudio;
	public static void main(String[] args) {
		// load opencv
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		

	    ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

		          BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		          
		          images.add(image);
		          
	 
		
		UI ui = new UI(images);
		//PlayWaveFile aaaFile=new PlayWaveFile(fullNameAudio);
		ui.setTitle("Multimedia Queries");
		ui.showUI();
		

	}

}
