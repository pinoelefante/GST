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

public class Database {
	private static Connection con;
	
	public final static String TABLE_SERIETV="serie";
	public final static String TABLE_TORRENT="torrent";
	public final static String TABLE_ITASA="itasa";
	public final static String TABLE_SUBSFACTORY="subsfactory";
	public final static String TABLE_LOGSUB="logsub";
	public final static String TABLE_LINGUE="lingue";
	public final static String TABLE_TRADUZIONI="traduzioni";
	public final static String TABLE_SETTINGS="settings";
	public final static String TABLE_TVRAGE="tvrage";
	private final static String NOMEDB="database.db";
	
	public static void Connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			
			SQLiteConfig conf=new SQLiteConfig();
			conf.enableRecursiveTriggers(true);
			conf.enforceForeignKeys(true);
			conf.setSynchronous(SynchronousMode.OFF);
			
			con = DriverManager.getConnection("jdbc:sqlite:"+getNomeDB(), conf.toProperties());
			System.out.println("Connessione OK");
			creaDB();
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
			/**
			 * Tabella Serie TV
			 * 
			 * id (int) | nome (string) | url (string) | end (int) | stato (int) | inserita (int) | id_itasa (int) | directory_subsfactory (string)  
			 */
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS serie (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"nome TEXT NOT NULL, " +
					"url TEXT NOT NULL UNIQUE," +
					"stato INTEGER DEFAULT 0,"+
					"end INTEGER DEFAULT 0," +
					"inserita INTEGER DEFAULT 0," +
					"id_itasa INTEGER DEFAULT 0," +
					"tv_rage INTEGER DEFAULT 0,"+
					"cleanup INTEGER DEFAULT 0,"+
					"directory_subsfactory TEXT DEFAULT 0)");
			/**
			 *  Tabella Torrent
			 *  
			 *   id (int) | id_serie (int) | magnet (string) | vista (int) | serie (int) | episodio (int) | 720p(int) | repack (int) | preair (int) | proper (int) | sottotitolo (int)
			 */
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS torrent (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"id_serie INTEGER NOT NULL, " +
					"magnet TEXT NOT NULL UNIQUE," +
					"vista INTEGER DEFAULT 0," +
					"serie INTEGER DEFAULT 0," +
					"episodio INTEGER DEFAULT 0," +
					"HD720p INTEGER DEFAULT 0," +
					"repack INTEGER DEFAULT 0," +
					"preair INTEGER DEFAULT 0," +
					"proper INTEGER DEFAULT 0," +
					"sottotitolo INTEGER DEFAULT 0," +
					"FOREIGN KEY(id_serie) REFERENCES serie(id) ON DELETE CASCADE)");
			/**
			 *  Tabella Itasa
			 *   id_serie (int) | nome_serie (string)
			 */
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS itasa(" +
					"id_serie INTEGER PRIMARY KEY," +
					"nome_serie TEXT NOT NULL)");
			/**
			 *  Tabella Subsfactory.it
			 *   id_serie (int) | nome_serie (string)
			 */
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS subsfactory(" +
					"id_serie INTEGER PRIMARY KEY NOT NULL," +
					"nome_serie TEXT NOT NULL)");
			/**
			 * Log sottotitoli scaricati
			 *  id (int) | nome_serie (string) | serie (int) | episodio (int) | provider (string)
			 */
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS logsub(" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"nome_serie TEXT NOT NULL," +
					"serie INTEGER DEFAULT 0," +
					"episodio INTEGER DEFAULT 0," +
					"provider TEXT DEFAULT '')");
			/**
			 * Tabella lingue
			 *  id (int) | nome (string) 
			 */
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS lingue(" +
					"id INTEGER PRIMARY KEY NOT NULL," +
					"nome TEXT NOT NULL)");
			/**
			 * Tabella traduzioni
			 *  id (int) | id_lingua (int) | voce (string) | traduzione (string)
			 */
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS traduzioni(" +
					"id INTEGER PRIMARY KEY," +
					"id_serie INTEGER NOT NULL," +
					"voce TEXT NOT NULL," +
					"traduzione TEXT DEFAULT ''," +
					"FOREIGN KEY(id_serie) REFERENCES lingue(id) ON DELETE CASCADE)");
			/**
			 * Tabella Settings
			 * dir_download | dir_client | dir_vlc | tray_on_icon | start_hidden | ask_on_close | always_on_top | start_win | ricerca_auto | min_ricerca | lingua | new_update | last_version | numero_avvii | ricerca_sub | itasa_id | itasa_pass | id_client
			 */
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
				insert(TABLE_SETTINGS, parametri);
			}
			stat.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
	}
	private static void checkIntegrita(){
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
		if(!checkColumn(TABLE_TORRENT, "id")){
			alter_aggiungicampo(TABLE_TORRENT, "id", "INTEGER", "");
		}
		if(!checkColumn(TABLE_TORRENT, "id_serie")){
			alter_aggiungicampo(TABLE_TORRENT, "id_serie", "INTEGER", "");
		}
		if(!checkColumn(TABLE_TORRENT, "magnet")){
			alter_aggiungicampo(TABLE_TORRENT, "magnet", "TEXT", "");
		}
		if(!checkColumn(TABLE_TORRENT, "vista")){
			alter_aggiungicampo(TABLE_TORRENT, "vista", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_TORRENT, "serie")){
			alter_aggiungicampo(TABLE_TORRENT, "serie", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_TORRENT, "episodio")){
			alter_aggiungicampo(TABLE_TORRENT, "episodio", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_TORRENT, "HD720p")){
			alter_aggiungicampo(TABLE_TORRENT, "HD720p", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_TORRENT, "repack")){
			alter_aggiungicampo(TABLE_TORRENT, "repack", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_TORRENT, "preair")){
			alter_aggiungicampo(TABLE_TORRENT, "preair", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_TORRENT, "proper")){
			alter_aggiungicampo(TABLE_TORRENT, "proper", "INTEGER", "0");
		}
		if(!checkColumn(TABLE_TORRENT, "sottotitolo")){
			alter_aggiungicampo(TABLE_TORRENT, "sottotitolo", "INTEGER", "0");
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
	public static boolean alter_rimuovicampo(String tabella, String campo){return false;}
	/** TODO completare
	public static boolean alter_rimuovicampo(String tabella, String campo){
		boolean is_in=false;
		ArrayList<String> fields=getTableColumns(tabella);
		for(int i=0;i<fields.size();i++){
			if(fields.get(i).compareTo(campo)==0){
				is_in=true;
				fields.remove(i);
				break;
			}
		}
		if(!is_in){
			ManagerException.registraEccezione(new Exception("La tabella '"+tabella+"' non esiste. Il campo '"+campo+"' non pu� essere rimosso"));
			return false;
		}
		if(fields.size()==0){
			return drop(tabella);
		}
		
		String query="SELECT ";
		for(int i=0;i<fields.size();i++){
			query+=fields.get(i);
			if(i<fields.size()-1);
			query+=", ";
		}
		query+=" FROM "+tabella;
		try {
			Statement st=con.createStatement();
			ResultSet res=st.executeQuery(query);
			ResultSetMetaData meta=res.getMetaData();
			ArrayList<SQLParameter[]> selezione = new ArrayList<SQLParameter[]>();
			int j=0;
			while(res.next()){
				selezione.add(new SQLParameter[meta.getColumnCount()]);
				for(int i=1;i<=meta.getColumnCount();i++){
					int tipo=0;
					switch(meta.getColumnTypeName(i)){
						case "integer":
							//System.out.print("Intero - ");
							tipo=SQLParameter.INTEGER;
							break;
						case "text":
							tipo=SQLParameter.TEXT;
							break;
						default:
							System.out.println("SELECT " + meta.getColumnTypeName(i));
					}
					selezione.get(j)[i-1]=new SQLParameter(tipo, tipo==SQLParameter.INTEGER?res.getInt(i):res.getString(i), meta.getColumnName(i));
				}
				j++;
			}
			st.close();
			drop(tabella);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			ManagerException.registraEccezione(e);
		}
		return false;
	}
	*/
	/** TODO completare
	private static ArrayList<SQLColumn> getColumnStats(ResultSetMetaData meta){
		ArrayList<SQLColumn> fields=new ArrayList<SQLColumn>();
		if(meta!=null){
			try {
				for(int i=1;i<=meta.getColumnCount();i++){
					String nome=meta.getColumnLabel(i);
					String type_s=meta.getColumnTypeName(i);
					int tipo=0;
					switch(type_s){
						case "integer":
							tipo=SQLParameter.INTEGER;
							break;
						case "text":
							tipo=SQLParameter.TEXT;
							break;
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
				ManagerException.registraEccezione(e);
			}
		}
		return fields;
	}
	*/
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
				//ManagerException.registraEccezione(new Exception("Campo '"+field+"' gi� presente nella tabella '"+table+"'"));
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
				ManagerException.registraEccezione(new Exception("Campo '"+campo+"' gi� presente nella tabella '"+table+"'"));
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
	public static boolean insert(String table, SQLParameter[] parametri){
		String query="INSERT into "+table+" (";
		for(int i=0;i<parametri.length-1;i++)
			query+=parametri[i].getField()+",";
		query+=parametri[parametri.length-1].getField()+") VALUES (";
		for(int i=0;i<parametri.length;i++){
			switch(parametri[i].ptype()){
				case SQLParameter.INTEGER:
					query+=parametri[i].pvalueAsString();
					break;
				case SQLParameter.TEXT:
					query+=parametri[i].pvalueAsFormattedString();
					break;
			}
			if(i==parametri.length-1)
				query+=")";
			else
				query+=",";
		}
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
	/*
	 * tipo condizione= AND - OR - NOT
	 * tipo_confronto= =  <  > etc.etc.
	 */
	public static boolean update(String table, SQLParameter[] parametri, SQLParameter[] condizioni, String tipo_condizione, String tipo_confronto){
		String query="UPDATE "+table+" SET ";
		for(int i=0;i<parametri.length;i++){
			switch(parametri[i].ptype()){
				case SQLParameter.INTEGER:
					query+=parametri[i].getField()+"="+parametri[i].pvalueAsString();
					break;
				case SQLParameter.TEXT:
					query+=parametri[i].getField()+"="+parametri[i].pvalueAsFormattedString();
					break;
			}
			if(i==parametri.length-1)
				query+=" ";
			else
				query+=", ";
		}
		if(condizioni!=null){
			if(condizioni.length>0){
				query+="WHERE ";
				for(int i=0;i<condizioni.length;i++){
					switch(condizioni[i].ptype()){
						case SQLParameter.INTEGER:
							query+=condizioni[i].getField()+tipo_confronto+condizioni[i].pvalueAsString();
							break;
						case SQLParameter.TEXT:
							query+=condizioni[i].getField()+tipo_confronto+condizioni[i].pvalueAsFormattedString();
							break;
					}
					if(i==condizioni.length-1)
						query+=" ";
					else
						query+=tipo_condizione+" ";
				}
			}
		}
		int row_affected=0;
		try {
			Statement stat=con.createStatement();
			row_affected=stat.executeUpdate(query);
			stat.close();
		}
		catch (SQLException e) {
			System.out.println("UPDATE "+e.getMessage());
			ManagerException.registraEccezione(e);
			return false;
		}
		return row_affected>0;
	}
	/*
	 * tipo condizione= AND - OR - NOT
	 * tipo_confronto= =  <  > etc.etc.
	 */
	public static ArrayList<SQLParameter[]> select(String table, SQLParameter[] condizioni, String tipo_condizione, String tipo_confronto){
		String query="SELECT * FROM "+table;
		if(condizioni!=null){
			if(condizioni.length>0){
				query+=" WHERE ";
				for(int i=0;i<condizioni.length;i++){
					switch(condizioni[i].ptype()){
						case SQLParameter.INTEGER:
							query+=condizioni[i].getField()+tipo_confronto+condizioni[i].pvalueAsString();
							break;
						case SQLParameter.TEXT:
							query+=condizioni[i].getField()+tipo_confronto+condizioni[i].pvalueAsFormattedString();
							break;
					}
					if(i==condizioni.length-1)
						query+=" ";
					else
						query+=tipo_condizione+" ";
				}
			}
		}
		ResultSet res=null;
		ArrayList<SQLParameter[]> selezione=null;
		try {
			Statement stat=con.createStatement();
			res=stat.executeQuery(query);
			ResultSetMetaData meta=res.getMetaData();
			selezione=new ArrayList<SQLParameter[]>();
			int j=0;
			while(res.next()){
				selezione.add(new SQLParameter[meta.getColumnCount()]);
				for(int i=1;i<=meta.getColumnCount();i++){
					int tipo=0;
					switch(meta.getColumnTypeName(i)){
						case "integer":
							//System.out.print("Intero - ");
							tipo=SQLParameter.INTEGER;
							break;
						case "text":
							tipo=SQLParameter.TEXT;
							break;
						default:
							System.out.println("SELECT " + meta.getColumnTypeName(i));
					}
					selezione.get(j)[i-1]=new SQLParameter(tipo, tipo==SQLParameter.INTEGER?res.getInt(i):res.getString(i), meta.getColumnName(i));
				}
				j++;
			}
			stat.close();
		}
		catch (SQLException e) {
			System.out.println("SELECT "+e.getMessage());
			ManagerException.registraEccezione(e);
			//e.printStackTrace();
		}
		return selezione;
	}
	//extra - esempio ORDER BY
	public static ArrayList<SQLParameter[]> select(String table, SQLParameter[] condizioni, String tipo_condizione, String tipo_confronto, String extra){
		String query="SELECT * FROM "+table;
		if(condizioni!=null){
			if(condizioni.length>0){
				query+=" WHERE ";
				for(int i=0;i<condizioni.length;i++){
					switch(condizioni[i].ptype()){
						case SQLParameter.INTEGER:
							query+=condizioni[i].getField()+tipo_confronto+condizioni[i].pvalueAsString();
							break;
						case SQLParameter.TEXT:
							query+=condizioni[i].getField()+tipo_confronto+condizioni[i].pvalueAsFormattedString();
							break;
					}
					if(i==condizioni.length-1)
						query+=" ";
					else
						query+=tipo_condizione+" ";
				}
			}
		}
		if(extra!=null)
			query+=extra;
		ResultSet res=null;
		ArrayList<SQLParameter[]> selezione=null;
		try {
			Statement stat=con.createStatement();
			res=stat.executeQuery(query);
			ResultSetMetaData meta=res.getMetaData();
			selezione=new ArrayList<SQLParameter[]>();
			int j=0;
			while(res.next()){
				selezione.add(new SQLParameter[meta.getColumnCount()]);
				for(int i=1;i<=meta.getColumnCount();i++){
					int tipo=0;
					switch(meta.getColumnTypeName(i)){
						case "integer":
							//System.out.print("Intero - ");
							tipo=SQLParameter.INTEGER;
							break;
						case "text":
							tipo=SQLParameter.TEXT;
							break;
						default:
							System.out.println("SELECT " + meta.getColumnTypeName(i));
					}
					selezione.get(j)[i-1]=new SQLParameter(tipo, tipo==SQLParameter.INTEGER?res.getInt(i):res.getString(i), meta.getColumnName(i));
				}
				j++;
			}
			stat.close();
		}
		catch (SQLException e) {
			System.out.println("SELECT "+e.getMessage());
			ManagerException.registraEccezione(e);
			//e.printStackTrace();
		}
		return selezione;
	}
	public static String getNomeDB() {
		return NOMEDB;
	}
	public static boolean insertMulti(String table, ArrayList<SQLParameter[]> parametri){
		String query="";
		if(parametri!=null){
			if(parametri.size()>0){
				SQLParameter[] p=parametri.get(0);
				if(p.length<=0)
					return false;
				
				query+="INSERT INTO "+table+" (";
				for(int i=0;i<p.length-1;i++)
					query+=p[i].getField()+", ";
				query+=p[p.length-1].getField()+")\n";
				
				for(int i=0;i<parametri.size();i++){
					SQLParameter[] par=parametri.get(i);
					query+="SELECT ";
					for(int j=0;j<par.length;j++){
						SQLParameter p1=par[j];
						switch(p1.ptype()){
							case SQLParameter.INTEGER:
								query+=(Integer)p1.pvalue();
								break;
							case SQLParameter.TEXT:
								query+=p1.pvalueAsFormattedString();
								break;
						}
						if(j==par.length-1)
							query+="\n";
						else
							query+=", ";
					}
					if(i<parametri.size()-1)
						query+="UNION ALL\n";
				}
				try {
					Statement st=con.createStatement();
					int r=st.executeUpdate(query);
					st.close();
					return r>0;
				}
				catch (SQLException e) { 
					System.out.println(e.getMessage());
					ManagerException.registraEccezione(e);
				}
			}
			else
				return false;
		}
		return false;
	}
	public static void main(String[] args){
		Connect();
		ArrayList<String> col=getTableColumns(TABLE_SETTINGS);
		for(int i=0;i<col.size();i++)
			System.out.println(col.get(i));
	}
}
