package GUI;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserCommandEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserListener;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowOpeningEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowWillOpenEvent;

import Programma.Download;
import Programma.Main;
import Programma.OperazioniFile;
import Programma.Settings;
import Programma.ThreadControlloAggiornamento;
import SerieTV.ElencoIndicizzato;
import SerieTV.GestioneSerieTV;
import SerieTV.Indexable;
import SerieTV.SerieTV;
import SerieTV.Torrent;
import Sottotitoli.GestoreSottotitoli;
import Sottotitoli.SerieSub;

public class Interfaccia {
	public static JTabbedPane	TabbedPane;
	public static JTabbedPane	TabbedDownload		= new JTabbedPane();
	public static JPanel		panel_download		= new JPanel(new BorderLayout());
	private static JPanel		panel_opzioni		= new JPanel(new BorderLayout());
	private static JPanel		panel_sottotitoli	= new JPanel(new BorderLayout());
	private static JPanel		panel_refactor		= new JPanel(new BorderLayout());
	private static JPanel		panel_about			= new JPanel(new BorderLayout());
	public static JFrame		frame;
	public static SystemTray	tray;
	public static boolean		isHidden			= true;
	public static final int		Index_Download		= 0;
	public static final int		Index_Refactor		= 1;
	public static final int		Index_Opzioni		= 2;
	
	public static void createPanel() {
		Dimension dim_screen=Toolkit.getDefaultToolkit().getScreenSize();
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		
		frame.setBounds(0, 0, 750, dim_screen.height<=600?500:600);
		frame.setIconImage(Resource.getIcona("res/icona32.png").getImage());
		frame.setTitle(Language.TITLE);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(3);
		frame.setAlwaysOnTop(Settings.isAlwaysOnTop());
		frame.revalidate();
		frame.repaint();

		TabbedPane = new JTabbedPane();
		frame.add(TabbedPane);

		creaTabDownload();
		creaTabRefactor();
		creaTabOpzioni();
		OpzioniInizializzaCampi();
		creaTabItasa();
		creaTabAbout();

		TabbedPane.addTab(Language.TAB_DOWNLOAD_TITLE, Resource.getIcona("res/download.png"), TabbedDownload, Language.TAB_DOWNLOAD_TOOLTIP);
		TabbedPane.addTab(Language.TAB_REFACTOR_LABEL, Resource.getIcona("res/player.png"), panel_refactor, Language.TAB_REFACTOR_TOOLTIP);
		TabbedPane.addTab(Language.TAB_OPZIONI_LABEL, Resource.getIcona("res/opzioni.png"), panel_opzioni, Language.TAB_OPZIONI_TOOLTIP);
		TabbedPane.addTab(Language.TAB_ABOUT, Resource.getIcona("res/info.png"), panel_about, "");

		TabbedPane.setSelectedIndex(0);
		setTray();
		
		if (!Settings.isStartHidden())
			frame.setVisible(true);

		frame.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				if (Settings.isAskOnClose()) {
					int scelta = JOptionPane.showConfirmDialog(frame, Language.DIALOGUE_EXIT_PROMPT, Language.DIALOGUE_EXIT_PROMPT_TITLE, 0);
					if (scelta == JOptionPane.YES_OPTION) {
						Database.Database.Disconnect();
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
					else {
						frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						return;
					}
				}
			}
			public void windowIconified(WindowEvent e) {
				frame.setVisible(false);
				Runtime.getRuntime().gc();
			}
			public void windowDeiconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {
				windowClosing(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			public void windowActivated(WindowEvent arg0) {	}
		});
	}
	
	protected static JPanel				download_panel_inserimento			= new JPanel();
	protected static JPanel				download_panel_download				= new JPanel();
	protected static JButton			download_bottone_aggiungi			= new JButton();
	protected static JButton			download_bottone_rimuovi			= new JButton();
	protected static JButton			download_bottone_reload				= new JButton();
	private static JComboBox<SerieTV>	download_combo_proprie				= new JComboBox<SerieTV>();
	private static JComboBox<SerieTV>	download_combo_eztv					= new JComboBox<SerieTV>();
	protected static JTextField			download_text_top_left				= new JTextField();
	protected static JTextField			download_text_top_right				= new JTextField();
	protected static JButton			download_bottone_aggiorna_torrent	= new JButton();
	protected static JButton			download_bottone_torrent_offline	= new JButton();
	private static JLabel				download_label_stato				= new JLabel("");
	public static JLabel				download_label_notifiche			= new JLabel("");
	protected static JButton			download_bottone_selectAll			= new JButton();
	protected static JButton			download_bottone_inverti_selezione	= new JButton();
	protected static JButton			download_bottone_720p				= new JButton();
	protected static JButton			download_bottone_already_seen		= new JButton();
	public static JButton				download_bottone_download			= new JButton();
	protected static JButton			download_bottone_esplora			= new JButton();
	private static JPanel				download_panel_scroll				= new JPanel();
	private static JButton				download_bottone_test				= new JButton("42");
	private static JWebBrowser			download_browser_adv;
	private static JFrame				download_advertising_window			= null;
	private static JLabel 				download_label_adv					= new JLabel();
	
