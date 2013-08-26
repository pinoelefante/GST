package SerieTV;

import java.util.ArrayList;

import StruttureDati.serietv.Episodio;

public abstract class ProviderSerieTV {
	protected final static int PROVIDER_EZTV=1;
	
	protected ArrayList<SerieTV2> elenco_serie, nuove_serie, preferite;
	
	public ProviderSerieTV(){
		elenco_serie=new ArrayList<SerieTV2>();
		preferite=new ArrayList<SerieTV2>();
		nuove_serie=new ArrayList<SerieTV2>();
		
		caricaSerieDB();
	}
	protected ArrayList<SerieTV2> getElencoSerie(){
		return elenco_serie;
	}
	protected SerieTV2 addSerieFromOnline(SerieTV2 s){
		SerieTV2 s2;
		if((s2=cercaSerie(s))!=null){
			s2.setConclusa(s.isConclusa());
			return s2;
		}
		for(int i=0;i<getSeriesCount();i++){
			SerieTV2 st=elenco_serie.get(i);
			if(s.compareTo(st)<0){
				elenco_serie.add(i,s);
				nuove_serie.add(s);
				return null;
			}
		}
		elenco_serie.add(s);
		nuove_serie.add(s);
		return null;
	}
	protected void addSerieFromDB(SerieTV2 s){
		elenco_serie.add(s);
		if(s.isInserita())
			preferite.add(s);
	}
	public int getSeriesCount(){
		return elenco_serie.size();
	}
	public SerieTV2 getSerieAt(int i){
		if(i<0 || i>=getSeriesCount())
			return null;
		return elenco_serie.get(i);
	}
	public int getNuoveSerieCount(){
		return nuove_serie.size();
	}
	private SerieTV2 cercaSerie(SerieTV2 daCercare){
		for(int i=0;i<elenco_serie.size();i++){
			SerieTV2 s=elenco_serie.get(i);
			if(daCercare.getUrl().compareToIgnoreCase(s.getUrl())==0){
				return s;
			}
		}
		return null;
	}
	
	public boolean addSeriePreferita(SerieTV2 s){
		if(!s.isInserita()){
			boolean inserita=false;
			for(int i=0;i<getPreferiteSerieCount();i++){
				SerieTV2 s2=getPreferiteSerieAt(i);
				if(s.compareTo(s2)<0){
					preferite.add(i, s);
					s.setInserita(true);
					salvaSerieInDB(s);
					inserita=true;
				}
			}
			if(!inserita){
				preferite.add(s);
				s.setInserita(true);
				salvaSerieInDB(s);
			}
			return true;
		}
		return false;
	}
	public void rimuoviSeriePreferita(SerieTV2 s){
		if(s.isInserita()){
			preferite.remove(s);
			s.setInserita(false);
			salvaSerieInDB(s);
		}
	}
	public SerieTV2 getNuoveSerieAt(int i){
		if(i<0 || i>=getNuoveSerieCount())
			return null;
		return nuove_serie.get(i);
	}
	public int getPreferiteSerieCount(){
		return preferite.size();
	}
	public SerieTV2 getPreferiteSerieAt(int i){
		if(i<0 || i>=getPreferiteSerieCount())
			return null;
		return preferite.get(i);
	}

	public abstract String getProviderName();
	public abstract String getBaseURL();
	public abstract void aggiornaElencoSerie();
	public abstract ArrayList<Episodio> nuoviEpisodi(SerieTV2 serie);
	public abstract ArrayList<Torrent2> caricaEpisodiDB(SerieTV2 serie);
	public abstract void caricaSerieDB();
	protected abstract void salvaSerieInDB(SerieTV2 s);
	protected abstract void salvaEpisodioInDB(Torrent2 t);
	public abstract int getProviderID();
	public abstract void caricaEpisodiOnline(SerieTV2 serie);
}
