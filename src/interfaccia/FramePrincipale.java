package interfaccia;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JButton;

import Programma.Settings;
import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;
import SerieTV.Torrent;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.io.IOException;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import java.awt.Component;

public class FramePrincipale extends JFrame {
	private static final long  serialVersionUID = 1L;
	private JPanel			 contentPane;
	private JLabel			 lblDisponibili;
	private JComboBox<SerieTV> eztv_elenco_serie;
	private JButton			btnAggiungi;
	private JTextField		 txt_eztv;

	private String			 DEFAULT_SEARCH   = "Cerca...";
	private JTextField		 txt_proprie;
	private JComboBox<SerieTV> proprie_elenco;
	private JTabbedPane		tabbedPane;
	private JPanel			 panel_principale;
	private JComboBox<String>  combo_tipo_selezione;
	private JButton			btn_aggiorna;
	private JButton			button_extend_frame;
	private JTable			 table_subdownload;
	private JScrollPane		scrollPane_sottotitoli_da_scaricare;
	private JPanel			 panel_sub_da_scaricare;
	private JPanel			 panel_associatore;
	private JPanel			 panel_extended;
	private JLabel			 lbllogo;
	private JPasswordField	 passwordField;
	private JCheckBox		  chckbxAvviaConIl;
	private JCheckBox		  chckbxstart_icon;
	private JCheckBox		  chckbxSempreInPrimo;
	private JCheckBox		  chckbxChiediConfermaPrima;
	private JCheckBox		  chckbxMostrap;
	private JCheckBox		  chckbxMostraPreair;
	private JCheckBox		  chckbxAbilitaDownloadAutomatico;
	private JCheckBox		  chckbxScaricap;
	private JCheckBox		  chckbxScaricaPreair;
	private JSpinner		   spinner_1;
	private JCheckBox		  chckbxAbilitaRicercaDei;
	private JCheckBox		  chckbxAbilitaItaliansubsnet;
	private JTextField		 textField;
	private JButton			btnConnetti;
	private JLabel			 lblStato;
	private JLabel			 lblDisconnesso;
	private JPanel			 panel_software;
	private JLabel			 lblCartellaDownload;
	private JTextField		 textField_1;
	private JButton			btnSeleziona;
	private JLabel			 lblUtorrent;
	private JTextField		 textField_2;
	private JButton			btnSeleziona_1;
	private JLabel			 lblVlcPlayer;
	private JCheckBox		  chckbxUtilizzaPlayerIntegrato;
	private JTextField		 textField_3;
	private JButton			btnSeleziona_2;
	private JLabel			 lblWine;
	private JTextField		 textField_4;
	private JButton			btnSeleziona_3;
	private JButton			btnProvaDownload;
	private JButton			btnScarica;
	private JButton			btnIgnora;
	private JPanel			 panel_scroll;
	private JScrollPane		scrollPane_episodi_down;
	private JPanelManagerLista panel_lista_download;
	private JButton			btnProvaLettore;
	private JPanelManagerLista panel_lista_episodi;
	private JComboBox<SerieTV> comboBox_lettore_serie;
	private JButton			btnRimuovi_lettore;
	private JLabel			 lblSerie_1;
	private JComboBox<SerieTV>		  comboBox_serie_sottotitoli;
	private JLabel			 lblItasaLogo;
	private JTextField		 textField_itasa;
	private JLabel			 lblSubsfactoryLogo;
	private JLabel			 lblStatoAssociazione;
	private JLabel			 lblSerieAssociataItasa;
	private JLabel			 lblStatoAssociazione_1;
	private JLabel			 lblSerieAssociataSubsfactory;
	private JTextField		 textField_5;
	private JComboBox		  comboBox_sub_elenco_subsfactory;
	private JButton			btnAssociaSubsfactory;
	private JButton			btnRimuoviSubsfactory;
	private JCheckBox chckbxNascondiViste;
	private JCheckBox chckbxNascondiIgnorate;
	private JCheckBox chckbxNascondiRimosse;
	private JButton btnaggiornasubsfactory;
	private JButton btnaggiornaitasa;
	private JComboBox<Integer> comboBox_lettore_stagione;
	private JComboBox<String> comboBox_lettore_ordine;

