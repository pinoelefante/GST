/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009, 2010, 2011, 2012, 2013, 2014 Caprica Software Limited.
 */
package GUI.player;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.AudioOutput;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

/**
 * Simple test harness creates an AWT Window and plays a video.
 * <p>
 * This is <strong>very</strong> basic but should give you an idea of how to build a media player.
 * <p>
 * In case you didn't realise, you can press F12 to toggle the visibility of the player controls.
 * <p>
 * Java7 provides -Dsun.java2d.xrender=True or -Dsun.java2d.xrender=true, might give some general
 * performance improvements in graphics rendering.
 */
public class Player extends VlcjTest {
    private final JPanel mainFrame;
    private final Canvas videoSurface;
    private final JPanel controlsPanel;
    private static Player instance;

    private MediaPlayerFactory mediaPlayerFactory;

    private EmbeddedMediaPlayer mediaPlayer;
	private PanelPlaylist playlistPanel;

    
    public static void main(final String[] args) throws Exception {
        LibVlc libVlc = LibVlcFactory.factory().create();

        Logger.info("  version: {}", libVlc.libvlc_get_version());
        Logger.info(" compiler: {}", libVlc.libvlc_get_compiler());
        Logger.info("changeset: {}", libVlc.libvlc_get_changeset());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Player();
            }
        });
    }
    public static synchronized Player getInstance(){
    	if(instance==null)
    		instance=new Player();
    	return instance;
    }
    public JPanel getPanel(){
    	return mainFrame;
    }
    private Player() {
    	playlist=new ArrayList<ItemPlaylist>();
        
        videoSurface = new Canvas();

        videoSurface.setBackground(Color.black);
        //videoSurface.setSize(800, 600); // Only for initial layout

        // Since we're mixing lightweight Swing components and heavyweight AWT
        // components this is probably a good idea
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        TestPlayerMouseListener mouseListener = new TestPlayerMouseListener();
        videoSurface.addMouseListener(mouseListener);
        videoSurface.addMouseMotionListener(mouseListener);
        videoSurface.addMouseWheelListener(mouseListener);
        videoSurface.addKeyListener(new TestPlayerKeyListener());

        List<String> vlcArgs = new ArrayList<String>();

        vlcArgs.add("--no-snapshot-preview");
        vlcArgs.add("--quiet");
        vlcArgs.add("--quiet-synchro");
        vlcArgs.add("--intf");
        vlcArgs.add("dummy");

        // Special case to help out users on Windows (supposedly this is not actually needed)...
        // if(RuntimeUtil.isWindows()) {
        // vlcArgs.add("--plugin-path=" + WindowsRuntimeUtil.getVlcInstallDir() + "\\plugins");
        // }
        // else {
        // vlcArgs.add("--plugin-path=/home/linux/vlc/lib");
        // }

        // vlcArgs.add("--plugin-path=" + System.getProperty("user.home") + "/.vlcj");

        Logger.debug("vlcArgs={}", vlcArgs);

        mainFrame = new JPanel(new BorderLayout());
        mainFrame.setBounds(0, 0, 739, 277);
        //mainFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        
        mediaPlayerFactory = new MediaPlayerFactory(vlcArgs.toArray(new String[vlcArgs.size()]));
        mediaPlayerFactory.setUserAgent("vlcj test player");

        List<AudioOutput> audioOutputs = mediaPlayerFactory.getAudioOutputs();
        Logger.debug("audioOutputs={}", audioOutputs);

        //FullScreenStrategy fullScreenStrategy = new Win32FullScreenStrategy(Interfaccia.getInterfaccia());
        FullScreenStrategy fullScreenStrategy=new GSTFullScreenWindows();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));
        mediaPlayer.setPlaySubItems(true);

        mediaPlayer.setEnableKeyInputHandling(false);
        mediaPlayer.setEnableMouseInputHandling(true);

        controlsPanel = new PlayerControlsPanel(mediaPlayer);
        playlistPanel = new PanelPlaylist();

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setBackground(Color.black);
        mainFrame.add(videoSurface, BorderLayout.CENTER);
        mainFrame.add(controlsPanel, BorderLayout.SOUTH);
        mainFrame.add(playlistPanel, BorderLayout.EAST);
        //mainFrame.setJMenuBar(buildMenuBar());
        /*
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                Logger.debug("windowClosing(evt={})", evt);

                if(mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                if(mediaPlayerFactory != null) {
                    mediaPlayerFactory.release();
                    mediaPlayerFactory = null;
                }
            }
        });
        */

        // Global AWT key handler, you're better off using Swing's InputMap and
        // ActionMap with a JFrame - that would solve all sorts of focus issues too
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if(event instanceof KeyEvent) {
                    KeyEvent keyEvent = (KeyEvent)event;
                    if(keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                        if(keyEvent.getKeyCode() == KeyEvent.VK_F12) {
                            controlsPanel.setVisible(!controlsPanel.isVisible());
                            mainFrame.invalidate();
                            mainFrame.validate();
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_A) {
                            mediaPlayer.setAudioDelay(mediaPlayer.getAudioDelay() - 50000);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_S) {
                            mediaPlayer.setAudioDelay(mediaPlayer.getAudioDelay() + 50000);
                        }
                        // else if(keyEvent.getKeyCode() == KeyEvent.VK_N) {
                        // mediaPlayer.nextFrame();
                        // }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_1) {
                            mediaPlayer.setTime(60000 * 1);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_2) {
                            mediaPlayer.setTime(60000 * 2);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_3) {
                            mediaPlayer.setTime(60000 * 3);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_4) {
                            mediaPlayer.setTime(60000 * 4);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_5) {
                            mediaPlayer.setTime(60000 * 5);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_6) {
                            mediaPlayer.setTime(60000 * 6);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_7) {
                            mediaPlayer.setTime(60000 * 7);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_8) {
                            mediaPlayer.setTime(60000 * 8);
                        }
                        else if(keyEvent.getKeyCode() == KeyEvent.VK_9) {
                            mediaPlayer.setTime(60000 * 9);
                        }
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);

        mainFrame.setVisible(true);

        mediaPlayer.addMediaPlayerEventListener(new TestPlayerMediaPlayerEventListener());
     
        // This might be useful
        enableMousePointer(true);
    }

    private final class TestPlayerMediaPlayerEventListener extends MediaPlayerEventAdapter {
        @Override
        public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
            Logger.debug("mediaChanged(mediaPlayer={},media={},mrl={})", mediaPlayer, media, mrl);
        }

        @Override
        public void finished(MediaPlayer mediaPlayer) {
            Logger.debug("finished(mediaPlayer={})", mediaPlayer);
            next();
        }

        @Override
        public void paused(MediaPlayer mediaPlayer) {
            Logger.debug("paused(mediaPlayer={})", mediaPlayer);
        }

        @Override
        public void playing(MediaPlayer mediaPlayer) {
            Logger.debug("playing(mediaPlayer={})", mediaPlayer);
            MediaDetails mediaDetails = mediaPlayer.getMediaDetails();
            Logger.info("mediaDetails={}", mediaDetails);
        }

        @Override
        public void stopped(MediaPlayer mediaPlayer) {
            Logger.debug("stopped(mediaPlayer={})", mediaPlayer);
        }

        @Override
        public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
            Logger.debug("videoOutput(mediaPlayer={},newCount={})", mediaPlayer, newCount);
            if(newCount == 0) {
                return;
            }

            MediaDetails mediaDetails = mediaPlayer.getMediaDetails();
            Logger.info("mediaDetails={}", mediaDetails);

            MediaMeta mediaMeta = mediaPlayer.getMediaMeta();
            Logger.info("mediaMeta={}", mediaMeta);

            final Dimension dimension = mediaPlayer.getVideoDimension();
            Logger.debug("dimension={}", dimension);
            if(dimension != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        videoSurface.setSize(dimension);
                        //mainFrame.pack();
                    }
                });
            }

            // You can set a logo like this if you like...
            File logoFile = new File("./etc/vlcj-logo.png");
            if(logoFile.exists()) {
                mediaPlayer.setLogoFile(logoFile.getAbsolutePath());
                mediaPlayer.setLogoOpacity(0.5f);
                mediaPlayer.setLogoLocation(10, 10);
                mediaPlayer.enableLogo(true);
            }

            // Demo the marquee
            mediaPlayer.setMarqueeText("vlcj java bindings for vlc");
            mediaPlayer.setMarqueeSize(40);
            mediaPlayer.setMarqueeOpacity(95);
            mediaPlayer.setMarqueeColour(Color.white);
            mediaPlayer.setMarqueeTimeout(5000);
            mediaPlayer.setMarqueeLocation(50, 120);
            mediaPlayer.enableMarquee(true);

            // Not quite sure how crop geometry is supposed to work...
            //
            // Assertions in libvlc code:
            //
            // top + height must be less than visible height
            // left + width must be less than visible width
            //
            // With DVD source material:
            //
            // Reported size is 1024x576 - this is what libvlc reports when you call
            // get video size
            //
            // mpeg size is 720x576 - this is what is reported in the native log
            //
            // The crop geometry relates to the mpeg size, not the size reported
            // through the API
            //
            // For 720x576, attempting to set geometry to anything bigger than
            // 719x575 results in the assertion failures above (seems like it should
            // allow 720x576) to me

            // mediaPlayer.setCropGeometry("4:3");
        }

        @Override
        public void error(MediaPlayer mediaPlayer) {
            Logger.debug("error(mediaPlayer={})", mediaPlayer);
        }

        @Override
        public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
            Logger.debug("mediaSubItemAdded(mediaPlayer={},subItem={})", mediaPlayer, subItem);
        }

        @Override
        public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
            Logger.debug("mediaDurationChanged(mediaPlayer={},newDuration={})", mediaPlayer, newDuration);
        }

        @Override
        public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
            Logger.debug("mediaParsedChanged(mediaPlayer={},newStatus={})", mediaPlayer, newStatus);
        }

        @Override
        public void mediaFreed(MediaPlayer mediaPlayer) {
            Logger.debug("mediaFreed(mediaPlayer={})", mediaPlayer);
        }

        @Override
        public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
            Logger.debug("mediaStateChanged(mediaPlayer={},newState={})", mediaPlayer, newState);
        }

        @Override
        public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
            Logger.debug("mediaMetaChanged(mediaPlayer={},metaType={})", mediaPlayer, metaType);
        }
    }

    /**
     *
     *
     * @param enable
     */
    private void enableMousePointer(boolean enable) {
        Logger.debug("enableMousePointer(enable={})", enable);
        if(enable) {
            videoSurface.setCursor(null);
        }
        else {
            Image blankImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            videoSurface.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(blankImage, new Point(0, 0), ""));
        }
    }

    /**
     *
     */
    private final class TestPlayerMouseListener extends MouseAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            Logger.trace("mouseMoved(e={})", e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Logger.debug("mousePressed(e={})", e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Logger.debug("mouseReleased(e={})", e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Logger.debug("mouseClicked(e={})", e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            Logger.debug("mouseWheelMoved(e={})", e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Logger.debug("mouseEntered(e={})", e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Logger.debug("mouseExited(e={})", e);
        }
    }

    /**
     *
     */
    private final class TestPlayerKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            Logger.debug("keyPressed(e={})", e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Logger.debug("keyReleased(e={})", e);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            Logger.debug("keyTyped(e={})", e);
        }
    }

	public void stop() {
		mediaPlayer.stop();
	}

	public void play() {
		if(mediaPlayer.isPlayable())
			mediaPlayer.play();
		else if(!playlist.isEmpty())
			mediaPlayer.playMedia(playlist.get(current_item_playlist).getPath());
	}

	public void next() {
		current_item_playlist++;
		if(current_item_playlist>=playlist.size())
			current_item_playlist=0;
		if(!playlist.isEmpty())
			mediaPlayer.playMedia(playlist.get(current_item_playlist).getPath());
	}

	public void prev() {
		if(current_item_playlist>0){
			current_item_playlist--;
			if(!playlist.isEmpty())
				mediaPlayer.playMedia(playlist.get(current_item_playlist).getPath());
		}
		
	}
	public void playItem(int index) {
		if(index<playlist.size() && index>=0 && !playlist.isEmpty()){
			mediaPlayer.playMedia(playlist.get(index).getPath());
			current_item_playlist=index;
		}
	};
	
	public ArrayList<String> getPlayList() {
		ArrayList<String> str=new ArrayList<>();
		for(ItemPlaylist i: playlist)
			str.add(i.getToShow());
		return str;
	}
	private ArrayList<ItemPlaylist> playlist;
	private int current_item_playlist;

	public void add(String path) {
		if(path==null){
			System.out.println("Il percorso è null");
			return;
		}
		playlist.add(new ItemPlaylist(path));
		if(playlistPanel!=null)
			playlistPanel.ridisegna();
	}
	public void remove(int index) {
		if(index<0 || index>=playlist.size())
			return;
		if(index==current_item_playlist){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
				playlist.remove(index);
				playItem(current_item_playlist);
			}
			else {
				playlist.remove(index);
			}
		}
		else if(index<current_item_playlist){
			playlist.remove(index);
			current_item_playlist--;
		}
		else {
			playlist.remove(index);
		}
		if(playlistPanel!=null)
			playlistPanel.ridisegna();
	}
	public void pause() {
		mediaPlayer.pause();
	}
	protected ArrayList<ItemPlaylist> getPlaylist(){
		return playlist;
	}
	public float getPosition(){
		if(mediaPlayer.isPlaying() || mediaPlayer.isPlayable())
			return mediaPlayer.getPosition();
		return -1;
	}
	public void setPosition(float p){
		mediaPlayer.setPosition(p);
	}
	public Dimension getVideoSurfaceSize(){
		return videoSurface.getSize();
	}
	public void setVideoSurfaceSize(Dimension d){
		videoSurface.setPreferredSize(d);
	}
	public void setPlaylistVisibile(boolean b) {
		playlistPanel.setVisible(b);		
	}
	public JPanel getControls(){
		return controlsPanel;
	}
	public boolean isPlaying(){
		return mediaPlayer.isPlaying();
	}
}
