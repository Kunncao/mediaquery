package dataaccess.descriptor;

import java.util.ArrayList;
import java.util.List;

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
	public static List<Double> getEffectiveSoundPressure(AudioStream as) {
		// RMS for each chunk
		List<Double> rmsList = new ArrayList<>();
		// set the time length of the window and overlap
		int windowSizeMillis = (int) (nFrameAvg * 1.0 / VideoConst.FPS * 1000), overlapMillis = 0;
		
		EffectiveSoundPressure esp = new EffectiveSoundPressure(as, windowSizeMillis, overlapMillis);
		@SuppressWarnings("unused")
		SampleChunk sc = null;
		while ((sc = esp.nextSampleChunk()) != null) {
			double rms = esp.getEffectiveSoundPressure() / 20d;
			rmsList.add(rms);
		}

		return rmsList;
	}
}
