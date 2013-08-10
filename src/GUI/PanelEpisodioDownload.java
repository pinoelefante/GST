package GUI;

import javax.swing.JPanel;
import javax.swing.JCheckBox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import SerieTV.Torrent;

public class PanelEpisodioDownload extends JPanel {
	private static final long serialVersionUID = 1L;
	private Torrent episodio;
	private JButton btnScarica;
	private JButton btnIgnora;
	private JLabel lblserie;
	private JLabel lblepisodio;

	public PanelEpisodioDownload(Torrent t) {
		episodio = t;
		Color bkgcolor=backgroundByTags();
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JCheckBox chckbxNomeserie = new JCheckBox("<html>"+episodio.getNomeSerie()+"</html>");
		chckbxNomeserie.setBounds(6, 7, 173, 23);
		panel.add(chckbxNomeserie);
		chckbxNomeserie.setBackground(bkgcolor);
		
		JLabel labelEpisodio = new JLabel("Episodio:");
		labelEpisodio.setBounds(94, 37, 60, 14);
		panel.add(labelEpisodio);
		labelEpisodio.setBackground(bkgcolor);
		
		JLabel labelStagione = new JLabel("Stagione:");
		labelStagione.setBounds(8, 37, 60, 14);
		panel.add(labelStagione);
		labelStagione.setBackground(bkgcolor);
		
		lblserie = new JLabel("<html><center><b>"+episodio.getStagione()+"</b></center></html>");
		lblserie.setBounds(65, 37, 27, 16);
		panel.add(lblserie);
		lblserie.setBackground(bkgcolor);
		
		lblepisodio = new JLabel("<html><center><b>"+episodio.getEpisodio()+"</b></center></html>");
		lblepisodio.setBounds(152, 37, 22, 16);
		panel.add(lblepisodio);
		lblepisodio.setBackground(bkgcolor);
		
		JLabel lbllinktorrent = new JLabel(episodio.toString());
		lbllinktorrent.setBounds(8, 60, 352, 14);
		panel.add(lbllinktorrent);
		lbllinktorrent.setBackground(bkgcolor);
		
		btnScarica = new JButton("<html>Scarica</html>");
		btnScarica.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/salva.png")));
		btnScarica.setBounds(190, 6, 90, 25);
		panel.add(btnScarica);
		
		btnIgnora = new JButton("<html>Ignora</html>");
		btnIgnora.setIcon(new ImageIcon(PanelEpisodioDownload.class.getResource("/GUI/res/reset.png")));
		btnIgnora.setBounds(280, 6, 90, 25);
		panel.add(btnIgnora);
		
		String tags=""+(episodio.is720p()?"720p":"")+" "+(episodio.isPROPER()?"PROPER":"")+" "+(episodio.isRepack()?"REPACK":"")+" "+(episodio.isPreAir()?"PREAIR":"").trim();
		JLabel lbltags = new JLabel("<html><b>"+tags+"</b></html>");
		lbltags.setBounds(192, 37, 115, 16);
		panel.add(lbltags);
		lbltags.setBackground(bkgcolor);
		
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
		LayoutManager layout=getParent().getLayout();
		Container parent=getParent();
		parent.remove(PanelEpisodioDownload.this);
		if(layout instanceof GridLayout){
			GridLayout lay=(GridLayout)layout;
			int count=parent.getComponentCount();
			if(count%2==0){
				if(lay.getColumns()*lay.getRows()>count){
					lay.setRows(lay.getRows()-1);
				}
			}
		}
		parent.revalidate();
		parent.repaint();
	}
	public static void main(String[] args){
		JFrame frame=new JFrame("Prova");
		frame.setSize(750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Torrent t=new Torrent("magnet:?xt=urn:btih:CESZGU2HYDQ3V7PMARB3MXELSZ3AMDWU&dn=Anger.Management.S02E31.HDTV.x264-ASAP&tr=udp://tracker.openbittorrent.com:80&tr=udp://tracker.publicbt.com:80&tr=udp://tracker.istole.it:80&tr=udp://open.demonii.com:80&tr=udp://tracker.coppersurfer.tk:80",Torrent.SCARICARE, "Anger Management", 0);
		t.parseMagnet();
		PanelEpisodioDownload p=new PanelEpisodioDownload(t);
		frame.getContentPane().add(p);
		
		frame.setVisible(true);
	}
}
