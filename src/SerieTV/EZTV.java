package SerieTV;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Database.Database2;
import Programma.Download2;
import Programma.ManagerException;
import Programma.OperazioniFile;
import Programma.Settings;

public class EZTV extends ProviderSerieTV{
	public String getProviderName() {
		return "eztv.it";
	}
	public String getBaseURL() {
		return "https://eztv.it";
	}

	@Override
	public void aggiornaElencoSerie() {
		Download2 downloader=new Download2("http://eztv.it/showlist/", Settings.getCurrentDir()+"file.html");
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
			f_r=new FileReader(Settings.getCurrentDir()+"file.html");
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
					SerieTV2 toInsert=new SerieTV2(this, nomeserie, url);
					toInsert.setConclusa(stato==0?false:true);
					if(addSerie(toInsert)==null){ //null se non è presente nel database
						
					}
					
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
		OperazioniFile.deleteFile(Settings.getCurrentDir()+"file.html");
	}

	@Override
	public ArrayList<Torrent> nuoviEpisodi(SerieTV2 serie) {
		// TODO
		return null;
	}

	@Override
	public void caricaSerieDB() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void salvaSerieInDB(SerieTV2 s) {
		if(s.getIDDb()>0){
			String query="INSERT INTO "+Database2.TABLE_SERIETV+" (nome, url, inserita, conclusa, stop_search, provider, id_itasa, id_subsfactory, id_subspedia, id_tvdb) VALUES ("+
					"\""+s.getNomeSerie()+"\", "+
					"\""+s.getUrl()+"\","+
					(s.isInserita()?1:0)+","+
					(s.isConclusa()?1:0)+","+
					(s.isStopSearch()?1:0)+","+
					getProviderID()+","+
					s.getIDItasa()+","+
					s.getIDSubsfactory()+","+
					s.getIDSubspedia()+","+
					s.getIDTvdb()+")";
			Database2.updateQuery(query);
		}
		else {
			String query="UPDATE "+Database2.TABLE_SERIETV+" SET "+
					  "nome="+"\""+s.getNomeSerie()+"\""+
					", url="+"\""+s.getUrl()+"\""+
					", inserita="+(s.isInserita()?1:0)+
					", conclusa="+(s.isConclusa()?1:0)+
					", stop_search="+(s.isStopSearch()?1:0)+
					", id_itasa="+s.getIDItasa()+
					", id_subsfactory="+s.getIDSubsfactory()+
					", id_subspedia="+s.getIDSubspedia()+
					", id_tvdb="+s.getIDTvdb()+
					" WHERE id="+s.getIDDb();
			Database2.updateQuery(query);
		}
		
	}
	@Override
	public ArrayList<Torrent> caricaEpisodiDB(SerieTV2 serie) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void salvaEpisodioInDB(Torrent2 t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getProviderID() {
		return 1;
	}
	
}
