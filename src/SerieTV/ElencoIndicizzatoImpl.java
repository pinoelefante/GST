package SerieTV;

import java.util.ArrayList;

public class ElencoIndicizzatoImpl implements ElencoIndicizzato {
	private ArrayList<ArrayList<Indexable>> elenco;
	private final int DIM_DEFAULT=20;
	private int size=0;
	
	public ElencoIndicizzatoImpl() {
		elenco=new ArrayList<ArrayList<Indexable>>(DIM_DEFAULT);
		for(int i=0;i<DIM_DEFAULT;i++){
			elenco.add(new ArrayList<Indexable>());
		}
	}

	private void expand(int index) {
		int size=elenco.size();
		int incr=index-size+1;
		while(incr>=0){
			elenco.add(new ArrayList<Indexable>());
			incr--;
		}
	}
	private boolean checkIndex(int index){
		if(index>=elenco.size())
			return false;
		return true;
	}

	@Override
	public void add(Indexable e) {
		if(!checkIndex(e.getIndex()))
			expand(e.getIndex());
		ArrayList<Indexable> el=elenco.get(e.getIndex());
		
		int i=0;
		while(i<el.size()){
			if(el.get(i).getOffKey().compareToIgnoreCase(e.getOffKey())==0)
				return;
			if(el.get(i).getKey()>e.getKey()){
				el.add(i, e);
				size++;
				return;
			}
			i++;
		}
		el.add(e);
		size++;
	}

	@Override
	public boolean remove(Indexable e) {
		if(!checkIndex(e.getIndex()))
			return false;
		ArrayList<Indexable> el;
		el=elenco.get(e.getIndex());
		for(int i=0;i<el.size();i++){
			Indexable cur=el.get(i);
			if(cur.getKey()>e.getKey())
				return false;
			else if(cur.getKey()==e.getKey()){
				el.remove(i);
				size--;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(int index) {
		if(!checkIndex(index))
			return false;
		elenco.remove(index);
		size--;
		return true;
	}

	@Override
	public boolean remove(int index, int key) {
		if(!checkIndex(index))
			return false;
		ArrayList<Indexable> el=elenco.get(index);
		for(int i=0;i<el.size();i++){
			Indexable cur=el.get(i);
			if(cur.getKey()>key)
				return false;
			else if(cur.getKey()==key){
				el.remove(i);
				size--;
				return true;
			}
		}
		return false;
	}

	@Override
	public ArrayList<Indexable> get(int index, int key) {
		if(!checkIndex(index))
			return null;
		ArrayList<Indexable> found=new ArrayList<Indexable>();
		ArrayList<Indexable> el=elenco.get(index);
		for(int i=0;i<el.size();i++){
			Indexable cur=el.get(i);
			if(cur.getKey()==key){
				found.add(cur);
			}
			else if(cur.getKey()>key)
				return found;
		}
		return found;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public ArrayList<Indexable> get(int i) {
		if(!checkIndex(i))
			return null;
		return elenco.get(i);
	}

	@Override
	public int n_index() {
		return elenco.size();
	}

	@Override
	public Indexable get(int index, String offkey) {
		if(!checkIndex(index))
			return null;
		ArrayList<Indexable> list=elenco.get(index);
		for(int i=0;i<list.size();i++){
			if(list.get(i).getOffKey().compareToIgnoreCase(offkey)==0)
				return list.get(i);
		}
		return null;
	}

	@Override
	public void reset() {
		for(int i=0;i<elenco.size();i++){
			ArrayList<Indexable> el=elenco.get(i);
			el.clear();
		}
		elenco.clear();
		size=0;
	}

	@Override
	public Indexable get(String offkey) {
		for(int i=0;i<elenco.size();i++){
			ArrayList<Indexable> el=elenco.get(i);
			for(int j=0;j<el.size();j++)
				if(el.get(j).getOffKey().compareToIgnoreCase(offkey)==0)
					return el.get(j);
		}
		return null;
	}
}
