package SerieTV;

import java.util.ArrayList;

import Sottotitoli.GestoreSottotitoli;

public class GestioneSerieTV2 {
	private static boolean instanced=false; 
	private static ProviderSerieTV p_eztv;

	private static GestoreSottotitoli submanager;
	
	public static void instance(){
		if(!instanced){
    		p_eztv=new EZTV();
    		submanager=new GestoreSottotitoli();
    		instanced=true;
		}
	}
	
	public static void carica_serie_database(){
		p_eztv.caricaSerieDB();
	}
	
	public static ArrayList<SerieTV2> getElencoSerieCompleto(){
		ArrayList<SerieTV2> st=new ArrayList<SerieTV2>();
		for(int i=0;i<p_eztv.getSeriesCount();i++){
			if(p_eztv.getSerieAt(i)!=null)
				st.add(p_eztv.getSerieAt(i));
		}
		return st;
	}
	public static ArrayList<SerieTV2> getElencoSerieInserite(){
		p_eztv.cercaPreferite();
		ArrayList<SerieTV2> st=new ArrayList<SerieTV2>(p_eztv.getPreferiteSerieCount());
		for(int i=0;i<p_eztv.getPreferiteSerieCount();i++){
			if(p_eztv.getPreferiteSerieAt(i)!=null)
				st.add(p_eztv.getPreferiteSerieAt(i));
		}
		return st;
	}
	
	public static void Showlist() {
		p_eztv.aggiornaElencoSerie();
	}
	
	public static boolean aggiungiSerie(SerieTV2 st){
		//TODO
		return false;
	}
	public static boolean rimuoviSerie(SerieTV st){
		//TODO
		return false;
	}
	public static GestoreSottotitoli getSubManager(){
		return submanager;
	}
}
