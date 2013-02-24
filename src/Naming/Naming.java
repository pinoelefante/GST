package Naming;

import Programma.ManagerException;

public class Naming {
	/*
	 * S00E00 / s00e00 y
	 * 0x00 / 00x00 y y
	 * 0.0			y
	 * Part 0 - singola serie y
	 * Part_1 y
	 * 0of0 -singola serie y
	 * Series X YofZ
	 * s00 / subsfactory - stagione completa
	 * Stagione 0 Completa / itasa - stagione completa
	 */
	
	public static CaratteristicheFile parseMagnetName(String nome){
		return null;
	}
	private static CaratteristicheFile retrieveSE(String url){
		String[] regex_s_e={
			"[Ss][0-9]{1,}[Ee][0-9]{1,}",
			"[0-9]{1,}x[0-9]{1,}",						// 1x02
			"[0-9]{1,}.[0-9]{1,}",						// x.y
			"[Ss]eries\\S[0-9]{1,}\\S[0-9]{1,}of",
			"[Ss][0-9]{1,}",
			"[0-9]{1,}of[0-3]{1,}",
			"[Pp][Aa][Rr][Tt]\\S[0-9]{1,}",	// Part_X
			"[Pp][Aa][Rr][Tt]\\s[0-9]{1,}"  //PART X
				/*
				"[Ss]eason [0-9]{1,} [Ee]pisode [0-9]{1,}",	// Season x Episode y
				"[Ss]eason [0-9]{1,2}"							// Season x Promo
				*/
		};
		int num_prova=-1;
		for(int i=0;i<regex_s_e.length;i++){
			if(url.split(regex_s_e[i]).length>=2){
				num_prova=i;
				break;
			}	
		}
		if(num_prova==-1)
			return null;
		
		String regex=regex_s_e[num_prova];
		
		CaratteristicheFile cf=new CaratteristicheFile();
		if(url.toLowerCase().contains("720p"))
			cf.set720p(true);
		if(url.toUpperCase().contains("PROPER"))
			cf.setProper(true);
		if(url.toUpperCase().contains("REPACK"))
			cf.setRepack(true);
		
		String[] split=url.split(regex);
		String splitted=url;
		for(int i=0;i<split.length;i++){
			splitted=splitted.replace(split[i], " ");
		}
		System.out.println(splitted);
		String[] dati=null; 
		switch(num_prova){
			case 0:
				splitted=splitted.replace("S", "").replace("s", "").replace("E", "_").replace("e", "_");
				dati=splitted.split("_");
				break;
			case 1:
				dati=splitted.split("x");
				break;
			case 2:
				dati=splitted.split(".");
				break;
			case 3:
				//[Ss]eries\\S[0-9]{1,}of[0-9]{1,}
				splitted=splitted.toLowerCase();
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			default:
				cf.setStagione(0);
				cf.setEpisodio(0);
				return cf;
		}
		
		try {
			cf.setStagione(Integer.parseInt(dati[0]));
		}
		catch(NumberFormatException | NullPointerException e){
			e.printStackTrace();
			ManagerException.registraEccezione(e);
			cf.setStagione(0);
		}
		try {
			cf.setEpisodio(Integer.parseInt(dati[1]));
		}
		catch(NumberFormatException | NullPointerException e){
			e.printStackTrace();
			ManagerException.registraEccezione(e);
			cf.setEpisodio(0);
		}
		return cf;
	}
	public static void main(String[] args){
		
		System.out.println(retrieveSE("Discovery.Ch.River.Monsters.Series.3.10of10.The.Lost.Reels.Part.2.DVDrip.x264.AACmp4-MVGroup"));
	}
}
