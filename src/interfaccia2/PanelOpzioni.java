package interfaccia2;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;

import Programma.Settings;

import java.awt.BorderLayout;

public class PanelOpzioni extends JPanel implements GSTPanel{
	private static final long serialVersionUID = 1L;
	
	private JTextField textField_utorrent;
	private JTextField textField_playervideo;
	private JTextField textField_cartella_download;
	private JTextField textField_minuti_download_automatico;
	private JTextField textField_itasa_username;
	private JPasswordField textField_itasa_password;
	private JCheckBox chckbxAvvioAutomatico;
	private JCheckBox chckbxAvviaRidottoIcona;
	private JCheckBox chckbxSempreInPrimoPiano;
	private JCheckBox chckbxAskExit;
	private JCheckBox chckbxNascondiDuranteLaRiproduzione;
	private JCheckBox chckbxAbilitaDownloadAutomatico;
	private JCheckBox chckbxScaricaPreair;
	private JCheckBox chckbxScarica720p;
	private JCheckBox chckbxAbilitaDownloadSottotitoli;

	/**
	 * Create the panel.
	 */
	public PanelOpzioni() {
		setBorder(new TitledBorder(null, "Opzioni", TitledBorder.RIGHT, TitledBorder.ABOVE_TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Aspetto", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		add(panel);
		panel.setLayout(new GridLayout(6, 1, 0, 0));
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		
		JLabel lblLingua = new JLabel("Lingua: ");
		panel_3.add(lblLingua);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		panel_3.add(comboBox);
		
		chckbxAvvioAutomatico = new JCheckBox("Avvia con il sistema operativo");
		panel.add(chckbxAvvioAutomatico);
		
		chckbxAvviaRidottoIcona = new JCheckBox("Avvia ridotto a icona");
		panel.add(chckbxAvviaRidottoIcona);
		
		chckbxSempreInPrimoPiano = new JCheckBox("Sempre in primo piano");
		panel.add(chckbxSempreInPrimoPiano);
		
		chckbxAskExit = new JCheckBox("Chiedi conferma prima di uscire");
		panel.add(chckbxAskExit);
		
		chckbxNascondiDuranteLaRiproduzione = new JCheckBox("Nascondi durante la visione di un video");
		panel.add(chckbxNascondiDuranteLaRiproduzione);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Programmi", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		
		JLabel lblPercorsoUtorrent = new JLabel("Percorso uTorrent: ");
		panel_4.add(lblPercorsoUtorrent);
		
		textField_utorrent = new JTextField();
		textField_utorrent.setEditable(false);
		panel_4.add(textField_utorrent);
		textField_utorrent.setColumns(30);
		
		JButton btnSfoglia = new JButton("Sfoglia");
		panel_4.add(btnSfoglia);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		
		JLabel lblPercorsoPlayer = new JLabel("    Percorso player: ");
		panel_5.add(lblPercorsoPlayer);
		
		textField_playervideo = new JTextField();
		textField_playervideo.setEditable(false);
		panel_5.add(textField_playervideo);
		textField_playervideo.setColumns(30);
		
		JButton btnSfoglia_1 = new JButton("Sfoglia");
		panel_5.add(btnSfoglia_1);
		
		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6);
		
		JLabel lblCartellaSalvataggio = new JLabel("Cartella download: ");
		panel_6.add(lblCartellaSalvataggio);
		
		textField_cartella_download = new JTextField();
		textField_cartella_download.setEditable(false);
		panel_6.add(textField_cartella_download);
		textField_cartella_download.setColumns(30);
		
		JButton btnSfoglia_2 = new JButton("Sfoglia");
		panel_6.add(btnSfoglia_2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Download", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		add(panel_2);
		panel_2.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel_8 = new JPanel();
		panel_2.add(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		chckbxAbilitaDownloadAutomatico = new JCheckBox("Abilita download automatico");
		panel_8.add(chckbxAbilitaDownloadAutomatico, BorderLayout.WEST);
		
		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.EAST);
		
		textField_minuti_download_automatico = new JTextField();
		panel_9.add(textField_minuti_download_automatico);
		textField_minuti_download_automatico.setColumns(5);
		
		JLabel lblMinutiTraOgni = new JLabel("minuti tra ogni ricerca");
		panel_9.add(lblMinutiTraOgni);
		
		chckbxScaricaPreair = new JCheckBox("Scarica preair");
		panel_2.add(chckbxScaricaPreair);
		
		chckbxScarica720p = new JCheckBox("Scarica 720p");
		panel_2.add(chckbxScarica720p);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "Sottotitoli", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		add(panel_7);
		panel_7.setLayout(new GridLayout(3, 1, 0, 0));
		
		chckbxAbilitaDownloadSottotitoli = new JCheckBox("Abilita download sottotitoli");
		panel_7.add(chckbxAbilitaDownloadSottotitoli);
		
		JPanel panel_10 = new JPanel();
		panel_7.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));
		
		JLabel lblUsernameItaliansubsnet = new JLabel("Username Italiansubs.net: ");
		panel_10.add(lblUsernameItaliansubsnet, BorderLayout.WEST);
		
		JPanel panel_12 = new JPanel();
		panel_10.add(panel_12, BorderLayout.CENTER);
		
		textField_itasa_username = new JTextField();
		panel_12.add(textField_itasa_username);
		textField_itasa_username.setColumns(30);
		
		JPanel panel_11 = new JPanel();
		panel_7.add(panel_11);
		panel_11.setLayout(new BorderLayout(0, 0));
		
		JLabel lblPasswordItaliansubsnet = new JLabel(" Password Italiansubs.net: ");
		panel_11.add(lblPasswordItaliansubsnet, BorderLayout.WEST);
		
		JPanel panel_13 = new JPanel();
		panel_11.add(panel_13, BorderLayout.CENTER);
		
		textField_itasa_password = new JPasswordField();
		panel_13.add(textField_itasa_password);
		textField_itasa_password.setColumns(30);
		
		JPanel panel_14 = new JPanel();
		add(panel_14);
		
		JButton btnSalva = new JButton("Salva");
		panel_14.add(btnSalva);
		
		JButton btnPredefiniti = new JButton("Predefiniti");
		panel_14.add(btnPredefiniti);
		
	}

	@Override
	public void caricaDefault() {
		textField_utorrent.setText(Settings.getClientPath());
		textField_playervideo.setText(Settings.getVLCPath());
		textField_cartella_download.setText(Settings.getDirectoryDownload());
		textField_minuti_download_automatico.setText(Settings.getMinRicerca()+"");
		textField_itasa_username.setText(Settings.getItasaUsername());
		textField_itasa_password.setText("");
		chckbxAvvioAutomatico.setSelected(Settings.isAutostart());
		chckbxAvviaRidottoIcona.setSelected(Settings.isStartHidden());
		chckbxSempreInPrimoPiano.setSelected(Settings.isAlwaysOnTop());
		chckbxAskExit.setSelected(Settings.isAskOnClose());
		chckbxNascondiDuranteLaRiproduzione.setSelected(Settings.isHiddenOnPlay());
		chckbxAbilitaDownloadAutomatico.setSelected(Settings.isDownloadAutomatico());
		chckbxScaricaPreair.setSelected(Settings.isDownloadPreair());
		chckbxScarica720p.setSelected(Settings.isDownload720p());
		chckbxAbilitaDownloadSottotitoli.setSelected(Settings.isRicercaSottotitoli());
	}

	@Override
	public void traduci() {
		// TODO Auto-generated method stub
		
	}

}
