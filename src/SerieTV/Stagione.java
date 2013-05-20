package SerieTV;

import java.util.ArrayList;

public class Stagione {
	private int stagione;
	private ArrayList<Episodio> episodi;
	
	public Stagione(int s){
		stagione=s;
	}
	public int getStagione(){
		return stagione;
	}
	public Episodio getEpisodio(int e){
		for(int i=0;i<episodi.size();i++)
			if(episodi.get(i).getEpisodio()==e)
				return episodi.get(i);
		return null;
	}
	public Episodio addEpisodio(int e){
		boolean inserito=false;
		Episodio ep=null;
		for(int i=0;i<episodi.size();i++){
			if(e<episodi.get(i).getEpisodio()){
				ep=new Episodio(e);
				episodi.add(i, ep);
				inserito=true;
				break;
			}
			else if(e==episodi.get(i).getEpisodio()){
				ep=episodi.get(i);
				inserito=true;
				break;
			}
		}
		if(!inserito){
			ep=new Episodio(e);
			episodi.add(ep);
		}
		return ep;
	}
}