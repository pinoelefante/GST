package interfaccia2;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JInternalFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

public class PanelPrincipale extends JPanel implements GSTPanel{
	private static final long serialVersionUID = 1L;
	private JTextField textFieldRicercaSerie;
	private JComboBox<SerieTV> comboBoxSerieDisponibili;

	/**
	 * Create the panel.
	 */
	public PanelPrincipale() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		JLabel lblSerie = new JLabel("Serie");
		panel.add(lblSerie);
		
		textFieldRicercaSerie = new JTextField();
		panel.add(textFieldRicercaSerie);
		textFieldRicercaSerie.setColumns(10);
		
		comboBoxSerieDisponibili = new JComboBox<SerieTV>();
		panel.add(comboBoxSerieDisponibili);
		
		JLabel lblStatoSerie = new JLabel("stato serie (rimuovere testo)");
		panel.add(lblStatoSerie);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		
		JProgressBar progressBar = new JProgressBar();
		panel_1.add(progressBar);
		
		JLabel lblDownloadStatus = new JLabel("Download Status");
		panel_1.add(lblDownloadStatus);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.WEST);
		
		JPanel panel_2 = new JPanel();
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCopertinaserie = new JLabel("COPERTINASERIE");
		panel_2.add(lblCopertinaserie, BorderLayout.NORTH);
		
		JPanel panel_7 = new JPanel();
		panel_2.add(panel_7, BorderLayout.CENTER);
		panel_7.setLayout(new GridLayout(5, 1, 0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_7.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		
		JLabel lblNomeSerie = new JLabel("Nome serie:");
		panel_4.add(lblNomeSerie);
		
		JLabel lblNomeserie = new JLabel("nome_serie");
		panel_4.add(lblNomeserie);
		
		JPanel panel_5 = new JPanel();
		panel_7.add(panel_5);
		
		JPanel panel_6 = new JPanel();
		panel_5.add(panel_6);
		
		JLabel lblGenere = new JLabel("Genere: ");
		panel_6.add(lblGenere);
		
		JLabel lblNomegenere = new JLabel("nome_genere");
		panel_6.add(lblNomegenere);
		
		JPanel panel_8 = new JPanel();
		panel_7.add(panel_8);
		
		JLabel lblProssima = new JLabel("Prossima: ");
		panel_8.add(lblProssima);
		
		JLabel lblDataprossimoepisodio = new JLabel("data_prossimo_episodio");
		panel_8.add(lblDataprossimoepisodio);
		
		JPanel panel_9 = new JPanel();
		add(panel_9, BorderLayout.CENTER);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_bottoni_selezione = new JPanel();
		panel_9.add(panel_bottoni_selezione, BorderLayout.NORTH);
		
		JButton btnSelezionaTutti = new JButton("Seleziona tutti");
		panel_bottoni_selezione.add(btnSelezionaTutti);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_9.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_episodi_download = new JPanel();
		tabbedPane.addTab("Episodi", null, panel_episodi_download, null);
		panel_episodi_download.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_trama = new JPanel();
		JScrollPane scrollPane_trama = new JScrollPane(panel_trama);
		panel_trama.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTitoloEpisodio = new JLabel("Titolo episodio");
		panel_trama.add(lblTitoloEpisodio, BorderLayout.NORTH);
		
		JLabel lblTestoTrama = new JLabel("Testo trama");
		panel_trama.add(lblTestoTrama, BorderLayout.SOUTH);
		panel_episodi_download.add(scrollPane_trama, BorderLayout.SOUTH);
		
		
		
		JPanel panel_sottotitoli = new JPanel();
		tabbedPane.addTab("Sottotitoli", null, panel_sottotitoli, null);

	}

	@Override
	public void caricaDefault() {
		comboBoxSerieDisponibili.removeAllItems();
		ArrayList<SerieTV> sts=GestioneSerieTV.getElencoSerieInserite();
		for(int i=0;i<sts.size();i++)
			comboBoxSerieDisponibili.addItem(sts.get(i));
	}

	@Override
	public void traduci() {
		// TODO Auto-generated method stub
		
	}
}
