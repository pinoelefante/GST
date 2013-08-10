package GUI;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class PanelEpisodioLettore extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PanelEpisodioLettore() {
		setSize(750, 100);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnPlaylist = new JButton("");
		btnPlaylist.setToolTipText("Aggiungi alla playlist");
		btnPlaylist.setIcon(new ImageIcon(PanelEpisodioLettore.class.getResource("/GUI/res/add.png")));
		btnPlaylist.setBounds(691, 33, 49, 32);
		panel.add(btnPlaylist);

	}
}