	private static void creaTabDownload() {
		TabbedDownload.addTab(Language.TAB_DOWNLOAD_EPISODI, panel_download);
		TabbedDownload.addTab(Language.TAB_SOTTOTITOLI_LABEL, panel_sottotitoli);
		panel_download.setLayout(new BorderLayout());
		panel_download.add(download_panel_inserimento, BorderLayout.NORTH);
		panel_download.add(download_panel_download, BorderLayout.CENTER);
		createInserimento();
		createDownload();
		download_bottone_aggiorna_torrent.doClick();
		TabbedDownload.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(TabbedDownload.getSelectedIndex()==1)
					sottotitoli_bottone_list.doClick();
			}
		});
	}

	private static void createDownload() {
		download_bottone_download.setFont(new Font("Arial", 1, 16));
		download_bottone_already_seen.setFont(new Font("Arial", 1, 16));
		download_panel_download.setBorder(new TitledBorder(""));
		download_panel_download.setLayout(new BorderLayout());
		JPanel panel_top = new JPanel();
		panel_top.setLayout(new BorderLayout());
		JPanel panel_top_north = new JPanel();
		panel_top_north.add(download_bottone_aggiorna_torrent);
		panel_top_north.add(download_bottone_torrent_offline);
		panel_top_north.add(download_label_stato);
		panel_top_north.add(download_label_notifiche);
		JPanel top_center = new JPanel();
		top_center.add(download_bottone_selectAll);
		top_center.add(download_bottone_inverti_selezione);
		top_center.add(download_bottone_720p);
		panel_top.add(top_center, BorderLayout.CENTER);
		panel_top.add(panel_top_north, BorderLayout.NORTH);
		download_panel_download.add(panel_top, BorderLayout.NORTH);
		download_panel_scroll.setLayout(new GridLayout(4, 1));
		JScrollPane panel_scrollpane = new JScrollPane(download_panel_scroll);
		panel_scrollpane.getVerticalScrollBar().setUnitIncrement(8);
		download_panel_download.add(panel_scrollpane, BorderLayout.CENTER);
		final JPanel panel_bottom = new JPanel();
		panel_bottom.setLayout(new BorderLayout());
		JPanel panel_bottom_center = new JPanel();
		panel_bottom_center.add(download_bottone_download);
		panel_bottom_center.add(download_bottone_already_seen);
		panel_bottom_center.add(download_bottone_esplora);
		panel_bottom.add(panel_bottom_center, BorderLayout.CENTER);

		try {
			Main.panel_adv.join();
		}
		catch (InterruptedException e2) {e2.printStackTrace();}
		
		JPanel panel_bottom_sud=new JPanel(new BorderLayout());
		panel_bottom_sud.add(download_browser_adv=ThreadPanelBrowser.getBrowser(), BorderLayout.CENTER);
		panel_bottom_sud.add(download_label_adv=new JLabel("Clicca gli sponsor per aiutare lo sviluppo del software"), BorderLayout.SOUTH);
		download_label_adv.setFont(new Font("Tahoma", Font.BOLD, 12));
		frame.add(panel_bottom_sud, BorderLayout.SOUTH);
		download_panel_download.add(panel_bottom, BorderLayout.SOUTH);

		download_browser_adv.addWebBrowserListener(new WebBrowserListener(){
			public void windowWillOpen(WebBrowserWindowWillOpenEvent e) {}
			public void windowOpening(WebBrowserWindowOpeningEvent e) {}
			public void windowClosing(WebBrowserEvent e) {}
			public void titleChanged(final WebBrowserEvent e) {
				if(e.getWebBrowser().getPageTitle().compareToIgnoreCase("about:blank")==0 || e.getWebBrowser().getPageTitle().isEmpty() || e.getWebBrowser().getPageTitle()==null)
					return;
				if(e.getWebBrowser().getPageTitle().compareToIgnoreCase(Advertising.url_ads_alter)!=0){
					if(download_advertising_window==null){
						download_advertising_window=new JFrame();
						download_advertising_window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						download_advertising_window.setLayout(new BorderLayout());
						download_advertising_window.add(e.getWebBrowser());
						download_advertising_window.setVisible(true);
						download_advertising_window.setAlwaysOnTop(Settings.isAlwaysOnTop());
						download_advertising_window.addWindowListener(new WindowListener() {
							public void windowOpened(WindowEvent arg0) {}
							public void windowIconified(WindowEvent arg0) {	}
							public void windowDeiconified(WindowEvent arg0) {}
							public void windowDeactivated(WindowEvent arg0) {}
							public void windowClosing(WindowEvent arg0) {}
							public void windowClosed(WindowEvent arg0) {
								download_advertising_window=null;
							}
							public void windowActivated(WindowEvent arg0) {}
						});
					}
					download_advertising_window.setSize(1024, 600);
				}
			}
			public void statusChanged(WebBrowserEvent e) {}
			public void locationChanging(WebBrowserNavigationEvent e) {	}
			public void locationChanged(WebBrowserNavigationEvent e) {}
			public void locationChangeCanceled(WebBrowserNavigationEvent e) {}
			public void loadingProgressChanged(WebBrowserEvent e) {}
			public void commandReceived(WebBrowserCommandEvent e) {}
		});
		download_bottone_esplora.setFont(new Font("Tahoma", Font.BOLD, 14));
		download_bottone_esplora.setIcon(Resource.getIcona("res/folder_24.png"));
		download_bottone_esplora.setText(Language.OPZIONI_ESPLORA);
		download_bottone_esplora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					OperazioniFile.esploraCartella(Settings.getDirectoryDownload());
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, "Imposta una cartella dove scaricare gli episodi");
				}
			}
		});
		download_bottone_aggiorna_torrent.setIcon(Resource.getIcona("res/aggiorna.png"));
		download_bottone_aggiorna_torrent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				download_label_stato.setText(Language.DOWNLOAD_ATTENDERE);

				class ThreadUpdate extends Thread {
					public void run() {
						download_bottone_aggiorna_torrent.setEnabled(false);
						GestioneSerieTV.aggiornaListeTorrent();
						updatePanel();
						download_bottone_aggiorna_torrent.setEnabled(true);
						Settings.setCanStartDownloadAutomatico(true);
					}

					private void updatePanel() {
						download_bottone_torrent_offline.doClick();
					}
				}
				ThreadUpdate t1 = new ThreadUpdate();
				t1.start();
			}
		});
		download_bottone_download.setIcon(Resource.getIcona("res/scarica.png"));
		download_bottone_download.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				class BottoneDownload extends Thread {
					public void run(){
						try {
							int initial_count=download_panel_scroll.getComponentCount();
							int removed=0;
							if(!Settings.verificaUtorrent()){
								JOptionPane.showMessageDialog(frame, "Configurare il percorso di uTorrent correttamente");
								return;
							}
							for (int i = 0; i < download_panel_scroll.getComponentCount();) {
								CasellaDownload casella=(CasellaDownload) download_panel_scroll.getComponent(i);
								if(casella.getCheckBox().isSelected()){
									casella.scarica.doClick();
									removed++;
									Thread.sleep(200);
								}
								else
									i++;
							}
							download_label_stato.setText((initial_count-removed) + Language.DOWNLOAD_PUNTATE);
							Interfaccia.libreria_addItemBoxSerie();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				Thread t=new BottoneDownload();
				t.start();
			}
		});
		download_bottone_torrent_offline.setIcon(Resource.getIcona("res/offline.png"));
		download_bottone_torrent_offline.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				RidisegnaScrollPanel();
			}
		});
		download_bottone_already_seen.setIcon(Resource.getIcona("res/remove_1.png"));
		download_bottone_already_seen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				class BottoneGiaViste extends Thread {
					public void run() {
						int choise = JOptionPane.showConfirmDialog(Interfaccia.frame, Language.DOWNLOAD_DIALOGUE_GIA_VISTE, Language.DOWNLOAD_DIALOGUE_GIA_VISTE_TITLE, 0);
						if (choise == 0) {
							for(int i=0;i<download_panel_scroll.getComponentCount();i++){
								CasellaDownload casella=(CasellaDownload) download_panel_scroll.getComponent(i);
								if(casella.getCheckBox().isSelected()){
									casella.getTorrent().setScaricato(Torrent.IGNORATO, true);
								}
							}
							RidisegnaScrollPanel();
							/*
							ArrayList<Torrent> rimuovere=new ArrayList<Torrent>();
							for(int i=0;i<download_panel_scroll.getComponentCount();i++){
								CasellaDownload casella=(CasellaDownload) download_panel_scroll.getComponent(i);
								if(casella.getCheckBox().isSelected()){
									Torrent t=casella.getTorrent();
									rimuovere.add(t);
								}
							}
							for(int i=0;i<rimuovere.size();i++)
								rimuovere.get(i).setScaricato(Torrent.IGNORATO, false);
							RidisegnaScrollPanel();
							for(int i=0;i<rimuovere.size();i++)
								rimuovere.get(i).update();
							Interfaccia.libreria_addItemBoxSerie();
							rimuovere.clear();
							rimuovere=null;
							*/
						}
					}
				}
				Thread t=new BottoneGiaViste();
				t.start();
			}
		});
		download_bottone_selectAll.setIcon(Resource.getIcona("res/selectall.png"));
		download_bottone_selectAll.addActionListener(new ActionListener(){
			private int current_status=0;
			private final static int SELECT_ALL=0;
			private final static int DESELECT_ALL=1;
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<download_panel_scroll.getComponentCount();i++){
					CasellaDownload casella=(CasellaDownload) download_panel_scroll.getComponent(i);
					casella.getCheckBox().setSelected(current_status==SELECT_ALL?true:false);
				}
				switch(current_status){
					case SELECT_ALL:
						download_bottone_selectAll.setIcon(Resource.getIcona("res/deselectall.png"));
						download_bottone_selectAll.setText(Language.DOWNLOAD_BOTTONE_DESELEZIONA_TUTTO);
						current_status=DESELECT_ALL;
						break;
					case DESELECT_ALL:
						download_bottone_selectAll.setIcon(Resource.getIcona("res/selectall.png"));
						download_bottone_selectAll.setText(Language.DOWNLOAD_BOTTONE_SELEZIONA_TUTTO);
						current_status=SELECT_ALL;
						break;
				}
			}
		});
		download_bottone_720p.setIcon(Resource.getIcona("res/720.png"));
		download_bottone_720p.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<download_panel_scroll.getComponentCount();i++){
					CasellaDownload casella=(CasellaDownload) download_panel_scroll.getComponent(i);
					casella.getCheckBox().setSelected(casella.getTorrent().is720p()?true:false);
				}
			}
		});
		download_bottone_inverti_selezione.setIcon(Resource.getIcona("res/inverti.png"));
		download_bottone_inverti_selezione.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<download_panel_scroll.getComponentCount();i++){
					CasellaDownload casella=(CasellaDownload) download_panel_scroll.getComponent(i);
					JCheckBox check=casella.getCheckBox();
					check.setSelected(!check.isSelected());
				}
			}
		});
	}
	
	public static void RidisegnaScrollPanel() {
		class RidisegnaScroll extends Thread {
			public void run() {
				download_panel_scroll.removeAll();
				ArrayList<Torrent> download_torrent_download = GestioneSerieTV.getTorrentDownload();
				GridLayout lay = (GridLayout) download_panel_scroll.getLayout();
				lay.setColumns(1);
				lay.setRows(download_torrent_download.size() > 4 ? download_torrent_download.size() : 4);
				
				for (int i = 0; i < download_torrent_download.size(); i++) {
					Torrent tor = (Torrent) download_torrent_download.get(i);
					CasellaDownload panel_t=new CasellaDownload(tor, download_panel_scroll);
					download_panel_scroll.add(panel_t);
				}
				lay.setRows(download_torrent_download.size() > 4 ? download_torrent_download.size() : 4);
				download_label_stato.setText(download_panel_scroll.getComponentCount() + Language.DOWNLOAD_PUNTATE);
				download_panel_scroll.revalidate();
				download_panel_scroll.repaint();
			}
		}
		Thread t=new RidisegnaScroll();
		t.start();
	}

	private static void createInserimento() {
		download_panel_inserimento.setBorder(new TitledBorder(""));
		download_panel_inserimento.setLayout(new GridLayout(1, 2));
		JPanel destra = new JPanel();
		JPanel sinistra = new JPanel();
		JPanel top_destra = new JPanel();
		JPanel top_sinistra = new JPanel();
		top_sinistra.setLayout(new BorderLayout());
		JLabel label_top_left = new JLabel(Language.INSERIMENTO_LABEL_EZTV + "  ");
		label_top_left.setFont(new Font("Tahoma", 3, 14));
		top_sinistra.add(label_top_left, BorderLayout.WEST);
		top_sinistra.add(download_text_top_left, BorderLayout.CENTER);
		download_bottone_reload.setIcon(Resource.getIcona("res/aggiorna.png"));
		top_sinistra.add(download_bottone_reload, BorderLayout.EAST);
		sinistra.setLayout(new BorderLayout());
		sinistra.add(top_sinistra, BorderLayout.NORTH);
		sinistra.add(download_combo_eztv, BorderLayout.CENTER);
		sinistra.add(download_bottone_aggiungi, BorderLayout.EAST);
		download_panel_inserimento.add(sinistra);
		top_destra.setLayout(new BorderLayout());
		JLabel label_top_right = new JLabel(Language.INSERIMENTO_LABEL_PROPRIE + "  ");
		label_top_right.setFont(new Font("Tahoma", 3, 14));
		top_destra.add(label_top_right, BorderLayout.WEST);
		top_destra.add(download_text_top_right, BorderLayout.CENTER);
		top_destra.add(download_bottone_test, BorderLayout.EAST);
		destra.setLayout(new BorderLayout());
		destra.add(top_destra, BorderLayout.NORTH);
		destra.add(download_combo_proprie, BorderLayout.CENTER);
		destra.add(download_bottone_rimuovi, BorderLayout.EAST);
		download_panel_inserimento.add(destra);

		aggiornaComboEZTV();
		AggiornaComboBoxProprie();

		download_bottone_aggiungi.setIcon(Resource.getIcona("res/add.png"));
		download_bottone_aggiungi.setEnabled(GestioneSerieTV.getElencoSerieCompleto().size() > 0);
		download_bottone_aggiungi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				class BottoneAggiungi extends Thread{
					public void run(){
						SerieTV st=(SerieTV) download_combo_eztv.getSelectedItem();
						if(GestioneSerieTV.aggiungiSerie(st)){
							new ThreadModificaLabel(download_label_notifiche, "  " + st.getNomeSerie() + " " + Language.INSERIMENTO_LABEL_INSERITO + "  ", 1500);
							//TODO verificare il funzionamento
							if(!GestioneSerieTV.getSubManager().associaSerie(st)){
								if(Settings.isRicercaSottotitoli()){
									int scelta=(JOptionPane.showConfirmDialog(frame, "Non è stato possibile associare la serie a ItaSA.\nVuoi associarla manualmente?", "Associa ItaSA", JOptionPane.YES_NO_OPTION));
									if(scelta==JOptionPane.YES_OPTION){
										associaFrame();
										associa_serie.setSelectedItem((SerieTV)download_combo_eztv.getSelectedItem());
									}
								}
							}
							download_text_top_left.setText("");
							aggiornaComboEZTV();
							AggiornaComboBoxProprie();
							Thread t=st.aggiornaTorrentList();
							try {
								t.join();
								RidisegnaScrollPanel();
							}
							catch (InterruptedException e) {
								e.printStackTrace();
							}
							Interfaccia.libreria_addItemBoxSerie();
						}
					}
				}
				Thread t=new BottoneAggiungi();
				t.start();
			}
		});
		download_bottone_rimuovi.setIcon(Resource.getIcona("res/remove.png"));
		download_bottone_rimuovi.setEnabled(GestioneSerieTV.getElencoSerieInserite().size() > 0);
		download_bottone_rimuovi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				class BottoneRimuovi extends Thread {
					public void run(){
						SerieTV serie = (SerieTV) download_combo_proprie.getSelectedItem();
						if (serie != null) {
							int scelta = JOptionPane.showConfirmDialog(Interfaccia.frame, Language.INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE + serie.getNomeSerie().trim() + "\n", Language.INSERIMENTO_DIALOGUE_CONFERMA_RIMOZIONE_TITLE, 0);
							if (scelta == JOptionPane.YES_OPTION) {
								String folder = serie.getNomeSerieFolder();
								if (GestioneSerieTV.rimuoviSerie(serie)) {
									libreria_box_serie.removeItem(serie);
									File cartella = new File(Settings.getDirectoryDownload() + File.separator + folder);
									if (cartella.isDirectory()) {
										int dir_del = JOptionPane.showConfirmDialog(Interfaccia.frame, Language.INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA, Language.INSERIMENTO_DIALOGUE_CANCELLA_CARTELLA_TITLE, 0);
										if (dir_del == JOptionPane.YES_OPTION)
											OperazioniFile.DeleteDirectory(cartella);
									}
									download_text_top_right.setText("");
									AggiornaComboBoxProprie();
									download_bottone_rimuovi.setEnabled(GestioneSerieTV.getElencoSerieInserite().size() > 0);
									new ThreadModificaLabel(download_label_notifiche, "  " + folder + " " + Language.INSERIMENTO_LABEL_RIMOSSA + "  ", 1500);
								}
							}
							RidisegnaScrollPanel();
						}
						else {
							JOptionPane.showMessageDialog(Interfaccia.frame, Language.INSERIMENTO_DIALOGUE_ERRORE_RIMOZIONE);
						}
					}
				}
				Thread t=new BottoneRimuovi();
				t.start();
			}
		});
		download_bottone_reload.setIcon(Resource.getIcona("res/aggiorna.png"));
		download_bottone_reload.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				class BottoneReload extends Thread{
					public void run(){
						download_bottone_reload.setEnabled(false);
						if (GestioneSerieTV.Showlist()) {
							download_combo_eztv.removeAllItems();
							aggiornaComboEZTV();
							download_bottone_aggiungi.setEnabled(GestioneSerieTV.getElencoSerieCompleto().size() > 0);
							download_text_top_left.setText("");
						}
						download_bottone_reload.setEnabled(true);
					}
				}
				BottoneReload r=new BottoneReload();
				r.start();
			}
		});
		download_text_top_left.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {
				AggiornaComboBoxEZTV_Ricerca(download_text_top_left.getText().trim());
			}
			public void keyPressed(KeyEvent arg0) {}
		});
		download_text_top_left.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {
				if (download_text_top_left.getText().compareToIgnoreCase(Language.INSERIMENTO_BOXRICERCA) == 0) {
					download_text_top_left.setText("");
					aggiornaComboEZTV();
				}
			}
		});
		download_text_top_right.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {
				AggiornaComboBoxProprie_Ricerca(download_text_top_right.getText().trim());
			}

			public void keyPressed(KeyEvent arg0) {}
		});
		download_text_top_right.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {
				if (download_text_top_right.getText().compareToIgnoreCase(Language.INSERIMENTO_BOXRICERCA) == 0) {
					download_text_top_right.setText("");
					AggiornaComboBoxProprie();
				}
			}
		});
		download_bottone_test.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(Interfaccia.frame,
						"English: \nJust wait a sodding minute! You want a question that goes with the answer for 42? Well, how about \"What's six times seven?\"\n\n"
					  + "Italiano: \nLa domanda, l'unica della quale avrei voluto la risposta: ï¿½ la ragazza giusta? E la risposta non ï¿½ 42\n");
			}
		});
	}

	private static void AggiornaComboBoxEZTV_Ricerca(String cerca) {
		if (cerca.isEmpty()) {
			aggiornaComboEZTV();
			return;
		}
		download_combo_eztv.removeAllItems();
		for (int i = 0; i < GestioneSerieTV.getElencoSerieCompleto().size(); i++) {
			SerieTV st = GestioneSerieTV.getElencoSerieCompleto().get(i);
			if (st.getNomeSerie().toLowerCase().contains(cerca.toLowerCase()))
				download_combo_eztv.addItem(st);
		}
		download_combo_eztv.setEnabled(download_combo_eztv.getItemCount() > 0);
		download_bottone_aggiungi.setEnabled(download_combo_eztv.getItemCount() > 0);
	}

	private static void AggiornaComboBoxProprie_Ricerca(String cerca) {
		if (cerca.isEmpty()) {
			AggiornaComboBoxProprie();
			return;
		}
		download_combo_proprie.removeAllItems();
		for (int i = 0; i < GestioneSerieTV.getElencoSerieInserite().size(); i++) {
			SerieTV st = GestioneSerieTV.getElencoSerieInserite().get(i);
			if (st.getNomeSerie().toLowerCase().contains(cerca.toLowerCase()))
				download_combo_proprie.addItem(st);
		}
		download_combo_proprie.setEnabled(download_combo_proprie.getItemCount() > 0);
		download_bottone_rimuovi.setEnabled(download_combo_proprie.getItemCount() > 0);
	}

	private static void AggiornaComboBoxProprie() {
		download_combo_proprie.removeAllItems();
		for (int i = 0; i < GestioneSerieTV.getElencoSerieInserite().size(); i++) {
			download_combo_proprie.addItem(GestioneSerieTV.getElencoSerieInserite().get(i));
		}
		download_combo_proprie.setEnabled(GestioneSerieTV.getElencoSerieInserite().size() > 0);
		download_bottone_rimuovi.setEnabled(GestioneSerieTV.getElencoSerieInserite().size() > 0);
	}

	private static void aggiornaComboEZTV() {
		download_combo_eztv.removeAllItems();
		for (int i = 0; i < GestioneSerieTV.getElencoSerieCompleto().size(); i++)
			download_combo_eztv.addItem(GestioneSerieTV.getElencoSerieCompleto().get(i));
		download_combo_eztv.setEnabled(GestioneSerieTV.getElencoSerieCompleto().size() > 0);
		download_bottone_aggiungi.setEnabled(GestioneSerieTV.getElencoSerieCompleto().size() > 0);
	}
	
	protected static JLabel				opzioni_label_client					= new JLabel("Client: ");
	protected static JLabel				opzioni_label_path_download				= new JLabel(Language.OPZIONI_CLIENT_LABEL_DIRECTORYDOWNLOAD);
	protected static JLabel				opzioni_label_path_client				= new JLabel(Language.OPZIONI_CLIENT_LABEL_PERCORSO);
	protected static JLabel				opzioni_label_cerca_min					= new JLabel(Language.OPZIONI_RICERCA_MINUTIRICERCA);
	protected static JLabel				opzioni_label_lingua					= new JLabel(Language.OPZIONI_LINGUA);
	protected static JButton			opzioni_bottone_directory_download		= new JButton(Language.OPZIONI_CLIENT_BOTTONE_DIRECTORYDOWNLOAD);
	protected static JButton			opzioni_bottone_seleziona_client		= new JButton(Language.OPZIONI_CLIENT_BOTTONE_CLIENTPATH);
	protected static JButton			opzioni_bottone_salva					= new JButton(Language.OPZIONI_SALVAOPZIONI);
	private static JTextField			opzioni_textfield_directory_download	= new JTextField(30);
	private static JTextField			opzioni_textfield_attuale_client		= new JTextField(30);
	protected static JTextField			opzioni_textfield_minuti				= new JTextField(2);
	
	protected static JCheckBox			opzioni_box_askonclose					= new JCheckBox(Language.OPZIONI_AVVIO_CONFERMA_CHIUSURA);
	protected static JCheckBox			opzioni_box_starthidden					= new JCheckBox(Language.OPZIONI_AVVIO_AVVIO_ICONA);
	protected static JCheckBox			opzioni_box_startwindows				= new JCheckBox(Language.OPZIONI_AVVIO_AVVIO_WINDOWS);
	protected static JCheckBox			opzioni_box_abilita_ricerca				= new JCheckBox(Language.OPZIONI_RICERCA_ABILITARICERCA);
	private static JComboBox<String>	opzioni_combo_lingua					= new JComboBox<String>();
	protected static JCheckBox			opzioni_box_itasa						= new JCheckBox();
	protected static JButton			opzioni_bottone_ripristina				= new JButton(Language.OPZIONI_DEFAULT);
	protected static JCheckBox			opzione_box_alwaysontop					= new JCheckBox(Language.OPZIONI_ALWAYSONTOP);
	protected static JButton			opzioni_bottone_esplora					= new JButton(Language.OPZIONI_ESPLORA);
	
	protected static JLabel				opzioni_label_vlc						= new JLabel(Language.OPZIONI_VLC_LABEL);
	protected static JTextField			opzioni_text_vlc						= new JTextField(30);
	protected static JButton			opzioni_button_vlc						= new JButton("Cerca");
	
	private static JLabel				opzioni_lab_itasa_user					= new JLabel("Username");
	private static JLabel				opzioni_lab_itasa_pass					= new JLabel("Password");
	private static JTextField			opzioni_text_itasa_user					= new JTextField(25);
	private static JPasswordField		opzioni_text_itasa_pass					= new JPasswordField(25);
	private static JRadioButton			opzioni_itasa_notifica					= new JRadioButton("Notifica");
	private static JRadioButton 		opzioni_itasa_download					= new JRadioButton("Download");
	
	private static void creaTabOpzioni() {
		@SuppressWarnings("serial")
		class PanelOpzione extends JPanel{
			public PanelOpzione(JComponent[] el, int r){
				int righe=r;
				if(el.length>r)
					righe=el.length;
				setLayout(new GridLayout(righe, 1));
				for(int i=0;i<el.length;i++){
					JPanel p=new JPanel(new BorderLayout());
					p.add(el[i], BorderLayout.WEST);
					add(p);
				}
			}
		}
		JPanel p_bottom=new JPanel();
		p_bottom.add(opzioni_bottone_salva);
		p_bottom.add(opzioni_bottone_ripristina);
		panel_opzioni.add(p_bottom, BorderLayout.SOUTH);
		JTabbedPane tab=new JTabbedPane();
		panel_opzioni.add(tab, BorderLayout.CENTER);
		JPanel tab_aspetto=new JPanel(new BorderLayout()), 
			   tab_programmi=new JPanel(new BorderLayout()),
			   tab_download=new JPanel(new BorderLayout());
		tab.addTab("Aspetto", tab_aspetto);
		tab.addTab("Programmi", tab_programmi);
		tab.addTab("Download", tab_download);
		
		JPanel pan_lingua=new JPanel();
		pan_lingua.add(opzioni_label_lingua);
		pan_lingua.add(opzioni_combo_lingua);
		
		JComponent[] componenti_aspetto={pan_lingua, 
				opzioni_box_startwindows, 
				opzioni_box_starthidden, 
				opzione_box_alwaysontop,
				opzioni_box_askonclose,
		}; 
		PanelOpzione opzioni_aspetto=new PanelOpzione(componenti_aspetto, 10);
		tab_aspetto.add(opzioni_aspetto);
		
		JPanel p_cl_1=new JPanel();
		p_cl_1.add(opzioni_label_path_client);
		p_cl_1.add(opzioni_textfield_attuale_client);
		p_cl_1.add(opzioni_bottone_seleziona_client);
		final JPanel p_dir=new JPanel();
		p_dir.add(opzioni_label_path_download);
		p_dir.add(opzioni_textfield_directory_download);
		p_dir.add(opzioni_bottone_directory_download);
		p_dir.add(opzioni_bottone_esplora);
		
		JPanel p_vlc=new JPanel();
		p_vlc.add(opzioni_label_vlc);
		p_vlc.add(opzioni_text_vlc);
		p_vlc.add(opzioni_button_vlc);
		
		JComponent[] componenti_prog={p_cl_1,
				p_dir,
				p_vlc,
		};
		PanelOpzione opzioni_programmi=new PanelOpzione(componenti_prog, 10);
		tab_programmi.add(opzioni_programmi);
		
		JPanel p_da=new JPanel();
		p_da.add(opzioni_box_abilita_ricerca);
		JPanel p_da1=new JPanel();
		p_da1.add(opzioni_textfield_minuti);
		p_da1.add(opzioni_label_cerca_min);
		JComponent[] componenti_down1={
				p_da,
				p_da1
		};
		PanelOpzione opzioni_download=new PanelOpzione(componenti_down1, 2);
		opzioni_download.setBorder(new TitledBorder("Ricerca"));
		tab_download.add(opzioni_download, BorderLayout.NORTH);
		JPanel it_1=new JPanel(), it_2=new JPanel();
		it_1.add(opzioni_lab_itasa_user);
		it_1.add(opzioni_text_itasa_user);
		it_2.add(opzioni_lab_itasa_pass);
		it_2.add(opzioni_text_itasa_pass);
		JPanel itasa_notifica_pan_opt=new JPanel();
		ButtonGroup g_opt_itasa=new ButtonGroup();
		if(Settings.isItasaThreadAutoDownload())
			opzioni_itasa_download.setSelected(true);
		else
			opzioni_itasa_notifica.setSelected(true);
		g_opt_itasa.add(opzioni_itasa_download);
		g_opt_itasa.add(opzioni_itasa_notifica);
		itasa_notifica_pan_opt.add(opzioni_itasa_download);
		itasa_notifica_pan_opt.add(opzioni_itasa_notifica);
		JComponent[] componenti_down_2={
				opzioni_box_itasa,
				it_1,
				it_2,
				itasa_notifica_pan_opt
		};
		PanelOpzione opzioni_itasa=new PanelOpzione(componenti_down_2, 8);
		opzioni_itasa.setBorder(new TitledBorder("ItaSA"));
		tab_download.add(opzioni_itasa);

		opzioni_text_itasa_user.setText(Settings.getItasaUsername());
		opzioni_textfield_directory_download.setEditable(false);
		opzioni_textfield_attuale_client.setEditable(false);
		opzioni_text_vlc.setEditable(false);
		opzioni_textfield_minuti.setToolTipText(Language.OPZIONI_MINUTI_RICERCA_TOOLTIP);
		
		for(int i=0;i<Language.lingue.length;i++)
			opzioni_combo_lingua.addItem(Language.lingue[i]);

		opzioni_bottone_seleziona_client.setIcon(Resource.getIcona("res/utorrent.png"));
		
		opzioni_itasa_download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.setItasaThreadAutoDownload(opzioni_itasa_download.isSelected());
			}
		});
		opzioni_itasa_notifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.setItasaThreadAutoDownload(opzioni_itasa_notifica.isSelected());
			}
		});
		//TODO modificare per linux e mac
		opzioni_bottone_seleziona_client.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser filechooser=new JFileChooser();
				String client="*.*";
				String description="TUTTO";
				
				if(Settings.isWindows()){
					client = "utorrent.exe";
					description = "uTorrent";
				}
				filechooser.setFileFilter(new ClientFilter(description, client));
				if (filechooser.showOpenDialog(Interfaccia.frame) == 0) {
					File f = filechooser.getSelectedFile();
					if(Settings.isWindows()){
						if(f.getName().compareToIgnoreCase("utorrent.exe")!=0){
							JOptionPane.showMessageDialog(frame, "L'unico client utilizzabile ï¿½ uTorrent");
							return;
						}
					}
					Settings.setClientPath(f.getAbsolutePath());
					opzioni_textfield_attuale_client.setText(f.getAbsolutePath());
					Interfaccia.download_bottone_download.setEnabled(true);
				}
			}
		});
		opzioni_button_vlc.setIcon(Resource.getIcona("res/vlc.png"));
		opzioni_button_vlc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser filechooser = new JFileChooser();
				String client = "vlc.exe";
				String description = "VideoLAN Client - vlc.exe";
				
				filechooser.setFileFilter(new ClientFilter(description, client));
				if (filechooser.showOpenDialog(Interfaccia.frame) == 0) {
					File f = filechooser.getSelectedFile();
					Settings.setVLCPath(f.getAbsolutePath());
					System.out.println(f.getAbsolutePath());
					opzioni_text_vlc.setText(f.getAbsolutePath());
				}
			}
		});
		opzioni_bottone_directory_download.setIcon(Resource.getIcona("res/cartella.png"));
		opzioni_bottone_directory_download.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser dir_choose = new JFileChooser();
				dir_choose.setFileSelectionMode(1);
				dir_choose.setAcceptAllFileFilterUsed(false);
				if (dir_choose.showOpenDialog(Interfaccia.frame) == 0) {
					Settings.setDirectoryDownload(dir_choose.getSelectedFile().getAbsolutePath());
					opzioni_textfield_directory_download.setText(Settings.getDirectoryDownload());
				}
			}
		});
		opzioni_bottone_esplora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					OperazioniFile.esploraCartella(Settings.getDirectoryDownload());
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
			}
		});
		opzioni_box_askonclose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Settings.setAskOnClose(opzioni_box_askonclose.isSelected());
			}
		});
		opzioni_box_starthidden.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Settings.setStartHidden(opzioni_box_starthidden.isSelected());
			}
		});
		opzioni_box_startwindows.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if ((Settings.setAutostart(opzioni_box_startwindows.isSelected())))
					Settings.createAutoStart();
				else
					Settings.removeAutostart();
			}
		});
		opzioni_box_abilita_ricerca.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Settings.setDownloadAutomatico(opzioni_box_abilita_ricerca.isSelected());
				if (opzioni_box_abilita_ricerca.isSelected()) {
					Main.avviaThreadRicercaAutomatica();
				}
				else
					Main.thread_autosearch.interrupt();
			}
		});
		opzioni_bottone_salva.setIcon(Resource.getIcona("res/salva.png"));
		opzioni_bottone_salva.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try {
					Settings.setMinRicerca(Integer.parseInt(opzioni_textfield_minuti.getText().trim()));
				}
				catch (NumberFormatException e) {
					Settings.setMinRicerca(480);
					opzioni_textfield_minuti.setText("480");
				}
				Settings.setClientPath(opzioni_textfield_attuale_client.getText().trim());
				Settings.setDirectoryDownload(opzioni_textfield_directory_download.getText().trim());
				Settings.setVLCPath(opzioni_text_vlc.getText().trim());
				Settings.setItasaUsername(opzioni_text_itasa_user.getText());
				if(opzioni_text_itasa_pass.getPassword().length>0)
					Settings.setItasaPassword(String.copyValueOf(opzioni_text_itasa_pass.getPassword()));
			}
		});
		opzioni_combo_lingua.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int index = opzioni_combo_lingua.getSelectedIndex();
				Settings.setLingua(index+1);
				Language.setLanguage(index+1);
				Language.setLang();
			}
		});
		opzioni_box_itasa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Settings.setRicercaSottotitoli(opzioni_box_itasa.isSelected());
				if(Settings.isItasaThreadAutoDownload())
					GestioneSerieTV.getSubManager().avviaRicercaAutomatica();
				else
					GestioneSerieTV.getSubManager().stopRicercaAutomatica();
			}
		});
		opzioni_bottone_ripristina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.setDefault();
				OpzioniInizializzaCampi();
			}
		});
		opzione_box_alwaysontop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				Settings.setAlwaysOnTop(opzione_box_alwaysontop.isSelected());
				frame.setAlwaysOnTop(Settings.isAlwaysOnTop());
			}
		});
	}
	private static void OpzioniInizializzaCampi(){
		opzioni_box_startwindows.setSelected(Settings.isAutostart());
		opzioni_box_askonclose.setSelected(Settings.isAskOnClose());
		opzioni_box_starthidden.setSelected(Settings.isStartHidden());
		opzioni_box_abilita_ricerca.setSelected(Settings.isDownloadAutomatico());
		opzioni_textfield_directory_download.setText(Settings.getDirectoryDownload());
		opzioni_textfield_attuale_client.setText(Settings.getClientPath());
		opzioni_textfield_minuti.setText("" + Settings.getMinRicerca());
		opzioni_box_itasa.setSelected(Settings.isRicercaSottotitoli());
		opzioni_combo_lingua.setSelectedIndex(Settings.getLingua()-1);
		opzione_box_alwaysontop.setSelected(Settings.isAlwaysOnTop());
		opzioni_text_vlc.setText(Settings.getVLCPath());
	}

	public  static JButton					sottotitoli_bottone_list				= new JButton("Aggiorna");
	private static JButton					sottotitoli_associatore					= new JButton("Associatore");
	public 	static JLabel 					sottotitoli_itasa_loggedas	 			= new JLabel("Logged as: ");
	//TODO inizializzare Textarea con log nel database
	public	static JTextArea				sottotitoli_textarea_log				= new JTextArea(5, 90);
	
	private static void creaTabItasa() {
		final JPanel scroll_sub = new JPanel(new GridLayout(5, 1));;
		
		class PanelSub extends JPanel {
			private static final long	serialVersionUID	= 1L;
			private Torrent		puntata;
			private JButton		bot_rimuovi, 
								bot_scarica, 
								bot_cerca;
			private JLabel		lab_stat;
			private PanelSub    thisp=this;

			public PanelSub(Torrent sub) {
				this.puntata = sub;
				crea();
			}

			private void crea() {
				setLayout(new BorderLayout());
				setBorder(new EtchedBorder());
				lab_stat=new JLabel();
				add(new JLabel("  " + puntata.toString()), BorderLayout.WEST);
				bot_rimuovi=new JButton(Language.INSERIMENTO_BOTTONE_RIMUOVI);
				bot_scarica=new JButton(Language.DOWNLOAD_BOTTONE_DOWNLOAD);
				
				bot_scarica.setEnabled(false);
				bot_cerca=new JButton(Language.INSERIMENTO_BOXRICERCA);
				JPanel sud=new JPanel(new BorderLayout());
				sud.add(lab_stat, BorderLayout.WEST);
				JPanel s_east=new JPanel();
				s_east.add(bot_cerca);
				s_east.add(bot_scarica);
				s_east.add(bot_rimuovi);
				sud.add(s_east, BorderLayout.EAST);
				add(sud, BorderLayout.SOUTH);
				
				bot_cerca.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						class CercaThread extends Thread{
							public void run(){
								boolean down_en=GestioneSerieTV.getSubManager().cercaSottotitolo(puntata);
								bot_scarica.setEnabled(down_en);
								if(down_en){
									lab_stat.setText("Sottotitolo trovato");
									sottotitoli_textarea_log.append(puntata.toString()+" ï¿½ disponibile"+"\n");
								}
								else
									lab_stat.setText("Sottotitolo non trovato");
							}
						}
						Thread t=new CercaThread();
						t.start();
					}
				});
				bot_rimuovi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						puntata.setSottotitolo(false, true);
						
						scroll_sub.remove(thisp);
						int size=scroll_sub.getComponentCount();
						GridLayout lay=(GridLayout) scroll_sub.getLayout();
						if(size>5)
							lay.setRows(size);
						scroll_sub.revalidate();
						scroll_sub.repaint();
					}
				});
				bot_scarica.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						class Thread_i extends Thread{
							public void run(){
								boolean res=GestioneSerieTV.getSubManager().scaricaSottotitolo(puntata);
								if(res){
									sottotitoli_textarea_log.append(puntata.toString()+" Ã¨ stato scaricato"+"\n");
									bot_rimuovi.doClick();
								}
							}
						}
						Thread_i t=new Thread_i();
						t.start();
					}
				});
			}
		}
		JPanel centro = new JPanel(new BorderLayout());
		JScrollPane scroll=new JScrollPane(scroll_sub); 
		centro.add(scroll, BorderLayout.CENTER);
		scroll.getVerticalScrollBar().setUnitIncrement(5);
		panel_sottotitoli.add(centro, BorderLayout.CENTER);
		
		JPanel centro_sud=new JPanel();
		JScrollPane scroll_log=new JScrollPane(sottotitoli_textarea_log);
		centro_sud.add(scroll_log);
		centro.add(centro_sud, BorderLayout.SOUTH);
		sottotitoli_textarea_log.setEditable(false);
		sottotitoli_textarea_log.setAutoscrolls(true);
		sottotitoli_textarea_log.setWrapStyleWord(true);
		/*
		scroll_log.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}
		});
		 */
		JPanel sud = new JPanel(new BorderLayout());
		sud.add(sottotitoli_itasa_loggedas, BorderLayout.WEST);
		
		panel_sottotitoli.add(sud, BorderLayout.SOUTH);
		panel_sottotitoli.add(centro, BorderLayout.CENTER);

		JPanel nord = new JPanel(new BorderLayout());
		JPanel nord_center = new JPanel();
		nord_center.add(sottotitoli_associatore);
		nord_center.add(sottotitoli_bottone_list);
		nord.add(nord_center, BorderLayout.CENTER);
		panel_sottotitoli.add(nord, BorderLayout.NORTH);
		
		sottotitoli_bottone_list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scroll_sub.removeAll();
				ArrayList<Torrent> el=GestioneSerieTV.getSubManager().getSottotitoliDaScaricare();
				for(int i=0;i<el.size();i++){
					scroll_sub.add(new PanelSub(el.get(i)));
				}
				GridLayout lay=(GridLayout)scroll_sub.getLayout();
				
				if(el.size()>5)
					lay.setRows(el.size());
				else
					lay.setRows(5);

				scroll_sub.revalidate();
				scroll_sub.repaint();
			}
		});
		sottotitoli_associatore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				associaFrame();
			}
		});
	}

	private static JComboBox<SerieTV> libreria_box_serie= new JComboBox<SerieTV>();
	private static JComboBox<Integer> libreria_box_stagioni=new JComboBox<Integer>();
	private static JComboBox<String>  libreria_box_ordine=new JComboBox<String>();	
	protected static JButton libreria_esplora_cartella=new JButton("Apri cartella");
	protected static JLabel libreria_label_stagioni=new JLabel("Stagione: ");
	
	private static void creaTabRefactor() {
		@SuppressWarnings("serial")
		class PanelEpisodio extends JPanel{
			private Torrent torrent;
			private JLabel stato=new JLabel();
			private JLabel nome_torrent=new JLabel();
			private JLabel tag_torrent=new JLabel();
			private JCheckBox check_scaricato=new JCheckBox();
			private JButton scarica=new JButton(),
					play=new JButton(),
					sottotitolo=new JButton(),
					cancella=new JButton();
			
			public PanelEpisodio(Torrent t){
				torrent=t;
				assembla();
				inizializza();
			}
			private void assembla(){
				setLayout(new BorderLayout());
				setBorder(new EtchedBorder(10));
				JPanel nord=new JPanel(new BorderLayout());
				JPanel centro=new JPanel();
				JPanel sud=new JPanel();
				
				JPanel nord_n=new JPanel(new BorderLayout());
				nord_n.add(check_scaricato, BorderLayout.WEST);
				nord_n.add(stato, BorderLayout.EAST);
				nord.add(nord_n, BorderLayout.NORTH);
				JPanel nord_s=new JPanel(new BorderLayout());
				nord_s.add(nome_torrent, BorderLayout.WEST);
				nord_s.add(tag_torrent, BorderLayout.EAST);
				nord.add(nord_s, BorderLayout.SOUTH);
				
				sud.add(scarica);
				sud.add(play);
				sud.add(sottotitolo);
				sud.add(cancella);
				sud.setBorder(BorderFactory.createLoweredBevelBorder());
				sud.setBackground(Color.ORANGE);
				
				add(nord, BorderLayout.NORTH);
				add(centro, BorderLayout.CENTER);
				add(sud, BorderLayout.SOUTH);
			}
			private void inizializza(){
				stato.setFont(new Font("Tahoma", Font.BOLD, 12));
				setLabelStato(torrent.getScaricato());
				check_scaricato.setText(torrent.getNomeSerie()+" "+torrent.getSerie()+"x"+torrent.getPuntata());
				check_scaricato.setSelected(torrent.isScaricato());
				nome_torrent.setText(Language.REFACTOR_LABEL_NOMETORRENT+torrent.getName());
				scarica.setText("Scarica");
				scarica.setIcon(Resource.getIcona("res/salva.png"));
				play.setText("Play");
				play.setIcon(Resource.getIcona("res/vlc.png"));
				sottotitolo.setText("Sottotitolo");
				sottotitolo.setIcon(Resource.getIcona("res/sub16.png"));
				cancella.setText("Rimuovi");
				cancella.setIcon(Resource.getIcona("res/remove.png"));
				String lista_tag="";
				if(torrent.isPreAir())
					lista_tag+="PREAIR ";
				if(torrent.is720p())
					lista_tag+="720p ";
				if(torrent.isRepack())
					lista_tag+="REPACK ";
				if(torrent.isPROPER())
					lista_tag+="PROPER ";
				tag_torrent.setText(lista_tag);
				addListener();
			}
			private void setLabelStato(int stato){
				switch(stato){
					case Torrent.SCARICARE:
						this.stato.setText(Language.REFACTOR_LABEL_TODOWNLOAD);
						break;
					case Torrent.SCARICATO:
						this.stato.setText(Language.REFACTOR_LABEL_DOWNLOADED);
						break;
					case Torrent.IGNORATO:
						this.stato.setText("Ignorato");
						break;
					case Torrent.RIMOSSO:
						this.stato.setText("Rimosso");
						break;
					case Torrent.VISTO:
						this.stato.setText("Visto");
						break;
				}
			}
			private void addListener(){
				check_scaricato.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						torrent.setScaricato(check_scaricato.isSelected()?Torrent.SCARICATO:Torrent.SCARICARE, true);
						setLabelStato(torrent.getScaricato());
						RidisegnaScrollPanel();
					}
				});
				scarica.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							Download.downloadMagnet(torrent.getUrl(), torrent.getNomeSerieFolder());
							check_scaricato.setSelected(true);
							torrent.setScaricato(Torrent.SCARICATO);
							check_scaricato.getActionListeners()[0].actionPerformed(new ActionEvent(check_scaricato, 0, "", 0));
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				});
				play.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SerieTV st=((SerieTV)libreria_box_serie.getSelectedItem());
						String nomepuntata;
						try {
							nomepuntata = OperazioniFile.cercavideofile(torrent);
						}
						catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(frame, "File video non trovato");
							return; 
						}					
						//TODO verifica presenza sottotitolo
						//else if TODO download del sottotitolo
						//TODO mostra warning se non si ï¿½ potuto scaricare
						
						Player.play(st.getNomeSerieFolder()+File.separator+nomepuntata);
						torrent.setScaricato(Torrent.VISTO, true);
						setLabelStato(torrent.getScaricato());
					}
				});
				sottotitolo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!GestioneSerieTV.getSubManager().scaricaSottotitolo(torrent)){
							torrent.setSottotitolo(true, true);
							JOptionPane.showMessageDialog(frame, "Sottotitolo aggiunto in coda");
						}
						else
							JOptionPane.showMessageDialog(frame, "Sottotitolo scaricato");
					}
				});
				cancella.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							String nome=OperazioniFile.cercavideofile(torrent);
							String nome_files=nome.substring(0,nome.lastIndexOf("."));
							File dir=new File(Settings.getDirectoryDownload()+torrent.getNomeSerieFolder());
							if(dir.exists()){
								if(dir.isDirectory()){
									String[] files=dir.list();
									//System.out.println("Nome file da cancellare: "+nome_files);
									for(int i=0;i<files.length;i++){
										//System.out.println("Cancellando: "+files[i]);
										if(files[i].startsWith(nome_files)){
											if(OperazioniFile.deleteFile(Settings.getDirectoryDownload()+torrent.getNomeSerieFolder()+File.separator+files[i]))
												torrent.setScaricato(Torrent.RIMOSSO, true);
										}
									}
								}
							}
						}
						catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(frame, "Il file non era presente.\nSi imposterà lo stato di RIMOSSO.");
							torrent.setScaricato(Torrent.RIMOSSO, true);
						}
						setLabelStato(torrent.getScaricato());
					}
				});
			}
		}
		final JPanel libreria_panel_scrollable=new JPanel(new GridLayout(8,1));
		JPanel nord=new JPanel(new BorderLayout());
		JScrollPane panel_scroll=new JScrollPane(libreria_panel_scrollable);
		panel_scroll.getVerticalScrollBar().setUnitIncrement(15);
		
		libreria_box_ordine.addItem("1-x");
		libreria_box_ordine.addItem("x-1");
		
		nord.add(libreria_box_serie, BorderLayout.NORTH);
		JPanel nord_s=new JPanel(new BorderLayout());
		JPanel n_s=new JPanel();
		n_s.add(libreria_esplora_cartella);
		JPanel n_w=new JPanel();
		n_w.add(libreria_label_stagioni);
		n_w.add(libreria_box_stagioni);
		n_w.add(libreria_box_ordine);
		nord_s.add(n_s, BorderLayout.SOUTH);
		nord_s.add(n_w, BorderLayout.WEST);
		nord.add(nord_s, BorderLayout.SOUTH);
		
		JPanel sud=new JPanel(new BorderLayout());
		//sud.add(new JLabel("ATTENZIONE! Alcune funzioni, come il tasto Play potrebbero essere ancora incomplete"), BorderLayout.WEST);
		panel_refactor.add(sud, BorderLayout.SOUTH);
		
		panel_refactor.add(nord, BorderLayout.NORTH);
		panel_refactor.add(panel_scroll, BorderLayout.CENTER);
		
		libreria_box_serie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(libreria_box_serie.getSelectedItem()==null)
					return;
				
				SerieTV st = (SerieTV) libreria_box_serie.getSelectedItem();
				ElencoIndicizzato scaricati=st.getEpisodi();
				int dim=0;
				int last_item_count=libreria_box_stagioni.getItemCount();
				Stack<Integer> serie_sel=new Stack<Integer>();
				while(dim<scaricati.n_index()){
					ArrayList<Indexable> list=scaricati.get(dim);
					if(list.size()>0)
						serie_sel.push(dim);
					dim++;
				}
				while(!serie_sel.isEmpty())
					libreria_box_stagioni.addItem(serie_sel.pop());
				while(last_item_count>0){
					libreria_box_stagioni.removeItemAt(0);
					last_item_count--;
				}
				frame.revalidate();
				frame.repaint();
			}
		});
		libreria_box_stagioni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				libreria_box_ordine.getActionListeners()[0].actionPerformed(new ActionEvent(libreria_box_ordine, 0, ""));
			}
		});
		libreria_box_ordine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int stagione=(Integer)libreria_box_stagioni.getSelectedItem();
				
				SerieTV st = (SerieTV) libreria_box_serie.getSelectedItem();
				ElencoIndicizzato scaricati=st.getEpisodi();
				ArrayList<Indexable> l_torrent=scaricati.get(stagione);
				
				libreria_panel_scrollable.removeAll();
				GridLayout lay = (GridLayout) libreria_panel_scrollable.getLayout();
				lay.setColumns(1);
				lay.setRows(l_torrent.size() > 4 ? l_torrent.size()	: 4);
				
				int ordine=libreria_box_ordine.getSelectedIndex();
				if(ordine==0){
					for(int i=0;i<l_torrent.size();i++){
						Torrent t=(Torrent) l_torrent.get(i);
						libreria_panel_scrollable.add(new PanelEpisodio(t));
					}
				}
				else{
					for(int i=l_torrent.size()-1;i>=0;i--){
						Torrent t=(Torrent) l_torrent.get(i);
						libreria_panel_scrollable.add(new PanelEpisodio(t));
					}
				}
				frame.revalidate();
				frame.repaint();
			}
		});
		libreria_esplora_cartella.setIcon(Resource.getIcona("res/cartella.png"));
		libreria_esplora_cartella.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String folder=Settings.getDirectoryDownload()+File.separator+((SerieTV)libreria_box_serie.getSelectedItem()).getNomeSerieFolder();
				try {
					OperazioniFile.esploraCartella(folder);
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(frame, e.getMessage());
				}
			}
		});
		libreria_addItemBoxSerie();
	}
	
	public static void libreria_addItemBoxSerie() {
		libreria_box_serie.removeAllItems();
		if (GestioneSerieTV.getElencoSerieInserite() == null)
			return;
		for (int i = 0; i < GestioneSerieTV.getElencoSerieInserite().size(); i++) {
			libreria_box_serie.addItem(GestioneSerieTV.getElencoSerieInserite().get(i));
		}
		libreria_box_serie.setEnabled(libreria_box_serie.getItemCount() > 0);
		panel_refactor.revalidate();
		panel_refactor.repaint();
	}
	
	public static void removeTray() {
		TrayIcon[] ic = tray.getTrayIcons();
		if (ic.length > 0)
			tray.remove(ic[0]);
		frame.setExtendedState(JFrame.NORMAL);
		frame.setVisible(true);
		isHidden = false;
		frame.setLocation(0, 0);
	}

	public static void setTray() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(Resource.createImage("res/icona16x16.png", Language.TITLE), Language.TITLE);

		tray = SystemTray.getSystemTray();
		MenuItem restoreWin = new MenuItem(Language.TRAYICON_RIPRISTINA);
		MenuItem exitItem = new MenuItem(Language.TRAYICON_ESCI);

		popup.add(restoreWin);
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);
		try {
			tray.add(trayIcon);
			frame.setVisible(false);

			isHidden = true;
		}
		catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				frame.setExtendedState(JFrame.NORMAL);
			}
		});
		restoreWin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(true);
				frame.setExtendedState(JFrame.NORMAL);
			}
		});
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}
	
	private static JFrame frame_donazione;
	private static JWebBrowser browser_donazione;
	private static JButton donazione_bottone_chiudi;
	private static JLabel donazione_testo_1, donazione_testo_2;
	private static JSpinner donazione_amount;
	private static String html_donazione="<form action=\"https://www.paypal.com/cgi-bin/webscr\" method=\"post\">"
										+"<input type=\"hidden\" name=\"cmd\" value=\"_xclick\" />"
										+"<input type=\"hidden\" name=\"business\" value=\"pino.elefante@hotmail.it\" />"
										+"<input type=\"hidden\" name=\"item_name\" value=\"GST\" />"
										+"<input type=\"hidden\" name=\"item_number\" value=\"GST\" />"
										+"<input type=\"hidden\" name=\"amount\" value=\"5.00\" />"
										+"<input type=\"hidden\" name=\"lc\" value=\"IT\" />"
										+"<input type=\"image\" src=\"https://www.paypal.com/it_IT/i/btn/x-click-but21.gif\" border=\"0\" name=\"submit\" alt=\"Dona con PayPal - il modo sicuro per pagare e farsi pagare online\" />"
										+"<img alt=\"\" border=\"0\" src=\"https://www.paypal.com/it_IT/i/scr/pixel.gif\" width=\"1\" height=\"1\" />"
										+"<input type=\"hidden\" name=\"no_shipping\" value=\"2\" />"
										+"<input type=\"hidden\" name=\"no_note\" value=\"1\" />"
										+"<input type=\"hidden\" name=\"currency_code\" value=\"EUR\" />"
										+"<input type=\"hidden\" name=\"tax\" value=\"0\" />"
										+"<input type=\"hidden\" name=\"bn\" value=\"IC_esempio\" />"
										+"</form>";
	
	public static void donazione_visualizza_frame(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(frame_donazione==null){
					NativeInterface.open();
					browser_donazione=new JWebBrowser(JWebBrowser.destroyOnFinalization());
					browser_donazione.setBarsVisible(false);
					browser_donazione.setStatusBarVisible(false);
					browser_donazione.setHTMLContent(html_donazione);
					
					browser_donazione.addWebBrowserListener(new WebBrowserListener() {
						public void windowWillOpen(WebBrowserWindowWillOpenEvent arg0) {}
						public void windowOpening(WebBrowserWindowOpeningEvent arg0) {}
						public void windowClosing(WebBrowserEvent arg0) {}
						public void titleChanged(WebBrowserEvent arg0) {}
						public void statusChanged(WebBrowserEvent arg0) {}
						public void locationChanging(WebBrowserNavigationEvent arg0) {
							if(browser_donazione.getResourceLocation().contains("www.paypal.com")){
								frame_donazione.setLocation(0, 0);
								frame_donazione.setSize(1050, 768);
							}
							else{
								frame_donazione.setSize(300, 130);
							}
						}
						public void locationChanged(WebBrowserNavigationEvent arg0) {}
						public void locationChangeCanceled(WebBrowserNavigationEvent arg0) {}
						public void loadingProgressChanged(WebBrowserEvent arg0) {}
						public void commandReceived(WebBrowserCommandEvent arg0) {}
					});
					
					frame_donazione=new JFrame("Donazione");
					donazione_amount=new JSpinner();
					donazione_amount.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent arg0) {
							html_donazione="<form action=\"https://www.paypal.com/cgi-bin/webscr\" method=\"post\">"
									+"<input type=\"hidden\" name=\"cmd\" value=\"_xclick\" />"
									+"<input type=\"hidden\" name=\"business\" value=\"pino.elefante@hotmail.it\" />"
									+"<input type=\"hidden\" name=\"item_name\" value=\"GST\" />"
									+"<input type=\"hidden\" name=\"item_number\" value=\"GST\" />"
									+"<input type=\"hidden\" name=\"amount\" value=\""+(Integer)donazione_amount.getValue()+".00\" />"
									+"<input type=\"hidden\" name=\"lc\" value=\"IT\" />"
									+"<input type=\"image\" src=\"https://www.paypal.com/it_IT/i/btn/x-click-but21.gif\" border=\"0\" name=\"submit\" alt=\"Dona con PayPal - il modo sicuro per pagare e farsi pagare online\" />"
									+"<img alt=\"\" border=\"0\" src=\"https://www.paypal.com/it_IT/i/scr/pixel.gif\" width=\"1\" height=\"1\" />"
									+"<input type=\"hidden\" name=\"no_shipping\" value=\"2\" />"
									+"<input type=\"hidden\" name=\"no_note\" value=\"1\" />"
									+"<input type=\"hidden\" name=\"currency_code\" value=\"EUR\" />"
									+"<input type=\"hidden\" name=\"tax\" value=\"0\" />"
									+"<input type=\"hidden\" name=\"bn\" value=\"IC_esempio\" />"
									+"</form>";
							browser_donazione.navigate("about:blank");
							browser_donazione.setHTMLContent(html_donazione);
							frame_donazione.revalidate();
							frame_donazione.repaint();
						}
					});
					donazione_amount.setModel(new SpinnerNumberModel(5, 1, 9999, 1));
					
					donazione_bottone_chiudi=new JButton("No, grazie");
					donazione_bottone_chiudi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							frame_donazione.dispose();
						}
					});
					donazione_testo_1=new JLabel("Hai usato questo software "+Settings.getNumeroAvvii()+" volte.");
					donazione_testo_2=new JLabel("Effettua una donazione per supportarne lo sviluppo.");
					
					//frame_donazione.setResizable(false);
					frame_donazione.setAlwaysOnTop(true);
					frame_donazione.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame_donazione.setLayout(new BorderLayout());
					
					frame_donazione.add(browser_donazione, BorderLayout.CENTER);
					
					JPanel nord=new JPanel(new BorderLayout());
					nord.add(donazione_testo_1, BorderLayout.NORTH);
					nord.add(donazione_testo_2, BorderLayout.CENTER);
					frame_donazione.add(nord, BorderLayout.NORTH);
					
					JPanel west=new JPanel();
					west.add(new JLabel("ï¿½"));
					west.add(donazione_amount);
					frame_donazione.add(west, BorderLayout.WEST);
					
					JPanel south=new JPanel();
					south.add(donazione_bottone_chiudi);
					frame_donazione.add(south, BorderLayout.SOUTH);
				}
				else{
					browser_donazione.setHTMLContent(html_donazione);
				}
				
				Point p=frame.getLocation();
				frame_donazione.setLocation(p.x+frame.getWidth()/2, p.y+frame.getHeight()/2);
				frame_donazione.setVisible(true);
				frame_donazione.setSize(300, 130);
			}
		});
	}
	
	//TODO modificare per piï¿½ gestori, come subsfactory
	public static JFrame frame_associa_itasa;
	private static JComboBox<SerieSub> associa_itasa; 
	private static JComboBox<SerieTV> associa_serie;
	private static JLabel associa_testo, associa_stato;
	private static JButton associa_aggiorna, associa_associa, associa_rimuovi;
	public static void associaFrame(){
		if(frame_associa_itasa==null){
			frame_associa_itasa=new JFrame();
			frame_associa_itasa.setAlwaysOnTop(true);
			frame_associa_itasa.setResizable(false);
			frame_associa_itasa.setLayout(new BorderLayout());
			frame_associa_itasa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame_associa_itasa.setSize(600, 200);
			
			associa_itasa=new JComboBox<SerieSub>();
			
			associa_serie=new JComboBox<SerieTV>();
			
			associa_testo=new JLabel("Associata a: ");
			associa_stato=new JLabel("");
			
			associa_aggiorna=new JButton("Aggiorna elenchi");
			associa_aggiorna.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					ArrayList<SerieTV> serie=GestioneSerieTV.getElencoSerieInserite();
					associa_serie.removeAllItems();
					for(int i=0;i<serie.size();i++)
						associa_serie.addItem(serie.get(i));
					
					GestioneSerieTV.getSubManager();
					ArrayList<SerieSub> itasa=GestioneSerieTV.getSubManager().getElencoSerie(GestoreSottotitoli.ITASA);
					associa_itasa.removeAllItems();
					for(int i=0;i<itasa.size();i++)
						associa_itasa.addItem(itasa.get(i));
				}
			});
			associa_serie.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					SerieTV st=(SerieTV)associa_serie.getSelectedItem();
					if(st==null)
						return;
					int id=st.getItasaID();
					if(id!=-1){
						GestioneSerieTV.getSubManager();
						ArrayList<SerieSub> itasa=GestioneSerieTV.getSubManager().getElencoSerie(GestoreSottotitoli.ITASA);
						String nome="";
						for(int i=0;i<itasa.size();i++)
							if((int)itasa.get(i).getID()==id)
								nome=itasa.get(i).getNomeSerie();
						associa_stato.setText(nome+" ("+id+")");
					}
					else
						associa_stato.setText(" non associato.");
				}
			});
			
			associa_associa=new JButton("Associa");
			associa_associa.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SerieTV st=(SerieTV)associa_serie.getSelectedItem();
					if(st==null)
						return;
					SerieSub itasa=(SerieSub)associa_itasa.getSelectedItem();
					if(itasa==null)
						return;
					st.setItasaID((int)itasa.getID());
					st.UpdateDB();
					associa_stato.setText(itasa.getNomeSerie()+" ("+st.getItasaID()+")");
				}
			});
			associa_rimuovi=new JButton("Rimuovi");
			associa_rimuovi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					SerieTV st=(SerieTV)associa_serie.getSelectedItem();
					if(st==null)
						return;
					st.setItasaID(-1);
					associa_stato.setText(" non associato.");
				}
			});
			
			JPanel nord=new JPanel();
			nord.add(associa_aggiorna);
			
			JPanel centro=new JPanel(new BorderLayout());
			JPanel centro_w=new JPanel();
			JPanel centro_e=new JPanel();
			JPanel centro_s=new JPanel();
			centro_w.add(associa_serie);
			centro_e.add(associa_itasa);
			centro_s.add(associa_associa);
			centro_s.add(associa_rimuovi);
			centro.add(centro_s, BorderLayout.SOUTH);
			centro.add(centro_e, BorderLayout.EAST);
			centro.add(centro_w, BorderLayout.WEST);
			
			JPanel sud=new JPanel();
			sud.add(associa_testo);
			sud.add(associa_stato);
			
			frame_associa_itasa.add(nord, BorderLayout.NORTH);
			frame_associa_itasa.add(sud, BorderLayout.SOUTH);
			frame_associa_itasa.add(centro, BorderLayout.CENTER);
		}
		frame_associa_itasa.setVisible(true);
		Point p;
		if(frame!=null)
			p=frame.getLocation();
		else
			p=Programma.Main.fl.getFrame().getLocation();
		frame_associa_itasa.setLocation((int)p.getX(), (int)p.getY());
		
		associa_aggiorna.doClick();
	}
	
	protected static JLabel		about_label_donazione		= new JLabel(Language.ABOUT_DONATION);
	protected static JLabel 	about_label_stato			= new JLabel(Language.TITLE);
	protected static JLabel 	indirizzo					= new JLabel(Language.ABOUT_SITO);
	protected static JLabel		about_email					= new JLabel(Language.ABOUT_MAIL);
	protected static JButton 	about_bottone_update		= new JButton(Language.OPZIONI_AGGIORNAMENTI);

	
	private static void creaTabAbout(){
		JPanel sud=new JPanel(new BorderLayout());
		sud.add(about_label_donazione, BorderLayout.EAST);
		JPanel p_upd=new JPanel();
		p_upd.add(about_bottone_update);
		sud.add(p_upd, BorderLayout.CENTER);
		sud.add(about_email, BorderLayout.WEST);
		panel_about.add(sud, BorderLayout.SOUTH);
		
		JPanel nord=new JPanel(new BorderLayout());
		JLabel image=new JLabel();
		image.setIcon(Resource.getIcona("res/logo.png"));
		JPanel imgp=new JPanel();
		imgp.add(image);
		nord.add(imgp, BorderLayout.NORTH);
		JPanel p_nord_c=new JPanel();
		p_nord_c.add(about_label_stato);
		nord.add(p_nord_c, BorderLayout.CENTER);
		JPanel p_nord_s=new JPanel();
		p_nord_s.add(indirizzo);
		nord.add(p_nord_s, BorderLayout.SOUTH);
		panel_about.add(nord, BorderLayout.NORTH);
		
		about_label_donazione.setFont(new Font("Tahoma", Font.BOLD, 14));
		about_label_donazione.setForeground(Color.BLUE);
		about_label_donazione.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {
				donazione_visualizza_frame();
			}
			public void mouseExited(MouseEvent arg0) {
				about_label_donazione.setText(Language.ABOUT_DONATION);
			}
			public void mouseEntered(MouseEvent arg0) {
				about_label_donazione.setText(Language.ABOUT_ON_DONATION);
			}
			public void mouseClicked(MouseEvent arg0) {}
		});
		indirizzo.setFont(new Font("Tahoma", Font.BOLD+Font.ITALIC, 12));
		indirizzo.setForeground(Color.BLUE);
		indirizzo.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {	
				OperazioniFile.esploraWeb("http://pinoelefante.altervista.org");
			}
			public void mouseExited(MouseEvent arg0) {
				indirizzo.setText(Language.ABOUT_SITO);
			}
			public void mouseEntered(MouseEvent arg0) {
				indirizzo.setText(Language.ABOUT_ON_SITO);
			}
			public void mouseClicked(MouseEvent arg0) {}
		});
		about_email.setFont(new Font("Tahoma", Font.BOLD, 14));
		about_email.setForeground(Color.BLUE);
		about_email.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {
				OperazioniFile.email("mailto:gestioneserietv@gmail.com?subject=GestioneSerieTV_"+Settings.getVersioneSoftware());
			}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {}
		});
		about_bottone_update.setIcon(Resource.getIcona("res/update.png"));
		about_bottone_update.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ThreadControlloAggiornamento t1 = new ThreadControlloAggiornamento(true);
				t1.start();
			}
		});
	}

	public static void ShowFrameOpzioni() {
		//TODO creare Wizard opzioni
		
	}

	public static JFrame getFrameOpzioni() {
		// TODO get frame opzioni
		return null;
	}
	
}
