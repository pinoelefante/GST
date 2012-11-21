package Sottotitoli;

public class SerieSub {
	public final static int TIPOID_INT=1, TIPOID_STRING=2;
	private String nomeserie;
	private Object ID;
	private int tipo_id;
	
	public SerieSub(String nome, Object id) {
		setNomeSerie(nome);
		setID(id);
	}
	public String getNomeSerie() {
		return nomeserie;
	}
	public void setNomeSerie(String nomeserie) {
		this.nomeserie = nomeserie;
	}
	public Object getID() {
		return ID;
	}
	public void setID(Object iD) {
		ID = iD;
	}
	public int getTipoID() {
		return tipo_id;
	}
	public void setTipoID(int tipo_id) {
		this.tipo_id = tipo_id;
	}
	public String toString(){
		return getNomeSerie();
	}
}
