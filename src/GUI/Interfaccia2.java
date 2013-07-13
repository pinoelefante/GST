package GUI;

import javax.swing.JFrame;

import Programma.ControlloAggiornamenti;
import Programma.OperazioniFile;
import Programma.Settings;

import LettoreVideo.Player;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

public class Interfaccia2 extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel InfoPanel;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	
	private Player VLCPanel;
	private JPanel LettorePanel;

	public Interfaccia2(){
		super("Gestione Serie TV rel."+Settings.getVersioneSoftware()+" by pinoelefante");
		
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 600);
		Dimension res=Toolkit.getDefaultToolkit().getScreenSize();
		if(res.getWidth()>750){
			int x_screen=(int)(res.getWidth()-750)/2;
			setLocation(x_screen,10);
		}
		
		JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tab, BorderLayout.CENTER);
		
		JPanel DownloadPanel = new JPanel();
		tab.addTab("Download", new ImageIcon(Interfaccia2.class.getResource("/GUI/res/download.png")), DownloadPanel, null);
		tab.setEnabledAt(0, true);
		
		JPanel SottotitoliPanel = new JPanel();
		tab.addTab("Sottotitoli", new ImageIcon(Interfaccia2.class.getResource("/GUI/res/sottotitoli.png")), SottotitoliPanel, null);
		
		LettorePanel = new JPanel();
		tab.addTab("Lettore", new ImageIcon(Interfaccia2.class.getResource("/GUI/res/player.png")), LettorePanel, null);
		LettorePanel.setLayout(null);
		
		VLCPanel=new Player(LettorePanel);
		if(VLCPanel.isLinked()){
			VLCPanel.setBounds(10, 0, 417, 235);
			LettorePanel.add(VLCPanel.getPlayerPane());
		}
		
		
		/*	TODO commentare per la build
		JPanel PanelLettoreVideoVLC = new JPanel();
		PanelLettoreVideoVLC.setBounds(10, 0, 417, 235);
		LettorePanel.add(PanelLettoreVideoVLC);
		*/
		
		JComboBox comboBoxLettoreSerie = new JComboBox();
		comboBoxLettoreSerie.setBounds(45, 263, 260, 20);
		LettorePanel.add(comboBoxLettoreSerie);
		
		JLabel lblSerie = new JLabel("Serie");
		lblSerie.setBounds(12, 265, 37, 16);
		LettorePanel.add(lblSerie);
		
		JLabel lblStagione = new JLabel("Stagione");
		lblStagione.setBounds(323, 265, 55, 16);
		LettorePanel.add(lblStagione);
		
		JComboBox comboBoxLettoreStagione = new JComboBox();
		comboBoxLettoreStagione.setBounds(377, 263, 45, 20);
		LettorePanel.add(comboBoxLettoreStagione);
		
		JScrollPane scrollPaneLettoreEpisodi = new JScrollPane();
		scrollPaneLettoreEpisodi.setBounds(0, 285, 739, 210);
		LettorePanel.add(scrollPaneLettoreEpisodi);
		
		JPanel panel_elenco_puntate = new JPanel();
		scrollPaneLettoreEpisodi.setViewportView(panel_elenco_puntate);
		
		JCheckBox chckbxNascondiViste = new JCheckBox("Nascondi viste");
		chckbxNascondiViste.setBounds(10, 497, 112, 24);
		LettorePanel.add(chckbxNascondiViste);
		
		JCheckBox chckbxNascondiIgnorate = new JCheckBox("Nascondi ignorate");
		chckbxNascondiIgnorate.setBounds(134, 497, 135, 24);
		LettorePanel.add(chckbxNascondiIgnorate);
		
		JCheckBox chckbxNascondiRimosse = new JCheckBox("Nascondi rimosse");
		chckbxNascondiRimosse.setBounds(277, 497, 140, 24);
		LettorePanel.add(chckbxNascondiRimosse);
		
		JPanel panelLettorePlaylist = new JPanel();
		panelLettorePlaylist.setBorder(new TitledBorder(null, "Playlist", TitledBorder.LEADING, TitledBorder.BOTTOM, null, null));
		panelLettorePlaylist.setBounds(469, 0, 226, 210);
		LettorePanel.add(panelLettorePlaylist);
		
		JButton btnVLCFullscreen = new JButton("");
		btnVLCFullscreen.setToolTipText("Schermo intero");
		btnVLCFullscreen.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/fullscreen.png")));
		btnVLCFullscreen.setBounds(433, 54, 26, 26);
		LettorePanel.add(btnVLCFullscreen);
		
		JLabel lblOrdine = new JLabel("Ordine");
		lblOrdine.setBounds(565, 265, 44, 16);
		LettorePanel.add(lblOrdine);
		
		JComboBox<String> comboBoxLettoreOrdine = new JComboBox<String>();
		comboBoxLettoreOrdine.addItem("Crescente");
		comboBoxLettoreOrdine.addItem("Decrescente");
		comboBoxLettoreOrdine.setBounds(615, 263, 112, 20);
		LettorePanel.add(comboBoxLettoreOrdine);
		
		final JButton buttonVLCPlay = new JButton("");
		buttonVLCPlay.setToolTipText("Play");
		buttonVLCPlay.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/play_pause.png")));
		buttonVLCPlay.setBounds(433, 0, 26, 26);
		LettorePanel.add(buttonVLCPlay);
		
		JButton buttonVLCStop = new JButton("");
		buttonVLCStop.setToolTipText("Stop");
		buttonVLCStop.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/stop.png")));
		buttonVLCStop.setBounds(433, 26, 26, 26);
		LettorePanel.add(buttonVLCStop);
		
		JButton buttonPlaylistRimuovi = new JButton("");
		buttonPlaylistRimuovi.setToolTipText("Rimuovi dalla playlist");
		buttonPlaylistRimuovi.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/cestino.png")));
		buttonPlaylistRimuovi.setBounds(701, 172, 26, 26);
		LettorePanel.add(buttonPlaylistRimuovi);
		
		JButton buttonPlaylistDown = new JButton("");
		buttonPlaylistDown.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/down.png")));
		buttonPlaylistDown.setBounds(701, 107, 26, 26);
		LettorePanel.add(buttonPlaylistDown);
		
		JButton buttonPlaylistUp = new JButton("");
		buttonPlaylistUp.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/up.png")));
		buttonPlaylistUp.setBounds(701, 76, 26, 26);
		LettorePanel.add(buttonPlaylistUp);
		
		JSlider slider_time = new JSlider();
		slider_time.setValue(0);
		slider_time.setBounds(11, 237, 368, 16);
		LettorePanel.add(slider_time);
		
		JSlider slider_volume = new JSlider();
		slider_volume.setValue(100);
		slider_volume.setOrientation(SwingConstants.VERTICAL);
		slider_volume.setBounds(433, 124, 26, 86);
		LettorePanel.add(slider_volume);
		
		JLabel lblTimer = new JLabel("0.20.25");
		lblTimer.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimer.setBounds(377, 237, 50, 16);
		LettorePanel.add(lblTimer);
		
		JLabel imgVolume = new JLabel("");
		imgVolume.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/volume.png")));
		imgVolume.setBounds(433, 215, 26, 26);
		LettorePanel.add(imgVolume);
		
		JPanel OpzioniPanel = new JPanel();
		tab.addTab("Opzioni", new ImageIcon(Interfaccia2.class.getResource("/GUI/res/opzioni.png")), OpzioniPanel, null);
		OpzioniPanel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Aspetto", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 11, 350, 141);
		OpzioniPanel.add(panel_1);
		panel_1.setLayout(null);
		
		JCheckBox chckbxSempreInPrimo = new JCheckBox("Sempre in primo piano");
		chckbxSempreInPrimo.setBounds(6, 19, 338, 23);
		panel_1.add(chckbxSempreInPrimo);
		
		JCheckBox chckbxNascondiDuranteLa = new JCheckBox("Nascondi durante la visualizzazione");
		chckbxNascondiDuranteLa.setBounds(6, 71, 338, 23);
		panel_1.add(chckbxNascondiDuranteLa);
		
		JCheckBox chckbxChiediConfermaPrima = new JCheckBox("Chiedi conferma prima di uscire");
		chckbxChiediConfermaPrima.setBounds(6, 45, 338, 23);
		panel_1.add(chckbxChiediConfermaPrima);
		
		JCheckBox chckbxAvviaConIl = new JCheckBox("Avvia con il sistema operativo");
		chckbxAvviaConIl.setBounds(6, 97, 196, 23);
		panel_1.add(chckbxAvviaConIl);
		
		JCheckBox chckbxAvviaRidottoA = new JCheckBox("Avvia ridotto a icona");
		chckbxAvviaRidottoA.setBounds(204, 97, 140, 23);
		panel_1.add(chckbxAvviaRidottoA);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Download", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(379, 11, 350, 57);
		OpzioniPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JCheckBox chckbxMostraEpisodiIn = new JCheckBox("Mostra episodi in HD");
		chckbxMostraEpisodiIn.setBounds(6, 18, 156, 23);
		panel_2.add(chckbxMostraEpisodiIn);
		
		JCheckBox chckbxMostraEpisodiPre = new JCheckBox("Mostra episodi pre air");
		chckbxMostraEpisodiPre.setBounds(164, 18, 180, 23);
		panel_2.add(chckbxMostraEpisodiPre);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Download automatico", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_3.setBounds(379, 65, 350, 87);
		OpzioniPanel.add(panel_3);
		panel_3.setLayout(null);
		
		JCheckBox chckbxAbilita = new JCheckBox("Abilita");
		chckbxAbilita.setBounds(6, 9, 97, 23);
		panel_3.add(chckbxAbilita);
		
		final JComboBox<Integer> comboBoxMinutiRicercaAutomatica = new JComboBox<Integer>();
		comboBoxMinutiRicercaAutomatica.addItem(60);
		comboBoxMinutiRicercaAutomatica.addItem(240);
		comboBoxMinutiRicercaAutomatica.addItem(480);
		comboBoxMinutiRicercaAutomatica.addItem(960);
		comboBoxMinutiRicercaAutomatica.addItem(1440);
		comboBoxMinutiRicercaAutomatica.setBounds(10, 33, 66, 20);
		panel_3.add(comboBoxMinutiRicercaAutomatica);
		
		JLabel lblMinutiTraOgni = new JLabel("minuti tra ogni ricerca");
		lblMinutiTraOgni.setBounds(83, 35, 157, 14);
		panel_3.add(lblMinutiTraOgni);
		
		JCheckBox chckbxScaricaEpisodiIn = new JCheckBox("Scarica episodi in HD");
		chckbxScaricaEpisodiIn.setBounds(6, 56, 157, 23);
		panel_3.add(chckbxScaricaEpisodiIn);
		
		JCheckBox chckbxScaricaEpisodiPre = new JCheckBox("Scarica episodi pre air");
		chckbxScaricaEpisodiPre.setBounds(165, 56, 179, 23);
		panel_3.add(chckbxScaricaEpisodiPre);
		
		final JLabel lblRicercaOre = new JLabel("");
		lblRicercaOre.setBounds(260, 35, 78, 16);
		lblRicercaOre.setText("( 1 ora 0 min )");
		panel_3.add(lblRicercaOre);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Sottotitoli", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_4.setBounds(10, 159, 717, 136);
		OpzioniPanel.add(panel_4);
		panel_4.setLayout(null);
		
		JCheckBox chckbxAbilitaDownloadSottotitoli = new JCheckBox("Abilita download sottotitoli");
		chckbxAbilitaDownloadSottotitoli.setBounds(6, 10, 190, 23);
		panel_4.add(chckbxAbilitaDownloadSottotitoli);
		
		JCheckBox chckbxAbilitaItaliansubsnet = new JCheckBox("Abilita ItalianSubs.net");
		chckbxAbilitaItaliansubsnet.setBounds(6, 35, 150, 23);
		panel_4.add(chckbxAbilitaItaliansubsnet);
		
		JCheckBox chckbxAbilitaSubsfactory = new JCheckBox("Abilita Subsfactory");
		chckbxAbilitaSubsfactory.setBounds(277, 35, 135, 23);
		panel_4.add(chckbxAbilitaSubsfactory);
		
		JCheckBox chckbxAbilitaSubspedia = new JCheckBox("Abilita Subspedia");
		chckbxAbilitaSubspedia.setBounds(458, 29, 141, 34);
		panel_4.add(chckbxAbilitaSubspedia);
		
		JLabel lblUsername = new JLabel("<html>Username ItaSa</html>");
		lblUsername.setBounds(6, 66, 91, 23);
		panel_4.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(115, 66, 150, 20);
		panel_4.add(textField);
		textField.setColumns(10);
		
		JLabel lblPasswordItasa = new JLabel("<html>Password ItaSa</html>");
		lblPasswordItasa.setBounds(6, 92, 98, 28);
		panel_4.add(lblPasswordItasa);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(115, 98, 150, 20);
		panel_4.add(passwordField);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Programmi", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_5.setBounds(10, 307, 717, 125);
		OpzioniPanel.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblPercorsoUtorrent = new JLabel("Percorso uTorrent");
		lblPercorsoUtorrent.setBounds(12, 32, 115, 16);
		panel_5.add(lblPercorsoUtorrent);
		
		textField_1 = new JTextField();
		textField_1.setBounds(145, 30, 240, 20);
		panel_5.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblPercorsoDownload = new JLabel("Percorso download");
		lblPercorsoDownload.setBounds(12, 60, 115, 16);
		panel_5.add(lblPercorsoDownload);
		
		textField_2 = new JTextField();
		textField_2.setBounds(145, 58, 240, 20);
		panel_5.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblPercorsoVlc = new JLabel("Percorso VLC");
		lblPercorsoVlc.setBounds(12, 88, 115, 16);
		panel_5.add(lblPercorsoVlc);
		
		textField_3 = new JTextField();
		textField_3.setBounds(145, 86, 240, 20);
		panel_5.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnSfoglia = new JButton("Sfoglia");
		btnSfoglia.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/utorrent.png")));
		btnSfoglia.setBounds(397, 27, 98, 26);
		panel_5.add(btnSfoglia);
		
		JButton btnSfoglia_1 = new JButton("Sfoglia");
		btnSfoglia_1.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/cartella.png")));
		btnSfoglia_1.setBounds(397, 55, 98, 26);
		panel_5.add(btnSfoglia_1);
		
		JButton btnSfoglia_2 = new JButton("Sfoglia");
		btnSfoglia_2.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/vlc.png")));
		btnSfoglia_2.setBounds(397, 83, 98, 26);
		panel_5.add(btnSfoglia_2);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(10, 480, 717, 37);
		OpzioniPanel.add(panel_6);
		
		JButton btnSalva = new JButton("Salva");
		btnSalva.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/salva.png")));
		panel_6.add(btnSalva);
		
		JButton btnPredefiniti = new JButton("Predefiniti");
		btnPredefiniti.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/reset.png")));
		panel_6.add(btnPredefiniti);
		
		InfoPanel = new JPanel();
		tab.addTab("Info  ", new ImageIcon(Interfaccia2.class.getResource("/GUI/res/info.png")), InfoPanel, null);
		InfoPanel.setLayout(null);
		
		JLabel imgLogo = new JLabel("");
		imgLogo.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/logo.png")));
		imgLogo.setBounds(10, 11, 350, 350);
		InfoPanel.add(imgLogo);
		
		JLabel lblStoria = new JLabel("<html><font size=4><b>Gestione Serie Tv nasce dalla mia passione per le serie tv e per la programmazione, in un pomeriggio estivo.<br>Inizialmente era un semplice programma sviluppato in C (in console) che scaricava tutti gli episodi non ancora visti, sprovvisto delle funzionalit\u00E0 presenti attualmente come il download dei sottotitoli.<br><br>Mettiamo fine a questo amarcord e godiamoci quello che abbiamo oggi.</b></font>");
		lblStoria.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoria.setVerticalAlignment(SwingConstants.TOP);
		lblStoria.setBounds(381, 11, 337, 220);
		InfoPanel.add(lblStoria);
		
		JLabel lblVersioneAttuale = new JLabel("Versione attuale: "+Settings.getVersioneSoftware());
		lblVersioneAttuale.setBounds(539, 223, 179, 20);
		InfoPanel.add(lblVersioneAttuale);
		
		final JButton btnCercaAggiornamenti = new JButton("Cerca aggiornamenti");
		btnCercaAggiornamenti.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/update.png")));
		btnCercaAggiornamenti.setBounds(380, 223, 152, 44);
		InfoPanel.add(btnCercaAggiornamenti);
		
		final JLabel lblRisultatoAggiornamenti = new JLabel("Verifica disponibilit\u00E0 aggiornamenti");
		lblRisultatoAggiornamenti.setBounds(539, 242, 179, 35);
		InfoPanel.add(lblRisultatoAggiornamenti);
		
		JLabel lblDona = new JLabel("<html>Se il programma \u00E8 di tua utilit\u00E0, potresti pensare di effettua una donazione (tramite PayPal) cliccando sull'immagine. Un tuo piccolo gesto pu\u00F2 essere uno grande per me</html>");
		lblDona.setToolTipText("Clicca per effettuare una donazione");
		lblDona.setVerticalAlignment(SwingConstants.TOP);
		lblDona.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/dona.png")));
		lblDona.setBounds(381, 291, 337, 70);
		InfoPanel.add(lblDona);
		
		final JButton btnAggiornaAds = new JButton("");
		btnAggiornaAds.setToolTipText("Aggiorna pubblicit\u00E0");
		btnAggiornaAds.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/aggiorna.png")));
		btnAggiornaAds.setBounds(696, 372, 33, 23);
		InfoPanel.add(btnAggiornaAds);
		
		final JWebBrowser advertising=new JWebBrowser(JWebBrowser.destroyOnFinalization());
		/**TODO rimuovere commento per la distribuzione */
		
		if(advertising!=null){
			if(!NativeInterface.isOpen())
				NativeInterface.open();
			advertising.setBarsVisible(false);
			advertising.setStatusBarVisible(false);
			advertising.setBounds(10, 372, 676, 143);
			advertising.navigate("http://pinoelefante.altervista.org/ads.html");
			InfoPanel.add(advertising);
		}
	
		
		
		/** Solo per windows builder*//*
		InfoPanel.add(btnAggiornaAds);
		JPanel panel = new JPanel();
		panel.setBounds(10, 372, 676, 143);
		InfoPanel.add(panel);*/
		
		final JButton btnChiudiADS = new JButton("");
		btnChiudiADS.setIcon(new ImageIcon(Interfaccia2.class.getResource("/GUI/res/remove.png")));
		btnChiudiADS.setToolTipText("Chiudi pubblicit\u00E0");
		btnChiudiADS.setBounds(696, 492, 33, 23);
		InfoPanel.add(btnChiudiADS);
		

		
		btnAggiornaAds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(advertising!=null)
					advertising.navigate("http://pinoelefante.altervista.org/ads.html");
			}
		});
		
		lblDona.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {
				try {
					OperazioniFile.esploraWeb(URL_DONAZIONE);
				} 
				catch (Exception e) {
					int s=JOptionPane.showConfirmDialog(Interfaccia2.this, "Non è stato possibibile aprire il browser di sistema.\nVuoi copiare l'indirizzo da visitare negli appunti?", "Sito donazione", JOptionPane.YES_NO_OPTION);
					if(s==JOptionPane.YES_OPTION){
						Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
						StringSelection toCopy=new StringSelection(URL_DONAZIONE);
						clip.setContents(toCopy, toCopy);
						JOptionPane.showMessageDialog(Interfaccia2.this, "Indirizzo copiato!");
					}
				}
			}
			private final static String URL_DONAZIONE="http://pinoelefante.altervista.org/donazioni/donazione_gst.html";
		});
		
		btnCercaAggiornamenti.addActionListener(new ActionListener() {
			private ControlloAggiornamenti controller=new ControlloAggiornamenti();
			public void actionPerformed(ActionEvent arg0) {
				class ThreadAggiornameno extends Thread {
					private void rimuoviListener(){
						while(lblRisultatoAggiornamenti.getMouseListeners().length>0)
							lblRisultatoAggiornamenti.removeMouseListener(lblRisultatoAggiornamenti.getMouseListeners()[0]);
					}
					public void run(){
						btnCercaAggiornamenti.setEnabled(false);
						int versione=controller.getVersioneOnline();
						if(versione>Settings.getVersioneSoftware()){
							btnCercaAggiornamenti.setEnabled(true);
							lblRisultatoAggiornamenti.setText("<html>Versione online: "+versione+"<br><font color='blue'><u>Clicca qui per scaricarla</u></font></html>");
							lblRisultatoAggiornamenti.setToolTipText("Clicca per aggiornare");
							System.out.println(lblRisultatoAggiornamenti.getMouseListeners().length);
							rimuoviListener();
							lblRisultatoAggiornamenti.addMouseListener(new MouseListener() {
								public void mouseReleased(MouseEvent arg0) {}
								public void mousePressed(MouseEvent arg0) {}
								public void mouseExited(MouseEvent arg0) {}
								public void mouseEntered(MouseEvent arg0) {}
								public void mouseClicked(MouseEvent arg0) {
									JOptionPane.showMessageDialog(Interfaccia2.this, "Prova");
								}
							});
						}
						else {
							lblRisultatoAggiornamenti.setText("<html>Nessun aggiornamento.</html>");
							lblRisultatoAggiornamenti.setToolTipText("");
							rimuoviListener();
							btnCercaAggiornamenti.setEnabled(true);
						}
					}
				}
				Thread t=new ThreadAggiornameno();
				t.start();
			}
		});
		
		btnChiudiADS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NativeInterface.close();
				InfoPanel.remove(advertising);
				InfoPanel.remove(btnAggiornaAds);
				InfoPanel.remove(btnChiudiADS);
				InfoPanel.revalidate();
				InfoPanel.repaint();
			}
		});
		
		comboBoxMinutiRicercaAutomatica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int minuti=(Integer)comboBoxMinutiRicercaAutomatica.getSelectedItem();
				int ore=minuti/60;
				minuti=minuti%60;
				lblRicercaOre.setText("( "+ore+((ore==1)?" ora ":" ore ")+minuti+" min )");
			}
		});
		
		VLCPanel.addToPlaylist("D:\\SerieTV\\Alcatraz\\Alcatraz.S01E01.HDTV.XviD-LOL.[VTV].avi");
		buttonVLCPlay.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				VLCPanel.play();
			}
		});
		buttonVLCStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VLCPanel.stop();
			}
		});
		btnVLCFullscreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VLCPanel.set_fullscreen();
			}
		});
		
		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {
				advertising.disposeNativePeer();
				NativeInterface.close();
				VLCPanel.stop();
				VLCPanel.release();
				
			}
			public void windowClosed(WindowEvent arg0) {}
			public void windowActivated(WindowEvent arg0) {}
		});
		
	}
}
