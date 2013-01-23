package Database;

public class SQLColumn {
	private String nome_colonna;
	private String tipo_dato;
	private String default_value;
	private String chiave;
	private String other;
	
	public SQLColumn(String nome, String tipo, String valore, String chiave, String altro) {
		setNomeColonna(nome);
		setTipoDato(tipo);
		setDefaultValue(valore);
		this.setChiave(chiave);
		setOther(altro);
	}

	public String getNomeColonna() {
		return nome_colonna;
	}

	public void setNomeColonna(String nome_colonna) {
		this.nome_colonna = nome_colonna;
	}

	public String getTipoDato() {
		return tipo_dato;
	}

	public void setTipoDato(String tipo_dato) {
		this.tipo_dato = tipo_dato;
	}

	public String getDefaultValue() {
		return default_value;
	}

	public void setDefaultValue(String default_value) {
		this.default_value = default_value;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
	public String toString(){
		return getNomeColonna()+" "+getTipoDato()+" "+getChiave()+" "+getOther()+(getDefaultValue().isEmpty()?"":(" DEFAULT "+getDefaultValue()));
	}
}
