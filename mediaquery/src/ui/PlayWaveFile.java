package ui;
// Java program to play an Audio 
// file using Clip Object 
import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 
  
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 
  
public class PlayWaveFile
{	
  
    // to store current position 
    Long currentFrame; 
    Clip clip; 
      
    // current status of clip 
    String status; 
      
    AudioInputStream audioInputStream; 
    static String filePath; 
  
    // constructor to initialize streams and clip 
     
    public  PlayWaveFile(String path)
        throws UnsupportedAudioFileException, 
        IOException, LineUnavailableException  
    { 	//
    	try { 
	    	 filePath = path; 
	    	 status="load"; 
	        // create clip reference 
	    	 clip = AudioSystem.getClip(); 
	          }
    	catch (Exception ex)  
        { 
            System.out.println("Error with playing sound."); 
            ex.printStackTrace(); 
        } 
    	
    } 
  
  
      

      
    // Method to play the audio 
    public void play()  
    { 
        //start the clip 
        if (status.equals("paused"))
        {
	    	try {
	    			resumeAudio();
	    			clip.start(); 
	    			status="play";
		    	}  catch(Exception e) {}
	    	
	    }else if (status.equals("restart")){
	    	try {
	    			resetAudioStream();
		        	clip.start(); 
			        status = "play"; 
		        }catch(Exception e) {}
	    }else if (status.equals("load")){
	    	try {
	    			resetAudioStream();
        			clip.start(); 
        			status = "play"; 
	    	}catch(Exception e) {}
	    }
	    else if (!clip.isActive()) 
	    {
	    	try {clip.close();
    			resetAudioStream();
    			clip.start(); 
    			status = "play"; 
	    	}catch(Exception e) {}
	    }
    } 
      
    // Method to pause the audio 
    public void pause()  
    { 
        if (!status.equals("paused"))  
        { 
            this.currentFrame =  
            this.clip.getMicrosecondPosition(); 
            clip.stop(); 
            status = "paused"; 
        } 
    } 
      
    // Method to resume the audio 
    public void resumeAudio() throws UnsupportedAudioFileException, 
                                IOException, LineUnavailableException  
    { 
        clip.close(); 
        resetAudioStream(); 
        clip.setMicrosecondPosition(currentFrame); 
    } 
      
    // Method to restart the audio 
    public void restart() throws IOException, LineUnavailableException, 
                                            UnsupportedAudioFileException  
    { 	
    	if (!status.equals("restart"))  
    	{ 
	    	status="restart";
	        clip.stop(); 
	        clip.close(); 
	        currentFrame = 0L; 
	        clip.setMicrosecondPosition(0); 
    	}
    } 
      
    // Method to stop the audio 
    public void stop() throws UnsupportedAudioFileException, 
    IOException, LineUnavailableException  
    { 
        currentFrame = 0L; 
        clip.stop(); 
        clip.close(); 
    } 
      
    // Method to jump over a specific part 

      
    // Method to reset audio stream 
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, 
                                            LineUnavailableException  
    { 
        audioInputStream = AudioSystem.getAudioInputStream( 
        new File(filePath).getAbsoluteFile()); 
        clip.open(audioInputStream); 
        //clip.loop(Clip.LOOP_CONTINUOUSLY); 
    } 
    
    public void jump(float percent) throws UnsupportedAudioFileException, IOException, 
    LineUnavailableException  
	{   long c=(long)(clip.getMicrosecondLength()*percent);
		if (c > 0 && c < clip.getMicrosecondLength())  
		{ 
			clip.stop(); 
			clip.close(); 
			resetAudioStream(); 
			currentFrame = c; 
			clip.setMicrosecondPosition(c); 
			this.play(); 
		} 
	} 
  
} 