package Sottotitoli;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Naming.Renamer;
import Programma.Download2;
import Programma.ManagerException;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.SerieTV2;
import SerieTV.Torrent2;

public class Subsfactory implements ProviderSottotitoli {
	private final static String URL_ELENCO_SERIE="http://subsfactory.it/subtitle/index.php?&direction=0&order=nom";
	private final static String URL_FEED_RSS="http://subsfactory.it/subtitle/rss.php";
	private GregorianCalendar RSS_UltimoAggiornamento;
	private final long update_time_rss=900000L;  //15 minuti
	private ArrayList<RSSItemSubsfactory> feed_rss;
	private ArrayList<SerieSub> elenco_serie;
	private static int download_corrente=0;
	
	public Subsfactory() {
		feed_rss=new ArrayList<RSSItemSubsfactory>();
		elenco_serie=new ArrayList<SerieSub>();
		caricaElencoSerie();
	}

	@Override
	public boolean scaricaSottotitolo(Torrent2 t) {
		if(t==null)
			return false;
		SerieTV2 st=t.getSerieTV();
		if(st==null)
			return false;
		String id_subsfactory=st.getSubsfactoryDirectory();
		System.out.println(t.getNomeSerie()+" - id_subsfactory: "+id_subsfactory);
		if(id_subsfactory.isEmpty())
			return false;
		/*
		 * cercare nel database
		 * se non è presente caricare cartella online
		 * cercare di nuovo nel database
		 * se non è presente cercare nel feed rss
		 */
		String url="";
		switch(0){
			case 0:
				url=cercaFeed(st.getSubsfactoryDirectory(), t);
				if(url!=null){
					if(url.length()>0)
						break;
				}
			case 1:
				url=cercaSubInDB(st.getSubsfactoryDirectory(), t);
				if(url!=null){
					if(url.length()>0)
						break;
				}
			case 2:
				caricaCartella(st.getSubsfactoryDirectory(), "");
				url=cercaSubInDB(st.getSubsfactoryDirectory(), t);
				if(url!=null){
					if(url.length()>0)
						break;
					else
						return false;
				}
				else
					return false;
		}
		
		if(url!=null){
			if(url.length()>0){
				url=url.replace(" ", "%20");
				if(scaricaSub(url, Renamer.generaNomeDownload(t), t.getNomeSerieFolder())){
					t.setSubDownload(false);
					return true;
				}
			}
		}
		return false;
	}
	private boolean scaricaSub(String url, String nome, String folder){
		String cartella_destinazione=Settings.getDirectoryDownload()+(Settings.getDirectoryDownload().endsWith(File.pathSeparator)?folder:(File.separator+folder));
		String destinazione=cartella_destinazione+File.separator+nome;
		try {
			Download2.downloadFromUrl(url, destinazione);
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
			return false;
		}
	}
	@Override
	public String getIDSerieAssociata(String nome_serie) {
		for(int i=0;i<elenco_serie.size();i++)
			if(elenco_serie.get(i).getNomeSerie().compareToIgnoreCase(nome_serie)==0)
				return ((String)elenco_serie.get(i).getID());
		return null;
	}

	@Override
	public boolean cercaSottotitolo(Torrent2 t) {
		System.out.println("Subsfactory.it - "+t.getNomeSerie());
		SerieTV2 st=t.getSerieTV();
		
		//cerca in database
		String url=cercaSubInDB(st.getSubsfactoryDirectory(), t);
		if(url.length()>0)
			return true;
		
		//carica cartella online
		caricaCartella(st.getSubsfactoryDirectory(), "");
		//cerca di nuovo in database
		url=cercaSubInDB(st.getSubsfactoryDirectory(), t);
		if(url.length()>0)
			return true;

		//cerca in feed
		return cercaFeed(st.getSubsfactoryDirectory(), t)==null?false:true;
	}
	
