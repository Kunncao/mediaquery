package ui;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.opencv.core.Core;
 
public class start {
	
	static final int WIDTH = 352;
	static final int HEIGHT = 288;
	static String fullNameAudio;
	public static void main(String[] args) {
		// load opencv
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String fileFolder = "query/first";
	    ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	    StringBuilder reverseS = new StringBuilder(fileFolder).reverse();
		String filename = fileFolder.substring(fileFolder.length()-reverseS.toString().indexOf('/'), fileFolder.length());
		try {
		    	  //String fullName = fileFolder + "/" + filename + "001.rgb";
		    	  //fullNameAudio= fileFolder + "/" + filename + ".wav";
			String fullName ="first001.rgb";
		    	  File file = new File(fullName);
		    	  InputStream is = new FileInputStream(file);

		   	      long len = file.length();
			      byte[] bytes = new byte[(int)len];
			      int offset = 0;
		          int numRead = 0;
		          while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		              offset += numRead;
		          }
		          System.out.println("Start loading frame: " + fullName);
		    	  int index = 0;
		          BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		          for (int y = 0; y < HEIGHT; y++) {
		            for (int x = 0; x < WIDTH; x++) {
		   				byte r = bytes[index];
		   				byte g = bytes[index+HEIGHT*WIDTH];
		   				byte b = bytes[index+HEIGHT*WIDTH*2]; 
		   				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
		    			image.setRGB(x,y,pix);
		    			index++;
		    			
		    		}
		    	  }
		          images.add(image);
		          is.close();
		          System.out.println("End loading frame: " + fullName);

		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		
		
		
		
	 
		
		UI ui = new UI(images);
		//PlayWaveFile aaaFile=new PlayWaveFile(fullNameAudio);
		ui.showUI();
		

	}

}
