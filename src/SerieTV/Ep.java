package SerieTV;

import Naming.CaratteristicheFile;

public class Ep {
	private String link;
	private CaratteristicheFile stats;
	
	public Ep(String url, CaratteristicheFile s){
		link=url;
		stats=s;
	}
	public String getLink(){
		return link;
	}
	public CaratteristicheFile getStats(){
		return stats;
	}
}
