package Database;

public class SQLParameter {
	public final static int	INTEGER=0;
	public final static int	TEXT=1;
	
	private int tipo;
	private Object parametro;
	private String field;
	
	public SQLParameter(int type, Object e, String campo){
		tipo=type;
		parametro=e;
		field=campo;
	}
	public int ptype(){
		return tipo;
	}
	public Object pvalue(){
		return parametro;
	}
	public boolean isString(){
		return tipo==TEXT;
	}
	public boolean isInteger(){
		return tipo==INTEGER;
	}
	public String pvalueAsString(){
		return ""+(tipo==TEXT?(String)parametro:(Integer)parametro);
	}
	public String pvalueAsFormattedString(){
		return "\""+pvalueAsString()+"\"";
	}
	public String getField(){
		return field;
	}
}