	/**
	 * Create the frame.
	 */
	public FramePrincipale() {
		setResizable(false);
		setTitle("Gestione Serie TV");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBounds(0, 0, 594, 480);
		contentPane.add(tabbedPane);

		panel_principale = new JPanel();
		tabbedPane.addTab("Principale", null, panel_principale, null);
		panel_principale.setLayout(null);

		JPanel panel_inserimento = new JPanel();
		panel_inserimento.setBounds(0, 0, 589, 103);
		panel_principale.add(panel_inserimento);
		panel_inserimento.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_inserimento.setLayout(null);

		lblDisponibili = new JLabel();
		lblDisponibili.setBounds(92, 11, 82, 20);
		lblDisponibili.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_inserimento.add(lblDisponibili);

		eztv_elenco_serie = new JComboBox<SerieTV>();
		eztv_elenco_serie.setBounds(10, 57, 260, 20);
		panel_inserimento.add(eztv_elenco_serie);

		txt_eztv = new JTextField();
		txt_eztv.setBounds(10, 36, 164, 20);
		panel_inserimento.add(txt_eztv);
		txt_eztv.setColumns(10);

		btnAggiungi = new JButton();
		btnAggiungi.setBounds(181, 35, 89, 23);
		panel_inserimento.add(btnAggiungi);

		JSeparator separator = new JSeparator();
		separator.setBounds(292, 10, 2, 68);
		separator.setOrientation(SwingConstants.VERTICAL);
		panel_inserimento.add(separator);

		JLabel lblProprie = new JLabel("Proprie");
		lblProprie.setBounds(415, 11, 65, 20);
		lblProprie.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_inserimento.add(lblProprie);

		txt_proprie = new JTextField();
		txt_proprie.setBounds(310, 36, 164, 20);
		panel_inserimento.add(txt_proprie);
		txt_proprie.setColumns(10);

		JButton btnRimuovi = new JButton("Rimuovi");
		btnRimuovi.setBounds(484, 35, 89, 23);
		panel_inserimento.add(btnRimuovi);

		proprie_elenco = new JComboBox<SerieTV>();
		proprie_elenco.setBounds(310, 57, 263, 20);
		panel_inserimento.add(proprie_elenco);

		panel_scroll = new JPanel();
		panel_scroll.setBounds(4, 139, 575, 276);
		panel_principale.add(panel_scroll);
		panel_scroll.setLayout(new BorderLayout(0, 0));

		scrollPane_episodi_down = new JScrollPane();
		panel_scroll.add(scrollPane_episodi_down, BorderLayout.CENTER);

		panel_lista_download = new JPanelManagerLista();
		scrollPane_episodi_down.setViewportView(panel_lista_download);

		JLabel lblTipoSelezione = new JLabel("Tipo selezione:");
		lblTipoSelezione.setBounds(10, 114, 89, 14);
		panel_principale.add(lblTipoSelezione);

		combo_tipo_selezione = new JComboBox<String>();

		combo_tipo_selezione.setBounds(99, 113, 131, 20);
		panel_principale.add(combo_tipo_selezione);

		btn_aggiorna = new JButton("aggiorna");
		btn_aggiorna.setBounds(490, 110, 89, 23);
		panel_principale.add(btn_aggiorna);

		btnScarica = new JButton("Scarica");
		btnScarica.setBounds(161, 418, 89, 23);
		panel_principale.add(btnScarica);

		btnIgnora = new JButton("Ignora");
		btnIgnora.setBounds(319, 418, 89, 23);
		panel_principale.add(btnIgnora);

		JPanel panel_sottotitoli = new JPanel();
		tabbedPane.addTab("Sottotitoli", null, panel_sottotitoli, null);
		panel_sottotitoli.setLayout(null);

		JScrollPane scrollPane_subdownload = new JScrollPane();
		scrollPane_subdownload.setBounds(0, 325, 590, 127);
		panel_sottotitoli.add(scrollPane_subdownload);

		String[] columnNames = { "Serie", "Stagione", "Episodio", "tramite", "play" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		table_subdownload = new JTable(model);
		table_subdownload.setEnabled(true);
		table_subdownload.getColumn("play").setCellRenderer(new ButtonRenderer());
		table_subdownload.getColumn("play").setCellEditor(new ButtonPlaySub(new JCheckBox()));
		table_subdownload.getColumn("tramite").setCellEditor(new TextSubRender(new JTextField()));
		table_subdownload.getColumn("Stagione").setCellEditor(new TextSubRender(new JTextField()));
		table_subdownload.getColumn("Serie").setCellEditor(new TextSubRender(new JTextField()));
		table_subdownload.getColumn("Episodio").setCellEditor(new TextSubRender(new JTextField()));

		scrollPane_subdownload.setViewportView(table_subdownload);

		scrollPane_sottotitoli_da_scaricare = new JScrollPane();
		scrollPane_sottotitoli_da_scaricare.setBounds(0, 164, 589, 161);
		panel_sottotitoli.add(scrollPane_sottotitoli_da_scaricare);

		panel_sub_da_scaricare = new JPanel();
		scrollPane_sottotitoli_da_scaricare.setViewportView(panel_sub_da_scaricare);
		panel_sub_da_scaricare.setLayout(null);

		panel_associatore = new JPanel();
		panel_associatore.setBounds(0, 0, 589, 161);
		panel_sottotitoli.add(panel_associatore);
		panel_associatore.setLayout(null);

		lblSerie_1 = new JLabel("Serie: ");
		lblSerie_1.setBounds(10, 11, 37, 14);
		panel_associatore.add(lblSerie_1);

		comboBox_serie_sottotitoli = new JComboBox<SerieTV>();
		comboBox_serie_sottotitoli.setBounds(57, 8, 215, 20);
		panel_associatore.add(comboBox_serie_sottotitoli);

		lblItasaLogo = new JLabel("itasa logo");
		lblItasaLogo.setBounds(10, 36, 200, 35);
		panel_associatore.add(lblItasaLogo);

		textField_itasa = new JTextField();
		textField_itasa.setBounds(10, 75, 200, 20);
		panel_associatore.add(textField_itasa);
		textField_itasa.setColumns(10);

		JComboBox comboBox_sub_elenco_itasa = new JComboBox();
		comboBox_sub_elenco_itasa.setBounds(10, 96, 226, 20);
		panel_associatore.add(comboBox_sub_elenco_itasa);

		JButton btnAssociaItasa = new JButton("associa");
		btnAssociaItasa.setBounds(10, 116, 105, 23);
		panel_associatore.add(btnAssociaItasa);

		JButton btnRimuoviItasa = new JButton("rimuovi");
		btnRimuoviItasa.setBounds(131, 116, 105, 23);
		panel_associatore.add(btnRimuoviItasa);

		lblSubsfactoryLogo = new JLabel("subsfactory logo");
		lblSubsfactoryLogo.setBounds(361, 36, 200, 35);
		panel_associatore.add(lblSubsfactoryLogo);

		lblStatoAssociazione = new JLabel("Stato:");
		lblStatoAssociazione.setBounds(10, 145, 46, 14);
		panel_associatore.add(lblStatoAssociazione);

		lblSerieAssociataItasa = new JLabel("serie associata");
		lblSerieAssociataItasa.setBounds(57, 145, 179, 14);
		panel_associatore.add(lblSerieAssociataItasa);

		lblStatoAssociazione_1 = new JLabel("Stato: ");
		lblStatoAssociazione_1.setBounds(361, 145, 46, 14);
		panel_associatore.add(lblStatoAssociazione_1);

		lblSerieAssociataSubsfactory = new JLabel("serie associata");
		lblSerieAssociataSubsfactory.setBounds(405, 145, 184, 14);
		panel_associatore.add(lblSerieAssociataSubsfactory);

		textField_5 = new JTextField();
		textField_5.setBounds(361, 75, 200, 20);
		panel_associatore.add(textField_5);
		textField_5.setColumns(10);

		comboBox_sub_elenco_subsfactory = new JComboBox();
		comboBox_sub_elenco_subsfactory.setBounds(361, 96, 228, 20);
		panel_associatore.add(comboBox_sub_elenco_subsfactory);

		btnAssociaSubsfactory = new JButton("associa");
		btnAssociaSubsfactory.setBounds(361, 116, 105, 23);
		panel_associatore.add(btnAssociaSubsfactory);

		btnRimuoviSubsfactory = new JButton("rimuovi");
		btnRimuoviSubsfactory.setBounds(484, 116, 105, 23);
		panel_associatore.add(btnRimuoviSubsfactory);
		
		btnaggiornaitasa = new JButton();
		btnaggiornaitasa.setBounds(206, 74, 30, 23);
		panel_associatore.add(btnaggiornaitasa);
		
		btnaggiornasubsfactory = new JButton();
		btnaggiornasubsfactory.setBounds(559, 74, 30, 23);
		panel_associatore.add(btnaggiornasubsfactory);

		JPanel panel_lettore = new JPanel();
		tabbedPane.addTab("Lettore", null, panel_lettore, null);
		panel_lettore.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 0, 589, 89);
		panel_lettore.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblSerie = new JLabel("Serie:");
		lblSerie.setBounds(143, 11, 47, 14);
		panel_2.add(lblSerie);

		comboBox_lettore_serie = new JComboBox<SerieTV>();

		comboBox_lettore_serie.setBounds(187, 11, 192, 20);
		panel_2.add(comboBox_lettore_serie);

		JCheckBox chckbxConclusa = new JCheckBox("conclusa");
		chckbxConclusa.setEnabled(false);
		chckbxConclusa.setBounds(504, 40, 79, 23);
		panel_2.add(chckbxConclusa);

		chckbxNascondiViste = new JCheckBox("Nascondi viste");
		chckbxNascondiViste.setBounds(6, 40, 111, 23);
		panel_2.add(chckbxNascondiViste);

		chckbxNascondiIgnorate = new JCheckBox("Nascondi ignorate");
		chckbxNascondiIgnorate.setSelected(true);
		chckbxNascondiIgnorate.setBounds(119, 40, 133, 23);
		panel_2.add(chckbxNascondiIgnorate);

		chckbxNascondiRimosse = new JCheckBox("Nascondi rimosse");
		chckbxNascondiRimosse.setSelected(true);
		chckbxNascondiRimosse.setBounds(254, 38, 133, 23);
		panel_2.add(chckbxNascondiRimosse);

		btnRimuovi_lettore = new JButton("Rimuovi");
		btnRimuovi_lettore.setBounds(494, 10, 89, 23);
		panel_2.add(btnRimuovi_lettore);
		
		JLabel lblStagione = new JLabel("Stagione: ");
		lblStagione.setBounds(10, 70, 61, 14);
		panel_2.add(lblStagione);
		
		comboBox_lettore_stagione = new JComboBox();
		comboBox_lettore_stagione.setBounds(65, 67, 40, 20);
		panel_2.add(comboBox_lettore_stagione);
		
		JLabel lblOrdine = new JLabel("Ordine:");
		lblOrdine.setBounds(119, 70, 46, 14);
		panel_2.add(lblOrdine);
		
		comboBox_lettore_ordine = new JComboBox();
		comboBox_lettore_ordine.setBounds(163, 67, 89, 20);
		panel_2.add(comboBox_lettore_ordine);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 90, 589, 351);
		panel_lettore.add(scrollPane);

