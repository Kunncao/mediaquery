package dataaccess.descriptor;

public class ColorDescriptor {
	/**
	 * compute the Euclidean distance of two given points
	 */
	public static double getDistance(double[] p1, double[] p2) {
		// dimension
		int d = p1.length;
		double sumOfSquare = 0; 
		
		for (int i = 0; i < d; i++) {
			double dist = p1[i] - p2[i];
			sumOfSquare += dist * dist;
		}
		
		return Math.sqrt(sumOfSquare);
	}
}
