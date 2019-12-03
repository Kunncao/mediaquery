package dataaccess.descriptor;

import java.awt.Color;

public class ColorDes {
	// 6 hue sections degrees (ending degrees)
	public static final double RED = 20.0 / 360;
	public static final double YELLOW = 80.0 / 360;
	public static final double GREEN = 140.0 / 360;
	public static final double CYAN = 200.0 / 360;
	public static final double BLUE = 260.0 / 360;
	public static final double MAGNT = 320.0 / 360;
	
	/**
	 * get hue distribution of a frame
	 * @param src an rgb matrix (h * w * each channel)
	 */
	public static int[] getHueDistr(byte[][][] src) {
		// divide hue into 6 section, count each section's pixel numbers
		int[] distr = new int[6];
		int h = src.length, w = src[0].length;
				
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				// convert rgb to hsb 
				int r = src[y][x][0] & 0xff, g = src[y][x][1] & 0xff, b = src[y][x][2] & 0xff;
				float hsbVals[] = Color.RGBtoHSB(r, g, b, null);
				// count number
				float hue = hsbVals[0];
				if (hue <= RED || hue > MAGNT) distr[0]++;
				else if (hue <= YELLOW) distr[1]++;
				else if (YELLOW < hue && hue <= GREEN) distr[2]++;
				else if (hue <= CYAN) distr[3]++;
				else if (hue <= BLUE) distr[4]++;
				else distr[5]++;
			}
		}
		
		return distr;
	}
}
