package Sottotitoli;

import java.util.ArrayList;

import SerieTV.Torrent2;

public abstract interface ProviderSottotitoli {
	public boolean scaricaSottotitolo(Torrent2 t);
	public String getIDSerieAssociata(String nome_serie);
	public boolean cercaSottotitolo(Torrent2 t);
	public ArrayList<SerieSub> getElencoSerie();
	public String getProviderName();
	public void caricaElencoSerie();
	
}
