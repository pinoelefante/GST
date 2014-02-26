package GUI.player;

import java.awt.Container;
import java.awt.Toolkit;
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
		if(!isPlaying)
			Player.getInstance().pause();
		isFullscreen=true;
		Interfaccia.getInterfaccia().setVisible(false);
		Player.getInstance().setPlaylistVisibile(false);
	}

	@Override
	public void exitFullScreenMode() {
		if(parent!=null){
			frame.remove(panel_player);
			panel_player.setBounds(0, 0, 739, 277);
			parent.add(panel_player);
		}
		boolean isPlaying=Player.getInstance().isPlaying();
		float current_position=Player.getInstance().getPosition();
		Player.getInstance().stop();
		if(current_position>=0){
			Player.getInstance().play();
			Player.getInstance().setPosition(current_position);
			if(!isPlaying)
				Player.getInstance().pause();
		}
		frame.setVisible(false);
		isFullscreen=false;
		Interfaccia.getInterfaccia().setVisible(true);
		Player.getInstance().setPlaylistVisibile(true);
		Player.getInstance().getControls().setVisible(true);
	}

	@Override
	public boolean isFullScreenMode() {
		return isFullscreen;
	}
}
