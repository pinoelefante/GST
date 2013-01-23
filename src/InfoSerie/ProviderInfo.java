package InfoSerie;

public interface ProviderInfo {
	public Object cercaSerie(String nome) throws SerieNotFound;
	public Object cercaEpisodio(Object id, int serie, int episodio) throws EpisodioNotFound;
	public String scaricaTrailer(Object id, int serie, int episodio);
}
