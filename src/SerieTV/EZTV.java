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
import StruttureDati.db.KVResult;
import StruttureDati.serietv.Episodio;

public class EZTV extends ProviderSerieTV{
	public EZTV(){
		super();
	}
	
	public String getProviderName() {
		return "eztv.it";
	}
	public String getBaseURL() {
		return "http://eztv.it";
	}

	@Override
	public void aggiornaElencoSerie() {
		System.out.println("EZTV.it - Aggiornando elenco serie tv");
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
		int caricate=0;
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
					
					if(addSerieFromOnline(toInsert)==null){ //null se non � presente nel database
						salvaSerieInDB(toInsert);
						caricate++;
					}
				}
			}
			System.out.println("EZTV - aggiornamento elenco serie tv completo\nCaricate "+caricate+" nuove serie");
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
	public ArrayList<Episodio> nuoviEpisodi(SerieTV2 serie) {
		ArrayList<Episodio> res=new ArrayList<Episodio>();
		for(int i=0;i<serie.getNumEpisodi();i++){
			Episodio e=serie.getEpisodio(i);
			if(!e.isScaricato()){
				res.add(e);
			}
		}
		res.trimToSize();
		return res;
	}

	@Override
	public void caricaSerieDB() {
		String query="SELECT rowid,* FROM "+Database2.TABLE_SERIETV+ " WHERE provider="+PROVIDER_EZTV+" ORDER BY nome ASC";
		elenco_serie.clear();
		ArrayList<KVResult<String, Object>> res=Database2.selectQuery(query);
		for(int i=0;i<res.size();i++){
			KVResult<String, Object> riga=res.get(i);
			int id_db=(int) riga.getValueByKey("rowid");
			String url=(String) riga.getValueByKey("url");
			String nome=(String) riga.getValueByKey("nome");
			boolean inserita=((int)riga.getValueByKey("inserita")==0?false:true);
			boolean conclusa=((int)riga.getValueByKey("conclusa")==0?false:true);
			boolean stop_search=((int)riga.getValueByKey("stop_search")==0?false:true);
			int id_itasa=(int) riga.getValueByKey("id_itasa");
			int id_subsf=(int) riga.getValueByKey("id_subsfactory");
			int id_subsp=(int) riga.getValueByKey("id_subspedia");
			int id_tvdb=(int) riga.getValueByKey("id_tvdb");
			SerieTV2 st=new SerieTV2(this, nome, url);
			st.setIDDb(id_db);
			st.setInserita(inserita);
			st.setConclusa(conclusa);
			st.setIDItasa(id_itasa);
			st.setIDSubsfactory(id_subsf);
			st.setIDSubspedia(id_subsp);
			st.setIDTvdb(id_tvdb);
			st.setStopSearch(stop_search, false);
			addSerieFromDB(st);
		}
		System.out.println("Caricate "+res.size()+" serietv dal database - EZTV.it");
	}
	@Override
	protected void salvaSerieInDB(SerieTV2 s) {
		if(s.getIDDb()==0){
			String query="INSERT INTO "+Database2.TABLE_SERIETV+" (nome, url, inserita, conclusa, stop_search, provider, id_itasa, id_subsfactory, id_subspedia, id_tvdb) VALUES ("+
					"\""+s.getNomeSerie()+"\", "+
					"\""+s.getUrl()+"\","+
					(s.isInserita()?1:0)+","+
					(s.isConclusa()?1:0)+","+
					(s.isStopSearch()?1:0)+","+
					getProviderID()+","+
					s.getIDItasa()+","+
					s.getIDDBSubsfactory()+","+
					s.getIDSubspedia()+","+
					s.getIDTvdb()+")";
			Database2.updateQuery(query);
			
			String query_id="SELECT rowid FROM "+Database2.TABLE_SERIETV+" WHERE url=\""+s.getUrl()+"\"";
			ArrayList<KVResult<String, Object>> res=Database2.selectQuery(query_id);
			if(res.size()==1){
				KVResult<String, Object> row=res.get(0);
				int id_db=(int) row.getValueByKey("rowid");
				s.setIDDb(id_db);
			}
		}
		else {
			String query="UPDATE "+Database2.TABLE_SERIETV+" SET "+
					  "nome="+"\""+s.getNomeSerie()+"\""+
					", url="+"\""+s.getUrl()+"\""+
					", inserita="+(s.isInserita()?1:0)+
					", conclusa="+(s.isConclusa()?1:0)+
					", stop_search="+(s.isStopSearch()?1:0)+
					", id_itasa="+s.getIDItasa()+
					", id_subsfactory="+s.getIDDBSubsfactory()+
					", id_subspedia="+s.getIDSubspedia()+
					", id_tvdb="+s.getIDTvdb()+
					" WHERE rowid="+s.getIDDb();
			Database2.updateQuery(query);
		}
		
	}
	@Override
	public ArrayList<Torrent2> caricaEpisodiDB(SerieTV2 serie) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void salvaEpisodioInDB(Torrent2 t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getProviderID() {
		return PROVIDER_EZTV;
	}
	@Override
	public void caricaEpisodiOnline(SerieTV2 serie) {
		if(serie.isStopSearch())
			return;
		System.out.println("Aggiornando i link di: "+serie.getNomeSerie());
	
		try{
    		
			Download2 download=new Download2(getBaseURL()+serie.getUrl(), Settings.getCurrentDir()+serie.getNomeSerie());
    		download.avviaDownload();
    		download.getDownloadThread().join();
    		
    		FileReader fr=new FileReader(Settings.getCurrentDir()+serie.getNomeSerie());
    		Scanner file=new Scanner(fr);
    		while(file.hasNextLine()){
    			String linea=file.nextLine();
    			if (linea.contains("magnet:?xt=urn:btih:")) {
    				int inizio = linea.indexOf("magnet:?xt=urn:btih:");
    				int fine = linea.indexOf("\" class=\"magnet\"");
    				String url_magnet = linea.substring(inizio, fine);
    				Torrent2 t=new Torrent2(serie, url_magnet, Torrent2.SCARICARE);
    				t.parseMagnet();
    				serie.addEpisodio(t);
    			}
    		}
    		file.close();
    		fr.close();
    		OperazioniFile.deleteFile(Settings.getCurrentDir()+serie.getNomeSerie());
    		
    		if(serie.isConclusa()){
    			serie.setStopSearch(true, true);
    		}
		}
		
		catch(InterruptedException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		catch (IOException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		
	}
}