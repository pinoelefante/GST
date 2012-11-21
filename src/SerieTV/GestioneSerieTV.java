package SerieTV;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Database.*;
import Programma.Download;
import Programma.OperazioniFile;
import Sottotitoli.GestoreSottotitoli;

public class GestioneSerieTV {
	private static ArrayList<SerieTV> serietv=new ArrayList<SerieTV>();
	private static ArrayList<SerieTV> serietv_inserite=new ArrayList<SerieTV>();
	private static GestoreSottotitoli submanager=new GestoreSottotitoli();
	
	public static void carica_serie_database(){
		ArrayList<SQLParameter[]>res=Database.select(Database.TABLE_SERIETV, null, "AND", "=", (" ORDER BY "+"nome"+" ASC"));
		for(int i=0;i<res.size();i++){
			String nome ="", url="", id_subsfactory="";
			int id=0, stato=0, end=0, inserita=0, id_itasa=0;
			for(int j=0;j<res.get(i).length;j++){
				SQLParameter p=res.get(i)[j];
				switch(p.ptype()){
					case SQLParameter.INTEGER:{
						int val=(Integer)p.pvalue();
						switch(p.getField()){
							case "id":
								id=val;
							break;
							case "stato":
								stato=val;
							break;
							case "end":
								end=val;
							break;
							case "inserita":
								inserita=val;
							break;
							case "id_itasa":
								id_itasa=val;
							break;
						}
						break;
					}
					case SQLParameter.TEXT:{
						String val=p.pvalueAsString();
						switch(p.getField()){
							case "nome":
								nome=val;
							break;
							case "url":
								url=val;
							break;
							case "directory_subsfactory":
								id_subsfactory=val;
							break;
						}
					}
					break;
				}
			}
			SerieTV st=new SerieTV(nome, url);
			st.setItasaID(id_itasa);
			st.setSubsfactoryID(id_subsfactory);
			st.setIDDB(id);
			st.setEnd(end);
			st.setInserita(inserita);
			st.setStato(stato);
			serietv.add(st);
			if(inserita>0){
				if(!isSeriePresente(getElencoSerieInserite(), st.getUrl())){
					serietv_inserite.add(st);
					carica_episodi_serie(st);
				}
			}
		}
	}
	@SuppressWarnings("unused")
	public static void carica_episodi_serie(SerieTV st){
		SQLParameter[] cond=new SQLParameter[1];
		cond[0]=new SQLParameter(SQLParameter.INTEGER, st.getIDDB(), "id_serie");
		ArrayList<SQLParameter[]> res=Database.select(Database.TABLE_TORRENT, cond, "AND", "==", ("ORDER BY "+"serie"+" ASC, "+"episodio"+" ASC"));
		
		st.resetEpisodi();
		for(int i=0;i<res.size();i++){
			int id_serie=0, vista=0, serie=0, episodio=0, hd720p=0, repack=0, preair=0, proper=0, sottotitolo=0;
			String magnet="";
			for(int j=0;j<res.get(i).length;j++){
				SQLParameter p=res.get(i)[j];
				switch(p.ptype()){
					case SQLParameter.INTEGER:{
						int val=(Integer)p.pvalue();
						switch(p.getField()){
							case "id":
							break;
							case "id_serie":
								id_serie=val;
							break;
							case "vista":
								vista=val;
							break;
							case "serie":
								serie=val;
							break;
							case "episodio":
								episodio=val;
							break;
							case "HD720p":
								hd720p=val;
							break;
							case "repack":
								repack=val;
							break;
							case "preair":
								preair=val;
							break;
							case "proper":
								proper=val;
							break;
							case "sottotitolo":
								sottotitolo=val;
							break;
						}
					}
					break;
					case SQLParameter.TEXT:{
						String val=p.pvalueAsString();
						switch(p.getField()){
							case "magnet":
								magnet=val;
							break;
						}
					}
					break;
				}
			}
			Torrent t=new Torrent(magnet, vista, st.getNomeSerie(), st.getIDDB());
			t.setSottotitolo(sottotitolo==1?true:false, false);
			t.set720p(hd720p==1?true:false);
			t.setRepack(repack==1?true:false);
			t.setProper(proper==1?true:false);
			t.setStagione(serie);
			t.setEpisodio(episodio);
			t.setPreair(preair==1?true:false);
			st.addEpisodioFromDB(t);
		}
	}
	public static void carica_episodi_tutti(){
		for(int i=0;i<serietv_inserite.size();i++)
			carica_episodi_serie(serietv_inserite.get(i));
	}
	public static boolean isSeriePresente(ArrayList<SerieTV> elenco, String url_serie){
		for(int i=0;i<elenco.size();i++){
			if(elenco.get(i).getUrl().compareToIgnoreCase(url_serie)==0)
				return true;
		}
		return false;
	}
	public static ArrayList<SerieTV> getElencoSerieCompleto(){
		return serietv;
	}
	public static ArrayList<SerieTV> getElencoSerieInserite(){
		return serietv_inserite;
	}
	public static SerieTV getSerie(ArrayList<SerieTV> el, String url){
		for(int i=0;i<el.size();i++){
			if(el.get(i).getUrl().compareToIgnoreCase(url)==0)
				return el.get(i);
		}
		return null;
	}
	public static SerieTV getSerieFromName(ArrayList<SerieTV> el, String nome){
		for(int i=0;i<el.size();i++){
			if(el.get(i).getNomeSerie().compareToIgnoreCase(nome)==0)
				return el.get(i);
		}
		return null;
	}
	public static boolean Showlist() {
		serietv.clear();
		carica_serie_database();
		
		try {
			Download.downloadFromUrl("http://eztv.it/showlist/", "file.html");
		}
		catch (IOException e1) {
			return false;
		}
		FileReader f_r=null;
		Scanner file=null;
		try {
			f_r=new FileReader("file.html");
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
					if(!isSeriePresente(getElencoSerieCompleto(), url)){
						SerieTV st=new SerieTV(nomeserie, url);
						st.setStato(stato);
						st.InsertInDB();
						serietv.add(st);
					}
					else{
						SerieTV st=getSerie(getElencoSerieCompleto(), url);
						if(st!=null){
							if(st.getEnd()==0){
								if(st.getStato()!=stato){
									st.setStato(stato);
									st.UpdateDB();
								}
							}
						}
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			return false;
		}
		finally{
			file.close();
			try {
				f_r.close();
			}
			catch (IOException e) {	e.printStackTrace(); }
		}
		OperazioniFile.deleteFile("file.html");
		return true;
	}
	public static void aggiornaListeTorrent(){
		class AggiornaTList extends Thread{
			ArrayList<Thread> tr_up=new ArrayList<Thread>();
			public void run(){
				for(int i=0;i<serietv_inserite.size();i++){
					SerieTV st=serietv_inserite.get(i);
					if(!st.isEnd()){
						System.out.println("Aggiornando: "+st.getNomeSerie());
						tr_up.add(st.aggiornaTorrentList());
					}
				}
				try {
					while(tr_up.size()>0){
						for(int i=0;i<tr_up.size();){
							Thread t=tr_up.get(i);
							if(!t.isAlive())
								tr_up.remove(t);
							else
								i++;
						}
						sleep(500);
					}
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Thread t=new AggiornaTList();
		t.start();
		
		try {
			t.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<Torrent> getTorrentDownload(){
		ArrayList<Torrent> scaricare=new ArrayList<Torrent>();
		for(int i=0;i<serietv_inserite.size();i++){
			SerieTV st=serietv_inserite.get(i);
			scaricare.addAll(st.daScaricare());
		}
		return scaricare;
	}
	public static boolean aggiungiSerie(SerieTV st){
		if(st==null)
			return false;;
		if(!isSeriePresente(getElencoSerieInserite(), st.getUrl())){
			st.setInserita(1);
			st.UpdateDB();
			for(int i=0;i<getElencoSerieInserite().size();i++){
				SerieTV s=getElencoSerieInserite().get(i);
				if(s.getNomeSerie().compareToIgnoreCase(st.getNomeSerie())>0){
					getElencoSerieInserite().add(i, st);
					return true;
				}
			}
			getElencoSerieInserite().add(st);
			return true;
		}
		return false;
	}
	public static boolean rimuoviSerie(SerieTV st){
		if(st==null)
			return false;
		st.setInserita(0);
		st.UpdateDB();
		getElencoSerieInserite().remove(st);
		return true;
	}
	public static GestoreSottotitoli getSubManager(){
		return submanager;
	}
}
