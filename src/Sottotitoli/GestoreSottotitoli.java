package Sottotitoli;

import java.util.ArrayList;

import Database.Database;
import GUI.Interfaccia;
import Naming.Renamer;
import Programma.ManagerException;
import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;
import SerieTV.Torrent;
import StruttureDati.db.KVResult;

public class GestoreSottotitoli {
	class AssociatoreAutomatico extends Thread {
		public void run(){
			System.out.println("Avvio associatore");
			ArrayList<SerieTV> st=GestioneSerieTV.getElencoSerieInserite();
			for(int i=0;i<st.size();i++){
				SerieTV s=st.get(i);
				associaSerie(s);
			}
		}
	}
	class RicercaSottotitoliAutomatica extends Thread{
		public void run(){
			long sleep_time=/*un minuto*/(60*1000)*10/*10 minuti*/;
			
			if(((ItalianSubs)itasa).isLocked())
				((ItalianSubs)itasa).attendiUnlock();
			
			try {
				Thread t=new AssociatoreAutomatico();
				t.start();
				t.join();
			} 
			catch (InterruptedException e1) {
				e1.printStackTrace();
				ManagerException.registraEccezione(e1);
			}
			
			System.out.println("Avvio thread ricerca automatica");
			if(sottotitoli_da_scaricare.size()==0)
				System.out.println("Ricerca sottotitoli - Coda vuota");
			
			do{
				for(int i=0;i<sottotitoli_da_scaricare.size();){
					Torrent t=sottotitoli_da_scaricare.get(i);
					if(t==null)
						continue;
					System.out.println("Thread sottotitolo - Cercando "+t);
					if(!scaricaSottotitolo(t))
						i++;
				}
				try {
					System.out.println("Thread ricerca sottotitoli - pausa");
					sleep(sleep_time);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
					ManagerException.registraEccezione(e);
					break;
				}
			}while(true);
		}
	}
	public final static int ITASA=1, SUBSFACTORY=2, SUBSPEDIA=3; 
	private Thread ricerca_automatica;
	private ProviderSottotitoli itasa;
	private ProviderSottotitoli subsfactory;
	private ProviderSottotitoli subspedia;
	
	private ArrayList<Torrent> sottotitoli_da_scaricare;
	
	public GestoreSottotitoli(){
		sottotitoli_da_scaricare=new ArrayList<Torrent>();
		itasa=new ItalianSubs();
		subsfactory=new Subsfactory();
		subspedia=new Subspedia();
		ricerca_automatica=new RicercaSottotitoliAutomatica();
	}
	public void avviaRicercaAutomatica(){
		if(ricerca_automatica==null)
			ricerca_automatica=new RicercaSottotitoliAutomatica();
		else if(!ricerca_automatica.isAlive())
			ricerca_automatica=new RicercaSottotitoliAutomatica();
		ricerca_automatica.start();
	}
	public void stopRicercaAutomatica(){
		if(ricerca_automatica!=null){
			ricerca_automatica.interrupt();
			ricerca_automatica=null;
		}
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
		String query="INSERT INTO "+Database.TABLE_LOGSUB+" (serie, stagione, episodio, id_provider) VALUES ("+
				"\""+t.getSerieTV().getNomeSerie()+"\""+
				","+t.getStagione()+
				","+t.getEpisodio()+
				","+provider.getProviderID()+")";
		Database.updateQuery(query);
		Interfaccia.getInterfaccia().addEntrySottotitolo(t.getNomeSerie(), t.getStagione(), t.getEpisodio(), provider.getProviderName());
	}
	private void aggiungiLogEntry(String serie, int stagione, int episodio, int provider){
		Interfaccia.getInterfaccia().addEntrySottotitolo(serie, stagione,episodio, getProvider(provider).getProviderName());
	}
	public void loadLast10(){
		String query="SELECT * FROM "+Database.TABLE_LOGSUB+" ORDER BY id DESC LIMIT 10";
		ArrayList<KVResult<String, Object>> res=Database.selectQuery(query);
		for(int i=res.size()-1;i>=0;i--){
			KVResult<String, Object> r=res.get(i);
			String nomeserie=(String) r.getValueByKey("serie");
			int stagione=(int) r.getValueByKey("stagione");
			int episodio=(int) r.getValueByKey("episodio");
			int id_provider=(int) r.getValueByKey("id_provider");
			aggiungiLogEntry(nomeserie, stagione, episodio, id_provider);
		}
	}
	public void aggiungiEpisodio(Torrent t){
		for(int i=0;i<sottotitoli_da_scaricare.size();i++){
			Torrent t1=sottotitoli_da_scaricare.get(i);
			if(t.getUrl().compareToIgnoreCase(t1.getUrl())==0)
				return;
		}
		sottotitoli_da_scaricare.add(t);
		if(Interfaccia.getInterfaccia()!=null)
			Interfaccia.getInterfaccia().subAddSubDownload(t);
	}

	public void rimuoviEpisodio(Torrent torrent) {
		sottotitoli_da_scaricare.remove(torrent);
	}
	public boolean associaSerie(SerieTV s){
		boolean itasa_assoc=false, it_al=false, subs_assoc=false, subs_al=false;
		if(s.getIDItasa()<=0){
			String id=itasa.getIDSerieAssociata(s.getNomeSerie());
			if(id!=null){
				s.setIDItasa(Integer.parseInt(id));
				itasa_assoc=true;
			}
		}
		else
			it_al=true;
		
		if(s.getSubsfactoryDirectory().isEmpty()){ //TODO modificare usando id numerico
			String id=subsfactory.getIDSerieAssociata(s.getNomeSerie());
			if(id!=null){
				s.setSubsfactoryDirectory(id);
				subs_assoc=true;
			}
		}
		else
			subs_al=true;
		
		if(itasa_assoc || subs_assoc)
			s.aggiornaDB();
		
		return (itasa_assoc || subs_assoc) || (it_al || subs_al);
	}
	public boolean cercaSottotitolo(Torrent t){
		boolean itasa, subsfactory, subspedia;
		itasa=this.itasa.cercaSottotitolo(t);
		subsfactory=this.subsfactory.cercaSottotitolo(t);
		subspedia=this.subspedia.cercaSottotitolo(t);
		return (itasa || subsfactory || subspedia);
	}
	public boolean scaricaSottotitolo(Torrent t){
		if(itasa.scaricaSottotitolo(t)){
			aggiungiLogEntry(t, this.itasa);
		}
		else if(subsfactory.scaricaSottotitolo(t)){
			aggiungiLogEntry(t, this.subsfactory);
		}
		else if(subspedia.scaricaSottotitolo(t)){
			aggiungiLogEntry(t, this.subspedia);
		}
		else 
			return false;
		
		Renamer.rinominaSottotitolo(t);
		return true;
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
	public ProviderSottotitoli getProvider(int provider){
		switch(provider){
			case ITASA:
				return itasa;
			case SUBSFACTORY:
				return subsfactory;
			case SUBSPEDIA:
				return subspedia;
		}
		return null;
	}
	public boolean localSearch(Torrent t){
		//TODO ricerca locale del sottotitolo
		return false;
	}
}