package InfoSerie;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public interface ProviderInfo {
	public Object cercaSerie(String nome) throws SerieNotFound;
	public Object cercaEpisodio(Object id, int serie, int episodio) throws EpisodioNotFound;
	public String scaricaTrailer(Object id, int serie, int episodio);
	public ArrayList<String> cercaAttori(Object id);
	public float cercaVotoEpisodio(Object id, int serie, int episodio);
	public GregorianCalendar cercaDataProssimoEpisodio(Object id);
	public String cercaGenere(Object id);
}
