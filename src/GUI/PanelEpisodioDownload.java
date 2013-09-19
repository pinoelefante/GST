package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import Programma.Download;
import Programma.ManagerException;
import Programma.Settings;
import SerieTV.Torrent;
import StruttureDati.serietv.Episodio;

public class PanelEpisodioDownload extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Episodio ep;
	private JButton btnHd;
	private JButton btnSd;
	private JButton btnPreair;
	private JButton btnInfo;
	private JCheckBox chckbxnomeserie;
	
	public PanelEpisodioDownload(Episodio e) {
		ep=e;
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setSize(375,95);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.NORTH);
		
		chckbxnomeserie = new JCheckBox("<html><b>"+ep.getNomeSerie()+"</b></html>");
		chckbxnomeserie.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxnomeserie.setSelected(true);
		panel.add(chckbxnomeserie);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		btnInfo = new JButton("");
		btnInfo.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/info.png")));
		panel_3.add(btnInfo);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		
		btnHd = new JButton("HD");
		Torrent t_hd=ep.getLinkHD();
		if(t_hd!=null){
			btnHd.setEnabled(t_hd.isScaricato()?false:true);
		}
		else
			btnHd.setEnabled(false);
		panel_2.add(btnHd);
		
		btnSd = new JButton("SD");
		Torrent t_sd=ep.getLinkNormale();
		if(t_sd!=null){
			btnSd.setEnabled(t_sd.isScaricato()?false:true);
		}
		else
			btnSd.setEnabled(false);
		panel_2.add(btnSd);
		
		btnPreair = new JButton("PreAir");
		Torrent t_pre=ep.getLinkPreair();
		if(t_pre!=null){
			btnPreair.setEnabled(t_pre.isScaricato()?false:true);
		}
		else
			btnPreair.setEnabled(false);
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
		return ep.getLinkDownload();
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
				// TODO Auto-generated method stub
				
			}
		});
	}
}
