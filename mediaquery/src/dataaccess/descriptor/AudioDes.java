package dataaccess.descriptor;

import org.openimaj.audio.AudioStream;
import org.openimaj.audio.SampleChunk;
import org.openimaj.audio.analysis.EffectiveSoundPressure;

import util.VideoConst;

public class AudioDes {
	/** Number of frames average to take sound pressure over */
	private final static int nFrameAvg = 1;
	
	/**
	 * @param as pass a XuggleAudio obj
	 * @return effective sound pressure
	 * @throws Exception 
	 */
	public static double getEffectiveSoundPressure(AudioStream as) throws Exception {
		// average RMS for each chunk
		double soundPressure = 0;
		// set the time length of the window and overlap
		int windowSizeMillis = (int) (nFrameAvg * 1.0 / VideoConst.FPS * 1000), overlapMillis = 0;
		double RMS = 0;
		
		EffectiveSoundPressure esp = new EffectiveSoundPressure(as, windowSizeMillis, overlapMillis);
		@SuppressWarnings("unused")
		SampleChunk sc = null;
		int i = 0;
		while ((sc = esp.nextSampleChunk()) != null) {
			RMS += esp.getEffectiveSoundPressure() / 20d;
			i++;
		}

		soundPressure = RMS / i;
		return soundPressure;
	}
}
