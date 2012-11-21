package Sottotitoli;

import java.util.ArrayList;

import SerieTV.Torrent;

public class Subsfactory implements ProviderSottotitoli {
	
	public Subsfactory() {
	
	}

	@Override
	public boolean scaricaSottotitolo(Torrent t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getIDSerieAssociata(String nome_serie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean cercaSottotitolo(Torrent t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<SerieSub> getElencoSerie() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProviderName() {
		return "Subsfactory.it";
	}

}
