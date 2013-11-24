package SerieTV;

import java.util.ArrayList;

import StruttureDati.serietv.Episodio;

public abstract class ProviderSerieTV {
	protected final static int PROVIDER_EZTV=1;
	
	protected ArrayList<SerieTV> elenco_serie, nuove_serie, preferite;
	
	public ProviderSerieTV(){
		elenco_serie=new ArrayList<SerieTV>();
		preferite=new ArrayList<SerieTV>();
		nuove_serie=new ArrayList<SerieTV>();
		
		caricaSerieDB();
	}
	protected ArrayList<SerieTV> getElencoSerie(){
		return elenco_serie;
	}
	protected SerieTV addSerieFromOnline(SerieTV s){
		SerieTV s2;
		if((s2=cercaSerie(s))!=null){
			s2.setConclusa(s.isConclusa());
			return s2;
		}
		for(int i=0;i<getSeriesCount();i++){
			SerieTV st=elenco_serie.get(i);
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
	protected void addSerieFromDB(SerieTV s){
		elenco_serie.add(s);
		if(s.isInserita())
			preferite.add(s);
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
	private SerieTV cercaSerie(SerieTV daCercare){
		for(int i=0;i<elenco_serie.size();i++){
			SerieTV s=elenco_serie.get(i);
			if(daCercare.getUrl().compareToIgnoreCase(s.getUrl())==0){
				return s;
			}
		}
		return null;
	}
	
	public boolean addSeriePreferita(SerieTV s){
		if(!s.isInserita()){
			boolean inserita=false;
			for(int i=0;i<getPreferiteSerieCount();i++){
				SerieTV s2=getPreferiteSerieAt(i);
				if(s.getNomeSerie().compareTo(s2.getNomeSerie())<0){
					preferite.add(i, s);
					s.setInserita(true);
					salvaSerieInDB(s);
					return true;
				}
			}
			if(!inserita){
				preferite.add(s);
				s.setInserita(true);
				salvaSerieInDB(s);
				return true;
			}
		}
		return false;
	}
	public void rimuoviSeriePreferita(SerieTV s){
		if(s.isInserita()){
			preferite.remove(s);
			s.setInserita(false);
			s.setConclusa(false);
			s.setStopSearch(false, false);
			rimuoviSerieDaDB(s);
			for(int i=0;i<s.getNumEpisodi();i++){
				s.getEpisodio(i).setStatus(Torrent.SCARICARE);
			}
		}
	}
	protected boolean rimuoviSerieDaDB(SerieTV serie){
		try {
			serie.aggiornaDB();
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	public SerieTV getNuoveSerieAt(int i){
		if(i<0 || i>=getNuoveSerieCount())
			return null;
		return nuove_serie.get(i);
	}
	public int getPreferiteSerieCount(){
		return preferite.size();
	}
	public SerieTV getPreferiteSerieAt(int i){
		if(i<0 || i>=getPreferiteSerieCount())
			return null;
		return preferite.get(i);
	}

	public abstract String getProviderName();
	public abstract String getBaseURL();
	public abstract void aggiornaElencoSerie();
	public abstract ArrayList<Episodio> nuoviEpisodi(SerieTV serie);
	public abstract void caricaEpisodiDB(SerieTV serie);
	public abstract void caricaSerieDB();
	protected abstract void salvaSerieInDB(SerieTV s);
	protected abstract void salvaEpisodioInDB(Torrent t);
	public abstract int getProviderID();
	public abstract void caricaEpisodiOnline(SerieTV serie);
}
