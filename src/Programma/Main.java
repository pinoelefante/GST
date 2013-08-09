package Programma;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import SerieTV.GestioneSerieTV;
import SerieTV.ThreadRicercaAutomatica;

import Database.Database;
import GUI.FrameLoading;
import GUI.Interfaccia;
import GUI.Interfaccia2;
import GUI.Language;

public class Main {
	public static Interfaccia					frame;
	public static ThreadAggiornamentiSoftware	thread_update		= new ThreadAggiornamentiSoftware(false);
	public static ThreadRicercaAutomatica		thread_autosearch	= new ThreadRicercaAutomatica();
	public static FrameLoading 					fl;
	
	public static void avviaThreadRicercaAutomatica() {
		if (!thread_autosearch.isAlive()) {
			thread_autosearch = new ThreadRicercaAutomatica();
			thread_autosearch.start();
		}
	}
	static Interfaccia2 frame2;
	private static Interfaccia2 GUIframe;
	public static void main(String[] args) {
		
		try{	
			
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
			
			fl.settext("Settaggi base");
			Settings.baseSettings();
			fl.setprog(++i);
			
			
			fl.settext("Controllo dipendenze");
			Prerequisiti.checkDipendenze();
			fl.setprog(++i);
			
			
			fl.settext("Connessione al database");
			Database.Connect();
			fl.setprog(++i);
			
			
			fl.settext("Caricamento impostazioni");
			Settings.CaricaSetting();
			fl.setprog(++i);
			
			fl.settext("Impostando la lingua");
			Language.setLanguage(Settings.getLingua());
			fl.setprog(++i);
		
			fl.settext("Eliminazione dump files");
			OperazioniFile.dumpfileclean();
			fl.setprog(++i);
			
			
			fl.settext("Applicando aggiornamenti");
			Update.start();
			fl.setprog(++i);
			
			fl.settext("Controllo aggiornamenti");
			thread_update.start();
			fl.setprog(++i);
			thread_update.join();
			/*
			fl.settext("Scaricando lista serie");
			GestioneSerieTV.Showlist();
			fl.setprog(++i);
			*/
			fl.settext("Avvio interfaccia grafica");
			fl.setprog(++i);
			
			if (Settings.isDownloadAutomatico()) {
				avviaThreadRicercaAutomatica();
			}
			
			fl.chiudi();
			
			if(Settings.getGUI()==1){
				Interfaccia.createPanel();
				if(Settings.getNumeroAvvii()>0 && (Settings.getNumeroAvvii()%30)==0)
					Interfaccia.donazione_visualizza_frame();
			}
			
			else{
				GUIframe = new Interfaccia2();
				GUIframe.setVisible(true);
				GestioneSerieTV.carica_serie_database();
				GUIframe.init();
				GestioneSerieTV.Showlist();
				GUIframe.reloadSerieDisponibili();
			}
				
			
			
			if(Settings.isRicercaSottotitoli()){
				GestioneSerieTV.getSubManager().avviaRicercaAutomatica();
			}
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(GUIframe, e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
}
