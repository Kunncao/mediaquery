package util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dataaccess.VideoConst;
import model.Video;

public class Cache {
	@SuppressWarnings("unchecked")
	public static void cacheVideo(Video v) throws IOException {
		String folder = VideoConst.CACHE_PATH;
		new File(folder).mkdir();
		
		// get video info
		String vPath = v.getPath();
		int vLen = v.totalLen();
		int vStep = v.getStep();
		int vWidth = v.getWidth();
		int vHeight = v.getHeight();
		List<String> vFingerprint = v.getFingerprint();
		List<Map<Color, Double>> vMainColors = v.getMainColors();
		
		// fill json obj
		JSONObject jo = new JSONObject();
		jo.put("path", vPath);
		jo.put("length", vLen);
		jo.put("step", vStep);
		jo.put("width", vWidth);
		jo.put("height", vHeight);
		JSONArray arrFp = new JSONArray();
		arrFp.addAll(vFingerprint);
		jo.put("fingerprint", arrFp);
		JSONArray arrMC = new JSONArray();
		for (Map<Color, Double> map : vMainColors) {
			// each map -> array
			JSONArray jMap = new JSONArray();
			for (Entry<Color, Double> e : map.entrySet()) {
				// key value -> array
				JSONArray jArr = new JSONArray();
				Color c = e.getKey();
				jArr.add(c.getRed());
				jArr.add(c.getGreen());
				jArr.add(c.getBlue());
				jArr.add(e.getValue());
				jMap.add(jArr);
			}
			// add map to List (total 3 dims)
			arrMC.add(jMap);
		}
		jo.put("mainColors", arrMC);
		
		// write
		String output = folder + v.getName() + ".json";
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		bw.write(jo.toJSONString());
		bw.close();
	}
	
	public static Video readCache(String jPath) throws FileNotFoundException, IOException, ParseException {
		BufferedReader br = new BufferedReader(new FileReader(jPath));
		Object obj = new JSONParser().parse(br);
		JSONObject jo = (JSONObject)obj;
		// get info
		String path = (String) jo.get("path");
		int length = (int)(long) jo.get("length");
		int step = (int)(long) jo.get("step");
		int w = (int)(long) jo.get("width"), h = (int)(long) jo.get("height");
		
		JSONArray vFingerprint = (JSONArray) jo.get("fingerprint");
		List<String> fingerprint = new ArrayList<String>();
		for (Object fp : vFingerprint) {
			fingerprint.add((String)fp);
		}
		
		JSONArray arrMC = (JSONArray) jo.get("mainColors");
		List<Map<Color, Double>> mainColors = new ArrayList<Map<Color,Double>>();
		for (Object mC : arrMC) {
			JSONArray jMap = (JSONArray) mC;
			Map<Color, Double> map = new HashMap<Color, Double>();
			// r, g, b, value
			for (Object val : jMap) {
				JSONArray rgbwt = (JSONArray) val;
				int r = (int)(long) rgbwt.get(0), g = (int)(long) rgbwt.get(1), b = (int)(long) rgbwt.get(2);
				double wt = (double) rgbwt.get(3);
				Color c = new Color(r, g, b);
				map.put(c, wt);
			}
			mainColors.add(map);
		}
				
		Video v = new Video(path, length, step, w, h);
		v.setFingerprint(fingerprint);
		v.setMainColors(mainColors);
		
		return v;
		
	}
}
