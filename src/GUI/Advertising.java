package GUI;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class Advertising {
	private static JWebBrowser brow1, brow2;
	private static boolean b1_clicked, b2_clicked;
	private static String b1_last_ip="0.0.0.0", b1_current_ip, b2_last_ip="0.0.0.0", b2_current_ip;
	
	public static void setLastIPB1(String ip){
		b1_last_ip=ip;
	}
	public static void setLastIPB2(String ip){
		b2_last_ip=ip;
	}
	public static void setB1(JWebBrowser b){
		brow1=b;
	}
	public static void setB2(JWebBrowser b){
		brow2=b;
	}
	public static void sendClickB1(){
		//TODO add web browser listener
	}
	public static void sendClickB2(){
		
	}
}