	//Verifica all'interno della pagina della serie
	private void caricaCartella(String id_serie, String id_cartella){
		/*
		 http://subsfactory.it/subtitle/index.php?&direction=0&order=nom&directory=Serie%20USA/Arrow 
		*/
		String url="http://subsfactory.it/subtitle/index.php?&direction=0&order=nom&directory="+id_serie.replace(" ", "%20")+"/"+id_cartella.replace(" ", "%20");
		try {
			//String path=url.substring(url.indexOf("directory=")+"directory=".length());
			int id_download=download_corrente++;
			Download2.downloadFromUrl(url, Settings.getCurrentDir()+"subsf_response_"+id_download);
			FileReader f=new FileReader(Settings.getCurrentDir()+"subsf_response_"+id_download);
			Scanner file=new Scanner(f);
			while(file.hasNextLine()){
				String linea=file.nextLine().trim();
				if(linea.contains(".zip") && linea.contains("title=")){
					//System.out.println(linea);
					String nome_file=linea.substring(linea.indexOf("title=\"")+"title=\"".length(), linea.indexOf(".zip")+".zip".length());
					SottotitoloSubsfactory sub=new SottotitoloSubsfactory(nome_file, id_serie);
					if(linea.toLowerCase().contains("normale") && linea.toLowerCase().contains("normale")){
						sub.setNormale(true);
						sub.set720p(true);
					}
					else if(linea.toLowerCase().contains("720p")){
						sub.set720p(true);
						sub.setNormale(false);
					}
					else {
						sub.set720p(false);
						sub.setNormale(true);
					}
					String path_d="http://www.subsfactory.it/subtitle/index.php?action=downloadfile"+"&directory="+id_serie+(id_cartella.length()>0?("/"+id_cartella):""+"&filename="+sub.getNomeFile());
					if(sub.getEpisodio()>0 && sub.getStagione()>0 && !isSubInDB(path_d)){
						inserisciSubInDB(path_d, sub, sub.getNomeFile().toLowerCase().contains("stagione"));
					}
				}
			}
			file.close();
			f.close();
			OperazioniFile.deleteFile(Settings.getCurrentDir()+"subsf_response_"+id_download);
		}
		catch (IOException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	private boolean isSubInDB(String path){
		return false;
		/*
		SQLParameter[] cond=new SQLParameter[1];
		cond[0]=new SQLParameter(SQLParameter.TEXT, path, "path");
		ArrayList<SQLParameter[]> res=Database.select(Database.TABLE_SUBSFACTORY, cond, "", "=");
		for(int i=0;i<res.size();i++){
			SQLParameter[] p=res.get(i);
			for(int j=0;j<p.length;j++)
				System.out.print(p[j].pvalueAsString()+" ");
			System.out.println();
		}
		return res.size()>0;
		*/
	}
	private void inserisciSubInDB(String path, SottotitoloSubsfactory sub, boolean completa){
		/*
		SQLParameter[] parametri=new SQLParameter[6];
		int i=0;
		parametri[i++]=new SQLParameter(SQLParameter.TEXT, path, "path");
		parametri[i++]=new SQLParameter(SQLParameter.TEXT, sub.getIDSerie(), "id_serie");
		parametri[i++]=new SQLParameter(SQLParameter.INTEGER, sub.getStagione(), "stagione");
		parametri[i++]=new SQLParameter(SQLParameter.INTEGER, sub.getEpisodio(), "episodio");
		int tipo=0;
		if(sub.isNormale() && sub.is720p())
			tipo=2;
		else if(sub.isNormale())
			tipo=0;
		else if(sub.is720p())
			tipo=1;
		parametri[i++]=new SQLParameter(SQLParameter.INTEGER, tipo, "tipo");
		parametri[i++]=new SQLParameter(SQLParameter.INTEGER, completa?1:0, "is_completa");
		Database.insert(Database.TABLE_SUBSFACTORY, parametri);
		*/
	}
	private String cercaSubInDB(String id_serie, Torrent2 t){
		ArrayList<SottotitoloSubsfactoryDB> subs=caricaSubDaDB(id_serie);
		
		for(int i=0;i<subs.size();i++){
			SottotitoloSubsfactoryDB s=subs.get(i);
			if(s.getEpisodio()==t.getEpisodio() && s.getStagione()==t.getStagione()){
				switch(t.is720p()?1:0){
					case 0:
						if(s.isNormale())
							return s.getUrl();
						break;
					case 1:
						if(s.is720p())
							return s.getUrl();
						break;
					case 2:
						return s.getUrl();
				}
			}
		}
		
		return "";
	}
	private ArrayList<SottotitoloSubsfactoryDB> caricaSubDaDB(String id_serie){
		/*
		ArrayList<SottotitoloSubsfactoryDB> subs=new ArrayList<SottotitoloSubsfactoryDB>();
		SQLParameter[] par=new SQLParameter[1];
		par[0]=new SQLParameter(SQLParameter.TEXT, id_serie, "id_serie");
		ArrayList<SQLParameter[]> res=Database.select(Database.TABLE_SUBSFACTORY, par, "", "=");
		for(int i=0;i<res.size();i++){
			SQLParameter[] r=res.get(i); 
			String url = null, id_s = null;
			int ep = 0, seas = 0, tipo = 0;
			boolean compl = false;
			for(int j=0;j<r.length;j++){
				switch(r[j].getField()){
					case "path":
						url=r[j].pvalueAsString();
						break;
					case "id_serie":
						id_s=r[j].pvalueAsString();
						break;
					case "stagione":
						seas=(int)r[j].pvalue();
						break;
					case "episodio":
						ep=(int)r[j].pvalue();
						break;
					case "tipo":
						tipo=(int)r[j].pvalue();
						break;
					case "is_completa":
						compl=(((int)r[j].pvalue())==0?false:true);
						break;
				}
			}
			subs.add(new SottotitoloSubsfactoryDB(tipo, url, seas, ep, compl, id_s));
		}
		return subs;
		*/
		return null;
	}
	public static void main(String[] args){	}

	@Override
	public ArrayList<SerieSub> getElencoSerie() {
		return elenco_serie;
	}

	@Override
	public String getProviderName() {
		return "Subsfactory.it";
	}

	@Override
	public void caricaElencoSerie() {
		FileReader f_r;
		try {
			Download2.downloadFromUrl(URL_ELENCO_SERIE, Settings.getCurrentDir()+"response_subs");
			f_r=new FileReader(Settings.getCurrentDir()+"response_subs");
		}
		catch (IOException e) {
			OperazioniFile.deleteFile(Settings.getCurrentDir()+"response_subs");
			ManagerException.registraEccezione(e);
			return;
		}
		elenco_serie.clear();
		Scanner file=new Scanner(f_r);
		
		while(!file.nextLine().contains("<select name=\"loc\""));
		
		while(true){
			String riga=file.nextLine().trim();
			if(riga.startsWith("<option value=")){
				String path=riga.substring("<option value=\"files/".length(), riga.indexOf("\">")-1).trim();
				if(path.startsWith("Film"))
					continue;
				String nome=riga.substring(riga.indexOf("\">")+2, riga.indexOf("</option>")).trim().replace("&nbsp;", "");
				if(path.split("/").length>2)
					continue;
				addSerie(new SerieSub(nome, path));
			}
			else if(riga.compareToIgnoreCase("</select>")==0)
				break;
		}
		
		file.close();
		try {
			f_r.close();
			OperazioniFile.deleteFile(Settings.getCurrentDir()+"response_subs");
		}
		catch (IOException e) {
			ManagerException.registraEccezione(e);
		}
	}
	private void addSerie(SerieSub s){
		if(elenco_serie==null){
			elenco_serie=new ArrayList<SerieSub>();
		}
		if(elenco_serie.isEmpty())
			elenco_serie.add(s);
		else {
			int i=0;
			while(i<elenco_serie.size()){
				SerieSub s_f_e=elenco_serie.get(i);
				int res=s.getNomeSerie().compareToIgnoreCase(s_f_e.getNomeSerie());
				if(res<0)
					break;
				else if(res==0)
					return;
				i++;
			}
			elenco_serie.add(i, s);
		}
	}
	private String cercaFeed(String id_subs, Torrent2 t){
		if(verificaTempo(update_time_rss, RSS_UltimoAggiornamento)){
			System.out.println("Aggiornando il feed RSS - Subsfactory.it");
			aggiornaFeedRSS();
		}
		for(int i=0;i<feed_rss.size();i++){
			RSSItemSubsfactory rss=feed_rss.get(i);
			System.out.println(rss.getStagione()+" "+rss.getEpisodio()+" "+ rss.getUrlDownload());
			System.out.println("ID: "+rss.getID()+" - "+id_subs);
			if(rss.getID().toLowerCase().startsWith(id_subs.toLowerCase())){
				System.out.println("Stagione: "+rss.getStagione() + " - "+t.getStagione());
				if(rss.getStagione()==t.getStagione()){
					System.out.println("Puntata: "+rss.getEpisodio()+" - "+t.getEpisodio());
					if(rss.getEpisodio()==t.getEpisodio()){
						System.out.println("Risoluzione: Rss("+rss.is720p()+rss.isNormale()+")"+" - Torrent("+t.is720p()+!t.is720p()+")");
						if(rss.isNormale()==!t.is720p())
							return rss.getUrlDownload();
						else if(rss.is720p()==t.is720p())
							return rss.getUrlDownload();
					}
				}
			}
		}
		return null;
	}
	private void aggiornaFeedRSS(){
		RSS_UltimoAggiornamento=new GregorianCalendar();
		feed_rss.clear();
		try {
			Download2.downloadFromUrl(URL_FEED_RSS, Settings.getCurrentDir()+"feed_subs");
			
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domparser = dbfactory.newDocumentBuilder();
			Document doc = domparser.parse(new File(Settings.getCurrentDir()+"feed_subs"));
			
			NodeList elementi=doc.getElementsByTagName("item");
			for(int i=0;i<elementi.getLength();i++){
				Node item=elementi.item(i);
				NodeList attributi=item.getChildNodes();
				String titolo="", descrizione="", link="";
				for(int j=0;j<attributi.getLength();j++){
					Node attributo=attributi.item(j);
					if(attributo instanceof Element){
						Element attr=(Element)attributo;
						switch(attr.getTagName()){
							case "title":
								titolo=attr.getTextContent();
								break;
							case "description":
								descrizione=attr.getTextContent();
								break;
							case "link":
								link=attr.getTextContent();
								break;
						}
					}
				}
				RSSItemSubsfactory rss=new RSSItemSubsfactory(titolo, descrizione, link);
				if(rss.isValid())
					feed_rss.add(rss);
			}
			OperazioniFile.deleteFile(Settings.getCurrentDir()+"feed_subs");
		} 
		catch (IOException e) {	
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		catch (SAXException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	private boolean verificaTempo(long maxdif, GregorianCalendar last){
		if(last==null)
			return true;
		GregorianCalendar adesso=new GregorianCalendar();
		long time_now=adesso.getTimeInMillis();
		adesso=null;
		long time_last=last.getTimeInMillis();
		if((time_now-time_last)>maxdif)
			return true;
		return false;
	}
	public void stampa_feed(){
		for(int i=0;i<feed_rss.size();i++)
			System.out.println(feed_rss.get(i));
	}
}
