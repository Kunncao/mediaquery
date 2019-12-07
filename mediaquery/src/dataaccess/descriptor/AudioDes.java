package dataaccess.descriptor;

import org.openimaj.audio.analysis.FourierTransform;
import org.openimaj.video.xuggle.XuggleAudio;

public class AudioDes {
	/**
	 * do the fast fourier transfer
	 */
	public static void doFFT(XuggleAudio xa) {
		FourierTransform fft = new FourierTransform(xa);
	}
}
