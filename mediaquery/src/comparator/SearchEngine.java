package comparator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.simple.parser.ParseException;

import dataaccess.VideoAnalyzer;
import model.Video;
import util.Cache;
import util.Plot;
import util.VideoConst;

public class SearchEngine {
	private String dbPath;
	// all videos name(folder) in db folder
	private String[] dbVideos;
	private int step = VideoConst.STEP;
	private int k = VideoConst.K;
	private int plotWidth, plotHeight;
	private Map<String, BufferedImage> plots = new HashMap<String, BufferedImage>();
	
	public class RankInfo implements Comparable<RankInfo>{
		String videoPath;
		double sim;
		
		public RankInfo(String videoPath, double sim) {
			this.videoPath = videoPath;
			this.sim = sim;
		}
		
		public String[] toStringArr() {
			return new String[] {videoPath, "" + sim};
		}
		
		@Override
		public int compareTo(RankInfo r2) {
			if (r2.sim - sim > 1e-6) return 1;
			else return -1;
		}
	}
	
	/**
	 * @param dbPath all db videos folder
	 */
	public SearchEngine(String dbPath) {
		if (!dbPath.endsWith("/")) dbPath += "/";
		this.dbPath = dbPath;
		dbVideos = getDBVideos();
	}
	
	/**
	 * @param dbPath all db videos folder
	 * @param step frame read step
	 * @param k the num of main colors extracted from each frame
	 */
	public SearchEngine(String dbPath, int step, int k) {
		this(dbPath);
		this.step = step;
		this.k = k;
	}
	
	
	/**
	 * @return a arrays contains {path, similarity} with desc order
	 * @throws ParseException 
	 */
	public String[][] search(String queryFolder) throws FileNotFoundException, IOException, ParseException {
		return search(queryFolder, step, k);
	}
	
	/**
	 * @param step frame read step
	 * @param k the num of main colors extracted from each frame
	 * @return a arrays contains {path, similarity} with desc order
	 */
	public String[][] search(String queryFolder, int step, int k) throws FileNotFoundException, IOException, ParseException {
		Queue<RankInfo> q = new PriorityQueue<>();
		
		// analyse all videos
		VideoAnalyzer va = new VideoAnalyzer();
		Video query = va.analyseVideo(queryFolder, step, k);
		
		Video[] dbs = new Video[dbVideos.length];
		for (int i = 0; i < dbVideos.length; i++) {
			String path = dbPath + dbVideos[i];
			
			// cache video
			File f = new File(VideoConst.CACHE_PATH + dbVideos[i] + ".json");
			if (f.exists()) {
				dbs[i] = Cache.readCache(f.getPath());
			} else {
				dbs[i] = va.analyseVideo(path, step, k);
				Cache.cacheVideo(dbs[i]);
			}
		}
		
		// compare query video with all db videos
		for (int i = 0; i < dbs.length; i++) {
			// compare color
			int len = Math.abs(query.length() - dbs[i].length()) + 1;
			double[] cSimData = new double[len];
			double cSim = ColorComp.compare(query, dbs[i], cSimData);
			// TODO: compare others
			// compare frequency
			double fSim = FreqComp.compare(query, dbs[i]);
			
			// TODO: given specific portion
			// total sim
			double sim = 0;
			// proportion: color proportion, freq proportion
			double cpp = 0, fpp = 0;
			if (fSim == 0) {
				cpp = 1;
			} else {
				cpp = 0.7;
				fpp = 0.3;
			}
			// compute weighted sim
			sim = cpp * cSim + fpp * fSim;
			
			// record ranking
			RankInfo ri = new RankInfo(dbs[i].getPath(), sim);
			q.offer(ri);
			
			// make the plot
			// TODO: combine or not ?
//			double[] simData = null;
			setPlot(dbVideos[i], cSimData);
		}
		
		// output result
		String[][] res = new String[dbs.length][];
		int i = 0;
		while (!q.isEmpty()) {
			res[i] = q.poll().toStringArr();
			i++;
		}
		
		// comparing finished:
		System.out.println(query.getName() + " search finished:\nBest match: " 
							+ Video.getName(res[0][0]) + " " + res[0][1] + 
							"\n==========================================");
		
		return res;
	}
	
	/**
	 * @return all db videos folder path
	 */
	public String[] getDBVideos() {
		File f = new File(dbPath);
		String[] folders = f.list();
		
		return folders;
	}

	public Map<String, BufferedImage> getPlots() {
		return plots;
	}

	/**
	 * @param sim similarity of each frame
	 * @param name video name
	 */
	private void setPlot(String name, double[] sim) {
		int w = plotWidth, h = plotHeight;
		BufferedImage pt = null;
		if (w != 0 && h != 0) {
			pt = Plot.draw(sim, w, h);
		} else {
			pt = Plot.draw(sim);
		}
		plots.put(name, pt);
	}
	
	public void setPlotSize(int plotWidth, int plotHeight) {
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
	}
}
