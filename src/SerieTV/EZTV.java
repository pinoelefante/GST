package SerieTV;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import Database.Database;
import Naming.CaratteristicheFile;
import Programma.Download;
import Programma.HttpsDownload;
import Programma.ManagerException;
import Programma.OperazioniFile;
import Programma.Settings;
import Programma.WebProxyManager;
import StruttureDati.db.KVResult;
import StruttureDati.serietv.Episodio;

public class EZTV extends ProviderSerieTV{
	private ArrayList<String> baseUrls;
	private String baseUrl;
	public EZTV(){
		super();
		baseUrls=new ArrayList<String>();
		baseUrls.add("http://eztvproxy.org");
		baseUrls.add("http://eztv.it");
		baseUrls.add("https://eztv-proxy.net");
		baseUrl=getOnlineUrl();
		System.out.println("Base URL in uso: "+baseUrl);
	}
	
	private String getOnlineUrl(){
		for(int i=0;i<baseUrls.size();i++){
			if(Download.isRaggiungibile(baseUrls.get(i)))
				return baseUrls.get(i);
		}
		return WebProxyManager.getUrlProxy()+"http://eztv.it";
	}
	
	public String getProviderName() {
		return "eztv.it";
	}
	public String getBaseURL() {
		return baseUrl;
		//return "https://eztv-proxy.net";
		//return "http://eztv.it";
	}

