package SerieTV;

import java.util.ArrayList;

public abstract class ProviderSerieTV {
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
	protected SerieTV2 addSerie(SerieTV2 s){
		for(int i=0;i<getSeriesCount();i++){
			SerieTV2 st=elenco_serie.get(i);
			if(s.getNomeSerie().compareTo(st.getNomeSerie())<0){
				elenco_serie.add(i,s);
				salvaSerieInDB(s);
				nuove_serie.add(s);
				return null;
			}
			else if(s.getNomeSerie().compareTo(st.getNomeSerie())==0)
				return st;
		}
		elenco_serie.add(s);
		salvaSerieInDB(s);
		nuove_serie.add(s);
		return null;
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
	public abstract ArrayList<Torrent> nuoviEpisodi(SerieTV2 serie);
	public abstract ArrayList<Torrent> caricaEpisodiDB(SerieTV2 serie);
	public abstract void caricaSerieDB();
	protected abstract void salvaSerieInDB(SerieTV2 s);
	protected abstract void salvaEpisodioInDB(Torrent2 t);
}
