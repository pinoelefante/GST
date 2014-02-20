package Programma;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import SerieTV.GestioneSerieTV;
import SerieTV.ThreadRicercaAutomatica;
import Database.Database;
import GUI.Advertising;
import GUI.FrameLoading;
import GUI.Interfaccia;
import InfoManager.TheTVDB;

public class Main {
	public static FrameLoading 					fl;
	private static Interfaccia 					GUIframe;
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		InstanceManager instance_manager=new InstanceManager();
		Settings.baseSettings();
		try{
			//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			if(Settings.isWindows())
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			else 
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
					Database.Disconnect();
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
			
			fl.settext("Controllo aggiornamenti");
			fl.setprog(++i);
			ControlloAggiornamenti aggiornamenti=new ControlloAggiornamenti();
			aggiornamenti.update();
			
			fl.settext("Caricando serie dal database");
			fl.setprog(++i);
			GestioneSerieTV.instance();
			
			fl.settext("Caricando mirrors TheTVDB");
			fl.setprog(++i);
			TheTVDB.caricaMirrors();
			
			fl.settext("Avvio interfaccia grafica");
			fl.setprog(++i);
			
			fl.chiudi();
			
			FileManager.instance();
			
			GUIframe = new Interfaccia();
			GUIframe.init();
			Thread subThread=new Thread(new Runnable() {
				public void run() {
					if(Settings.isRicercaSottotitoli())
						GestioneSerieTV.getSubManager().avviaRicercaAutomatica();
				}
			});
			subThread.start();
			
			if(Settings.isDownloadAutomatico())
				ThreadRicercaAutomatica.avvia();
			
			Advertising.avvio();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(GUIframe, e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
}
