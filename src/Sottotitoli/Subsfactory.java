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

import Database.SQLParameter;
import Database.Database;
import Naming.CaratteristicheFile;
import Naming.Naming;
import Naming.Renamer;
import Programma.Download;
import Programma.ManagerException;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;
import SerieTV.Torrent;

public class Subsfactory implements ProviderSottotitoli {
	class RSSItem {
		private String titolo, descrizione, url, url_download;
		private String ID="";
		private int stagione=0, episodio=0;
		private boolean HD720p=false, Normale=true;
		public RSSItem(String t, String d, String u){
			setTitolo(t);
			setDescrizione(d);
			setUrl(u);
			parse();
		}
		private void parse(){
			String id=getUrl().substring(getUrl().indexOf("directory=")+"directory=".length(), getUrl().indexOf("&filename")).replace("%2F", "/").replace("%20", " ");
			String url_d=getUrl().replace("action=view", "action=downloadfile");
			String filename=getUrl().substring(getUrl().indexOf("filename")+"filename=".length());
			setID(id);
			setUrlDownload(url_d);
			String[] r=filename.split("[sS0-9]{3}[eE][0-9]{2}");
			if(r.length==2){
				String analyze=filename.substring(r[0].length(), filename.indexOf(r[1]));
				String[] res=analyze.replace("s", "").replace("S", "").replace("e", " ").replace("E", " ").split(" ");
				setStagione(Integer.parseInt(res[0]));
				setEpisodio(Integer.parseInt(res[1]));
			}
			else
				return;
			if(descrizione.contains("normale") || descrizione.contains("Normale"))
				setNormale(true);
			if(descrizione.contains("720p"))
				set720p(true);
			if(descrizione.toLowerCase().contains("WEB-DL".toLowerCase()))
				setNormale(false);
		}
		public boolean isValid(){
			return (getStagione()!=0 && getEpisodio()!=0);
		}
		public String getTitolo() {
			return titolo;
		}
		public void setTitolo(String titolo) {
			this.titolo = titolo;
		}
		public void setUrlDownload(String url){
			url_download=url;
		}
		public String getUrlDownload(){
			return url_download;
		}
		public String getDescrizione() {
			return descrizione;
		}
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getID() {
			return ID;
		}
		public void setID(String iD) {
			ID = iD;
		}
		public int getStagione() {
			return stagione;
		}
		public void setStagione(int stagione) {
			this.stagione = stagione;
		}
		public int getEpisodio() {
			return episodio;
		}
		public void setEpisodio(int episodio) {
			this.episodio = episodio;
		}
		public boolean is720p() {
			return HD720p;
		}
		public void set720p(boolean hD720p) {
			HD720p = hD720p;
		}
		public boolean isNormale() {
			return Normale;
		}
		public void setNormale(boolean normale) {
			Normale = normale;
		}
		public String toString(){
			return getStagione()+" "+getEpisodio()+" "+isNormale()+" "+is720p();
		}
	}
	class SottotitoloSubsfactory {
		private String nomefile, id_serie;
		private int season, ep;
		private boolean normale=true, hd720p;
		
		public SottotitoloSubsfactory(String nome, String id) {
			nomefile=nome;
			this.id_serie=id;
			parseNome();
		}
		private void parseNome(){
			try{
				CaratteristicheFile stats=Naming.parseString(nomefile);
				ep=stats.getEpisodio();
				season=stats.getStagione();
			}
			catch(Exception e){
				ep=0;
				season=0;
			}
		}
		public boolean isNormale(){
			return normale;
		}
		public boolean is720p(){
			return hd720p;
		}
		public void setNormale(boolean s){
			normale=s;
		}
		public void set720p(boolean s){
			hd720p=s;
		}
		public int getStagione(){
			return season;
		}
		public int getEpisodio(){
			return ep;
		}
		public String getNomeFile(){
			return nomefile;
		}
		public String getIDSerie(){
			return id_serie;
		}
	}
	private final static String URL_ELENCO_SERIE="http://subsfactory.it/subtitle/index.php?&direction=0&order=nom";
	private final static String URL_FEED_RSS="http://subsfactory.it/subtitle/rss.php";
	private GregorianCalendar RSS_UltimoAggiornamento;
	private final long update_time_rss=900000L;  //15 minuti
	private ArrayList<RSSItem> feed_rss;
	private ArrayList<SerieSub> elenco_serie;
	private static int download_corrente=0;
	
	public Subsfactory() {
		feed_rss=new ArrayList<RSSItem>();
		elenco_serie=new ArrayList<SerieSub>();
		caricaElencoSerie();
	}

