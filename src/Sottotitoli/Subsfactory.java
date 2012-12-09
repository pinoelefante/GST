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

import Programma.Download;
import Programma.OperazioniFile;
import SerieTV.Torrent;

public class Subsfactory implements ProviderSottotitoli {
	class RSSItem {
		private String titolo, descrizione, url;
		private String ID;
		private int stagione, episodio;
		private boolean HD720p, Normale;
		public RSSItem(String t, String d, String u){
			setTitolo(t);
			setDescrizione(d);
			setUrl(u);
		}
		public String getTitolo() {
			return titolo;
		}
		public void setTitolo(String titolo) {
			this.titolo = titolo;
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
			return getUrl();
		}
	}
	
	private final static String URL_ELENCO_SERIE="http://subsfactory.it/subtitle/index.php?&direction=0&order=nom";
	private final static String URL_FEED_RSS="http://subsfactory.it/subtitle/rss.php";
	private GregorianCalendar RSS_UltimoAggiornamento;
	private final long update_time_rss=900000L;  //15 minuti
	private ArrayList<RSSItem> feed_rss;
	private ArrayList<SerieSub> elenco_serie;
	
	public Subsfactory() {
		feed_rss=new ArrayList<RSSItem>();
		elenco_serie=new ArrayList<SerieSub>();
		caricaElencoSerie();
	}

	@Override
	public boolean scaricaSottotitolo(Torrent t) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
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
		catch (IOException e) {}
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
			System.out.println("Aggiornando il feed RSS");
			aggiornaFeedRSS();
		}
		for(int i=0;i<feed_rss.size();i++){
			RSSItem rss=feed_rss.get(i);
			if(rss.getID().compareToIgnoreCase(id_subs)==0){
				if(rss.is720p()==t.is720p()){
					if(rss.isNormale()==!t.is720p()){
						if(rss.getStagione()==t.getSerie()){
							if(rss.getEpisodio()==t.getPuntata()){
								return rss.getUrl();
							}
						}
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
			
			//TODO XML
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
				feed_rss.add(new RSSItem(titolo, descrizione, link));
			}
			OperazioniFile.deleteFile("feed_subs");
		} 
		catch (IOException e) {	
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (SAXException e) {
			e.printStackTrace();
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
	public static void main(String[] args){
		Subsfactory s=new Subsfactory();
		s.aggiornaFeedRSS();
		s.stampa_feed();
	}
}
