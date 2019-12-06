package dataaccess;

import java.io.File;
import java.io.FileNotFoundException;

import org.openimaj.video.xuggle.XuggleAudio;

public class AudioReader {
	public static XuggleAudio readAudio(String path) throws FileNotFoundException {
		File f = new File(path);
		if (!f.exists()) {
			throw new FileNotFoundException("path");
		}
		XuggleAudio xa = new XuggleAudio(f);
		
		return xa;
	}
}
