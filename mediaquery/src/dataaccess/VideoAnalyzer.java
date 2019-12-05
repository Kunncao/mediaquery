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

import dataaccess.descriptor.ColorDes;
import dataaccess.descriptor.FreqDes;
import model.Video;
import util.ImageProc;

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
	public Video readAnalyseVideo(String path, int step, int k) throws FileNotFoundException, IOException {
		return readAnalyseVideo(width, height, path, step, k);
	}
	
	/**
	 * read all the frame under given path and analysis
	 */
	public static Video readAnalyseVideo(int w, int h, String path, int step, int k) throws FileNotFoundException, IOException {
		// read
		String[] fNames = getFNames(path);
		Video v = new Video(path, fNames.length, step, w, h);
		byte[][][][] frames = readFrames(w, h, step, fNames);
		
		// analyse
		// color and freq
		List<Map<Color, Double>> mainColors = new ArrayList<>();
		String[] fingerprint = new String[fNames.length];
		for (int i = 0; i < frames.length; i++) {
			Mat mat = ImageProc.rgb2mat(frames[i]);
			// color
			mainColors.add(ColorDes.getMains(mat, k));
			// freq
			fingerprint[i] = FreqDes.pHash(frames[i]);
		}
		
		v.setMainColors(mainColors);
		v.setFingerprint(fingerprint);
		
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
	public static String[] getFNames(String path) throws FileNotFoundException {
		if (!path.endsWith("/")) path += "/";
		File f = new File(path);
		if (!f.exists()) {
			throw new FileNotFoundException();
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
