package Programma;

import SerieTV.SerieTV;
import SerieTV.Torrent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import Database.SQLColumn;
import GUI.Interfaccia;
import SerieTV.GestioneSerieTV;
import StruttureDati.Indexable;

public class Update {
	public static void start() {
		if(Settings.isNewUpdate()){
			boolean option_set=false;
			switch(Settings.getLastVersion()){
				case 0: { //versione < 81 - prima release con questo metodo
					if(!OperazioniFile.fileExists("settings.dat")){
						option_set=true;
						Interfaccia.ShowFrameOpzioni();	
						if(Interfaccia.getFrameOpzioni()!=null)
							while(Interfaccia.getFrameOpzioni().isVisible())
								try {
									Thread.sleep(1000);
								} 
								catch (InterruptedException e) {
									ManagerException.registraEccezione(e);
								}
					}
				}
				case 85:{
					if(!option_set)
						update_85_to_86_settings();
					try{
						update_85_to_86_db();
					}
					catch(Exception e){
						System.out.println(e.getMessage());
						e.printStackTrace();
						ManagerException.registraEccezione(e);
					}
					Settings.setLastVersion(86);
				}
				case 89: {
					ArrayList<SQLColumn> colonne=new ArrayList<SQLColumn>();
					colonne.add(new SQLColumn("nomeserie", "TEXT", "", "", "NOT NULL"));
					colonne.add(new SQLColumn("id", "INTEGER", "0", "UNIQUE", ""));
					colonne.add(new SQLColumn("generi", "TEXT", "", "", ""));
					colonne.add(new SQLColumn("episodi", "TEXT", "", "", ""));
					colonne.add(new SQLColumn("attori", "TEXT", "", "", ""));
					try {
						Database.Database.createTable(Database.Database.TABLE_TVRAGE, colonne);
					}
					catch (Exception e) {
						ManagerException.registraEccezione(e);
						e.printStackTrace();
					}
					Settings.setLastVersion(90);
				}
				case 90: {	//Controlla lo stato attuale delle puntate
					GestioneSerieTV.carica_serie_database();
					ArrayList<SerieTV> el_s=GestioneSerieTV.getElencoSerieInserite();
					while(!el_s.isEmpty()){
						SerieTV st=el_s.get(0);
						ArrayList<Indexable> eps=st.getEpisodi().getLinear();
						for(int i=0;i<eps.size();i++){
							Torrent t=(Torrent)eps.get(i);
							try {
								String path=OperazioniFile.cercavideofile(t);
								if(path.length()>0){
									if(t.getScaricato()==Torrent.RIMOSSO || t.getScaricato()==Torrent.IGNORATO)
										t.setScaricato(Torrent.SCARICATO, true);
								}
							}
							catch (FileNotFoundException e) {
								if(t.getScaricato()==Torrent.SCARICATO || t.getScaricato()==Torrent.VISTO)
									t.setScaricato(Torrent.RIMOSSO, true);
								//e.printStackTrace();
								//ManagerException.registraEccezione(e);
							}
						}
						el_s.remove(st);
					}
					GestioneSerieTV.getElencoSerieInserite().clear();
					Settings.setLastVersion(91);
				}
				default:
					Settings.setLastVersion(Settings.getVersioneSoftware());
					Settings.setNewUpdate(false);
			}
		}
	}
	private static void update_85_to_86_settings(){
		FileReader file_r = null;
		try {
			file_r = new FileReader("settings.dat");
			Scanner file = new Scanner(file_r);
			while(file.hasNextLine()){
				String linea=file.nextLine().trim();
				if(!linea.contains("=")) continue;
				String tipo_opzione=linea.substring(0, linea.indexOf("=")).trim();
				String opzione=linea.substring(linea.indexOf("=")+1).trim();
				if(opzione==null || opzione.isEmpty()) continue;
				try{
					switch(tipo_opzione){
						case "client_path":	Settings.setClientPath(opzione); break;
						case "directory": Settings.setDirectoryDownload(opzione); break;
						case "tray_on_icon": Settings.setTrayOnIcon(Boolean.parseBoolean(opzione)); break;
						case "ask_on_close": Settings.setAskOnClose(Boolean.parseBoolean(opzione)); break;
						case "start_hidden": Settings.setStartHidden(Boolean.parseBoolean(opzione)); break;
						case "auto_search": Settings.setDownloadAutomatico(Boolean.parseBoolean(opzione)); break;
						case "auto_search_between": Settings.setMinRicerca(Integer.parseInt(opzione)); break;
						case "autostart": Settings.setAutostart(Boolean.parseBoolean(opzione)); break;
						case "language": Settings.setLingua(Integer.parseInt(opzione)+1); break;
						case "update": Settings.setNewUpdate(Boolean.parseBoolean(opzione)); break;
						case "last_version": Settings.setLastVersion(Integer.parseInt(opzione)); break;
						case "n_starts": Settings.setNumeroAvvii(Integer.parseInt(opzione)+1); break;
						case "itasa_active": Settings.setRicercaSottotitoli(Boolean.parseBoolean(opzione)); break;
						case "always_on_top": Settings.setAlwaysOnTop(Boolean.parseBoolean(opzione)); break;
						case "VLCPath": Settings.setVLCPath(opzione); break;
						case "itasa_user": Settings.setItasaUsername(opzione); break;
						case "itasa_psw": Settings.setItasaPassword(opzione); break;
					}
				} 
				catch (NumberFormatException e) {
					ManagerException.registraEccezione(e);
				}
			}
			file.close();
			OperazioniFile.copyfile("settings.dat", "settings.dat.bak");
			OperazioniFile.deleteFile("settings.dat");
		} 
		catch (FileNotFoundException e) {	
			ManagerException.registraEccezione(e);
		}
	}
	private static void update_85_to_86_db(){
		if(GestioneSerieTV.Showlist()){
			
			FileReader file_reader;
			try {
				file_reader = new FileReader("st.dat");
			}
			catch (FileNotFoundException e) {
				ManagerException.registraEccezione(e);
				return;
			}
			
			Scanner file = new Scanner(file_reader);
			SerieTV serie = null;
			while (file.hasNext()) {
				String test = file.next();
				switch (test.charAt(0)) {
					case 's':
						String nome_serie = file.next().trim().replace('_', ' ');
						String url = file.next().trim();
						System.out.println("Convertendo: " +nome_serie+", "+url);
						serie = GestioneSerieTV.getSerie(GestioneSerieTV.getElencoSerieCompleto(), url);
						if(serie!=null){
							System.out.println("Corrispondenza: " +serie.getNomeSerie());
							serie.setInserita(1);
							try {
								serie.aggiornaTorrentList().join();
							}
							catch (InterruptedException e) {
								e.printStackTrace();
								ManagerException.registraEccezione(e);
							}
						}
						break;
					case 'i':
						int id_sub=file.nextInt();
						if(serie!=null)
							serie.setItasaID(id_sub);
						break;
					case 'z':
						int seas=file.nextInt();
						int ep=file.nextInt();
						int tipo=file.nextInt();
						if(serie!=null){
							Torrent t_s=serie.getTorrentBySeasonAndEpisode(seas, ep, tipo==1?true:false);
							if(t_s!=null)
								t_s.setSottotitolo(true, true);
						}
						break;
					case 't':
						boolean visto = file.nextBoolean();
						String url_torrent = file.next().trim();
						if(serie!=null){
							Torrent t=(Torrent)serie.getEpisodi().get(url_torrent);
							t.setScaricato(visto==true?Torrent.SCARICATO:Torrent.SCARICARE, true);
						}
						break;
					case 'e':
						if(serie!=null)
							serie.UpdateDB();
						break;
				}
			}
			file.close();
			OperazioniFile.copyfile("st.dat", "st.dat.bak");
			OperazioniFile.deleteFile("st.dat");
		}
	}
}
