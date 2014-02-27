package GUI.player;

import java.io.File;

class ItemPlaylist implements ItemPlaylistInterface{
	private String path;
	private String toShow;
	public ItemPlaylist(String path){
		this.path=path;
		if(path.contains(File.separator)){
			toShow=path.substring(path.lastIndexOf(File.separator)+1);
			if(toShow.contains(".")){
				toShow=toShow.substring(0, toShow.lastIndexOf("."));
			}
		}
		else 
			toShow=path;
	}
	public String getPath(){
		return path;
	}
	public String getToShow(){
		return toShow;
	}
	public String toString(){
		return toShow;
	}
	@Override
	public void setPlayed() {}
}