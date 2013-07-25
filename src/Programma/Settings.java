package Programma;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Database.Database;
import Database.SQLParameter;
import SerieTV.GestioneSerieTV;

public class Settings {
	private static int 			GUI									= 2;
	private static final int	VersioneSoftware					= 99;
	private static final boolean beta								= false;
	private static final int	beta_versione						= 1;
	private static int			Client								= 1;
	public static final String	IndirizzoDonazioni					= "http://pinoelefante.altervista.org/donazioni/donazione_gst.html";
	private static final String	NomeEseguibile						= "GestioneSerieTV5.exe";
	private static String		current_dir							= "";
	private static String		DirectoryDownload					= "";
	private static String		ClientPath							= "";
	private static boolean		TrayOnIcon							= true;
	private static boolean		AskOnClose							= false;
	private static boolean		StartHidden							= false;
	private static boolean		Autostart							= true;
	private static boolean		DownloadAutomatico					= false;
	private static int			MinRicerca							= 480;
	private static int			MinRicercaMilli						= MinRicerca * 60 * 1000;
	private static String		SistemaOperativo					= "";
	private static int			Lingua								= 1;
	private static boolean		canStartDownloadAutomatico			= false;
	
	private static boolean		NewUpdate							= true;
	private static int			LastVersion							= 0;
	private static int			numero_avvii						= 0;
	private static boolean		RicercaSottotitoli					= true;
	private static boolean 		alwaysontop							= true;
	private static String		VLCPath								= "";
	private static String		Itasa_Username						= "";
	private static String		Itasa_Password						= "";
	private static boolean		mostraPreair						= true;
	private static boolean		mostra720p							= true;
	private static String		ClientID = "";
	private static boolean		downloadPreair						= false;
	private static boolean		download720p						= false;
	private static boolean 		lettore_nascondi_ignore				= false;
	private static boolean 		lettore_nascondi_rimosso			= false;
	private static int			last_check							= 0; //ultimo controllo dei file
	private static boolean		hidden_on_play						= true;
	
