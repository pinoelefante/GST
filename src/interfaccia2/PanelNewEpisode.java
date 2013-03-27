package interfaccia2;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.GridLayout;

public class PanelNewEpisode extends JPanel {

	/**
	 * Create the panel.
	 */
	public PanelNewEpisode() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		JCheckBox chckbxCheck = new JCheckBox("check");
		panel_1.add(chckbxCheck);
		
		JLabel lblNometorrent = new JLabel("nome_torrent");
		panel.add(lblNometorrent, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.NORTH);
		
		JButton btnScarica = new JButton("scarica");
		panel_3.add(btnScarica);
		
		JButton btnInformazioni = new JButton("informazioni");
		panel_3.add(btnInformazioni);
		
		JButton btnIgnora = new JButton("ignora");
		panel_3.add(btnIgnora);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.SOUTH);
		
		JLabel lblTags = new JLabel("TAGS");
		panel_4.add(lblTags);

	}

}
