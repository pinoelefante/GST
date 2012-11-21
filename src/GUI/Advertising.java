package GUI;

/*
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.swing.SwingUtilities;

*/
public class Advertising {
	public static final String	url_ads_alter	= "http://pinoelefante.altervista.org/ads.html";
/*
	private static Robot		mouse;
	private static final int	X_OFF			= 350;
	private static final int	Y_OFF			= 550;
	
	private static int			browserclick	= 0;
	private static String 		CurrentIP;
	public final static long	ONE_DAY=86400000;
	private static Thread t_adv;

	

	public static void instance() {
		class ThreadAdv extends Thread{
			public int genera(){
				Random rnd=new Random(GregorianCalendar.getInstance().getTimeInMillis());
				return rnd.nextInt(10);
			}
			public void run(){
				int rnd=genera();
				System.out.println("Random="+rnd);
				rnd=1;
				if(rnd<3){
					try {
						//while(true){
							sleep(5000);
							//if(!Interfaccia.isHidden){
								Advertising.sendClick();
								//break;
							/*}
							else{
								
							}*/
						//}
/*					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		try {
			mouse = new Robot();
			CurrentIP = currentIP();
			Thread t=new ThreadAdv();
			t.start();
		}
		catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	public static void sendClick() {
		class ThreadClick extends Thread {
			public boolean	click	= false;

			public void run() {
				try {
					if(CurrentIP.compareTo(Settings.LastIP_Alter)==0  &&  (new GregorianCalendar()).getTimeInMillis()-Settings.LastClickTime_Alter<ONE_DAY){
						//long spent=(new GregorianCalendar()).getTimeInMillis()-Settings.LastClickTime_Alter;
						//System.out.println("alter-Mancano: "+((ONE_DAY-spent)/1000));
						return;
					}
					
					if (browserclick > 15){
						browserclick=0;
						return;
					}
					if (Interfaccia.isHidden){
						while(Interfaccia.isHidden)
							sleep(300);
					}
					SwingUtilities.invokeLater(new Runnable(){
						public void run() {
							if(!isClickedAlter()){
								Point location_start = MouseInfo.getPointerInfo().getLocation();
								mouse.mouseMove(Interfaccia.frame.getX() + X_OFF, Interfaccia.frame.getY() + Y_OFF);
								mouse.mousePress(InputEvent.BUTTON1_DOWN_MASK);
								mouse.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
								mouse.mouseMove(Interfaccia.frame.getX() + X_OFF+15, Interfaccia.frame.getY() + Y_OFF);
								mouse.mouseMove(location_start.x, location_start.y);
							}
							else{
								Settings.LastIP_Alter=CurrentIP;
								Settings.LastClickTime_Alter=(new GregorianCalendar()).getTimeInMillis();
								click = true;
							}
							addBrowserClick();
						}

						public boolean isClickedAlter() {
							String cws = ThreadPanelBrowser.getBrowser().getResourceLocation();
							if (cws.compareToIgnoreCase(url_ads_alter) == 0)
								return false;
							if(cws.compareTo("about:blank")==0)
								return false;
							return true;
						}
					});
					sleep(1200);
					if (!click){
						t_adv=null;
						sendClick();
					}
				}
				catch (InterruptedException e) {
				}
			}
		}
		ThreadClick tc = new ThreadClick();
		
		if(t_adv==null)
			t_adv=tc;
		if(!t_adv.isAlive())
			tc.start();
		
	}

	public static void addBrowserClick() {
		browserclick += 1;
	}

*/
/*
	public static void loadArgomenti(){ 
		argomenti_chitika.add("Web series");
		argomenti_chitika.add("House"); argomenti_chitika.add("Travel");
		argomenti_chitika.add("Iceland"); argomenti_chitika.add("Employment");
		argomenti_chitika.add("Community Services");
		argomenti_chitika.add("Health");
		argomenti_chitika.add("Handyman Repairs");
		argomenti_chitika.add("Legal Services");
		argomenti_chitika.add("Insurance");
		argomenti_chitika.add("Shopping Centers");
		argomenti_chitika.add("Moving Services");
		argomenti_chitika.add("Car Repair"); argomenti_chitika.add("Remodeling");
		for(int i=0;i<Main.favoriti.getNumSerieTV();i++)
			argomenti_chitika.add(Main.favoriti.getSerieTVAtIndex(i).getNomeSerie());
	 }
*/
	  
/*
	public static String getArgomentoRandom(){ int
		rand=(int)(Math.random()*100); System.out.println(rand);
	 	if(rand>=argomenti_chitika.size()) rand=argomenti_chitika.size()-1;
	 		return argomenti_chitika.get(rand); }
 */
/*

	public static String currentIP(){
		String result = null;
		InputStream in = null;
	    try {
	    	URLConnection conn = new URL("http://pinoelefante.altervista.org/misc/ip.php").openConnection();
	    	int length = Integer.valueOf(conn.getHeaderField("Content-Length"));
	    	byte[] buf = new byte[length];
	    	in = conn.getInputStream();
	    	in.read(buf);
	    	result =  new String(buf);
	    }
	    catch(IOException e) {
	    	e.printStackTrace();
	    }
	    finally {
	    	if (in != null) 
	    		try { 
	    			in.close(); 
	    		} 
	    		catch(IOException e) {  }
	     }
	     return result;
	}

*/
}
