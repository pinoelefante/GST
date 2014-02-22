package GUI;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EtchedBorder;

import Manutenzione.Manutenzione;
import Database.Database;
import Programma.Settings;

public class InterfacciaManutenzione extends JFrame{
	public static void main(String[] args){
		Settings.baseSettings();
		Settings.CaricaSetting();
		JFrame gui=new InterfacciaManutenzione();
		gui.setVisible(true);
	}
	private static final long serialVersionUID = 1L;
	private JButton btnEsportaDatabase;
	private JButton btnImportaDaSql;
	private JButton btnImportaDaDatabase;
	private JButton btnCancellaTutto;

	public InterfacciaManutenzione(){
		super("Gestione Serie TV - Manutenzione");
		setAlwaysOnTop(true);
		setSize(500, 350);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblEsportaDatabaseIn = new JLabel("Esporta database in file SQL");
		lblEsportaDatabaseIn.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblEsportaDatabaseIn, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		
		btnEsportaDatabase = new JButton("Esporta Database");
		panel_1.add(btnEsportaDatabase);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblImportaDatabase = new JLabel("Importa Database");
		lblImportaDatabase.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblImportaDatabase, BorderLayout.NORTH);
		
		JLabel lblAttenzioneVerrannoCancellati = new JLabel("  ATTENZIONE: verranno cancellati i dati esistenti");
		lblAttenzioneVerrannoCancellati.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(lblAttenzioneVerrannoCancellati, BorderLayout.SOUTH);
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.CENTER);
		
		btnImportaDaSql = new JButton("Importa da SQL");
		panel_3.add(btnImportaDaSql);
		
		btnImportaDaDatabase = new JButton("Importa da database SQLite");
		panel_3.add(btnImportaDaDatabase);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCancellaContenuto = new JLabel("Cancella contenuto");
		lblCancellaContenuto.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblCancellaContenuto, BorderLayout.NORTH);
		
		JLabel lblAttenzioneSiConsiglia = new JLabel("  ATTENZIONE: si consiglia di effettuare un backup prima di procedere");
		lblAttenzioneSiConsiglia.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_4.add(lblAttenzioneSiConsiglia, BorderLayout.SOUTH);
		
		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.CENTER);
		
		btnCancellaTutto = new JButton("Cancella tutto");
		panel_5.add(btnCancellaTutto);
		
		addListener();
	}

	private void addListener() {
		btnEsportaDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Manutenzione.esportaDBinSQL(Database.Connect(), Settings.getUserDir())){
					JOptionPane.showMessageDialog(InterfacciaManutenzione.this, "Esportazione effettuata.\nFile: "+Settings.getUserDir()+"gst_db_backup.sql");
				}
				else {
					JOptionPane.showMessageDialog(InterfacciaManutenzione.this, "Si � verificato un errore durante il salvataggio");
				}
			}
		});
		btnImportaDaSql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Manutenzione.importaDBdaSQL(Database.Connect(), Settings.getUserDir()+"gst_db_backup.sql")){
					JOptionPane.showMessageDialog(InterfacciaManutenzione.this, "Importazione completata con successo");
				}
				else {
					JOptionPane.showMessageDialog(InterfacciaManutenzione.this, "Importazione completata con degli errori");
				}
			}
		});
		btnImportaDaDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Manutenzione.importaDBdaSQLite("C:\\Documents and Settings\\Pino\\Documenti\\database2.sqlite", Database.Connect());
				
			}
		});
		btnCancellaTutto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Manutenzione.truncateAll(Database.Connect());
			}
		});
	}
}
