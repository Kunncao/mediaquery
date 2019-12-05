package dataaccess;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import util.ImageProc;

public class FrameReader {
	private int width = VideoConst.WIDTH;
	private int height = VideoConst.HEIGHT;
	
	public FrameReader() {
		
	}
	
	public FrameReader(int w, int h) {
		width = w;
		height = h;
	}
	
	/**
	 * read image from a .rgb file in a designated resolution(352 * 288)
	 */
	public BufferedImage readFrameImg(String path) throws FileNotFoundException, IOException {
		return readFrameImg(width, height, path);
	}
	
	/**
	 * read image from a .rgb file in a designated path with 8-bit RGB color 
	 */
	public static BufferedImage readFrameImg(int width, int height, String path) throws FileNotFoundException, IOException {
		byte[][][] data = getMat(width, height, path);
		return readFrameImg(width, height, data);
	}
	
	/**
	 * read image from a .rgb file in a given data stream (raster) with 8-bit RGB color
	 */
	public static BufferedImage readFrameImg(int width, int height, byte[][][] data) throws FileNotFoundException, IOException {
		BufferedImage res = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				byte r = data[y][x][0], g = data[y][x][1], b = data[y][x][2];
				// & 0xff keep the MSB when casting from byte to int
				int rgb = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				res.setRGB(x, y, rgb);
			}
		}
		
		return res;
	}
	
	/**
	 * get rgb image matrix from a raster data stream with given w, h
	 */
	public byte[][][] getMat(String path) throws FileNotFoundException, IOException {
		return getMat(width, height, path);
	}
	
	/**
	 * get rgb image matrix from a raster data stream
	 */
	public static byte[][][] getMat(int w, int h, String path) throws FileNotFoundException, IOException {
		int channels = 3;
		byte[][][] mat = ImageProc.raster2mat(w, h, getRaster(w * h * channels, path));
		
		return mat;
	}
	
	/**
	 * @param frameLen width * height * channels
	 * @param path rgb file path
	 * @return one dimension data stream
	 */
	public static byte[] getRaster(int frameLen, String path) throws FileNotFoundException, IOException {
		// read file
		File imgf = new File(path);
		
		RandomAccessFile raf = new RandomAccessFile(imgf, "r");
		raf.seek(0);
		
		// get the raster
		byte[] data = new byte[frameLen];
		raf.read(data);
		raf.close();
		
		return data;
	}
}
