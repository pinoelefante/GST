package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.SynchronousMode;

import Programma.ManagerException;
import Programma.Settings;

public class Database2 {
	private static Connection con;
	
	public final static String TABLE_SERIETV="serietv";
	public final static String TABLE_EPISODI="episodi";
	public final static String TABLE_ITASA="itasa";
	public final static String TABLE_SUBSFACTORY="subsfactory";
	public final static String TABLE_LOGSUB="logsub";
	public final static String TABLE_LINGUE="lingue";
	public final static String TABLE_TRADUZIONI="traduzioni";
	public final static String TABLE_SETTINGS="settings";
	public final static String TABLE_TVRAGE="tvrage";
	private final static String NOMEDB=Settings.getCurrentDir()+"database2.sqlite";
	
	public static void Connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			
			SQLiteConfig conf=new SQLiteConfig();
			conf.enableRecursiveTriggers(true);
			conf.enforceForeignKeys(true);
			conf.setSynchronous(SynchronousMode.OFF);
			
			con = DriverManager.getConnection("jdbc:sqlite:"+NOMEDB, conf.toProperties());
			System.out.println("Connessione OK");
			checkIntegrita();
		} 
		catch (Exception e) {
			System.out.println("Connessione Fallita");
			System.out.println(e.getMessage());
			ManagerException.registraEccezione(e);
			JOptionPane.showMessageDialog(null, "Errore fatale: impossibile connettersi al database");
			System.exit(0);
		}
	}
	public static void Disconnect(){
		try {
			con.close();
			con=null;
		}
		catch (SQLException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	public static void createTable(String nome_tabella, ArrayList<SQLColumn> colonne) throws Exception{
		if(nome_tabella.trim().isEmpty() || colonne.isEmpty())
			throw new Exception("Specificare il nome della tabella e/o le colonne");
		
		String query="CREATE TABLE IF NOT EXISTS "+nome_tabella+" (";
		for(int i=0;i<colonne.size()-1;i++)
			query+=colonne.get(i)+", ";
		query+=colonne.get(colonne.size()-1)+" )";
		
		Statement st=con.createStatement();
		//System.out.println(query);
		st.executeUpdate(query);
		st.close();
	}
	public static void creaDB(){
		try {
			if(con==null || con.isClosed())
				Connect();

			Statement stat = con.createStatement();
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_SERIETV+" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_EPISODI+" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_ITASA+" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"nome_serie TEXT NOT NULL,"+
					"id_serie INTEGER NOT NULL)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_SUBSFACTORY +" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"nome_serie TEXT NOT NULL,"+
					"directory TEXT NOT NULL)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS logsub(" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"id_serie INTEGER NOT NULL,"+
					"id_provider INTEGER)");
	
			/**
			 * Tabella Settings
			 * dir_download | dir_client | dir_vlc | tray_on_icon | start_hidden | ask_on_close | always_on_top | start_win | ricerca_auto | min_ricerca | lingua | new_update | last_version | numero_avvii | ricerca_sub | itasa_id | itasa_pass | id_client
			 */
			//TODO modificare tabella settings
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS settings(" +
					"dir_download TEXT DEFAULT ''," +
					"dir_client TEXT DEFAULT ''," +
					"dir_vlc TEXT DEFAULT ''," +
					"tray_on_icon INTEGER DEFAULT 1," +
					"start_hidden INTEGER DEFAULT 0," +
					"ask_on_close INTEGER DEFAULT 1," +
					"always_on_top INTEGER DEFAULT 0," +
					"start_win INTEGER DEFAULT 1," +
					"ricerca_auto INTEGER DEFAULT 0," +
					"min_ricerca INTEGER DEFAULT 480," +
					"lingua INTEGER DEFAULT 1," +
					"new_update INTEGER DEFAULT 1," +
					"last_version INTEGER DEFAULT 0," +
					"numero_avvii INTEGER DEFAULT 1," +
					"ricerca_sub INTEGER DEFAULT 1," +
					"itasa_id TEXT DEFAULT ''," +
					"itasa_pass TEXT DEFAULT ''," +
					"client_id TEXT DEFAULT ''," +
					"mostra_preair INTEGER DEFAULT 1," +
					"mostra_720p INTEGER DEFAULT 1," +
					"download_preair INTEGER DEFAULT 0," +
					"download_720p INTEGER DEFAULT 0"+
					")");
			if(isEmptyTable(TABLE_SETTINGS)){
				SQLParameter[] parametri=new SQLParameter[1];
				parametri[0]=new SQLParameter(SQLParameter.INTEGER, 1, "tray_on_icon");
				//TODO inserire valore per avere i settaggi di default inseriti
				//insert(TABLE_SETTINGS, parametri);
			}
			stat.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	private static void checkIntegrita(){
		creaDB();
		if(!checkColumn(TABLE_SERIETV, "id")){
			alter_aggiungicampo(TABLE_SERIETV, "id", "INTEGER", "");
		}
		if(!checkColumn(TABLE_SERIETV, "nome")){
			alter_aggiungicampo(TABLE_SERIETV, "nome", "TEXT", "");
		}
		if(!checkColumn(TABLE_SERIETV, "url")){
			alter_aggiungicampo(TABLE_SERIETV, "url", "TEXT", "");
		}
		if(!checkColumn(TABLE_SERIETV, "stato")){
			alter_aggiungicampo(TABLE_SERIETV, "stato", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "end")){
			alter_aggiungicampo(TABLE_SERIETV, "id", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "inserita")){
			alter_aggiungicampo(TABLE_SERIETV, "inserita", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "id_itasa")){
			alter_aggiungicampo(TABLE_SERIETV, "id_itasa", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "cleanup")){
			alter_aggiungicampo(TABLE_SERIETV, "cleanup", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "directory_subsfactory")){
			alter_aggiungicampo(TABLE_SERIETV, "directory_subsfactory", "TEXT", "");
		}
		if(!checkColumn(TABLE_EPISODI, "id")){
			alter_aggiungicampo(TABLE_EPISODI, "id", "INTEGER", "");
		}
		if(!checkColumn(TABLE_EPISODI, "id_serie")){
			alter_aggiungicampo(TABLE_EPISODI, "id_serie", "INTEGER", "");
		}
		if(!checkColumn(TABLE_EPISODI, "magnet")){
			alter_aggiungicampo(TABLE_EPISODI, "magnet", "TEXT", "");
		}
		if(!checkColumn(TABLE_EPISODI, "vista")){
			alter_aggiungicampo(TABLE_EPISODI, "vista", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "serie")){
			alter_aggiungicampo(TABLE_EPISODI, "serie", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "episodio")){
			alter_aggiungicampo(TABLE_EPISODI, "episodio", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "HD720p")){
			alter_aggiungicampo(TABLE_EPISODI, "HD720p", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "repack")){
			alter_aggiungicampo(TABLE_EPISODI, "repack", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "preair")){
			alter_aggiungicampo(TABLE_EPISODI, "preair", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "proper")){
			alter_aggiungicampo(TABLE_EPISODI, "proper", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "sottotitolo")){
			alter_aggiungicampo(TABLE_EPISODI, "sottotitolo", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_LOGSUB, "id")){
			alter_aggiungicampo(TABLE_LOGSUB, "id", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_LOGSUB, "serie")){
			alter_aggiungicampo(TABLE_LOGSUB, "serie", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_LOGSUB, "episodio")){
			alter_aggiungicampo(TABLE_LOGSUB, "episodio", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_LOGSUB, "nome_serie")){
			alter_aggiungicampo(TABLE_LOGSUB, "nome_serie", "TEXT", "");
		}
		if(!checkColumn(TABLE_LOGSUB, "provider")){
			alter_aggiungicampo(TABLE_LOGSUB, "provider", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "dir_download")){
			alter_aggiungicampo(TABLE_SETTINGS, "dir_download", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "dir_client")){
			alter_aggiungicampo(TABLE_SETTINGS, "dir_client", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "dir_vlc")){
			alter_aggiungicampo(TABLE_SETTINGS, "dir_vlc", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "itasa_id")){
			alter_aggiungicampo(TABLE_SETTINGS, "itasa_id", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "itasa_pass")){
			alter_aggiungicampo(TABLE_SETTINGS, "itasa_pass", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "client_id")){
			alter_aggiungicampo(TABLE_SETTINGS, "client_id", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "tray_on_icon")){
			alter_aggiungicampo(TABLE_SETTINGS, "tray_on_icon", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "start_hidden")){
			alter_aggiungicampo(TABLE_SETTINGS, "start_hidden", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "ask_on_close")){
			alter_aggiungicampo(TABLE_SETTINGS, "ask_on_close", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "always_on_top")){
			alter_aggiungicampo(TABLE_SETTINGS, "always_on_top", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "start_win")){
			alter_aggiungicampo(TABLE_SETTINGS, "start_win", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "ricerca_auto")){
			alter_aggiungicampo(TABLE_SETTINGS, "ricerca_auto", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "min_ricerca")){
			alter_aggiungicampo(TABLE_SETTINGS, "min_ricerca", "INTEGER", "480");
		}
		if(!checkColumn(TABLE_SETTINGS, "lingua")){
			alter_aggiungicampo(TABLE_SETTINGS, "lingua", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "new_update")){
			alter_aggiungicampo(TABLE_SETTINGS, "new_update", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "last_version")){
			alter_aggiungicampo(TABLE_SETTINGS, "last_version", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "numero_avvii")){
			alter_aggiungicampo(TABLE_SETTINGS, "numero_avvii", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "ricerca_sub")){
			alter_aggiungicampo(TABLE_SETTINGS, "ricerca_sub", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "mostra_preair")){
			alter_aggiungicampo(TABLE_SETTINGS, "mostra_preair", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "mostra_720p")){
			alter_aggiungicampo(TABLE_SETTINGS, "mostra_720p", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "download_preair")){
			alter_aggiungicampo(TABLE_SETTINGS, "download_preair", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "download_720p")){
			alter_aggiungicampo(TABLE_SETTINGS, "download_720p", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "check_episodi")){
			alter_aggiungicampo(TABLE_SETTINGS, "check_episodi", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "hidden_on_play")){
			alter_aggiungicampo(TABLE_SETTINGS, "hidden_on_play", "INTEGER", "1");
		}
	}
	
	public static boolean drop(String table){
		String query="DROP TABLE IF EXISTS "+table;
		try {
			Statement st=con.createStatement();
			st.executeUpdate(query);
			st.close();
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		return false;
	}
	public static ArrayList<String> getTableColumns(String tableName) { 
		ArrayList<String> columns = new ArrayList<String>();
		try {
			String cmd = "pragma table_info(" + tableName + ");"; 
			Statement st=con.createStatement();
			ResultSet cur=st.executeQuery(cmd);
			
			while (cur.next()) {
				String nome=cur.getString(2);
				columns.add(nome);
			}
			cur.close();
		}
		catch(SQLException e){
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		return columns;
	}
	private static boolean checkColumn(String table, String field){
		ArrayList<String> columns=getTableColumns(table);
		for(int i=0;i<columns.size();i++){
			if(columns.get(i).compareTo(field)==0){
				//ManagerException.registraEccezione(new Exception("Campo '"+field+"' già presente nella tabella '"+table+"'"));
				return true;
			}
		}
		System.out.println(field+ " non presente");
		return false;
	}
	public static boolean alter_aggiungicampo(String table, String campo, String tipo, String default_v){
		ArrayList<String> columns=getTableColumns(table);
		for(int i=0;i<columns.size();i++){
			if(columns.get(i).compareTo(campo)==0){
				ManagerException.registraEccezione(new Exception("Campo '"+campo+"' già presente nella tabella '"+table+"'"));
				return false;
			}
		}
		
		String query="ALTER TABLE "+table+" ADD COLUMN "+campo+" "+tipo+(default_v.isEmpty()?"":(" DEFAULT "+default_v));
		try {
			Statement st=con.createStatement();
			st.executeUpdate(query);
			st.close();
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		return false;
	}
	public static boolean isEmptyTable(String tableName){
		return rowCount(tableName)==0;
	}
	public static int rowCount(String table){
		String query="SELECT COUNT(*) FROM "+table;
		try {
			Statement stat=con.createStatement();
			ResultSet rs=stat.executeQuery(query);
			int count=0;
			while(rs.next()){
				count=rs.getInt(1);
			}
			rs.close();
			stat.close();
			return count;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		return -1;
	}
	public static boolean updateQuery(String query){
		int ins_ok=0;
		try {
			Statement stat=con.createStatement();
			ins_ok=stat.executeUpdate(query);
		}
		catch (SQLException e) {
			System.out.println("INSERT "+e.getMessage());
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		return (ins_ok==0?false:true);
	}
}
