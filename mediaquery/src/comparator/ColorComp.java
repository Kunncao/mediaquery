package comparator;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.Video;

public class ColorComp {
	/**
	 * compare two videos
	 * @return similarity
	 */
	public static double compare(Video v1, Video v2) {
		int len = Math.abs(v1.length() - v2.length()) + 1;
		double[] simData = new double[len];
		return compare(v1, v2, simData);
	}
	
	/**
	 * compare two videos
	 * @param sim record the similarity of each compare
	 * @return similarity
	 */
	public static double compare(Video v1, Video v2, double[] simData) {
		// comparing starts
		System.out.println("Start comparing: " + v1.getName() + " and " + v2.getName());
		
		// max sim
		double sim = 0;
		// max sim clip in db video
		int maxIndex = 0;
		
		// query video, db video
		Video q, db;
		// query video total frames, db video total frames
		int qLen, dbLen;
		// select short one as query video
		if (v1.length() < v2.length()) {
			q = v1; 
			db = v2;
			qLen = v1.length();
			dbLen = v2.length();
		} else {
			q = v2;
			db = v1;
			qLen = v2.length();
			dbLen = v1.length();
		}
		List<Map<Color, Double>> qColors = q.getMainColors(), dbColors = db.getMainColors();
		
		// regard query clip as a window matching from 1st frame of db video, return the max sim
		int moves = dbLen - qLen;
		for (int i = 0; i <= moves; i++) {
			// current similarity
			double curr = 0;
			for (int j = 0; j < qLen; j++) {
				curr += frameCompare(qColors.get(j), dbColors.get(i + j)) / qLen;
			}
			// record similarity
			simData[i] = curr;
			// update max similarity
			if (curr > sim) {
				sim = curr;
				maxIndex = i;
			}
		}
		
		// comparing finish
		System.out.println("Result:\nMax similarity: " + sim + ", from " + maxIndex * db.getStep() + " to " + 
							(maxIndex * db.getStep() + qLen * q.getStep() - 1) + " frame");
		System.out.println();
		
		return sim;
	}
	
	/**
	 * compare two frames color 
	 * map is used to store the main colors and corresponding weights
	 * @param qf query frame
	 * @param dbf db frame
	 */
	public static double frameCompare(Map<Color, Double> qf, Map<Color, Double> dbf) {
		double sim = 0;
		
		// compare each color
		Set<Entry<Color, Double>> qSet = qf.entrySet();
		Set<Entry<Color, Double>> dbSet = dbf.entrySet();
		for (Entry<Color, Double> e : qSet) {
			// query color
			Color qc = e.getKey();
			// query color weight
			double qwt = e.getValue();
			
			// compare to color in db frame
			// if there is same color in db frame
			if (dbf.containsKey(qc)) {
				sim += Math.min(qwt, dbf.get(qc));
				continue;
			}
			// max sim of this color in db frame
			double max = 0;
			for (Entry<Color, Double> et : dbSet) {
				Color dbc = et.getKey();
				double dbwt = et.getValue();
				if (isSim(qc, dbc)) {
					double curr = Math.min(qwt, dbwt);
					max = Math.max(curr, max);
				}
			}
			sim += max;
			
		}
		
		return sim;
	}
	
	/**
	 * if two color is similar
	 */
	public static boolean isSim(Color c1, Color c2) {
		// compute weighted euclidean distance 
		int r1 = c1.getRed(), r2 = c2.getRed(), 
			g1 = c1.getGreen(), g2 =c2.getGreen(), 
			b1 = c1.getBlue(), b2 = c2.getBlue();
		int threshold = 60;
  
		double rBar = (r1 + r2) / 2.0;
		// differece of r, g, b
		int dR = r1 - r2, dG = g1 - g2, dB = b1 - b2;
		double dC = Math.sqrt((2 + rBar / 256.0) * dR * dR + 4 * dG * dG + 
				(2 + (255 - rBar) / 256.0) * dB * dB);
		if (dC < threshold) return true;
		return false;
	}
}
