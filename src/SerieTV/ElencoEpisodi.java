package SerieTV;

import java.util.ArrayList;

public class ElencoEpisodi {
	private ArrayList<Stagione> stagioni;
	
	public ElencoEpisodi(){
		stagioni=new ArrayList<Stagione>();
	}
	public ArrayList<Integer> getElencoStagioniDisponibili(){
		ArrayList<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<stagioni.size();i++){
			list.add(stagioni.get(i).getStagione());
		}
		return list;
	}
	public void addStagione(int n){
		synchronized (this) {
			boolean inserita=false;
			for(int i=0;i<stagioni.size();i++){
				if(n<stagioni.get(i).getStagione()){
					stagioni.add(i, new Stagione(n));
					inserita=true;
					break;
				}
				else if(n==stagioni.get(i).getStagione()){
					return;
				}
			}
			if(!inserita)
				stagioni.add(new Stagione(n));
		}
	}
	public void addEpisodio(/*Aggiungere parametri*/){
		//TODO completare
	}
	public int isStagionePresente(int s){
		for(int i=0;i<stagioni.size();i++)
			if(stagioni.get(i).getStagione()==s)
				return i;
		return -1;
	}
	public Stagione getStagione(int stagione){
		int index=isStagionePresente(stagione);
		if(index>=0)
			return stagioni.get(index);
		else
			return null;
	}
	public Episodio getEpisodio(int s, int e){
		Stagione st=getStagione(s);
		if(st!=null){
			Episodio ep=st.getEpisodio(e);
			if(ep!=null)
				return ep;
			else
				return null;
		}
		else
			return null;
	}
}
