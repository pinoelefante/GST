package GUI.player;

import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.Interfaccia;

import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

public class GSTFullScreenWindows implements FullScreenStrategy {
	private JFrame frame;
	private boolean isFullscreen=false;
	private Container parent;
	private JPanel panel_player;
	private Thread t_mouse_move;
	
	public GSTFullScreenWindows(){
		if(frame==null){
			frame=new JFrame();
			frame.setUndecorated(true);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new WindowListener() {
				public void windowOpened(WindowEvent arg0) {}
				public void windowIconified(WindowEvent arg0) {}
				public void windowDeiconified(WindowEvent arg0) {}
				public void windowDeactivated(WindowEvent arg0) {}
				public void windowClosing(WindowEvent arg0) {
					exitFullScreenMode();
				}
				public void windowClosed(WindowEvent arg0) {}
				public void windowActivated(WindowEvent arg0) {}
			});
			frame.setFocusable(true);
			frame.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					System.out.println("focus perso");
					if(!Player.getInstance().getControls().isVisible()){
						frame.setState(JFrame.NORMAL);
						frame.toFront();
						frame.requestFocus();
					}
				}
				public void focusGained(FocusEvent arg0) {
					System.out.println("Focus acquisito");
				}
			});
			frame.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent arg0) {}
				public void keyReleased(KeyEvent arg0) {}
				
				public void keyPressed(KeyEvent arg0) {
					Player p=Player.getInstance();
					switch(arg0.getKeyCode()){
						case KeyEvent.VK_SPACE:
							Player.getInstance().pause();
							break;
						case KeyEvent.VK_F:
						case KeyEvent.VK_ESCAPE:
							exitFullScreenMode();
							break;
						case KeyEvent.VK_P:
							JPanel panel=p.getPlaylistPanel();
							panel.setVisible(!panel.isVisible());
							break;
						case KeyEvent.VK_LEFT:
							p.skip(-5000);
							break;
						case KeyEvent.VK_RIGHT:
							p.skip(5000L);
							break;
					}
				}
			});
			frame.requestFocusInWindow();
			frame.requestFocus();
		}
	}
	@Override
	public void enterFullScreenMode() {
		float current_position=Player.getInstance().getPosition();
		if(current_position<0){
			System.out.println("Current position:"+current_position);
			return;
		}
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		panel_player=Player.getInstance().getPanel();
		parent=panel_player.getParent();
		if(parent!=null)
			parent.remove(panel_player);
		boolean isPlaying=Player.getInstance().isPlaying();
		Player.getInstance().stop();
		frame.add(panel_player);
		frame.setVisible(true);
		Player.getInstance().play();
		Player.getInstance().setPosition(current_position);
		try {
			if(!isPlaying){
				Thread.sleep(500L);
				Player.getInstance().pause();
				Player.getInstance().setPosition(current_position);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		isFullscreen=true;
		Interfaccia.getInterfaccia().setVisible(false);
		Player.getInstance().setPlaylistVisibile(false);
		
		t_mouse_move=new MouseMove();
		t_mouse_move.start();
	}

	@Override
	public void exitFullScreenMode() {
		if(parent!=null){
			frame.remove(panel_player);
			panel_player.setBounds(0, 0, 739, 277);
			parent.add(panel_player);
		}
		float current_position=Player.getInstance().getPosition();
		boolean isPlaying=Player.getInstance().isPlaying();
		Player.getInstance().stop();
		if(current_position>=0){
			Player.getInstance().play();
			Player.getInstance().setPosition(current_position);
			try {
				if(!isPlaying){
					Thread.sleep(500L);
					Player.getInstance().pause();
					Player.getInstance().setPosition(current_position);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		frame.setVisible(false);
		isFullscreen=false;
		Interfaccia.getInterfaccia().setVisible(true);
		Player.getInstance().setPlaylistVisibile(true);
		Player.getInstance().getControls().setVisible(true);
		t_mouse_move.interrupt();
	}

	@Override
	public boolean isFullScreenMode() {
		return isFullscreen;
	}
	class MouseMove extends Thread {
		private Point lastPoint;
		private int checkCount=0; //1 secondo = 5 checks
		public MouseMove(){
			lastPoint=MouseInfo.getPointerInfo().getLocation();
		}
		public void run(){
			try {
				while (true) {
					Point p=MouseInfo.getPointerInfo().getLocation();
					if(p.x==lastPoint.x && p.y==lastPoint.y){
						checkCount++;
					}
					else {
						checkCount=0;
						lastPoint=p;
					}
					
					if(checkCount>=25){
						Player.getInstance().getControls().setVisible(false);
						frame.requestFocus();
					}
					else {
						Player.getInstance().getControls().setVisible(true);
						//frame.requestFocus();
					}
					sleep(200L);
				}
			}
			catch (InterruptedException e) {}
		}
	}
}
