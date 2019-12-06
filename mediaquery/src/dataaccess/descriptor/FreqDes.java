package dataaccess.descriptor;

import java.util.Arrays;

import util.DCT;
import util.ImageProc;

public class FreqDes {
	/**
	 * perceptual hash to generate hash code for each frame 
	 * @param mat rgb matrix height * width * (r, g, b)
	 */
	public static String pHash(byte[][][] mat) {
		// hash result
		StringBuilder hash = new StringBuilder();
		int h = mat.length, w = mat[0].length;
		// resize 32 * 32
		int size = 32;
		byte[][][] resized = ImageProc.resize(w, h, size, size, mat);
		
		// rgb to gray
		byte[][] gray = ImageProc.cvt2gray(size, size, resized);
		
		// do dct
		DCT dct = new DCT(size);
		double[][] afterDCT = dct.doDCT(gray);
		
		// pick the top left 8 * 8 block as the pattern
		// block size, total pixels
		int s = 8, hLen = 64;
		// compute median
		double[] medians = new double[hLen];
		int i = 0;
		for (int y = 0; y < s; y++) {
			for (int x = 0; x < s; x++) {
				medians[i] = afterDCT[y][x];
				i++;
			}
		}
		Arrays.sort(medians);
		double median = medians[hLen/2];
		
		// compute hash value
		for (int y = 0; y < s; y++) {
			for (int x = 0; x < s; x++) {
				if (afterDCT[y][x] > median) hash.append(1);
				else hash.append(0);
			}
		}
		
		return hash.toString();
	}
	
	/**
	 * use the hamming distance 
	 */
	public static double similarity(String h1, String h2) {
		int len = h1.length();
		char[] h1Arr = h1.toCharArray(), h2Arr = h2.toCharArray();
		int sameBits = 0;
		
		for (int i = 0; i < len; i++) {
			if (h1Arr[i] == h2Arr[i]) sameBits++;
		}
		
		double sim = sameBits * 1.0 / len;
		return sim;
	}
}
