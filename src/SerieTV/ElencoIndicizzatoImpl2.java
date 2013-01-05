package SerieTV;

import java.util.ArrayList;

public class ElencoIndicizzatoImpl2 implements ElencoIndicizzato {
	private class INDEX {
		private int index;
		private ArrayList<Indexable> list;
		
		public INDEX(int i){
			list=new ArrayList<Indexable>();
			this.index=i;
		}
		public int getIndex(){
			return index;
		}
		public ArrayList<Indexable> getList(){
			return list;
		}
	}
	private ArrayList<INDEX> elenco;
	public ElencoIndicizzatoImpl2() {
		elenco=new ArrayList<INDEX>();
	}

	private boolean checkIndex(int index){
		for(int i=0;i<elenco.size();i++)
			if(elenco.get(i).getIndex()==index)
				return true;
		return false;
	}
	private void addIndex(int index){
		for(int i=0;i<elenco.size();i++){
			if(index<elenco.get(i).getIndex()){
				elenco.add(i, new INDEX(index));
				return;
			}
		}
		elenco.add(new INDEX(index));
	}
	private INDEX getIndex(int index){
		for(int i=0;i<elenco.size();i++)
			if(elenco.get(i).getIndex()==index)
				return elenco.get(i);
		return null;
	}
	public boolean add(Indexable e) {
		if(e==null)
			return false;
		
		if(!checkIndex(e.getIndex()))
			addIndex(e.getIndex());
		ArrayList<Indexable> el=getIndex(e.getIndex()).getList();
		for(int i=0;i<el.size();i++){
			Indexable cur=el.get(i);
			if(e.getKey()<cur.getKey()){
				el.add(i, e);
				return true;
			}
			if(e.getOffKeyTrim().compareTo(cur.getOffKeyTrim())==0){
				if(cur.getOffKey().length()<e.getOffKey().length())
					cur.setOffKey(e.getOffKey());
				return true;
			}
		}
		el.add(e);
		return true;
	}

	@Override
	public boolean remove(Indexable e) {
		if(checkIndex(e.getIndex())){
			ArrayList<Indexable> el=getIndex(e.getIndex()).getList();
			return el.remove(e);
		}
		return false;
	}

	@Override
	public Indexable get(String offkey) {
		String offkey_trim=offkey;
		if(offkey.contains("&dn"))
			offkey_trim=offkey.substring(0, offkey.indexOf("&dn")).toLowerCase();
		ArrayList<Indexable> el=getLinear();
		for(int i=0;i<el.size();i++){
			Indexable index=el.get(i);
			if(index.getOffKeyTrim().compareTo(offkey_trim)==0){
				return index;
			}
		}
		return null;
	}

	@Override
	public ArrayList<Indexable> getLinear() {
		ArrayList<Indexable> linear=new ArrayList<Indexable>();
		for(int i=0;i<elenco.size();i++){
			ArrayList<Indexable> el=elenco.get(i).getList();
			for(int j=0;j<el.size();j++)
				linear.add(el.get(j));
		}
		return linear;
	}

	@Override
	public Indexable get(Indexable e) {
		if(!checkIndex(e.getIndex()))
			return null;
		ArrayList<Indexable> el=getIndex(e.getIndex()).getList();
		for(int i=0;i<el.size();i++){
			Indexable index=el.get(i);
			if(index.getOffKeyTrim().compareTo(e.getOffKeyTrim())==0){
				if(e.getOffKey().length()>index.getOffKey().length())
					index.setOffKey(e.getOffKey());
				return index;
			}
		}
		
		return null;
	}

	public ArrayList<Indexable> get(int index) {
		if(checkIndex(index))
			return getIndex(index).getList();
		return null;
	}

	@Override
	public Integer[] getIndexes() {
		Integer[] indici=new Integer[elenco.size()];
		
		for(int i=0;i<elenco.size();i++)
			indici[i]=elenco.get(i).getIndex();
		return indici;
	}

	@Override
	public void removeAll() {
		for(int i=0;i<elenco.size();i++)
			getIndex(elenco.get(i).getIndex()).getList().clear();
		elenco.clear();
	}

}
