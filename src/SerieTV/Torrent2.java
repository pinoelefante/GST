package SerieTV;

import Database.*;
import Naming.CaratteristicheFile;
import Programma.ManagerException;

public class Torrent2 {
	public final static int SCARICARE=0, SCARICATO=1, VISTO=2, RIMOSSO=3, IGNORATO=4; 
	private String	url;
	private int		stato; //0 non scaricato - 1 scaricato - 2 visto - 3 rimosso - 4 ignorato
	private boolean preair;
	private boolean sub_down; //true se è da scaricare, false non scaricare
	private CaratteristicheFile prop_torrent;
	private SerieTV2 serietv;

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
		update();
	}
	public void setScaricato(int visto, boolean update){
		this.stato = visto;
		if(update){
			update();
		}
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

	public String getName() {
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
		prop_torrent=Naming.Naming.parse(getName(), patt);
	}
	
	public String getNomeSerieFolder() {
		return serietv.getFolderSerie();
	}
	/*
	public void setSottotitolo(boolean stato, boolean update) {
		sub_down=stato;
		if(stato)
			GestioneSerieTV.getSubManager().aggiungiEpisodio(this);
		else
			GestioneSerieTV.getSubManager().rimuoviEpisodio(this);
		if(update)
			update();
	}
	*/
	public boolean isSottotitolo(){
		return sub_down;
	}
	/*
	public void setPreair(boolean stato) {
		preair=stato;
	}
	*/
	public void insert(){
		String query="INSERT INTO "+Database.TABLE_TORRENT+" "+"(magnet, id_serie, vista, serie, episodio, HD720p, repack, preair, proper, sottotitolo) VALUES ("+
				"\""+getUrl()+"\","+
				serietv.getIDDb()+","+
				stato+","+
				getStagione()+","+
				getEpisodio()+","+
				(is720p()?1:0)+","+
				(isRepack()?1:0)+","+
				(isPreAir()?1:0)+","+
				(isPROPER()?1:0)+","+
				(isSottotitolo()?1:0)+")";
		if(Database2.updateQuery(query)==false)
			ManagerException.registraEccezione(getClass().getName()+ "\nMetodo insert()\nQuery:"+query+"\n");
		
	}
	public void update(){
		int i=0;
		SQLParameter[] par=new SQLParameter[9];
		par[i++]=new SQLParameter(SQLParameter.INTEGER, serietv.getIDDb(), "id_serie");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getScaricato(), "vista");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getStagione(), "serie");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getEpisodio(), "episodio");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, is720p()?1:0, "HD720p");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isRepack()?1:0, "repack");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isPreAir()?1:0, "preair");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isPROPER()?1:0, "proper");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isSottotitolo()?1:0, "sottotitolo");
		
		SQLParameter[] con=new SQLParameter[1];
		con[0]=new SQLParameter(SQLParameter.TEXT, getUrl(), "magnet");
		
		Database.update(Database.TABLE_TORRENT, par, con, "AND", "=");
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
}
