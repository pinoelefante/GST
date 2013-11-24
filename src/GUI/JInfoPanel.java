package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class JInfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private boolean lock=false;
	private JWebBrowser browser_adv;
	private JPanel contentPane;
	
	public JInfoPanel(){
		super();
		contentPane=new JPanel();
		super.setLayout(new BorderLayout());
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				if (!NativeInterface.isOpen())
					NativeInterface.open();
    			browser_adv=new JWebBrowser(JWebBrowser.destroyOnFinalization());
    			browser_adv.setBarsVisible(false);
    			browser_adv.setStatusBarVisible(false);
    			browser_adv.setLocationBarVisible(false);
    			browser_adv.setPreferredSize(new Dimension(300, 260));
    			browser_adv.navigate("http://pinoelefante.altervista.org/ads.html");
    			JInfoPanel.super.add(browser_adv, BorderLayout.SOUTH);
    			Advertising.setB1(browser_adv);
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
		}
		else {
			super.setLayout(l);
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
}