	public static int getVersioneSoftware() {
		return VersioneSoftware;
	}
	public static int getClient() {
		return Client;
	}
	public static void setClient(int client) {
		Client = client;
		AggiornaDB();
	}
	public static String getNomeeseguibile() {
		return NomeEseguibile;
	}
	public static String getCurrentDir() {
		return current_dir;
	}
	public static void setCurrentDir(String current_dir) {
		Settings.current_dir = current_dir;
		AggiornaDB();
	}
	public static String getClientPath() {
		return ClientPath;
	}
	public static void setClientPath(String clientPath) {
		ClientPath = clientPath;
		AggiornaDB();
	}
	public static String getDirectoryDownload() {
		return DirectoryDownload+(DirectoryDownload.endsWith(File.separator)?"":File.separator);
	}
	public static void setDirectoryDownload(String directoryDownload) {	
		File f=new File(directoryDownload);
		if(!f.exists())
			f.mkdirs();
		DirectoryDownload = directoryDownload;
		AggiornaDB();
	}
	public static boolean isTrayOnIcon() {
		return TrayOnIcon;
	}
	public static void setTrayOnIcon(boolean trayOnIcon) {
		TrayOnIcon = trayOnIcon;
		AggiornaDB();
	}
	public static boolean isAskOnClose() {
		return AskOnClose;
	}
	public static void setAskOnClose(boolean askOnClose) {
		AskOnClose = askOnClose;
		AggiornaDB();
	}
	public static boolean isStartHidden() {
		return StartHidden;
	}
	public static void setStartHidden(boolean startHidden) {
		StartHidden = startHidden;
		AggiornaDB();
	}
	public static boolean isAutostart() {
		return Autostart;
	}
	public static boolean setAutostart(boolean autostart) {
		Autostart = autostart;
		
		if(isAutostart())
			createAutoStart();
		else
			removeAutostart();
		AggiornaDB();
		return autostart;
	}
	public static boolean isDownloadAutomatico() {
		return DownloadAutomatico;
	}
	public static void setDownloadAutomatico(boolean downloadAutomatico) {
		DownloadAutomatico = downloadAutomatico;
		AggiornaDB();
	}
	public static String getSistemaOperativo() {
		return SistemaOperativo;
	}
	public static void setSistemaOperativo(String sistemaOperativo) {
		SistemaOperativo = sistemaOperativo;
		AggiornaDB();
	}
	public static int getLingua() {
		return Lingua;
	}
	public static void setLingua(int lingua) {
		Lingua = lingua;
		AggiornaDB();
	}
	public static boolean isCanStartDownloadAutomatico() {
		return canStartDownloadAutomatico;
	}
	public static void setCanStartDownloadAutomatico(boolean canStartDownloadAutomatico) {
		Settings.canStartDownloadAutomatico = canStartDownloadAutomatico;
		AggiornaDB();
	}
	public static boolean isNewUpdate() {
		return NewUpdate;
	}
	public static void setNewUpdate(boolean newUpdate) {
		NewUpdate = newUpdate;
		AggiornaDB();
	}
	public static int getLastVersion() {
		return LastVersion;
	}
	public static void setLastVersion(int lastVersion) {
		LastVersion = lastVersion;
		AggiornaDB();
	}
	public static int getNumeroAvvii() {
		return numero_avvii;
	}
	public static void setNumeroAvvii(int numero_avvii) {
		Settings.numero_avvii = numero_avvii;
		AggiornaDB();
	}
	public static boolean isRicercaSottotitoli() {
		return RicercaSottotitoli;
	}
	public static void setRicercaSottotitoli(boolean ricercaSottotitoli) {
		RicercaSottotitoli = ricercaSottotitoli;
		AggiornaDB();
	}
	public static boolean isAlwaysOnTop() {
		return alwaysontop;
	}
	public static void setAlwaysOnTop(boolean alwaysontop) {
		Settings.alwaysontop = alwaysontop;
		AggiornaDB();
	}
	public static String getVLCPath() {
		return VLCPath;
	}
	public static void setVLCPath(String vLCPath) {
		VLCPath = vLCPath;
		AggiornaDB();
	}
	public static String getItasaUsername() {
		return Itasa_Username;
	}
	public static void setItasaUsername(String itasa_Username) {
		Itasa_Username = itasa_Username;
		AggiornaDB();
	}
	public static String getItasaPassword() {
		return Itasa_Password;
	}
	public static void setItasaPassword(String itasa_Password) {
		Itasa_Password = itasa_Password;
		AggiornaDB();
	}
	public static boolean isMostraPreair() {
		return mostraPreair;
	}
	public static void setMostraPreair(boolean mostraPreair) {
		Settings.mostraPreair = mostraPreair;
		AggiornaDB();
	}
	public static boolean isMostra720p() {
		return mostra720p;
	}
	public static void setMostra720p(boolean mostra720p) {
		Settings.mostra720p = mostra720p;
		AggiornaDB();
	}
	public static int getMinRicerca() {
		return MinRicerca;
	}
	public static void setMinRicerca(int minRicerca) {
		MinRicerca = minRicerca;
		aggiornaMinRicercaMilli();
		AggiornaDB();
	}
	public static void aggiornaMinRicercaMilli() {
		setMinRicercaMilli(MinRicerca * 60 * 1000);
	}
	public static void setClientID(String id){
		ClientID=id;
		AggiornaDB();
	}
	public static String getClientID(){
		return ClientID;
	}
	public static int getMinRicercaMilli() {
		return MinRicercaMilli;
	}
	public static void setMinRicercaMilli(int minRicercaMilli) {
		MinRicercaMilli = minRicercaMilli;
	}
	
	public static void setDefault() {
		setTrayOnIcon(true);
		setAskOnClose(false);
		setStartHidden(false);
		setAutostart(true);
		setDownloadAutomatico(false);
		setMinRicerca(480);
		setRicercaSottotitoli(true);
		setAlwaysOnTop(true);
		setLingua(1);
		setMostraPreair(true);
		setMostra720p(true);
		setDownloadPreair(false);
		setDownload720p(false);
		setHiddenOnPlay(true);
	}

