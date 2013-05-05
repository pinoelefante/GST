package interfaccia;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import Programma.Download;
import SerieTV.Torrent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class JPanelEpisodioPlayer extends JPanel {
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboBox_stati;
	private JPanelManagerLista parent;
	private Torrent torrent;
	private JLabel lblNomeSerie;
	private JLabel lblstato_puntata;
	private JButton btnScarica;
	private JButton btnSottotitolo;
	private JButton btnPlay;
	private JButton btnVerifica;
	private JButton btnRimuovi;
	
	private static boolean h_viste=false, h_ignorate=true, h_rimosse=true;
	
	public JPanelEpisodioPlayer(JPanelManagerLista parent, Torrent t) {
		this.parent=parent;
		torrent=t;
		if(torrent.getSerie()==0 && torrent.getPuntata()==0)
			torrent.parseMagnet();
		
		setBorder(new MatteBorder(4, 2, 4, 2, (Color) new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_nord = new JPanel();
		add(panel_nord, BorderLayout.NORTH);
		panel_nord.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_n = new JPanel();
		panel_nord.add(panel_n, BorderLayout.NORTH);
		panel_n.setLayout(new BorderLayout(0, 0));
		
		lblNomeSerie = new JLabel("<html>&nbsp;&nbsp;"+torrent.getNomeSerie()+"<br>&nbsp;&nbsp;Stagione: <b>%num_stagione%</b>&nbsp;&nbsp; Episodio: <b>%num_episodio%</b></html>");
		panel_n.add(lblNomeSerie, BorderLayout.WEST);
		
		JPanel panel_stato = new JPanel();
		FlowLayout fl_panel_stato = (FlowLayout) panel_stato.getLayout();
		fl_panel_stato.setAlignment(FlowLayout.RIGHT);
		panel_n.add(panel_stato, BorderLayout.EAST);
		
		JLabel lblStatoCorrent = new JLabel("Stato corrente: ");
		panel_stato.add(lblStatoCorrent);
		
		comboBox_stati = new JComboBox<String>();
		panel_stato.add(comboBox_stati);
		
		JPanel panel_sud = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_sud.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel_sud, BorderLayout.SOUTH);
		
		
		lblstato_puntata = new JLabel("<html>Nome file:<b>%nome_file%</b><br>Sottotitoli:<b>%stato_sub%</b><br>Tag:<b>%tag%</b></html>");
		panel_sud.add(lblstato_puntata);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		
		JLabel lblPuntata = new JLabel("Puntata: ");
		panel.add(lblPuntata);
		
		btnScarica = new JButton("Scarica");
		
		panel.add(btnScarica);
		
		btnSottotitolo = new JButton("Sottotitolo");
		panel.add(btnSottotitolo);
		
		JLabel lblFile = new JLabel("File: ");
		panel.add(lblFile);
		
		btnPlay = new JButton("Play");
		panel.add(btnPlay);
		
		btnVerifica = new JButton("Verifica");
		panel.add(btnVerifica);
		
		btnRimuovi = new JButton("Rimuovi");
		panel.add(btnRimuovi);
		
		inizializza();
		addListener();
	}
	private void inizializza(){
		lblNomeSerie.setText(lblNomeSerie.getText().replace("%num_stagione%", torrent.getSerie()+"").replace("%num_episodio%", ""+torrent.getPuntata()));
		//int SCARICARE=0, SCARICATO=1, VISTO=2, RIMOSSO=3, IGNORATO=4, NASCOSTO=5; 
		comboBox_stati.addItem("SCARICARE");
		comboBox_stati.addItem("SCARICATO");
		comboBox_stati.addItem("VISTO");
		comboBox_stati.addItem("RIMOSSO");
		comboBox_stati.addItem("IGNORATO");
		comboBox_stati.setSelectedIndex(torrent.getScaricato());
		
		String tag=(torrent.is720p()?"720p":"")+" "+(torrent.isPROPER()?"PROPER":"")+" "+(torrent.isRepack()?"REPACK":"")+" "+(torrent.isPreAir()?"PRE-AIR":"").trim();
		tag=(tag.isEmpty()?"Nessuno":tag);
		lblstato_puntata.setText(lblstato_puntata.getText().replace("%tag%", tag));
	}
	private void addListener() {
		btnScarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int last_stato=torrent.getScaricato();
					Download.downloadMagnet(torrent.getUrl(), torrent.getNomeSerieFolder());
					switch(last_stato){
						case Torrent.IGNORATO:
						case Torrent.NASCOSTO:
						case Torrent.RIMOSSO:
						case Torrent.SCARICARE:
							torrent.setScaricato(Torrent.SCARICATO);
							comboBox_stati.setSelectedIndex(Torrent.SCARICATO);
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		comboBox_stati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				torrent.setScaricato(comboBox_stati.getSelectedIndex(), true);
				//TODO ridisegna
			}
		});
	}
	public JComponent getParent() {
		return parent;
	}
	public static boolean getNascondiViste(){
		return h_viste;
	}
	public static boolean getNascondiRimosse(){
		return h_rimosse;
	}
	public static boolean getNascondiIgnorate(){
		return h_ignorate;
	}
	public static void setNascondiViste(boolean s){
		h_viste=s;
	}
	public static void setNascondiRimosse(boolean s){
		h_rimosse=s;
	}
	public static void setNascondiIgnorate(boolean s){
		h_ignorate=s;
	}
}
