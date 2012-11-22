package GUI;

import java.io.File;
import java.io.IOException;

import Programma.Settings;

public class Player {
	private static String stringa;
	private static Thread player;
	
	public static void play(String file){
		stringa=file;
		
		class PlayerVideo extends Thread{
			public void run(){
				String file_r=Settings.getDirectoryDownload()+File.separator+stringa;
				try {
					Runtime.getRuntime().exec("\""+Settings.getVLCPath()+"\""+" "+"\""+file_r+"\"");
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(player!=null)
			if(player.isAlive())
				player.interrupt();
		player=new PlayerVideo();
		player.start();
	}

	/*
	SwingUtilities.invokeLater(new Runnable() { 
		JVLCPlayer vlc; 
		public void run() { 
			NativeInterface.open(); 
			vlc=new JVLCPlayer(NSComponentOptions.destroyOnFinalization()); 
			
			JFrame frame=new JFrame(); 
			frame.setSize(800, 600); 
			frame.setVisible(true);
			frame.add(vlc);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			vlc.load("D:\\Multimedia\\Video\\Film\\SD\\visti\\Splendor.avi");;

			if(!vlc.getVLCPlaylist().isPlaying()){
				frame.dispose();
				try {
					Runtime.getRuntime().exec("\""+Settings.VLCPath+"\""+" "+"\""+"D:\\Multimedia\\Video\\Film\\SD\\visti\\Splendor.avi"+"\"");
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			frame.addWindowListener(new WindowListener() { 
				public void windowOpened(WindowEvent arg0) {} 
				public void windowIconified(WindowEvent arg0) {} 
				public void windowDeiconified(WindowEvent arg0) {} 
				public void windowDeactivated(WindowEvent arg0) {} 
				public void windowClosing(WindowEvent arg0) { 
					vlc.getVLCPlaylist().stop();
					vlc.getVLCPlaylist().clear();
				} 
				public void windowClosed(WindowEvent arg0) {} 
				public void windowActivated(WindowEvent arg0) { } 
			}); 
		} 
	});
		*/
}