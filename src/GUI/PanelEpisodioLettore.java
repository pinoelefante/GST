package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import LettoreVideo.Player;
import LettoreVideo.PlaylistItem;
import Programma.Download;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.Torrent;
import StruttureDati.serietv.Episodio;

public class PanelEpisodioLettore extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JPlaylist playlist;
	private Episodio episodio;
	private Torrent torrent;
	private JComboBox<String> cmb_stato_episodio;
	private JButton btnPlaylist;
	private JButton btnScarica;
	private JButton btnSottotitolo;
	private JButton btnCancellaFile;
	private JButton btnCopiaSuDispositivo;
	private JButton btnPlay;
	
	public PanelEpisodioLettore(Episodio ep) {
		episodio=ep;
		torrent=ep.getLink();
		
		setSize(750, 100);
		setLayout(new BorderLayout(0, 0));
		setBorder(new EtchedBorder(EtchedBorder.RAISED));
		JLabel lblNomeFile = new JLabel("<html><br>&nbsp;&nbsp;Nome file: "+torrent.getFilePath()+"</html>");
		add(lblNomeFile, BorderLayout.SOUTH);
		
		JPanel panel_n = new JPanel();
		add(panel_n, BorderLayout.NORTH);
		panel_n.setLayout(new BorderLayout(0, 0));
				
		JPanel panel = new JPanel();
		panel_n.add(panel, BorderLayout.WEST);
				
		JLabel lblnomeserie = new JLabel("<html><b>"+episodio.getNomeSerie()+"</b>&nbsp;&nbsp;Stagione: <b>"+episodio.getStagione()+"</b> Episodio: <b>"+episodio.getEpisodio()+"</b></html>");
		panel.add(lblnomeserie);
		
		cmb_stato_episodio = new JComboBox<String>();
		panel_n.add(cmb_stato_episodio, BorderLayout.EAST);
		cmb_stato_episodio.addItem("SCARICARE");
		cmb_stato_episodio.addItem("SCARICATO");
		cmb_stato_episodio.addItem("VISTO");
		cmb_stato_episodio.addItem("RIMOSSO");
		cmb_stato_episodio.addItem("IGNORATO");
		
		JPanel panel_e = new JPanel();
		add(panel_e, BorderLayout.EAST);
		panel_e.setLayout(new BorderLayout(0, 0));
		JPanel p_e_c=new JPanel();
		panel_e.add(p_e_c, BorderLayout.CENTER);
		btnPlaylist = new JButton("");
		btnPlaylist.setToolTipText("Aggiungi alla playlist");
		btnPlaylist.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/add.png")));
		p_e_c.add(btnPlaylist);
		
		JPanel panel_c = new JPanel();
		add(panel_c, BorderLayout.CENTER);
		btnScarica = new JButton("Scarica");
		btnScarica.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/utorrent.png")));
		panel_c.add(btnScarica);
		
		btnSottotitolo = new JButton("Sottotitolo");
		btnSottotitolo.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/sub16.png")));
		panel_c.add(btnSottotitolo);
		
		btnCancellaFile = new JButton("Cancella");
		btnCancellaFile.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/cestino16.png")));
		panel_c.add(btnCancellaFile);
		
		btnCopiaSuDispositivo = new JButton("Copia su...");
		
		btnCopiaSuDispositivo.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/cartella.png")));
		panel_c.add(btnCopiaSuDispositivo);
		btnPlay = new JButton("Play");
		panel_c.add(btnPlay);
		btnPlay.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/vlc.png")));
		btnPlay.setBounds(603, 66, 89, 23);
		
		addListener();
		cmb_stato_episodio.setSelectedIndex(torrent.getScaricato());
	}
	private void addListener(){
		cmb_stato_episodio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				torrent.setScaricato(cmb_stato_episodio.getSelectedIndex());
				switch((String)cmb_stato_episodio.getSelectedItem()){
					case "SCARICARE":
						btnPlaylist.setEnabled(false);
						btnCancellaFile.setEnabled(false);
						btnCopiaSuDispositivo.setEnabled(false);
						btnPlay.setEnabled(false);
						btnScarica.setEnabled(true);
						btnSottotitolo.setEnabled(true);
						break;
					case "SCARICATO":
						btnPlaylist.setEnabled(true);
						btnCancellaFile.setEnabled(true);
						btnCopiaSuDispositivo.setEnabled(true);
						btnPlay.setEnabled(true);
						btnScarica.setEnabled(true);
						btnSottotitolo.setEnabled(true);
						break;
					case "VISTO":
						btnPlaylist.setEnabled(true);
						btnCancellaFile.setEnabled(true);
						btnCopiaSuDispositivo.setEnabled(true);
						btnPlay.setEnabled(true);
						btnScarica.setEnabled(true);
						btnSottotitolo.setEnabled(true);
						break;
					case "RIMOSSO":
						btnPlaylist.setEnabled(false);
						btnCancellaFile.setEnabled(false);
						btnCopiaSuDispositivo.setEnabled(false);
						btnPlay.setEnabled(false);
						btnScarica.setEnabled(true);
						btnSottotitolo.setEnabled(true);
						break;
					case "IGNORATO":
						btnPlaylist.setEnabled(false);
						btnCancellaFile.setEnabled(false);
						btnCopiaSuDispositivo.setEnabled(false);
						btnPlay.setEnabled(false);
						btnScarica.setEnabled(true);
						btnSottotitolo.setEnabled(true);
						break;
				}
			}
		});
		btnPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(playlist==null){
					playlist=cercaPlaylist(PanelEpisodioLettore.this);
					if(playlist!=null){
						playlist.addItem(new PlaylistItem(torrent));
						System.out.println("Playlist trovata");
					}
					else {
						System.out.println("Playlist non trovata");
					}
				}
				if(playlist!=null){
					playlist.addItem(new PlaylistItem(torrent));
				}
			}
		});
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Settings.isExtenalVLC()){
					String filepath=torrent.getFilePath();
					if(filepath.length()>0)
						PlayerOLD.play(filepath);
				}
				else {
					playlist.addItem(new PlaylistItem(torrent));
					Player player=playlist.getPlayer();
					String filepath=torrent.getFilePath();
					if(filepath.length()>0){
						if(player!=null){
							if(player.isLinked())
								player.play(filepath);
						}
						else {
							PlayerOLD.play(filepath);
						}
					}
				}
			}
		});
		btnCancellaFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int r=JOptionPane.showConfirmDialog(PanelEpisodioLettore.this.getParent(), "Sei sicuro di voler cancellare:\n"+torrent.getFormattedName(), "Conferma cancellazione", JOptionPane.YES_NO_OPTION);
				if(r==JOptionPane.YES_OPTION){
					String file=torrent.getFilePath();
					if(OperazioniFile.deleteFile(file)){
						torrent.setScaricato(Torrent.RIMOSSO);
						torrent.updateTorrentInDB();
						cmb_stato_episodio.setSelectedIndex(Torrent.RIMOSSO);
					}
					else {
						JOptionPane.showMessageDialog(PanelEpisodioLettore.this.getParent(), "Non è stato possibile eliminare il file. Potrebbe essere in uso da un altro processo.\nSe sei sicuro che il file non esiste, imposta manualmente lo stato RIMOSSO");
					}
				}
			}
		});
		
		btnCopiaSuDispositivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String origine_filepath=torrent.getFilePath();
				String destinazione_path="";
				JFileChooser chooser= new JFileChooser();
				chooser.setDialogTitle("Cartella di destinazione");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if(chooser.showOpenDialog(PanelEpisodioLettore.this.getParent().getParent().getParent()) == JFileChooser.APPROVE_OPTION){
					destinazione_path=chooser.getSelectedFile().getAbsolutePath();
				}
				else {
				      return;
				}
				if(!origine_filepath.isEmpty() & !destinazione_path.isEmpty()){
    				Download.copiaFile(origine_filepath, destinazione_path);
    				JOptionPane.showMessageDialog(PanelEpisodioLettore.this.getParent().getParent(), "Il file è stato aggiunto alla coda dei file da copiare.\nControlla la sezione File Manager per vedere lo stato della copia.");
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
}
