package InfoSerie;

import java.io.File;
import java.io.IOException;

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

public class TVRage implements ProviderInfo {
	private static String apikey="ZmOBeDs4Pd0FPS5OyooV";
	
	private static String API_SEARCH="http://services.tvrage.com/myfeeds/search.php?key={api_key}&show={show_search}";
	//private static String API_EPISODELIST="http://services.tvrage.com/myfeeds/episode_list.php?key={api_key}&sid={show_id}";
	//private static String API_SHOWINFO="http://services.tvrage.com/myfeeds/showinfo.php?key={api_key}&sid={show_id}";
	
	public TVRage() {}

	@Override
	public Object cercaEpisodio(Object id, int serie, int episodio) throws EpisodioNotFound {
		return null;
	}
	public synchronized Object cercaSerie(String nome) throws SerieNotFound{
		
		try {
			Download.downloadFromUrl(API_SEARCH.replace("{api_key}", apikey).replace("{show_search}", nome.replace(" ", "%20")), "search_res.xml");
		}
		catch (IOException e1) {
			ManagerException.registraEccezione(e1);
			e1.printStackTrace();
			throw new SerieNotFound("TVRage - errore download: "+nome);
		}
		
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder domparser = null;
		try {
			domparser = dbfactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e1) {
			ManagerException.registraEccezione(e1);
			e1.printStackTrace();
		}
	    
	    Document doc = null;
		try {
			doc = domparser.parse(new File("search_res.xml"));
		}
		catch (SAXException e1) {
			ManagerException.registraEccezione(e1);
			e1.printStackTrace();
		}
		catch (IOException e1) {
			ManagerException.registraEccezione(e1);
			e1.printStackTrace();
		}
		OperazioniFile.deleteFile("search_res.xml");
	    
	    NodeList elementi=doc.getElementsByTagName("show");
	    for(int i=0;i<elementi.getLength();i++){
	    	Node nodo=elementi.item(i);
	    	NodeList attributi=nodo.getChildNodes();
	    	Integer id=0;
	    	String nome_serie="";
	    	for(int j=0;j<attributi.getLength();j++){
	    		Node attr=attributi.item(j);
	    		if(attr instanceof Element){
	    			Element e=(Element)attr;
	    			switch(e.getTagName()){
	    				case "showid":
	    					id=Integer.parseInt(e.getTextContent());
	    					break;
	    				case "name":
	    					nome_serie=e.getTextContent();
	    					if(nome_serie.compareToIgnoreCase(nome)==0 && id!=0)
	    						return id;
	    					break;
	    				case "seasons":
	    					//Integer.parseInt(e.getTextContent());
	    					break;
	    			}
	    		}
	    	}
	    	id=0;
	    	nome_serie="";
	    }
		throw new SerieNotFound("TVRage - Serie non trovata: "+nome);
	}

	@Override
	public String scaricaTrailer(Object id, int serie, int episodio) {
		// TODO Auto-generated method stub
		return null;
	}

}