	public static void baseSettings(){
		SistemaOperativo = System.getProperty("os.name");
		current_dir = ClassLoader.getSystemClassLoader().getResource(".").getPath();
		if(isWindows()){
			current_dir = current_dir.substring(1).replace("/", "\\").replace("%20", " ");
			try {
				if(!OperazioniFile.fileExists("autostart.exe"))
					Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/autostart.exe", "autostart.exe");
				registraProgramma();
			}
			catch (IOException e) {
				e.printStackTrace();
				ManagerException.registraEccezione(e);
			}
		}
		DirectoryDownload=current_dir+"Download";
	}
	public static void CaricaSetting(){
		ArrayList<SQLParameter[]> res=Database.select(Database.TABLE_SETTINGS, null, "AND", "==");
		for (int i=0;i<res.size();i++){
			SQLParameter[] par=res.get(i);
			for(int j=0;j<par.length;j++){
				switch(par[j].ptype()){
					case SQLParameter.INTEGER:
						int p=(Integer)par[j].pvalue();
						switch(par[j].getField()){
							case "tray_on_icon" : setTrayOnIcon(p==0?false:true); break;
							case "start_hidden" : setStartHidden(p==0?false:true); break;
							case "ask_on_close" : setAskOnClose(p==0?false:true); break;
							case "always_on_top" : setAlwaysOnTop(p==0?false:true); break;
							case "start_win" : setAutostart(p==0?false:true); break;
							case "ricerca_auto" : setDownloadAutomatico(p==0?false:true); break;
							case "min_ricerca" : setMinRicerca(p); break;
							case "lingua" : setLingua(p); break;
							case "new_update" : setNewUpdate(p==0?false:true); break;
							case "last_version" : setLastVersion(p); break;
							case "numero_avvii" : setNumeroAvvii(p+1); break;
							case "ricerca_sub" : setRicercaSottotitoli(p==0?false:true); break;
							case "mostra_preair" : setMostraPreair(p==0?false:true); break;
							case "mostra_720p" : setMostra720p(p==0?false:true); break;
							case "download_preair": setDownloadPreair(p==0?false:true); break;
							case "download_720p": setDownload720p(p==0?false:true); break;
							case "check_episodi": 
								if(p==0)
									GestioneSerieTV.controlloStatoEpisodi();
								setLastCheckFiles(p+1); 
								break;
							case "hidden_on_play": setHiddenOnPlay(p==0?false:true); break;
						}
						break;
					case SQLParameter.TEXT:
						String s=(String)par[j].pvalue();
						switch(par[j].getField()){
							case "dir_download" :
								if(s.isEmpty() || !(new File(s)).exists()){
									setDirectoryDownload(getCurrentDir()+"Download"+File.separator);
									new File(getCurrentDir()+"Download"+File.separator).mkdir();
								}
								else{
									if(!s.endsWith(File.separator))
										s+=File.separator;
									setDirectoryDownload(s);
								}
								break;
							case "dir_client" :
								if (s.trim().isEmpty() || !OperazioniFile.fileExists(s)){
									s=rilevaUtorrent();
								}
								setClientPath(s); 
								break;
							case "dir_vlc" : 
								if(s.trim().isEmpty() || !OperazioniFile.fileExists(s)){
									s=rilevaVLC();
								}
								setVLCPath(s); 
								break;
							case "itasa_id" : setItasaUsername(s); break;
							case "itasa_pass" : setItasaPassword(s); break;
							case "client_id" : setClientID(s); break;
						}
						break;
				}
			}
		}
		AggiornaDB();
	}

	public static void createAutoStart() {
		try {
			Runtime.getRuntime().exec("autostart.exe 1 \"" + current_dir.substring(0, current_dir.length() - 1) + "\"");
		}
		catch (IOException e) {
			ManagerException.registraEccezione(e);
		}
	}
	public static void removeAutostart() {
		try {
			Runtime.getRuntime().exec("autostart.exe 2");
		}
		catch (IOException e) {
			ManagerException.registraEccezione(e);
		}
	}

