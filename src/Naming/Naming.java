package Naming;

public class Naming {
	/*
	 * S00E00 / s00e00
	 * 0x00 / 00x00 y
	 * 0.0			y
	 * Part 0 - singola serie
	 * 0of0 -singola serie
	 * s00 / subsfactory - stagione completa
	 * Stagione 0 Completa / itasa - stagione completa
	 */
	
	public static CaratteristicheFile parseMagnetName(String nome){
		return null;
	}
	private void retrieveSE(String url, int tri){
		String[] regex_s_e={
				"[0-9]{1,}x[0-9]{1,}",						// 1x02
				"[0-9]{1,}.[0-9]{1,}",						// x.y
				"[Ss][0-9]{1,}[Ee][0-9]{1,}",
				"[0-9]{1,}of[0-3]{1,}",
				"[^PpAaRrT][0-9]{1,}",	//trimmare prendere pure il numero
				"[Ss]eason [0-9]{1,2} [Ee]pisode [0-9]{1,2}",	// Season x Episode y
				"[Ss]eason [0-9]{1,2}"							// Season x Promo
		};
		
		if(tri==regex_s_e.length){
			setSerie(0);
			setEpisodio(0);
			return;
		}
		else {
			String regex=regex_s_e[tri];
			String[] s_e=null;
			String[] splitted=null;
			String result=null;
			switch(tri){
				case 0:
					splitted=url.split(regex);
					result=regex_splittered(url, splitted);
					s_e=result.split("x");
					break;
				case 1:
					splitted=getTitolo().split(regex);
					result=regex_splittered(getTitolo(), splitted);
					s_e=result.replace(".", "x").replace(" ", "x").split("x");
					break;
				case 2:
					splitted=getTitolo().split(regex);
					result=regex_splittered(getTitolo(), splitted);
					result=result.replace("Season ","").replace("Episode ", "").replace("season ","").replace("episode ", "");
					s_e=result.replace(" ", "x").split("x");
					break;
				case 3:
					splitted=getTitolo().split(regex);
					result=regex_splittered(getTitolo(), splitted);
					result=result.replace("Season ", "").replace("season ", "").trim();
					s_e=new String[2];
					//TODO controllare se contiente premier, teaser o finale
					s_e[0]=result;
					s_e[1]="1";
					break;
			}
			try{
				setSerie(Integer.parseInt(s_e[0].trim()));
				setEpisodio(Integer.parseInt(s_e[1].trim()));
			}
			catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
				setSerie(0);
				setEpisodio(0);
			}
			if(getSerie()==0 && getEpisodio()==0)
				retrieveSE(url, tri+1);
		}
	}
	private String regex_splittered(String origine, String[] split){
		String res=origine;
		for(int i=0;i<split.length;i++){
			res=res.replace(split[i], " ");
		}
		return res.trim();
	}
}
