package SerieTV;

import java.util.ArrayList;

import Naming.CaratteristicheFile;

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
	private Stagione addStagione(int num_stagione){
		synchronized (this) {
			boolean inserita=false;
			for(int i=0;i<stagioni.size();i++){
				if(num_stagione<stagioni.get(i).getStagione()){
					Stagione s=new Stagione(num_stagione);
					stagioni.add(i, s);
					inserita=true;
					break;
				}
				else if(num_stagione==stagioni.get(i).getStagione()){
					return stagioni.get(i);
				}
			}
			if(!inserita){
				Stagione s=new Stagione(num_stagione);
				stagioni.add(s);
				return s;
			}
			return null;
		}
	}
	public void addEpisodio(String link, CaratteristicheFile c){
		Stagione stag=getStagione(c.getStagione());
		if(stag!=null){
			
		}
		//TODO completare
	}
	public void addEpisodio(String link){
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
