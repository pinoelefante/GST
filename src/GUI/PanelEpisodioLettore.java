package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import GUI.player.Player;
import GUI.player.PlayerEsterno;
import Interfacce.ValueChangeSubscriber;
import Programma.Download;
import Programma.OperazioniFile;
import Programma.Settings;
import SerieTV.GestioneSerieTV;
import SerieTV.Torrent;
import StruttureDati.serietv.Episodio;

public class PanelEpisodioLettore extends JPanel implements ValueChangeSubscriber{
	private static final long serialVersionUID = 1L;
	private Episodio episodio;
	private Torrent torrent;
	private JComboBox<String> cmb_stato_episodio;
	private JButton btnPlaylist;
	private JButton btnScarica;
	private JButton btnSottotitolo;
	private JButton btnCancellaFile;
	private JButton btnCopiaSuDispositivo;
	private JButton btnPlay;
	private JLabel lblNomeFile;
	private JLabel lblnomeserie;
	private JComboBox<String> cmb_versione;
	private JButton btnMostra;
	
	private boolean isOK=true;
	
	public PanelEpisodioLettore(Episodio ep) {
		episodio=ep;
		torrent=ep.getLinkScaricato();
		if(torrent==null){
			torrent=ep.getLinkLettore();
		}
		if(torrent==null){
			isOK=false;
			return;
		}
		
		torrent.subscribe(this);
		
		setSize(750, 100);
		setLayout(new BorderLayout(0, 0));
		setBorder(new EtchedBorder(EtchedBorder.RAISED));
		lblNomeFile = new JLabel();
		add(lblNomeFile, BorderLayout.SOUTH);
		
		JPanel panel_n = new JPanel();
		add(panel_n, BorderLayout.NORTH);
		panel_n.setLayout(new BorderLayout(0, 0));
				
		JPanel panel = new JPanel();
		panel_n.add(panel, BorderLayout.WEST);
		
		lblnomeserie = new JLabel();
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
		
		btnMostra = new JButton("Mostra");
		//TODO decommentare per debug
		btnMostra.setVisible(false);
		panel_c.add(btnMostra);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.WEST);
		cmb_versione = new JComboBox<String>();
		if(episodio.getLinkHD()!=null)
			cmb_versione.addItem("HD");
		if(episodio.getLinkNormale()!=null)
			cmb_versione.addItem("SD");
		if(episodio.getLinkPreair()!=null)
			cmb_versione.addItem("Pre");
		panel_1.add(cmb_versione);
		
		addListener();
		
		if(torrent.is720p())
			cmb_versione.setSelectedItem("HD");
		else if(torrent.isPreAir())
			cmb_versione.setSelectedItem("Pre");
		else
			cmb_versione.setSelectedItem("SD");
		
