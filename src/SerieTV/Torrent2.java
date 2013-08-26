package SerieTV;

import Naming.CaratteristicheFile;

public class Torrent2 {
	public final static int SCARICARE=0, SCARICATO=1, VISTO=2, RIMOSSO=3, IGNORATO=4; 
	private String	url;
	private int		stato; //0 non scaricato - 1 scaricato - 2 visto - 3 rimosso - 4 ignorato
	private boolean preair;
	private boolean sub_down; //true se è da scaricare, false non scaricare
	private CaratteristicheFile prop_torrent;
	private SerieTV2 serietv;
	private int id_db, id_tvdb;

	public Torrent2(SerieTV2 st, String url, int stato_t) {
		this.url=url;
		stato=stato_t;
		serietv=st;
	}
	public Torrent2(SerieTV2 st, String url, int stato_t, CaratteristicheFile f){
		this.url=url;
		stato=stato_t;
		serietv=st;
		prop_torrent=f;
	}
	public boolean isPreAir(){
		return preair;
	}
	public boolean isPROPER(){
		return prop_torrent.isProper();
	}
	public String getUrl() {
		return this.url;
	}
	public boolean isScaricato() {
		return stato!=SCARICARE;
	}
	
	public void setScaricato(int visto) {
		this.stato = visto;
		updateTorrentInDB();
	}

	public boolean is720p() {
		return prop_torrent.is720p();
	}

	public int getStagione() {
		return prop_torrent.getStagione();
	}

	public int getEpisodio() {
		return prop_torrent.getEpisodio();
	}

	public String getNomeSerie() {
		return serietv.getNomeSerie();
	}

	public boolean isRepack() {
		return prop_torrent.isRepack();
	}

	public String getNameFromMagnet() {
		String nome = this.url.substring(this.url.indexOf("&dn"), this.url.indexOf("&tr"));
		nome = nome.substring(nome.indexOf("=") + 1);
		return nome;
	}

	public void parseMagnet() {
		this.preair = this.url.toUpperCase().contains("PREAIR");
		String[] patt=new String[]{
				Naming.Naming.PATTERN_SnEn,
				Naming.Naming.PATTERN_SxE,
				Naming.Naming.PATTERN_Part_dotnofn,
				Naming.Naming.PATTERN_nofn
		};
		prop_torrent=Naming.Naming.parse(getNameFromMagnet(), patt);
	}
	
	public String getNomeSerieFolder() {
		return serietv.getFolderSerie();
	}
	
	public boolean isSottotitolo(){
		return sub_down;
	}
	public void setSubDownload(boolean stat){
		sub_down=stat;
		
		/* TODO aggiornare
		public void setSottotitolo(boolean stato, boolean update) {
			sub_down=stato;
			if(stato)
				GestioneSerieTV2.getSubManager().aggiungiEpisodio(this);
			else
				GestioneSerieTV2.getSubManager().rimuoviEpisodio(this);
			if(update)
				update();
		}
		*/
	}
	
	public void setPreair(boolean stato) {
		preair=stato;
	}
	public void updateTorrentInDB(){
		getSerieTV().getProvider().salvaEpisodioInDB(this);
	}
	public String toString(){
		if(isMagnetLink())
			return this.url.substring(this.url.indexOf("&dn")+4, this.url.indexOf("&tr"));
		else if(isTorrent())
			return url.substring(url.lastIndexOf("/")+1);
		else
			return serietv.getNomeSerie()+" S"+getStagione()+"E"+getEpisodio();
	}
	public int getScaricato(){
		return stato;
	}
	private boolean isTorrent(){
		return (url.toLowerCase().endsWith(".torrent"));
	}
	private boolean isMagnetLink(){
		return url.toLowerCase().startsWith("magnet");
	}
	public void setEpisodio(int e){
		prop_torrent.setEpisodio(e);
	}
	public void setStagione(int nuovo) {
		prop_torrent.setStagione(nuovo);
	}
	public String getFilePath(){
		//TODO ricerca file
		return "";
	}
	public CaratteristicheFile getStats(){
		return prop_torrent;
	}
	public SerieTV2 getSerieTV(){
		return serietv;
	}
	public int getIDDB() {
		return id_db;
	}
	public void setIDDB(int id_db) {
		this.id_db = id_db;
	}
	public int getIDTVDB() {
		return id_tvdb;
	}
	public void setIDTVDB(int id_tvdb) {
		this.id_tvdb = id_tvdb;
	}
}
