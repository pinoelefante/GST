package Sottotitoli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Programma.Download;
import Programma.ManagerException;
import Programma.OperazioniFile;
import SerieTV.Torrent;

public class Subspedia implements ProviderSottotitoli {
	private final String URLFeedRSS="http://subspedia.weebly.com/1/feed";
	private static ArrayList<SubsPediaRSSItem> rss;
	
	public Subspedia(){
		rss=new ArrayList<SubsPediaRSSItem>();
	}
	
	public boolean scaricaSottotitolo(Torrent t) {return false;}
	public String getIDSerieAssociata(String nome_serie) {return null;}
	public boolean cercaSottotitolo(Torrent t) {
		// TODO Auto-generated method stub
		return false;
	}
	public ArrayList<SerieSub> getElencoSerie() {return null;}
	public String getProviderName() {
		return "Subspedia";
	}
	public void caricaElencoSerie() {}
	
	private void scaricaFeed() {
		try {
			Download.downloadFromUrl(URLFeedRSS, "feed_subspedia");
			rss.clear();
			
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			dbfactory.setNamespaceAware(true);
			DocumentBuilder domparser = dbfactory.newDocumentBuilder();
			Document doc = domparser.parse(new File("feed_subspedia"));
			
			NodeList elementi=doc.getElementsByTagName("item");
			for(int i=0;i<elementi.getLength();i++){
				Node item=elementi.item(i);
				NodeList attributi=item.getChildNodes();
				String titolo="", link="";
				for(int j=0;j<attributi.getLength();j++){
					Node attributo=attributi.item(j);
					if(attributo instanceof Element){
						Element attr=(Element)attributo;
						switch(attr.getTagName()){
							case "title":
								titolo=attr.getTextContent();
								//System.out.println(titolo);
								break;
							case "content:encoded":
								if(attr.getTextContent().contains("a href")){
									if(attr.getTextContent().contains(".zip")){
										link=attr.getTextContent().substring(attr.getTextContent().indexOf("a href")+"a href".length()+2, attr.getTextContent().indexOf(".zip")+".zip".length());
									}
								}
								break;
						}
					}
				}
				rss.add(new SubsPediaRSSItem(titolo, link));
			}
			OperazioniFile.deleteFile("feed_subspedia");
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
	private void stampaFeed(){
		for(int i=0;i<rss.size();i++){
			System.out.println(rss.get(i));
		}
	}
	
	public static void main(String[] args) {
		Subspedia subs=new Subspedia();
		subs.scaricaFeed();
		subs.stampaFeed();
	}

}
