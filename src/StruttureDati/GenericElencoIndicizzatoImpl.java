package StruttureDati;

import java.util.ArrayList;

public class GenericElencoIndicizzatoImpl implements GenericElencoIndicizzato {
	class INDEX {
		private int index;
		private ArrayList<Indexable> elementi;
		
		public INDEX(int index){
			this.index=index;
			elementi=new ArrayList<Indexable>();
		}
		public int getIndex(){
			return index;
		}
		public ArrayList<Indexable> getElements(){
			return elementi;
		}
	}
	private ArrayList<INDEX> key_index;
	public GenericElencoIndicizzatoImpl() {
		key_index=new ArrayList<INDEX>(1);
	}

	@Override
	public void add(Indexable e) {
		INDEX index=getIndex(e.getIndex());
		if(index==null){
			index=new INDEX(e.getIndex());
			key_index.add(index);
		}
		ArrayList<Indexable> elem=index.getElements();
		if(elem.isEmpty())
			elem.add(e);
		else{
			for(int i=0;i<elem.size();i++)
				if(e.getIndex()<elem.get(i).getIndex()){
					elem.add(i, e);
					return;
				}
			elem.add(e);
		}
	}

	@Override
	public void remove(Indexable e) {
		INDEX index=getIndex(e.getIndex());
		if(index!=null)
			index.getElements().remove(e);
	}

	@Override
	public Indexable get(int key, int off) {
		INDEX index=getIndex(key);
		if(index!=null){
			ArrayList<Indexable> elem=index.getElements();
			for(int i=0;i<elem.size();i++){
				if(elem.get(i).getIndex()==off)
					return elem.get(i);
			}
		}
		return null;
	}
	private INDEX getIndex(int ind){
		for(int i=0;i<key_index.size();i++)
			if(key_index.get(i).getIndex()==ind)
				return key_index.get(i);
		return null;
	}
}
