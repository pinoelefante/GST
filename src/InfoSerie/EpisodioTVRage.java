package InfoSerie;

import StruttureDati.Indexable;

public class EpisodioTVRage implements Indexable {
	private int serie, episodio;
	private float voto;
	
	public EpisodioTVRage(int s, int e, float v){
		serie=s;
		episodio=e;
		setVoto(v);
	}
	
	public int getIndex() {
		return episodio;
	}

	public int getKey() {
		return serie;
	}
	public String getOffKey() {
		return "";
	}
	public String getOffKeyTrim() {
		return "";
	}
	public void setOffKey(String key) {}
	public void setVoto(float v){
		voto=v;
	}
	public float getVoto(){
		return voto;
	}
	public int getSerie(){
		return serie;
	}
	public int getEpisodio(){
		return episodio;
	}
}
