package dataaccess.descriptor;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

public class ColorDes {
	private int k;
	
	public ColorDes(int k) {
		this.k = k;
	}
	
	/**
	 * get main colors with given k by k means clustering 
	 * @return a mat matrix {labels, centroids}
	 */
	public Mat[] getMainColors(Mat frame) {
		return getMainColors(frame, k);
	}
	
	/**
	 * get k main colors of a frame by k means clustering
	 */
	public static Mat[] getMainColors(Mat frame, int k) {
		// convert the mat to one dimesion raster
		Mat ras = frame.reshape(1, frame.cols() * frame.rows());
		// TODO: remove 1.0 / 255
		ras.convertTo(ras, CvType.CV_32F);
		
		// record each pixel's labels and each label's corresponding color
		Mat labels = new Mat(), centroids = new Mat();
		// set max iterations = 100, COUNT + EPS
		TermCriteria criteria = new TermCriteria(TermCriteria.COUNT + TermCriteria.EPS, 100, 1);
		// do K means clustering
		Core.kmeans(ras, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centroids);
		
		// convert centroids to byte
		centroids.convertTo(centroids, CvType.CV_8UC1);
		
		return new Mat[] {labels, centroids};
	}
	
	
}
