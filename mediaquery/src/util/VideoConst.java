package util;


/**
 * videos basic parameters
 */
public class VideoConst {
	/** video resolution, width * height = 352 * 288 */
	public static final int WIDTH = 352;
	public static final int HEIGHT = 288;
	/** fps: 30 */
	public static final int FPS = 30;
	/** query and db video path */
	public static final String QUERY_PATH = "query/";
	public static final String DB_PATH = "database_videos/";
	/** cache path*/
	public static final String CACHE_PATH = "cache/"; 
	/** total frames for each video in query and db folder (5s & 20s)*/
	public static final int QUERY_FRAMES = 150;
	public static final int DB_FRAMES = 600; 
	/** frame read step */
	public static final int STEP = 3;
	/** k means */
	public static final int K = 6;
}
