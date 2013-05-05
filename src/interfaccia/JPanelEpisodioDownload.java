package interfaccia;

import javax.swing.JPanel;

import Programma.Download;
import SerieTV.Torrent;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Font;

import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.BorderLayout;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class JPanelEpisodioDownload extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanelManagerLista parent;
	private Torrent torrent;
	private JCheckBox chckbxNomeserie;
	private JButton btnScarica;
	private JButton btnIgnora;
	private JPanel thisp=this;
	
	public JPanelEpisodioDownload(JPanelManagerLista parent, Torrent t) {
		this.parent=parent;
		this.torrent=t;
		setBorder(new MatteBorder(2, 1, 2, 1, (Color) new Color(0, 0, 0)));
		if(t.getPuntata()==0 || t.getSerie()==0)
			t.parseMagnet();
		setLayout(new BorderLayout(0, 0));
		
		String tag=""+(t.is720p()?"720p":"")+" "+(torrent.isPROPER()?"PROPER":"")+(t.isRepack()?"REPACK":"")+" "+(t.isPreAir()?"PRE-AIR":"").trim();
		
		JPanel panel_nord = new JPanel();
		add(panel_nord, BorderLayout.NORTH);
		panel_nord.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_w = new JPanel();
		panel_nord.add(panel_w, BorderLayout.WEST);
		
		chckbxNomeserie = new JCheckBox("<html>"+t.getNomeSerie()+"<br>Stagione: <b>"+torrent.getSerie()+"</b>&nbsp;Episodio: <b>"+torrent.getPuntata()+"</b></html>");
		panel_w.add(chckbxNomeserie);
		chckbxNomeserie.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_e = new JPanel();
		panel_nord.add(panel_e, BorderLayout.EAST);
		panel_e.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_bottoni = new JPanel();
		panel_e.add(panel_bottoni, BorderLayout.CENTER);
		
		btnScarica = new JButton("Scarica");
		
		panel_bottoni.add(btnScarica);
		
		btnIgnora = new JButton("Ignora");
		panel_bottoni.add(btnIgnora);
		JLabel lblTagqui = new JLabel(tag);
		panel_e.add(lblTagqui, BorderLayout.SOUTH);
		lblTagqui.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		JPanel panel_sud = new JPanel();
		add(panel_sud, BorderLayout.SOUTH);
		
		JLabel lblNometorrent = new JLabel("<html>"+t.getName()+"</html>");
		panel_sud.add(lblNometorrent);
		
		Color colore=null;
		if(torrent.is720p())
			colore=Color.GREEN;
		else if(torrent.isRepack())
			colore=Color.ORANGE;
		else 
			colore=Color.LIGHT_GRAY;
		
		colora(this, colore);
		
		addListener();
	}
	private void addListener(){
		btnIgnora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				torrent.setScaricato(Torrent.IGNORATO, true);
				parent.remove(thisp);
			}
		});
		btnScarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Download.downloadMagnet(torrent.getUrl(), torrent.getNomeSerieFolder());
				}
				catch (IOException e) {
					e.printStackTrace();
					return;
				}
				torrent.setScaricato(Torrent.SCARICATO, true);
			}
		});
	}
	private void colora(JPanel p, Color colore){
		p.setBackground(colore);
		for(int i=0;i<p.getComponentCount();i++){
			if(p.getComponent(i) instanceof JPanel)
				colora((JPanel) p.getComponent(i), colore);
			else
				p.getComponent(i).setBackground(colore);
		}
	}
	public JCheckBox getCheckbox(){
		return chckbxNomeserie;
	}
	public Torrent getTorrent() {
		return torrent;
	}
}
