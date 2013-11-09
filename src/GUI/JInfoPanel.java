package GUI;

import javax.swing.JPanel;

public class JInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private boolean lock=false;
	
	public boolean isLocked(){
		return lock;
	}
	public void setLocked(boolean s){
		lock=s;
	}
}
