package Naming;

public class CaratteristicheFile {
	private int stagione, episodio;
	private boolean hd720, repack, proper;
	
	public CaratteristicheFile() {}
	public int getStagione() {
		return stagione;
	}
	public void setStagione(int stagione) {
		this.stagione = stagione;
	}
	public int getEpisodio() {
		return episodio;
	}
	public void setEpisodio(int episodio) {
		this.episodio = episodio;
	}
	public boolean is720p() {
		return hd720;
	}
	public void set720p(boolean hd720) {
		this.hd720 = hd720;
	}
	public boolean isRepack() {
		return repack;
	}
	public void setRepack(boolean repack) {
		this.repack = repack;
	}
	public boolean isProper() {
		return proper;
	}
	public void setProper(boolean proper) {
		this.proper = proper;
	}
	
}
