package SerieTV;

import java.util.ArrayList;

public interface ElencoIndicizzato {
	public boolean add(Indexable e);
	public boolean remove(Indexable e);
	public void removeAll();
	public Indexable get(Indexable e);
	public ArrayList<Indexable> get(int index);
	public Indexable get(String offkey);
	public Integer[] getIndexes();
	public ArrayList<Indexable> getLinear();
}
