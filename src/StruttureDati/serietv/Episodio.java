package StruttureDati.serietv;

import java.util.ArrayList;

import SerieTV.Torrent;

public class Episodio {
	private int episodio, stagione;
	private String nome_serie=null;
	
	private ArrayList<Torrent> ep_hd, ep_normali, ep_preair;
	
	public Episodio(int stagione, int episodio){
		this.stagione=stagione;
		this.episodio=episodio;
		ep_hd=new ArrayList<Torrent>(1);
		ep_normali=new ArrayList<Torrent>(1);
		ep_preair=new ArrayList<Torrent>(1);
	}
	
	public int getEpisodio(){
		return episodio;
	}
	
	public void addLink(Torrent link){
		if(nome_serie==null)
			nome_serie=link.getNomeSerie();
		
		if(link.isPreAir()){
			if(addLinkToList(ep_preair, link)){
				link.updateTorrentInDB();
			}
		}
		else if(link.is720p()){
			if(addLinkToList(ep_hd, link)){
				link.updateTorrentInDB();
			}
		}
		else {
			if(addLinkToList(ep_normali, link)){
				link.updateTorrentInDB();
			}
		}
		checkStatus(link);
	}
	public void addLinkFromDB(Torrent link){
		if(nome_serie==null)
			nome_serie=link.getNomeSerie();
		
		if(link.isPreAir()){
			addLinkToList(ep_preair, link);
		}
		else if(link.is720p()){
			addLinkToList(ep_hd, link);
		}
		else {
			addLinkToList(ep_normali, link);
		}
		checkStatus(link);
	}
	private boolean addLinkToList(ArrayList<Torrent> elenco, Torrent link){
		if(elenco.size()==0){
			elenco.add(link);
			return true;
		}
		else {
			boolean inserito=false;
			for(int i=0;i<elenco.size();i++){
				Torrent t=elenco.get(i);
				if(t.getUrl().compareTo(link.getUrl())==0)
					return false;
				else {
					if(link.getStats().compareStats(t.getStats())>0){
						elenco.add(i, link);
						inserito=true;
						break;
					}
				}
			}
			if(!inserito){
				elenco.add(link);
			}
			return true;
		}
	}
	public boolean isScaricato(){
		boolean hd = false, sd = false,pre = false;
		for(int i=0;i<ep_hd.size();i++){
			if(ep_hd.get(i).isScaricato()){
				//System.out.println("HD - scaricato: stato "+ep_hd.get(i).getScaricato());
				hd=true;
			}
		}
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i).isScaricato()){
				//System.out.println("SD - scaricato: stato "+ep_normali.get(i).getScaricato());
				sd=true;
			}
		}
		for(int i=0;i<ep_preair.size();i++){
			if(ep_preair.get(i).isScaricato()){
				//System.out.println("PRE - scaricato: stato "+ep_preair.get(i).getScaricato());
				pre=true;
			}
		}
		return hd&&sd&&pre;
	}
	public Torrent getLinkHD(){
		if(ep_hd.size()>0)
			return ep_hd.get(0);
		return null;
	}
	public Torrent getLinkNormale(){
		if(ep_normali.size()>0)
			return ep_normali.get(0);
		return null;
	}
	public Torrent getLinkPreair(){
		if(ep_preair.size()>0)
			return ep_preair.get(0);
		return null;
	}
	public void scaricaLink(Torrent link){
		for(int i=0;i<ep_hd.size();i++){
			if(ep_hd.get(i)==link){
				link.setScaricato(Torrent.SCARICATO);
			}
			else if(ep_hd.get(i).getScaricato()==Torrent.SCARICARE){
				ep_hd.get(i).setScaricato(Torrent.IGNORATO);
			}
		}
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i)==link){
				link.setScaricato(Torrent.SCARICATO);
			}
			else if(ep_normali.get(i).getScaricato()==Torrent.SCARICARE){
				ep_normali.get(i).setScaricato(Torrent.IGNORATO);
			}
		}
		for(int i=0;i<ep_preair.size();i++){
			if(ep_preair.get(i)==link){
				link.setScaricato(Torrent.SCARICATO);
			}
			else if(ep_preair.get(i).getScaricato()==Torrent.SCARICARE){
				ep_preair.get(i).setScaricato(Torrent.IGNORATO);
			}
		}
	}
	public int getStagione(){
		return stagione;
	}
	public String getNomeSerie(){
		return nome_serie;
	}
	public void cleanAll(){
		ep_hd.clear();
		ep_normali.clear();
		ep_preair.clear();
	}
	public String toString(){
		return nome_serie+" "+getStagione()+"x"+getEpisodio();
	}
	public void ottimizzaSpazio(){
		for(int i=0;i<ep_hd.size();){
			switch(ep_hd.get(i).getScaricato()){
				case Torrent.IGNORATO:
					ep_hd.remove(ep_hd.get(i));
					break;
				default:
					i++;
			}
		}
		for(int i=0;i<ep_normali.size();){
			switch(ep_normali.get(i).getScaricato()){
				case Torrent.IGNORATO:
					ep_normali.remove(ep_hd.get(i));
					break;
				default:
					i++;
			}
		}
		for(int i=0;i<ep_preair.size();){
			switch(ep_preair.get(i).getScaricato()){
				case Torrent.IGNORATO:
					ep_preair.remove(ep_hd.get(i));
					break;
				default:
					i++;
			}
		}
		ep_hd.trimToSize();
		ep_normali.trimToSize();
		ep_preair.trimToSize();
		Runtime.getRuntime().gc();
	}
	public Torrent getLinkLettore(){ //TODO get link in base alle preferenze e se scaricato
		//al momento cerca il link scaricato e se non trovato, in ordine hd, normale, preair
		if(ep_hd.size()>0){
			if(ep_hd.get(0).getSerieTV().getPreferenze().isPreferisciHD()){
				for(int i=0;i<ep_hd.size();i++){
					if(ep_hd.get(i).isScaricato())
						return ep_hd.get(i);
				}
			}
		}
		
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i).isScaricato())
				return ep_normali.get(i);
		}
		
		if(ep_preair.size()>0){
			if(ep_preair.get(0).getSerieTV().getPreferenze().isDownloadPreair()){
				for(int i=0;i<ep_preair.size();i++){
					if(ep_preair.get(i).isScaricato())
						return ep_preair.get(i);
				}
			}
		}

		if(ep_hd.size()>0){
			if(ep_hd.get(0).getSerieTV().getPreferenze().isPreferisciHD()){
				if(getLinkHD()!=null)
					return getLinkHD();
			}
		}
		if(getLinkNormale()!=null)
			return getLinkNormale();
		if(ep_preair.size()>0){
			if(ep_preair.get(0).getSerieTV().getPreferenze().isDownloadPreair()){
				if(getLinkPreair()!=null)
					return getLinkPreair();
			}
		}
		
		return null;
	}
	public boolean isVisto(){
		for(int i=0;i<ep_hd.size();i++){
			if(ep_hd.get(i).getScaricato()==Torrent.VISTO)
				return true;
		}
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i).getScaricato()==Torrent.VISTO)
				return true;
		}
		for(int i=0;i<ep_preair.size();i++){
			if(ep_preair.get(i).getScaricato()==Torrent.VISTO)
				return true;
		}
		return false;
	}
	public boolean isRimosso(){
		for(int i=0;i<ep_hd.size();i++){
			if(ep_hd.get(i).getScaricato()!=Torrent.RIMOSSO)
				return false;
		}
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i).getScaricato()!=Torrent.RIMOSSO)
				return false;
		}
		for(int i=0;i<ep_preair.size();i++){
			if(ep_preair.get(i).getScaricato()!=Torrent.RIMOSSO)
				return false;
		}
		return true;
	}
	public boolean isIgnorato(){
		for(int i=0;i<ep_hd.size();i++){
			if(ep_hd.get(i).getScaricato()!=Torrent.IGNORATO)
				return false;
		}
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i).getScaricato()!=Torrent.IGNORATO)
				return false;
		}
		for(int i=0;i<ep_preair.size();i++){
			if(ep_preair.get(i).getScaricato()!=Torrent.IGNORATO)
				return false;
		}
		return true;
	}
	public void checkStatus(Torrent t){
		switch(t.getScaricato()){
			case Torrent.SCARICARE:
			case Torrent.RIMOSSO:
			case Torrent.IGNORATO:
				if(t.getFilePath()!=null)
					t.setScaricato(Torrent.SCARICATO);
				break;
			case Torrent.SCARICATO:
			case Torrent.VISTO:
				if(t.getFilePath()==null)
					t.setScaricato(Torrent.RIMOSSO);
		}
	}
	public Torrent getLinkScaricato(){
		for(int i=0;i<ep_hd.size();i++){
			Torrent t=ep_hd.get(i);
			if(t.getScaricato()==Torrent.SCARICATO || t.getScaricato()==Torrent.VISTO)
				return t;
		}
		for(int i=0;i<ep_normali.size();i++){
			Torrent t=ep_normali.get(i);
			if(t.getScaricato()==Torrent.SCARICATO || t.getScaricato()==Torrent.VISTO)
				return t;
		}
		for(int i=0;i<ep_preair.size();i++){
			Torrent t=ep_preair.get(i);
			if(t.getScaricato()==Torrent.SCARICATO || t.getScaricato()==Torrent.VISTO)
				return t;
		}
		return null;
	}
	public Torrent getLinkDownload(){
		if(ep_hd.size()>0){
			if(ep_hd.get(0).getSerieTV().getPreferenze().isPreferisciHD())
				return ep_hd.get(0);
		}
		if(ep_normali.size()>0)
			return getLinkNormale();
		if(ep_preair.size()>0){
			if(ep_preair.get(0).getSerieTV().getPreferenze().isDownloadPreair()){
				return ep_preair.get(0);
			}
		}
		return null;
	}
	public final static int INDEX_HD=1, INDEX_PRE=2, INDEX_SD=3; 
	public void setDownloadableFirst(int elenco, int status_to_change, int which_status){
		switch(elenco){
			case INDEX_HD:{
				Torrent t=getLinkHD();
				if(t!=null){
					if(t.getScaricato()==status_to_change)
						t.setScaricato(which_status);
				}
				break;
			}
			case INDEX_SD:{
				Torrent t=getLinkNormale();
				if(t!=null){
					if(t.getScaricato()==status_to_change)
						t.setScaricato(which_status);
				}
				break;
			}
			case INDEX_PRE:{
				Torrent t=getLinkPreair();
				if(t!=null){
					if(t.getScaricato()==status_to_change)
						t.setScaricato(which_status);
				}
				break;
			}
		}
	}
}