	public static void registraProgramma() {
		try {
			Runtime.getRuntime().exec("autostart.exe 3 \"" + current_dir.substring(0, current_dir.length() - 1) + "\"" + " " + "GestioneSerieTV5.exe");
		}
		catch (IOException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	public static boolean verificaUtorrent(){
		String nome_client=ClientPath.substring(ClientPath.lastIndexOf(File.separator)+1);
		if(nome_client.compareToIgnoreCase("utorrent.exe")!=0)
			return false;
		return true;
	}
	private static void AggiornaDB() {
		SQLParameter[] par=new SQLParameter[22];
		int i=0;
		par[i++]=new SQLParameter(SQLParameter.TEXT, getDirectoryDownload(), "dir_download");
		par[i++]=new SQLParameter(SQLParameter.TEXT, getClientPath(), "dir_client");
		par[i++]=new SQLParameter(SQLParameter.TEXT, getVLCPath(), "dir_vlc");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isTrayOnIcon()?1:0, "tray_on_icon");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isStartHidden()?1:0, "start_hidden");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isAskOnClose()?1:0, "ask_on_close");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isAlwaysOnTop()?1:0, "always_on_top");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isAutostart()?1:0, "start_win");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isDownloadAutomatico()?1:0, "ricerca_auto");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getMinRicerca(), "min_ricerca");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getLingua(), "lingua");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isNewUpdate()?1:0, "new_update");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getLastVersion(), "last_version");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getNumeroAvvii(), "numero_avvii");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isRicercaSottotitoli()?1:0, "ricerca_sub");
		par[i++]=new SQLParameter(SQLParameter.TEXT, getItasaUsername(), "itasa_id");
		par[i++]=new SQLParameter(SQLParameter.TEXT, getItasaPassword(), "itasa_pass");
		par[i++]=new SQLParameter(SQLParameter.TEXT, getClientID(), "client_id");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isMostraPreair()?1:0, "mostra_preair");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isMostra720p()?1:0, "mostra_720p");
		if(getVersioneSoftware()>=92){
			par[i++]=new SQLParameter(SQLParameter.INTEGER, getLastCheckFiles(), "check_episodi");
			par[i++]=new SQLParameter(SQLParameter.INTEGER, getLastCheckFiles(), "hidden_on_play");
		}
		Database.update(Database.TABLE_SETTINGS, par, null, "AND", "=");
	}
	public static boolean isWindows(){
		return getSistemaOperativo().contains("Windows");
	}
	public static boolean isLinux(){
		return getSistemaOperativo().contains("Linux");
	}
	public static boolean isMacOS(){
		return getSistemaOperativo().contains("Mac");
	}
	public static boolean isDownloadPreair() {
		return downloadPreair;
	}
	public static void setDownloadPreair(boolean downloadPreair) {
		Settings.downloadPreair = downloadPreair;
		AggiornaDB();
	}
	public static boolean isDownload720p() {
		return download720p;
	}
	public static void setDownload720p(boolean download720p) {
		Settings.download720p = download720p;
		AggiornaDB();
	}
	public static boolean isBeta() {
		return beta;
	}
	public static int getBetaVersione() {
		return beta_versione;
	}
	//TODO completare rilevamento vlc
	public static String rilevaVLC(){
		if(isWindows()){
			
		}
		else if(isLinux()){
			if(OperazioniFile.fileExists("/usr/bin/vlc"))
				return "/usr/bin/vlc";
		}
		else if(isMacOS()){
			
		}
		return "";
	}
	public static boolean isLettoreNascondiIgnore() {
		return lettore_nascondi_ignore;
	}
	public static void setLettoreNascondiIgnore(boolean lettore_nascondi_ignore) {
		Settings.lettore_nascondi_ignore = lettore_nascondi_ignore;
	}
	public static boolean isLettoreNascondiRimosso() {
		return lettore_nascondi_rimosso;
	}
	public static void setLettoreNascondiRimosso(boolean lettore_nascondi_rimosso) {
		Settings.lettore_nascondi_rimosso = lettore_nascondi_rimosso;
	}
	//TODO completare rilevamento client utorrent
	public static String rilevaUtorrent(){
		if(isWindows()){
			if(OperazioniFile.fileExists(getCurrentDir()+"utorrent.exe")){
				return getCurrentDir()+"utorrent.exe";
			}
		}
		else if(isMacOS()){
			if(OperazioniFile.fileExists("/Applications/uTorrent.app/Contents/MacOS/uTorrent"))
				return "/Applications/uTorrent.app/Contents/MacOS/uTorrent";
		}
		else if(isLinux()){
			
		}
		return "";
	}
	public static int getLastCheckFiles() {
		return last_check;
	}
	public static void setLastCheckFiles(int last_check) {
		Settings.last_check = last_check==10?0:last_check;
		AggiornaDB();
	}
	public static boolean isHiddenOnPlay() {
		return hidden_on_play;
	}
	public static void setHiddenOnPlay(boolean hidden_on_play) {
		Settings.hidden_on_play = hidden_on_play;
		AggiornaDB();
	}
	public static boolean isVLC(){
		String path=getVLCPath();
		if(path.isEmpty())
			return false;
		
		if(isWindows()){
			return path.toLowerCase().endsWith("vlc.exe");
		}
		else if(isLinux())
			return path.toLowerCase().endsWith("vlc");
		else
			return true;
	}
	public static int getGUI() {
		return GUI;
	}
	public static void setGUI(int gUI) {
		GUI = gUI;
	}
	public static boolean is32bit(){
		String arch_vm = System.getProperty("os.arch");
		boolean x86 = arch_vm.contains("x86")||arch_vm.contains("i386");
		return x86;
	}
	public static boolean is64bit(){
		return !is32bit();
	}
	public static String getOSName(){
		String name="";
		
		if(isWindows())
			name="win32";
		else if(isLinux())
			name="linux";
		else if(isMacOS())
			name="macos";
		
		return name;
	}
	public static String getVMArch(){
		if(is32bit())
			return "i386";
		else
			return "amd64";
	}
}
