package GUI;

import java.awt.Font;
import javax.swing.JLabel;

public class ThreadModificaLabel extends Thread {
	private JLabel		label;
	private String		testo;
	private int			milli;
	private static Font	font	= new Font("Tahoma", 3, 10);

	public ThreadModificaLabel(JLabel targa, String text, int time) {
		this.label = targa;
		this.testo = text;
		this.milli = time;
		start();
	}

	public void run() {
		this.label.setFont(font);

		this.label.setText(this.testo);
		try {
			Thread.sleep(this.milli);
			this.label.setText("");
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
