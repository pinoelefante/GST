package LettoreVideo;

import java.util.ArrayList;

import Programma.ManagerException;

public class Playlist {
	private ArrayList<PlaylistItem> playlist;
	private int currentItem=0;
	
	public Playlist(){
		playlist=new ArrayList<PlaylistItem>(3);
	}
	public void addItem(String file_path){
		playlist.add(new PlaylistItem(file_path));
	}
	public void addItem(PlaylistItem i){
		playlist.add(i);
	}
	public void removeItem(PlaylistItem item){
		int index=playlist.indexOf(item);
		if(index<0)
			return;
		if(index<currentItem)
			currentItem--;
		playlist.remove(playlist.get(index));
		playlist.trimToSize();
	}
	public void removeItem(int index){
		if(playlist.isEmpty())
			return;
		if(index<0 || index>=playlist.size())
			return;
		if(index<currentItem)
			currentItem--;
		playlist.remove(index);
	}
	public void clear(){
		playlist.clear();
		playlist.trimToSize();
	}
	private int search(String filename){
		for(int i=0;i<playlist.size();i++){
			String p=playlist.get(i).toString();
			if(p.compareTo(filename)==0)
				return i;
		}
		return -1;
	}
	public void moveUp(PlaylistItem item){
		if(playlist.isEmpty())
			return;
		int index=playlist.indexOf(item);
		if(index>0){
			if(index==currentItem)
				currentItem--;
			PlaylistItem last=playlist.set(index-1, item);
			playlist.set(index, last);
		}
	}
	public void moveUp(int index){
		if(playlist.isEmpty())
			return;
		if(index>0){
			if(index==currentItem)
				currentItem--;
			PlaylistItem last=playlist.set(index-1, playlist.get(index));
			playlist.set(index, last);
		}
	}
	public void moveUp(String file){
		if(playlist.isEmpty())
			return;
		
		int index=search(file);
		
		if(index==0 || index==-1)
			return;
		if(index==currentItem)
			currentItem--;
		PlaylistItem last=playlist.set(index-1, playlist.get(index));
		playlist.set(index, last);
	}
	public void moveDown(PlaylistItem item){
		if(playlist.isEmpty())
			return;
		int index=playlist.indexOf(item);
		if(index>=0 && index!=playlist.size()-1){
			if(index==currentItem)
				currentItem++;
			PlaylistItem last=playlist.set(index+1, item);
			playlist.set(index, last);
		}
	}
	public void moveDown(int index){
		if(playlist.isEmpty())
			return;
		if(index>=0 && index!=playlist.size()-1){
			if(index==currentItem)
				currentItem++;
			PlaylistItem last=playlist.set(index+1, playlist.get(index));
			playlist.set(index, last);
		}
	}
	public void moveDown(String file){
		if(playlist.isEmpty())
			return;
		
		int index=search(file);
		if(index==playlist.size()-1 || index==-1)
			return;
		if(index==currentItem)
			currentItem++;
		PlaylistItem last=playlist.set(index+1, playlist.get(index));
		playlist.set(index, last);
	}
	public String toString(){
		String s="";
		if(playlist.isEmpty())
			s="list empty";
		else
			for(int i=0;i<playlist.size();i++)
				s+=i+"."+playlist.get(i)+"\n";
		return s;
	}
	public boolean hasNext(){
		if(playlist.isEmpty())
			return false;
		else if(currentItem>=playlist.size())
			return false;
		else
			return true;
	}
	public String getNext(){
		return getItem(currentItem);
	}
	public String getPrevious(){
		return getItem(currentItem-2);
	}
	public String getItem(int i) throws RuntimeException{
		if(i<0)
			i=playlist.size()-1;
		if(i==playlist.size())
			i=0;
		if(i>=0 && i<playlist.size()){
			currentItem=i;
			return playlist.get(i++).getPath();
		}
		RuntimeException e=new RuntimeException("Playlist - Indice non valido\nCurrentItem="+currentItem+"\n"+"Indice richiesto: "+i+"\nLunghezza playlist: "+playlist.size());
		ManagerException.registraEccezione(e);
		throw e;
	}
	public int getCurrentItem(){
		return currentItem;
	}
	public int getLastItem(){
		return playlist.size()-1;
	}
	public PlaylistItem[] getArray(){
		return playlist.toArray(new PlaylistItem[playlist.size()]);
	}
	public void setCurrentItem(int i){
		if(!playlist.isEmpty()){
			if(i>=0 && i<playlist.size())
				currentItem=i;
		}
	}
}
