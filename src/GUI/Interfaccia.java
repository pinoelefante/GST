package GUI;

import javax.swing.JFrame;

import Programma.ControlloAggiornamenti;
import Programma.Download;
import Programma.FileManager;
import Programma.ManagerException;
import Programma.OperazioniFile;
import Programma.Settings;
import LettoreVideo.Player;
import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;
import SerieTV.Torrent;
import Sottotitoli.GestoreSottotitoli;
import Sottotitoli.ProviderSottotitoli;
import Sottotitoli.SerieSub;
import Sottotitoli.SerieSubSubsfactory;
import StruttureDati.serietv.Episodio;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

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

import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.BevelBorder;

public class Interfaccia extends JFrame {
	private static Interfaccia thisframe;
	private static final long  serialVersionUID = 1L;
	private JPanel			 InfoPanel;
	private JTextField		 txt_itasa_user;
	private JPasswordField	 txt_itasa_pass;
	private JTextField		 txt_utorrent_path;
	private JTextField		 txt_download_path;
	private JTextField		 txt_vlc_path;

	private Player			 VLCPanel;
	private JPanel			 LettorePanel;
	private JWebBrowser		advertising;
	private JTextField		 txt_itasa_cerca;
	private JTextField		 txt_subsfactory_cerca;
	private JTable			 tableSubDownloaded;
	private JTextField		 txt_sub_custom_serie;
	private JTextField		 txt_sub_custom_stagione;
	private JTextField		 txt_sub_custom_episodio;
	private JTextField		 txt_sub_custom_destinazione;
	private JTextField		 txt_cerca_serie_tutte;
	private JTextField		 txt_cerca_serie_inserite;
	private JTextField		 txt_add_episodio_stagione;
	private JTextField		 txt_add_episodio_episodio;
	private JTextField		 txt_add_episodio_link;
	private JPlaylist		  playlist;
	private JComboBox<SerieTV> cmb_serie_aggiunte;
	private JComboBox<SerieTV> cmb_serie_aggiunte_add_episodio;
	private JComboBox<SerieTV> cmb_serie_lettore;
	private JComboBox<SerieTV> cmb_serie_sottotitoli;
	private JComboBox<SerieTV> cmb_serie_tutte;

	private JSlider			slider_volume;
	private JLabel			 lblTimer;
	private JLabel			 imgVolume;
	private JSlider			slider_time;
	private JPanel			 panel_scroll_download;
	private JButton			btnAggiorna;
	private JPanel			 panel_nuove_serie;
	private JButton			buttonReloadSerie;
	private JComboBox<String>  comboBoxLettoreOrdine;
	private JComboBox<Integer> comboBoxLettoreStagione;
	private JPanel			 panel_elenco_puntate_lettore;
	private JCheckBox		  chckbxNascondiViste;
	private JCheckBox		  chckbxNascondiIgnorate;
	private JCheckBox		  chckbxNascondiRimosse;
	private JComboBox<String>  cmb_down_selezione;
	private JCheckBox		  chckbxSempreInPrimo;
	private JCheckBox		  chckbxChiediConfermaPrima;
	private JCheckBox		  chckbxExternalVLC;
	private JCheckBox		  chckbxAvviaConIl;
	private JCheckBox		  chckbxAvviaRidottoA;
	private JCheckBox		  chckbxTrayOnIcon;
	private JCheckBox		  chckbxAutoAbilita;
	private JComboBox<Integer> comboBoxMinutiRicercaAutomatica;
	private JCheckBox		  chckbxAbilitaDownloadSottotitoli;
	private JCheckBox		  chckbxAbilitaItaliansubsnet;
	private JLabel			 lblItasaloginresult;
	private JButton			btnDirDownloadSfoglia;
	private JButton			btnUtorrentSfoglia;
	private JButton			btnVLCSfoglia;
	private JButton			btnOpzioniSalva;
	private JButton			btnAggiungiTorrent;
	private JLabel			 imgItasaLogo;
	private JLabel			 imgSubsfactoryLogo;
	private JLabel			 imgSubspediaLogo;
	private JButton			btnAggiungi;
	private JButton			btnRimuovi;
	private JButton			btnScarica;
	private JButton			btnIgnora;
	private JButton			btnTest;
	private JLabel			 lblDona;
	private JButton			btnCercaAggiornamenti;
	private JButton			btnAggiornaAds;
	private JButton			btnChiudiADS;
	private JButton			btnVLCInstance;
	private JButton			buttonVLCPlay;
	private JButton			buttonVLCStop;
	private JButton			btnVLCPrec;
	private JButton			btnVLCNext;
	private JButton			btnVLCFullscreen;
	private JLabel			 lblRisultatoAggiornamenti;
	private JLabel			 lblVersioneAttuale;
	private JLabel			 lblRicercaOre;
	private JTabbedPane		tab;
	private JPanel			 OpzioniPanel;
	private JButton			btnLettoreUpdate;
	private JPanel			 panel_12;
	private JPanel			 panel_13;
	private JButton			btnAggiornaElencoRegole;
	private JPanel			 panel_regole_scroll;
	private JPanel			 PreferenzeSeriePanel;
	private JPanel			 SottotitoliPanel;
	private JPanel			 ManagerCopie;
	private JButton			btnOffline;
	private JPanel panel_SottotitoliDaScaricare;
	private JComboBox<SerieSub> cmb_itasa_serie;
	private JComboBox<SerieSub> cmb_subsfactory_serie;
	private JButton btnItasaAssocia;
	private JButton btnSubsfactoryUpdate;
	private JButton btnItasaUpdate;
	private JLabel lblItasaSerieAss;
	private JLabel lblSubsfactorySerieAss;
	private JButton btnItasaRimuovi;
	private JButton btnSubsfactoryAssocia;
	private JButton btnSubsfactoryRimuovi;

	@SuppressWarnings("serial")
	public Interfaccia() {
		super("Gestione Serie TV rel." + Settings.getVersioneSoftware() + " by pinoelefante");
		thisframe = this;
		setIconImage(Toolkit.getDefaultToolkit().getImage(Interfaccia.class.getResource("/GUI/res/icona32.png")));

		setResizable(false);
		setAlwaysOnTop(Settings.isAlwaysOnTop());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 600);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		if (res.getWidth() > 750) {
			int x_screen = (int) (res.getWidth() - 750) / 2;
			setLocation(x_screen, 10);
		}

