package util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import dataaccess.FrameReader;

public class Plot {
	private static final int RANGE_H = 100;
	
	/**
	 * draw a plot with given similarity array
	 * @param sim each two frames similarity between query clip and db video 
	 * @param step read step
	 * @return plot histogram image
	 */
	public static BufferedImage draw(double[] sim, int step) {
		int w = sim.length * step, h = RANGE_H;
		return draw(sim, step, w, h);
	}
	
	/**
	 * draw a plot with given similarity array
	 * @param sim each two frames similarity between query clip and db video 
	 * @param step read step 
	 * @param w plot width
	 * @param h plot height
	 * @return plot image
	 */
	public static BufferedImage draw(double[] sim, int step, int w, int h) {
		byte[][][] data = formMat(sim, step);
		
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
	 * @param step read step
	 */
	public static byte[][][] formMat(double[] sim, int step) {
		int h = RANGE_H, w = sim.length * step;
		byte[][][] mat = new byte[h][w][3];
		
		// sim arr index
		int i = 0;
		// x axis
		for (int x = 0; x < w; x += step, i++) {
			for (int y = h - 1; y >= 0; y--) {
				// y axis, oY starts from 1 to 100
				int oY = h - y;
				for (int j = 0; j < step; j++) {
					if (oY <= sim[i] * 100) {
						// fill the pixel with red
						mat[y][x + j][0] = (byte) 0xff;
						// others set 0
						mat[y][x + j][1] = 0;
						mat[y][x + j][2] = 0;
					}
				}
			}
		}
		
		return mat;
	}
}
