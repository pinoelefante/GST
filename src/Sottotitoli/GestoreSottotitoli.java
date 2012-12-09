package Sottotitoli;

import java.util.ArrayList;

import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;
import SerieTV.Torrent;
import Database.Database;
import Database.SQLParameter;
import GUI.Interfaccia;

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
			} catch (InterruptedException e1) {
				e1.printStackTrace();
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
					break;
				}
			}while(true);
		}
	}
	public final static int ITASA=1, SUBSFACTORY=2, SUBSPEDIA=3; 
	private Thread ricerca_automatica;
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
		SQLParameter[] p=new SQLParameter[4];
		p[0]=new SQLParameter(SQLParameter.TEXT, t.getNomeSerie(), "nome_serie");
		p[1]=new SQLParameter(SQLParameter.INTEGER, t.getSerie(), "serie");
		p[2]=new SQLParameter(SQLParameter.INTEGER, t.getPuntata(), "episodio");
		p[3]=new SQLParameter(SQLParameter.TEXT, provider.getProviderName(), "provider");
		Database.insert(Database.TABLE_LOGSUB, p);
		Interfaccia.addEntryLogSottotitoli(t, provider.getProviderName());
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