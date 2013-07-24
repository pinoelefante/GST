package LettoreVideo;

import java.io.File;

import Naming.CaratteristicheFile;

public class PlaylistItem {
	private String path_file;
	private CaratteristicheFile proprieta;
	private String nomeItem; //nome della serie
	
	public PlaylistItem(String path){
		path_file=path;
	}
	public PlaylistItem(String path, CaratteristicheFile f){
		path_file=path;
		proprieta=f;
	}
	public PlaylistItem(String path, String nomeserie){
		path_file=path;
		nomeItem=nomeserie;
		proprieta=Naming.Naming.parse(toString(), null);
	}
	public PlaylistItem(String path, CaratteristicheFile f, String nome){
		path_file=path;
		proprieta=f;
		nomeItem=nome;
	}
	public void setProprieta(CaratteristicheFile f){
		proprieta=f;
	}
	public void setNomeFile(String s){
		nomeItem=s;
	}
	public String toString(){
		if(proprieta!=null && nomeItem!=null){
			int episodio=proprieta.getEpisodio();
			int serie=proprieta.getStagione();
			boolean hd=proprieta.is720p();
			return nomeItem+" "+(serie<10?"0"+serie:serie)+" - "+(episodio<10?"0"+episodio:episodio)+(hd?" HD":" SD");
		}
		else {
			if(path_file.contains(File.separator)){
				return path_file.substring(path_file.lastIndexOf(File.separator)+1);
			}
			else
				return path_file;
		}
	}
	public String getPath() {
		return path_file;
	}
}
