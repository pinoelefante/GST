package InfoManager;

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

import Programma.ManagerException;

public class TheTVDB {
	private static String APIKEY="294AFD865CEB421D";
	
	//private static final int MIRROR_MASK_XML=1, MIRROR_MASK_BANNER=2, MIRROR_MASK_ZIP=4;
	private static String API_MIRROR_PATH="http://thetvdb.com/api/"+APIKEY+"/mirrors.xml";
	private static ArrayList<Mirror> list_mirrors;
	
	private static long current_time_server;
	private static String API_CURRENT_TIME="http://thetvdb.com/api/Updates.php?type=none";
	
	private static String API_IMAGE="<mirror_path>/banners/<path_image>";
	
	private static String API_SERIE="<mirrorpath>/api/GetSeries.php?seriesname=<seriesname>";
	private static String API_SERIE_LINGUA="<mirrorpath>/api/GetSeries.php?seriesname=<seriesname>&language=<language>";
	
	public static void main(String[] args){
		System.out.println(API_MIRROR_PATH);
		caricaMirrors();
		
		ArrayList<SerieTVDB> serie=getSeries("Arrow");
		for(int i=0;i<serie.size();i++)
			System.out.println(serie.get(i));
		//stampaMirrors();
	}
	
	public static void caricaMirrors() {
		if(list_mirrors==null)
			list_mirrors=new ArrayList<Mirror>(2);
		try {
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domparser = dbfactory.newDocumentBuilder();
			Document doc = domparser.parse(API_MIRROR_PATH);
			
			NodeList elementi=doc.getElementsByTagName("Mirror");
			for(int i=0;i<elementi.getLength();i++){
				Node mirror=elementi.item(i);
				NodeList attributi=mirror.getChildNodes();
				String mirror_path="";
				int id=0, mask=0;
				for(int j=0;j<attributi.getLength();j++){
					Node attributo=attributi.item(j);
					if(attributo instanceof Element){
						Element attr=(Element)attributo;
						switch(attr.getTagName()){
							case "id":
								id=Integer.parseInt(attr.getTextContent());
								break;
							case "mirrorpath":
								mirror_path=attr.getTextContent();
								break;
							case "typemask":
								mask=Integer.parseInt(attr.getTextContent());
								break;
						}
					}
				}
				Mirror newMirror=new Mirror(id, mirror_path, mask);
				list_mirrors.add(newMirror);
			}
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
	public static void stampaMirrors(){
		for(int i=0;i<list_mirrors.size();i++)
			System.out.println(list_mirrors.get(i));
	}
	public static Mirror getXMLMirror(){
		if(list_mirrors!=null){
			for(int i=0;i<list_mirrors.size();i++){
				Mirror mirror=list_mirrors.get(i);
				if(mirror.isXML())
					return mirror;
			}
		}
		return null;
	}
	public static Mirror getBannerMirror(){
		if(list_mirrors!=null){
			for(int i=0;i<list_mirrors.size();i++){
				Mirror mirror=list_mirrors.get(i);
				if(mirror.isBanner())
					return mirror;
			}
		}
		return null;
	}
	public static Mirror getZipMirror(){
		if(list_mirrors!=null){
			for(int i=0;i<list_mirrors.size();i++){
				Mirror mirror=list_mirrors.get(i);
				if(mirror.isZip())
					return mirror;
			}
		}
		return null;
		
	}
	public static long getCurrentTimeServer(){
		return current_time_server;
	}
	public static ArrayList<SerieTVDB> getSeries(String nomeserie){ 
		//TODO ricerca e inserimento nel database
		try {
			Mirror mirror=getXMLMirror();
			if(mirror==null)
				return null;
			
			String API_PATH=API_SERIE.replace("<seriesname>", nomeserie).replace("<mirrorpath>", mirror.getUrl());
			
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder domparser = dbfactory.newDocumentBuilder();
			Document doc = domparser.parse(API_PATH);
			
			ArrayList<SerieTVDB> serie_trovate=new ArrayList<SerieTVDB>(1);
			
			NodeList elementi=doc.getElementsByTagName("Series");
			for(int i=0;i<elementi.getLength();i++){
				Node serie=elementi.item(i);
				NodeList attributi=serie.getChildNodes();
				String banner_path="", descrizione="", data_inizio="", nome_serie="";
				int id=0;
				for(int j=0;j<attributi.getLength();j++){
					Node attributo=attributi.item(j);
					if(attributo instanceof Element){
						Element attr=(Element)attributo;
						switch(attr.getTagName()){
							case "id":
							case "seriesid":
								id=Integer.parseInt(attr.getTextContent());
								break;
							case "banner":
								banner_path=attr.getTextContent();
								break;
							case "Overview":
								descrizione=attr.getTextContent();
								break;
							case "FirstAired":
								data_inizio=attr.getTextContent();
								break;
							case "SeriesName":
								nome_serie=attr.getTextContent();
								break;
						}
					}
				}
				SerieTVDB newSerie=new SerieTVDB(id, nome_serie, descrizione, banner_path, data_inizio);
				serie_trovate.add(newSerie);
			}
			return serie_trovate;
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
		return null;
	}
	public static ArrayList<ActorTVDB> getActors(SerieTVDB serie){//
		return null;
	}
}
