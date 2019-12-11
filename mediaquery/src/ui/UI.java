package ui;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.json.simple.parser.ParseException;

import com.ibm.icu.text.DecimalFormat;
import com.lowagie.text.Image;

import cern.colt.Arrays;
import comparator.SearchEngine;
import dataaccess.ImageDisplay;
import dataaccess.VideoAnalyzer;
import model.Video;
import util.VideoConst;
 

public class UI extends Frame implements ActionListener{
	
	private SearchEngine se=new SearchEngine(VideoConst.DB_PATH);
	public Map<String, BufferedImage> plots; 
	static final int WIDTH = 352;
    static final int HEIGHT = 288;
    
	private String fileFolder="query";
	private String fullNameAudio;
    public ArrayList<BufferedImage> imagesQuery= new ArrayList<BufferedImage>(); 
    private int totalFrameNumQuery;
    private int playStatus = 3;//1 for play, 2 for pause, 3 for stop
	
    String queryFieldText;
    private PlayWaveFile audioQuery;
    private int currentFrameNum = 0;
    private Thread playingThread;
    private Thread playingDBThread;
    private Thread audioThread;
    private Thread audioDBThread;
    
    public  ImageIcon imgsvisual;;
    
    private JSlider slider;
    private JList list;
    private  DefaultListModel listModel;
    private JTextField queryField;
    private JButton loadButton;
    private JButton searchButton;
    public JLabel videoStartJLable;
    public JLabel diagram;
    
    
    private ArrayList<BufferedImage> imagesResult= new ArrayList<BufferedImage>();
    private int DBtotalFrameNumQuery;
    public JLabel videoEndJLable;
    private PlayWaveFile DBaudioQuery;
    private Thread DBplayingThread;
    private Thread DBaudioThread;
    private int DBcurrentFrameNum = 0;
    private int DBplayStatus = 3;
    private int DBsliderstatus=0;
    
    private String DBloadfile;
    private String  DBfullNameAudio;
    int threadvalue;
    float DBpercent=0;
	public void showUI() {
	    pack();
	    setVisible(true);
	}
	
	public UI(ArrayList<BufferedImage> imgs){
		 
//row1col1	
		JLabel Query=new JLabel("Query:");
		JTextField queryField=new JTextField(13);
		JButton loadButton = new JButton("Load");
		
		//loadButton.addActionListener(this);
		JButton searchButton = new JButton("Search");
		//searchButton.addActionListener(this);
		// layout
		JPanel row1col1=new JPanel();
		row1col1.setLayout(new GridBagLayout());
		row1col1.setBackground(Color.white);
		row1col1.add(Query);
		row1col1.add(queryField);
		row1col1.add(loadButton);
		row1col1.add(searchButton);	
//row1col2 
		//Jlist https://blog.csdn.net/leafinsnowfield/article/details/47400717
		//String [] listEntries ={"Flower.rgb 90%","car.rgb 90%","gamma.rgb 90%","delta.rgb 90%","epslion.rgb 90%","zeta.rgb 90%","eta.rgb 90%","theta.rgb 90%"};
		listModel = new DefaultListModel();
//		listModel.addElement("element");
//		listModel.addElement("item 2");
		list = new JList(listModel);  
		JScrollPane scroller=new JScrollPane(list);
		
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		list.setBorder(BorderFactory.createTitledBorder("Matched Videos:")); 
		list.setVisibleRowCount(10);//设定显示的行数
		//set width
		Dimension d = list.getPreferredSize();
		d.width =200;
		list.setPreferredSize(d);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
	    JButton load=new JButton("Load");
	    //	set layout
	    JPanel row1col2 = new JPanel();
	    row1col2.setBackground(Color.white);
	    GridBagLayout gb=new GridBagLayout();
		row1col2.setLayout(gb);
		
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		
	    gb.setConstraints(scroller,gbc);
	    row1col2.add(scroller);
	    
	    gbc.gridwidth=GridBagConstraints.REMAINDER;
	    gb.setConstraints(load,gbc);
	    row1col2.add(load);
 //visual diagram   
	    	//imgsvisual=null;
	     BufferedImage res = new BufferedImage(400, 50, BufferedImage.TYPE_INT_RGB);

	     ImageIcon  imgsvisual = new ImageIcon(res);
	     diagram=new JLabel(imgsvisual);
	    
	     gbc.gridwidth=GridBagConstraints.REMAINDER;
	     gb.setConstraints(diagram,gbc);
	     row1col2.add(diagram);
	     row1col2.setBackground(Color.white);
	    
//JSlider
	    slider = new JSlider(JSlider.HORIZONTAL,0,100,0);
	    slider.setBackground(Color.white);
	    slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(5);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);

