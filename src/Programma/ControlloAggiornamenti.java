package Programma;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ControlloAggiornamenti {
	private final static String URL_DB="http://pinoelefante.altervista.org/software/GST2/version.dat";
	private int versione_online;
	
	private void retrieveVersioneOnline(){
		Download2 downloader=new Download2(URL_DB, Settings.getCurrentDir()+"version.dat");
		downloader.avviaDownload();
		try {
			downloader.getDownloadThread().join();
			FileReader f=new FileReader(Settings.getCurrentDir()+"version.dat");
			Scanner file=new Scanner(f);
			if(file.hasNextInt()){
				versione_online=file.nextInt();
			}
			file.close();
			f.close();
			OperazioniFile.deleteFile(Settings.getCurrentDir()+"version.dat");
		} 
		catch (InterruptedException e) {
			return;
		} 
		catch (FileNotFoundException e) {
			versione_online=Settings.getVersioneSoftware();
		} 
		catch (IOException e) {	}
	}
	public int getVersioneOnline(){
		retrieveVersioneOnline();
		return versione_online;
	}
}
