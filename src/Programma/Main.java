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
			if(Settings.isWindows())
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
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
			
			
			fl.settext("Caricando serie dal database");
			fl.setprog(++i);
			GestioneSerieTV.instance();
			//GestioneSerieTV2.carica_serie_database();
			
			
			/* TODO
			fl.settext("Controllo aggiornamenti");
			thread_update.start();
			fl.setprog(++i);
			thread_update.join();
			*/
			/*
			fl.settext("Scaricando lista serie");
			GestioneSerieTV.Showlist();
			fl.setprog(++i);
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
			//GestioneSerieTV2.caricaElencoSerieOnline();
			//GUIframe.reloadSerieDisponibili();
				
			/*
			if(Settings.isRicercaSottotitoli()){
				GestioneSerieTV2.getSubManager().avviaRicercaAutomatica();
			}
			*/
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(GUIframe, e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
}
