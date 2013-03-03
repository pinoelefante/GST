package SerieTV;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import Database.*;
import GUI.Interfaccia;
import GUI.ThreadModificaLabel;
import Programma.Download;
import Programma.ManagerException;
import Programma.OperazioniFile;
import StruttureDati.ElencoIndicizzato;
import StruttureDati.ElencoIndicizzatoImpl2;
import StruttureDati.Indexable;

public class SerieTV {
	public final static int STATO_CONCLUSA=1;
	private int id_database;
	private String	nome_serie;
	private String	url_eztv;
	private ElencoIndicizzato 	episodi;
	private int		id_itasa=-1;
	private String 	directory_subsfactory="";
	private int		stato; //1 se la serie è Ended altrimenti 0
	private int 	inserita;
	private int		end; //1 se sono stati scaricati tutti gli episodi e la serie è ended. Setta da non cercare altri episodi
	private int		tvrage;

	public SerieTV(String nome, String url) {
		setNomeSerie(nome);
		this.url_eztv = url;
		episodi=new ElencoIndicizzatoImpl2();
	}
	public SerieTV(String nome, String url, boolean insert_db) {
		setNomeSerie(nome);
		this.url_eztv = url;
		episodi=new ElencoIndicizzatoImpl2();
		InsertInDB();
	}
	public void setIDDB(int id){
		id_database=id;
	}
	private void getIDFromDB() {
		SQLParameter[] par=new SQLParameter[1];
		par[0]=new SQLParameter(SQLParameter.TEXT, getUrl(), "url");
		ArrayList<SQLParameter[]>res=Database.select(Database.TABLE_SERIETV, par, "", "=");
		if(res!=null){
			for(int i=0;i<res.size();i++){
				SQLParameter[] parametri=res.get(i);
				for(int j=0;j<parametri.length;j++){
					switch(parametri[j].getField()){
						case "id":
							id_database=(Integer)parametri[j].pvalue();
					}
				}
			}
		}
	}
	public ElencoIndicizzato getEpisodi(){
		return episodi;
	}
	public String getNomeSerie() {
		return this.nome_serie;
	}

	public void setNomeSerie(String nome_serie) {
		this.nome_serie = nome_serie;
		nome_the();
	}
	private void nome_the(){
		if(nome_serie.endsWith(", The"))
			nome_serie="The "+nome_serie.substring(0, nome_serie.length()-", The".length());
	}
	public int getIDDB() {
		return id_database;
	}
	public String getUrl() {
		return this.url_eztv;
	}
	public void setUrl(String url_eztv) {
		this.url_eztv = url_eztv;
	}
	public boolean addEpisodio(Torrent t){
		if(episodi.get(t.getOffKey())==null){
			//t.parseMagnet();
			episodi.add(t);
			t.insert();
			return true;
		}
		return false;
	}
	public void addEpisodioFromDB(Torrent t){
		episodi.add(t);
	}
	public void resetEpisodi(){
		episodi.removeAll();
	}
	
	public String getNomeSerieFile() {
		return getNomeSerieFolder();
	}
	public ArrayList<Torrent> daScaricare() {
		ArrayList<Torrent> scaricare=new ArrayList<Torrent>();
		ArrayList<Indexable> ind=episodi.getLinear();
		for(int i=0;i<ind.size();i++){
			Torrent t=(Torrent) ind.get(i);
			if(!t.isScaricato())
				scaricare.add(t);
		}
		return scaricare;
	}
	
	public Thread aggiornaTorrentList() {
		class update extends Thread {
			public void run() {
				try {
					Download.downloadFromUrl("http://eztv.it" + getUrl(), getNomeSerieFile());
					Interfaccia.download_label_notifiche.setText("");
				}
				catch (IOException e) {
					new ThreadModificaLabel(Interfaccia.download_label_notifiche, "  ERROR: Update later", 1000);
					ManagerException.registraEccezione(e);
					return;
				}
				parse();
				if(getStato()==STATO_CONCLUSA){
					setEnd(1);
					UpdateDB();
				}
				Runtime.getRuntime().gc();
			}

			private void parse() {
				FileReader reader;
				try {
					reader = new FileReader(SerieTV.this.getNomeSerieFile());
				}
				catch (FileNotFoundException e) {
					ManagerException.registraEccezione(e);
					return;
				}
				Scanner file_read = new Scanner(reader);
				while (file_read.hasNextLine()) {
					String linea = file_read.nextLine();
					if (linea.contains("magnet:?xt=urn:btih:")) {
						int inizio = linea.indexOf("magnet:?xt=urn:btih:");
						int fine = linea.indexOf("\" class=\"magnet\"");
						String url_magnet = linea.substring(inizio, fine);
						Torrent t=new Torrent(url_magnet, Torrent.SCARICARE, getNomeSerie(), getIDDB());
						t.parseMagnet();
						/*
						if(t.is720p() && !Settings.isMostra720p()){
							t.setScaricato(Torrent.IGNORATO, true);
						}
						if(t.isPreAir() && !Settings.isMostraPreair()){
							t.setScaricato(Torrent.IGNORATO, true);
						}
						*/
						if(!addEpisodio(t))
							t=null;
					}
				}
				file_read.close();
				OperazioniFile.deleteFile(getNomeSerieFile());
			}
		};
		update thread = new update();
		thread.start();
		return thread;
	}

