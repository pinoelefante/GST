package SerieTV;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import Programma.Download;
import Programma.Settings;

public class ThreadRicercaAutomatica extends Thread {
	public void run() {
		if ((!Settings.isStartHidden())) {
			while (!Settings.isCanStartDownloadAutomatico()) {
				try {
					Thread.sleep(1000L);
				}
				catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		try {
			System.out.println("Inizio sleep");
			Thread.sleep(15000L);
			System.out.println("Fine sleep");
		}
		catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		boolean error_e = false;
		while (true) {
			System.out.println("Controllo serie tv...");
			GestioneSerieTV.aggiornaListeTorrent();
			ArrayList<Torrent> torrent_download = GestioneSerieTV.getTorrentDownload();
			for (int i = 0; i < torrent_download.size(); i++) {
				try {
					Torrent t=torrent_download.get(i);
					
					if(t.is720p())
						if(!Settings.isDownload720p())
							continue;
					if(t.isPreAir())
						if(!Settings.isDownloadPreair())
							continue;
					
					Download.downloadMagnet(t.getUrl(), t.getNomeSerieFolder());
					t.setScaricato(Torrent.SCARICATO);
					t.setSottotitolo(true, false);
				}
				catch (IOException e) {
					error_e = true;
					break;
				}
				try {
					Thread.sleep(333L);
				}
				catch (InterruptedException localInterruptedException1) {
				}
			}
			if (error_e) {
				JOptionPane.showMessageDialog(null, "Errore durante il download automatico.\nControllare le impostazioni.");
			}

			//TODO aggiornamento interfaccia scrollpane
			//Interfaccia.download_bottone_torrent_offline.doClick();
			try {
				Runtime.getRuntime().gc();
				System.out.println("Fine controllo...");
				Thread.sleep(Settings.getMinRicercaMilli());
			}
			catch (InterruptedException e) {
				System.out.println("Interrotto");
			}
		}
	}
}