		slider.addMouseListener(new java.awt.event.MouseAdapter()
        {
		    public void mousePressed(MouseEvent e) {  
						DBplayStatus = 2;
						try {
							DBpauseVideo();
							
							DBplayingThread = null;
							
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							//errorLabel.setText(e1.getMessage());
						}
				    	try {
				        	DBaudioQuery.pause();
					    }  catch(Exception E) {} 
		    }  
		    public void mouseReleased(MouseEvent e) { 
		    	DBpercent=(float)(slider.getValue()*0.01);
		    	DBcurrentFrameNum= (int)(DBtotalFrameNumQuery*slider.getValue()*0.01);
		    	//loadDBVideo();
		    	videoEndJLable.setIcon(new ImageIcon(imagesResult.get(DBcurrentFrameNum)));
		    	System.out.println(DBcurrentFrameNum);
		    	DBsliderstatus=1;
		    	
		    	
		    }  
             }
           );

		
//slider width		
		gbc.ipadx=180;
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gb.setConstraints(slider,gbc);
		row1col2.add(slider);
//row2col1  
      
	     JPanel row2col1=new JPanel();
	     videoStartJLable=new JLabel(new ImageIcon(imgs.get(0)));
	     row2col1.add(videoStartJLable);
	     row2col1.setBackground(Color.white);
//row2col2       
	     JPanel row2col2=new JPanel();
	     videoEndJLable=new JLabel(new ImageIcon(imgs.get(0)));
	     row2col2.add(videoEndJLable);
	     row2col2.setBackground(Color.white);
	    
	     
//row3col1
	    Button playButton1= new Button("PLAY");
	    Button playButton2= new Button("PAUSE");
	    Button playButton3= new Button("STOP");
	    JPanel row3col1=new JPanel();
	    row3col1.add(playButton1);
	    row3col1.add(playButton2);
	    row3col1.add(playButton3);
	    row3col1.setBackground(Color.white);
//row3col2
	    Button playButton4= new Button("PLAY");
	    Button playButton5= new Button("PAUSE");
	    Button playButton6= new Button("STOP");
	    JPanel row3col2=new JPanel();
	    row3col2.add(playButton4);
	    row3col2.add(playButton5);
	    row3col2.add(playButton6);
	    row3col2.setBackground(Color.white);

	    JPanel window=new JPanel();
	    window.setLayout(new GridLayout(3,2,20,20));
//set window dimension
	    window.setPreferredSize(new Dimension(1500,1000));	
	    window.add(row1col1);
	    window.add(row1col2);
	    window.add(row2col1);
	    window.add(row2col2);
	    window.add(row3col1);
	    window.add(row3col2);
	    window.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
	    window.setBackground(Color.white);
	    add(window);
	    addWindowListener(new MyListener());
//addlistener	    
		loadButton.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
				 	queryFieldText=queryField.getText();
				 	
