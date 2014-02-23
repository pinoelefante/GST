package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import Programma.Settings;
import SerieTV.Preferenze;
import SerieTV.SerieTV;
import SerieTV.Torrent;
import StruttureDati.serietv.Episodio;

import javax.swing.border.EtchedBorder;

public class PanelPreferenze extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private SerieTV serie;

	private JButton btnSalva;
	private JButton btnDefault;

	private JCheckBox chckbxScaricaHd;
	private JCheckBox chckbxScaricaTutto;
	private JCheckBox chckbxScaricaPreair;

	public PanelPreferenze(SerieTV s) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		serie=s;
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		JLabel lblnomeserie = new JLabel("<html><b>"+serie.getNomeSerie()+"</b></html>");
		lblnomeserie.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(lblnomeserie);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setVgap(10);
		add(panel_1, BorderLayout.CENTER);
		
		chckbxScaricaTutto=new JCheckBox("Scarica tutto");
		chckbxScaricaTutto.setSelected(serie.getPreferenze().isScaricaTutto());
		panel_1.add(chckbxScaricaTutto);
		
		chckbxScaricaHd = new JCheckBox("Scarica HD (se disponibile)");
		chckbxScaricaHd.setSelected(serie.getPreferenze().isPreferisciHD());
		panel_1.add(chckbxScaricaHd);
		
		chckbxScaricaPreair = new JCheckBox("Scarica Preair (se disponibile)");
		chckbxScaricaPreair.setSelected(serie.getPreferenze().isDownloadPreair());
		panel_1.add(chckbxScaricaPreair);
		
		if(serie.getPreferenze().isScaricaTutto()){
			chckbxScaricaPreair.setEnabled(false);
			chckbxScaricaHd.setEnabled(false);
		}
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setVgap(10);
		add(panel_2, BorderLayout.EAST);
		
		btnSalva = new JButton("Salva");
		btnSalva.setEnabled(false);
		panel_2.add(btnSalva);
		
		btnDefault=new JButton("Default");
		panel_2.add(btnDefault);
		
		addListener();
	}
	public SerieTV getSerie(){
		return serie;
	}
	private void addListener(){
		btnDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Preferenze p=new Preferenze(Settings.getRegolaDownloadDefault());
				serie.setPreferenze(p);
				serie.aggiornaDB();
				chckbxScaricaHd.setSelected(p.isPreferisciHD());
				chckbxScaricaPreair.setSelected(p.isDownloadPreair());
				chckbxScaricaTutto.setSelected(p.isScaricaTutto());
				btnSalva.getActionListeners()[0].actionPerformed(new ActionEvent(btnSalva, 0, ""));
			}
		});
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSalva.setEnabled(false);
				boolean last_hd=serie.getPreferenze().isPreferisciHD();
				boolean last_preair=serie.getPreferenze().isDownloadPreair();
				//boolean last_tutto=serie.getPreferenze().isScaricaTutto();
				serie.getPreferenze().setDownloadPreair(chckbxScaricaPreair.isSelected());
				serie.getPreferenze().setPreferisciHD(chckbxScaricaHd.isSelected());
				serie.getPreferenze().setScaricaTutto(chckbxScaricaTutto.isSelected());
				serie.aggiornaDB();
				
				if(serie.getPreferenze().isScaricaTutto()){
					for(int i=0;i<serie.getNumEpisodi();i++){
						serie.getEpisodio(i).setDownloadableFirst(Episodio.INDEX_HD, Torrent.IGNORATO, Torrent.SCARICARE);
						serie.getEpisodio(i).setDownloadableFirst(Episodio.INDEX_SD, Torrent.IGNORATO, Torrent.SCARICARE);
						serie.getEpisodio(i).setDownloadableFirst(Episodio.INDEX_PRE, Torrent.IGNORATO, Torrent.SCARICARE);
					}
				}
				else {
					if(last_hd!=chckbxScaricaHd.isSelected() || last_preair!=chckbxScaricaPreair.isSelected()){
						for(int i=0;i<serie.getNumEpisodi();i++){
							serie.getEpisodio(i).setDownloadableFirst(Episodio.INDEX_HD, chckbxScaricaHd.isSelected()?Torrent.IGNORATO:Torrent.SCARICARE, chckbxScaricaHd.isSelected()?Torrent.SCARICARE:Torrent.IGNORATO);
							serie.getEpisodio(i).setDownloadableFirst(Episodio.INDEX_PRE, chckbxScaricaPreair.isSelected()?Torrent.IGNORATO:Torrent.SCARICARE, chckbxScaricaPreair.isSelected()?Torrent.SCARICARE:Torrent.IGNORATO);
						}
						Interfaccia.getInterfaccia().inizializzaDownloadScroll();
					}
				}
			}
		});
		chckbxScaricaHd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abilitaTastoSalva();
			}
		});
		chckbxScaricaPreair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abilitaTastoSalva();
			}
		});
		chckbxScaricaTutto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abilitaTastoSalva();
				if(chckbxScaricaTutto.isSelected()){
					chckbxScaricaHd.setEnabled(false);
					chckbxScaricaPreair.setEnabled(false);
					chckbxScaricaHd.setSelected(true);
					chckbxScaricaPreair.setSelected(true);
				}
				else {
					chckbxScaricaHd.setEnabled(true);
					chckbxScaricaPreair.setEnabled(true);
				}
				
			}
		});
	}
	private void abilitaTastoSalva(){
		boolean stato_hd=serie.getPreferenze().isPreferisciHD();
		boolean stato_pre=serie.getPreferenze().isDownloadPreair();
		boolean stato_all=serie.getPreferenze().isScaricaTutto();
		if(stato_hd!=chckbxScaricaHd.isSelected() || stato_pre!=chckbxScaricaPreair.isSelected() || stato_all!=chckbxScaricaTutto.isSelected())
			btnSalva.setEnabled(true);
		else
			btnSalva.setEnabled(false);
	}
}
