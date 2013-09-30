package Sottotitoli;

import java.util.ArrayList;

import SerieTV.SerieTV;
import SerieTV.Torrent;

public abstract interface ProviderSottotitoli {
	public boolean scaricaSottotitolo(Torrent t);
	public SerieSub getSerieAssociata(SerieTV serie);
	public boolean cercaSottotitolo(Torrent t);
	public ArrayList<SerieSub> getElencoSerie();
	public String getProviderName();
	public void aggiornaElencoSerieOnline();
	public int getProviderID();
}