				 	int count = 0;
				 	String countpath="query/"+queryFieldText;
			        for( File file: new File(countpath).listFiles( ) ) {
			            if( file.isFile( ) ) ++count;}
			        System.out.println("count   "+count);
			        totalFrameNumQuery=count-2;
				 	loadVideo();
				  }
				});	
		searchButton.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
				  try {
					String[][] res = se.search(VideoConst.QUERY_PATH + queryFieldText);
					System.out.println(Arrays.toString(res));
					
					// TODO: get video name
					List<String> videoNames = new ArrayList<>();
					
					for (int i = 0; i < res.length; i++) {
						videoNames.add(Video.getName(res[i][0]));
					}
					
					//Map<String, BufferedImage> plots = se.getPlots();//map
					plots = se.getPlots();
					listModel.removeAllElements();
					for (String vName : videoNames) {
						BufferedImage bi = plots.get(vName);
						ImageDisplay id = new ImageDisplay(bi);
						id.showImg(vName);
						
						;
					}
					
					for (int i=0;i<7;i++) {
						String path=res [i][0];
						String data=res [i][1];
							//listModel.addElement(aa+bb);
							//System.out.println("aa"+aa);
						DecimalFormat df = new DecimalFormat("00%");
						String [] name=path.split("[/]");	 
						int n=20-name[1].length();
						System.out.println("n"+n);
						String item=name[1]+".rgb";
						for (int j=0;j<n;j++) {
							item+="  ";
						}
						item=item+df.format(Float.parseFloat(data));
						//format(format,item, df.format(Float.parseFloat(data)));
						listModel.addElement(item);
					}
					
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}

				  
				  }
				});	  
		playButton1.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
				  if (playStatus !=1) {
					  playStatus = 1;
					  playVideo();
				  }
				  }
				});	 
		
		playButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent event) {
				playStatus = 2;
				try {
					pauseVideo();
					//audioQuery.play();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//errorLabel.setText(e1.getMessage());
				}
		    	try {
		        	audioQuery.pause();
			    }  catch(Exception e) {}
		    	
			}
		});	 
		
		playButton3.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
				    
				 	stopVideo();
				 	playStatus = 3;
				 	try {
				 	audioQuery.restart();
				 	}  catch(Exception e) {}
				  }
				});	
		
		load.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
				 	queryFieldText=queryField.getText();
				 	currentFrameNum=0;
				 	//System.out.println("list.getSelectedValue()---"+list.getSelectedValue());
				 	String path=list.getSelectedValue().toString();
				 	String []aa=path.split("[.]");
				 	//System.out.println("aa[0]---"+aa[0]);
				 	DBloadfile=aa[0];
				 	int count = 0;
				 	String countpath="database_videos/"+aa[0];
			        for( File file: new File(countpath).listFiles( ) ) {
			            if( file.isFile( ) ) ++count;}
			        System.out.println("count   "+count);
			        DBtotalFrameNumQuery=count-2;
				 	loadDBVideo();
				 	
				 	
				 	BufferedImage resized = resize(plots.get(aa[0]), 400, 50);
//				 	BufferedImage thumbnail = Scalr.resize(image, 150);
//				 	Image image = plots.get(aa[0]) // transform it 
//				 	Image newimg = plots.get(aa[0]).getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
//				 	imageIcon = new ImageIcon(newimg);  // transform it back
				 	diagram.setIcon(new ImageIcon(resized));
				 	
				  }
				});	
		playButton4.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
				  if (DBplayStatus !=1) {
					  DBplayStatus = 1;
					  DBplayVideo();
					  if(DBsliderstatus==1) 
					  {
						  try {
							  DBaudioQuery.jump(DBpercent);
							  DBsliderstatus=0;
						  }  catch(Exception e) {}
					}
					  //System.out.println("click"+currentFrameNum);//103
				  }
				  }
				});	
		playButton5.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
					DBplayStatus = 2;
					try {
						DBpauseVideo();
						//audioQuery.play();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						//errorLabel.setText(e1.getMessage());
					}
			    	try {
			        	DBaudioQuery.pause();
				    }  catch(Exception e) {}
				  }
				});	
		playButton6.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(ActionEvent event) {
				    
				 	DBstopVideo();
				 	DBplayStatus = 3;
				 	try {
				 	DBaudioQuery.restart();
				 	}  catch(Exception e) {} 
				  }
				});	
