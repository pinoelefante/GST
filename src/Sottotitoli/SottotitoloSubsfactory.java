package Sottotitoli;

import Naming.CaratteristicheFile;
import Naming.Naming;

class SottotitoloSubsfactory {
	protected String nomefile, id_serie;
	protected int season, ep;
	private boolean normale=true, hd720p;
	
	public SottotitoloSubsfactory(){}
	public SottotitoloSubsfactory(String nome, String id) {
		nomefile=nome;
		this.id_serie=id;
		parseNome();
	}
	private void parseNome(){
		try{
			CaratteristicheFile stats=Naming.parseString(nomefile);
			ep=stats.getEpisodio();
			season=stats.getStagione();
		}
		catch(Exception e){
			ep=0;
			season=0;
		}
	}
	public boolean isNormale(){
		return normale;
	}
	public boolean is720p(){
		return hd720p;
	}
	public void setNormale(boolean s){
		normale=s;
	}
	public void set720p(boolean s){
		hd720p=s;
	}
	public int getStagione(){
		return season;
	}
	public int getEpisodio(){
		return ep;
	}
	public String getNomeFile(){
		return nomefile;
	}
	public String getIDSerie(){
		return id_serie;
	}
}