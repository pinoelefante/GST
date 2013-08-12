package Programma;

import java.io.File;
import java.util.ArrayList;

import Database.Database2;
import StruttureDati.KVResult;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public class Settings {
	private static int 			GUI									= 2;
	private static final int	VersioneSoftware					= 100;
	public static final String	IndirizzoDonazioni					= "http://pinoelefante.altervista.org/donazioni/donazione_gst.html";
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
	private static boolean		canStartDownloadAutomatico			= false;
	
	private static boolean		NewUpdate							= true;
	private static int			LastVersion							= 0;
	private static boolean		RicercaSottotitoli					= true;
	private static boolean 		alwaysontop							= true;
	private static String		VLCPath								= "";
	private static String		Itasa_Username						= "";
	private static String		Itasa_Password						= "";
	private static boolean		mostraPreair						= true;
	private static boolean		mostra720p							= true;
	private static String		ClientID 							= "";
	private static boolean		download_auto_preair				= false;
	private static boolean		download_auto_hd					= false;
	private static boolean 		lettore_nascondi_ignore				= true;
	private static boolean 		lettore_nascondi_rimosso			= true;
	
	private static boolean 		extenal_VLC							= false;
	private static boolean 		enableITASA							= false;
	private static boolean 		lettore_nascondi_viste				= true;
	private static int			lettore_ordine						= 0;
	
	
	
	public static int getVersioneSoftware() {
		return VersioneSoftware;
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
		setMostraPreair(true);
		setMostra720p(true);
		setDownloadPreair(false);
		setDownload720p(false);
	}

	public static void baseSettings(){
		SistemaOperativo = System.getProperty("os.name");
		current_dir = ClassLoader.getSystemClassLoader().getResource(".").getPath();
		if(current_dir.startsWith("/")){
			current_dir=current_dir.substring(1).replace("%20", " ").replace("\\", File.separator).replace("/", File.separator);
		}
		System.setProperty("user.dir", current_dir);
		
		DirectoryDownload=current_dir+"Download";
	}
	public static void main(String[] args){
		
	}
	/* Tabelle database
    "download_path"
    "utorrent"
	"vlc"
    "itasa_user"
    "itasa_pass"
    "client_id"
    "tray_on_icon"
    "start_hidden"
    "ask_on_close"
    "always_on_top"
    "autostart"
    "download_auto"
    "min_download_auto"
    "new_update"
    "last_version"
    "download_sottotitoli"
    "mostra_preair"
    "mostra_hd"
    "download_auto_preair"
    "download_auto_hd"
    "external_vlc"
    "itasa"
    "hide_viste"
    "hide_ignorate"
    "hide_rimosse"
    "ordine_lettore"
    */
	public static void CaricaSetting(){
		String query="SELECT * FROM "+Database2.TABLE_SETTINGS;
		ArrayList<KVResult<String, Object>> opzioni=Database2.selectQuery(query);
		for(int i=0;i<opzioni.size();i++){
			KVResult<String, Object> res=opzioni.get(i);
			
			setDirectoryDownload(res.getValueByKey("download_path")!=null?(String)res.getValueByKey("download_path"):DirectoryDownload);
			setClientPath((String) res.getValueByKey("utorrent"));
			setVLCPath((String) res.getValueByKey("vlc"));
			setItasaUsername((String) res.getValueByKey("itasa_user"));
			setItasaPassword((String) res.getValueByKey("itasa_pass"));
			setClientID((String) res.getValueByKey("client_id"));
			setTrayOnIcon(((int) res.getValueByKey("tray_on_icon"))==1?true:false);
			setStartHidden(((int) res.getValueByKey("start_hidden"))==1?true:false);
			setAskOnClose(((int) res.getValueByKey("ask_on_close"))==1?true:false);
			setAlwaysOnTop(((int) res.getValueByKey("always_on_top"))==1?true:false);
            setAutostart(((int) res.getValueByKey("autostart"))==1?true:false);
            setDownloadAutomatico(((int) res.getValueByKey("download_auto"))==1?true:false);
            setMinRicerca((int) res.getValueByKey("min_download_auto"));
            setNewUpdate(((int) res.getValueByKey("new_update"))==1?true:false);
            setLastVersion((int) res.getValueByKey("last_version"));
            setRicercaSottotitoli(((int) res.getValueByKey("download_sottotitoli"))==1?true:false);
            setMostraPreair(((int) res.getValueByKey("mostra_preair"))==1?true:false);
            setMostra720p(((int) res.getValueByKey("mostra_hd"))==1?true:false);
            setDownloadPreair(((int) res.getValueByKey("download_auto_preair"))==1?true:false);
            setDownload720p(((int) res.getValueByKey("download_auto_hd"))==1?true:false);
            setExtenalVLC(((int) res.getValueByKey("external_vlc"))==1?true:false);
            setEnableITASA(((int) res.getValueByKey("itasa"))==1?true:false);
            setLettoreNascondiViste(((int) res.getValueByKey("hide_viste"))==1?true:false);
            setLettoreNascondiIgnore(((int) res.getValueByKey("hide_ignorate"))==1?true:false);
            setLettoreNascondiRimosso(((int) res.getValueByKey("hide_rimosse"))==1?true:false);
            setLettoreOrdine((int) res.getValueByKey("ordine_lettore"));
		}
		
	}

	public static void createAutoStart() {
		if(isWindows()){
			Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "GestioneSerieTV", getCurrentDir()+"GestioneSerieTV5.exe");
		}
		else if(isLinux()){
			
		}
		else if(isMacOS()){
			
		}
	}
	public static void removeAutostart() {
		if(isWindows()){
			Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Run", "GestioneSerieTV");
		}
		else if(isLinux()){
			
		}
		else if(isMacOS()){
			
		}
	}
	public static boolean verificaUtorrent(){
		String nome_client=ClientPath.substring(ClientPath.lastIndexOf(File.separator)+1);
		if(nome_client.compareToIgnoreCase("utorrent.exe")!=0)
			return false;
		return true;
	}
	private static void AggiornaDB() {
		String query="UPDATE "+Database2.TABLE_SETTINGS+" SET "+
				"always_on_top="+(isAlwaysOnTop()?1:0)+","+
				"download_path="+"\""+getDirectoryDownload()+"\","+
                "utorrent="+"\""+getClientPath()+"\","+
				"vlc="+"\""+getVLCPath()+"\","+
                "itasa_user="+"\""+getItasaUsername()+"\","+
                "itasa_pass="+"\""+getItasaPassword()+"\","+
                "client_id="+"\""+getClientID()+"\","+
                "tray_on_icon="+(isTrayOnIcon()?1:0)+","+
                "start_hidden="+(isStartHidden()?1:0)+","+
                "ask_on_close="+(isAskOnClose()?1:0)+","+
                "always_on_top="+(isAlwaysOnTop()?1:0)+","+
                "autostart="+(isAutostart()?1:0)+","+
                "download_auto="+(isDownloadAutomatico()?1:0)+","+
                "min_download_auto="+getMinRicerca()+","+
                "new_update="+(isNewUpdate()?1:0)+","+
                "last_version="+getLastVersion()+","+
                "download_sottotitoli="+(isRicercaSottotitoli()?1:0)+","+
                "mostra_preair="+(isMostraPreair()?1:0)+","+
                "mostra_hd="+(isMostra720p()?1:0)+","+
                "download_auto_preair="+(isDownloadPreair()?1:0)+","+
                "download_auto_hd="+(isDownload720p()?1:0)+","+
                "external_vlc="+(isExtenalVLC()?1:0)+","+
                "itasa="+(isEnableITASA()?1:0)+","+
                "hide_viste="+(isLettoreNascondiViste()?1:0)+","+
                "hide_ignorate="+(isLettoreNascondiIgnore()?1:0)+","+
                "hide_rimosse="+(isLettoreNascondiRimosso()?1:0)+","+
                "ordine_lettore="+getLettoreOrdine();
		Database2.updateQuery(query);
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
		return download_auto_preair;
	}
	public static void setDownloadPreair(boolean downloadPreair) {
		Settings.download_auto_preair = downloadPreair;
		AggiornaDB();
	}
	public static boolean isDownload720p() {
		return download_auto_hd;
	}
	public static void setDownload720p(boolean download720p) {
		Settings.download_auto_hd = download720p;
		AggiornaDB();
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


	public static boolean isExtenalVLC() {
		return extenal_VLC;
	}


	public static void setExtenalVLC(boolean extenal_VLC) {
		Settings.extenal_VLC = extenal_VLC;
		AggiornaDB();
	}


	public static boolean isEnableITASA() {
		return enableITASA;
	}


	public static void setEnableITASA(boolean enableITASA) {
		Settings.enableITASA = enableITASA;
		AggiornaDB();
	}


	public static boolean isLettoreNascondiViste() {
		return lettore_nascondi_viste;
	}


	public static void setLettoreNascondiViste(boolean lettore_nascondi_viste) {
		Settings.lettore_nascondi_viste = lettore_nascondi_viste;
		AggiornaDB();
	}


	public static int getLettoreOrdine() {
		return lettore_ordine;
	}


	public static void setLettoreOrdine(int lettore_ordine) {
		Settings.lettore_ordine = lettore_ordine;
		AggiornaDB();
	}
}
