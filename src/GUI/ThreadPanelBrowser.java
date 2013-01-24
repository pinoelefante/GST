package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import Programma.ManagerException;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class ThreadPanelBrowser extends Thread {
	private static JWebBrowser wb=null;
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				NativeInterface.open();
				wb=new JWebBrowser(JWebBrowser.destroyOnFinalization());
				wb.setBarsVisible(false);
				wb.setStatusBarVisible(false);
				
				wb.setSize(300, 220);
				
				String web_site=Advertising.url_ads_alter;
				wb.navigate(web_site);
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				final JFrame frame=new JFrame();
				
				frame.setLayout(new BorderLayout());
				frame.setLocation(screen.width-300, -260);
				frame.setSize(300, 250);
				frame.setResizable(false);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				final JLabel count_down=new JLabel();
				frame.add(count_down, BorderLayout.SOUTH);
				frame.add(wb, BorderLayout.CENTER);
				
				class timer extends Thread {
					public void run(){
						try {
							int sleep_time=30;
							while(sleep_time>0){
								count_down.setText("  "+sleep_time+" secondi alla chiusura automatica");
								Thread.sleep(1000);
								sleep_time--;
							}
							
							frame.setVisible(false);
						}
						catch (InterruptedException e) {
							ManagerException.registraEccezione(e);
						}
					}
				}
				Thread t=new timer();
				t.start();
				
			}
		});
	}
	public static JWebBrowser getBrowser(){
		return wb;
	}
	
}
