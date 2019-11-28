package dataaccess;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import descriptor.VideoConst;

public class FrameReader {
	private int width = VideoConst.WIDTH;
	private int height = VideoConst.HEIGHT;
	
	/**
	 * read image from a .rgb file in a designated resolution(352 * 288)
	 */
	public BufferedImage readFrameImg(String path) {
		return readFrameImg(width, height, path);
	}
	
	/**
	 * read image from a .rgb file in a designated path with 8-bit RGB color 
	 */
	public static BufferedImage readFrameImg(int width, int height, String path) {
		BufferedImage res = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		try {
			// read file
			File imgf = new File(path);
			// image resolution
			int offset = width * height; 
			// pixel value in r, g, b
			int frameLen = offset * 3;
			
			RandomAccessFile raf = new RandomAccessFile(imgf, "r");
			raf.seek(0);
			
			// get the raster
			byte[] data = new byte[frameLen];
			raf.read(data);
			
			// set rgb value for each pixel
			int rafInd = 0;
			// for blue
			int offset2 = offset * 2;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					byte r = data[rafInd], g = data[rafInd + offset], b = data[rafInd + offset2];
					// & 0xff keep the MSB when casting from byte to int
					int rgb = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					res.setRGB(x, y, rgb);
					rafInd++;
				}
			}
			
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
