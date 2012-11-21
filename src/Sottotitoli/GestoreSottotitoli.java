package Sottotitoli;

import java.util.ArrayList;

import Database.SQLParameter;
import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;
import SerieTV.Torrent;
//import SerieTV.GestioneSerieTV;
import Database.Database;

public class GestoreSottotitoli {
	public final static int ITASA=1, SUBSFACTORY=2, SUBSPEDIA=3; 
	private ProviderSottotitoli itasa;
	private ProviderSottotitoli subsfactory;
	//TODO classe per subspedia
	@SuppressWarnings("unused")
	private ProviderSottotitoli subspedia;
	
	private ArrayList<Torrent> sottotitoli_da_scaricare;
	
	public GestoreSottotitoli(){
		sottotitoli_da_scaricare=new ArrayList<Torrent>();
		itasa=new ItalianSubs();
		subsfactory=new Subsfactory();
	}
	
	public ArrayList<Torrent> getSottotitoliDaScaricare(){
		return sottotitoli_da_scaricare;
	}
	public void cercaAssociazioniSub(){
		ArrayList<SerieTV> elenco_serie=GestioneSerieTV.getElencoSerieCompleto();
		for(int i=0;i<elenco_serie.size();i++){
			SerieTV s=elenco_serie.get(i);
			associaSerie(s);
		}
		elenco_serie.clear();
		elenco_serie.trimToSize();
		elenco_serie=null;
	}
	public void aggiungiLogEntry(Torrent t, ProviderSottotitoli provider){
		SQLParameter[] p=new SQLParameter[4];
		p[0]=new SQLParameter(SQLParameter.TEXT, t.getNomeSerie(), "nome_serie");
		p[1]=new SQLParameter(SQLParameter.INTEGER, t.getSerie(), "serie");
		p[2]=new SQLParameter(SQLParameter.INTEGER, t.getPuntata(), "episodio");
		p[3]=new SQLParameter(SQLParameter.TEXT, provider.getProviderName(), "provider");
		Database.insert(Database.TABLE_LOGSUB, p);
	}
	public void aggiungiEpisodio(Torrent t){
		for(int i=0;i<sottotitoli_da_scaricare.size();i++){
			Torrent t1=sottotitoli_da_scaricare.get(i);
			if(t.getUrl().compareToIgnoreCase(t1.getUrl())==0)
				return;
		}
		sottotitoli_da_scaricare.add(t);
	}

	public void rimuoviEpisodio(Torrent torrent) {
		sottotitoli_da_scaricare.remove(torrent);
	}
	public boolean associaSerie(SerieTV s){
		boolean itasa_assoc=false, it_al=false, subs_assoc=false, subs_al=false;
		if(s.getItasaID()<=0){
			String id=itasa.getIDSerieAssociata(s.getNomeSerie());
			if(id!=null){
				s.setItasaID(Integer.parseInt(id));
				itasa_assoc=true;
			}
		}
		else
			it_al=true;
		
		
		if(s.getSubsfactoryDirectory().isEmpty()){
			String id=subsfactory.getIDSerieAssociata(s.getNomeSerie());
			if(id!=null){
				s.setSubsfactoryID(id);
				subs_assoc=true;
			}
		}
		else
			subs_al=true;
		
		if(itasa_assoc || subs_assoc)
			s.UpdateDB();
		
		return (itasa_assoc || subs_assoc) || (it_al || subs_al);
	}
	public boolean cercaSottotitolo(Torrent t){
		boolean itasa, subsfactory;
		itasa=this.itasa.cercaSottotitolo(t);
		subsfactory=this.subsfactory.cercaSottotitolo(t);
		return (itasa || subsfactory);
	}
	public boolean scaricaSottotitolo(Torrent t){
		boolean subsfactory=false;
		boolean itasa=this.itasa.scaricaSottotitolo(t);
		
		if(!itasa)
			subsfactory=this.subsfactory.scaricaSottotitolo(t);
	
		if(itasa||subsfactory)
			Renamer.rinominaSottotitolo(t);
		
		if(itasa)
			aggiungiLogEntry(t, this.itasa);
		else if(subsfactory)
			aggiungiLogEntry(t, this.subsfactory);
		
		return (itasa||subsfactory);
	}
	public ArrayList<SerieSub> getElencoSerie(int provider){
		switch(provider){
			case ITASA:
				return itasa.getElencoSerie();
			case SUBSFACTORY:
				return subsfactory.getElencoSerie();
			case SUBSPEDIA:
				System.out.println("Subspedia - getElencoSerie() - Funzione non supportata");
		}
		return null;
	}
}

