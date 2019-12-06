package comparator;

import java.util.List;

import dataaccess.descriptor.FreqDes;
import model.Video;

public class FreqComp {
	public static final double THRESHOLD = 0.7;
	
	public static double compare(Video qV, Video dbV) {
		double sim = 0;
		// compare fingerprint
		// query's fingerprint
		List<String> qfp = qV.getFingerprint();
		// database video fingerprint
		List<String> dbfp = dbV.getFingerprint();
		// compare in order (move the clip like a window)
		int move = dbfp.size() - qfp.size();
		for (int i = 0; i <= move; i++) {
			// current sim
			double curr = 0;
			for (int j = 0; j < qfp.size(); j++) {
				if (isSim(qfp.get(j), dbfp.get(i + j)))
					curr += 1.0 / qfp.size();
			}
			sim = Math.max(curr, sim);
		}
		
		// if the whole similarity is lower the threshold, then return 0
		if (sim < THRESHOLD) sim = 0;
		
		return sim;
	}
	
	public static boolean isSim(String fp1, String fp2) {
		if (FreqDes.similarity(fp1, fp2) > THRESHOLD) return true;
		else return false;
	}
}
