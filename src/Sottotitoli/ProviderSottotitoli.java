package Sottotitoli;

import java.util.ArrayList;

import SerieTV.Torrent;

public abstract interface ProviderSottotitoli {
	public boolean scaricaSottotitolo(Torrent t);
	public String getIDSerieAssociata(String nome_serie);
	public boolean cercaSottotitolo(Torrent t);
	public ArrayList<SerieSub> getElencoSerie();
	public String getProviderName();
	public void caricaElencoSerie();
	
}
