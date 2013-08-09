package SerieTV;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Programma.Download2;
import Programma.ManagerException;
import Programma.OperazioniFile;

public class EZTV extends ProviderSerieTV{
	public String getProviderName() {
		return "eztv.it";
	}
	public String getBaseURL() {
		return "https://eztv.it";
	}

	@Override
	public void aggiornaElencoSerie() {
		Download2 downloader=new Download2("http://eztv.it/showlist/", "file.html");
		downloader.avviaDownload();
		
		try {
			downloader.getDownloadThread().join();
		}
		catch (InterruptedException e1) {
			e1.printStackTrace();
			aggiornaElencoSerie();
		}
		
		FileReader f_r=null;
		Scanner file=null;
		try {
			f_r=new FileReader("file.html");
			file=new Scanner(f_r);
			
			while(file.hasNextLine()){
				String linea=file.nextLine().trim();
				if(linea.contains("\"thread_link\"")){
					String nomeserie=linea.substring(linea.indexOf("class=\"thread_link\">")+"class=\"thread_link\">".length(), linea.indexOf("</a>")).trim();
					String url=linea.substring(linea.indexOf("<a href=\"")+"<a href=\"".length(), linea.indexOf("\" class=\"thread_link\">")).trim();
					String nextline=file.nextLine().trim();
					int stato=0;
					if(nextline.contains("ended"))
						stato=1;
					SerieTV toInsert=new SerieTV(nomeserie, url);
					toInsert.setStato(stato);
					addSerie(toInsert);
					
					/*
					if(!isSeriePresente(getElencoSerieCompleto(), url)){
						SerieTV st=new SerieTV(nomeserie, url);
						st.setStato(stato);
						st.InsertInDB();
						serietv.add(st);
					}
					else{
						SerieTV st=getSerie(getElencoSerieCompleto(), url);
						if(st!=null){
							if(st.getEnd()==0){
								if(st.getStato()!=stato){
									st.setStato(stato);
									st.UpdateDB();
								}
							}
						}
					}
					*/
				}
			}
		}
		catch (FileNotFoundException e) {
			ManagerException.registraEccezione(e);
		}
		finally{
			file.close();
			try {
				f_r.close();
			}
			catch (IOException e) {	
				e.printStackTrace();
				ManagerException.registraEccezione(e);
			}
		}
		OperazioniFile.deleteFile("file.html");
	}

	@Override
	public ArrayList<Torrent> nuoviEpisodi(SerieTV serie) {
		
		return null;
	}

	@Override
	public void caricaSerieDB() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void salvaSerieInDB(SerieTV s) {
		// TODO Auto-generated method stub
		
	}
	
}
