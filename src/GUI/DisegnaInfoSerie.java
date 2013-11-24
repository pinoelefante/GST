package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import InfoManager.SerieTVDB;
import InfoManager.TheTVDB;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.SerieTV;

public class DisegnaInfoSerie extends Thread {
	private SerieTV serie;
	
	private static JInfoPanel panel_info_episodio;
	
	public DisegnaInfoSerie(SerieTV serie){
		this.serie=serie;
	}
	public static void setPanelInfo(JInfoPanel p){
		panel_info_episodio=p;
	}
	public static boolean isPanelSet(){
		return panel_info_episodio!=null;
	}
	private JLabel banner, descrizione;
	private JPanel nord, centro;
	public void run(){
		
		if(!isPanelSet() || panel_info_episodio.isLocked()){
			System.out.println("Pannello bloccato");
			//TODO cosa fare se il pannello delle informazioni è bloccato
		}
		else {
			try{
				panel_info_episodio.setLocked(true);
				panel_info_episodio.removeAll();
				panel_info_episodio.revalidate();
				panel_info_episodio.repaint();
				
				banner=new JLabel("Download delle informazioni in corso...");
				
				nord=new JPanel();
				((FlowLayout)nord.getLayout()).setAlignment(FlowLayout.CENTER);
				nord.add(banner);
				panel_info_episodio.add(nord, BorderLayout.NORTH);
				
				panel_info_episodio.revalidate();
				panel_info_episodio.repaint();
				
				SerieTVDB serie=TheTVDB.getSeries(this.serie);
				if(serie==null){
					banner.setText("Informazioni sulla serie non trovate");
				}
				else {
					disegna(serie);
				}
				panel_info_episodio.setLocked(false);
			}
			catch(Exception e){
				e.printStackTrace();
				panel_info_episodio.setLocked(false);
			}
		}
	}
	public void disegna(SerieTVDB serie){
		getBanner(banner, serie.getUrlBanner());
		centro=new JPanel();
		((FlowLayout)centro.getLayout()).setAlignment(FlowLayout.LEFT);
		String genere="";
		if(serie.getGeneri()!=null){
			for(int i=0;i<serie.getGeneri().size();i++){
				genere+=serie.getGeneri().get(i)+" ";
			}
		}
		descrizione=new JLabel("<html><font size=3>Titolo: <b>"+serie.getNomeSerie()+"</b><br>"
				+ "Messa in onda: <b>"+serie.getDataInizio()+"</b><br>"
				+ "Genere: <b>"+genere+"</b><br>"
				+ "Network: <b>"+(serie.getNetwork()==null?"":serie.getNetwork())+"</b><br>"
				+ "Valutazione: <b>"+(serie.getRating()==0.0?"S.V.":serie.getRating())+"</b><br>"
				+ "Trama: <b>"+formattaDescrizione(serie.getDescrizione())+"</b><br>"
				+ "</font></html>");
		centro.add(descrizione);
		
		panel_info_episodio.add(centro, BorderLayout.CENTER);
		panel_info_episodio.revalidate();
		panel_info_episodio.repaint();
	}
	public void getBanner(JLabel banner, String url_banner){
		try {
			if(url_banner!=null){
				
//				String url_img=TheTVDB.getUrlBanner(url_banner);
//				if(url_img!=null)
//					Resource.setImage(banner, url_img, 320);
//				else
//					banner.setText("Banner non disponibile");
				
				
				if(OperazioniFile.fileExists(Settings.getDirectoryDownload()+"tvdb"+File.separator+url_banner.replace("/", File.separator))){
					Resource.setImage(banner, Settings.getDirectoryDownload()+"tvdb"+File.separator+url_banner.replace("/", File.separator), 320);
				}
				else {
					if(TheTVDB.scaricaBanner(Settings.getDirectoryDownload()+"tvdb", url_banner))
						Resource.setImage(banner, Settings.getDirectoryDownload()+"tvdb"+File.separator+url_banner.replace("/", File.separator), 320);
					else
						banner.setText("Banner non disponibile");
				}
			}
			else
				banner.setText("Banner non disponibile");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String formattaDescrizione(String descrizione){
		String[] dotSelect=descrizione.split("[.]");
		String descr="";
		String line="";
		for(int i=0;i<dotSelect.length;i++){
			String[] spaceSelect=dotSelect[i].split(" ");
			for(int j=0;j<spaceSelect.length;j++){
				if(spaceSelect[j].length()+line.length()>40){
					line+=" "+spaceSelect[j];
					if(j==spaceSelect.length-1)
						line+=".";
					descr+=line+"<br>";
					line="";
				}
				else {
					line+=" "+spaceSelect[j];
					if(j==spaceSelect.length-1){
						line+=".<br>";
						descr+=line;
						line="";
					}
				}
			}
			
		}
		return descr;
	}
}