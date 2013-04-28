package Sottotitoli;

public class SottotitoloSubsfactoryDB extends SottotitoloSubsfactory {
	private boolean completa;
	private String url;
	public SottotitoloSubsfactoryDB(int tipo, String url, int stag, int ep, boolean completa, String id) {
		super();
		setIDSerie(id);
		setSeason(stag);
		setEpisode(ep);
		setTipo(tipo);
		setUrl(url);
		setCompleta(completa);
	}
	private void setIDSerie(String id){
		super.id_serie=id;
	}
	private void setSeason(int s){
		super.season=s;
	}
	private void setEpisode(int e){
		super.ep=e;
	}
	private void setTipo(int t){
		switch(t){
			case 0:
				super.setNormale(true);
				super.set720p(false);
				break;
			case 1:
				super.setNormale(false);
				super.set720p(true);
				break;
			case 2:
				super.setNormale(true);
				super.set720p(true);
				break;
		}
	}
	private void setCompleta(boolean c){
		completa=c;
	}
	private void setUrl(String u){
		url=u;
	}
	public String getUrl(){
		return url;
	}
	public boolean isCompleta(){
		return completa;
	}
}
