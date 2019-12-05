package model;

import java.awt.Color;
import java.util.Map;

import dataaccess.VideoConst;

public class Video {
	private String path;
	/** total frames */
	private int length;
	/** 64 chars frequence feature for each frame */
	private String[] fingerprint;
	private Map<Color, Double>[] mainColors;
	private int width = VideoConst.WIDTH, height = VideoConst.HEIGHT;
	
	public Video(String path, int length) {
		this.path = path;
		this.length = length;
	}
	
	public Video(String path, int length, int w, int h) {
		this.path = path;
		this.length = length;
		width = w;
		height = h;
	}
	
	public Map<Color, Double>[] getMainColors() {
		return mainColors;
	}
	
	public void setMainColors(Map<Color, Double>[] mainColors) {
		this.mainColors = mainColors;
	}
	
	public String getPath() {
		return path;
	}
	
	public int length() {
		return length;
	}
	
	public void setFingerprint(String[] fingerprint) {
		this.fingerprint = fingerprint;
	}
	
	public String[] getFingerprint() {
		return fingerprint;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
}
