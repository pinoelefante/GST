package StruttureDati.serietv;

import java.util.ArrayList;

import SerieTV.Torrent2;

public class Episodio {
	private int episodio, stagione;
	private String nome_serie=null;
	
	private ArrayList<Torrent2> ep_hd, ep_normali, ep_preair;
	
	public Episodio(int stagione, int episodio){
		this.stagione=stagione;
		this.episodio=episodio;
		ep_hd=new ArrayList<Torrent2>(1);
		ep_normali=new ArrayList<Torrent2>(1);
		ep_preair=new ArrayList<Torrent2>(1);
	}
	
	public int getEpisodio(){
		return episodio;
	}
	
	public void addLink(Torrent2 link){
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
	}
	public void addLinkFromDB(Torrent2 link){
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
	}
	private boolean addLinkToList(ArrayList<Torrent2> elenco, Torrent2 link){
		if(elenco.size()==0){
			elenco.add(link);
			return true;
		}
		else {
			boolean inserito=false;
			for(int i=0;i<elenco.size();i++){
				Torrent2 t=elenco.get(i);
				if(t.getUrl().compareTo(link.getUrl())==0)
					return false;
				else {
					if(link.getStats().compare(t.getStats())>0){
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
		for(int i=0;i<ep_hd.size();i++){
			if(ep_hd.get(i).isScaricato())
				return true;
		}
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i).isScaricato())
				return true;
		}
		for(int i=0;i<ep_preair.size();i++){
			if(ep_preair.get(i).isScaricato())
				return true;
		}
		return false;
	}
	public Torrent2 getLinkHD(){
		if(ep_hd.size()>0)
			return ep_hd.get(0);
		return null;
	}
	public Torrent2 getLinkNormale(){
		if(ep_normali.size()>0)
			return ep_normali.get(0);
		return null;
	}
	public Torrent2 getLinkPreair(){
		if(ep_preair.size()>0)
			return ep_preair.get(0);
		return null;
	}
	public void scaricaLink(Torrent2 link){
		for(int i=0;i<ep_hd.size();i++){
			if(ep_hd.get(i)==link){
				link.setScaricato(Torrent2.SCARICATO);
			}
			else if(!ep_hd.get(i).isScaricato()){
				ep_hd.get(i).setScaricato(Torrent2.IGNORATO);
			}
		}
		for(int i=0;i<ep_normali.size();i++){
			if(ep_normali.get(i)==link){
				link.setScaricato(Torrent2.SCARICATO);
			}
			else if(!ep_normali.get(i).isScaricato()){
				ep_normali.get(i).setScaricato(Torrent2.IGNORATO);
			}
		}
		for(int i=0;i<ep_preair.size();i++){
			if(ep_preair.get(i)==link){
				link.setScaricato(Torrent2.SCARICATO);
			}
			else if(!ep_preair.get(i).isScaricato()){
				ep_preair.get(i).setScaricato(Torrent2.IGNORATO);
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
				case Torrent2.IGNORATO:
					ep_hd.remove(ep_hd.get(i));
					break;
				default:
					i++;
			}
		}
		for(int i=0;i<ep_normali.size();){
			switch(ep_normali.get(i).getScaricato()){
				case Torrent2.IGNORATO:
					ep_normali.remove(ep_hd.get(i));
					break;
				default:
					i++;
			}
		}
		for(int i=0;i<ep_preair.size();){
			switch(ep_preair.get(i).getScaricato()){
				case Torrent2.IGNORATO:
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
}
