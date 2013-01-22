package GUI;

import javax.swing.JButton;

import Programma.ManagerException;

public class ThreadModificaAttivoBottone extends Thread {
	private JButton			bottone;
	private int				tempo;
	public static boolean	alreadyStarted	= false;
	public static boolean	stopped			= false;

	public ThreadModificaAttivoBottone(JButton button, int tempo) {
		this.bottone = button;
		this.tempo = tempo;
	}

	public void run() {
		alreadyStarted = true;
		int i = this.tempo;
		while (this.tempo >= 0) {
			try {
				Thread.sleep(1000L);
				this.bottone.setText(Language.DOWNLOAD_BOTTONE_DOWNLOAD + " (" + i-- + ")");
				this.tempo -= 1;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				ManagerException.registraEccezione(e);
			}
		}
		this.bottone.setText(Language.DOWNLOAD_BOTTONE_DOWNLOAD);
		this.bottone.setEnabled(true);
		stopped = true;
	}
}
