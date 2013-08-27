package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.SynchronousMode;

import Programma.ManagerException;
import Programma.Settings;
import StruttureDati.db.KVItem;
import StruttureDati.db.KVResult;

public class Database2 {
	private static Connection con;
	
	public final static String TABLE_SERIETV="serietv";
	public final static String TABLE_EPISODI="episodi";
	public final static String TABLE_ITASA="itasa";
	public final static String TABLE_SUBSFACTORY="subsfactory";
	public final static String TABLE_LOGSUB="logsub";
	public final static String TABLE_SETTINGS="settings";
	public static final String TABLE_SUBSPEDIA = "subspedia";
	public static final String TABLE_TVDB_SERIE = "tvdb_serie";
	public static final String TABLE_TVDB_EPISODI = "tvdb_ep";
	
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
	public static void creaDB(){
		try {
			if(con==null || con.isClosed())
				Connect();

			Statement stat = con.createStatement();
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_SERIETV+" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, "+
					"url TEXT NOT NULL"
					+ ")");
			/*
			String autoincrement_q="ALTER TABLE id AUTOINCREMENT=1";
			stat.executeUpdate(autoincrement_q);
			*/
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_EPISODI+" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_ITASA+" (" +
					"id_serie INTEGER PRIMARY KEY AUTOINCREMENT)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_SUBSFACTORY +" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT)");
					
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_LOGSUB+" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT)");

			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_SUBSPEDIA+" (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_TVDB_SERIE+" (" +
					"id_serie INTEGER PRIMARY KEY AUTOINCREMENT)");
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_TVDB_EPISODI+" (" +
					"id_episodio INTEGER PRIMARY KEY AUTOINCREMENT)");
	
			/**
			 * Tabella Settings
			 * dir_download | dir_client | dir_vlc | tray_on_icon | start_hidden | ask_on_close | always_on_top | start_win | ricerca_auto | min_ricerca | lingua | new_update | last_version | numero_avvii | ricerca_sub | itasa_id | itasa_pass | id_client
			 */
			//TODO modificare tabella settings
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS "+TABLE_SETTINGS+" ("+
					"ask_on_close INTEGER)");
			if(isEmptyTable(TABLE_SETTINGS)){
				String query="INSERT INTO "+TABLE_SETTINGS+" (ask_on_close) VALUES(1)";
				updateQuery(query);
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
		
		/** SERIETV */
		if(!checkColumn(TABLE_SERIETV, "id")){
			alter_aggiungicampo(TABLE_SERIETV, "id", "INTEGER PRIMARY KEY AUTOINCREMENT", "");
		}
		if(!checkColumn(TABLE_SERIETV, "nome")){
			alter_aggiungicampo(TABLE_SERIETV, "nome", "TEXT", "");
		}
		if(!checkColumn(TABLE_SERIETV, "url")){
			alter_aggiungicampo(TABLE_SERIETV, "url", "TEXT", "");
		}
		if(!checkColumn(TABLE_SERIETV, "inserita")){
			alter_aggiungicampo(TABLE_SERIETV, "inserita", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "conclusa")){
			alter_aggiungicampo(TABLE_SERIETV, "conclusa", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "stop_search")){
			alter_aggiungicampo(TABLE_SERIETV, "stop_search", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "provider")){
			alter_aggiungicampo(TABLE_SERIETV, "provider", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "id_itasa")){
			alter_aggiungicampo(TABLE_SERIETV, "id_itasa", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "id_subsfactory")){
			alter_aggiungicampo(TABLE_SERIETV, "id_subsfactory", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "id_subspedia")){
			alter_aggiungicampo(TABLE_SERIETV, "id_subspedia", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SERIETV, "id_tvdb")){
			alter_aggiungicampo(TABLE_SERIETV, "id_tvdb", "INTEGER", "0");
		}
		/** SERIETV - FINE*/
		
		/** ITASA*/
		if(!checkColumn(TABLE_ITASA, "id_serie")){
			alter_aggiungicampo(TABLE_ITASA, "id_serie", "INTEGER PRIMARY KEY", "");
		}
		if(!checkColumn(TABLE_ITASA, "nome_serie")){
			alter_aggiungicampo(TABLE_ITASA, "nome_serie", "TEXT", "");
		}
		/** ITASA - FINE*/
		
		/** SUBSFACTORY */
		if(!checkColumn(TABLE_SUBSFACTORY, "id")){
			alter_aggiungicampo(TABLE_SUBSFACTORY, "id", "INTEGER PRIMARY KEY AUTOINCREMENT", "");
		}
		if(!checkColumn(TABLE_SUBSFACTORY, "nome_serie")){
			alter_aggiungicampo(TABLE_SUBSFACTORY, "nome_serie", "TEXT", "");
		}
		if(!checkColumn(TABLE_SUBSFACTORY, "directory")){
			alter_aggiungicampo(TABLE_SUBSFACTORY, "directory", "TEXT", "");
		}
		/** SUBSFACTORY - FINE */
		
		/** SUBSPEDIA */
		if(!checkColumn(TABLE_SUBSPEDIA, "id")){
			alter_aggiungicampo(TABLE_SUBSPEDIA, "id", "INTEGER PRIMARY KEY AUTOINCREMENT", "");
		}
		if(!checkColumn(TABLE_SUBSPEDIA, "nome_serie")){
			alter_aggiungicampo(TABLE_SUBSPEDIA, "nome_serie", "TEXT", "");
		}
		if(!checkColumn(TABLE_SUBSPEDIA, "directory")){
			alter_aggiungicampo(TABLE_SUBSPEDIA, "directory", "TEXT", "");
		}
		/** SUBSPEDIA - FINE */
		
		/** LOGSUB */
		if(!checkColumn(TABLE_LOGSUB, "id")){
			alter_aggiungicampo(TABLE_LOGSUB, "id", "INTEGER PRIMARY KEY AUTOINCREMENT", "");
		}
		if(!checkColumn(TABLE_LOGSUB, "id_episodio")){
			alter_aggiungicampo(TABLE_LOGSUB, "id_episodio", "INTEGER", "");
		}
		if(!checkColumn(TABLE_LOGSUB, "id_provider")){
			alter_aggiungicampo(TABLE_LOGSUB, "id_provider", "INTEGER", "");
		}
		/** LOGSUB - FINE */
		
		/** TVDB_SERIE */
		if(!checkColumn(TABLE_TVDB_SERIE, "id_serie")){
			alter_aggiungicampo(TABLE_TVDB_SERIE, "id_serie", "INTEGER PRIMARY KEY", "");
		}
		if(!checkColumn(TABLE_TVDB_SERIE, "banner")){
			alter_aggiungicampo(TABLE_TVDB_SERIE, "banner", "TEXT", "");
		}
		if(!checkColumn(TABLE_TVDB_SERIE, "trama")){
			alter_aggiungicampo(TABLE_TVDB_SERIE, "trama", "TEXT", "");
		}
		if(!checkColumn(TABLE_TVDB_SERIE, "zap2it")){
			alter_aggiungicampo(TABLE_TVDB_SERIE, "zap2it", "TEXT", "");
		}
		/** TVDB_SERIE - FINE*/
		
		/** TVDB_EP */
		if(!checkColumn(TABLE_TVDB_EPISODI, "id_episodio")){
			alter_aggiungicampo(TABLE_TVDB_EPISODI, "id_episodio", "INTEGER PRIMARY KEY", "");
		}
		if(!checkColumn(TABLE_TVDB_EPISODI, "id_serie")){
			alter_aggiungicampo(TABLE_TVDB_EPISODI, "id_serie", "INTEGER", "");
		}
		if(!checkColumn(TABLE_TVDB_EPISODI, "stagione")){
			alter_aggiungicampo(TABLE_TVDB_EPISODI, "stagione", "INTEGER", "");
		}
		if(!checkColumn(TABLE_TVDB_EPISODI, "episodio")){
			alter_aggiungicampo(TABLE_TVDB_EPISODI, "episodio", "INTEGER", "");
		}
		if(!checkColumn(TABLE_TVDB_EPISODI, "titolo")){
			alter_aggiungicampo(TABLE_TVDB_EPISODI, "titolo", "TEXT", "");
		}
		if(!checkColumn(TABLE_TVDB_EPISODI, "trama")){
			alter_aggiungicampo(TABLE_TVDB_EPISODI, "trama", "TEXT", "");
		}
		if(!checkColumn(TABLE_TVDB_EPISODI, "attori")){
			alter_aggiungicampo(TABLE_TVDB_EPISODI, "attori", "TEXT", "");
		}
		/** TVDB_EP - FINE */
		
		/** EPISODI */
		if(!checkColumn(TABLE_EPISODI, "id")){
			alter_aggiungicampo(TABLE_EPISODI, "id", "INTEGER PRIMARY KEY AUTOINCREMENT", "");
		}
		if(!checkColumn(TABLE_EPISODI, "id_serie")){
			alter_aggiungicampo(TABLE_EPISODI, "id_serie", "INTEGER", "");
		}
		if(!checkColumn(TABLE_EPISODI, "url")){
			alter_aggiungicampo(TABLE_EPISODI, "url", "TEXT", "");
		}
		if(!checkColumn(TABLE_EPISODI, "vista")){
			alter_aggiungicampo(TABLE_EPISODI, "vista", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "stagione")){
			alter_aggiungicampo(TABLE_EPISODI, "stagione", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "episodio")){
			alter_aggiungicampo(TABLE_EPISODI, "episodio", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "tags")){
			alter_aggiungicampo(TABLE_EPISODI, "tags", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "preair")){
			alter_aggiungicampo(TABLE_EPISODI, "preair", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "sottotitolo")){
			alter_aggiungicampo(TABLE_EPISODI, "sottotitolo", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_EPISODI, "id_tvdb_ep")){
			alter_aggiungicampo(TABLE_EPISODI, "id_tvdb_ep", "INTEGER", "0");
		}
		/** EPISODI - FINE */
		
		/** SETTINGS */
		if(!checkColumn(TABLE_SETTINGS, "download_path")){
			alter_aggiungicampo(TABLE_SETTINGS, "download_path", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "utorrent")){
			alter_aggiungicampo(TABLE_SETTINGS, "utorrent", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "vlc")){
			alter_aggiungicampo(TABLE_SETTINGS, "vlc", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "itasa_user")){
			alter_aggiungicampo(TABLE_SETTINGS, "itasa_user", "TEXT", "");
		}
		if(!checkColumn(TABLE_SETTINGS, "itasa_pass")){
			alter_aggiungicampo(TABLE_SETTINGS, "itasa_pass", "TEXT", "");
		}
		/** UTILE QUANDO VERRA' INSERITO IL PREMIUM*/
		if(!checkColumn(TABLE_SETTINGS, "client_id")){
			alter_aggiungicampo(TABLE_SETTINGS, "client_id", "TEXT", "");
		}
		/** */
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
			alter_aggiungicampo(TABLE_SETTINGS, "always_on_top", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "autostart")){
			alter_aggiungicampo(TABLE_SETTINGS, "autostart", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "download_auto")){
			alter_aggiungicampo(TABLE_SETTINGS, "download_auto", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "min_download_auto")){
			alter_aggiungicampo(TABLE_SETTINGS, "min_download_auto", "INTEGER", "480");
		}
		
		if(!checkColumn(TABLE_SETTINGS, "new_update")){
			alter_aggiungicampo(TABLE_SETTINGS, "new_update", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "last_version")){
			alter_aggiungicampo(TABLE_SETTINGS, "last_version", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "download_sottotitoli")){
			alter_aggiungicampo(TABLE_SETTINGS, "download_sottotitoli", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "mostra_preair")){
			alter_aggiungicampo(TABLE_SETTINGS, "mostra_preair", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "mostra_hd")){
			alter_aggiungicampo(TABLE_SETTINGS, "mostra_hd", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "download_auto_preair")){
			alter_aggiungicampo(TABLE_SETTINGS, "download_auto_preair", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "download_auto_hd")){
			alter_aggiungicampo(TABLE_SETTINGS, "download_auto_hd", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "external_vlc")){
			alter_aggiungicampo(TABLE_SETTINGS, "external_vlc", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "itasa")){
			alter_aggiungicampo(TABLE_SETTINGS, "itasa", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_SETTINGS, "hide_viste")){
			alter_aggiungicampo(TABLE_SETTINGS, "hide_viste", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "hide_ignorate")){
			alter_aggiungicampo(TABLE_SETTINGS, "hide_ignorate", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "hide_rimosse")){
			alter_aggiungicampo(TABLE_SETTINGS, "hide_rimosse", "INTEGER", "1");
		}
		if(!checkColumn(TABLE_SETTINGS, "ordine_lettore")){
			alter_aggiungicampo(TABLE_SETTINGS, "ordine_lettore", "INTEGER", "0");
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
			e.printStackTrace();
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
			ManagerException.registraEccezione(e);
		}
		return -1;
	}
	public static ArrayList<KVResult<String,Object>> selectQuery(String query){
		ArrayList<KVResult<String, Object>> result=new ArrayList<KVResult<String, Object>>();
		try {
			Statement stat=con.createStatement();
			ResultSet rs=stat.executeQuery(query);
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				KVResult<String, Object> res=new KVResult<String, Object>();
				for(int i=1;i<=meta.getColumnCount();i++){
					String key=meta.getColumnName(i);
					Object value=rs.getObject(i);
					res.addItem(new KVItem<String, Object>(key, value));
				}
				result.add(res);
			}
			rs.close();
			stat.close();
			return result;
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			ManagerException.registraEccezione(e);
			return null;
		}
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
	public static void rebuildDB(){
		System.out.println("ottimizzazione db in corso...");
		Statement st;
		try {
			st = con.createStatement();
			st.execute("VACUUM");
			st.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
