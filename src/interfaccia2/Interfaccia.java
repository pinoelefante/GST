package interfaccia2;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;

public class Interfaccia extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private PanelOpzioni p_opzioni;
	private PanelPrincipale p_principale=new PanelPrincipale();
	private JPanel p_serietv, p_lettore, p_torrent;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Interfaccia frame = new Interfaccia();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private JMenuItem mntmPrincipale, mntmSerieTv, mntmLettore,  mntmDownload, mntmOpzioni;
	public Interfaccia() {
		setTitle("GestioneSerieTV by pinoelefante");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmEsci = new JMenuItem("Esci");
		mnFile.add(mntmEsci);
		
		JMenu mnVista = new JMenu("Vista");
		menuBar.add(mnVista);
		
		mntmPrincipale = new JMenuItem("Principale");
		mntmPrincipale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(p_principale==null)
					p_principale=new PanelPrincipale();
				changeView(p_principale);
			}
		});
		mnVista.add(mntmPrincipale);
		
		JSeparator separator = new JSeparator();
		mnVista.add(separator);
		
		mntmSerieTv = new JMenuItem("Serie TV");
		mnVista.add(mntmSerieTv);
		
		mntmLettore = new JMenuItem("Lettore");
		mnVista.add(mntmLettore);
		
		mntmDownload = new JMenuItem("Download");
		mnVista.add(mntmDownload);
		
		mntmOpzioni = new JMenuItem("Opzioni");
		mntmOpzioni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(p_opzioni==null)
					p_opzioni=new PanelOpzioni();
				p_opzioni.caricaDefault();
				changeView(p_opzioni);
			}
		});
		mnVista.add(mntmOpzioni);
		
		JMenu mnAbout = new JMenu("?");
		menuBar.add(mnAbout);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnAbout.add(mntmAbout);
		
		JMenuItem mntmCirca = new JMenuItem("Dona");
		mnAbout.add(mntmCirca);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(8);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		changeView(p_principale);
	}
	private void changeView(JComponent c){
		scrollPane.setViewportView(c);
		revalidate();
		repaint();
	}
}
