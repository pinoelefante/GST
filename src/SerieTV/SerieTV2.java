package SerieTV;

public class SerieTV2 {
	private String titolo;
	private ProviderSerieTV provider;
	private String url_serie;
	
	private int id_db;
	
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
	public int getIdDb(){
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
}
