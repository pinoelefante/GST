package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import InfoManager.SerieTVDB;
import InfoManager.TheTVDB;
import Programma.Download;
import Programma.ManagerException;
import Programma.Settings;
import SerieTV.Torrent;
import StruttureDati.serietv.Episodio;

public class PanelEpisodioDownload extends JPanel {
	
	private static JInfoPanel panel_info;
	private static final long serialVersionUID = 1L;
	private Episodio ep;
	private JButton btnHd;
	private JButton btnSd;
	private JButton btnPreair;
	private JButton btnInfo;
	private JCheckBox chckbxnomeserie;
	private JButton btnX;
	
	public static void setPanelInfo(JInfoPanel p){
		panel_info=p;
	}
	public PanelEpisodioDownload(Episodio e) {
		ep=e;
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setSize(375,95);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5, BorderLayout.WEST);
		
		chckbxnomeserie = new JCheckBox("<html><b>"+ep.getSerieTV().getNomeSerie()+"</b></html>");
		panel_5.add(chckbxnomeserie);
		chckbxnomeserie.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxnomeserie.setSelected(true);
		
		JPanel panel_6 = new JPanel();
		panel.add(panel_6, BorderLayout.EAST);
		
		btnX = new JButton("");
		btnX.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/Xclose.png")));
		panel_6.add(btnX);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		btnInfo = new JButton("");
		btnInfo.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/info.png")));
		//TODO abilitare quando verrà creata la classe per TheTVDB
		//btnInfo.setEnabled(false);
		panel_3.add(btnInfo);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		
		btnHd = new JButton("HD");
		btnHd.setEnabled(false);
		Torrent t_hd=ep.getLinkHD();
		if(t_hd!=null){
			if(t_hd.getScaricato()==Torrent.SCARICARE)
			btnHd.setEnabled(true);
		}
		panel_2.add(btnHd);
		
		btnSd = new JButton("SD");
		Torrent t_sd=ep.getLinkNormale();
		btnSd.setEnabled(false);
		if(t_sd!=null){
			if(t_sd.getScaricato()==Torrent.SCARICARE)
				btnSd.setEnabled(true);
		}
		panel_2.add(btnSd);
		
		btnPreair = new JButton("PreAir");
		btnPreair.setEnabled(false);
		Torrent t_pre=ep.getLinkPreair();
		if(t_pre!=null){
			if(t_pre.getScaricato()==Torrent.SCARICARE)
				btnPreair.setEnabled(true);
		}
		panel_2.add(btnPreair);
		
		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.WEST);
		
		JLabel lblStagione = new JLabel("<html>Stagione: </html>");
		panel_4.add(lblStagione);
		
		JLabel lblSeason = new JLabel("<html><b>"+ep.getStagione()+"</b></html>");
		panel_4.add(lblSeason);
		
		JLabel lblEpisodio = new JLabel("<html>Episodio:</html>");
		panel_4.add(lblEpisodio);
		
		JLabel lblEpisode = new JLabel("<html><b>"+ep.getEpisodio()+"</html></b>");
		panel_4.add(lblEpisode);
		
		addListener();
	}
	public boolean isDownloadSelected(){
		return chckbxnomeserie.isSelected();
	}
	public void setSelected(boolean state){
		chckbxnomeserie.setSelected(state);
	}
	public Torrent getLink(){
		Torrent t=ep.getLinkDownload();
		return t;
	}
	public void scarica(Torrent link){
		ep.scaricaLink(link);
	}
	public Episodio getEpisodio(){
		return ep;
	}
	private void addListener(){
		btnHd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Torrent link=ep.getLinkHD();
				if(link!=null){
					try {
						Download.downloadMagnet(link.getUrl(), Settings.getDirectoryDownload()+File.separator+link.getSerieTV().getFolderSerie());
						ep.scaricaLink(link);
						PanelEpisodioDownload.this.getParent().remove(PanelEpisodioDownload.this);
					}
					catch (IOException e1) {
						ManagerException.registraEccezione(e1);
						e1.printStackTrace();
					}
				}
			}
		});
		btnSd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Torrent link=ep.getLinkNormale();
				if(link!=null){
					try {
						Download.downloadMagnet(link.getUrl(), Settings.getDirectoryDownload()+File.separator+link.getSerieTV().getFolderSerie());
						ep.scaricaLink(link);
						PanelEpisodioDownload.this.getParent().remove(PanelEpisodioDownload.this);
					}
					catch (IOException e1) {
						ManagerException.registraEccezione(e1);
						e1.printStackTrace();
					}
				}
			}
		});
		btnPreair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Torrent link=ep.getLinkPreair();
				if(link!=null){
					try {
						Download.downloadMagnet(link.getUrl(), Settings.getDirectoryDownload()+File.separator+link.getSerieTV().getFolderSerie());
						ep.scaricaLink(link);
						PanelEpisodioDownload.this.getParent().remove(PanelEpisodioDownload.this);
					}
					catch (IOException e1) {
						ManagerException.registraEccezione(e1);
						e1.printStackTrace();
					}
				}
			}
		});
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				class disegnaInfo extends Thread {
					public void run(){
						if(panel_info.isLocked()){
							//TODO cosa fare se il pannello delle informazioni è bloccato
						}
						else {
							panel_info.setLocked(true);
							panel_info.removeAll();
							
							JLabel banner=new JLabel("Download delle informazioni in corso...");
							/*
							JLabel l_titolo=new JLabel("Titolo: "), titolo=new JLabel(),
								   l_data=new JLabel("Data inizio:"), data=new JLabel(),
								   l_descr=new JLabel("Descrizione: "), descr=new JLabel(),
								   l_attori=new JLabel("Attori: "), attori=new JLabel();
							*/
							JPanel nord=new JPanel();
							((FlowLayout)nord.getLayout()).setAlignment(FlowLayout.CENTER);
							nord.add(banner);
							
							ArrayList<SerieTVDB> series=TheTVDB.getSeries(ep.getNomeSerie());
							SerieTVDB serie=series.get(0);
							
							JPanel centro=new JPanel();
							JLabel label=new JLabel("<html>Titolo: <b>"+serie.getNomeSerie()+"</b><br>"
									+ "Messa in onda: <b>"+serie.getDataInizio()+"</b><br>"
									+ "Trama: <b>"+serie.getDescrizione()+"</b><br>"
									+ "</html>");
							centro.add(label);
							
							panel_info.add(nord, BorderLayout.NORTH);
							panel_info.add(centro, BorderLayout.CENTER);
							panel_info.revalidate();
							panel_info.repaint();
							
							panel_info.setLocked(false);
						}
					}
				}
				Thread t=new disegnaInfo();
				t.start();
			}
		});
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ep.ignoraEpisodio();
				PanelEpisodioDownload.this.getParent().remove(PanelEpisodioDownload.this);
			}
		});
	}
}
