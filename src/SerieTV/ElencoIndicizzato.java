package SerieTV;

import java.util.ArrayList;

public interface ElencoIndicizzato {
	public void add(Indexable e);
	public boolean remove(Indexable e);
	public boolean remove(int index);
	public boolean remove(int index, int key);
	public ArrayList<Indexable> get(int index, int key);
	public ArrayList<Indexable> get(int i);
	public int size();
	public int n_index();
	public Indexable get(int index, String offkey);
	public Indexable get(String offkey);
	public void reset();
	public ArrayList<Indexable> getLinear();
}
