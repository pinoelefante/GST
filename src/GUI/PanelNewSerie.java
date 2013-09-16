package GUI;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import SerieTV.GestioneSerieTV;
import SerieTV.SerieTV;

public class PanelNewSerie extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btnInserisci;
	private SerieTV st;
	
	public PanelNewSerie(SerieTV serie) {
		st=serie;
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setSize(250, 70);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		String nomeserie=serie.getNomeSerie();
		if(nomeserie.length()>25){
			ArrayList<Integer> index_space=new ArrayList<Integer>(2);
			for(int i=0;i<nomeserie.length();i++){
				if(nomeserie.charAt(i)==' '){
					index_space.add(i);
				}
			}
			int split_index=index_space.get(index_space.size()/2);
			nomeserie=nomeserie.substring(0, split_index)+"<br>"+nomeserie.substring(split_index);
		}
		JLabel lblnomeSerie = new JLabel("<html><b>"+nomeserie+"</b></html>");
		panel.add(lblnomeSerie);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		
		btnInserisci = new JButton("");
		btnInserisci.setIcon(new ImageIcon(PanelNewSerie.class.getResource("/GUI/res/add.png")));
		panel_1.add(btnInserisci);
		
		addListener();
	}
	private void addListener(){
		btnInserisci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(GestioneSerieTV.aggiungiSeriePreferita(st)){
					Interfaccia.getInterfaccia().reloadSeriePreferite();
					class updateThread extends Thread{
						public void run(){
							st.aggiornaEpisodiOnline();
							Interfaccia.getInterfaccia().inizializzaDownloadScroll();
						}
					}
					Thread t=new updateThread();
					t.start();
				}
			}
		});
	}
}
