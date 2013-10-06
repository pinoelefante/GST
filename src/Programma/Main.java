package Programma;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import SerieTV.GestioneSerieTV;
import Database.Database;
import GUI.FrameLoading;
import GUI.Interfaccia;

public class Main {
	//public static ThreadAggiornamentiSoftware	thread_update		= new ThreadAggiornamentiSoftware(false);
	//public static ThreadRicercaAutomatica		thread_autosearch	= new ThreadRicercaAutomatica();
	public static FrameLoading 					fl;
	/*
	public static void avviaThreadRicercaAutomatica() {
		if (!thread_autosearch.isAlive()) {
			thread_autosearch = new ThreadRicercaAutomatica();
			thread_autosearch.start();
		}
	}
	*/
	static Interfaccia frame2;
	private static Interfaccia GUIframe;
	public static void main(String[] args) {
		
		Settings.baseSettings();
		try{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			
			if(Settings.isWindows())
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			else if(Settings.isLinux())
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			
			
			fl=new FrameLoading();
			fl.start();
			try {
				fl.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				ManagerException.registraEccezione(e);
			}
			int i=0;
			
			fl.settext("Controllo dipendenze");
			Prerequisiti.checkDipendenze();
			fl.setprog(++i);
			
			
			fl.settext("Connessione al database");
			Database.Connect();
			fl.setprog(++i);
			Runtime.getRuntime().addShutdownHook(new Thread(){
				public void run(){
					Database.rebuildDB();
				}
			});
			
			
			fl.settext("Caricamento impostazioni");
			Settings.CaricaSetting();
			fl.setprog(++i);
		
			fl.settext("Eliminazione dump files");
			OperazioniFile.dumpfileclean();
			fl.setprog(++i);
			
			
			fl.settext("Applicando aggiornamenti");
			Update.start();
			fl.setprog(++i);
			
			String[] query={
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T1\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T2\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T3\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T4\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T5\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T6\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T7\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T8\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T9\""
			};
			for(int j=0;j<query.length;j++)
				Database.updateQuery(query[j]);
			
			
			fl.settext("Caricando serie dal database");
			fl.setprog(++i);
			GestioneSerieTV.instance();
			
			
			/* TODO controllo aggiornamenti
			fl.settext("Controllo aggiornamenti");
			thread_update.start();
			fl.setprog(++i);
			thread_update.join();
			*/
			
			fl.settext("Avvio interfaccia grafica");
			fl.setprog(++i);
			
			/*
			if (Settings.isDownloadAutomatico()) {
				avviaThreadRicercaAutomatica();
			}
			*/
			
			fl.chiudi();
			
			FileManager.instance();
			
			GUIframe = new Interfaccia();
			GUIframe.setVisible(true);
			GUIframe.init();
			
			Thread subThread=new Thread(new Runnable() {
				public void run() {
					if(Settings.isRicercaSottotitoli())
						GestioneSerieTV.getSubManager().avviaRicercaAutomatica();
				}
			});
			subThread.start();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(GUIframe, e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
}
