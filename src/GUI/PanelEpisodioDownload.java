package GUI;

import javax.swing.JPanel;
import javax.swing.JCheckBox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import SerieTV.EZTV;
import SerieTV.SerieTV2;
import SerieTV.Torrent;
import SerieTV.Torrent2;
import javax.swing.SwingConstants;

public class PanelEpisodioDownload extends JPanel {
	private static final long serialVersionUID = 1L;
	private Torrent2 episodio;
	
	private JButton btnScarica;
	
	private JButton btnIgnora;
	private JLabel lblserie;
	private JLabel lblepisodio;
	private JButton btnInfo;
	

	public PanelEpisodioDownload(Torrent2 t) {
		episodio = t;
		Color bkgcolor=backgroundByTags();
		setSize(375,90);
		setLayout(new BorderLayout(0, 0));
		setBorder(new EtchedBorder());
		setBackground(bkgcolor);
		
		JPanel panel_n = new JPanel();
		panel_n.setBackground(bkgcolor);
		add(panel_n, BorderLayout.NORTH);
		panel_n.setLayout(new BorderLayout(0, 0));
		
		JCheckBox chckbxNomeserie = new JCheckBox(episodio.getNomeSerie());
		chckbxNomeserie.setBackground(bkgcolor);
		chckbxNomeserie.setHorizontalAlignment(SwingConstants.CENTER);
		panel_n.add(chckbxNomeserie, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(bkgcolor);
		panel_n.add(panel_1, BorderLayout.EAST);
		
		btnScarica = new JButton("Scarica");
		btnScarica.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/salva.png")));
		panel_1.add(btnScarica);
		
		btnIgnora = new JButton("Ignora");
		btnIgnora.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/reset.png")));
		panel_1.add(btnIgnora);
		
		JPanel panel_s = new JPanel();
		panel_s.setBackground(bkgcolor);
		add(panel_s, BorderLayout.SOUTH);
		panel_s.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTorrentname = new JLabel("  "+episodio.toString());
		lblTorrentname.setBackground(bkgcolor);
		panel_s.add(lblTorrentname, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		panel.setBackground(bkgcolor);
		add(panel, BorderLayout.WEST);
		
		JLabel lblStagione = new JLabel("Stagione: ");
		lblStagione.setBackground(bkgcolor);
		panel.add(lblStagione);
		
		lblserie = new JLabel("<html><b>"+episodio.getStagione()+"</b></html>");
		lblserie.setBackground(bkgcolor);
		panel.add(lblserie);
		
		JLabel lblEpisodio = new JLabel("Episodio: ");
		lblEpisodio.setBackground(bkgcolor);
		panel.add(lblEpisodio);
		
		lblepisodio = new JLabel("<html><b>"+episodio.getEpisodio()+"</b></html>");
		lblepisodio.setBackground(bkgcolor);
		panel.add(lblepisodio);
		
		String tags=""+(episodio.is720p()?"720p":"")+" "+(episodio.isPROPER()?"PROPER":"")+" "+(episodio.isRepack()?"REPACK":"")+" "+(episodio.isPreAir()?"PREAIR":"").trim();
		JLabel lbltags = new JLabel("<html><b>"+tags+"</b></html>");
		lbltags.setBackground(bkgcolor);
		add(lbltags, BorderLayout.EAST);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.EAST);
		
		btnInfo = new JButton("");
		panel_3.add(btnInfo);
		btnInfo.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/info.png")));
		
		addListener();
		
	}
	private Color backgroundByTags(){
		boolean hd=episodio.is720p();
		boolean repack=episodio.isRepack();
		boolean preair=episodio.isPreAir();
		boolean proper=episodio.isPROPER();
		
		if(hd && (repack || proper)){
			return (Color.ORANGE);
		}
		else if(hd){
			return (Color.GREEN);
		}
		else if(repack || proper){
			return (Color.RED);
		}
		else if(preair){
			return (Color.WHITE);
		}
		return new Color(240,240,240);
	}
	private void addListener(){
		
		btnIgnora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				episodio.setScaricato(Torrent.IGNORATO, false);
				removeFromParent();
			}
		});
		btnScarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO inserire download
			}
		});
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Inserire info button listener
				
			}
		});
		lblepisodio.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				int cambio=JOptionPane.showConfirmDialog(getRootPane()==null?getParent():getRootPane(), "Vuoi cambiare il numero dell'episodio?", "Modifica episodio", JOptionPane.YES_NO_OPTION);
				if(cambio==JOptionPane.YES_OPTION){
					String input=JOptionPane.showInputDialog(getRootPane()==null?getParent():getRootPane(), "Nuovo numero episodio: ");
					try {
						int nuovo=Integer.parseInt(input);
						if(nuovo<=0){
							JOptionPane.showMessageDialog(getRootPane()==null?getParent():getRootPane(), "Devi inserire un numero maggiore di zero");
						}
						else {
							episodio.setEpisodio(nuovo);
							lblepisodio.setText("<html><center><b>"+nuovo+"</b></center></html>");
						}
					}
					catch(NumberFormatException e){
						JOptionPane.showMessageDialog(getRootPane()==null?getParent():getRootPane(), "Devi inserire un numero intero");
					}
				}
			}
		});
		lblserie.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				int cambio=JOptionPane.showConfirmDialog(getRootPane()==null?getParent():getRootPane(), "Vuoi cambiare il numero della stagione?", "Modifica stagione", JOptionPane.YES_NO_OPTION);
				if(cambio==JOptionPane.YES_OPTION){
					String input=JOptionPane.showInputDialog(getRootPane()==null?getParent():getRootPane(), "Nuovo numero stagione: ");
					try {
						int nuovo=Integer.parseInt(input);
						if(nuovo<=0){
							JOptionPane.showMessageDialog(getRootPane()==null?getParent():getRootPane(), "Devi inserire un numero maggiore di zero");
						}
						else {
							episodio.setStagione(nuovo);
							lblserie.setText("<html><center><b>"+nuovo+"</b></center></html>");
						}
					}
					catch(NumberFormatException e){
						JOptionPane.showMessageDialog(getRootPane()==null?getParent():getRootPane(), "Devi inserire un numero intero");
					}
				}
			}
		});
		
	}
	private void removeFromParent(){
		//LayoutManager layout=getParent().getLayout();
		Container parent=getParent();
		parent.remove(PanelEpisodioDownload.this);
		/*
		if(layout instanceof GridLayout){
			GridLayout lay=(GridLayout)layout;
			lay.setRows(lay.getRows()-1);
		}
		parent.revalidate();
		parent.repaint();
		*/
	}
	public static void main(String[] args){
		JFrame frame=new JFrame("Prova");
		frame.setSize(750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Torrent2 t=new Torrent2(new SerieTV2(new EZTV(), "Anger Management", ""), "magnet:?xt=urn:btih:CESZGU2HYDQ3V7PMARB3MXELSZ3AMDWU&dn=Anger.Management.S02E31.HDTV.x264-ASAP&tr=udp://tracker.openbittorrent.com:80&tr=udp://tracker.publicbt.com:80&tr=udp://tracker.istole.it:80&tr=udp://open.demonii.com:80&tr=udp://tracker.coppersurfer.tk:80",Torrent.SCARICARE);
		t.parseMagnet();
		PanelEpisodioDownload p=new PanelEpisodioDownload(t);
		frame.getContentPane().add(p);
		
		frame.setVisible(true);
	}
}
