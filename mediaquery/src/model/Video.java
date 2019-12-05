package model;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import dataaccess.VideoConst;

public class Video {
	private String path;
	/** total frames */
	private int length;
	/** 64 chars frequence feature for each read frame */
	private List<String> fingerprint;
	private List<Map<Color, Double>> mainColors;
	private int width = VideoConst.WIDTH, height = VideoConst.HEIGHT;
	/** reading step */
	private int step;
	
	public Video(String path, int length, int step) {
		this.path = path;
		this.length = length;
	}
	
	public Video(String path, int length, int step, int w, int h) {
		this.path = path;
		this.length = length;
		this.step = step;
		width = w;
		height = h;
	}
	
	
	public List<Map<Color, Double>> getMainColors() {
		return mainColors;
	}

	public void setMainColors(List<Map<Color, Double>> mainColors) {
		this.mainColors = mainColors;
	}

	public String getPath() {
		return path;
	}
	
	public String getName() {
		 return getName(path);
	}
	
	public static String getName(String path) {
		int i = path.lastIndexOf("/");
		return path.substring(i + 1);
	}
	
	/** how many frame have been read */
	public int length() {
		return mainColors.size();
	}
	
	/** total number of frame */
	public int totalLen() {
		return length;
	}
	
	public void setFingerprint(List<String> fingerprint) {
		this.fingerprint = fingerprint;
	}
	
	public List<String> getFingerprint() {
		return fingerprint;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getStep() {
		return step;
	}
	
	
}
