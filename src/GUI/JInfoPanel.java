package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class JInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private boolean lock=false;
	private JWebBrowser browser_adv;
	private JPanel contentPane=new JPanel();
	
	public JInfoPanel(){
		super();
		super.setLayout(new BorderLayout());
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
    			browser_adv=new JWebBrowser(JWebBrowser.destroyOnFinalization());
    			browser_adv.setBarsVisible(false);
    			browser_adv.setStatusBarVisible(false);
    			browser_adv.setPreferredSize(new Dimension(320, 260));
    			browser_adv.navigate("https://www.google.it");
    			JInfoPanel.super.add(browser_adv, BorderLayout.SOUTH);
			}
		});
		super.add(contentPane, BorderLayout.CENTER);
	}
	public synchronized boolean isLocked(){
		return lock;
	}
	public synchronized void setLocked(boolean s){
		lock=s;
	}
	public void setLayout(LayoutManager l){
		if(contentPane!=null){
			contentPane.setLayout(l);
			System.out.println("Nuovo layout");
		}
		else {
			super.setLayout(l);
			System.out.println("Old layout");
		}
	}
	public Component add(Component c){
		contentPane.add(c);
		return c;
	}
	public void add(Component c, Object constr){
		contentPane.add(c, constr);
	}
	public void removeAll(){
		contentPane.removeAll();
	}
	
//TODO
// aggiungere web browser	
// sovrascrivere metodo removeall
// sovrascrivere il metodo add
}
