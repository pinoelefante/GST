package SerieTV;

import java.util.ArrayList;

import Naming.CaratteristicheFile;
import Naming.Naming;

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
			Episodio ep=stag.getEpisodio(c.getEpisodio());
			if(ep!=null){
				ep.addLink(link, c);
			}
			else {
				ep=stag.addEpisodio(c.getEpisodio());
				ep.addLink(link, c);
			}
		}
		else {
			stag=addStagione(c.getStagione());
			Episodio ep=stag.addEpisodio(c.getEpisodio());
			ep.addLink(link, c);
		}
	}
	public void addEpisodio(String link){
		addEpisodio(link, Naming.parse(link, null));
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
