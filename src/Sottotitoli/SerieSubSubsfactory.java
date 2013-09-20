package Sottotitoli;

import java.util.ArrayList;

import Database.Database;
import StruttureDati.db.KVResult;

public class SerieSubSubsfactory extends SerieSub {
	private boolean directory_search;
	private int id_db;
	public SerieSubSubsfactory(String nome, Object id) {
		super(nome, id);
	}
	public boolean isDirectorySearch(){
		return directory_search;
	}
	public void setDirectorySearch(boolean stat){
		directory_search=stat;
	}
	public void setIDDB(int i){
		id_db=i;
	}
	public int getIDDB(){
		if(id_db<=0){
			String query="SELECT id FROM "+Database.TABLE_SUBSFACTORY+" WHERE nome_serie=\""+getNomeSerie()+"\", AND directory=\""+(String)getID()+"\"";
			ArrayList<KVResult<String, Object>> res=Database.selectQuery(query);
			if(res.size()==1){
				setIDDB(((int)res.get(0).getValueByKey("id")));
			}
		}
		return id_db;
	}
}