	@Override
	public boolean scaricaSottotitolo(Torrent t) {
		if(t==null)
			return false;
		SerieTV st=GestioneSerieTV.getSerieFromName(GestioneSerieTV.getElencoSerieInserite(), t.getNomeSerie());
		if(st==null)
			return false;
		String id_subsfactory=st.getSubsfactoryDirectory();
		System.out.println(t.getNomeSerie()+" - id_subsfactory: "+id_subsfactory);
		if(id_subsfactory.isEmpty())
			return false;
		String path=cercaFeed(id_subsfactory, t);
		System.out.println(t.getNomeSerie()+" - url: "+path);
		if(path!=null){
			if(scaricaSub(path, Renamer.generaNomeDownload(t), t.getNomeSerieFolder())){
				t.setSottotitolo(false, true);
				return true;
			}
		}
		return false;
	}
	private boolean scaricaSub(String url, String nome, String folder){
		String cartella_destinazione=Settings.getDirectoryDownload()+(Settings.getDirectoryDownload().endsWith(File.pathSeparator)?folder:(File.separator+folder));
		String destinazione=cartella_destinazione+File.separator+nome;
		try {
			Download.downloadFromUrl(url, destinazione);
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
	public boolean cercaSottotitolo(Torrent t) {
		System.out.println("Subsfactory.it - "+t.getNomeSerie());
		SerieTV st=GestioneSerieTV.getSerieFromName(GestioneSerieTV.getElencoSerieInserite(), t.getNomeSerie());
		/*TODO*/
		//cerca in database
		//cerca in feed
		//cerca nella cartella
		return cercaFeed(st.getSubsfactoryDirectory(), t)==null?false:true;
	}
	//Verifica all'interno della pagina della serie
	private void caricaCartella(String id_serie, String id_cartella){
		/*
		 http://subsfactory.it/subtitle/index.php?&direction=0&order=nom&directory=Serie%20USA/Arrow 
		*/
		String url="http://subsfactory.it/subtitle/index.php?&direction=0&order=nom&directory="+id_serie.replace(" ", "%20")+"/"+id_cartella.replace(" ", "%20");
		try {
			//TODO caricamento dei sottotitoli della seguente serie
			String path=url.substring(url.indexOf("directory=")+"directory=".length());
			int id_download=download_corrente++;
			Download.downloadFromUrl(url, "subsf_response_"+id_download);
			FileReader f=new FileReader("subsf_response_"+id_download);
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
					//TODO controllare che il sottotitolo non sia già presente
					//TODO inserimento nel database
					//System.out.println(nome_file);
					System.out.println(sub.getNomeFile()+"\n"+sub.getStagione()+"x"+sub.getEpisodio());
				}
			}
			file.close();
			f.close();
			OperazioniFile.deleteFile("subsf_response_"+id_download);
		}
		catch (IOException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	private void inserisciSubInDB(String path, SottotitoloSubsfactory sub, boolean completa){
		SQLParameter[] parametri=new SQLParameter[6];
		int i=0;
		parametri[i++]=new SQLParameter(SQLParameter.TEXT, path+"/"+sub.getNomeFile(), "path");
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
	}
	private ArrayList<SottotitoloSubsfactory> caricaSubDaDB(String id_serie){
		//TODO stub caricamento sub da db
		return null;
	}
	public static void main(String[] args){
		Subsfactory subs=new Subsfactory();
		subs.caricaCartella("Serie USA/Breaking Bad", "");
	}

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
			Download.downloadFromUrl(URL_ELENCO_SERIE, "response_subs");
			f_r=new FileReader("response_subs");
		}
		catch (IOException e) {
			OperazioniFile.deleteFile("response_subs");
			ManagerException.registraEccezione(e);
			return;
		}
		elenco_serie.clear();
		Scanner file=new Scanner(f_r);
		
		while(!file.nextLine().contains("<select name=\"loc\""));
		//file.nextLine();
		
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
			OperazioniFile.deleteFile("response_subs");
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
	private String cercaFeed(String id_subs, Torrent t){
		if(verificaTempo(update_time_rss, RSS_UltimoAggiornamento)){
			System.out.println("Aggiornando il feed RSS - Subsfactory.it");
			aggiornaFeedRSS();
		}
		for(int i=0;i<feed_rss.size();i++){
			RSSItem rss=feed_rss.get(i);
			System.out.println(rss.getStagione()+" "+rss.getEpisodio()+" "+ rss.getUrlDownload());
			System.out.println("ID: "+rss.getID()+" - "+id_subs);
			if(rss.getID().toLowerCase().startsWith(id_subs.toLowerCase())){
				System.out.println("Stagione: "+rss.getStagione() + " - "+t.getSerie());
				if(rss.getStagione()==t.getSerie()){
					System.out.println("Puntata: "+rss.getEpisodio()+" - "+t.getPuntata());
					if(rss.getEpisodio()==t.getPuntata()){
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
			Download.downloadFromUrl(URL_FEED_RSS, "feed_subs");
			
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domparser = dbfactory.newDocumentBuilder();
			Document doc = domparser.parse(new File("feed_subs"));
			
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
				RSSItem rss=new RSSItem(titolo, descrizione, link);
				if(rss.isValid())
					feed_rss.add(rss);
			}
			OperazioniFile.deleteFile("feed_subs");
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
