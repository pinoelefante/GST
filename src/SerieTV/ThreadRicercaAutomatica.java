package SerieTV;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import GUI.Interfaccia;
import Programma.Download;
import Programma.Settings;
import StruttureDati.serietv.Episodio;

public class ThreadRicercaAutomatica extends Thread {
	public static ThreadRicercaAutomatica getInstance(){
		if(thisInstance==null)
			thisInstance=new ThreadRicercaAutomatica();
		return thisInstance;
	}
	private ThreadRicercaAutomatica(){
		super();
		thisInstance=this;
	}
	public void run(){
		System.out.println("Avvio download automatico");
		try {
			while(!GestioneSerieTV.isFirstLoaded() || GestioneSerieTV.isLoading()){
				sleep(1000L);
				System.out.println("Attesa fine caricamento");
			}
			int i=0;
			while(true){
				download();
				Interfaccia.getInterfaccia().inizializzaDownloadScroll();
				do {
					sleep(60000L);
					System.out.println("Attesa prossimo download: "+i+"/28800");
					i=i+60;
				}
				while(i<28800);
				aggiorna();
			}
		}
		catch(InterruptedException e){
			System.out.println("Download automatico interrotto");
			thisInstance=null;
		}
	}
	private void download(){
		System.out.println("Download automatico degli episodi");
		ArrayList<Episodio> torrents=GestioneSerieTV.caricaEpisodiDaScaricareOffline();
		for(int i=0;i<torrents.size();i++){
			Episodio ep=torrents.get(i);
			ArrayList<Torrent> l_torrent=ep.getLinkDownload();
			for(int j=0;j<l_torrent.size();j++){
				Torrent torrent=l_torrent.get(j);
				try {
					Download.downloadMagnet(torrent.getUrl(), Settings.getDirectoryDownload() + File.separator + torrent.getSerieTV().getFolderSerie());
					ep.scaricaLink(torrent);
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void aggiorna(){
		System.out.println("Download automatico - aggiornamento");
		if(!GestioneSerieTV.isLoading()){
			System.out.println("Download automatico - aggiornamento avviato");
			GestioneSerieTV.caricaEpisodiDaScaricare();
		}
	}
	private static ThreadRicercaAutomatica thisInstance;
	public static void avvia(){
		Thread t=getInstance();
		if(t.isInterrupted()){
			arresta();
			t=getInstance();
		}
		if(!t.isAlive())
			t.start();
	}
	public static void arresta(){
		if(thisInstance!=null){
			thisInstance.interrupt();
			thisInstance=null;
		}
	}
}