	public String toString() {
		return getNomeSerie();
	}

	public String getNomeSerieFolder() {
		return nome_serie.replace(":", "-").replace("?", "").replace("/", "-").replace("\\", "-").replace("*", "").replace("<", "").replace(">", "").replace("|", "").replace("\"", "");
	}

	public int getItasaID() {
		return id_itasa;
	}
	public void setItasaID(int id_itasa) {
		this.id_itasa = id_itasa;
	}
	public String getSubsfactoryDirectory() {
		return directory_subsfactory;
	}
	public void setSubsfactoryID(String dir_subsfactory) {
		directory_subsfactory = dir_subsfactory;
	}
	
	public Torrent getTorrentBySeasonAndEpisode(int season, int ep, boolean is720p){
		ArrayList<Indexable> el=episodi.getLinear();
		for(int i=0;i<el.size();i++){
			Torrent t=(Torrent)el.get(i);
			if(t.getSerie()==season && t.getPuntata()==ep && t.is720p()==is720p)
				return t;
		}
		return null;
	}
	
	public int getStato() {
		return stato;
	}
	public void setStato(int stato) {
		this.stato = stato;
	}
	public int getInserita() {
		return inserita;
	}
	public void setInserita(int inserita) {
		this.inserita = inserita;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public void InsertInDB() {
		if(id_database==0){
			SQLParameter[] par=new SQLParameter[7];
			par[0]=new SQLParameter(SQLParameter.TEXT, getNomeSerie(), "nome");
			par[1]=new SQLParameter(SQLParameter.TEXT, getUrl(), "url");
			par[2]=new SQLParameter(SQLParameter.INTEGER, getStato(), "stato");
			par[3]=new SQLParameter(SQLParameter.INTEGER, getEnd(), "end");
			par[4]=new SQLParameter(SQLParameter.INTEGER, getInserita(), "inserita");
			par[5]=new SQLParameter(SQLParameter.INTEGER, getItasaID(), "id_itasa");
			par[6]=new SQLParameter(SQLParameter.TEXT, getSubsfactoryDirectory(), "directory_subsfactory");
			
			Database.insert(Database.TABLE_SERIETV, par);
			
			getIDFromDB();
		}
		else
			UpdateDB();
	}
	public void UpdateDB() {
		SQLParameter[] par=new SQLParameter[8];
		par[0]=new SQLParameter(SQLParameter.TEXT, getNomeSerie(), "nome");
		par[1]=new SQLParameter(SQLParameter.TEXT, getUrl(), "url");
		par[2]=new SQLParameter(SQLParameter.INTEGER, getStato(), "stato");
		par[3]=new SQLParameter(SQLParameter.INTEGER, getEnd(), "end");
		par[4]=new SQLParameter(SQLParameter.INTEGER, getInserita(), "inserita");
		par[5]=new SQLParameter(SQLParameter.INTEGER, getItasaID(), "id_itasa");
		par[6]=new SQLParameter(SQLParameter.TEXT, getSubsfactoryDirectory(), "directory_subsfactory");
		par[7]=new SQLParameter(SQLParameter.INTEGER, getTVRage(), "tv_rage");
		
		SQLParameter[] condizioni=new SQLParameter[1];
		condizioni[0]=new SQLParameter(SQLParameter.TEXT, getUrl(), "url");
		
		Database.update(Database.TABLE_SERIETV, par, condizioni, "AND", "==");
	}
	public boolean isEnd(){
		return getEnd()==1;
	}
	protected boolean verificaVisualizzazioneTutte(){
		ArrayList<Indexable> ind=episodi.getLinear();
		for(int i=0;i<ind.size();i++){
			Torrent t=(Torrent) ind.get(i);
			if(!t.isScaricato())
				return false;
		}
		return true;
	}
	public int getTVRage() {
		return tvrage;
	}
	public void setTVRage(int tvrage) {
		this.tvrage = tvrage;
	}
	public void resetSerie(){
		ArrayList<Indexable> ep=episodi.getLinear();
		for(int i=0;i<ep.size();i++){
			Torrent t=(Torrent)ep.get(i);
			t.setSottotitolo(false, false);
			t.setScaricato(Torrent.SCARICARE);
		}
	}
}
