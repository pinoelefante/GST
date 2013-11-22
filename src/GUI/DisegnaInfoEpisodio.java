package GUI;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import InfoManager.TheTVDB;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.Torrent;

public class DisegnaInfoEpisodio extends Thread {
	private Torrent torrent;
	private static JInfoPanel panel;
	
	public DisegnaInfoEpisodio(Torrent t){
		this.torrent=t;
	}
	public void run(){
		if(isPanelSet() && !panel.isLocked()){
			disegna();
		}
		else {
			JLabel error=new JLabel("Panel bloccato");
			panel.add(error);
		}
	}
	public static void setInfoPanel(JInfoPanel p){
		panel=p;
	}
	public static boolean isPanelSet(){
		return panel!=null;
	}
	public void disegna() {
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
		
		JPanel nord=new JPanel();
		JLabel mainscreen=new JLabel("<html><b>"+torrent.getNomeSerie()+" Stagione: "+torrent.getStagione()+" Episodio: "+torrent.getEpisodio()+"</b></html>");
		nord.add(mainscreen);
		panel.add(nord, BorderLayout.NORTH);
		
		JPanel centro=new JPanel(); 
	}
	public void getScreen(JLabel banner, String url_banner){
		try {
			if(url_banner!=null){
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
