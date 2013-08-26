package Programma;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ThreadAggiornamentiSoftware extends Thread {
	private boolean	negative;

	public ThreadAggiornamentiSoftware(boolean negative) {
		this.negative = negative;
	}

	public void run() {
		FileReader file_r = null;
		try {
			Download2.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/version.dat", Settings.getCurrentDir()+"version.dat");
			file_r = new FileReader(Settings.getCurrentDir()+"version.dat");
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Impossibile verificare la versione online");
			ManagerException.registraEccezione(e1);
			return;
		}
		Scanner file = new Scanner(file_r);
		int versione = file.nextInt();
		file.close();

		File file_v = new File(Settings.getCurrentDir()+"version.dat");
		file_v.delete();
		//TODO aggiungere aggiornamento versione beta
		//TODO Modificare per versione Linux e Mac
		if (versione > Settings.getVersioneSoftware() ) {
			OperazioniFile.copyfile("GestioneSerieTV5.exe", "GestioneSerieTV5.exe_back");
			try {
				Download2.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/GestioneSerieTV5.exe", Settings.getCurrentDir()+"GestioneSerieTV5.exe");
				File f=new File(Settings.getCurrentDir()+"GestioneSerieTV5.exe_back");
				f.delete();
				Settings.setNewUpdate(true);
				Settings.setLastVersion(Settings.getVersioneSoftware());
				Runtime.getRuntime().exec(Settings.getCurrentDir()+"GestioneSerieTV5.exe");
				System.exit(0);
			}
			catch (IOException e1) {
				OperazioniFile.copyfile(Settings.getCurrentDir()+"GestioneSerieTV5.exe_back", Settings.getCurrentDir()+"GestioneSerieTV5.exe");
				ManagerException.registraEccezione(e1);
				JOptionPane.showMessageDialog(null, "Errore durante il download dell'aggiornamento");
			}
		}
		else if (this.negative) {
			JOptionPane.showMessageDialog(null, "Nessun aggiornamento disponibile");
		}
	}
}
