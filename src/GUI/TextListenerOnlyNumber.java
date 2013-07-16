package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

public class TextListenerOnlyNumber extends KeyAdapter {
	private String old="";
	private JTextComponent c;
	
	public TextListenerOnlyNumber(JTextComponent c){
		this.c=c;
	}
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getExtendedKeyCode()==KeyEvent.VK_DELETE || arg0.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE){
			if(!old.isEmpty()){
				old=old.substring(0, old.length()-1);
				System.out.println(old);
			}
			return;
		}
		
		try {
			Integer.parseInt(arg0.getKeyChar()+"");
			old=old.concat(""+arg0.getKeyChar());
		}
		catch(NumberFormatException e){}
	}
	public void keyReleased(KeyEvent arg){
		c.setText(old);
	}
}
