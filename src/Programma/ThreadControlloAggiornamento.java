package Programma;

import interfaccia.Language;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ThreadControlloAggiornamento extends Thread {

	public ThreadControlloAggiornamento() {
	}

	public void run() {
		FileReader file_r = null;
		try {
			Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/version.dat", "version.dat");
			file_r = new FileReader("version.dat");
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(null, Language.DIALOGUE_ERROR_UPDATE);
			ManagerException.registraEccezione(e1);
			return;
		}
		Scanner file = new Scanner(file_r);
		int versione = file.nextInt();
		file.close();

		File file_v = new File("version.dat");
		file_v.delete();
		//TODO aggiungere aggiornamento versione beta
		//TODO Modificare per versione Linux e Mac
		if (versione > Settings.getVersioneSoftware() || (Settings.isBeta() && versione==Settings.getVersioneSoftware())) {
			OperazioniFile.copyfile("GestioneSerieTV5.exe", "GestioneSerieTV5.exe_back");
			try {
				Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/GestioneSerieTV5.exe", "GestioneSerieTV5.exe");
				File f=new File("GestioneSerieTV5.exe_back");
				f.delete();
				Settings.setNewUpdate(true);
				Settings.setLastVersion(Settings.getVersioneSoftware());
				Runtime.getRuntime().exec("GestioneSerieTV5.exe");
				System.exit(0);
			}
			catch (IOException e1) {
				OperazioniFile.copyfile("GestioneSerieTV5.exe_back", "GestioneSerieTV5.exe");
				ManagerException.registraEccezione(e1);
			}
		}
	}
}
