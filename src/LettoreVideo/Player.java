package LettoreVideo;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;

import Programma.ManagerException;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Player {
	private static Playlist playlist;
	private boolean isLinked=false;
	//TODO modificare
	private final static String DEFAULT_PATH="C:\\vlc";
	private static EmbeddedMediaPlayerComponent vlc;
	private JComponent default_parent;
	private boolean isFullscreen;
	
	public Player(JComponent f){
		default_parent=f;
		instance();
	}
	
	private boolean search_path=false;
	private void locateVLC() throws RuntimeException{
		if(!search_path){
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), DEFAULT_PATH);
			search_path=true;
		}
		
		try {
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
			isLinked=true;
		}
		catch(Exception e){
			isLinked=false;
			throw new RuntimeException("Non � stato possibile trovare la libreria libvlc\n"+e.getMessage());
		}
	}
	public void instance(){
		if(playlist==null)
			playlist=new Playlist();
		
		if(!isLinked()){
			try{
				locateVLC();
				vlc=new EmbeddedMediaPlayerComponent();
			}
			catch(RuntimeException e){
				ManagerException.registraEccezione(e);
				return;
			}
		}
	}
	public boolean isLinked(){
		return isLinked;
	}
	public EmbeddedMediaPlayerComponent getPlayerPane(){
		return vlc;
	}
	public void play(){
		if(vlc.getMediaPlayer().isPlaying()){
			togglePause();
			return;
		}
		if(vlc.getMediaPlayer().isPlayable()){
			vlc.getMediaPlayer().play();
		}
		else {
			play(playlist.getNext());
		}
	}
	public void play(String s){
		if(s!=null){
			if(new File(s).exists()){
				//System.out.println(s);
				vlc.getMediaPlayer().playMedia(s);
			}
		}
	}
	public void setBounds(int x, int y, int wid, int hei){
		vlc.setBounds(x, y, wid, hei);
		default_player=new Rectangle(x, y, wid, hei);
	}
	public void stop(){
		if(vlc.getMediaPlayer().isPlaying())
			vlc.getMediaPlayer().stop();
	}
	public void release(){
		vlc.release();
	}
	public void togglePause(){
		if(vlc.getMediaPlayer().isPlaying())
			vlc.getMediaPlayer().pause();
		else
			vlc.getMediaPlayer().play();
	}
	
	private Rectangle default_player;
	private JFrame fullscreen_frame;
	
	private float last_seen;
	public void set_fullscreen(){
		if(fullscreen_frame==null){
			fullscreen_frame=new JFrame();
			fullscreen_frame.setAlwaysOnTop(true);
			fullscreen_frame.setUndecorated(true);
			fullscreen_frame.setVisible(true);
			fullscreen_frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			fullscreen_frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e){
					last_seen=vlc.getMediaPlayer().getPosition();
					stop();
					remove_fullscreen();
					fullscreen_frame=null;
				}
				public void windowClosed(WindowEvent e){
					last_seen=vlc.getMediaPlayer().getPosition();
					stop();
					remove_fullscreen();
					fullscreen_frame=null;
				}
			});
			fullscreen_frame.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent arg0) {}
				public void keyReleased(KeyEvent arg0) {}
				public void keyPressed(KeyEvent arg0) {
					if(arg0.getExtendedKeyCode()==KeyEvent.VK_F || arg0.getExtendedKeyCode()==KeyEvent.VK_ESCAPE){ //Tasto F e ESC - chiude finestra schermo intero
						fullscreen_frame.dispose();
					}
					else if(arg0.getExtendedKeyCode()==KeyEvent.VK_SPACE)
						play();
				}
			});
		}
		
		if(vlc.getMediaPlayer().isPlaying()){
			last_seen=vlc.getMediaPlayer().getPosition();
			stop();
		}
		fullscreen_frame.setVisible(true);
		vlc.getParent().remove(vlc);
		vlc.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		fullscreen_frame.add(vlc);
		vlc.getMediaPlayer().play();
		vlc.getMediaPlayer().setPosition(last_seen);
		isFullscreen=true;
	}
	public void remove_fullscreen(){
		fullscreen_frame.remove(vlc);
		vlc.setBounds(default_player);
		default_parent.add(vlc);
		vlc.getMediaPlayer().play();
		vlc.getMediaPlayer().setPosition(last_seen);
		isFullscreen=false;
	}
	public void addToPlaylist(String p){
		playlist.addItem(p);
	}
	public boolean isFullscreen(){
		return isFullscreen;
	}
	
	public void next(){
		stop();
		if(vlc.getMediaPlayer().isPlaying()){
			play(playlist.getItem(playlist.getCurrentItem()));
		}
		else {
			play(playlist.getItem(playlist.getCurrentItem()+1));
		}
	}
	public void prev(){
		stop();
		if(vlc.getMediaPlayer().isPlaying()){
			int index=playlist.getCurrentItem()-2;
			if(index<0)
				index=playlist.getLastItem();
			play(playlist.getItem(index));
		}
		else {
			int index=playlist.getCurrentItem()-1;
			if(index<0)
				index=playlist.getLastItem();
			play(playlist.getItem(index));
			
		}
	}
}
