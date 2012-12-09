package Sottotitoli;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Programma.Download;
import Programma.OperazioniFile;
import SerieTV.Torrent;

public class Subsfactory implements ProviderSottotitoli {
	private final static String URL_ELENCO_SERIE="http://subsfactory.it/subtitle/index.php?&direction=0&order=nom";
	
	private ArrayList<SerieSub> elenco_serie;
	
	public Subsfactory() {
		elenco_serie=new ArrayList<SerieSub>();
		caricaElencoSerie();
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
		return elenco_serie;
	}

	@Override
	public String getProviderName() {
		return "Subsfactory.it";
	}

	@Override
	public void caricaElencoSerie() {
		FileReader f_r;
		try {
			Download.downloadFromUrl(URL_ELENCO_SERIE, "response_subs");
			f_r=new FileReader("response_subs");
		}
		catch (IOException e) {
			OperazioniFile.deleteFile("response_subs");
			return;
		}
		elenco_serie.clear();
		Scanner file=new Scanner(f_r);
		
		while(!file.nextLine().contains("<select name=\"loc\""));
		//file.nextLine();
		
		while(true){
			String riga=file.nextLine().trim();
			if(riga.startsWith("<option value=")){
				String path=riga.substring("<option value=\"files/".length(), riga.indexOf("\">")-1).trim();
				if(path.startsWith("Film"))
					continue;
				String nome=riga.substring(riga.indexOf("\">")+2, riga.indexOf("</option>")).trim().replace("&nbsp;", "");
				if(path.split("/").length>2)
					continue;
				addSerie(new SerieSub(nome, path));
			}
			else if(riga.compareToIgnoreCase("</select>")==0)
				break;
		}
		
		file.close();
		try {
			f_r.close();
			OperazioniFile.deleteFile("response_subs");
		}
		catch (IOException e) {}
	}
	private void addSerie(SerieSub s){
		if(elenco_serie==null){
			elenco_serie=new ArrayList<SerieSub>();
		}
		if(elenco_serie.isEmpty())
			elenco_serie.add(s);
		else {
			int i=0;
			while(i<elenco_serie.size()){
				SerieSub s_f_e=elenco_serie.get(i);
				int res=s.getNomeSerie().compareToIgnoreCase(s_f_e.getNomeSerie());
				if(res<0)
					break;
				else if(res==0)
					return;
				i++;
			}
			elenco_serie.add(i, s);
		}
	}
	public void stampa(){
		for(int i=0;i<elenco_serie.size();i++){
			SerieSub s=elenco_serie.get(i);
			System.out.println(s.getNomeSerie()+" - "+(String)s.getID());
		}
	}
	
	public static void main(String[] args){
		Subsfactory s=new Subsfactory();
		s.stampa();
	}
}
