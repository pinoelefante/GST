package GUI;

import javax.swing.SwingUtilities;


import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.MozillaXPCOM.Mozilla;

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
				wb.setSize(700, 300);
				
				String web_site=Advertising.url_ads_alter;
				wb.navigate(web_site);
				
			}
		});
	}
	public static JWebBrowser getBrowser(){
		return wb;
	}
	
}
