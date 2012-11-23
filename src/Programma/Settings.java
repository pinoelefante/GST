package Programma;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Database.Database;
import Database.SQLParameter;

public class Settings {
	private static final int	VersioneSoftware				= 86;
	private static int			Client								= 1;
//	public static final String	IndirizzoDonazioni					= "http://pinoelefante.altervista.org/donazioni/donazione_gst.html";
	private static final String	NomeEseguibile						= "GestioneSerieTV5.exe";
	private static String		current_dir							= "";
	private static String		DirectoryDownload;
	private static String		ClientPath							= "";
	private static boolean		TrayOnIcon							= true;
	private static boolean		AskOnClose							= false;
	private static boolean		StartHidden							= false;
	private static boolean		Autostart							= true;
	private static boolean		DownloadAutomatico					= false;
	private static int			MinRicerca							= 480;
	private static int			MinRicercaMilli						= MinRicerca * 60 * 1000;
	private static String		SistemaOperativo;
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
	private static boolean		ItasaThread_DownloadOrNotifica		= true;
	private static boolean		mostraPreair						= true;
	private static boolean		mostra720p							= true;
	private static String		ClientID = "";
	private static boolean		downloadPreair						= false;
	private static boolean		download720p						= false;
	
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
		return DirectoryDownload;
	}
	public static void setDirectoryDownload(String directoryDownload) {
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
	public static boolean isItasaThreadAutoDownload() {
		return ItasaThread_DownloadOrNotifica;
	}
	public static void setItasaThreadAutoDownload(boolean itasa_ThreadAutoDownload) {
		ItasaThread_DownloadOrNotifica = itasa_ThreadAutoDownload;
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
	
	//TODO completare
	public static void setDefault() {
		TrayOnIcon				= true;
		AskOnClose				= false;
		StartHidden				= false;
		Autostart				= true;
		DownloadAutomatico		= false;
		MinRicerca				= 480;
		aggiornaMinRicercaMilli();
		RicercaSottotitoli		= true;
		alwaysontop				= true;
	}

	public static void baseSettings(){
		SistemaOperativo = System.getProperty("os.name");
		current_dir = ClassLoader.getSystemClassLoader().getResource(".").getPath();
		if(SistemaOperativo.contains("Windows")){
			current_dir = current_dir.substring(1).replace("/", "\\").replace("%20", " ");
			try {
				Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/autostart.exe", "autostart.exe");
				registraProgramma();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!OperazioniFile.fileExists(Database.getNomeDB())){
			//TODO avvio frame configurazione
		}
	}
	public static void CaricaSetting(){
		/*
		"dir_download TEXT DEFAULT ''," +
		"dir_client TEXT DEFAULT ''," +
		"dir_vlc TEXT DEFAULT ''," +
		"tray_on_icon INTEGER DEFAULT 1," +
		"start_hidden INTEGER DEFAULT 0," +
		"ask_on_close INTEGER DEFAULT 1," +
		"always_on_top INTEGER DEFAULT 0," +
		"start_win INTEGER DEFAULT 1," +
		"ricerca_auto INTEGER DEFAULT 0," +
		"min_ricerca INTEGER DEFAULT 720," +
		"lingua INTEGER DEFAULT 0," +
		"new_update INTEGER DEFAULT 1," +
		"last_version INTEGER DEFAULT 0," +
		"numero_avvii INTEGER DEFAULT 1," +
		"ricerca_sub INTEGER DEFAULT 1," +
		"itasa_id TEXT DEFAULT ''," +
		"itasa_pass TEXT DEFAULT ''," +
		"client_id TEXT DEFAULT ''," +
		"mostra_preair INTEGER DEFAULT 1," +
		"mostra_720p INTEGER DEFAULT 1" +
		*/
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
						}
						break;
					case SQLParameter.TEXT:
						String s=(String)par[j].pvalue();
						switch(par[j].getField()){
							case "dir_download" :
								if(s.isEmpty())
									setDirectoryDownload(getCurrentDir()+"Download"+File.separator);
								else{
									if(!s.endsWith(File.separator))
										s+=File.separator;
									setDirectoryDownload(s);
								}
								break;
							case "dir_client" : setClientPath(s); break;
							case "dir_vlc" : setVLCPath(s); break;
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
	/*
	public static void CaricaSetting() {
		if (SistemaOperativo.contains("Windows")) {
			try {
				Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/autostart.exe", "autostart.exe");
				registraProgramma();
				if (Autostart) {
					removeAutostarterOLD();
					createAutoStart();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		DirectoryDownload = current_dir + "Download\\";
		FileReader file_r = null;
		try {
			file_r = new FileReader("settings.dat");
			
			Scanner file = new Scanner(file_r);
			while(file.hasNextLine()){
				String linea=file.nextLine().trim();
				if(!linea.contains("="))
					continue;
				String tipo_opzione=linea.substring(0, linea.indexOf("=")).trim();
				String opzione=linea.substring(linea.indexOf("=")+1).trim();
				if(opzione==null || opzione.isEmpty())
					continue;
				
				try{
					switch(tipo_opzione){
						case "client_path":
							ClientPath=opzione;
							break;
						case "directory":
							DirectoryDownload=opzione;
							break;
						case "tray_on_icon":
							TrayOnIcon=Boolean.parseBoolean(opzione);
							break;
						case "ask_on_close":
							AskOnClose=Boolean.parseBoolean(opzione);
							break;
						case "start_hidden":
							StartHidden=Boolean.parseBoolean(opzione);
							break;
						case "auto_search":
							DownloadAutomatico=Boolean.parseBoolean(opzione);
							break;
						case "auto_search_between":
							MinRicerca=Integer.parseInt(opzione);
							aggiornaMinRicercaMilli();
							break;
						case "autostart":
							if((Autostart=Boolean.parseBoolean(opzione)))
								createAutoStart();
							else 
								removeAutostart();
							break;
						case "language":
							Lingua=Integer.parseInt(opzione);
								break;
						case "update":
							NewUpdate=Boolean.parseBoolean(opzione);
							break;
						case "last_version":
							LastVersion=Integer.parseInt(opzione);
							break;
						case "n_starts":
							numero_avvii=Integer.parseInt(opzione)+1;
							break;
						case "itasa_active":
							RicercaSottotitoli=Boolean.parseBoolean(opzione);
							break;
						case "always_on_top":
							alwaysontop=Boolean.parseBoolean(opzione);
							break;
						case "VLCPath":
							VLCPath=opzione;
							break;
						case "itasa_user":
							Itasa_Username=opzione;
							break;
						case "itasa_psw":
							Itasa_Password=opzione;
							break;
						case "itasa_auto":
							Itasa_ThreadAutoDownload=Boolean.parseBoolean(opzione);
							break;
					}
				}
				catch(NumberFormatException e){
				}
			}
			file.close();
		}
		catch (FileNotFoundException e) {
			
		}
	}
	*/
	/*
	@SuppressWarnings("unused")
	public static void oldCaricaSetting() {
		SistemaOperativo = System.getProperty("os.name");
		current_dir = ClassLoader.getSystemClassLoader().getResource(".").getPath();
		current_dir = current_dir.substring(1).replace("/", "\\").replace("%20", " ");
		

		if (SistemaOperativo.contains("Windows")) {
			try {
				Download.downloadFromUrl("http://pinoelefante.altervista.org/software/GST/autostart.exe", "autostart.exe");
				registraProgramma();
				if (Autostart) {
					removeAutostarterOLD();
					createAutoStart();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		DirectoryDownload = current_dir + "Download\\";
		FileReader file_r = null;
		try {
			file_r = new FileReader("settings.dat");
		}
		catch (FileNotFoundException e) {
			File f = new File("utorrent.exe");
			if (f.isFile()) {
				ClientPath = "utorrent.exe";
				Client = 1;
			}
			else {
				f = new File("Azureus.exe");
				if (f.isFile()) {
					ClientPath = "Azureus.exe";
					Client = 2;
				}
			}
			return;
		}
		Scanner file = new Scanner(file_r);
		try {
			Client = Integer.parseInt(file.nextLine().trim());
			ClientPath = file.nextLine().trim();
			DirectoryDownload = file.nextLine().trim();
			TrayOnIcon = Boolean.parseBoolean(file.nextLine().trim());
			TrayOnIcon = false;
			AskOnClose = Boolean.parseBoolean(file.nextLine().trim());
			StartHidden = Boolean.parseBoolean(file.nextLine().trim());
			DownloadAutomatico = Boolean.parseBoolean(file.nextLine().trim());
			MinRicerca = Integer.parseInt(file.nextLine().trim());
			Autostart = Boolean.parseBoolean(file.nextLine().trim());
			Lingua = Integer.parseInt(file.nextLine().trim());
			boolean CercaAutomaticamentePanelDownload = Boolean.parseBoolean(file.nextLine().trim());
			CercaAutomaticamentePanelDownload = true;
			int LibraryVersion = Integer.parseInt(file.nextLine().trim());
			LastIP_Alter=file.nextLine().trim();
			LastClickTime_Alter=Long.parseLong(file.nextLine().trim());
			String LastIP_Adsense=file.nextLine().trim();
			long LastClickTime_Adsense=Long.parseLong(file.nextLine().trim());
			boolean ParseNomeTorrent=Boolean.parseBoolean(file.nextLine().trim());
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Si � verificato un errore nel caricamento delle impostazioni.\nQuesto pu� succedere dopo un aggiornamento.\nControllare la tab 'Opzioni'");
			e.printStackTrace();
			return;
		}
		catch (NoSuchElementException localNoSuchElementException) {
		}
		finally {
			if (Autostart)
				createAutoStart();
			else {
				removeAutostart();
			}
			aggiornaMinRicercaMilli();
			file.close();
		}
	}
	*/

	public static void createAutoStart() {
		try {
			Runtime.getRuntime().exec("autostart.exe 1 \"" + current_dir.substring(0, current_dir.length() - 1) + "\"");
		}
		catch (IOException localIOException) {
		}
	}
	public static void removeAutostart() {
		try {
			Runtime.getRuntime().exec("autostart.exe 2");
		}
		catch (IOException localIOException) {
		}
	}

	public static void registraProgramma() {
		try {
			Runtime.getRuntime().exec("autostart.exe 3 \"" + current_dir.substring(0, current_dir.length() - 1) + "\"" + " " + "GestioneSerieTV5.exe");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean verificaUtorrent(){
		String nome_client=ClientPath.substring(ClientPath.lastIndexOf(File.separator)+1);
		if(nome_client.compareToIgnoreCase("utorrent.exe")!=0)
			return false;
		return true;
	}
	private static void AggiornaDB() {
		/*
		"dir_download TEXT DEFAULT ''," +
		"dir_client TEXT DEFAULT ''," +
		"dir_vlc TEXT DEFAULT ''," +
		"tray_on_icon INTEGER DEFAULT 1," +
		"start_hidden INTEGER DEFAULT 0," +
		"ask_on_close INTEGER DEFAULT 1," +
		"always_on_top INTEGER DEFAULT 0," +
		"start_win INTEGER DEFAULT 1," +
		"ricerca_auto INTEGER DEFAULT 0," +
		"min_ricerca INTEGER DEFAULT 720," +
		"lingua INTEGER DEFAULT 0," +
		"new_update INTEGER DEFAULT 1," +
		"last_version INTEGER DEFAULT 0," +
		"numero_avvii INTEGER DEFAULT 1," +
		"ricerca_sub INTEGER DEFAULT 1," +
		"itasa_id TEXT DEFAULT ''," +
		"itasa_pass TEXT DEFAULT ''," +
		"client_id TEXT DEFAULT ''," +
		"mostra_preair INTEGER DEFAULT 1," +
		"mostra_720p INTEGER DEFAULT 1" +
		*/
		SQLParameter[] par=new SQLParameter[20];
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
}
