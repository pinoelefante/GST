package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import Programma.Download;
import Programma.ManagerException;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.Torrent;


public class CasellaDownload extends JPanel{
	private JPanel thispanel=this;
	private JPanel download_panel_scroll;
	private static final long	serialVersionUID	= 1L;
	private Torrent torrent;
	
	public CasellaDownload(Torrent t, JPanel p){
		torrent=t;
		download_panel_scroll=p;
		disegna();
	}
	private JCheckBox box;
	private JLabel label_tag;
	
	public JButton scarica;
	public JButton rimuovi;
	public JButton reparse;
	
	private static final Font font_label=new Font("Arial", Font.BOLD, 12);
	
	public void disegna(){
		thispanel=this;
		box=new JCheckBox(torrent.getNomeSerie()+" "+torrent.getStagione()+"x"+torrent.getEpisodio());
		label_tag=new JLabel(torrent.isPreAir()?"PREAIR":""+" "+(torrent.isRepack()?"REPACK":"")+" "+(torrent.is720p()?"720p":"")+" "+(torrent.isPROPER()?"PROPER":""));
		label_tag.setFont(font_label);
		scarica=new JButton(Language.DOWNLOAD_BOTTONE_DOWNLOAD);
		rimuovi=new JButton(Language.INSERIMENTO_BOTTONE_RIMUOVI);
		reparse=new JButton("Re-Parse");
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder());
		add(box, BorderLayout.WEST);
		JPanel est=new JPanel(new BorderLayout());
		JPanel panel_button=new JPanel();
		panel_button.add(scarica);
		panel_button.add(rimuovi);
		panel_button.add(reparse);
		add(est, BorderLayout.EAST);
		est.add(panel_button, BorderLayout.CENTER);
		est.add(label_tag, BorderLayout.SOUTH);
		
		if(torrent.isRepack()){
			setBackground(Color.RED);
			est.setBackground(Color.RED);
			panel_button.setBackground(Color.RED);
			box.setBackground(Color.RED);
		}
		else if(torrent.is720p()){
			setBackground(Color.GREEN);
			est.setBackground(Color.GREEN);
			panel_button.setBackground(Color.GREEN);
			box.setBackground(Color.GREEN);
		}
		
		addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {
				box.setSelected(!box.isSelected());
			}
		});
		scarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!OperazioniFile.fileExists(Settings.getClientPath())){
					JOptionPane.showMessageDialog(Interfaccia.frame, Language.DOWNLOAD_DIALOGUE_ERRORE_CLIENT);
					return;
				}
				if(!Settings.verificaUtorrent()){
					JOptionPane.showMessageDialog(Interfaccia.frame, "Configurare il percorso di uTorrent correttamente");
					return;
				}
					
				try {
					Download.downloadMagnet(torrent.getUrl(), torrent.getNomeSerieFolder());
					torrent.setScaricato(Torrent.SCARICATO);
					torrent.setSottotitolo(true, true);
					rimuovi_panel();
				}
				catch (IOException e) {
					ManagerException.registraEccezione(e);
				}
			}
		});
		rimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				torrent.setScaricato(Torrent.IGNORATO, true);
				rimuovi_panel();
			}
		});
		reparse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				torrent.parseMagnet();
				torrent.update();
				box.setText(torrent.getNomeSerie()+" "+torrent.getStagione()+"x"+torrent.getEpisodio());
			}
		});
	}
	private void rimuovi_panel(){
		int num_panel=download_panel_scroll.getComponentCount();
		GridLayout lay=(GridLayout) download_panel_scroll.getLayout();
		download_panel_scroll.remove(thispanel);
		if(num_panel>5)
			lay.setRows(num_panel-1);
		download_panel_scroll.revalidate();
		download_panel_scroll.repaint();
		Interfaccia.aggiornaLabelStato();
	}
	public JCheckBox getCheckBox(){
		return box;
	}
	public Torrent getTorrent(){
		return torrent;
	}
}