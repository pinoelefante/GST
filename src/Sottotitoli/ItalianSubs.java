package Sottotitoli;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.security.auth.login.FailedLoginException;

import org.apache.commons.io.IOUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;

import Naming.Renamer;
import Programma.Download;
import Programma.ManagerException;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.GestioneSerieTV;
import SerieTV.Torrent;

public class ItalianSubs implements ProviderSottotitoli{
	public final static int HDTV = 0,	
							HD720p = 1,  
							WEB_DL = 2,
							DVDRIP = 3,
							BLUERAY = 4, 
							BRRIP = 5, 
							FILM = 6;
	
	private static String APIKEY="87c9d52fba19ba856a883b1d3ddb14dd";
	@SuppressWarnings("unused")
	private static String AUTHCODE="";
	
	private static String API_SHOWLIST="https://api.italiansubs.net/api/rest/shows?apikey="+APIKEY;
	private static String API_SUB_GETID = "https://api.italiansubs.net/api/rest/subtitles/search?q=<QUERY>&show_id=<SHOW_ID>&version=<VERSIONE>&apikey="+APIKEY;
	private static String API_LOGIN="https://api.italiansubs.net/api/rest/users/login?username=<USERNAME>&password=<PASSWORD>&apikey="+APIKEY;
	
	private ArrayList<ItalianSubsRSSItem> feed_rss; 
	private ArrayList<SerieSub> elenco_serie;
	
	private WebClient webClient;
	private boolean login_itasa=false;
	private boolean locked=true;
	private Thread LoggerItasa;
	private GregorianCalendar RSS_UltimoAggiornamento;
	private final long update_time_rss=900000L;  //15 minuti
	private static int download_corrente=0;
	
