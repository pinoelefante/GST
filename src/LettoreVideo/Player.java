package LettoreVideo;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Programma.ManagerException;
import Programma.Settings;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Player {
	private static Playlist playlist;
	private boolean isLinked=false;
	private final static String DEFAULT_PATH=Settings.getCurrentDir()+"lib"+File.separator+"vlc"+File.separator+Settings.getOSName()+"-"+Settings.getVMArch();
	private static EmbeddedMediaPlayerComponent vlc;
	private JComponent default_parent;
	private JSlider current_time, volume;
	private JLabel current_time_label;
	private boolean isFullscreen;
	private Thread thread_play;
	
	public Player(JComponent f, Playlist p) throws Exception{
		default_parent=f;
		playlist=p;
		instance();
		current_time=new JSlider(0,0,0);
		volume=new JSlider(0, 100, 100);
		current_time_label=new JLabel("00:00:00");
	}
	
	private boolean search_path=false;
	private void locateVLC() throws Exception{
		System.out.println(DEFAULT_PATH);
		if(!search_path){
			//TODO modificare path per linux
			if(Settings.isLinux()){
				System.setProperty("VLC_PLUGIN_PATH", DEFAULT_PATH+File.separator+"vlc"+File.separator+"plugins");
			}
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), DEFAULT_PATH);
			search_path=true;
		}
		try {
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
			isLinked=true;
		}
		catch(UnsatisfiedLinkError e){
			e.printStackTrace();
			ManagerException.registraEccezione(e.getMessage());
			throw e;
		}
	}
	public JSlider getProgressSlider(){
		return current_time;
	}
	public JSlider getVolumeSlider(){
		return volume;
	}
	public JLabel getCurrentTimeLabel(){
		return current_time_label;
	}
	public void instance() throws Exception{		
		if(vlc==null){
			try{
				locateVLC();
				vlc=new EmbeddedMediaPlayerComponent();
			}
			catch(Exception e){
				throw e;
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
		if(vlc==null)
			return;
		if(vlc.getMediaPlayer().isPlaying()){
			vlc.getMediaPlayer().pause();
			if(thread_play!=null)
			thread_play.interrupt();
			thread_play=null;
			return;
		}
		if(vlc.getMediaPlayer().isPlayable()){
			vlc.getMediaPlayer().play();
			if(thread_play==null || thread_play.isInterrupted()){
    			thread_play=new ThreadPlay();
    			thread_play.start();
			}
		}
		else {
			play(playlist.getNext());
		}
	}
	public void play(String s){
		if(vlc==null)
			return;
		
		if(s!=null){
			if(new File(s).exists()){
				//System.out.println(s);
				if(thread_play!=null)
					thread_play.interrupt();
				vlc.getMediaPlayer().playMedia(s);
				thread_play=new ThreadPlay();
	    		thread_play.start();
			}
		}
	}
	public void setBounds(int x, int y, int wid, int hei){
		vlc.setBounds(x, y, wid, hei);
		default_player=new Rectangle(x, y, wid, hei);
	}
	public void stop(){
		if(vlc==null)
			return;
		if(vlc.getMediaPlayer().isPlayable()){
			vlc.getMediaPlayer().stop();
			if(thread_play!=null)
				thread_play.interrupt();
			thread_play=null;
			current_time.setMaximum(0);
			current_time_label.setText("00:00:00");
			current_time.setValue(0);
		}
	}
	public void release(){
		if(vlc!=null)
			vlc.release();
	}
	private Rectangle default_player;
	private JFrame fullscreen_frame;
	
	private float last_seen;
	public void set_fullscreen(){
		if(vlc==null)
			return;
		
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
					else if(arg0.getExtendedKeyCode()==KeyEvent.VK_SPACE) //Tasto SPAZIO - pausa/play
						play();
				}
			});
		}
		
		if(vlc.getMediaPlayer().isPlayable()){
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
		if(vlc==null)
			return;
		
		fullscreen_frame.remove(vlc);
		vlc.setBounds(default_player);
		default_parent.add(vlc);
		play();
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
		if(vlc==null)
			return;
		stop();
		if(vlc.getMediaPlayer().isPlaying()){
			play(playlist.getItem(playlist.getCurrentItem()));
		}
		else {
			play(playlist.getItem(playlist.getCurrentItem()+1));
		}
	}
	public void prev(){
		if(vlc==null)
			return;
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
	public Playlist getPlaylist() {
		return playlist;
	}
	public void addListener(){
		volume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				vlc.getMediaPlayer().setVolume(volume.getValue());
			}
		});
		current_time.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent arg0) {
				if(vlc.getMediaPlayer().isPlayable() || vlc.getMediaPlayer().isPlaying()){
    				int pos=current_time.getValue();
    				
    				float perc=(float)pos/current_time.getMaximum();
    				System.out.println("Posizione:"+perc+"%");
    				vlc.getMediaPlayer().setPosition(perc);
				}
				else {
					current_time.setValue(0);
				}
			}
		});
	}
	class ThreadPlay extends Thread {
		public void run() {
			try {
				while(!vlc.getMediaPlayer().isPlaying()){
					current_time.setMaximum((int) vlc.getMediaPlayer().getLength()/1000);
					sleep(10);
				}
    			while(true) {
    				long current_pos=vlc.getMediaPlayer().getTime();
    				int secondi_passati=(int) (current_pos/1000);
    				current_time.setValue(secondi_passati);
    				int minuti=secondi_passati/60;
    				int ore=minuti/60;
    				if(minuti>=60)
    					minuti=minuti-(ore*60);
    				int secondi=secondi_passati%60;
    				current_time_label.setText((ore<10?"0"+ore:ore)+":"+(minuti<10?"0"+minuti:minuti)+":"+(secondi<10?"0"+secondi:secondi));
    				
    				long next_pos=(current_pos/1000)*1000+1000;
    			
    				if(current_time.getValue()==current_time.getMaximum())
    					break;
    				sleep(next_pos-current_pos);
    			}
    			current_time.setValue(0);
    			current_time_label.setText("00:00:00");
    			//TODO inserire qui play next
			}
			catch (InterruptedException e) {}
		}
	}
}
