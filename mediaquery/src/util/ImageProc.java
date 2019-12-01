package util;

public class ImageProc {
	/**
	 * a one dimension rgb raster to 2 dims matrix for r, g, b (0, 1, 2)
	 */
	public static byte[][][] raster2mat(int w, int h, byte[] src) {
		int channels = 3;
		// height * width * (0 r, 1 g, 2 b)
		byte[][][] dst = new byte[h][w][channels];
		int offset = w * h, offset2 = offset * 2;
		
		int i = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				// r
				dst[y][x][0] = src[i];
				// g
				dst[y][x][1] = src[i + offset];
				// b
				dst[y][x][2] = src[i + offset2];
				i++;	
			}
		}
		
		return dst;
	}
	
	/**
	 * resize an rgb image, return 3 color's matrix  
	 * @param oriW original width
	 * @param w width after resizing
	 * @param ori original raster
	 */
	public static byte[][][] resize(int srcW, int srcH, int dstW, int dstH, byte[][][] src) {
		int channels = 3;
		byte[][][] dst = new byte[dstH][dstW][channels];
		// ratio of width and height
		double rW = srcW * 1.0 / dstW,  rH = srcH * 1.0 / dstH;
		
		// resize
		for (int y = 0; y < dstH; y++) {
			for (int x = 0; x < dstW; x++) {
				// corresponding x,y in the src
				int srcX = (int)(x * rW), srcY = (int)(y * rH);
				// r
				dst[y][x][0] = src[srcY][srcX][0];
				// g
				dst[y][x][1] = src[srcY][srcX][1];	
				// b
				dst[y][x][2] = src[srcY][srcX][2];
			}
		}
		
		return dst;
	}
	
	/**
	 * convert rgb raster data to gray data
	 */
	public static byte[][] cvt2gray(int w, int h, byte[][][] src) {
		byte[][] dst = new byte[h][w];
		// conversion weight for r, g, b
		double w1 = 0.299, w2 = 0.587, w3 = 0.114;
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				// convert rgb val to gray val
				int r = src[y][x][0] & 0xff, g = src[y][x][1] & 0xff, b = src[y][x][2] & 0xff; 
				dst[y][x] = (byte)(w1 * r + w2 * g + w3 * b);
			}
		}
		
		return dst;
	}
}
