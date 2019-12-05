package model;

import java.awt.Color;
import java.util.Map;

public class Video {
	private String path;
	/** total frames */
	private int length;
	/** 64 chars frequence feature for each frame */
	private String[] fingerprint;
	private Map<Color, Double>[] mainColors;
	
	public Video(String path, int length) {
		this.path = path;
		this.length = length;
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
	
	
}
