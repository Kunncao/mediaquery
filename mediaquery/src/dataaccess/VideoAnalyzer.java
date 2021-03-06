package dataaccess;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.openimaj.video.xuggle.XuggleAudio;

import dataaccess.descriptor.AudioDes;
import dataaccess.descriptor.ColorDes;
import dataaccess.descriptor.FreqDes;
import model.Video;
import util.ImageProc;
import util.VideoConst;

public class VideoAnalyzer {
	private int width = VideoConst.WIDTH;
	private int height = VideoConst.HEIGHT;
	
	public VideoAnalyzer() {
		
	}
	
	public VideoAnalyzer(int w, int h) {
		width = w;
		height = h;
	}
	
	/**
	 * read all the frame under given path and analysis
	 */
	public Video analyseVideo(String path, int step, int k) throws FileNotFoundException, IOException {
		return analyseVideo(width, height, path, step, k);
	}
	
	/**
	 * read all the frame under given path and analysis
	 */
	public static Video analyseVideo(int w, int h, String path, int step, int k) throws FileNotFoundException, IOException {
		// read
		String[] fPaths = getFPath(path);
		Video v = new Video(path, fPaths.length, step, w, h);
		byte[][][][] frames = readFrames(w, h, step, fPaths);
		
		// analyse
		// color and freq
		List<Map<Color, Double>> mainColors = new ArrayList<>();
		List<String> fingerprint = new ArrayList<String>();
		// sound
		List<Double> rmsList = new ArrayList<>();
		ColorDes cd = new ColorDes(k);
		for (int i = 0; i < frames.length; i++) {
			Mat mat = ImageProc.rgb2mat(frames[i]);
			// color
			mainColors.add(cd.getMains(mat));
			// freq
			fingerprint.add(FreqDes.pHash(frames[i]));
		}
		
		// sound
		if (!path.endsWith("/")) path += "/";
		XuggleAudio xa = AudioReader.readAudio(path + Video.getName(path) + ".wav");
		rmsList = AudioDes.getEffectiveSoundPressure(xa);
		
		// set the corresponding value
		v.setMainColors(mainColors);
		v.setFingerprint(fingerprint);
		v.setRmsList(rmsList);
		
		return v;
	}
	
	/**
	 * read all the frame under given path, store then as a rgb matrix with 3 dims (h * w * (r, g, b))
	 */
	public static byte[][][][] readFrames(int w, int h, int step, String[] fNames) throws FileNotFoundException, IOException {
		int len = fNames.length / step;
		byte[][][][] frames = new byte[len][][][];
		
		for (int i = 0; i < len; i++) {
			frames[i] = FrameReader.getMat(w, h, fNames[i * step]);
		}
		
		return frames;
	}
	
	/**
	 * get all rgb files under given path
	 */
	public static String[] getFPath(String path) throws FileNotFoundException {
		if (!path.endsWith("/")) path += "/";
		File f = new File(path);
		if (!f.exists()) {
			throw new FileNotFoundException(path);
		}
		
		// get all frame's name
		FilenameFilter ff = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".rgb");
			}
		};
		String[] fNames = f.list(ff);
		Arrays.sort(fNames);
		// add path to each frame names
		for (int i = 0; i < fNames.length; i++) {
			fNames[i] = path + fNames[i];
		}
		
		return fNames;
	}
}
