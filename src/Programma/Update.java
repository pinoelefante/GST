package Programma;

import SerieTV.SerieTV;
import SerieTV.Torrent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import GUI.Interfaccia;
import SerieTV.GestioneSerieTV;

public class Update {
	public static void start() {
		if(Settings.isNewUpdate()){
			switch(Settings.getLastVersion()){
				case 0: { //versione < 81 - prima release con questo metodo
					Interfaccia.ShowFrameOpzioni();
					
					while(Interfaccia.getFrameOpzioni()!=null && Interfaccia.getFrameOpzioni().isVisible()){
						try {Thread.sleep(1000);}
						catch (InterruptedException e) {}
					}
					/*
					if(Settings.isRicercaSottotitoli()){
						int scelta=JOptionPane.showConfirmDialog(Main.fl.getFrame(), "Vuoi associare con il modulo ItaSA per il download dei sottotitoli?", "Associazione sottotitoli", JOptionPane.YES_NO_OPTION);
						if(scelta==JOptionPane.YES_OPTION){
							Interfaccia.associaFrame();
							while(Interfaccia.frame_associa_itasa!=null && Interfaccia.frame_associa_itasa.isVisible()){
								try {Thread.sleep(1000);}
								catch (InterruptedException e) {}
							}
						}
					}
					*/
					Settings.setLastVersion(81);
				}
				case 85:{
					update_85_to_86_settings();
					try{
						update_85_to_86_db();
					}
					catch(Exception e){
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
					Settings.setLastVersion(86);
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
						case "itasa_auto": Settings.setItasaThreadAutoDownload(Boolean.parseBoolean(opzione)); break;
					}
				} catch (NumberFormatException e) {}
			}
			file.close();
			OperazioniFile.deleteFile("settings.dat");
		} catch (FileNotFoundException e) {}
	}
	private static void update_85_to_86_db(){
		if(GestioneSerieTV.Showlist()){
			
			FileReader file_reader;
			try {
				file_reader = new FileReader("st.dat");
			}
			catch (FileNotFoundException e) {
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
							if(t_s!=null){
								t_s.setSottotitolo(true, false);
								t_s.update();
							}
						}
						break;
					case 't':
						boolean visto = file.nextBoolean();
						String url_torrent = file.next();
						if(serie!=null){
							Torrent t=(Torrent)serie.getEpisodi().get(url_torrent);
							t.setScaricato(visto==true?Torrent.SCARICATO:Torrent.SCARICARE, false);
							t.update();
						}
						break;
					case 'e':
						if(serie!=null)
							serie.UpdateDB();
						break;
				}
			}
			file.close();
			OperazioniFile.deleteFile("st.dat");
		}
	}
}
