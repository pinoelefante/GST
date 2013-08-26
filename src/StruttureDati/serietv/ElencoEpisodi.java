package StruttureDati.serietv;

import java.util.ArrayList;

import SerieTV.Torrent2;

public class ElencoEpisodi {
	private ArrayList<Episodio> episodi;
	
	public ElencoEpisodi(){
		episodi=new ArrayList<Episodio>();
	}
	public void aggiungiLink(Torrent2 t){
		Episodio ep=cercaEpisodio(t.getStagione(), t.getEpisodio());
		if(ep==null){
			ep=aggiungiEpisodio(t.getStagione(), t.getEpisodio());
		}
		ep.addLink(t);
	}
	private Episodio aggiungiEpisodio(int stagione, int episodio){
		Episodio daInserire=new Episodio(stagione, episodio);
		int i;
		if(size()==0){
			episodi.add(daInserire);
		}
		else {
    		boolean inserita=false;
    		for(i=0;i<episodi.size() &&!inserita;i++){
    			Episodio ep=episodi.get(i);
    			if(daInserire.getStagione()<ep.getStagione()){
    				episodi.add(i, daInserire);
    				inserita=true;
    			}
    			else if(daInserire.getStagione()==ep.getStagione()){
    				if(daInserire.getEpisodio()<ep.getEpisodio()){
    					episodi.add(i, daInserire);
        				inserita=true;
    				}
    			}
    		}
    		if(!inserita){
    			episodi.add(daInserire);
    			System.out.println("Inserimento all'esterno del for");
    		}
		}
		return daInserire;
	}
	private Episodio cercaEpisodio(int stagione, int episodio){
		for(int i=0;i<episodi.size();i++){
			if(stagione==episodi.get(i).getStagione()){
				if(episodio==episodi.get(i).getEpisodio())
					return episodi.get(i);
			}
		}
		return null;
	}
	public void clean(){
		for(int i=0;i<episodi.size();i++)
			episodi.get(i).cleanAll();
		episodi.clear();
	}
	public int size(){
		return episodi.size();
	}
	public Episodio get(int i){
		if(i<0 || i>=size())
			return null;
		return episodi.get(i);
	}
	public void stampaElenco(){
		for(int i=0;i<episodi.size();i++){
			System.out.println(episodi.get(i));
		}
	}
	public void ottimizzaSpazio(){
		
	}
}