	@Override
	public void aggiornaElencoSerie() {
		System.out.println("EZTV.it - Aggiornando elenco serie tv");
		String base_url=/*WebProxyManager.getUrlProxy()+*/getBaseURL();
		if(base_url.startsWith("https")){
			HtmlPage showlist = HttpsDownload.downloadPageFromHttpsUrl(base_url+"/showlist/");
			List<HtmlAnchor> shows=showlist.getAnchors();
			int caricate=0;
			for(int i=0;i<shows.size();i++){
				String link=shows.get(i).getHrefAttribute();
				String nome=shows.get(i).asText();
				if(link.startsWith("/shows/")){
					switch(nome){
						case "T1":
						case "T2":
						case "T3":
						case "T4":
						case "T5":
						case "T6":
						case "T7":
						case "T8":
						case "T9":
						case "Temp1":
						case "Temp2":
						case "Temp3":
						case "Temp4":
						case "Temp5":
						case "Temp6":
						case "Temp7":
						case "Temp8":
						case "Temp9":
						case "Temp 01":
						case "Temp 02":
						case "Temp 03":
						case "Temp 04":
						case "Temp01":
						case "Temp02":
						case "Temp03":
						case "Temp04":
						case "Temporary_Placeholder":
						case "Temporary_Placeholder_2":
							continue;
					}
					link=link.replace(base_url, "");
					link=link.replace("/shows/", "");
					link=link.substring(0,link.indexOf("/"));
					SerieTV toInsert=new SerieTV(this, nome, link);
					if(addSerieFromOnline(toInsert)==null){ //null se non � presente nel database
						salvaSerieInDB(toInsert);
						caricate++;
					}
					System.out.println(nome+" - "+link);
				}
			}
			System.out.println("EZTV - aggiornamento elenco serie tv completo\nCaricate "+caricate+" nuove serie");
		}
		else {
			Download downloader=new Download(base_url+"/showlist/", Settings.getUserDir()+"file.html");
			downloader.avviaDownload();
			try {
				downloader.getDownloadThread().join();
			}
			catch (InterruptedException e1) {
				e1.printStackTrace();
				//aggiornaElencoSerie();
			}
			FileReader f_r=null;
			Scanner file=null;
			int caricate=0;
			try {
				f_r=new FileReader(Settings.getUserDir()+"file.html");
				file=new Scanner(f_r);
				
				while(file.hasNextLine()){
					String linea=file.nextLine().trim();
					if(linea.contains("\"thread_link\"")){
						String nomeserie=linea.substring(linea.indexOf("class=\"thread_link\">")+"class=\"thread_link\">".length(), linea.indexOf("</a>")).trim();
						String url=linea.substring(linea.indexOf("<a href=\"")+"<a href=\"".length(), linea.indexOf("\" class=\"thread_link\">")).trim();
						url=url.replace(base_url, "");
						url=url.replace("/shows/", "");
						url=url.substring(0,url.indexOf("/"));
						String nextline=file.nextLine().trim();
						int stato=0;
						if(nextline.contains("ended"))
							stato=1;
						switch(nomeserie){
							case "T1":
							case "T2":
							case "T3":
							case "T4":
							case "T5":
							case "T6":
							case "T7":
							case "T8":
							case "T9":
							case "Temp1":
							case "Temp2":
							case "Temp3":
							case "Temp4":
							case "Temp5":
							case "Temp6":
							case "Temp7":
							case "Temp8":
							case "Temp9":
							case "Temporary_Placeholder":
							case "Temporary_Placeholder_2":
							case "Temp 01":
							case "Temp 02":
							case "Temp 03":
							case "Temp 04":
							case "Temp01":
							case "Temp02":
							case "Temp03":
							case "Temp04":
								continue;
								
						}
						SerieTV toInsert=new SerieTV(this, nomeserie, url);
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
			OperazioniFile.deleteFile(Settings.getUserDir()+"file.html");
		}
	}

	@Override
	public ArrayList<Episodio> nuoviEpisodi(SerieTV serie) {
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
		String query="SELECT * FROM "+Database.TABLE_SERIETV+ " WHERE provider="+PROVIDER_EZTV+" ORDER BY nome ASC";
		elenco_serie.clear();
		ArrayList<KVResult<String, Object>> res=Database.selectQuery(query);
		for(int i=0;i<res.size();i++){
			KVResult<String, Object> riga=res.get(i);
			int id_db=(int) riga.getValueByKey("id");
			String url=(String) riga.getValueByKey("url");
			String nome=(String) riga.getValueByKey("nome");
			boolean inserita=((int)riga.getValueByKey("inserita")==0?false:true);
			boolean conclusa=((int)riga.getValueByKey("conclusa")==0?false:true);
			boolean stop_search=((int)riga.getValueByKey("stop_search")==0?false:true);
			int id_itasa=(int) riga.getValueByKey("id_itasa");
			int id_subsf=(int) riga.getValueByKey("id_subsfactory");
			int id_subsp=(int) riga.getValueByKey("id_subspedia");
			int id_tvdb=(int) riga.getValueByKey("id_tvdb");
			int preferenze_d=(int)riga.getValueByKey("preferenze_download");
			SerieTV st=new SerieTV(this, nome, url);
			st.setIDDb(id_db);
			st.setInserita(inserita);
			st.setConclusa(conclusa);
			st.setIDItasa(id_itasa);
			st.setIDSubsfactory(id_subsf, false);
			st.setIDSubspedia(id_subsp);
			st.setIDTvdb(id_tvdb);
			st.setStopSearch(stop_search, false);
			st.setPreferenze(new Preferenze(preferenze_d));
			addSerieFromDB(st);
			if(st.isInserita())
				caricaEpisodiDB(st);
		}
		System.out.println("Caricate "+res.size()+" serietv dal database - EZTV.it");
	}
	@Override
	protected void salvaSerieInDB(SerieTV s) {
		if(s.getIDDb()==0){
			String query="INSERT INTO "+Database.TABLE_SERIETV+" (nome, url, inserita, conclusa, stop_search, provider, id_itasa, id_subsfactory, id_subspedia, id_tvdb, preferenze_download) VALUES ("+
					"\""+s.getNomeSerie()+"\", "+
					"\""+s.getUrl()+"\","+
					(s.isInserita()?1:0)+","+
					(s.isConclusa()?1:0)+","+
					(s.isStopSearch()?1:0)+","+
					getProviderID()+","+
					s.getIDItasa()+","+
					s.getIDDBSubsfactory()+","+
					s.getIDSubspedia()+","+
					s.getIDTvdb()+","+
					s.getPreferenze().toValue()+")";
			Database.updateQuery(query);
			
			String query_id="SELECT id FROM "+Database.TABLE_SERIETV+" WHERE url=\""+s.getUrl()+"\"";
			ArrayList<KVResult<String, Object>> res=Database.selectQuery(query_id);
			if(res.size()==1){
				KVResult<String, Object> row=res.get(0);
				int id_db=(int) row.getValueByKey("id");
				s.setIDDb(id_db);
			}
		}
		else {
			String query="UPDATE "+Database.TABLE_SERIETV+" SET "+
					  "nome="+"\""+s.getNomeSerie()+"\""+
					", url="+"\""+s.getUrl()+"\""+
					", inserita="+(s.isInserita()?1:0)+
					", conclusa="+(s.isConclusa()?1:0)+
					", stop_search="+(s.isStopSearch()?1:0)+
					", id_itasa="+s.getIDItasa()+
					", id_subsfactory="+s.getIDDBSubsfactory()+
					", id_subspedia="+s.getIDSubspedia()+
					", id_tvdb="+s.getIDTvdb()+
					", preferenze_download="+s.getPreferenze().toValue()+
					" WHERE id="+s.getIDDb();
			Database.updateQuery(query);
		}
		
	}
	@Override
	public void caricaEpisodiDB(SerieTV serie) {
		String query="SELECT * FROM "+Database.TABLE_EPISODI+" WHERE id_serie="+serie.getIDDb();
		ArrayList<KVResult<String, Object>> res=Database.selectQuery(query);
		for(int i=0;i<res.size();i++){
			KVResult<String, Object> r=res.get(i);
			//(id, id_serie,url,vista,stagione,episodio,tags,preair,sottotitolo,id_tvdb_ep)
			int id=(int) r.getValueByKey("id");
			String url=(String) r.getValueByKey("url");
			int stato=(int) r.getValueByKey("vista");
			int stagione=(int) r.getValueByKey("stagione");
			int episodio=(int) r.getValueByKey("episodio");
			int tags=(int) r.getValueByKey("tags");
			boolean preair=((int)r.getValueByKey("preair"))==1?true:false;
			boolean sub=((int)r.getValueByKey("sottotitolo"))==1?true:false;
			int id_tvdb=(int)r.getValueByKey("id_tvdb_ep");
			CaratteristicheFile stat=new CaratteristicheFile();
			stat.setStatsFromValue(tags);
			Torrent t=new Torrent(serie, url, stato, stat);
			t.setStagione(stagione);
			t.setEpisodio(episodio);
			t.setIDDB(id);
			t.setPreair(preair);
			t.setSubDownload(sub);
			t.setIDTVDB(id_tvdb);
			serie.addEpisodioDB(t);
			//System.out.println(t);
		}
	}
	@Override
	protected void salvaEpisodioInDB(Torrent t) {
		if(t.getIDDB()==0){
			String query_iddb="SELECT id FROM "+Database.TABLE_EPISODI+" WHERE url=\""+t.getUrl()+"\"";
			ArrayList<KVResult<String, Object>> res=Database.selectQuery(query_iddb);
			if(res.size()==1){
				int id=(int) res.get(0).getValueByKey("id");
				t.setIDDB(id);
			}
		}
		if(t.getIDDB()==0){
			String query="INSERT INTO "+Database.TABLE_EPISODI+" (id_serie,url,vista,stagione,episodio,tags,preair,sottotitolo,id_tvdb_ep) VALUES ("
				+t.getSerieTV().getIDDb()+","
				+"\""+t.getUrl()+"\","
				+t.getScaricato()+","
				+t.getStagione()+","+t.getEpisodio()+","+t.getStats().value()+","+(t.isPreAir()?1:0)+","+(t.isSottotitolo()?1:0)+","+t.getIDTVDB()+")";
			//System.out.println(query);
			Database.updateQuery(query);
		}
		else {
			String query="UPDATE "+Database.TABLE_EPISODI+" SET id_serie="+t.getSerieTV().getIDDb()+", url=\""+t.getUrl()+"\", vista="+t.getScaricato()+", stagione="+t.getStagione()+","
					+"episodio="+t.getEpisodio()+", tags="+t.getStats().value()+", preair="+(t.isPreAir()?1:0)+", sottotitolo="+(t.isSottotitolo()?1:0)+", id_tvdb_ep="+t.getIDTVDB()+" "+
					"WHERE id="+t.getIDDB();
			//System.out.println(query);
			Database.updateQuery(query);
		}
	}
	@Override
	public int getProviderID() {
		return PROVIDER_EZTV;
	}
	@Override
	public void caricaEpisodiOnline(SerieTV serie) {
		if(serie.isStopSearch())
			return;
		System.out.println("Aggiornando i link di: "+serie.getNomeSerie());
	
		try{
    		String base_url=/*WebProxyManager.getUrlProxy()+*/getBaseURL();
    		//System.out.println(base_url+"/shows/"+serie.getUrl()+"/");
    		base_url+="/shows/"+serie.getUrl()+"/";
    		if(base_url.startsWith("https")){
    			HtmlPage pagina=HttpsDownload.downloadPageFromHttpsUrl(base_url);
    			List<HtmlAnchor> links=pagina.getAnchors();
    			for(int i=0;i<links.size();i++){
    				String link=links.get(i).getHrefAttribute();
    				if(link.startsWith("magnet:?xt=urn:btih:")){
    					//System.out.println(link);
    					Torrent t=new Torrent(serie, link, Torrent.SCARICARE);
    					t.parseMagnet();
    					serie.addEpisodio(t);
    				}
    			}
    			pagina.cleanUp();
    			pagina.remove();
    		}
    		else {
    			Download download=new Download(base_url, Settings.getUserDir()+serie.getNomeSerie());
        		download.avviaDownload();
        		download.getDownloadThread().join();
    		
    		
        		FileReader fr=new FileReader(Settings.getUserDir()+serie.getNomeSerie());
        		Scanner file=new Scanner(fr);
        		while(file.hasNextLine()){
        			String linea=file.nextLine();
        			if (linea.contains("magnet:?xt=urn:btih:")) {
        				int inizio = linea.indexOf("magnet:?xt=urn:btih:");
        				int fine = linea.indexOf("\" class=\"magnet\"");
        				String url_magnet = linea.substring(inizio, fine);
        				System.out.println(url_magnet);
        				if(url_magnet.length()>0){
            				Torrent t=new Torrent(serie, url_magnet, Torrent.SCARICARE);
            				t.parseMagnet();
            				serie.addEpisodio(t);
        				}
        			}
        		}
        		file.close();
        		fr.close();
        		OperazioniFile.deleteFile(Settings.getUserDir()+serie.getNomeSerie());
    		}
    		
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
