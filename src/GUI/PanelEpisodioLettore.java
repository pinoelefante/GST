package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.EtchedBorder;

import SerieTV.EZTV;
import SerieTV.SerieTV2;
import SerieTV.Torrent;
import SerieTV.Torrent2;

public class PanelEpisodioLettore extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JPlaylist playlist;
	private Torrent2 torrent;
	private JComboBox<String> cmb_stato_episodio;
	private JButton btnPlaylist;
	
	public PanelEpisodioLettore(Torrent2 ep) {
		torrent=ep;
		
		setSize(750, 130);
		setLayout(new BorderLayout(0, 0));
		setBorder(new EtchedBorder(EtchedBorder.RAISED));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		btnPlaylist = new JButton("");
		btnPlaylist.setToolTipText("Aggiungi alla playlist");
		btnPlaylist.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/add.png")));
		btnPlaylist.setBounds(623, 27, 49, 32);
		panel.add(btnPlaylist);
		
		JButton btnScarica = new JButton("Scarica");
		btnScarica.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/utorrent.png")));
		btnScarica.setBounds(64, 66, 98, 23);
		panel.add(btnScarica);
		
		JButton btnSottotitolo = new JButton("Sottotitolo");
		btnSottotitolo.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/sub16.png")));
		btnSottotitolo.setBounds(172, 66, 112, 23);
		panel.add(btnSottotitolo);
		
		JButton btnCancellaFile = new JButton("Cancella");
		btnCancellaFile.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/cestino.png")));
		btnCancellaFile.setBounds(294, 66, 112, 23);
		panel.add(btnCancellaFile);
		
		JButton btnCopiaSuDispositivo = new JButton("Copia su...");
		btnCopiaSuDispositivo.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/cartella.png")));
		btnCopiaSuDispositivo.setBounds(416, 66, 112, 23);
		panel.add(btnCopiaSuDispositivo);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/vlc.png")));
		btnPlay.setBounds(603, 66, 89, 23);
		panel.add(btnPlay);
		
		JLabel lblnomeserie = new JLabel("<html><b>"+torrent.getNomeSerie()+"</b></html>");
		lblnomeserie.setBounds(64, 12, 342, 16);
		panel.add(lblnomeserie);
		
		JLabel lblStagione = new JLabel("Stagione:");
		lblStagione.setBounds(64, 38, 64, 16);
		panel.add(lblStagione);
		
		JLabel label_stagione = new JLabel("<html><b>"+torrent.getStagione()+"</b></html>");
		label_stagione.setBounds(130, 38, 28, 16);
		panel.add(label_stagione);
		
		JLabel lblEpisodio = new JLabel("Episodio:");
		lblEpisodio.setBounds(160, 38, 55, 16);
		panel.add(lblEpisodio);
		
		JLabel label_episodio = new JLabel("<html><b>"+torrent.getEpisodio()+"</b></html>");
		label_episodio.setBounds(219, 38, 28, 16);
		panel.add(label_episodio);
		
		cmb_stato_episodio = new JComboBox<String>();
		cmb_stato_episodio.setBounds(64, 99, 144, 20);
		panel.add(cmb_stato_episodio);
		cmb_stato_episodio.addItem("Scaricare");
		cmb_stato_episodio.addItem("Scaricato");
		cmb_stato_episodio.addItem("Visto");
		cmb_stato_episodio.addItem("Rimosso");
		cmb_stato_episodio.addItem("Ignorato");
		
		JLabel lblNomeFile = new JLabel("Nome file:");
		lblNomeFile.setBounds(218, 105, 66, 14);
		panel.add(lblNomeFile);
		
		JLabel lblfilename = new JLabel(torrent.getFilePath());
		lblfilename.setBounds(282, 104, 245, 16);
		panel.add(lblfilename);
		
		String tags=""+(torrent.is720p()?"720p":"")+" "+(torrent.isPROPER()?"PROPER":"")+" "+(torrent.isRepack()?"REPACK":"")+" "+(torrent.isPreAir()?"PREAIR":"").trim();
		JLabel lbltags = new JLabel("<html><b>"+tags+"</b></html>");
		lbltags.setBounds(603, 102, 133, 14);
		panel.add(lbltags);

		addListener();
	}
	private void addListener(){
		cmb_stato_episodio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				torrent.setScaricato(cmb_stato_episodio.getSelectedIndex());
			}
		});
		btnPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(playlist==null){
					playlist=cercaPlaylist(PanelEpisodioLettore.this);
					if(playlist!=null){
						System.out.println("Playlist trovata");
					}
					else {
						System.out.println("Playlist non trovata");
					}
				}
				if(playlist!=null){
					playlist.addItem(torrent.toString()/* TODO rimuovere commento torrent.getFilePath()*/);
				}
			}
		});
	}
	private JPlaylist cercaPlaylist(Container c){
		if(c instanceof JPlaylist)
			return (JPlaylist) c;
		else {
			for(int i=0;i<c.getComponentCount();i++){
				if(c.getComponent(i) instanceof JPlaylist)
					return (JPlaylist) c.getComponent(i);
			}
			if(c.getParent()!=null){
				return cercaPlaylist(c.getParent());
			}
			else 
				return null;
		}
	}
	public static void setPlaylist(JPlaylist p){
		playlist=p;
	}
	
	public static void main(String[] args){
		JFrame frame=new JFrame("Prova");
		frame.setSize(750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		Torrent2 t=new Torrent2(new SerieTV2(new EZTV(), "Anger Management", ""), "magnet:?xt=urn:btih:CESZGU2HYDQ3V7PMARB3MXELSZ3AMDWU&dn=Anger.Management.S02E31.HDTV.x264-ASAP&tr=udp://tracker.openbittorrent.com:80&tr=udp://tracker.publicbt.com:80&tr=udp://tracker.istole.it:80&tr=udp://open.demonii.com:80&tr=udp://tracker.coppersurfer.tk:80",Torrent.SCARICARE);
		t.parseMagnet();
		
		JPlaylist playlist=new JPlaylist();
		frame.getContentPane().add(playlist, BorderLayout.NORTH);
		
		PanelEpisodioLettore p=new PanelEpisodioLettore(t);
		frame.getContentPane().add(p);
		
		frame.setVisible(true);
	}
}