		init();
	}
	public boolean isOK(){
		return isOK;
	}
	private void init(){
		lblNomeFile.setText("<html><br>&nbsp;&nbsp;Nome file: "+torrent.getFilePath()+"</html>");
		String tag=torrent.is720p()?"720p":"Normale";
		if(torrent.isPreAir())
			tag+=" Preair";
		lblnomeserie.setText("<html><b><dynamic></b>&nbsp;&nbsp;Stagione: <b>"+episodio.getStagione()+"</b> Episodio: <b>"+episodio.getEpisodio()+"</b> - "+tag+"</html>");
		cmb_stato_episodio.setSelectedIndex(torrent.getScaricato());
	}
	private void addListener(){
		cmb_versione.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				switch((String)cmb_versione.getSelectedItem()){
					case "HD":
						torrent=episodio.getLinkHD();
						break;
					case "SD":
						torrent=episodio.getLinkNormale();
						break;
					case "Pre":
						torrent=episodio.getLinkPreair();
						break;
				}
				init();
			}
		});
		cmb_stato_episodio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				torrent.setScaricato(cmb_stato_episodio.getSelectedIndex(), true);
				switch((String)cmb_stato_episodio.getSelectedItem()){
					case "SCARICARE":
					case "RIMOSSO":
					case "IGNORATO":
						btnPlaylist.setEnabled(false);
						btnCancellaFile.setEnabled(false);
						btnCopiaSuDispositivo.setEnabled(false);
						btnPlay.setEnabled(false);
						btnScarica.setEnabled(true);
						btnSottotitolo.setEnabled(true);
						break;
					case "SCARICATO":
					case "VISTO":
						btnPlaylist.setEnabled(true);
						btnCancellaFile.setEnabled(true);
						btnCopiaSuDispositivo.setEnabled(true);
						btnPlay.setEnabled(true);
						btnScarica.setEnabled(true);
						btnSottotitolo.setEnabled(true);
						break;
				}
			}
		});
		btnPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path=torrent.getFilePath();
				if(path==null || path.isEmpty()){
					JOptionPane.showMessageDialog(PanelEpisodioLettore.this, "File non trovato");
					return;
				}
				Player.getInstance().add(path);
			}
		});
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Settings.isRicercaSottotitoli()){
					ArrayList<String> subs=torrent.getSottotitoliPath();
					if(subs==null || subs.size()==0){
						int r=JOptionPane.showConfirmDialog(PanelEpisodioLettore.this.getParent().getParent(), "Non � stato trovato alcun sottotitolo.\nVuoi visualizzare lo stesso l'episodio?", "Sottotitolo "+torrent.getFormattedName(), JOptionPane.YES_NO_OPTION);
						if(r==JOptionPane.NO_OPTION)
							return;
					}
				}
				
				if(Settings.isExtenalVLC()){
					String filepath=torrent.getFilePath();
					if(filepath.length()>0){
						PlayerEsterno.play(filepath);
						cmb_stato_episodio.setSelectedIndex(Torrent.VISTO);
					}
				}
				else {
					String filepath=torrent.getFilePath();
					if(filepath.length()>0){
						Player.getInstance().add(filepath);
						int ind=Player.getInstance().getPlayList().size();
						Player.getInstance().playItem(ind-1);
						cmb_stato_episodio.setSelectedIndex(Torrent.VISTO);
					}	
				}
			}
		});
		btnCancellaFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int r=JOptionPane.showConfirmDialog(PanelEpisodioLettore.this, "Sei sicuro di voler cancellare:\n"+torrent.getFormattedName(), "Conferma cancellazione", JOptionPane.YES_NO_OPTION);
				if(r==JOptionPane.YES_OPTION){
					String file=torrent.getFilePath();
					if(OperazioniFile.deleteFile(file)){
						ArrayList<String> res=torrent.getSottotitoliPath();
						if(res!=null)
							for(int i=0;i<res.size();i++)
								OperazioniFile.deleteFile(res.get(i));
						torrent.setScaricato(Torrent.RIMOSSO, true);
						torrent.updateTorrentInDB();
						//cmb_stato_episodio.setSelectedIndex(Torrent.RIMOSSO);
					}
					else {
						JOptionPane.showMessageDialog(PanelEpisodioLettore.this, "Non � stato possibile eliminare il file. Potrebbe essere in uso da un altro processo.\nSe sei sicuro che il file non esiste, imposta manualmente lo stato RIMOSSO");
					}
				}
			}
		});
		
		btnCopiaSuDispositivo.addActionListener(new ActionListener() {
			private String dest;
			public void actionPerformed(ActionEvent arg0) {
				String origine_filepath=torrent.getFilePath();
				if(origine_filepath==null){
					JOptionPane.showMessageDialog(PanelEpisodioLettore.this, "Il file non esiste");
					cmb_stato_episodio.setSelectedIndex(Torrent.RIMOSSO);
					return;
				}
				String destinazione_path="";
				JFileChooser chooser= new JFileChooser(dest);
				chooser.setDialogTitle("Cartella di destinazione");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if(chooser.showOpenDialog(PanelEpisodioLettore.this.getParent().getParent().getParent()) == JFileChooser.APPROVE_OPTION){
					destinazione_path=chooser.getSelectedFile().getAbsolutePath();
					dest=destinazione_path;
				}
				else {
				      return;
				}
				if(!origine_filepath.isEmpty() & !destinazione_path.isEmpty()){
    				Download.copiaFile(origine_filepath, destinazione_path);
    				ArrayList<String> subs=torrent.getSottotitoliPath();
    				if(subs!=null)
    					for(int i=0;i<subs.size();i++)
    						Download.copiaFile(subs.get(i), destinazione_path);
    				JOptionPane.showMessageDialog(PanelEpisodioLettore.this.getParent().getParent(), "Il file � stato aggiunto alla coda dei file da copiare.\nControlla la sezione File Manager per vedere lo stato della copia.");
				}
			}
		});
		btnScarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Download.downloadMagnet(torrent.getUrl(), Settings.getDirectoryDownload()+File.separator+torrent.getSerieTV().getFolderSerie());
					if(Settings.isRicercaSottotitoli()){
						torrent.setSubDownload(true, true);
					}
					episodio.scaricaLink(torrent);
					cmb_stato_episodio.setSelectedIndex(Torrent.SCARICATO);
				}
				catch (IOException e) {
					JOptionPane.showMessageDialog(PanelEpisodioLettore.this.getParent().getParent(), "Impossibile avvia il download.\nControllare il path di uTorrent");
					e.printStackTrace();
				}
				
			}
		});
		btnSottotitolo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				class t_look_sub extends Thread {
					public void run(){
						btnSottotitolo.setEnabled(false);
						if(GestioneSerieTV.getSubManager().scaricaSottotitolo(torrent)){
							JOptionPane.showMessageDialog(PanelEpisodioLettore.this, "Sottotitolo di "+torrent.getFormattedName()+" scaricato");
						}
						else {
							torrent.setSubDownload(true, true);
							JOptionPane.showMessageDialog(PanelEpisodioLettore.this, "Non � stato possibile scaricare il sottotitolo per "+torrent.getFormattedName()+"\nSottotitolo messo in coda download");
						}
						btnSottotitolo.setEnabled(true);
					}
				}
				Thread t=new t_look_sub();
				t.start();
			}
		});
		btnMostra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO creare un frame per vedere tutti i link di un episodio in maniera "umana"
				
				JFrame frame=new JFrame("Link - "+episodio.getSerieTV().getNomeSerie()+" "+episodio.getStagione()+" "+episodio.getEpisodio());
				frame.setAlwaysOnTop(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(800, 200);
				
				ArrayList<Torrent> t=episodio.getAll();
				JLabel lbl=new JLabel("<html>");
				for(int i=0;i<t.size();i++){
					Torrent t1=t.get(i);
					lbl.setText(lbl.getText()+t1.getUrl()+" - "+t1.getScaricato()+"<br>");
				}
				lbl.setText(lbl.getText()+"</html>");
				frame.add(lbl);
				frame.setVisible(true);
			}
		});
	}
	@Override
	public void sendNotifica() {
		cmb_stato_episodio.setSelectedIndex(torrent.getScaricato());
		//TODO agire in base ai settaggi: rimosso/visto/ignorato -> rimuovere panel
	}
}
