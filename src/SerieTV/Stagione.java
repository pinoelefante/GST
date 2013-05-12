package SerieTV;

import java.util.ArrayList;

public class Stagione {
	private int stagione;
	private ArrayList<Episodio> episodi;
	
	public Stagione(){
		episodi=new ArrayList<Episodio>();
	}
	
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
}