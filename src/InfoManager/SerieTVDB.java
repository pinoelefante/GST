package InfoManager;

public class SerieTVDB {
	private int id;
	private String nome, url_banner, descrizione;
	private int i_anno, i_mese, i_giorno; 
	
	public SerieTVDB(int id, String nomeserie, String descrizione, String banner, String inizio){
		this.setId(id);
		this.setNomeSerie(nomeserie);
		this.setDescrizione(descrizione);
		this.setUrlBanner(banner);
		this.setDataInizio(inizio);
	}


	private void setDataInizio(String inizio) {
		if(inizio.length()>0){
    		String[] comp=inizio.split("-");
    		i_anno=Integer.parseInt(comp[0]);
    		i_mese=Integer.parseInt(comp[1]);
    		i_giorno=Integer.parseInt(comp[2]);
		}
	}
	public String getDataInizio(){
		return i_giorno+"/"+i_mese+"/"+i_anno;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeSerie() {
		return nome;
	}

	public void setNomeSerie(String nome) {
		this.nome = nome;
	}

	public String getUrlBanner() {
		return url_banner;
	}

	public void setUrlBanner(String url_banner) {
		this.url_banner = url_banner;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String toString(){
		return "ID: "+getId()+"\nTitolo: "+getNomeSerie()+"\nData inizio: "+getDataInizio()+"\nBanner: "+getUrlBanner()+"\nDescrizione: "+getDescrizione();
	}
}
