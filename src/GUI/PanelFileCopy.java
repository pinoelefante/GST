package GUI;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JProgressBar;
import javax.swing.JButton;

import Programma.Download;
import Programma.FileManager;

import javax.swing.border.EtchedBorder;

import java.awt.GridLayout;

public class PanelFileCopy extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Download download;
	private Thread t_pending;

	public PanelFileCopy(Download d) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		download=d;
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.NORTH);
		
		String nomefile=d.getUrlDownload().substring(d.getUrlDownload().lastIndexOf(File.separator)+1);
		
		JLabel lblnomefile = new JLabel("<html>Origine: "+nomefile+"</html>");
		panel.add(lblnomefile);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_5.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_5);
		
		JLabel lbldestinazionepath = new JLabel("<html>Destinazione: "+d.getPathDownload().substring(0, d.getPathDownload().indexOf(nomefile))+"</html>");
		panel_5.add(lbldestinazionepath);
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_6.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_6);
		
		JLabel lblStato = new JLabel("Stato: ");
		panel_6.add(lblStato);
		
		lblCurrentStatus = new JLabel("In coda");
		panel_6.add(lblCurrentStatus);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.WEST);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(500, 25));
		panel_3.add(progressBar);
		
		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.EAST);
		FlowLayout flowLayout_2 = (FlowLayout) panel_4.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		
		btnAvvia = new JButton("Avvia");
		panel_4.add(btnAvvia);
		
		btnPausa = new JButton("Pausa");
		panel_4.add(btnPausa);
		
		btnAnnulla = new JButton("X");
		panel_4.add(btnAnnulla);
		
		addListener();
		
		t_pending=new threadCheckCopyStart();
		t_pending.start();
	}
	private Thread t_update_bar;

	private JProgressBar progressBar;

	private JButton btnAvvia;

	private JButton btnPausa;

	private JButton btnAnnulla;

	private JLabel lblCurrentStatus;
	public void avviaDownload(){
		if(!download.isStarted()){
			FileManager.downloadStarted();
			lblCurrentStatus.setText("Copia in corso...");
			btnAvvia.setEnabled(false);
			download.avviaDownload();
			t_update_bar=new updateProgressBarThread();
			t_update_bar.start();
		}
	}
	public void arrestaDownload(){
		download.interrompiDownload();
		t_update_bar.interrupt();
	}
	class updateProgressBarThread extends Thread {
		public void run(){
			try {
				Thread.sleep(500L);
				while(!download.isComplete()){
					long d_total=download.getFileSize();
					long d_size=download.getFileSizeDowloaded();
					
					int percentuale=(int) ((d_size*100)/d_total);
					progressBar.setValue(percentuale);
					Thread.sleep(500L);
				}
				progressBar.setValue(100);
				lblCurrentStatus.setText("Copia completata");
				FileManager.downloadStopped();
			}
			catch(InterruptedException e){
				FileManager.downloadStopped();
			}
			catch(ArithmeticException e){
				lblCurrentStatus.setText("Errore durante la copia");
				interrupt();
				btnAvvia.setEnabled(true);
			}
		}
	}
	private void addListener() {
		btnAvvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				avviaDownload();
				btnAvvia.setEnabled(false);
			}
		});
		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				class ThreadRimuoviCopia extends Thread {
					public void run() {
						btnAnnulla.setEnabled(false);
						if(download.isStarted() && !download.isComplete())
							arrestaDownload();
						FileManager.removePanel(PanelFileCopy.this);
						
						btnAnnulla.setEnabled(true);
					}
				}
				Thread t=new ThreadRimuoviCopia();
				t.start();
			}
		});
	}
	class threadCheckCopyStart extends Thread {
		public void run(){
			while(FileManager.isAnotherCopyNow()){
				try {
					sleep(1000);
				}
				catch (InterruptedException e) {}
			}
			avviaDownload();
		}
	}
}