		panel_lista_episodi = new JPanelManagerLista();
		scrollPane.setViewportView(panel_lista_episodi);

		JPanel panel_opzioni = new JPanel();
		tabbedPane.addTab("Opzioni", null, panel_opzioni, null);
		tabbedPane.setEnabledAt(3, true);
		panel_opzioni.setLayout(null);

		JPanel panel_opzioni_aspetto = new JPanel();
		panel_opzioni_aspetto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Aspetto", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_opzioni_aspetto.setBounds(10, 11, 273, 155);
		panel_opzioni.add(panel_opzioni_aspetto);
		panel_opzioni_aspetto.setLayout(null);

		chckbxAvviaConIl = new JCheckBox("Avvia con il sistema operativo");
		chckbxAvviaConIl.setBounds(6, 19, 261, 23);
		panel_opzioni_aspetto.add(chckbxAvviaConIl);

		chckbxstart_icon = new JCheckBox("Avvia ridotto nella tray");
		chckbxstart_icon.setBounds(6, 45, 261, 23);
		panel_opzioni_aspetto.add(chckbxstart_icon);

		chckbxSempreInPrimo = new JCheckBox("Sempre in primo piano");
		chckbxSempreInPrimo.setBounds(6, 71, 261, 23);
		panel_opzioni_aspetto.add(chckbxSempreInPrimo);

