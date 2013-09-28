package LettoreVideo;

import SerieTV.Torrent;

public class PlaylistItem {
	private Torrent t;
	/*
	private String path_file;
	private CaratteristicheFile proprieta;
	private String nomeItem; //nome della serie
	*/
	public PlaylistItem(Torrent torrent){
		t=torrent;
	}
	public String toString(){
		String nome=(t.getNomeSerie()+" S"+(t.getStagione()<10?"0"+t.getStagione():t.getStagione())+" E"+(t.getEpisodio()<10?"0"+t.getEpisodio():t.getEpisodio())+" - "+(t.is720p()?"HD":"SD")+" "+(t.isPreAir()?"Pre":"")).trim();
		return nome;
	}
	public String getPath() {
		return t.getFilePath();
	}
	public void setPlayed(){
		t.setScaricato(Torrent.VISTO, true);
	}
}
