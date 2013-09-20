package SerieTV;

import java.io.File;
import java.util.ArrayList;

import Database.Database;
import Naming.CaratteristicheFile;
import Programma.Settings;

public class Torrent {
	public final static int SCARICARE=0, SCARICATO=1, VISTO=2, RIMOSSO=3, IGNORATO=4; 
	private String	url;
	private int		stato; //0 non scaricato - 1 scaricato - 2 visto - 3 rimosso - 4 ignorato
	private boolean preair;
	private boolean sub_down; //true se è da scaricare, false non scaricare
	private CaratteristicheFile prop_torrent;
	private SerieTV serietv;
	private int id_db, id_tvdb;

	public Torrent(SerieTV st, String url, int stato_t) {
		this.url=url;
		stato=stato_t;
		serietv=st;
		prop_torrent=new CaratteristicheFile();
	}
	public Torrent(SerieTV st, String url, int stato_t, CaratteristicheFile f){
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
		if(stat)
			GestioneSerieTV.getSubManager().aggiungiEpisodio(this);
		else
			GestioneSerieTV.getSubManager().rimuoviEpisodio(this);
	}
	public void setSubDownload(boolean stat, boolean update_db){
		sub_down=stat;
		if(stat)
			GestioneSerieTV.getSubManager().aggiungiEpisodio(this);
		else
			GestioneSerieTV.getSubManager().rimuoviEpisodio(this);
		if(update_db)
			updateTorrentInDB();
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
	public String getFormattedName(){
		return serietv.getNomeSerie()+" S"+(getStagione()<10?"0"+getStagione():getStagione())+"E"+(getEpisodio()<10?"0"+getEpisodio():getEpisodio());
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
	private static final String[] estensione_video={".avi", ".mkv", ".mp4", ".m4v", ".mpg", ".mpeg", ".webm", ".ogv"};
	public String getFilePath(){
		String cartella=Settings.getDirectoryDownload()+File.separator+getSerieTV().getFolderSerie();
		//System.out.println("Looking in: "+cartella);
		File dir=new File(cartella);
		if(dir.exists() && dir.isDirectory()){
			String[] list_c=dir.list();
			ArrayList<String> list=new ArrayList<String>();
			for(int i=0;i<list_c.length;i++){
				list.add(dir.getAbsolutePath()+File.separator+list_c[i]);
			}
			
			for(int i=0;i<list.size();i++){
				File f=new File(list.get(i));
				if(f.isDirectory()){
					String[] list_d=f.list();
					for(int j=0;j<list_d.length;j++){
						list.add(f.getAbsolutePath()+File.separator+list_d[j]);
					}
				}
				else {
					String[] patt=new String[]{
							Naming.Naming.PATTERN_SnEn,
							Naming.Naming.PATTERN_SxE,
							Naming.Naming.PATTERN_Part_dotnofn,
							Naming.Naming.PATTERN_nofn
					};
					String toParse=list.get(i);
					if(toParse.contains(File.separator)){
						toParse=toParse.substring(toParse.lastIndexOf(File.separator));
					}
					CaratteristicheFile stat=Naming.Naming.parse(toParse, patt);
					if(stat!=null){
						if(stat.compareStats(getStats())==0){
							if(stat.getStagione()==getStagione() && stat.getEpisodio()==getEpisodio()){
								for(int j=0;j<estensione_video.length;j++){
									if(toParse.toLowerCase().endsWith(estensione_video[j]))
										return list.get(i);
								}
							}
						}
					}
				}
				//System.out.println(list.get(i));
			}
		}
		return null;
	}
	public static void main(String[] args){
		Settings.baseSettings();
		Database.Connect();
		Settings.CaricaSetting();
		GestioneSerieTV.instance();
		Torrent t=new Torrent(new SerieTV(new EZTV(), "Nomeserie", ""), "", SCARICATO);
		t.setStagione(1);
		t.setEpisodio(1);
		System.out.println("File trovato: "+t.getFilePath());
	}
	public CaratteristicheFile getStats(){
		return prop_torrent;
	}
	public SerieTV getSerieTV(){
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
	public ArrayList<String> getSottotitoliPath(){
		String filepath=getFilePath();
		if(filepath!=null){
			ArrayList<String> subs=new ArrayList<String>(1);
			
			try{
				String filename=filepath.substring(filepath.lastIndexOf(File.separator)+1);
				String directory=filepath.substring(0, filepath.indexOf(filename));
				filename=filename.substring(0, filename.lastIndexOf("."));
				File dir=new File(directory);
				String[] allfiles=dir.list();
				for(int i=0;i<allfiles.length;i++){
					if(allfiles[i].toLowerCase().startsWith(filename.toLowerCase()) && allfiles[i].toLowerCase().endsWith(".srt")){
						if(new File(directory+allfiles[i]).isFile())
							subs.add(directory+allfiles[i]);
					}
				}
			}
			catch(Exception e){}
			subs.trimToSize();
			return subs;
		}
		return null;
		
	}
}
