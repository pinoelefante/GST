package StruttureDati;

public interface GenericElencoIndicizzato {
	public void add(Indexable e);
	public void remove(Indexable e);
	public Indexable get(int key, int off);
}
