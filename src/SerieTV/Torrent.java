package SerieTV;

import Database.*;

public class Torrent implements Indexable{
	public final static int SCARICARE=0, SCARICATO=1, VISTO=2, RIMOSSO=3, IGNORATO=4; 
	private int 	id_serie;
	private String	url;
	private String	nomeserie;
	private int		scaricato; //0 non scaricato - 1 scaricato - 2 visto - 3 rimosso - 4 ignorato
	private int		serie;
	private int		puntata;
	private boolean	HD720p;
	private boolean	repack;
	private boolean preair;
	private boolean proper;
	private boolean sub_down; //true se è da scaricare, false non scaricare

	public Torrent(String url, int visto, String nome_serie, int id_serie) {
		this.url=url;
		scaricato=visto;
		this.setIdSerie(id_serie);
		this.nomeserie = nome_serie;
		this.serie = 0;
		this.puntata = 0;
	}
	public boolean isPreAir(){
		return preair;
	}
	public boolean isPROPER(){
		return proper;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isScaricato() {
		return scaricato!=SCARICARE;
	}
	
	public void setScaricato(int visto) {
		this.scaricato = visto;
		update();
	}
	public void setScaricato(int visto, boolean update){
		this.scaricato = visto;
		if(update){
			update();
		}
	}

	public boolean is720p() {
		return this.HD720p;
	}

	public int getSerie() {
		return this.serie;
	}

	public int getPuntata() {
		return this.puntata;
	}

	public String getNomeSerie() {
		return this.nomeserie;
	}

	public boolean isRepack() {
		return this.repack;
	}

	public String getName() {
		String nome = this.url.substring(this.url.indexOf("&dn"), this.url.indexOf("&tr"));
		nome = nome.substring(nome.indexOf("=") + 1);
		return nome;
	}

	public void parseMagnet() {
		int prova = 0;

		this.repack = this.url.contains("REPACK");
		this.HD720p = this.url.contains("720p");
		this.preair = this.url.contains("PREAIR");
		this.proper = this.url.contains("PROPER");

		char[] serie = new char[5];
		char[] puntata = new char[15];

		//PARSE NUMERI
		int i = 0;
		int j = 0;
		int z = 0;
		int step = 0;
		int err = 0;
		boolean cont = false;
		do {
			i = 0;
			j = 0;
			z = 0;
			step = 0;
			err = 0;
			String nome = this.url.substring(this.url.indexOf("&dn"), this.url.indexOf("&tr"));

			int cut = getCut(this.nomeserie);
			if (cut == -1) {
				this.serie = 1;
				this.puntata = 1;
				return;
			}

			nome = nome.substring(nome.indexOf("=") + cut - prova);

			while (i < nome.length()) {
				switch (nome.charAt(i)) {
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						switch (step) {
							case 0:
								if ((j > 0) && (err > 0)) {
									step++;
									cont = true;
								}
								else {
									if ((j == 0) && (nome.charAt(i) == '0'))
										break;
									serie[(j++)] = nome.charAt(i);
								}
								break;
							case 1:
								if ((z > 0) && (err > 0)) {
									step++;
								}
								else {
									if ((z == 0) && (nome.charAt(i) == '0'))
										break;
									puntata[(z++)] = nome.charAt(i);
								}
						}
						if (cont) {
							cont = false;
							continue;
						}
						err = 0;
						break;
					default:
						err++;
				}
				i++;
			}
			prova++;
		} while ((prova < this.nomeserie.length() / 2) && ((z == 0) || (j == 0)));
		String Serie = String.copyValueOf(serie, 0, j);
		try {
			this.serie = Integer.parseInt(Serie);
		}
		catch (NumberFormatException e) {
			this.serie = 0;
		}
		String Puntata = String.copyValueOf(puntata, 0, z);
		try {
			this.puntata = Integer.parseInt(Puntata);
		}
		catch (NumberFormatException e) {
			this.puntata = 0;
		}
		//FINE PARSE NUMERI
	}

	private int getCut(String nomeserie) {
		int cut = nomeserie.length() + 1;
		
		String parentesi="";
		String parentesi_content="";
		if(nomeserie.contains("(") && nomeserie.contains(")")){
			parentesi=nomeserie.substring(nomeserie.indexOf("("), nomeserie.indexOf(")")+1);
			parentesi_content=parentesi.substring(parentesi.indexOf("(")+1,parentesi.indexOf(")"));
		}

		switch (nomeserie) {
			case "CSI: Crime Scene Investigation":
				cut = 3;
				break;
			case "Merlin":
				if (url.contains("2008"))
					cut = nomeserie.length() + 6;
				else
					cut = nomeserie.length();
				break;
			case "Archer (2009)":
				if (!url.contains("2009")) {
					cut = nomeserie.length() - 6;
				}
				break;
			case "Beyond the Game":
				cut = -1;
				break;
			case "Big Brother (US)":
				cut = nomeserie.length() - 3;
				break;
			case "Doctor Who":
				if (url.contains("2005"))
					cut = nomeserie.length() + 7;
				else if (url.contains("Confidential"))
					cut = nomeserie.length() + "Confidential".length() + 3;
				break;
			case "Don't Trust the B---- in Apartment 23":
				cut = "Apartment 23".length() + 1;
				break;
			default:
				if(url.contains(parentesi))
					return cut;
				else if(url.contains(parentesi_content)){
					return cut-2;
				}
				else{
					nomeserie=nomeserie.substring(0, nomeserie.indexOf(parentesi)).trim();
					return nomeserie.length();
				}
		}
		return cut;
	}

	public String getNomeSerieFolder() {
		return nomeserie.replace(":", "-").replace("?", "").replace("/", "-").replace("\\", "-").replace("*", "").replace("<", "").replace(">", "").replace("|", "").replace("\"", "");
	}
	
	public int getIdSerie() {
		return id_serie;
	}
	public void setIdSerie(int id_serie) {
		this.id_serie = id_serie;
	}
	public void setSottotitolo(boolean stato, boolean update) {
		sub_down=stato;
		if(stato)
			GestioneSerieTV.getSubManager().aggiungiEpisodio(this);
		else
			GestioneSerieTV.getSubManager().rimuoviEpisodio(this);
		if(update)
			update();
	}
	public boolean isSottotitolo(){
		return sub_down;
	}
	public void set720p(boolean stato) {
		HD720p=stato;
	}
	public void setRepack(boolean stato) {
		repack=stato;
	}
	public void setProper(boolean stato) {
		proper=stato;
	}
	public void setStagione(int stato) {
		serie=stato;
	}
	public void setEpisodio(int stato) {
		puntata=stato;
	}
	public void setPreair(boolean stato) {
		preair=stato;
	}
	public void insert(){
		SQLParameter[] par=new SQLParameter[10];
		par[0]=new SQLParameter(SQLParameter.TEXT, getUrl(), "magnet");
		par[1]=new SQLParameter(SQLParameter.INTEGER, getIdSerie(), "id_serie");
		par[2]=new SQLParameter(SQLParameter.INTEGER, scaricato, "vista");
		par[3]=new SQLParameter(SQLParameter.INTEGER, getSerie(), "serie");
		par[4]=new SQLParameter(SQLParameter.INTEGER, getPuntata(), "episodio");
		par[5]=new SQLParameter(SQLParameter.INTEGER, is720p()?1:0, "HD720p");
		par[6]=new SQLParameter(SQLParameter.INTEGER, isRepack()?1:0, "repack");
		par[7]=new SQLParameter(SQLParameter.INTEGER, isPreAir()?1:0, "preair");
		par[8]=new SQLParameter(SQLParameter.INTEGER, isPROPER()?1:0, "proper");
		par[9]=new SQLParameter(SQLParameter.INTEGER, isSottotitolo()?1:0, "sottotitolo");
		Database.insert(Database.TABLE_TORRENT, par);
	}
	public void update(){
		int i=0;
		SQLParameter[] par=new SQLParameter[9];
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getIdSerie(), "id_serie");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getScaricato(), "vista");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getSerie(), "serie");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, getPuntata(), "episodio");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, is720p()?1:0, "HD720p");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isRepack()?1:0, "repack");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isPreAir()?1:0, "preair");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isPROPER()?1:0, "proper");
		par[i++]=new SQLParameter(SQLParameter.INTEGER, isSottotitolo()?1:0, "sottotitolo");
		
		SQLParameter[] con=new SQLParameter[1];
		con[0]=new SQLParameter(SQLParameter.TEXT, getUrl(), "magnet");
		
		Database.update(Database.TABLE_TORRENT, par, con, "AND", "=");
	}
	public String toString(){
		return getNomeSerie()+" "+getSerie()+"x"+getPuntata();
	}
	public int getScaricato(){
		return scaricato;
	}
	
	public void setOffKey(String key) {
		setUrl(key);
	}
	public int getIndex() {
		return serie;
	}
	public int getKey() {
		return puntata;
	}
	public String getOffKey(){
		return url;
	}
	@Override
	public String getOffKeyTrim() {
		if(getUrl().contains("&dn"))
			return getUrl().substring(0, getUrl().indexOf("&dn")).toLowerCase();
		else
			return getUrl();
	}
}
