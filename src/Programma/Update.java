package Programma;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.SynchronousMode;

import Database.Database;
import Naming.CaratteristicheFile;
import SerieTV.SerieTV;
import StruttureDati.db.KVResult;

public class Update {
	public static void start() {
		if(Settings.isNewUpdate() || Settings.getVersioneSoftware()>Settings.getLastVersion()){
			switch(Settings.getLastVersion()){
				case 0:
				case 102:
					update_102_to_103();
				case 103: 
					update_103_to_104();
				case 107:
					update_107_to_108();
				case 110:
					update_110_to_111();
				default:
					Settings.setLastVersion(Settings.getVersioneSoftware());
					Settings.setNewUpdate(false);
					Settings.AggiornaDB();
			}
		}
	}
	private static void update_110_to_111(){
		Database.Disconnect();
		if(OperazioniFile.fileExists(Settings.getUserDir()+"database2.sqlite"))
			OperazioniFile.copyfile(Settings.getUserDir()+"database2.sqlite", Settings.getUserDir()+"database2.sqlite.bak");
		if(OperazioniFile.deleteFile(Settings.getUserDir()+"database2.sqlite")){
			System.out.println("database2.sqlite eliminato con successo");
		}
		if(OperazioniFile.copyfile(Settings.getCurrentDir()+"database2.sqlite", Settings.getUserDir()+"database2.sqlite")){
			System.out.println("database2.sqlite copiato con successo");
			Database.Connect();
		}
		else {
			JOptionPane.showMessageDialog(null, "Errore durante l'aggiornamento del database");
		}
	}
	private static void update_107_to_108(){
		Settings.setEnableITASA(true);
		String query="UPDATE "+Database.TABLE_SETTINGS+" SET itasa=1";
		Database.updateQuery(query);
	}
	private static void update_103_to_104() {
		String[] query={
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T1\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T2\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T3\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T4\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T5\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T6\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T7\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T8\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"T9\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp1\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp2\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp3\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp4\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp5\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp6\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp7\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp8\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temp9\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temporary_Placeholder\"",
				"DELETE FROM "+Database.TABLE_SERIETV+" WHERE nome=\"Temporary_Placeholder_2\""
			};
			for(int j=0;j<query.length;j++)
				Database.updateQuery(query[j]);
	}
	private static void update_102_to_103(){
		SQLiteConfig conf=new SQLiteConfig();
		conf.enableRecursiveTriggers(true);
		conf.enforceForeignKeys(true);
		conf.setSynchronous(SynchronousMode.OFF);
		
		String NOMEDB=Settings.getCurrentDir()+"database.db";
		if(OperazioniFile.fileExists(NOMEDB)){
			try {
				Connection con = DriverManager.getConnection("jdbc:sqlite:"+NOMEDB, conf.toProperties());
				
				String query_settings="SELECT * FROM settings";
				ArrayList<KVResult<String, Object>> settings=Database.selectQuery(con, query_settings);
				if(settings!=null){
					String dir_down=(String) settings.get(0).getValueByKey("dir_download");
					Settings.setDirectoryDownload(dir_down);
					String dir_utorr=(String) settings.get(0).getValueByKey("dir_client");
					Settings.setClientPath(dir_utorr);
					String dir_vlc=(String) settings.get(0).getValueByKey("dir_vlc");
					Settings.setVLCPath(dir_vlc);
					int tray_on_icon=(int) settings.get(0).getValueByKey("tray_on_icon");
					Settings.setTrayOnIcon(tray_on_icon==1?true:false);
					int starthidden=(int) settings.get(0).getValueByKey("start_hidden");
					Settings.setStartHidden(starthidden==1?true:false);
					int ask_close=(int) settings.get(0).getValueByKey("ask_on_close");
					Settings.setAskOnClose(ask_close==1?true:false);
					int alwaysontop=(int) settings.get(0).getValueByKey("always_on_top");
					Settings.setAlwaysOnTop(alwaysontop==1?true:false);
					int startwin=(int) settings.get(0).getValueByKey("start_win");
					Settings.setAutostart(startwin==1?true:false);
					int ricerca_auto=(int) settings.get(0).getValueByKey("ricerca_auto");
					Settings.setDownloadAutomatico(ricerca_auto==1?true:false);
					int min_ricerca=(int) settings.get(0).getValueByKey("min_ricerca");
					Settings.setMinRicerca(min_ricerca);
					int last_ver=(int) settings.get(0).getValueByKey("last_version");
					Settings.setLastVersion(last_ver);
					int ricerca_sub=(int) settings.get(0).getValueByKey("ricerca_sub");
					Settings.setRicercaSottotitoli(ricerca_sub==1?true:false);
					String useritasa=(String) settings.get(0).getValueByKey("itasa_id");
					Settings.setItasaUsername(useritasa);
					String passitasa=(String) settings.get(0).getValueByKey("itasa_pass");
					Settings.setItasaPassword(passitasa);
					Settings.AggiornaDB();
				}
				
				String query_serie="SELECT * FROM serie";
				ArrayList<KVResult<String, Object>> series=Database.selectQuery(con, query_serie);
				if(series!=null){
					for(int i=0;i<series.size();i++){
						KVResult<String, Object> r=series.get(i);
						int id_db=(int) r.getValueByKey("id");
						String nome=(String) r.getValueByKey("nome");
						String url=(String) r.getValueByKey("url");
						url=url.replace("/shows/", "");
						url=url.substring(0,url.indexOf("/"));
						int stato=(int) r.getValueByKey("stato");
						int inserita=(int) r.getValueByKey("inserita");
						String nome_formattato=SerieTV.formattaNome(nome);
						String query_insert_serie="INSERT INTO "+Database.TABLE_SERIETV +" (id,url, nome, inserita, conclusa, stop_search, provider) VALUES("+
								id_db+
								",\""+url+"\""+
								",\""+nome_formattato+"\""+
								","+inserita+
								","+stato+
								","+0+
								","+1+")";
						Database.updateQuery(query_insert_serie);
						
						if(nome.compareToIgnoreCase(nome_formattato)!=0){
							String base_dir_download=Settings.getDirectoryDownload();
							if(base_dir_download!=null && !base_dir_download.isEmpty()){
								File old_dir=new File(Settings.getDirectoryDownload()+File.separator+nome);
								if(old_dir.exists() && old_dir.isDirectory()){
									if(old_dir.renameTo(new File(Settings.getDirectoryDownload()+File.separator+nome_formattato))){
										System.out.println(old_dir+"- Cartella rinominata correttamente");
									}
									else
										System.out.println(old_dir+"- errore durante rinominazione in: "+nome_formattato);
								}	
							}
						}
						if(inserita==1){
	    					String query_episodi="SELECT * FROM torrent WHERE id_serie="+id_db;
	    					ArrayList<KVResult<String, Object>> episodes=Database.selectQuery(con, query_episodi);
	    					if(episodes!=null){
	    						for(int j=0;j<episodes.size();j++){
	    							KVResult<String, Object> rt=episodes.get(j);
	    							String magnet=(String) rt.getValueByKey("magnet");
	    							int vista=(int) rt.getValueByKey("vista");
	    							int stagione=(int) rt.getValueByKey("serie");
	    							int episodio=(int) rt.getValueByKey("episodio");
	    							int hd=(int) rt.getValueByKey("HD720p");
	    							int repack=(int) rt.getValueByKey("repack");
	    							int preair=(int) rt.getValueByKey("preair");
	    							int proper=(int) rt.getValueByKey("proper");
	    							int sottotitolo=(int) rt.getValueByKey("sottotitolo");
	    							
	    							String query_insert_episodio="INSERT INTO "+Database.TABLE_EPISODI+" (id_serie, url, vista, stagione, episodio, tags, preair, sottotitolo) VALUES ("+
	    								id_db+
	    								",\""+magnet+"\""+
	    								","+vista+
	    								","+stagione+
	    								","+episodio+
	    								","+CaratteristicheFile.valueFromStat(hd==1?true:false, repack==1?true:false, proper==1?true:false)+
	    								","+preair+
	    								","+sottotitolo+")";
	    							Database.updateQuery(query_insert_episodio);
	    						}
	    					}
						}
					}
				}
				//TODO cercare doppioni serie tv
				con.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
