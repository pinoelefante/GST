package SerieTV;

public class SerieTV2 {
	private String titolo;
	private ProviderSerieTV provider;
	private String url_serie;
	
	private int id_db, id_itasa=0, id_subsfactory=0, id_tvdb=0, id_tvrage=0, id_subspedia=0;
	
	private boolean conclusa, stop_search, inserita;
	
	
	
	public SerieTV2(ProviderSerieTV provider, String nomeserie, String url) {
		this.provider=provider;
		titolo=nomeserie;
		url_serie=url;
	}

	public String getNomeSerie(){
		return titolo;
	}
	// Inserita o meno
	public void setConclusa(boolean stato) {
		conclusa=stato;
	}
	public boolean isConclusa(){
		return conclusa;
	}
	public String toString(){
		return getNomeSerie() + " ("+provider.getProviderName()+")";
	}
	public void clearEpisodi(){
		// TODO
		Runtime.getRuntime().gc();
	}
	public String getFolderSerie() {
		return getNomeSerie().replace(":", "-").replace("?", "").replace("/", "-").replace("\\", "-").replace("*", "").replace("<", "").replace(">", "").replace("|", "").replace("\"", "");
	}
	public int getIDDb(){
		return id_db;
	}
	public boolean isInserita(){
		return inserita;
	}
	public void setInserita(boolean s){
		inserita=s;
	}
	public void aggiornaDB(){
		provider.salvaSerieInDB(this);
	}
	public boolean isStopSearch(){
		return stop_search;
	}
	public String getUrl(){
		return url_serie;
	}

	public int getIDItasa() {
		return id_itasa;
	}

	public void setIDItasa(int id_itasa) {
		this.id_itasa = id_itasa;
	}

	public int getIDSubsfactory() {
		return id_subsfactory;
	}

	public void setIDSubsfactory(int id_subsfactory) {
		this.id_subsfactory = id_subsfactory;
	}

	public int getIDTvdb() {
		return id_tvdb;
	}

	public void setIDTvdb(int id_tvdb) {
		this.id_tvdb = id_tvdb;
	}

	public int getIDTvrage() {
		return id_tvrage;
	}

	public void setIDTvrage(int id_tvrage) {
		this.id_tvrage = id_tvrage;
	}

	public int getIDSubspedia() {
		return id_subspedia;
	}

	public void setIDSubspedia(int id_subspedia) {
		this.id_subspedia = id_subspedia;
	}
}
