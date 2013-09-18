package GUI;
import javax.swing.JPanel;

import SerieTV.Torrent;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

public class PanelSubDown extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public PanelSubDown(Torrent t) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_2, BorderLayout.SOUTH);
		
		JLabel lblRisultato = new JLabel();
		panel_2.add(lblRisultato);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNomeserieEStats = new JLabel("<html>"+t.getNomeSerie()+"<br>Stagione: <b>"+t.getStagione()+"</b> Episodio: <b>"+t.getEpisodio()+"</b></html>");
		panel.add(lblNomeserieEStats, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.EAST);
		
		JButton btnScarica = new JButton("Scarica");
		panel_1.add(btnScarica);
		
		JButton btnRimuovi = new JButton("Rimuovi");
		panel_1.add(btnRimuovi);
		
		addListener();
	}
	private void addListener(){
		
	}
}
