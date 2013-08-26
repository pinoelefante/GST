package SerieTV;

import java.util.ArrayList;

import Sottotitoli.GestoreSottotitoli;
import StruttureDati.serietv.Episodio;

public class GestioneSerieTV2 {
	private static boolean instanced=false; 
	private static ArrayList<ProviderSerieTV> providers;

	private static GestoreSottotitoli submanager;
	
	public static void instance(){
		if(!instanced){
			providers=new ArrayList<ProviderSerieTV>(1);
    		//submanager=new GestoreSottotitoli(); TODO rimuovere commento dopo aver fixato la classe
    		providers.add(new EZTV());
    		instanced=true;
		}
	}
	
	public static void carica_serie_database(){
		for(int i=0;i<providers.size();i++)
			providers.get(i).caricaSerieDB();
	}
	
	public static ArrayList<SerieTV2> getElencoSerieCompleto(){
		ArrayList<SerieTV2> res=new ArrayList<SerieTV2>();
		for(int i=0;i<providers.size();i++){
			for(int j=0;j<providers.get(i).getSeriesCount();j++){
				SerieTV2 st=providers.get(i).getSerieAt(j);
				if(st!=null)
					res.add(st);
			}
		}
		return res;
	}
	public static boolean aggiungiSeriePreferita(SerieTV2 serie){
		return serie.getProvider().addSeriePreferita(serie);
	}
	public static ArrayList<SerieTV2> getElencoSerieInserite(){
		ArrayList<SerieTV2> res=new ArrayList<SerieTV2>();
		for(int i=0;i<providers.size();i++){
			ProviderSerieTV provider=providers.get(i);
			for(int j=0;j<provider.getPreferiteSerieCount();j++){
				res.add(provider.getPreferiteSerieAt(j));
			}
		}
		return res;
	}
	
	public static void caricaElencoSerieOnline() {
		for(int i=0;i<providers.size();i++){
			providers.get(i).aggiornaElencoSerie();
		}
	}
	
	public static void rimuoviSeriePreferita(SerieTV2 st){
		st.getProvider().rimuoviSeriePreferita(st);
	}
	public static GestoreSottotitoli getSubManager(){
		return submanager;
	}
	public static ArrayList<Episodio> cericaEpisodiDaScaricare(){
		ArrayList<Episodio> episodi=new ArrayList<Episodio>();
		for(int i=0;i<providers.size();i++){
			ProviderSerieTV p=providers.get(i);
			for(int j=0;j<p.getPreferiteSerieCount();j++){
				p.getPreferiteSerieAt(j).aggiornaEpisodiOnline();
				episodi.addAll(p.nuoviEpisodi(p.getPreferiteSerieAt(j)));
			}
		}
		return episodi;
	}
}
