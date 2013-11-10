package GUI;

import javax.swing.JPanel;

public class JInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private boolean lock=false;
	
	public synchronized boolean isLocked(){
		return lock;
	}
	public synchronized void setLocked(boolean s){
		lock=s;
	}
}
