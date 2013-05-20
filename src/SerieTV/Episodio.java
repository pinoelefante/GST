package SerieTV;

import java.util.ArrayList;

import Naming.CaratteristicheFile;

public class Episodio {
	private int episodio;
	private ArrayList<Ep> ep_720p;
	private ArrayList<Ep> ep_normale;
	private ArrayList<Ep> ep_altri;
	private boolean sottotitolo_scaricare;
	private boolean puntata_scaricare;
	
	public Episodio(int e){
		ep_720p=new ArrayList<Ep>(1);
		ep_normale=new ArrayList<Ep>(1);
	}
	public int getEpisodio(){
		return episodio;
	}
	public void addLink(String l, CaratteristicheFile s){
		if(s.isDVDRip()){
			if(ep_altri==null)
				ep_altri=new ArrayList<Ep>(1);
			ep_altri.add(new Ep(l, s));
			return;
		}
		if(s.is720p()){
			boolean inserito=false;
			Ep ep=new Ep(l, s);
			for(int i=0;i<ep_720p.size();i++){
				Ep c=ep_720p.get(i);
				if(s.compare(c.getStats())>=0){
					ep_720p.add(i, ep);
					inserito=true;
					break;
				}
			}
			if(!inserito)
				ep_720p.add(ep);
			return;
		}
		else {
			boolean inserito=false;
			Ep ep=new Ep(l, s);
			for(int i=0;i<ep_normale.size();i++){
				Ep c=ep_normale.get(i);
				if(s.compare(c.getStats())>=0){
					ep_normale.add(i, ep);
					inserito=true;
					break;
				}
			}
			if(!inserito)
				ep_normale.add(ep);
			return;
		}
	}
	public boolean isSottotitoloScaricare() {
		return sottotitolo_scaricare;
	}
	public void setSottotitoloScaricare(boolean sottotitolo_scaricare) {
		this.sottotitolo_scaricare = sottotitolo_scaricare;
	}
	public boolean isPuntataScaricare() {
		return puntata_scaricare;
	}
	public void setPuntataScaricare(boolean puntata_scaricare) {
		this.puntata_scaricare = puntata_scaricare;
	}
}
