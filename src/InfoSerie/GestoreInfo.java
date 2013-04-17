package InfoSerie;

import java.util.ArrayList;

import Programma.ManagerException;
import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;

public class GestoreInfo {
	private ProviderInfo tvrage;
	
	public GestoreInfo(){
		tvrage=new TVRage();
		controllaAssociazioni();
	}
	public ProviderInfo getProvider(int id){
		switch(id){
			default:
				return tvrage;
		}
	}
	public void associa(SerieTV st){
		if(st.getTVRage()>0)
			return;
		
		try {
			Integer id=(Integer) getProvider(0).cercaSerie(st.getNomeSerie());
			System.out.println("ID serie tv_rage: "+id);
			st.setTVRage(id);
			st.UpdateDB();
		}
		catch (SerieNotFound e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	public void controllaAssociazioni(){
		class checkAssociazioni extends Thread {
			public void run(){
				ArrayList<SerieTV> st=GestioneSerieTV.getElencoSerieInserite();
				for(int i=0;i<st.size();i++)
					associa(st.get(i));
			}
		}
		Thread t=new checkAssociazioni();
		t.start();
	}
	/*
	public static void main(String[] args){
		GestoreInfo info=new GestoreInfo();
		ProviderInfo provider=info.getProvider(0);
		try {
			Integer id=(Integer) provider.cercaSerie("Ripper Street");
			System.out.println(""+id);
		}
		catch (SerieNotFound e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	*/
}