	public ItalianSubs(){
		feed_rss=new ArrayList<ItalianSubsRSSItem>();
		elenco_serie=new ArrayList<SerieSub>();
	
		caricaElencoSerie();
		loggaItasa();
	}
	public boolean isLocked(){
		return locked;
	}
	public void attendiUnlock(){
		try {
			LoggerItasa.join();
			locked=false;
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	
	public boolean scaricaSottotitolo(Torrent t) {
		int id_itasa=GestioneSerieTV.getSerieFromName(GestioneSerieTV.getElencoSerieInserite(), t.getNomeSerie()).getItasaID();
		try {
			if(id_itasa<=0)
				return false;
			int id=cercaIDSottotitoloFromAPI(id_itasa, t.getSerie(), t.getPuntata(), t.is720p()?HD720p:HDTV);
			scaricaSub(id, Renamer.generaNomeDownload(t), t.getNomeSerieFolder());
			t.setSottotitolo(false, true);
			return true;
		}
		catch (ItasaSubNotFound e) {
			//ManagerException.registraEccezione(e);
			int id_s=cercaFeed(id_itasa, t);
			if(id_s<=0)
				return false;
			try {
				scaricaSub(id_s, Renamer.generaNomeDownload(t), t.getNomeSerieFolder());
				t.setSottotitolo(false, true);
			}
			catch (FailedLoginException | FailingHttpStatusCodeException | IOException e1) {
				e1.printStackTrace();
				ManagerException.registraEccezione(e);
				return false;
			}
			return true;
		}
		catch (FailedLoginException e) {
			ManagerException.registraEccezione(e);
			return false;
		}
		catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
			return false;
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
			return false;
		}
		catch (IOException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
			return false;
		}
		
	}
	private int cercaFeed(int iditasa, Torrent t){
		if(verificaTempo(update_time_rss, RSS_UltimoAggiornamento)){
			System.out.println("Aggiornando il feed RSS - Italiansubs.net");
			aggiornaFeedRSS();
		}
		for(int i=0;i<feed_rss.size();i++){
			ItalianSubsRSSItem rss=feed_rss.get(i);
			if(rss.getIDSerie()==iditasa){
				if(rss.is720p()==t.is720p()){
					if(rss.isNormale()==!t.is720p()){
						if(rss.getStagione()==t.getSerie()){
							if(rss.getEpisodio()==t.getPuntata()){
								return rss.getIDSub();
							}
						}
					}
				}
			}
		}
		return -1;
	}
	private void aggiornaFeedRSS(){
		RSS_UltimoAggiornamento=new GregorianCalendar();
		feed_rss.clear();
		try {
			Download.downloadFromUrl("http://feeds.feedburner.com/ITASA-Ultimi-Sottotitoli", "feed_itasa");
			FileReader f_r=new FileReader("feed_itasa");
			Scanner file=new Scanner(f_r);
			while(file.hasNextLine()){
				String riga=file.nextLine().trim();
				if(riga.contains("<item>")){
					String linea=file.nextLine().trim();
					String nome="", url="";
					boolean n_done=false, 
							u_done=false;
					while(!linea.contains("</item>")){
						if(linea.contains("<title>")){
							nome=linea.replace("<title>", "").replace("</title>", "").trim();
							n_done=true;
						}
						else if(linea.startsWith("<guid")){
							//url=linea.replace("<link>", "").replace("</link>", "").trim();
							url=linea.substring(linea.indexOf("\">")+2, linea.indexOf("</guid>")).replace("&amp;", "&");
							u_done=true;
						}
						if(u_done && n_done){
							ItalianSubsRSSItem sub=new ItalianSubsRSSItem(this, nome, url);
							feed_rss.add(sub);
							u_done=false;
							n_done=false;
						}
						linea=file.nextLine().trim();
					}
				}
			}
			file.close();
			f_r.close();
			OperazioniFile.deleteFile("feed_itasa");
		} 
		catch (IOException e) {
			ManagerException.registraEccezione(e);
		}
	}
	
	@SuppressWarnings("resource")
	private int cercaIDSottotitoloFromAPI(int show_id, int serie, int episodio, int tipo) throws ItasaSubNotFound {
		String query = serie + "x"	+ (episodio < 10 ? "0" + episodio : episodio); 
		String tipo_sub = "Normale";
		switch (tipo) {
			case HDTV:
				tipo_sub = "Normale";
			break;
			case HD720p:
				tipo_sub = "720p";
			break;
		}
		String url_query = API_SUB_GETID.replace("<QUERY>", query).replace("<VERSIONE>", tipo_sub).replace("<SHOW_ID>", show_id	+ "");

		FileReader f_r = null;
		Scanner file = null;
		int id = -1;
		try {
			int id_r;
			synchronized (this) {
				id_r=download_corrente;
				download_corrente++;
			}
			Download.downloadFromUrl(url_query, "response_sub_"+id_r);
			f_r = new FileReader("response_sub_"+id_r);
			file = new Scanner(f_r);
			while (file.hasNextLine()) {
				String linea = file.nextLine().trim();
				if (linea.startsWith("<root>")) {
					String n_sub = linea.substring(linea.indexOf("<count>"), linea.indexOf("</count>")).replace("<count>", "").trim();
					int n = Integer.parseInt(n_sub);
					if (n == 0)
						throw new ItasaSubNotFound("Non sono presenti sottotitoli per questa puntata");
					else if (n > 0) {
						String id_s = linea.substring(linea.indexOf("<id>"), linea.indexOf("</id>")).replace("<id>", "").trim();
						id = Integer.parseInt(id_s);
						break;
					}
				}
			}
			file.close();
			f_r.close();
			OperazioniFile.deleteFile("response_sub_"+id_r);
		}
		catch (IOException e) {
			ManagerException.registraEccezione(e);
			throw new ItasaSubNotFound("Itasa exception - probabile errore di connessione");
		}
		return id;
	}
	public void caricaElencoSerie(){
		ArrayList<SerieSub> elenco=new ArrayList<SerieSub>();
		try {
			Download.downloadFromUrl(API_SHOWLIST, "response_itasa");
			FileReader f_r=new FileReader("response_itasa");
			Scanner file=new Scanner(f_r);
			while(file.hasNextLine()){
				String linea=file.nextLine().trim();
				if(!linea.startsWith("<root>"))
					continue;
				linea=linea.substring(linea.indexOf("<show>"));
				do{
					String parse="";
					parse=linea.substring(linea.indexOf("<show>"), linea.indexOf("</show>")+"</show>".length());
					int id=Integer.parseInt(parse.substring(parse.indexOf("<id>")+"<id>".length(), parse.indexOf("</id>")));
					String nome=parse.substring(parse.indexOf("<name>")+"<name>".length(), parse.indexOf("</name>"));
					nome=nome.replace("&amp;", "&");
					SerieSub s=new SerieSub(nome, id);
					elenco.add(s);
					linea=linea.substring(parse.length());
					if(linea.startsWith("</shows>"))
						break;
				}while(true);
			}
			file.close();
			f_r.close();
			OperazioniFile.deleteFile("response_itasa");
			if(elenco.size()>0){
				elenco_serie.clear();
				elenco_serie.addAll(elenco);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	public boolean VerificaLogin(String username, String password){
		String url_login=API_LOGIN.replace("<USERNAME>", username).replace("<PASSWORD>", password);
		boolean stato=false;
		
		FileReader f_r=null;
		Scanner file=null;
		try {
			Download.downloadFromUrl(url_login, "response_login");
			f_r=new FileReader("response_login");
			file=new Scanner(f_r);
			while(file.hasNextLine()){
				String linea=file.nextLine().trim();
				if(linea.startsWith("<root>")){
					String status=linea.substring(linea.indexOf("<status>"), linea.indexOf("</status>")).trim().replace("<status>", "");
					if(status.compareToIgnoreCase("success")==0){
						AUTHCODE=linea.substring(linea.indexOf("<authcode>"), linea.indexOf("</authcode>")).trim().replace("<authcode>", "").trim();
						stato=true;
					}
					else{
						AUTHCODE="";
						stato=false;
					}
					break;
				}
			}
		}
		catch (IOException e) {	
			ManagerException.registraEccezione(e);
		}
		finally{
			file.close();
			try {
				f_r.close();
			}
			catch (IOException e) {	
				ManagerException.registraEccezione(e);
			}
			OperazioniFile.deleteFile("response_login");
		}
		return stato;
	}
	public void loggaItasa() {
		class LoggerItasa extends Thread{
			public void run(){
				if (webClient == null){
					webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
					webClient.setActiveXNative(false);
					webClient.setAppletEnabled(false);
					webClient.setCssEnabled(false);
					webClient.setJavaScriptEnabled(false);
				}
				
				try {
					webClient.getCookieManager().clearCookies();
					
					final int NORMALE=2, CONDIVISO=1, NOUSER=0;
					int login=NOUSER;
					
					if(!Settings.getItasaUsername().isEmpty() && !Settings.getItasaPassword().isEmpty()){
						if(VerificaLogin(Settings.getItasaUsername(), Settings.getItasaPassword())){ //utente loggato come utente proprietario
							login=NORMALE;
						}
					}
					else if(VerificaLogin("GestioneSerieTV", "gestione@90")){
						login=CONDIVISO;
					}
					else{
						login=NOUSER;
						login_itasa=false;
						return;
					}
					
					HtmlPage page1 = (HtmlPage) webClient.getPage("http://www.italiansubs.net");
					HtmlForm form = page1.getFormByName("login");
					HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByName("Submit");
					HtmlTextInput textField = (HtmlTextInput) form.getInputByName("username");
					HtmlPasswordInput textField2 = (HtmlPasswordInput) form.getInputByName("passwd");
					
					textField.setValueAttribute(login==NORMALE?Settings.getItasaUsername():"GestioneSerieTV");
					textField2.setValueAttribute(login==NORMALE?Settings.getItasaPassword():"gestione@90");
					button.click();
					
					webClient.closeAllWindows();
					webClient.getCache().clear();
					login_itasa=true;
					locked=false;
				}
				catch (FailingHttpStatusCodeException | IOException e) {
					e.printStackTrace();
					login_itasa=false;
					ManagerException.registraEccezione(e);
				}
			}
		}
		LoggerItasa=new LoggerItasa();
		LoggerItasa.start();
	}
	public boolean isLogged(){
		try {
			LoggerItasa.join();
			return login_itasa;
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
			return false;
		}
		
	}
	private void scaricaSub(int id, String nome, String cartella) throws FailingHttpStatusCodeException, MalformedURLException, IOException, FailedLoginException {
		if(!isLogged())
			throw new FailedLoginException("Utente non loggato");
		if(nome.length()<=0)
			throw new IOException("Nome file destinazione vuoto");
		
		WebClient client=new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
		client.setActiveXNative(false);
		client.setAppletEnabled(false);
		client.setCssEnabled(false);
		client.setJavaScriptEnabled(false);
		
		Set<Cookie> cook=webClient.getCookieManager().getCookies();
		Iterator<Cookie> it_cook=cook.iterator();
		while(it_cook.hasNext()){
			Cookie c=it_cook.next();
			client.getCookieManager().addCookie(c);
		}
		
		HtmlPage esempio = null;
		try {
			esempio = (HtmlPage) client.getPage("http://www.italiansubs.net/index.php?option=com_remository&Itemid=6&func=fileinfo&id="+id);
		}
		catch (FailingHttpStatusCodeException | IOException e1) {
			e1.printStackTrace();
			ManagerException.registraEccezione(e1);
		}

		@SuppressWarnings("rawtypes")
		List list = esempio.getByXPath("//dt[1]/center/a/@href");
		for (int i = 0; i < list.size(); i++) {
			DomNode node = (DomNode) list.get(i);
			String sub = node.getTextContent();
			System.out.println(sub);
			if (!sub.equalsIgnoreCase("http://www.italiansubs.net")) {
				WebResponse wr = client.getPage(sub).getWebResponse();
				
				String fileCompleto = Settings.getDirectoryDownload()+File.separator+nome;
				File cartella_download=new File(Settings.getDirectoryDownload() + File.separator+cartella);
				
				if(!cartella_download.exists()){
					System.out.println("Cartella "+cartella_download.getAbsolutePath()+" non esistente");
					cartella_download.mkdir();
				}
				
				BufferedInputStream in = new BufferedInputStream(wr.getContentAsStream());
				FileOutputStream output = new FileOutputStream(fileCompleto);
				IOUtils.copy(in, output);
				output.close();
				in.close();
		
				OperazioniFile.copyfile(fileCompleto, cartella_download.getAbsolutePath()+File.separator+nome);
				OperazioniFile.deleteFile(fileCompleto);
			}
		}
	}
	int cercaSerie(String nome){
		for(int i=0;i<elenco_serie.size();i++){
			SerieSub s=elenco_serie.get(i);
			if(s.getNomeSerie().compareToIgnoreCase(nome)==0)
				return (int)s.getID();
		}
		return -1;
	}
	public String toStringFeed(){
		String str="";
		for(int i=0;i<feed_rss.size();i++)
			str+=feed_rss.get(i).toString()+"\n";
		
		return str;
	}
	public String toString(){
		String str="";
		for(int i=0;i<elenco_serie.size();i++)
			str+=elenco_serie.get(i).getNomeSerie()+" - "+(Integer)elenco_serie.get(i).getID()+"\n";
		return str;
	}
	@Override
	public String getIDSerieAssociata(String nome_serie) {
		for(int i=0;i<elenco_serie.size();i++)
			if(elenco_serie.get(i).getNomeSerie().compareToIgnoreCase(nome_serie)==0)
				return ((int)elenco_serie.get(i).getID())+"";
		return null;
	}

	@Override
	public boolean cercaSottotitolo(Torrent t) {
		System.out.println("ITASA "+t.toString());
		int id_itasa=GestioneSerieTV.getSerieFromName(GestioneSerieTV.getElencoSerieInserite(), t.getNomeSerie()).getItasaID();
		if(id_itasa>0){
			int api_search=-1;
			int feed_search=-1;
			try {
				api_search=cercaIDSottotitoloFromAPI(id_itasa, t.getSerie(), t.getPuntata(), t.is720p()?HD720p:HDTV);
				if(api_search>0){
					System.out.println("ITASA - Sottotitolo trovato tramite API");
					return true;
				}
			}
			catch (ItasaSubNotFound e) {
				System.out.println("ITASA - Sottotitolo non trovato");
				ManagerException.registraEccezione(e);
			}
			feed_search=cercaFeed(id_itasa, t);
			if(feed_search>0){
				System.out.println("ITASA - Sottotitolo trovato nel feed");
				return true;
			}
			else
				System.out.println("ITASA - Sottotitolo non trovato nel feed");
			
			return false;
		}
		else
			return false;
		
	}

	@Override
	public ArrayList<SerieSub> getElencoSerie() {
		return elenco_serie;
	}

	@Override
	public String getProviderName() {
		return "ItalianSubs.net";
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
}