		chckbxChiediConfermaPrima = new JCheckBox("Chiedi conferma prima di uscire");
		chckbxChiediConfermaPrima.setBounds(6, 97, 261, 23);
		panel_opzioni_aspetto.add(chckbxChiediConfermaPrima);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Download", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(292, 19, 287, 147);
		panel_opzioni.add(panel);
		panel.setLayout(null);

		chckbxMostrap = new JCheckBox("Mostra 720p");
		chckbxMostrap.setBounds(6, 7, 111, 23);
		panel.add(chckbxMostrap);

		chckbxMostraPreair = new JCheckBox("Mostra pre-air");
		chckbxMostraPreair.setBounds(6, 33, 275, 23);
		panel.add(chckbxMostraPreair);

		chckbxAbilitaDownloadAutomatico = new JCheckBox("Abilita download automatico");
		chckbxAbilitaDownloadAutomatico.setBounds(6, 59, 275, 23);
		panel.add(chckbxAbilitaDownloadAutomatico);

		chckbxScaricap = new JCheckBox("Scarica 720p");
		chckbxScaricap.setBounds(37, 85, 107, 23);
		panel.add(chckbxScaricap);

		chckbxScaricaPreair = new JCheckBox("Scarica pre-air");
		chckbxScaricaPreair.setBounds(146, 85, 135, 23);
		panel.add(chckbxScaricaPreair);

		spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(480, 30, 1440, 5));
		spinner_1.setBounds(10, 116, 57, 20);
		panel.add(spinner_1);

		JLabel lblMinutiTraOgni = new JLabel("minuti tra ogni ricerca");
		lblMinutiTraOgni.setBounds(72, 119, 209, 14);
		panel.add(lblMinutiTraOgni);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Sottotitoli italiani", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 177, 273, 217);
		panel_opzioni.add(panel_1);
		panel_1.setLayout(null);

		chckbxAbilitaRicercaDei = new JCheckBox("Abilita ricerca dei sottotitoli");

		chckbxAbilitaRicercaDei.setBounds(6, 21, 257, 23);
		panel_1.add(chckbxAbilitaRicercaDei);

		chckbxAbilitaItaliansubsnet = new JCheckBox("Abilita Italiansubs.net");

		chckbxAbilitaItaliansubsnet.setBounds(6, 47, 257, 23);
		panel_1.add(chckbxAbilitaItaliansubsnet);

		JLabel lblUsernameItaliansubs = new JLabel("Username Italiansubs");
		lblUsernameItaliansubs.setBounds(33, 77, 119, 14);
		panel_1.add(lblUsernameItaliansubs);

		textField = new JTextField();
		textField.setBounds(148, 74, 115, 20);
		panel_1.add(textField);
		textField.setColumns(10);

		JLabel lblPasswordItaliansubs = new JLabel("Password Italiansubs");
		lblPasswordItaliansubs.setBounds(33, 102, 107, 14);
		panel_1.add(lblPasswordItaliansubs);

		passwordField = new JPasswordField();
		passwordField.setBounds(148, 99, 115, 20);
		panel_1.add(passwordField);

		btnConnetti = new JButton("Connetti");
		btnConnetti.setBounds(33, 127, 89, 23);
		panel_1.add(btnConnetti);

		lblStato = new JLabel("Stato: ");
		lblStato.setBounds(132, 131, 46, 14);
		panel_1.add(lblStato);

		lblDisconnesso = new JLabel("Disconnesso");
		lblDisconnesso.setBounds(168, 131, 89, 14);
		panel_1.add(lblDisconnesso);

		panel_software = new JPanel();
		panel_software.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Cartelle e programmi", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_software.setBounds(292, 177, 287, 217);
		panel_opzioni.add(panel_software);
		panel_software.setLayout(null);

		lblCartellaDownload = new JLabel("Cartella download");
		lblCartellaDownload.setBounds(10, 24, 100, 14);
		panel_software.add(lblCartellaDownload);

		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(10, 43, 169, 20);
		panel_software.add(textField_1);
		textField_1.setColumns(10);

		btnSeleziona = new JButton("Seleziona");
		btnSeleziona.setBounds(188, 42, 89, 23);
		panel_software.add(btnSeleziona);

		lblUtorrent = new JLabel("uTorrent");
		lblUtorrent.setBounds(10, 74, 81, 14);
		panel_software.add(lblUtorrent);

		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(10, 91, 169, 20);
		panel_software.add(textField_2);
		textField_2.setColumns(10);

		btnSeleziona_1 = new JButton("Seleziona");
		btnSeleziona_1.setBounds(188, 90, 89, 23);
		panel_software.add(btnSeleziona_1);

		lblVlcPlayer = new JLabel("VLC Player");
		lblVlcPlayer.setBounds(10, 122, 81, 14);
		panel_software.add(lblVlcPlayer);

		chckbxUtilizzaPlayerIntegrato = new JCheckBox("Preferisci player integrato");
		chckbxUtilizzaPlayerIntegrato.setBounds(97, 118, 180, 23);
		panel_software.add(chckbxUtilizzaPlayerIntegrato);

		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(10, 143, 169, 20);
		panel_software.add(textField_3);
		textField_3.setColumns(10);

		btnSeleziona_2 = new JButton("Seleziona");
		btnSeleziona_2.setBounds(188, 142, 89, 23);
		panel_software.add(btnSeleziona_2);

		JButton btnSalva = new JButton("Salva");
		btnSalva.setBounds(194, 405, 89, 23);
		panel_opzioni.add(btnSalva);

		JButton btnDefault = new JButton("Default");
		btnDefault.setBounds(302, 405, 89, 23);
		panel_opzioni.add(btnDefault);

		// Wine
		if (Settings.isLinux()) {
			lblWine = new JLabel("Wine");
			lblWine.setBounds(10, 174, 46, 14);
			panel_software.add(lblWine);

			textField_4 = new JTextField();
			textField_4.setEditable(false);
			textField_4.setBounds(10, 191, 169, 20);
			panel_software.add(textField_4);
			textField_4.setColumns(10);

			btnSeleziona_3 = new JButton("Seleziona");
			btnSeleziona_3.setBounds(188, 190, 89, 23);
			panel_software.add(btnSeleziona_3);
		}

		JPanel panel_about = new JPanel();
		tabbedPane.addTab("About", null, panel_about, null);
		panel_about.setLayout(null);

		JButton btnControllaAggiornamenti = new JButton("Controlla aggiornamenti");
		btnControllaAggiornamenti.setBounds(362, 128, 170, 45);
		panel_about.add(btnControllaAggiornamenti);

		JLabel lblVersioneCorrente = new JLabel("Versione corrente: " + Settings.getVersioneSoftware());
		lblVersioneCorrente.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblVersioneCorrente.setBounds(200, 141, 152, 14);
		panel_about.add(lblVersioneCorrente);

		lbllogo = new JLabel("QUI VA IL LOGO");
		lbllogo.setBounds(10, 11, 160, 160);
		panel_about.add(lbllogo);

		JLabel lblUnPoDi = new JLabel("<html><b>Gestione Serie TV</b> nasce in un caldo giorno d'estate.<br>Di ritorno dalla spiaggia, con il resto della mia famiglia che dormiva, per non annoiarmi decisi di creare un software che mi tenesse al passo con l'uscita delle serie tv.<br>All'inizio non era altro che un programma in una console che scaricava tutti i torrent degli episodi non visti. Man mano \u00E8 diventato il programma attuale.</html>");
		lblUnPoDi.setBounds(200, 11, 379, 106);
		panel_about.add(lblUnPoDi);

		JPanel panel_donazione = new JPanel();
		panel_donazione.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_donazione.setBounds(115, 194, 357, 98);
		panel_about.add(panel_donazione);
		panel_donazione.setLayout(null);

		JLabel lblEffettuaUnDonazione = new JLabel("<html><font size=3><b>Se trovi il programma utile, potresti farmelo sapere offrendomi una birra<b></font></html>");
		lblEffettuaUnDonazione.setBounds(10, 11, 337, 33);
		panel_donazione.add(lblEffettuaUnDonazione);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 900, 1));
		spinner.setBounds(79, 55, 63, 20);
		panel_donazione.add(spinner);

		JLabel lblEur = new JLabel("EUR (\u20AC)");
		lblEur.setBounds(30, 58, 48, 14);
		panel_donazione.add(lblEur);

		JButton btnVaiAPaypal = new JButton("Vai a paypal");
		btnVaiAPaypal.setBounds(163, 54, 109, 23);
		panel_donazione.add(btnVaiAPaypal);

		/* TODO decommentare alla fine */
		// NativeInterface.open();
		JScrollPane scrollPane_advertising = new JScrollPane();
		// scrollPane_advertising.setBounds(0, 308, 589, 144);
		// JWebBrowser browser=new
		// JWebBrowser(JWebBrowser.destroyOnFinalization());
		// browser.setBarsVisible(false);
		// browser.setButtonBarVisible(false);
		// browser.setMenuBarVisible(false);
		// browser.setStatusBarVisible(false);
		// scrollPane_advertising.setViewportView(browser);
		panel_about.add(scrollPane_advertising);
		// browser.navigate("http://pinoelefante.altervista.org/ads.html");

		button_extend_frame = new JButton(">>");
		button_extend_frame.setBounds(545, 479, 49, 23);
		contentPane.add(button_extend_frame);

		panel_extended = new JPanel();
		panel_extended.setBounds(604, 22, 240, 480);
		contentPane.add(panel_extended);

		JButton btnProva_list_sub_download = new JButton("Prova sub");
		btnProva_list_sub_download.setBounds(0, 479, 89, 23);
		contentPane.add(btnProva_list_sub_download);

		btnProvaDownload = new JButton("Prova download");

		btnProvaDownload.setBounds(99, 479, 109, 23);
		contentPane.add(btnProvaDownload);

		btnProvaLettore = new JButton("Prova lettore");

		btnProvaLettore.setBounds(214, 479, 109, 23);
		contentPane.add(btnProvaLettore);

		btnProva_list_sub_download.addActionListener(new ActionListener() {
			private int i = 1;

			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel tb = (DefaultTableModel) table_subdownload.getModel();
				tb.addRow(new Object[] { "Prova_" + i, "Prova_" + i, "Prova_" + i, "Prova_" + i });
				i++;
			}
		});

		inizializza();
		
		addListener();
	}

	private void inizializza() {
		lblDisponibili.setText("Disponibili");
		btnAggiungi.setText("Aggiungi");
		txt_eztv.setText(DEFAULT_SEARCH);
		txt_proprie.setText(DEFAULT_SEARCH);
		chckbxNascondiIgnorate.setSelected(JPanelEpisodioPlayer.getNascondiIgnorate());
		chckbxNascondiRimosse.setSelected(JPanelEpisodioPlayer.getNascondiRimosse());
		chckbxNascondiViste.setSelected(JPanelEpisodioPlayer.getNascondiViste());
		
		for (int i = 0; i < GestioneSerieTV.getElencoSerieCompleto().size(); i++)
			eztv_elenco_serie.addItem(GestioneSerieTV.getElencoSerieCompleto().get(i));
		
		comboBox_lettore_serie.addItem(new SerieTV("Mostra tutte", null));
		for (int i = 0; i < GestioneSerieTV.getElencoSerieInserite().size(); i++){
			SerieTV st=GestioneSerieTV.getElencoSerieInserite().get(i);
			proprie_elenco.addItem(st);
			comboBox_lettore_serie.addItem(st);
			comboBox_serie_sottotitoli.addItem(st);
		}
		combo_tipo_selezione.addItem("Deseleziona tutto");
		combo_tipo_selezione.addItem("Seleziona tutto");
		combo_tipo_selezione.addItem("720p");
		combo_tipo_selezione.addItem("Inverti selezione");

		try {
			Resource.setImage(lbllogo, "res/logo.png", 160);
		}
		catch (IOException e) {
			lbllogo.setText("<html>" + e.getMessage() + "</html>");
		}
		
		btnRimuovi_lettore.setEnabled(false);
		try {
			Resource.setImage(lblItasaLogo, "res/itasa.png", 200);
			Resource.setImage(lblSubsfactoryLogo, "res/subsfactory.jpg", 200);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		btnaggiornaitasa.setIcon(Resource.getIcona("res/aggiorna.png"));
		btnaggiornasubsfactory.setIcon(Resource.getIcona("res/aggiorna.png"));
		
		comboBox_lettore_ordine.addItem("Crescente");
		comboBox_lettore_ordine.addItem("Decrescente");
	}

	private void addListener() {
		txt_eztv.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}

			public void mousePressed(MouseEvent arg0) {}

			public void mouseExited(MouseEvent arg0) {}

			public void mouseEntered(MouseEvent arg0) {}

			public void mouseClicked(MouseEvent arg0) {
				if (txt_eztv.getText().contains(DEFAULT_SEARCH))
					txt_eztv.setText("");
			}
		});
		txt_eztv.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}

			public void keyReleased(KeyEvent arg0) {
				String text = txt_eztv.getText().trim();
				ArrayList<SerieTV> st = GestioneSerieTV.getElencoSerieCompleto();
				eztv_elenco_serie.removeAllItems();
				for (int i = 0; i < st.size(); i++) {
					if (text.isEmpty()) {
						eztv_elenco_serie.addItem(st.get(i));
					}
					else {
						if (st.get(i).getNomeSerie().toLowerCase().contains(text.toLowerCase())) {
							eztv_elenco_serie.addItem(st.get(i));
						}
					}
				}
			}

			public void keyPressed(KeyEvent arg0) {}
		});

		txt_proprie.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}

			public void mousePressed(MouseEvent arg0) {}

			public void mouseExited(MouseEvent arg0) {}

			public void mouseEntered(MouseEvent arg0) {}

			public void mouseClicked(MouseEvent arg0) {
				if (txt_proprie.getText().contains(DEFAULT_SEARCH))
					txt_proprie.setText("");
			}
		});
		txt_proprie.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}

			public void keyReleased(KeyEvent arg0) {
				String text = txt_proprie.getText().trim();
				ArrayList<SerieTV> st = GestioneSerieTV.getElencoSerieInserite();
				proprie_elenco.removeAllItems();
				for (int i = 0; i < st.size(); i++) {
					if (text.isEmpty()) {
						proprie_elenco.addItem(st.get(i));
					}
					else {
						if (st.get(i).getNomeSerie().toLowerCase().contains(text.toLowerCase())) {
							proprie_elenco.addItem(st.get(i));
						}
					}
				}
			}

			public void keyPressed(KeyEvent arg0) {}
		});
		button_extend_frame.addActionListener(new ActionListener() {
			private String current_status = ">>";

			public void actionPerformed(ActionEvent e) {
				switch (current_status) {
					case ">>":
						FramePrincipale.this.setSize(850, 530);
						current_status = "<<";
						break;
					case "<<":
						FramePrincipale.this.setSize(600, 530);
						current_status = ">>";
						break;
				}
				button_extend_frame.setText(current_status);
			}
		});
		btn_aggiorna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
			}
		});
		btnAggiungi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SerieTV st=(SerieTV)eztv_elenco_serie.getSelectedItem();
				if(st==null)
					return;
				GestioneSerieTV.aggiungiSerie(st);
				proprie_elenco.removeAllItems();
				comboBox_lettore_serie.removeAllItems();
				comboBox_serie_sottotitoli.removeAllItems();
				for(int i=0;i<GestioneSerieTV.getElencoSerieInserite().size();i++){
					SerieTV s=GestioneSerieTV.getElencoSerieInserite().get(i);
					proprie_elenco.addItem(s);
					comboBox_lettore_serie.addItem(s);
					comboBox_serie_sottotitoli.addItem(s);
				}
				proprie_elenco.setEnabled(true);
				comboBox_lettore_serie.setEnabled(true);
				comboBox_serie_sottotitoli.setEnabled(true);
				
				
				//TODO associazione serie sottotitoli
			}
		});
		chckbxAbilitaRicercaDei.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					chckbxAbilitaItaliansubsnet.setEnabled(true);
				}
				else if (arg0.getStateChange() == ItemEvent.DESELECTED)
					chckbxAbilitaItaliansubsnet.setEnabled(false);
			}
		});
		chckbxAbilitaItaliansubsnet.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					textField.setEnabled(true);
					passwordField.setEnabled(true);
				}
				else if (arg0.getStateChange() == ItemEvent.DESELECTED) {
					textField.setEnabled(false);
					passwordField.setEnabled(false);
				}
			}
		});
		btnProvaDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Torrent t = new Torrent("magnet:?xt=urn:btih:Y4Q5NAGZBJ57V3H5RIITYO3ZMCYL27H3&dn=Grimm.S02E19.720p.HDTV.X264-DIMENSION&tr=udp://tracker.openbittorrent.com:80&tr=udp://tracker.publicbt.com:80&tr=udp://tracker.istole.it:80", Torrent.SCARICARE, "Grimm", 283);
				panel_lista_download.add(new JPanelEpisodioDownload(panel_lista_download, t));
				panel_lista_download.revalidate();
				panel_lista_download.repaint();
			}
		});
		btnProvaLettore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Torrent t = new Torrent("magnet:?xt=urn:btih:Y4Q5NAGZBJ57V3H5RIITYO3ZMCYL27H3&dn=Grimm.S02E19.720p.HDTV.X264-DIMENSION&tr=udp://tracker.openbittorrent.com:80&tr=udp://tracker.publicbt.com:80&tr=udp://tracker.istole.it:80", Torrent.SCARICARE, "Grimm", 283);
				panel_lista_episodi.add(new JPanelEpisodioPlayer(panel_lista_episodi, t));
				panel_lista_episodi.revalidate();
				panel_lista_episodi.repaint();
			}
		});
		combo_tipo_selezione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch (combo_tipo_selezione.getSelectedIndex()) {
				// DESELEZIONA TUTTO
					case 0:
						for (int i = 0; i < panel_lista_download.getComponentCount(); i++) {
							Component c = panel_lista_download.getComponent(i);
							if (c instanceof JPanelEpisodioDownload) {
								((JPanelEpisodioDownload) c).getCheckbox().setSelected(false);
							}
						}
						break;
					// SELEZIONA TUTTO
					case 1:
						for (int i = 0; i < panel_lista_download.getComponentCount(); i++) {
							Component c = panel_lista_download.getComponent(i);
							if (c instanceof JPanelEpisodioDownload) {
								((JPanelEpisodioDownload) c).getCheckbox().setSelected(true);
							}
						}
						break;
					// 720p
					case 2:
						for (int i = 0; i < panel_lista_download.getComponentCount(); i++) {
							Component c = panel_lista_download.getComponent(i);
							if (c instanceof JPanelEpisodioDownload) {
								Torrent t = ((JPanelEpisodioDownload) c).getTorrent();
								if (t.is720p())
									((JPanelEpisodioDownload) c).getCheckbox().setSelected(true);
								else
									((JPanelEpisodioDownload) c).getCheckbox().setSelected(false);
							}
						}
						break;
					// INVERTI SELEZIONE
					case 3:
						for (int i = 0; i < panel_lista_download.getComponentCount(); i++) {
							Component c = panel_lista_download.getComponent(i);
							if (c instanceof JPanelEpisodioDownload) {
								JCheckBox b = ((JPanelEpisodioDownload) c).getCheckbox();
								b.setSelected(!b.isSelected());
							}
						}
						break;
				}
			}
		});
		comboBox_lettore_serie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_lettore_serie.getSelectedIndex() == 0) {
					btnRimuovi_lettore.setEnabled(false);
					// TODO mostra tutte le serie
				}
				else {
					SerieTV st=(SerieTV)comboBox_lettore_serie.getSelectedItem();
					if(st==null)
						return;
					btnRimuovi_lettore.setEnabled(true);
					comboBox_lettore_stagione.removeAllItems();
					// TODO aggiungere le stagioni alla combobox
					// TODO mostra solo serie corrente
				}
			}
		});
		btnRimuovi_lettore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_lettore_serie.getSelectedIndex() == 0) {
					// TODO
					// rimuovere da lettore
					// rimuovere da principale
					// rimuovere da sottotitoli
				}
			}
		});
		chckbxNascondiViste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanelEpisodioPlayer.setNascondiViste(chckbxNascondiViste.isSelected());
			}
		});
		chckbxNascondiIgnorate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanelEpisodioPlayer.setNascondiIgnorate(chckbxNascondiIgnorate.isSelected());
			}
		});
		chckbxNascondiRimosse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPanelEpisodioPlayer.setNascondiRimosse(chckbxNascondiRimosse.isSelected());
			}
		});
	}
}
