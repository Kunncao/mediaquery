package comparator;

import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ColorComp {
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
		int threshold = 50;
  
		double rBar = (r1 + r2) / 2.0;
		// differece of r, g, b
		int dR = r1 - r2, dG = g1 - g2, dB = b1 - b2;
		double dC = Math.sqrt((2 + rBar / 256.0) * dR * dR + 4 * dG * dG + 
				(2 + (255 - rBar) / 256.0) * dB * dB);
		if (dC < threshold) return true;
		return false;
	}
}
