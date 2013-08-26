package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import StruttureDati.serietv.Episodio;

public class PanelEpisodioDownload extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Episodio ep;
	
	public PanelEpisodioDownload(Episodio e) {
		ep=e;
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setSize(375,95);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.NORTH);
		
		JCheckBox chckbxnomeserie = new JCheckBox("<html><b>"+ep.getNomeSerie()+"</b></html>");
		chckbxnomeserie.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxnomeserie.setSelected(true);
		panel.add(chckbxnomeserie);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		JButton btnInfo = new JButton("");
		btnInfo.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/info.png")));
		panel_3.add(btnInfo);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		
		JButton btnHd = new JButton("HD");
		panel_2.add(btnHd);
		
		JButton btnSd = new JButton("SD");
		panel_2.add(btnSd);
		
		JButton btnPreair = new JButton("PreAir");
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
	}

}