//functions
		
		
	}
	
	
	private void loadVideo()      {
		//System.out.println(queryFieldText);
		String[] fileName = null;
		//fileName=null;
		
			 
			 
				try {
					fileName=VideoAnalyzer.getFPath("query/"+queryFieldText);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			  
	    try {
	      if(queryFieldText == null || queryFieldText.isEmpty()){
	    	  return;
	      }
	     // imagesQuery = new ArrayList<BufferedImage>();
	      imagesQuery.clear();
	      currentFrameNum=3;
	      for(int i=1; i<=totalFrameNumQuery; i++) {
	    	  String fileNum = "00";
	    	  if(i < 100 && i > 9) {
	    		  fileNum = "0";
	    	  } else if(i > 99) {
	    		  fileNum = "";
	    	  }
	    	  String fullName = fileName[i];
	    	  //String audioFilename = fileFolder + "/" + pathTitle + "/" + pathTitle + ".wav";
	    	  //System.out.println(fullName);
	    	  File file = new File(fullName);
	    	  InputStream is = new FileInputStream(file);

	   	      long len = file.length();
		      byte[] bytes = new byte[(int)len];
		      int offset = 0;
	          int numRead = 0;
	          while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	              offset += numRead;
	          }
	          //System.out.println("Start loading frame: " + fullName);
	    	  int index = 0;
	          BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	          for (int y = 0; y < HEIGHT; y++) {
	            for (int x = 0; x < WIDTH; x++) {
	   				byte r = bytes[index];
	   				byte g = bytes[index+HEIGHT*WIDTH];
	   				byte b = bytes[index+HEIGHT*WIDTH*2]; 
	   				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	    			image.setRGB(x,y,pix);
	    			index++;
	    			//System.out.println("HEIGHT:      " + HEIGHT);
	    		}
	    	  }
	          imagesQuery.add(image);
	          is.close();
	          //playSound = new PlaySound(audioFilename);
	          
	          //videoStartJLable.setIcon(new ImageIcon(image));
	      }//end for
	      videoStartJLable.setIcon(new ImageIcon(imagesQuery.get(0)));
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	      //errorLabel.setText(e.getMessage());
	    } catch (IOException e) {
	      e.printStackTrace();
	      //errorLabel.setText(e.getMessage());
	    }
	   // this.playStatus = 3;
	    //currentFrameNum = 0;
	    //totalFrameNum = imagesQuery.size();
	    //displayScreenShot();
	    fullNameAudio= fileFolder+ "/" + queryFieldText + "/" + queryFieldText + ".wav";
	    try {
			audioQuery=new PlayWaveFile(fullNameAudio);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void loadDBVideo() {
		//System.out.println(queryFieldText);
	    try {
		      if(DBloadfile == null || DBloadfile.isEmpty()){
		    	  return;
		      }
		String[] fileName = null;
		//fileName=null;
		
			 
			 
				try {
					fileName=VideoAnalyzer.getFPath("database_videos/"+DBloadfile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	     // imagesQuery = new ArrayList<BufferedImage>();
	      imagesResult.clear();
	      
	      for(int i=1; i<=DBtotalFrameNumQuery; i++) {
	    	  String fileNum = "00";
	    	  if(i < 100 && i > 9) {
	    		  fileNum = "0";
	    	  } else if(i > 99) {
	    		  fileNum = "";
	    	  }
	    	  String DBfullName = fileName[i];
	    	  //String audioFilename = fileFolder + "/" + pathTitle + "/" + pathTitle + ".wav";
	    	  //System.out.println(fullName);
	    	  File file = new File(DBfullName);
	    	  InputStream is = new FileInputStream(file);

	   	      long len = file.length();
		      byte[] bytes = new byte[(int)len];
		      int offset = 0;
	          int numRead = 0;
	          while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	              offset += numRead;
	          }
	          //System.out.println("Start loading frame: " + fullName);
	    	  int index = 0;
	          BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	          for (int y = 0; y < HEIGHT; y++) {
	            for (int x = 0; x < WIDTH; x++) {
	   				byte r = bytes[index];
	   				byte g = bytes[index+HEIGHT*WIDTH];
	   				byte b = bytes[index+HEIGHT*WIDTH*2]; 
	   				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	    			image.setRGB(x,y,pix);
	    			index++;
	    			//System.out.println("HEIGHT:      " + HEIGHT);
	    		}
	    	  }
	          imagesResult.add(image);
	          is.close();
	          //playSound = new PlaySound(audioFilename);
	          //System.out.println("End loading query frame: " + fullName);
	          
	          //videoStartJLable.setIcon(new ImageIcon(image));
	      }//end for
	      videoEndJLable.setIcon(new ImageIcon(imagesResult.get(currentFrameNum)));
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	      //errorLabel.setText(e.getMessage());
	    } catch (IOException e) {
	      e.printStackTrace();
	      //errorLabel.setText(e.getMessage());
	    }
	   // this.playStatus = 3;
	    //currentFrameNum = 0;
	    //totalFrameNum = imagesQuery.size();
	    //displayScreenShot();
	    DBfullNameAudio= "database_videos"+ "/" + DBloadfile + "/" + DBloadfile + ".wav";
	    try {
	    	DBaudioQuery=new PlayWaveFile(DBfullNameAudio);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void playVideo() {
		playingThread = new Thread() {
            public void run() {
	            System.out.println("Start playing video: " + queryFieldText);
	          	for (int i = currentFrameNum; i < totalFrameNumQuery; i++) {
	          		videoStartJLable.setIcon(new ImageIcon(imagesQuery.get(i)));
	          	    try {
	                  	sleep(1000/30);
	          	    } catch (InterruptedException e) {
	          	    	if(playStatus == 3) {
	          	    		currentFrameNum = 0;
	          	    	} else {
	          	    		currentFrameNum = i;
	          	    	}
	          	    	 //System.out.println("currentFrameNum:------ " + currentFrameNum);
	          	    	videoStartJLable.setIcon(new ImageIcon(imagesQuery.get(currentFrameNum)));
	                  	currentThread().interrupt();
	                  	break;
	                }
	          	}
	          	if(playStatus < 2) {
	          		playStatus = 3;
		            currentFrameNum = 0;
	          	}
	            System.out.println("End playing video: " + queryFieldText);
	        }
	    };
	    
	    audioThread = new Thread() {
            public void run() {
                try {
                	audioQuery.play();
        	    }  catch(Exception e) {}
	        }
	    };
	    audioThread.start();
	    playingThread.start();
	}
	
	private void DBplayVideo() {
		DBplayingThread = new Thread() {
            public void run() {
	            //System.out.println("Start playing video: " + queryFieldText);
	          	for (int i = DBcurrentFrameNum; i < DBtotalFrameNumQuery; i++) {
	          		videoEndJLable.setIcon(new ImageIcon(imagesResult.get(i)));
	          		System.out.println("iiii==== " + i);
	          	    try {
	                  	sleep(1000/30);
	          	    } catch (InterruptedException e) {
	          	    	if(DBplayStatus == 3) {
	          	    		DBcurrentFrameNum = 0;
	          	    	} else {
	          	    		DBcurrentFrameNum = i;
	          	    	}
	          	    	//System.out.println("currentFrameNum:------ " + DBcurrentFrameNum);
	          	    	videoEndJLable.setIcon(new ImageIcon(imagesResult.get(DBcurrentFrameNum)));
	          	    	currentThread().interrupt();
	                  	break;
	                }
	          	  int n=(int)(i*100/DBtotalFrameNumQuery);
	          	 slider.setValue(n);
	          	}
//	          	if(DBplayStatus < 2) {
//	          		DBplayStatus = 3;
//	          		DBcurrentFrameNum = 0;
//	          	}
	        }
	    };
	    
	    DBaudioThread = new Thread() {
            public void run() {
                try {
                	DBaudioQuery.play();
        	    }  catch(Exception e) {}
	        }
	    };
	    DBaudioThread.start();
	    DBplayingThread.start();
	}
	
	private void pauseVideo() throws InterruptedException {
		if(playingThread != null) {
			playingThread.interrupt();
			//audioThread.interrupt();
			//audioQuery.pause();
			//playingThread = null;
			//audioThread = null;
		}
	}

	private void stopVideo() {
		if(playingThread != null) {
			if(playStatus!=2) {
			playingThread.interrupt();}
			//audioThread.interrupt();
			//playSound.stop();
			playingThread = null;
			//audioThread = null;
			slider.setValue(0);
		} else {
			currentFrameNum = 0;
			videoStartJLable.setIcon(new ImageIcon(imagesQuery.get(0)));
		}
	}
	
	private void DBpauseVideo() throws InterruptedException {
		if(DBplayingThread != null) {
			DBplayingThread.interrupt();
			//audioThread.interrupt();
			//audioQuery.pause();
			//playingThread = null;
			//audioThread = null;
		}
	}

	private void DBstopVideo() {
		if(DBplayingThread != null) {
			if(DBplayStatus!=2) {
			DBplayingThread.interrupt();}
			//audioThread.interrupt();
			//playSound.stop();
			DBplayingThread = null;
			//audioThread = null;
			slider.setValue(0);
		} else {
			DBcurrentFrameNum = 0;
			videoEndJLable.setIcon(new ImageIcon(imagesResult.get(0)));
		}
	}
	
	
	
	
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
	    int w = img.getWidth();  
	    int h = img.getHeight();  
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
	    Graphics2D g = dimg.createGraphics();  
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
	    g.dispose();  
	    return dimg;  
	}  
	
	
	
	
	
	
	class MyListener implements WindowListener{
		public void windowClosing(WindowEvent e) {
			System.exit(0);
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	


}
