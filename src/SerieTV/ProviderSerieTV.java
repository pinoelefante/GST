package SerieTV;

import java.util.ArrayList;

public abstract class ProviderSerieTV {
	protected ArrayList<SerieTV> elenco_serie, nuove_serie;
	
	public ProviderSerieTV(){
		elenco_serie=new ArrayList<SerieTV>();
		caricaSerieDB();
	}
	protected ArrayList<SerieTV> getElencoSerie(){
		return elenco_serie;
	}
	protected SerieTV addSerie(SerieTV s){
		for(int i=0;i<getSeriesCount();i++){
			SerieTV st=elenco_serie.get(i);
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
	public SerieTV getSerieAt(int i){
		if(i<0 || i>=getSeriesCount())
			return null;
		return elenco_serie.get(i);
	}
	public int getNuoveSerieCount(){
		return nuove_serie.size();
	}
	public SerieTV getNuoveSerieAt(int i){
		if(i<0 || i>=getNuoveSerieCount())
			return null;
		return nuove_serie.get(i);
	}

	public abstract String getProviderName();
	public abstract String getBaseURL();
	public abstract void aggiornaElencoSerie();
	public abstract ArrayList<Torrent> nuoviEpisodi(SerieTV serie);
	public abstract void caricaSerieDB();
	protected abstract void salvaSerieInDB(SerieTV s);
}
