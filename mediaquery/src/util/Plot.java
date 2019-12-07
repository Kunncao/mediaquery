package util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import dataaccess.FrameReader;

public class Plot {
	private static final int RANGE_H = 100;
	
	/**
	 * draw a plot with given similarity array
	 * @param sim each two frames similarity between query clip and db video 
	 * @return plot histogram image
	 */
	public static BufferedImage draw(double[] sim) {
		int w = sim.length, h = RANGE_H;
		return draw(sim, w, h);
	}
	
	/**
	 * draw a plot with given similarity array
	 * @param sim each two frames similarity between query clip and db video 
	 * @param w plot width
	 * @param h plot height
	 * @return plot image
	 */
	public static BufferedImage draw(double[] sim, int w, int h) {
		byte[][][] data = formMat(sim);
		
		int srcH = data.length, srcW = data[0].length;
		if (w != srcW || h != srcH) {
			data = ImageProc.resize(srcW, srcH, w, h, data);
		}
		
		BufferedImage plot = null;
		try {
			plot = FrameReader.readFrameImg(w, h, data);
		} catch (IOException e) {
			System.out.println("Plot data error");
			e.printStackTrace();
		}
		
		return plot;
	}
	
	/**
	 * form a histogram data matrix
	 * @param sim each two frames similarity between query clip and db video 
	 */
	public static byte[][][] formMat(double[] sim) {
		int h = RANGE_H, w = sim.length;
		byte[][][] mat = new byte[h][w][3];
		
		// x axis
		for (int x = 0; x < w; x++) {
			for (int y = h - 1; y >= 0; y--) {
				// y axis, oY starts from 1 to 100
				int oY = h - y;
				if (oY <= sim[x] * 100) {
					// fill the pixel with red
					mat[y][x][0] = (byte) 0xff;
					// others set 0
					mat[y][x][1] = 0;
					mat[y][x][2] = 0;
				}
			}
		}
		
		return mat;
	}
}
