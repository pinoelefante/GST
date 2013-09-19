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

import SerieTV.SerieTV;
import SerieTV.Torrent;
import StruttureDati.serietv.Episodio;

import javax.swing.border.EtchedBorder;

public class PanelPreferenze extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private SerieTV serie;

	private JButton btnSalva;

	private JCheckBox chckbxScaricaHd;

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
		
		chckbxScaricaHd = new JCheckBox("Scarica HD (se disponibile)");
		chckbxScaricaHd.setSelected(serie.getPreferenze().isPreferisciHD());
		panel_1.add(chckbxScaricaHd);
		
		chckbxScaricaPreair = new JCheckBox("Scarica Preair (se disponibile)");
		chckbxScaricaPreair.setSelected(serie.getPreferenze().isDownloadPreair());
		panel_1.add(chckbxScaricaPreair);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setVgap(10);
		add(panel_2, BorderLayout.EAST);
		
		btnSalva = new JButton("Salva");
		panel_2.add(btnSalva);
		
		addListener();
	}
	public SerieTV getSerie(){
		return serie;
	}
	private void addListener(){
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean last_hd=serie.getPreferenze().isPreferisciHD();
				boolean last_preair=serie.getPreferenze().isDownloadPreair();
				serie.getPreferenze().setDownloadPreair(chckbxScaricaPreair.isSelected());
				serie.getPreferenze().setPreferisciHD(chckbxScaricaHd.isSelected());
				serie.aggiornaDB();
				
				if(last_hd!=chckbxScaricaHd.isSelected() || last_preair!=chckbxScaricaPreair.isSelected()){
					for(int i=0;i<serie.getNumEpisodi();i++){
						serie.getEpisodio(i).setDownloadableFirst(Episodio.INDEX_HD, chckbxScaricaHd.isSelected()?Torrent.IGNORATO:Torrent.SCARICARE, chckbxScaricaHd.isSelected()?Torrent.SCARICARE:Torrent.IGNORATO);
						serie.getEpisodio(i).setDownloadableFirst(Episodio.INDEX_PRE, chckbxScaricaPreair.isSelected()?Torrent.IGNORATO:Torrent.SCARICARE, chckbxScaricaPreair.isSelected()?Torrent.SCARICARE:Torrent.IGNORATO);
					}
					Interfaccia.getInterfaccia().inizializzaDownloadScroll();
				}
			}
		});
	}
}