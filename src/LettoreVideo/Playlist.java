package LettoreVideo;

import java.util.ArrayList;

import Programma.ManagerException;

public class Playlist {
	private ArrayList<String> playlist;
	private int currentItem=0;
	
	public Playlist(){
		playlist=new ArrayList<String>(3);
	}
	public void addItem(String file_path){
		playlist.add(file_path);
	}
	public void removeItem(String file){
		int index=playlist.indexOf(file);
		if(index<currentItem)
			currentItem--;
		playlist.remove(file);
		playlist.trimToSize();
	}
	public void removeItem(String file, int index){
		String item=playlist.get(index);
		if(item.compareTo(file)==0){
			playlist.remove(index);
			if(index<currentItem)
				currentItem--;
		}
		if(playlist.isEmpty())
			clear();
	}
	public void clear(){
		playlist.clear();
		playlist.trimToSize();
	}
	public void moveUp(String file){
		if(playlist.isEmpty())
			return;
		
		int index=playlist.indexOf(file);
		if(index==0)
			return;
		if(index==currentItem)
			currentItem--;
		String last=playlist.set(index-1, file);
		playlist.set(index, last);
	}
	public void moveDown(String file){
		if(playlist.isEmpty())
			return;
		
		int index=playlist.indexOf(file);
		if(index==playlist.size()-1)
			return;
		if(index==currentItem)
			currentItem++;
		String last=playlist.set(index+1, file);
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
		if(currentItem>=playlist.size() || currentItem<0)
			currentItem=0;
		return playlist.get(currentItem++);
	}
	public String getItem(int i) throws RuntimeException{
		if(i>=0 && i<playlist.size()){
			currentItem=i;
			return playlist.get(i);
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
	
	
	public static void main(String[] args){
		
	}
}
