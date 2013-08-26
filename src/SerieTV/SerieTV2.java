package SerieTV;

import java.util.ArrayList;

import Database.Database2;
import StruttureDati.db.KVResult;
import StruttureDati.serietv.ElencoEpisodi;
import StruttureDati.serietv.Episodio;

public class SerieTV2 {
	private String titolo;
	private ProviderSerieTV provider;
	private String url_serie;
	private int id_db, id_itasa=0, id_subsfactory=0, id_tvdb=0, id_subspedia=0;
	private boolean conclusa, stop_search, inserita;
	private ElencoEpisodi episodi;
	
	public SerieTV2(ProviderSerieTV provider, String nomeserie, String url) {
		episodi=new ElencoEpisodi();
		this.provider=provider;
		titolo=formattaNome(nomeserie);
		url_serie=url;
	}

	public String getNomeSerie(){
		return titolo;
	}
	// Stato conclusa (ended su eztv)
	public void setConclusa(boolean stato) {
		if(conclusa!=stato){
			conclusa=stato;
		}
	}
	public boolean isConclusa(){
		return conclusa;
	}
	public String toString(){
		return getNomeSerie() + " ("+provider.getProviderName()+")";
	}
	public void ottimizzaEpisodi(){
		episodi.ottimizzaSpazio();
		Runtime.getRuntime().gc();
	}
	public String getFolderSerie() {
		return getNomeSerie().replace(":", "-").replace("?", "").replace("/", "-").replace("\\", "-").replace("*", "").replace("<", "").replace(">", "").replace("|", "").replace("\"", "");
	}
	public int getIDDb(){
		return id_db;
	}
	public void setIDDb(int i){
		id_db=i;
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
	public void setStopSearch(boolean s, boolean updateDB){
		stop_search=s;
		if(updateDB){
			aggiornaDB();
		}
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

	public int getIDDBSubsfactory() {
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

	public int getIDSubspedia() {
		return id_subspedia;
	}

	public void setIDSubspedia(int id_subspedia) {
		this.id_subspedia = id_subspedia;
	}
	public int compareTo(SerieTV2 s2){
		return this.getNomeSerie().trim().toLowerCase().compareTo(s2.getNomeSerie().trim().toLowerCase());
	}
	
	public int getProviderID(){
		return provider.getProviderID();
	}
	public ProviderSerieTV getProvider(){
		return provider;
	}
	private String formattaNome(String nome){
		String formattato=nome;
		if(formattato.contains(", The")){
			formattato="The "+formattato.replace(", The", "").trim();
		}
		//TODO Pattern anno
		
		return formattato;
	}
	public void addEpisodio(Torrent2 episodio){
		episodi.aggiungiLink(episodio);
	}
	public void aggiornaEpisodiOnline(){
		provider.caricaEpisodiOnline(this);
		episodi.stampaElenco();
	}
	public int getNumEpisodi(){
		return episodi.size();
	}
	public Episodio getEpisodio(int i){ // index in elencoepisodi
		return episodi.get(i);
	}
	private String SubsfactoryOnlineDirectory="";
	
	public String getSubsfactoryDirectory(){
		if(SubsfactoryOnlineDirectory.isEmpty()){
			String query="SELECT directory FROM "+Database2.TABLE_SUBSFACTORY+" WHERE id="+getIDDBSubsfactory();
			ArrayList<KVResult<String, Object>> res=Database2.selectQuery(query);
			if(res.size()==1){
				SubsfactoryOnlineDirectory=(String) res.get(0).getValueByKey("directory");
			}
		}
		return SubsfactoryOnlineDirectory;
	}

	public void setSubsfactoryDirectory(String id) {
		SubsfactoryOnlineDirectory=id;
		//TODO salvataggio in db
		
	}
	
}
