package SerieTV;

import Naming.CaratteristicheFile;
import Naming.Naming;

public class SerieTV {
	public final static int STATO_IN_CORSO=0, STATO_TERMINATA=1, STATO_CONCLUSA=2;
	
	private int 	id_database;
	private String	nome_serie;
	private String	eztv_url;
	private int		karmorra_id;
	private ElencoEpisodi 	episodi;
	private int		id_itasa=-1;
	private String 	directory_subsfactory="";
	private int		stato; //1 se la serie è Ended altrimenti 0
	private int 	inserita;
	private int		end; //1 se sono stati scaricati tutti gli episodi e la serie è ended. Setta da non cercare altri episodi
	private int 	tvrage, tvdb;
	
	public SerieTV() {
		episodi=new ElencoEpisodi();
	}
	public String getNomeSerie() {
		return nome_serie;
	}
	/**
	 * Aggiunge il link alla serie.
	 * @param link	link da aggiungere
	 * @param toParse stringa da parsare per ottenere informazioni
	 */
	public void aggiungiEpisodio(String link, String toParse) {
		episodi.addEpisodio(link, Naming.parse(link, null));
	}
	/**
	 * Aggiunge il link alla serie.
	 * Effettua il parsing del link per trovare informazioni sulla puntata.
	 * E' consigliabile usare aggiungiEpisodio(String, String)
	 * @param link link dell'episodio e stringa da parsare per ottenere informazioni
	 */
	public void aggiungiEpisodio(String link){
		episodi.addEpisodio(link);
	}
	/**
	 * Aggiunge il link alla serie avendo già effettuato il parsing dell'episodio
	 * @param link link dell'episodio
	 * @param s statistiche del link
	 */
	public void aggiungiEpisodio(String link, CaratteristicheFile s){
		episodi.addEpisodio(link, s);
	}
	public String toString(){
		return getNomeSerie();
	}
}
