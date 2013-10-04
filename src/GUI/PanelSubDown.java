package GUI;
import javax.swing.JPanel;

import SerieTV.GestioneSerieTV;
import SerieTV.Torrent;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

public class PanelSubDown extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel lblRisultato;
	private JButton btnScarica;
	private JButton btnRimuovi;
	private Torrent torrent;
	
	public PanelSubDown(Torrent t) {
		torrent=t;
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(panel_2, BorderLayout.SOUTH);
		
		lblRisultato = new JLabel();
		panel_2.add(lblRisultato);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNomeserieEStats = new JLabel("<html>"+torrent.getNomeSerie()+"<br>Stagione: <b>"+torrent.getStagione()+"</b> Episodio: <b>"+torrent.getEpisodio()+"</b></html>");
		panel.add(lblNomeserieEStats, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.EAST);
		
		btnScarica = new JButton("Scarica");
		panel_1.add(btnScarica);
		
		btnRimuovi = new JButton("Rimuovi");
		panel_1.add(btnRimuovi);
		
		addListener();
	}
	private void addListener(){
		btnRimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				torrent.setSubDownload(false,true);
				PanelSubDown.this.getParent().remove(PanelSubDown.this);
			}
		});
		btnScarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				class t_ricercaSub extends Thread {
					public void run(){
						lblRisultato.setText("<html>Ricerca sottotitolo in corso</html>");
						btnScarica.setEnabled(false);
						btnRimuovi.setEnabled(false);
						if(GestioneSerieTV.getSubManager().scaricaSottotitolo(torrent)){
							lblRisultato.setText("<html>Sottotitolo scaricato!</html>");
							class threadRemove extends Thread {
								public void run(){
									int secondi=5;
									while (secondi>=0){
										lblRisultato.setText("<html>Sottotitolo scaricato!<br>Rimozione tra "+secondi+" secondi</html>");
										try { sleep(1000); }
										catch (InterruptedException e) {}
										secondi--;
									}
									PanelSubDown.this.getParent().remove(PanelSubDown.this);
								}
							}
							Thread t_rimozione=new threadRemove();
							t_rimozione.start();
						}
						else {
							lblRisultato.setText("<html>Sottotitolo non trovato</html>");
							btnScarica.setEnabled(true);
							btnRimuovi.setEnabled(true);
						}
					}
				}
				Thread t=new t_ricercaSub();
				t.start();
			}
		});
	}
}
