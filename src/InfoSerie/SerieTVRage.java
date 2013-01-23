package InfoSerie;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import StruttureDati.GenericElencoIndicizzato;

public class SerieTVRage {
	private String titolo;
	private ArrayList<String> generi;
	private GregorianCalendar ultimo_episodio;
	private ArrayList<String> attori;
	private GenericElencoIndicizzato elenco_episodi;
	private Integer id_serie;
	
	public SerieTVRage(String t, Integer id){
		setTitolo(t);
		setIDSerie(id);
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public ArrayList<String> getGeneri() {
		return generi;
	}

	public void setGeneri(ArrayList<String> generi) {
		this.generi = generi;
	}

	public GregorianCalendar getUltimoEpisodio() {
		return ultimo_episodio;
	}

	public void setUltimoEpisodio(GregorianCalendar ultimo_episodio) {
		this.ultimo_episodio = ultimo_episodio;
	}

	public ArrayList<String> getAttori() {
		return attori;
	}

	public void setAttori(ArrayList<String> attori) {
		this.attori = attori;
	}

	public GenericElencoIndicizzato getElencoEpisodi() {
		return elenco_episodi;
	}

	public void setElencoEpisodi(GenericElencoIndicizzato elenco_episodi) {
		this.elenco_episodi = elenco_episodi;
	}

	public Integer getIDSerie() {
		return id_serie;
	}

	public void setIDSerie(Integer id_serie) {
		this.id_serie = id_serie;
	}
	
}
