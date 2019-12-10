package comparator;

import java.util.List;

public class AudioComp {
	/**
	 * @param qSound rms chunk list of query sound
	 * @param dbSound rms chunk list of query sound
	 * @return difference of RMS
	 */
	public static boolean compare(List<Double> qSound, List<Double> dbSound) {
		double d = Double.MAX_VALUE;
		int move = dbSound.size() - qSound.size();
		
		for (int i = 0; i <= move; i++) {
			// current section's difference
			double curr = 0;
			for (int j = 0; j < qSound.size(); j++) {
				curr += Math.abs(dbSound.get(i + j) - qSound.get(j));
			}
			d = Math.min(d, curr / qSound.size());
		}
		
		return isSim(d);
	}
	
	/**
	 * @param diff sound similarity
	 */
	public static boolean isSim(double diff) {
		int threshold = 50;
		if (diff > threshold) return false;
		else return true;
	}
}