		tab = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tab, BorderLayout.CENTER);

		JPanel DownloadPanel = new JPanel();
		tab.addTab("Download", new ImageIcon(Interfaccia.class.getResource("/GUI/res/download.png")), DownloadPanel, null);
		tab.setEnabledAt(0, true);
		DownloadPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new TitledBorder(null, "Nuove serie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setBounds(485, 0, 254, 223);
		scrollPane.getVerticalScrollBar().setUnitIncrement(5);
		DownloadPanel.add(scrollPane);

		panel_nuove_serie = new JPanel();
		panel_nuove_serie.setLayout(new GridLayout(1, 1));
		scrollPane.setViewportView(panel_nuove_serie);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new TitledBorder(null, "Aggiungi/Rimuovi serie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_9.setBounds(0, 0, 485, 140);
		DownloadPanel.add(panel_9);
		panel_9.setLayout(null);

		JLabel lblSerieDisponibili = new JLabel("<html><font size=4><b>Serie disponibili</b><html>");
		lblSerieDisponibili.setHorizontalAlignment(SwingConstants.CENTER);
		lblSerieDisponibili.setBounds(10, 15, 222, 22);
		panel_9.add(lblSerieDisponibili);

		JLabel lblCerca_2 = new JLabel("Cerca");
		lblCerca_2.setBounds(10, 45, 42, 14);
		panel_9.add(lblCerca_2);

		txt_cerca_serie_tutte = new JTextField();
		txt_cerca_serie_tutte.setBounds(73, 37, 159, 27);
		panel_9.add(txt_cerca_serie_tutte);
		txt_cerca_serie_tutte.setColumns(20);

		cmb_serie_tutte = new JComboBox<SerieTV>();
		cmb_serie_tutte.setBounds(73, 68, 159, 20);
		panel_9.add(cmb_serie_tutte);

		JLabel lblSeleziona = new JLabel("Seleziona");
		lblSeleziona.setBounds(10, 70, 55, 16);
		panel_9.add(lblSeleziona);

		JLabel lblNewLabel = new JLabel("<html><font size=4><b>Serie aggiunte</b></font></html>");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(253, 15, 222, 22);
		panel_9.add(lblNewLabel);

		JLabel lblCerca_3 = new JLabel("Cerca");
		lblCerca_3.setBounds(253, 45, 42, 16);
		panel_9.add(lblCerca_3);

		JLabel lblSeleziona_1 = new JLabel("Seleziona");
		lblSeleziona_1.setBounds(252, 70, 55, 16);
		panel_9.add(lblSeleziona_1);

		txt_cerca_serie_inserite = new JTextField();
		txt_cerca_serie_inserite.setBounds(315, 37, 159, 27);
		panel_9.add(txt_cerca_serie_inserite);
		txt_cerca_serie_inserite.setColumns(10);

		cmb_serie_aggiunte = new JComboBox<SerieTV>();
		cmb_serie_aggiunte.setBounds(315, 68, 159, 20);
		panel_9.add(cmb_serie_aggiunte);

		btnAggiungi = new JButton("Aggiungi");
		btnAggiungi.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/add.png")));
		btnAggiungi.setBounds(95, 95, 103, 26);
		panel_9.add(btnAggiungi);

		btnRimuovi = new JButton("Rimuovi");
		btnRimuovi.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/remove.png")));
		btnRimuovi.setBounds(335, 95, 103, 26);
		panel_9.add(btnRimuovi);

		buttonReloadSerie = new JButton("");
		buttonReloadSerie.setToolTipText("Ricarica elenco serie");
		buttonReloadSerie.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/aggiorna.png")));
		buttonReloadSerie.setBounds(197, 15, 33, 23);
		panel_9.add(buttonReloadSerie);

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Aggiungi episodio dall'esterno", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_10.setBounds(0, 143, 485, 80);
		DownloadPanel.add(panel_10);
		panel_10.setLayout(null);

		JLabel lblSerie_2 = new JLabel("Serie");
		lblSerie_2.setBounds(12, 38, 40, 16);
		panel_10.add(lblSerie_2);

		cmb_serie_aggiunte_add_episodio = new JComboBox<SerieTV>();
		cmb_serie_aggiunte_add_episodio.setBounds(57, 36, 150, 20);
		panel_10.add(cmb_serie_aggiunte_add_episodio);

		JLabel lblStagione_2 = new JLabel("Stagione");
		lblStagione_2.setBounds(225, 25, 55, 16);
		panel_10.add(lblStagione_2);

		txt_add_episodio_stagione = new JTextField();
		txt_add_episodio_stagione.addKeyListener(new TextListenerOnlyNumber(txt_add_episodio_stagione));
		txt_add_episodio_stagione.setBounds(280, 20, 30, 24);
		panel_10.add(txt_add_episodio_stagione);
		txt_add_episodio_stagione.setColumns(3);

		JLabel lblEpisodio_1 = new JLabel("Episodio");
		lblEpisodio_1.setBounds(325, 25, 55, 16);
		panel_10.add(lblEpisodio_1);

		txt_add_episodio_episodio = new JTextField();
		txt_add_episodio_episodio.addKeyListener(new TextListenerOnlyNumber(txt_add_episodio_episodio));
		txt_add_episodio_episodio.setBounds(380, 20, 30, 24);
		panel_10.add(txt_add_episodio_episodio);
		txt_add_episodio_episodio.setColumns(3);

		JLabel lblLink = new JLabel("Link");
		lblLink.setBounds(225, 52, 40, 16);
		panel_10.add(lblLink);

		txt_add_episodio_link = new JTextField();
		txt_add_episodio_link.setBounds(266, 48, 144, 27);
		txt_add_episodio_link.setToolTipText("Incolla il testo copiato cliccando il tasto destro del mouse");
		panel_10.add(txt_add_episodio_link);
		txt_add_episodio_link.setColumns(10);

		btnAggiungiTorrent = new JButton("");
		btnAggiungiTorrent.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/add.png")));
		btnAggiungiTorrent.setBounds(428, 30, 40, 26);
		panel_10.add(btnAggiungiTorrent);

		JScrollPane scrollPaneDownload = new JScrollPane();
		scrollPaneDownload.setBounds(0, 257, 375, 269);
		scrollPaneDownload.getVerticalScrollBar().setUnitIncrement(8);
		DownloadPanel.add(scrollPaneDownload);

		panel_scroll_download = new JPanel();
		scrollPaneDownload.setViewportView(panel_scroll_download);
		panel_scroll_download.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_info_episodio = new JPanel();
		panel_info_episodio.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_info_episodio.setBounds(385, 257, 344, 269);
		DownloadPanel.add(panel_info_episodio);

		btnTest = new JButton("test");
		panel_info_episodio.add(btnTest);

		btnAggiorna = new JButton("Aggiorna");

		btnAggiorna.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/aggiorna.png")));
		btnAggiorna.setBounds(10, 228, 105, 23);
		DownloadPanel.add(btnAggiorna);

		btnScarica = new JButton("Scarica");
		btnScarica.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/download.png")));
		btnScarica.setBounds(127, 228, 114, 23);
		DownloadPanel.add(btnScarica);

		btnIgnora = new JButton("Ignora");
		btnIgnora.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/remove.png")));
		btnIgnora.setBounds(253, 228, 97, 23);
		DownloadPanel.add(btnIgnora);

		JLabel lblselezione = new JLabel("<html>Selezione</html>");
		lblselezione.setBounds(361, 232, 55, 16);
		DownloadPanel.add(lblselezione);

		cmb_down_selezione = new JComboBox<String>();
		cmb_down_selezione.setBounds(422, 228, 140, 24);
		DownloadPanel.add(cmb_down_selezione);

		btnOffline = new JButton("");
		btnOffline.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/offline.png")));
		btnOffline.setBounds(703, 228, 26, 26);
		DownloadPanel.add(btnOffline);
		cmb_down_selezione.addItem("Seleziona tutto");
		cmb_down_selezione.addItem("Deseleziona tutto");

		PreferenzeSeriePanel = new JPanel();
		tab.addTab("Regole", new ImageIcon(Interfaccia.class.getResource("/GUI/res/preferiti.png")), PreferenzeSeriePanel, null);
		PreferenzeSeriePanel.setLayout(new BorderLayout(0, 0));

		panel_12 = new JPanel();
		PreferenzeSeriePanel.add(panel_12, BorderLayout.NORTH);
		panel_12.setLayout(new BorderLayout(0, 0));

		panel_13 = new JPanel();
		panel_12.add(panel_13, BorderLayout.EAST);

		btnAggiornaElencoRegole = new JButton("");
		btnAggiornaElencoRegole.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/aggiorna.png")));
		panel_13.add(btnAggiornaElencoRegole);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.getVerticalScrollBar().setUnitIncrement(5);
		PreferenzeSeriePanel.add(scrollPane_1, BorderLayout.CENTER);

		panel_regole_scroll = new JPanel();
		scrollPane_1.setViewportView(panel_regole_scroll);
		panel_regole_scroll.setLayout(new GridLayout(1, 0, 0, 0));

		SottotitoliPanel = new JPanel();
		tab.addTab("Sottotitoli", new ImageIcon(Interfaccia.class.getResource("/GUI/res/sottotitoli.png")), SottotitoliPanel, null);
		SottotitoliPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Associatore", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(0, 0, 739, 200);
		SottotitoliPanel.add(panel);
		panel.setLayout(null);

		imgItasaLogo = new JLabel("");
		imgItasaLogo.setToolTipText("Clicca per visitare ItalianSubs");
		imgItasaLogo.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/itasa.png")));
		imgItasaLogo.setBounds(10, 49, 208, 37);
		panel.add(imgItasaLogo);

		JLabel lblitaliansubs = new JLabel("<html><b>ItalianSubs</b></html>");
		lblitaliansubs.setBounds(82, 85, 73, 14);
		panel.add(lblitaliansubs);

		JLabel lblSerieTV = new JLabel("<html><b>Serie</b><html>");
		lblSerieTV.setBounds(172, 24, 46, 14);
		panel.add(lblSerieTV);

		cmb_serie_sottotitoli = new JComboBox<SerieTV>();
		cmb_serie_sottotitoli.setBounds(228, 21, 238, 20);
		panel.add(cmb_serie_sottotitoli);

		imgSubsfactoryLogo = new JLabel("");
		imgSubsfactoryLogo.setToolTipText("Clicca per visitare Subsfactory");
		imgSubsfactoryLogo.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/subsfactory.jpg")));
		imgSubsfactoryLogo.setBounds(258, 49, 208, 37);
		panel.add(imgSubsfactoryLogo);

		JLabel lblsubsfactory = new JLabel("<html><b>Subsfactory</b></html>");
		lblsubsfactory.setBounds(313, 85, 82, 14);
		panel.add(lblsubsfactory);

		imgSubspediaLogo = new JLabel("");
		imgSubspediaLogo.setToolTipText("Clicca per visitare Subspedia");
		imgSubspediaLogo.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/subspedia.png")));
		imgSubspediaLogo.setBounds(505, 49, 208, 37);
		panel.add(imgSubspediaLogo);

		JLabel lblsubspedia = new JLabel("<html><b>Subspedia</b></html>");
		lblsubspedia.setBounds(581, 87, 73, 14);
		panel.add(lblsubspedia);

		JLabel lblCerca = new JLabel("Cerca");
		lblCerca.setBounds(10, 105, 46, 14);
		panel.add(lblCerca);

		txt_itasa_cerca = new JTextField();
		txt_itasa_cerca.setBounds(57, 100, 161, 27);
		panel.add(txt_itasa_cerca);
		txt_itasa_cerca.setColumns(20);

		cmb_itasa_serie = new JComboBox<SerieSub>();
		cmb_itasa_serie.setBounds(10, 125, 208, 20);
		panel.add(cmb_itasa_serie);

		btnItasaAssocia = new JButton("Associa");
		btnItasaAssocia.setBounds(10, 163, 98, 26);
		panel.add(btnItasaAssocia);

		btnItasaRimuovi = new JButton("Rimuovi");
		btnItasaRimuovi.setBounds(120, 163, 98, 26);
		panel.add(btnItasaRimuovi);

		JLabel lblItasaAssociata = new JLabel("Associata a: ");
		lblItasaAssociata.setBounds(10, 145, 73, 16);
		panel.add(lblItasaAssociata);

		lblItasaSerieAss = new JLabel("");
		lblItasaSerieAss.setBounds(82, 145, 164, 16);
		panel.add(lblItasaSerieAss);

		JLabel lblCerca_1 = new JLabel("Cerca");
		lblCerca_1.setBounds(258, 105, 46, 14);
		panel.add(lblCerca_1);

		txt_subsfactory_cerca = new JTextField();
		txt_subsfactory_cerca.setBounds(302, 100, 164, 27);
		panel.add(txt_subsfactory_cerca);
		txt_subsfactory_cerca.setColumns(20);

		cmb_subsfactory_serie = new JComboBox<SerieSub>();
		cmb_subsfactory_serie.setBounds(258, 125, 208, 20);
		panel.add(cmb_subsfactory_serie);

		JLabel lblSubsfactoryAssociata = new JLabel("Associata a:");
		lblSubsfactoryAssociata.setBounds(258, 145, 73, 16);
		panel.add(lblSubsfactoryAssociata);

		lblSubsfactorySerieAss = new JLabel("");
		lblSubsfactorySerieAss.setBounds(330, 145, 164, 16);
		panel.add(lblSubsfactorySerieAss);

		btnSubsfactoryAssocia = new JButton("Associa");
		btnSubsfactoryAssocia.setBounds(258, 163, 98, 26);
		panel.add(btnSubsfactoryAssocia);

		btnSubsfactoryRimuovi = new JButton("Rimuovi");
		btnSubsfactoryRimuovi.setBounds(368, 163, 98, 26);
		panel.add(btnSubsfactoryRimuovi);
		
		btnItasaUpdate = new JButton("");
		btnItasaUpdate.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/aggiorna.png")));
		btnItasaUpdate.setBounds(220, 122, 26, 26);
		panel.add(btnItasaUpdate);
		
		btnSubsfactoryUpdate = new JButton("");
		btnSubsfactoryUpdate.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/aggiorna.png")));
		btnSubsfactoryUpdate.setBounds(468, 122, 26, 26);
		panel.add(btnSubsfactoryUpdate);

		JScrollPane scrollPane_subscaricare = new JScrollPane();
		scrollPane_subscaricare.setBounds(0, 202, 370, 220);
		scrollPane_subscaricare.getVerticalScrollBar().setUnitIncrement(5);
		SottotitoliPanel.add(scrollPane_subscaricare);

		JLabel lblSottotitoliDaScaricare = new JLabel("Sottotitoli da scaricare");
		scrollPane_subscaricare.setColumnHeaderView(lblSottotitoliDaScaricare);

		panel_SottotitoliDaScaricare = new JPanel();
		scrollPane_subscaricare.setViewportView(panel_SottotitoliDaScaricare);
		panel_SottotitoliDaScaricare.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane_logsub = new JScrollPane();
		scrollPane_logsub.setBounds(372, 202, 367, 220);
		scrollPane_logsub.getVerticalScrollBar().setUnitIncrement(5);
		SottotitoliPanel.add(scrollPane_logsub);

		tableSubDownloaded = new JTable();
		tableSubDownloaded.setModel(new DefaultTableModel(new String[] { "Serie", "Stagione", "Episodio", "da" },0) {
			boolean[] columnEditables = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableSubDownloaded.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableSubDownloaded.getColumnModel().getColumn(0).setMaxWidth(150);
		tableSubDownloaded.getColumnModel().getColumn(1).setPreferredWidth(60);
		tableSubDownloaded.getColumnModel().getColumn(1).setMinWidth(20);
		tableSubDownloaded.getColumnModel().getColumn(1).setMaxWidth(60);
		tableSubDownloaded.getColumnModel().getColumn(2).setPreferredWidth(50);
		tableSubDownloaded.getColumnModel().getColumn(2).setMinWidth(20);
		tableSubDownloaded.getColumnModel().getColumn(2).setMaxWidth(50);
		tableSubDownloaded.getColumnModel().getColumn(3).setPreferredWidth(110);
		tableSubDownloaded.getColumnModel().getColumn(3).setMaxWidth(125);
		tableSubDownloaded.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSubDownloaded.setRowSelectionAllowed(false);
		scrollPane_logsub.setViewportView(tableSubDownloaded);

		JPanel panel_7 = new JPanel();
		panel_7.setBounds(0, 0, 10, 10);
		SottotitoliPanel.add(panel_7);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(null, "Scarica sottotitolo da un provider", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.setBounds(0, 426, 739, 103);
		SottotitoliPanel.add(panel_8);
		panel_8.setLayout(null);

		JLabel lblProvider = new JLabel("Provider");
		lblProvider.setBounds(12, 25, 55, 16);
		panel_8.add(lblProvider);

		JComboBox<ProviderSottotitoli> cmb_sub_custom_provider = new JComboBox<ProviderSottotitoli>();
		cmb_sub_custom_provider.setBounds(72, 21, 200, 20);
		panel_8.add(cmb_sub_custom_provider);

		JLabel lblSerie_1 = new JLabel("Serie");
		lblSerie_1.setBounds(12, 53, 55, 16);
		panel_8.add(lblSerie_1);

		JComboBox<SerieSub> cmb_sub_custom_serie = new JComboBox<SerieSub>();
		cmb_sub_custom_serie.setBounds(72, 75, 200, 20);
		panel_8.add(cmb_sub_custom_serie);

		txt_sub_custom_serie = new JTextField();
		txt_sub_custom_serie.setBounds(72, 45, 200, 27);
		panel_8.add(txt_sub_custom_serie);
		txt_sub_custom_serie.setColumns(30);

		JLabel lblStagione_1 = new JLabel("Stagione");
		lblStagione_1.setBounds(291, 25, 55, 16);
		panel_8.add(lblStagione_1);

		txt_sub_custom_stagione = new JTextField();
		txt_sub_custom_stagione.addKeyListener(new TextListenerOnlyNumber(txt_sub_custom_stagione));

		txt_sub_custom_stagione.setBounds(345, 22, 40, 24);
		panel_8.add(txt_sub_custom_stagione);
		txt_sub_custom_stagione.setColumns(10);

		JLabel lblEpisodio = new JLabel("Episodio");
		lblEpisodio.setBounds(290, 53, 55, 16);
		panel_8.add(lblEpisodio);

		txt_sub_custom_episodio = new JTextField();
		txt_sub_custom_episodio.addKeyListener(new TextListenerOnlyNumber(txt_sub_custom_episodio));
		txt_sub_custom_episodio.setBounds(345, 48, 40, 24);
		panel_8.add(txt_sub_custom_episodio);
		txt_sub_custom_episodio.setColumns(10);

		JLabel lblDestinazione = new JLabel("Destinazione");
		lblDestinazione.setBounds(394, 25, 73, 16);
		panel_8.add(lblDestinazione);

		txt_sub_custom_destinazione = new JTextField();
		txt_sub_custom_destinazione.setEditable(false);
		txt_sub_custom_destinazione.setBounds(473, 20, 160, 27);
		panel_8.add(txt_sub_custom_destinazione);
		txt_sub_custom_destinazione.setColumns(10);

		JButton btn_sub_custom_Sfoglia = new JButton("Sfoglia");
		btn_sub_custom_Sfoglia.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/cartella.png")));
		btn_sub_custom_Sfoglia.setBounds(636, 21, 93, 24);
		panel_8.add(btn_sub_custom_Sfoglia);

		JButton btn_sub_custom_Scarica = new JButton("Scarica");
		btn_sub_custom_Scarica.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/download.png")));
		btn_sub_custom_Scarica.setBounds(500, 53, 120, 38);
		panel_8.add(btn_sub_custom_Scarica);

		LettorePanel = new JPanel();
		tab.addTab("Lettore", new ImageIcon(Interfaccia.class.getResource("/GUI/res/player.png")), LettorePanel, null);
		LettorePanel.setLayout(null);

		btnVLCInstance = new JButton("Carica VLC");
		btnVLCInstance.setBounds(165, 115, 100, 25);
		LettorePanel.add(btnVLCInstance);

		cmb_serie_lettore = new JComboBox<SerieTV>();
		cmb_serie_lettore.setBounds(45, 263, 260, 20);
		LettorePanel.add(cmb_serie_lettore);

		JLabel lblSerie = new JLabel("Serie");
		lblSerie.setBounds(12, 265, 37, 16);
		LettorePanel.add(lblSerie);

		JLabel lblStagione = new JLabel("Stagione");
		lblStagione.setBounds(323, 265, 55, 16);
		LettorePanel.add(lblStagione);

		comboBoxLettoreStagione = new JComboBox<Integer>();
		comboBoxLettoreStagione.setBounds(377, 263, 45, 20);
		LettorePanel.add(comboBoxLettoreStagione);

		JScrollPane scrollPaneLettoreEpisodi = new JScrollPane();
		scrollPaneLettoreEpisodi.setBounds(0, 285, 739, 210);
		scrollPaneLettoreEpisodi.getVerticalScrollBar().setUnitIncrement(8);
		LettorePanel.add(scrollPaneLettoreEpisodi);

		panel_elenco_puntate_lettore = new JPanel();
		scrollPaneLettoreEpisodi.setViewportView(panel_elenco_puntate_lettore);
		panel_elenco_puntate_lettore.setLayout(new GridLayout(1, 0, 0, 0));

		chckbxNascondiViste = new JCheckBox("Nascondi viste");
		chckbxNascondiViste.setSelected(true);
		chckbxNascondiViste.setBounds(10, 497, 112, 24);
		chckbxNascondiViste.setSelected(Settings.isLettoreNascondiViste());
		LettorePanel.add(chckbxNascondiViste);

		chckbxNascondiIgnorate = new JCheckBox("Nascondi ignorate");
		chckbxNascondiIgnorate.setSelected(true);
		chckbxNascondiIgnorate.setBounds(134, 497, 135, 24);
		chckbxNascondiIgnorate.setSelected(Settings.isLettoreNascondiIgnore());
		LettorePanel.add(chckbxNascondiIgnorate);

		chckbxNascondiRimosse = new JCheckBox("Nascondi rimosse");
		chckbxNascondiRimosse.setSelected(true);
		chckbxNascondiRimosse.setBounds(277, 497, 140, 24);
		chckbxNascondiRimosse.setSelected(Settings.isLettoreNascondiRimosso());
		LettorePanel.add(chckbxNascondiRimosse);

		playlist = new JPlaylist();
		PanelEpisodioLettore.setPlaylist(playlist);
		playlist.setBorder(new TitledBorder(null, "Playlist", TitledBorder.LEADING, TitledBorder.BOTTOM, null, null));
		playlist.setBounds(469, 0, 226, 210);
		LettorePanel.add(playlist);

		btnVLCFullscreen = new JButton("");
		btnVLCFullscreen.setToolTipText("Schermo intero");
		btnVLCFullscreen.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/fullscreen.png")));
		btnVLCFullscreen.setBounds(433, 104, 26, 26);
		LettorePanel.add(btnVLCFullscreen);

		JLabel lblOrdine = new JLabel("Ordine");
		lblOrdine.setBounds(450, 265, 44, 16);
		LettorePanel.add(lblOrdine);

		comboBoxLettoreOrdine = new JComboBox<String>();
		comboBoxLettoreOrdine.setModel(new DefaultComboBoxModel<String>(new String[] { "Crescente", "Decrescente" }));
		comboBoxLettoreOrdine.setBounds(500, 263, 112, 20);
		comboBoxLettoreOrdine.setSelectedIndex(Settings.getLettoreOrdine());
		LettorePanel.add(comboBoxLettoreOrdine);

		buttonVLCPlay = new JButton("");
		buttonVLCPlay.setToolTipText("Play");
		buttonVLCPlay.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/play_pause.png")));
		buttonVLCPlay.setBounds(433, 0, 26, 26);
		LettorePanel.add(buttonVLCPlay);

		buttonVLCStop = new JButton("");
		buttonVLCStop.setToolTipText("Stop");
		buttonVLCStop.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/stop.png")));
		buttonVLCStop.setBounds(433, 26, 26, 26);
		LettorePanel.add(buttonVLCStop);

		JButton buttonPlaylistRimuovi = playlist.getButtonDel();
		buttonPlaylistRimuovi.setToolTipText("Rimuovi dalla playlist");
		buttonPlaylistRimuovi.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/cestino.png")));
		buttonPlaylistRimuovi.setBounds(701, 172, 26, 26);
		LettorePanel.add(buttonPlaylistRimuovi);

		JButton buttonPlaylistDown = playlist.getButtonDown();
		buttonPlaylistDown.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/down.png")));
		buttonPlaylistDown.setBounds(701, 107, 26, 26);
		LettorePanel.add(buttonPlaylistDown);

		JButton buttonPlaylistUp = playlist.getButtonUp();
		buttonPlaylistUp.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/up.png")));
		buttonPlaylistUp.setBounds(701, 76, 26, 26);
		LettorePanel.add(buttonPlaylistUp);

		btnVLCPrec = new JButton("");
		btnVLCPrec.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/prev.png")));
		btnVLCPrec.setBounds(433, 52, 26, 26);
		LettorePanel.add(btnVLCPrec);

		btnVLCNext = new JButton("");
		btnVLCNext.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/nextt.png")));
		btnVLCNext.setBounds(433, 78, 26, 26);
		LettorePanel.add(btnVLCNext);

		btnLettoreUpdate = new JButton("");
		btnLettoreUpdate.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/aggiorna.png")));
		btnLettoreUpdate.setBounds(701, 260, 26, 26);
		LettorePanel.add(btnLettoreUpdate);

		ManagerCopie = new JPanel();
		tab.addTab("File Manager", new ImageIcon(Interfaccia.class.getResource("/GUI/res/salva.png")), ManagerCopie, null);
		ManagerCopie.setLayout(new BorderLayout(0, 0));
		ManagerCopie.add(FileManager.getPanel(), BorderLayout.CENTER);

		OpzioniPanel = new JPanel();
		OpzioniPanel.setBorder(new CompoundBorder());
		tab.addTab("Opzioni", new ImageIcon(Interfaccia.class.getResource("/GUI/res/opzioni.png")), OpzioniPanel, null);
		OpzioniPanel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Aspetto", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 11, 350, 144);
		OpzioniPanel.add(panel_1);
		panel_1.setLayout(null);

		chckbxSempreInPrimo = new JCheckBox("Sempre in primo piano");
		chckbxSempreInPrimo.setBounds(6, 19, 338, 23);
		panel_1.add(chckbxSempreInPrimo);

		chckbxExternalVLC = new JCheckBox("Preferisci il player esterno");
		chckbxExternalVLC.setBounds(6, 70, 338, 23);
		panel_1.add(chckbxExternalVLC);

		chckbxChiediConfermaPrima = new JCheckBox("Chiedi conferma prima di uscire");
		chckbxChiediConfermaPrima.setBounds(6, 44, 338, 23);
		panel_1.add(chckbxChiediConfermaPrima);

		chckbxAvviaConIl = new JCheckBox("Avvia con il sistema operativo");
		chckbxAvviaConIl.setBounds(6, 96, 196, 23);
		panel_1.add(chckbxAvviaConIl);

		chckbxAvviaRidottoA = new JCheckBox("Avvia ridotto a icona");
		chckbxAvviaRidottoA.setBounds(204, 96, 140, 23);
		panel_1.add(chckbxAvviaRidottoA);

		chckbxTrayOnIcon = new JCheckBox("Minimizza nella tray");
		chckbxTrayOnIcon.setBounds(6, 117, 336, 24);
		panel_1.add(chckbxTrayOnIcon);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Download automatico", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_3.setBounds(372, 11, 350, 65);
		OpzioniPanel.add(panel_3);
		panel_3.setLayout(null);

		chckbxAutoAbilita = new JCheckBox("Abilita");
		chckbxAutoAbilita.setBounds(6, 9, 97, 23);
		panel_3.add(chckbxAutoAbilita);

		comboBoxMinutiRicercaAutomatica = new JComboBox<Integer>();
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

		lblRicercaOre = new JLabel("");
		lblRicercaOre.setBounds(252, 35, 86, 16);
		lblRicercaOre.setText("( 1 ora 0 min )");
		panel_3.add(lblRicercaOre);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Sottotitoli", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_4.setBounds(10, 159, 712, 136);
		OpzioniPanel.add(panel_4);
		panel_4.setLayout(null);

		chckbxAbilitaDownloadSottotitoli = new JCheckBox("Abilita download sottotitoli");
		chckbxAbilitaDownloadSottotitoli.setBounds(6, 10, 190, 23);
		panel_4.add(chckbxAbilitaDownloadSottotitoli);

		chckbxAbilitaItaliansubsnet = new JCheckBox("Abilita ItalianSubs.net");
		chckbxAbilitaItaliansubsnet.setBounds(6, 35, 150, 23);
		panel_4.add(chckbxAbilitaItaliansubsnet);

		JLabel lblUsername = new JLabel("<html>Username ItaSa</html>");
		lblUsername.setBounds(6, 66, 91, 23);
		panel_4.add(lblUsername);

		txt_itasa_user = new JTextField();
		txt_itasa_user.setBounds(115, 64, 150, 27);
		panel_4.add(txt_itasa_user);
		txt_itasa_user.setColumns(10);

		JLabel lblPasswordItasa = new JLabel("<html>Password ItaSa</html>");
		lblPasswordItasa.setBounds(6, 92, 98, 28);
		panel_4.add(lblPasswordItasa);

		txt_itasa_pass = new JPasswordField();
		txt_itasa_pass.setBounds(115, 95, 150, 27);
		panel_4.add(txt_itasa_pass);

		JButton btnItasaVerificaLogin = new JButton("Verifica login");
		btnItasaVerificaLogin.setBounds(278, 64, 108, 26);
		panel_4.add(btnItasaVerificaLogin);

		lblItasaloginresult = new JLabel("");
		lblItasaloginresult.setBounds(278, 98, 108, 16);
		panel_4.add(lblItasaloginresult);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Programmi", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_5.setBounds(10, 307, 717, 125);
		OpzioniPanel.add(panel_5);
		panel_5.setLayout(null);

		JLabel lblPercorsoUtorrent = new JLabel("Percorso uTorrent");
		lblPercorsoUtorrent.setBounds(12, 32, 115, 16);
		panel_5.add(lblPercorsoUtorrent);

		txt_utorrent_path = new JTextField();
		txt_utorrent_path.setEditable(false);
		txt_utorrent_path.setBounds(145, 27, 240, 27);
		panel_5.add(txt_utorrent_path);
		txt_utorrent_path.setColumns(10);

		JLabel lblPercorsoDownload = new JLabel("Percorso download");
		lblPercorsoDownload.setBounds(12, 60, 115, 16);
		panel_5.add(lblPercorsoDownload);

		txt_download_path = new JTextField();
		txt_download_path.setEditable(false);
		txt_download_path.setBounds(145, 55, 240, 27);
		panel_5.add(txt_download_path);
		txt_download_path.setColumns(10);

		JLabel lblPercorsoVlc = new JLabel("Percorso VLC");
		lblPercorsoVlc.setBounds(12, 88, 115, 16);
		panel_5.add(lblPercorsoVlc);

		txt_vlc_path = new JTextField();
		txt_vlc_path.setEditable(false);
		txt_vlc_path.setBounds(145, 83, 240, 27);
		panel_5.add(txt_vlc_path);
		txt_vlc_path.setColumns(10);

		btnUtorrentSfoglia = new JButton("Sfoglia");
		btnUtorrentSfoglia.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/utorrent.png")));
		btnUtorrentSfoglia.setBounds(397, 27, 98, 26);
		panel_5.add(btnUtorrentSfoglia);

		btnDirDownloadSfoglia = new JButton("Sfoglia");
		btnDirDownloadSfoglia.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/cartella.png")));
		btnDirDownloadSfoglia.setBounds(397, 55, 98, 26);
		panel_5.add(btnDirDownloadSfoglia);

		btnVLCSfoglia = new JButton("Sfoglia");
		btnVLCSfoglia.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/vlc.png")));
		btnVLCSfoglia.setBounds(397, 83, 98, 26);
		panel_5.add(btnVLCSfoglia);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(10, 444, 717, 58);
		OpzioniPanel.add(panel_6);

		btnOpzioniSalva = new JButton("Salva");
		btnOpzioniSalva.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/salva.png")));
		panel_6.add(btnOpzioniSalva);

		JButton btnOpzioniPredefiniti = new JButton("Predefiniti");
		btnOpzioniPredefiniti.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/reset.png")));
		panel_6.add(btnOpzioniPredefiniti);

		InfoPanel = new JPanel();
		tab.addTab("Info  ", new ImageIcon(Interfaccia.class.getResource("/GUI/res/info.png")), InfoPanel, null);
		InfoPanel.setLayout(null);

		JLabel imgLogo = new JLabel("");
		imgLogo.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/logo.png")));
		imgLogo.setBounds(10, 11, 350, 350);
		InfoPanel.add(imgLogo);

		JLabel lblStoria = new JLabel("<html><font size=4><b>Gestione Serie Tv nasce dalla mia passione per le serie tv e per la programmazione, in un pomeriggio estivo.<br>Inizialmente era un semplice programma sviluppato in C (in console) che scaricava tutti gli episodi non ancora visti, sprovvisto delle funzionalit\u00E0 presenti attualmente come il download dei sottotitoli.<br><br>Mettiamo fine a questo amarcord e godiamoci quello che abbiamo oggi.</b></font>");
		lblStoria.setHorizontalAlignment(SwingConstants.CENTER);
		lblStoria.setVerticalAlignment(SwingConstants.TOP);
		lblStoria.setBounds(381, 11, 337, 220);
		InfoPanel.add(lblStoria);

		lblVersioneAttuale = new JLabel("Versione attuale: " + Settings.getVersioneSoftware());
		lblVersioneAttuale.setBounds(539, 223, 179, 20);
		InfoPanel.add(lblVersioneAttuale);

		btnCercaAggiornamenti = new JButton("Cerca aggiornamenti");
		btnCercaAggiornamenti.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/update.png")));
		btnCercaAggiornamenti.setBounds(380, 223, 152, 44);
		InfoPanel.add(btnCercaAggiornamenti);

		lblRisultatoAggiornamenti = new JLabel("Verifica disponibilit\u00E0 aggiornamenti");
		lblRisultatoAggiornamenti.setBounds(539, 242, 179, 35);
		InfoPanel.add(lblRisultatoAggiornamenti);

		lblDona = new JLabel("<html>Se il programma \u00E8 di tua utilit\u00E0, potresti pensare di effettua una donazione (tramite PayPal) cliccando sull'immagine. Un tuo piccolo gesto pu\u00F2 essere uno grande per me</html>");
		lblDona.setToolTipText("Clicca per effettuare una donazione");
		lblDona.setVerticalAlignment(SwingConstants.TOP);
		lblDona.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/dona.png")));
		lblDona.setBounds(381, 291, 337, 70);
		InfoPanel.add(lblDona);

		btnAggiornaAds = new JButton("");
		btnAggiornaAds.setToolTipText("Aggiorna pubblicit\u00E0");
		btnAggiornaAds.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/aggiorna.png")));
		btnAggiornaAds.setBounds(696, 372, 33, 23);
		InfoPanel.add(btnAggiornaAds);

		btnChiudiADS = new JButton("");
		btnChiudiADS.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/remove.png")));
		btnChiudiADS.setToolTipText("Chiudi pubblicit\u00E0");
		btnChiudiADS.setBounds(696, 492, 33, 23);
		InfoPanel.add(btnChiudiADS);

		/** TODO decommentare per la distribuzione */
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!NativeInterface.isOpen())
					NativeInterface.open();
				advertising = new JWebBrowser(JWebBrowser.destroyOnFinalization());
				advertising.setBarsVisible(false);
				advertising.setStatusBarVisible(false);
				advertising.setBounds(10, 372, 676, 143);
				advertising.navigate("http://pinoelefante.altervista.org/ads.html");
				InfoPanel.add(advertising);
			}
		});
		/* */

		addListener();
	}

	public void init() {
		/** inizializza i campi che contengono le serie tv inserite */
		class t_init extends Thread{
			public void run(){
				reloadSeriePreferite();
				reloadSerieDisponibili();

				inizializzaDownloadScroll();

				buttonReloadSerie.doClick();

				btnAggiorna.doClick();
				
				initSubDownload();
				GestioneSerieTV.getSubManager().loadLast10();
			}
		}
		Thread t=new t_init();
		t.start();
	}

	public void reloadSeriePreferite() {
		ArrayList<SerieTV> st = GestioneSerieTV.getElencoSerieInserite();

		cmb_serie_aggiunte.removeAllItems();
		cmb_serie_aggiunte_add_episodio.removeAllItems();
		cmb_serie_lettore.removeAllItems();
		cmb_serie_sottotitoli.removeAllItems();

		if (st != null) {
			for (int i = 0; i < st.size(); i++) {
				cmb_serie_aggiunte.addItem(st.get(i));
				cmb_serie_aggiunte_add_episodio.addItem(st.get(i));
				cmb_serie_lettore.addItem(st.get(i));
				cmb_serie_sottotitoli.addItem(st.get(i));
			}
			cmb_serie_lettore.getActionListeners()[0].actionPerformed(new ActionEvent(cmb_serie_lettore, 0, "", 0));
		}
		st = null;
		Runtime.getRuntime().gc();
	}

	public void reloadSerieDisponibili() {
		/** inizializza il campo che contiene tutte le serie disponibili */
		ArrayList<SerieTV> st = GestioneSerieTV.getElencoSerieCompleto();
		if (st != null) {
			cmb_serie_tutte.removeAllItems();
			for (int i = 0; i < st.size(); i++) {
				cmb_serie_tutte.addItem(st.get(i));
			}
		}
		st = null;
		Runtime.getRuntime().gc();
	}

	public void inizializzaDownloadScroll() {
		panel_scroll_download.removeAll();
		class UpdateEpisodes extends Thread {
			public void run() {
				btnAggiorna.setEnabled(false);
				ArrayList<Episodio> eps = GestioneSerieTV.caricaEpisodiDaScaricareOffline();
				for (int i = 0; i < eps.size(); i++) {
					panel_scroll_download.add(new PanelEpisodioDownload(eps.get(i)));
				}
				btnAggiorna.setEnabled(true);
			}
		}
		Thread t = new UpdateEpisodes();
		t.setName("update episodi");
		t.start();
	}

	public static Interfaccia getInterfaccia() {
		return thisframe;
	}

	public void disegnaLettore() {
		SerieTV serie = (SerieTV) cmb_serie_lettore.getSelectedItem();
		if (serie != null) {
			if(comboBoxLettoreStagione.getSelectedItem()!=null){
    			int stagione = (int) comboBoxLettoreStagione.getSelectedItem();
    			if (stagione >= 0) {
    				boolean ordine_crescente = comboBoxLettoreOrdine.getSelectedIndex() == 0 ? true : false;
    				panel_elenco_puntate_lettore.removeAll();
    
    				boolean hide_v = chckbxNascondiViste.isSelected(), // viste
    				hide_i = chckbxNascondiIgnorate.isSelected(), // ignorate
    				hide_r = chckbxNascondiRimosse.isSelected(); // rimosse
    
    				for (int i = 0; i < serie.getNumEpisodi(); i++) {
    					Episodio e = serie.getEpisodio(i);
    					if (e.getStagione() > stagione)
    						break;
    					else if (e.getStagione() == stagione) {
    						if (hide_v && e.isVisto())
    							continue;
    						if (hide_i && e.isIgnorato())
    							continue;
    						if (hide_r && e.isRimosso())
    							continue;
    
    						if (ordine_crescente){
    							PanelEpisodioLettore ep=new PanelEpisodioLettore(e);
    							if(ep.isOK())
    								panel_elenco_puntate_lettore.add(new PanelEpisodioLettore(e));
    						}
    						else {
    							PanelEpisodioLettore ep=new PanelEpisodioLettore(e);
    							if(ep.isOK())
    								panel_elenco_puntate_lettore.add(new PanelEpisodioLettore(e), 0);
    						}
    					}
    				}
    				panel_elenco_puntate_lettore.revalidate();
    				panel_elenco_puntate_lettore.repaint();
    			}
			}
		}
	}

	private void inizializzaOpzioni() {
		chckbxSempreInPrimo.setSelected(Settings.isAlwaysOnTop());
		chckbxChiediConfermaPrima.setSelected(Settings.isAskOnClose());
		chckbxExternalVLC.setSelected(Settings.isExtenalVLC());
		chckbxAvviaConIl.setSelected(Settings.isAutostart());
		chckbxAvviaRidottoA.setSelected(Settings.isStartHidden());
		chckbxTrayOnIcon.setSelected(Settings.isTrayOnIcon());
		chckbxAutoAbilita.setSelected(Settings.isDownloadAutomatico());
		comboBoxMinutiRicercaAutomatica.setSelectedItem(Settings.getMinRicerca());
		chckbxAbilitaDownloadSottotitoli.setSelected(Settings.isRicercaSottotitoli());
		chckbxAbilitaItaliansubsnet.setSelected(Settings.isEnableITASA());
		txt_itasa_user.setText(Settings.getItasaUsername());
		txt_itasa_pass.setText("");
		txt_utorrent_path.setText(Settings.getClientPath());
		txt_download_path.setText(Settings.getDirectoryDownload());
		txt_vlc_path.setText(Settings.getVLCPath());
	}

	private void addListener() {
		txt_add_episodio_link.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
					txt_add_episodio_link.setText("");
					Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
					Transferable contents = clip.getContents(null);
					boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
					if (hasTransferableText) {
						try {
							String result = (String) contents.getTransferData(DataFlavor.stringFlavor);
							txt_add_episodio_link.setText(result.trim());
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		btnAggiungiTorrent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SerieTV serie = (SerieTV) cmb_serie_aggiunte_add_episodio.getSelectedItem();
				int stagione = 0, episodio = 0;
				try {
					stagione = Integer.parseInt(txt_add_episodio_stagione.getText());
					episodio = Integer.parseInt(txt_add_episodio_episodio.getText());
				}
				catch (NumberFormatException e) {
					ManagerException.registraEccezione(e);
					JOptionPane.showMessageDialog(Interfaccia.this, "Stagione e/o episodio non possono contenere lettere");
					return;
				}
				String link = txt_add_episodio_link.getText();

				if (serie != null) {
					if (stagione <= 0 || episodio < 0) {
						System.out.println("S/E <= 0");
						return;
					}
					if (link.isEmpty()) {
						System.out.println("Link vuoto");
						return;
					}
					if (link.endsWith(".torrent") || link.startsWith("magnet:")) {
						System.out.println("Link valido");
						// return;
					}
					else {
						System.out.println("Link non valido");
						return;
					}
					Torrent t = new Torrent(serie, link, Torrent.SCARICARE);
					t.setStagione(stagione);
					t.setEpisodio(episodio);
					serie.addEpisodio(t);

					inizializzaDownloadScroll();
					JOptionPane.showMessageDialog(thisframe, serie.getNomeSerie() + " - Link aggiunto");
				}
			}
		});
		imgItasaLogo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try {
					OperazioniFile.esploraWeb("http://www.italiansubs.net");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		imgSubsfactoryLogo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					OperazioniFile.esploraWeb("http://www.subsfactory.it");
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		imgSubspediaLogo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					OperazioniFile.esploraWeb("http://subspedia.weebly.com/");
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnAggiornaAds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (advertising != null)
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
					int s = JOptionPane.showConfirmDialog(Interfaccia.this, "Non è stato possibibile aprire il browser di sistema.\nVuoi copiare l'indirizzo da visitare negli appunti?", "Sito donazione", JOptionPane.YES_NO_OPTION);
					if (s == JOptionPane.YES_OPTION) {
						Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
						StringSelection toCopy = new StringSelection(URL_DONAZIONE);
						clip.setContents(toCopy, toCopy);
						JOptionPane.showMessageDialog(Interfaccia.this, "Indirizzo copiato!");
					}
				}
			}

			private final static String URL_DONAZIONE = "http://pinoelefante.altervista.org/donazioni/donazione_gst.html";
		});

		btnCercaAggiornamenti.addActionListener(new ActionListener() {
			private ControlloAggiornamenti controller = new ControlloAggiornamenti();

			public void actionPerformed(ActionEvent arg0) {
				class ThreadAggiornameno extends Thread {
					private void rimuoviListener() {
						while (lblRisultatoAggiornamenti.getMouseListeners().length > 0)
							lblRisultatoAggiornamenti.removeMouseListener(lblRisultatoAggiornamenti.getMouseListeners()[0]);
					}

					public void run() {
						btnCercaAggiornamenti.setEnabled(false);
						int versione = controller.getVersioneOnline();
						if (versione > Settings.getVersioneSoftware()) {
							btnCercaAggiornamenti.setEnabled(true);
							lblRisultatoAggiornamenti.setText("<html>Versione online: " + versione + "<br><font color='blue'><u>Clicca qui per scaricarla</u></font></html>");
							lblRisultatoAggiornamenti.setToolTipText("Clicca per aggiornare");
							// System.out.println(lblRisultatoAggiornamenti.getMouseListeners().length);
							rimuoviListener();
							lblRisultatoAggiornamenti.addMouseListener(new MouseListener() {
								public void mouseReleased(MouseEvent arg0) {}

								public void mousePressed(MouseEvent arg0) {}

								public void mouseExited(MouseEvent arg0) {}

								public void mouseEntered(MouseEvent arg0) {}

								public void mouseClicked(MouseEvent arg0) {
									JOptionPane.showMessageDialog(Interfaccia.this, "Prova");
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
				Thread t = new ThreadAggiornameno();
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
				int minuti = (Integer) comboBoxMinutiRicercaAutomatica.getSelectedItem();
				int ore = minuti / 60;
				minuti = minuti % 60;
				lblRicercaOre.setText("( " + ore + ((ore == 1) ? " ora " : " ore ") + minuti + " min )");
			}
		});

		btnVLCInstance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				class VLCLoader extends Thread {
					private JButton source;

					public VLCLoader(JButton s) {
						source = s;
					}

					public void run() {
						source.setEnabled(false);
						source.setText("Loading...");
						try {
							VLCPanel = new Player(LettorePanel, playlist.getPlaylist());
						}
						catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(thisframe, "Errore durante il caricamento di VLC");
							source.setText("Carica VLC");
							source.setEnabled(true);
							return;
						}
						if (VLCPanel.isLinked()) {
							VLCPanel.setBounds(10, 0, 417, 235);
							LettorePanel.add(VLCPanel.getPlayerPane());
							LettorePanel.remove(source);

							slider_time = VLCPanel.getProgressSlider();
							slider_time.setBounds(11, 237, 368, 16);
							LettorePanel.add(slider_time);

							slider_volume = VLCPanel.getVolumeSlider();
							slider_volume.setOrientation(SwingConstants.VERTICAL);
							slider_volume.setBounds(433, 130, 26, 86);
							LettorePanel.add(slider_volume);

							lblTimer = VLCPanel.getCurrentTimeLabel();
							lblTimer.setHorizontalAlignment(SwingConstants.CENTER);
							lblTimer.setBounds(377, 237, 50, 16);
							LettorePanel.add(lblTimer);

							imgVolume = new JLabel("");
							imgVolume.setIcon(new ImageIcon(Interfaccia.class.getResource("/GUI/res/volume.png")));
							imgVolume.setBounds(433, 215, 26, 26);
							LettorePanel.add(imgVolume);

							VLCPanel.addListener();

							LettorePanel.revalidate();
							LettorePanel.repaint();
							playlist.setPlayer(VLCPanel);
						}
					}
				}
				Thread t = new VLCLoader((JButton) arg0.getSource());
				t.start();
			}
		});

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
		btnVLCNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VLCPanel.next();
			}
		});
		btnVLCPrec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VLCPanel.prev();
			}
		});

		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {}

			public void windowIconified(WindowEvent arg0) {}

			public void windowDeiconified(WindowEvent arg0) {}

			public void windowDeactivated(WindowEvent arg0) {}

			public void windowClosing(WindowEvent arg0) {
				if (advertising != null)
					advertising.disposeNativePeer();
				NativeInterface.close();
				if (VLCPanel != null) {
					if (VLCPanel.isLinked()) {
						VLCPanel.stop();
						VLCPanel.release();
					}
				}
			}

			public void windowClosed(WindowEvent arg0) {}

			public void windowActivated(WindowEvent arg0) {}
		});
		txt_cerca_serie_tutte.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String text = txt_cerca_serie_tutte.getText().trim().toLowerCase();
				if (text.isEmpty()) {
					cmb_serie_tutte.removeAllItems();
					ArrayList<SerieTV> t = GestioneSerieTV.getElencoSerieCompleto();
					for (int i = 0; i < t.size(); i++) {
						cmb_serie_tutte.addItem(t.get(i));
					}
				}
				else {
					cmb_serie_tutte.removeAllItems();
					ArrayList<SerieTV> t = GestioneSerieTV.getElencoSerieCompleto();
					for (int i = 0; i < t.size(); i++) {
						SerieTV s = t.get(i);
						if (s.getNomeSerie().toLowerCase().contains(text))
							cmb_serie_tutte.addItem(s);
					}
				}
			}
		});
		txt_cerca_serie_inserite.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String text = txt_cerca_serie_inserite.getText().trim().toLowerCase();
				if (text.isEmpty()) {
					cmb_serie_aggiunte.removeAllItems();
					ArrayList<SerieTV> t = GestioneSerieTV.getElencoSerieInserite();
					for (int i = 0; i < t.size(); i++) {
						cmb_serie_aggiunte.addItem(t.get(i));
					}
				}
				else {
					cmb_serie_aggiunte.removeAllItems();
					ArrayList<SerieTV> t = GestioneSerieTV.getElencoSerieInserite();
					for (int i = 0; i < t.size(); i++) {
						SerieTV s = t.get(i);
						if (s.getNomeSerie().toLowerCase().contains(text))
							cmb_serie_aggiunte.addItem(t.get(i));
					}
				}
			}
		});
		btnAggiungi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final SerieTV s = (SerieTV) cmb_serie_tutte.getSelectedItem();
				if (s != null) {
					if (GestioneSerieTV.aggiungiSeriePreferita(s)) {
						reloadSeriePreferite();
						class ThreadAggiungi extends Thread {
							public void run() {
								s.aggiornaEpisodiOnline();
								inizializzaDownloadScroll();
							}
						}
						Thread t_aggiungi = new ThreadAggiungi();
						t_aggiungi.start();
					}
				}
			}
		});
		btnRimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SerieTV s = (SerieTV) cmb_serie_aggiunte.getSelectedItem();
				if (s != null) {
					GestioneSerieTV.rimuoviSeriePreferita(s);
					reloadSeriePreferite();
					inizializzaDownloadScroll();
				}
			}
		});
		btnAggiorna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				class UpdateEpisodes extends Thread {
					public void run() {
						btnAggiorna.setEnabled(false);
						try {
							ArrayList<Episodio> eps = GestioneSerieTV.caricaEpisodiDaScaricare();
							panel_scroll_download.removeAll();
							Runtime.getRuntime().gc();
							for (int i = 0; i < eps.size(); i++) {
								panel_scroll_download.add(new PanelEpisodioDownload(eps.get(i)));
							}
							btnAggiorna.setEnabled(true);
						}
						catch (Exception e) {
							btnAggiorna.setEnabled(true);
						}
					}
				}
				Thread t = new UpdateEpisodes();
				t.setName("update episodi");
				t.start();

			}
		});
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		panel_scroll_download.addContainerListener(new ContainerListener() {
			public void componentRemoved(ContainerEvent arg0) {
				GridLayout l = (GridLayout) panel_scroll_download.getLayout();
				if (panel_scroll_download.getComponentCount() <= 2)
					l.setRows(2);
				else
					l.setRows(l.getRows() - 1);
				panel_scroll_download.revalidate();
				panel_scroll_download.repaint();
			}

			public void componentAdded(ContainerEvent arg0) {
				GridLayout l = (GridLayout) panel_scroll_download.getLayout();
				if (panel_scroll_download.getComponentCount() <= 2)
					l.setRows(2);
				else
					l.setRows(l.getRows() + 1);
				panel_scroll_download.revalidate();
				panel_scroll_download.repaint();
			}
		});
		panel_nuove_serie.addContainerListener(new ContainerListener() {
			public void componentRemoved(ContainerEvent arg0) {
				GridLayout lay = (GridLayout) panel_nuove_serie.getLayout();
				if (panel_nuove_serie.getComponentCount() <= 3) {
					lay.setRows(3);
				}
				else
					lay.setRows(lay.getRows() - 1);
				panel_nuove_serie.revalidate();
				panel_nuove_serie.repaint();
			}

			public void componentAdded(ContainerEvent arg0) {
				GridLayout lay = (GridLayout) panel_nuove_serie.getLayout();
				if (panel_nuove_serie.getComponentCount() <= 3) {
					lay.setRows(3);
				}
				else
					lay.setRows(lay.getRows() + 1);
				panel_nuove_serie.revalidate();
				panel_nuove_serie.repaint();
			}
		});
		buttonReloadSerie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				class updateSeries extends Thread {
					public void run() {
						buttonReloadSerie.setEnabled(false);
						try {
							GestioneSerieTV.caricaElencoSerieOnline();
							buttonReloadSerie.setEnabled(true);
						}
						catch (Exception e) {
							e.printStackTrace();
							buttonReloadSerie.setEnabled(true);
						}
						reloadSerieDisponibili();
						ArrayList<SerieTV> newseries = GestioneSerieTV.getElencoNuoveSerie();
						if (newseries.size() > 0) {
							panel_nuove_serie.removeAll();
							for (int i = 0; i < newseries.size(); i++) {
								panel_nuove_serie.add(new PanelNewSerie(newseries.get(i)));
							}
						}
					}
				}
				Thread t = new updateSeries();
				t.start();
			}
		});
		cmb_serie_lettore.addActionListener(new ActionListener() {
			private SerieTV last_serie = null;

			public void actionPerformed(ActionEvent arg0) {
				if (cmb_serie_lettore.getSelectedItem() == null) {
					comboBoxLettoreStagione.removeAllItems();
					panel_elenco_puntate_lettore.removeAll();
					panel_elenco_puntate_lettore.revalidate();
					panel_elenco_puntate_lettore.repaint();
					return;
				}

				if (cmb_serie_lettore.getSelectedItem() != last_serie) {
					int index_stagione = comboBoxLettoreStagione.getSelectedIndex();
					comboBoxLettoreStagione.removeAllItems();
					SerieTV serie = (SerieTV) cmb_serie_lettore.getSelectedItem();
					ArrayList<Integer> stagioni = new ArrayList<Integer>();
					for (int i = 0; i < serie.getNumEpisodi(); i++) {
						int stagione = serie.getEpisodio(i).getStagione();
						if (!stagioni.contains(stagione)) {
							stagioni.add(stagione);
							comboBoxLettoreStagione.addItem(stagione);
						}
					}
					if (stagioni.size() > 0) {
						comboBoxLettoreStagione.setSelectedIndex(0);
						if (index_stagione == 0)
							disegnaLettore();
					}
				}
			}
		});
		comboBoxLettoreStagione.addActionListener(new ActionListener() {
			private int last_season = -1;

			public void actionPerformed(ActionEvent e) {
				if (comboBoxLettoreStagione.getSelectedItem() != null) {
					if ((int) comboBoxLettoreStagione.getSelectedItem() != last_season) {
						disegnaLettore();
						last_season = comboBoxLettoreStagione.getSelectedItem() == null ? -1 : (int) comboBoxLettoreStagione.getSelectedItem();
					}
				}
			}
		});
		comboBoxLettoreOrdine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.setLettoreOrdine(comboBoxLettoreOrdine.getSelectedIndex());
				disegnaLettore();
			}
		});
		panel_elenco_puntate_lettore.addContainerListener(new ContainerListener() {
			public void componentRemoved(ContainerEvent arg0) {
				GridLayout layout = (GridLayout) panel_elenco_puntate_lettore.getLayout();
				if (panel_elenco_puntate_lettore.getComponentCount() <= 3)
					layout.setRows(2);
				else
					layout.setRows(layout.getRows() - 1);
			}

			public void componentAdded(ContainerEvent arg0) {
				GridLayout layout = (GridLayout) panel_elenco_puntate_lettore.getLayout();
				if (panel_elenco_puntate_lettore.getComponentCount() <= 2)
					layout.setRows(2);
				else
					layout.setRows(layout.getRows() + 1);
			}
		});
		chckbxNascondiViste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.setLettoreNascondiViste(chckbxNascondiViste.isSelected());
				disegnaLettore();
			}
		});
		chckbxNascondiIgnorate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.setLettoreNascondiIgnore(chckbxNascondiIgnorate.isSelected());
				disegnaLettore();
			}
		});
		chckbxNascondiRimosse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.setLettoreNascondiRimosso(chckbxNascondiRimosse.isSelected());
				disegnaLettore();
			}
		});
		btnScarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < panel_scroll_download.getComponentCount(); ) {
					if (panel_scroll_download.getComponent(i) instanceof PanelEpisodioDownload) {
						PanelEpisodioDownload p = (PanelEpisodioDownload) panel_scroll_download.getComponent(i);
						if (p.isDownloadSelected()) {
							Torrent t = p.getLink();
							if (t != null) {
								try {
									Download.downloadMagnet(t.getUrl(), Settings.getDirectoryDownload() + File.separator + t.getSerieTV().getFolderSerie());
									p.scarica(t);
									panel_scroll_download.remove(p);
								}
								catch (IOException e) {
									ManagerException.registraEccezione(e);
									e.printStackTrace();
								}
							}
						}
						else
							i++;
					}
				}
				panel_scroll_download.revalidate();
				panel_scroll_download.repaint();
			}
		});
		btnIgnora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0;i<panel_scroll_download.getComponentCount();){
					if(panel_scroll_download.getComponent(i) instanceof PanelEpisodioDownload){
						PanelEpisodioDownload panel_e=(PanelEpisodioDownload) panel_scroll_download.getComponent(i);
						if(panel_e.isDownloadSelected()){
							Episodio ep=panel_e.getEpisodio();
							ep.ignoraEpisodio();
							panel_scroll_download.remove(panel_e);
						}
						else
							i++;
					}
					else
						i++;
				}
			}
		});
		cmb_down_selezione.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				switch (cmb_down_selezione.getSelectedIndex()) {
					case 0: // seleziona tutto
						for (int i = 0; i < panel_scroll_download.getComponentCount(); i++) {
							if (panel_scroll_download.getComponent(i) instanceof PanelEpisodioDownload) {
								((PanelEpisodioDownload) panel_scroll_download.getComponent(i)).setSelected(true);
							}
						}
						break;
					case 1: // deseleziona tutto
						for (int i = 0; i < panel_scroll_download.getComponentCount(); i++) {
							if (panel_scroll_download.getComponent(i) instanceof PanelEpisodioDownload) {
								((PanelEpisodioDownload) panel_scroll_download.getComponent(i)).setSelected(false);
							}
						}
						break;
				}
			}
		});
		btnOpzioniSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.setAlwaysOnTop(chckbxSempreInPrimo.isSelected());
				Interfaccia.this.setAlwaysOnTop(Settings.isAlwaysOnTop());
				Settings.setAskOnClose(chckbxChiediConfermaPrima.isSelected());
				Settings.setExtenalVLC(chckbxExternalVLC.isSelected());
				Settings.setAutostart(chckbxAvviaConIl.isSelected());
				Settings.setStartHidden(chckbxAvviaRidottoA.isSelected());
				Settings.setTrayOnIcon(chckbxTrayOnIcon.isSelected());
				Settings.setDownloadAutomatico(chckbxAutoAbilita.isSelected());
				Settings.setMinRicerca((int) comboBoxMinutiRicercaAutomatica.getSelectedItem());
				Settings.setRicercaSottotitoli(chckbxAbilitaDownloadSottotitoli.isSelected());
				Settings.setEnableITASA(chckbxAbilitaItaliansubsnet.isSelected());
				if (txt_itasa_pass.getPassword().length > 0) {
					Settings.setItasaUsername(txt_itasa_user.getText());
					Settings.setItasaPassword(String.copyValueOf(txt_itasa_pass.getPassword()));
				}
				Settings.setClientPath(txt_utorrent_path.getText());
				Settings.setDirectoryDownload(txt_download_path.getText());
				Settings.setVLCPath(txt_vlc_path.getText());
				Settings.AggiornaDB();
			}
		});
		tab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tab.getSelectedComponent() == OpzioniPanel)
					inizializzaOpzioni();
				else if (tab.getSelectedComponent() == PreferenzeSeriePanel)
					btnAggiornaElencoRegole.getActionListeners()[0].actionPerformed(new ActionEvent(btnAggiornaElencoRegole, 0, ""));
				else if (tab.getSelectedComponent() == LettorePanel)
					disegnaLettore();
				else if(tab.getSelectedComponent()==SottotitoliPanel){
					inizializzaSubDownload();
					cmb_serie_sottotitoli.getActionListeners()[0].actionPerformed(new ActionEvent(cmb_serie_sottotitoli, 0, ""));
				}
					
				
			}
		});
		btnUtorrentSfoglia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String destinazione_path = Settings.getClientPath();
				JFileChooser chooser = new JFileChooser(destinazione_path.isEmpty() ? null : destinazione_path);
				chooser.setDialogTitle("Percorso uTorrent");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileHidingEnabled(false);
				chooser.setFileFilter(new ClientFilter("uTorrent", "uTorrent.exe")); // TODO fare multi piattaforma

				if (chooser.showOpenDialog(Interfaccia.this) == JFileChooser.APPROVE_OPTION) {
					destinazione_path = chooser.getSelectedFile().getAbsolutePath();
				}
				else {
					return;
				}
				txt_utorrent_path.setText(destinazione_path);
			}
		});
		btnVLCSfoglia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String destinazione_path = Settings.getVLCPath();
				JFileChooser chooser = new JFileChooser(destinazione_path.isEmpty() ? null : destinazione_path);
				chooser.setDialogTitle("Percorso VLC");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileHidingEnabled(false);
				chooser.setFileFilter(new ClientFilter("VLC", "vlc.exe")); // TODO fare multi piattaforma

				if (chooser.showOpenDialog(Interfaccia.this) == JFileChooser.APPROVE_OPTION) {
					destinazione_path = chooser.getSelectedFile().getAbsolutePath();
				}
				else {
					return;
				}
				txt_vlc_path.setText(destinazione_path);
			}
		});
		btnDirDownloadSfoglia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String destinazione_path = Settings.getDirectoryDownload();
				JFileChooser chooser = new JFileChooser(destinazione_path.isEmpty() ? null : destinazione_path);
				chooser.setDialogTitle("Cartella download");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileHidingEnabled(false);

				if (chooser.showOpenDialog(Interfaccia.this) == JFileChooser.APPROVE_OPTION) {
					destinazione_path = chooser.getSelectedFile().getAbsolutePath();
				}
				else {
					return;
				}
				txt_download_path.setText(destinazione_path);
			}
		});
		btnLettoreUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboBoxLettoreStagione.getSelectedItem()==null){
					cmb_serie_lettore.getActionListeners()[0].actionPerformed(new ActionEvent(cmb_serie_lettore, 0, ""));
					disegnaLettore();
				}
				else
					disegnaLettore();
			}
		});
		btnAggiornaElencoRegole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel_regole_scroll.removeAll();
				ArrayList<SerieTV> st = GestioneSerieTV.getElencoSerieInserite();
				if (st != null) {
					for (int i = 0; i < st.size(); i++) {
						panel_regole_scroll.add(new PanelPreferenze(st.get(i)));
					}
				}
			}
		});
		panel_regole_scroll.addContainerListener(new ContainerListener() {
			public void componentRemoved(ContainerEvent arg0) {
				GridLayout l = (GridLayout) panel_regole_scroll.getLayout();
				if (panel_regole_scroll.getComponentCount() <= 3)
					l.setRows(3);
				else
					l.setRows(l.getRows() - 1);
				panel_regole_scroll.revalidate();
				panel_regole_scroll.repaint();

			}

			public void componentAdded(ContainerEvent arg0) {
				GridLayout l = (GridLayout) panel_regole_scroll.getLayout();
				if (panel_regole_scroll.getComponentCount() <= 3)
					l.setRows(3);
				else
					l.setRows(l.getRows() + 1);
				panel_regole_scroll.revalidate();
				panel_regole_scroll.repaint();
			}
		});
		btnOffline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inizializzaDownloadScroll();
			}
		});
		panel_SottotitoliDaScaricare.addContainerListener(new ContainerListener() {
			public void componentRemoved(ContainerEvent arg0) {
				GridLayout l=(GridLayout) panel_SottotitoliDaScaricare.getLayout();
				if(panel_SottotitoliDaScaricare.getComponentCount()<=4)
					l.setRows(4);
				else
					l.setRows(l.getRows()-1);
				panel_SottotitoliDaScaricare.revalidate();
				panel_SottotitoliDaScaricare.repaint();
			}
			public void componentAdded(ContainerEvent arg0) {
				GridLayout l=(GridLayout) panel_SottotitoliDaScaricare.getLayout();
				if(panel_SottotitoliDaScaricare.getComponentCount()<=4)
					l.setRows(4);
				else
					l.setRows(l.getRows()+1);
				panel_SottotitoliDaScaricare.revalidate();
				panel_SottotitoliDaScaricare.repaint();
			}
		});
		btnItasaUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				class t_update_itasa extends Thread {
					public void run() {
						btnItasaUpdate.setEnabled(false);
						cmb_itasa_serie.setEnabled(false);
						ProviderSottotitoli p_itasa=GestioneSerieTV.getSubManager().getProvider(GestoreSottotitoli.ITASA);
						p_itasa.aggiornaElencoSerieOnline();
						ArrayList<SerieSub> s_ita=p_itasa.getElencoSerie();
						if(s_ita.size()>0){
							cmb_itasa_serie.removeAllItems();
							for(int i=0;i<s_ita.size();i++)
								cmb_itasa_serie.addItem(s_ita.get(i));
						}
						btnItasaUpdate.setEnabled(true);
						cmb_itasa_serie.setEnabled(true);
					}
				}
				Thread t=new t_update_itasa();
				t.start();
			}
		});
		btnSubsfactoryUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				class t_update_subsfactory extends Thread {
					public void run(){
						cmb_subsfactory_serie.setEnabled(false);
						btnSubsfactoryUpdate.setEnabled(false);
						ProviderSottotitoli p_subs=GestioneSerieTV.getSubManager().getProvider(GestoreSottotitoli.SUBSFACTORY);
						p_subs.aggiornaElencoSerieOnline();
						ArrayList<SerieSub> s_subs=p_subs.getElencoSerie();
						if(s_subs.size()>0){
							cmb_subsfactory_serie.removeAllItems();
							for(int i=0;i<s_subs.size();i++)
								cmb_subsfactory_serie.addItem(s_subs.get(i));
						}
						btnSubsfactoryUpdate.setEnabled(true);
						cmb_subsfactory_serie.setEnabled(true);
					}
				}
				Thread t=new t_update_subsfactory();
				t.start();
			}
		});
		//TODO download sottotitoli "personalizzati"
		//TODO opzione per caricare VLC automaticamente
		//TODO frame loading mostra progresso download dipendenze. chiudibile dopo aver scritto all'interno del db
		cmb_serie_sottotitoli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmb_serie_sottotitoli.getSelectedItem()!=null){
					SerieTV st=(SerieTV) cmb_serie_sottotitoli.getSelectedItem();
					ProviderSottotitoli itasa=GestioneSerieTV.getSubManager().getProvider(GestoreSottotitoli.ITASA);
					ArrayList<SerieSub> serie_itasa=itasa.getElencoSerie();
					lblItasaSerieAss.setText("Non associata");
					for(int i=0;i<serie_itasa.size();i++){
						SerieSub ss=serie_itasa.get(i);
						if(((int)ss.getID())==st.getIDItasa()){
							lblItasaSerieAss.setText("<html>"+ss.getNomeSerie()+"</html>");
							cmb_itasa_serie.setSelectedItem(ss);
							break;
						}
					}
					
					ProviderSottotitoli subsf=GestioneSerieTV.getSubManager().getProvider(GestoreSottotitoli.SUBSFACTORY);
					ArrayList<SerieSub> serie_subsf=subsf.getElencoSerie();
					lblSubsfactorySerieAss.setText("Non associata");
					for(int i=0;i<serie_subsf.size();i++){
						SerieSub ss=serie_subsf.get(i);
						if(((String)ss.getID()).compareToIgnoreCase(st.getSubsfactoryDirectory())==0){
							lblSubsfactorySerieAss.setText("<html>"+ss.getNomeSerie()+"</html>");
							cmb_subsfactory_serie.setSelectedItem(ss);
							break;
						}
					}
				}
			}
		});
		btnItasaAssocia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmb_serie_sottotitoli.getSelectedItem()!=null){
					SerieTV st=(SerieTV) cmb_serie_sottotitoli.getSelectedItem();
					if(cmb_itasa_serie.getSelectedItem()!=null){
						SerieSub ss=(SerieSub) cmb_itasa_serie.getSelectedItem();
						if(JOptionPane.showConfirmDialog(Interfaccia.this, "Vuoi associare "+st.getNomeSerie()+" con:\n"+ss.getNomeSerie()+"\n?", st.getNomeSerie()+" - Italiansubs", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_NO_OPTION){
							st.setIDItasa((int)ss.getID());
							st.aggiornaDB();
							lblItasaSerieAss.setText("<html>"+ss.getNomeSerie()+"</html>");
						}
					}
				}
			}
		});
		btnItasaRimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmb_serie_sottotitoli.getSelectedItem()!=null){
					SerieTV st=(SerieTV) cmb_serie_sottotitoli.getSelectedItem();
					if(JOptionPane.showConfirmDialog(Interfaccia.this, "Vuoi che venga rimossa l'associazione di "+st.getNomeSerie()+" con Italiansubs?", "Rimozione associazione Italiansubs", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
						st.setIDItasa(-1);
						st.aggiornaDB();
						lblItasaSerieAss.setText("<html>Non associata</html>");
					}
				}
				
			}
		});
		btnSubsfactoryAssocia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cmb_serie_sottotitoli.getSelectedItem()!=null){
					SerieTV st=(SerieTV) cmb_serie_sottotitoli.getSelectedItem();
					if(cmb_subsfactory_serie.getSelectedItem()!=null){
						SerieSubSubsfactory ss=(SerieSubSubsfactory) cmb_subsfactory_serie.getSelectedItem();
						if(JOptionPane.showConfirmDialog(Interfaccia.this, "Vuoi associare "+st.getNomeSerie()+" con:\n"+ss.getNomeSerie()+"\n?", st.getNomeSerie()+" - Subsfactory", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_NO_OPTION){
							st.setIDSubsfactory((int)ss.getIDDB(), false);
							st.aggiornaDB();
							lblItasaSerieAss.setText("<html>"+ss.getNomeSerie()+"</html>");
						}
					}
				}
				
			}
		});
		btnSubsfactoryRimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void addEntrySottotitolo(String nomeserie, int stagione, int episodio, String provider) {
		DefaultTableModel model = (DefaultTableModel) tableSubDownloaded.getModel();
		model.addRow(new Object[] { nomeserie, stagione, episodio, provider });
	}
	public void subAddSubDownload(Torrent t){
		panel_SottotitoliDaScaricare.add(new PanelSubDown(t));
	}
	public void inizializzaSubDownload(){
		ArrayList<Torrent> subDown=GestioneSerieTV.getSubManager().getSottotitoliDaScaricare();
		panel_SottotitoliDaScaricare.removeAll();
		for(int i=0;i<subDown.size();i++){
			panel_SottotitoliDaScaricare.add(new PanelSubDown(subDown.get(i)));
		}
		Runtime.getRuntime().gc();
	}
	private void initSubDownload(){
		ProviderSottotitoli p_itasa=GestioneSerieTV.getSubManager().getProvider(GestoreSottotitoli.ITASA);
		ArrayList<SerieSub> s_ita=p_itasa.getElencoSerie();
		for(int i=0;i<s_ita.size();i++)
			cmb_itasa_serie.addItem(s_ita.get(i));
		ProviderSottotitoli p_subsfactory=GestioneSerieTV.getSubManager().getProvider(GestoreSottotitoli.SUBSFACTORY);
		ArrayList<SerieSub> s_subs=p_subsfactory.getElencoSerie();
		for(int i=0;i<s_subs.size();i++)
			cmb_subsfactory_serie.addItem(s_subs.get(i));
	}
}
