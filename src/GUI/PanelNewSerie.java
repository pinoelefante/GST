package GUI;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import SerieTV.GestioneSerieTV2;
import SerieTV.SerieTV2;

public class PanelNewSerie extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btnInserisci;
	private SerieTV2 st;
	
	public PanelNewSerie(SerieTV2 serie) {
		st=serie;
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setSize(250, 70);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		JLabel lblnomeSerie = new JLabel("<html><b>"+serie.getNomeSerie()+"</b></html>");
		panel.add(lblnomeSerie);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		
		btnInserisci = new JButton("");
		btnInserisci.setIcon(new ImageIcon(PanelNewSerie.class.getResource("/GUI/res/add.png")));
		panel_1.add(btnInserisci);
		
		addListener();
	}
	private void addListener(){
		btnInserisci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(GestioneSerieTV2.aggiungiSeriePreferita(st)){
					Interfaccia2.getInterfaccia().reloadSeriePreferite();
				}
			}
		});
	}
}
