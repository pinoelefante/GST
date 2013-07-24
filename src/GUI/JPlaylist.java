package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import LettoreVideo.Playlist;
import LettoreVideo.PlaylistItem;
import LettoreVideo.Player;

public class JPlaylist extends JScrollPane {
	private static final long serialVersionUID = 1L;
	
	private Playlist playlist;
	private Player player;
	private JList<PlaylistItem> p_graphic;
	private JButton b_up, b_down, b_del;
	
	public JPlaylist(){
		playlist=new Playlist();
		p_graphic=new JList<PlaylistItem>();
		p_graphic.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		b_up=new JButton();
		b_down=new JButton();
		b_del=new JButton();
		
		addListener();
		
		setViewportView(p_graphic);
	}
	public void addItem(PlaylistItem s){
		playlist.addItem(s);
		ridisegna();
	}
	public void addItem(String path_file){
		playlist.addItem(path_file);
		ridisegna();
	}
	public JButton getButtonUp(){
		return b_up;
	}
	public JButton getButtonDown(){
		return b_down;
	}
	public JButton getButtonDel(){
		return b_del;
	}
	private void addListener(){
		b_up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int ind=p_graphic.getSelectedIndex();
				if(ind>=0){
					playlist.moveUp(ind);
					ridisegna();
					p_graphic.setSelectedIndex(ind-1);
				}
			}
		});
		b_down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int ind=p_graphic.getSelectedIndex();
				if(ind>=0){
					playlist.moveDown(ind);
					ridisegna();
					p_graphic.setSelectedIndex(ind+1);
				}
			}
		});
		b_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ind=p_graphic.getSelectedIndex();
				if(ind>=0){
					playlist.removeItem(ind);
					ridisegna();
					p_graphic.setSelectedIndex(ind);
				}
			}
		});
		p_graphic.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        @SuppressWarnings("unchecked")
				JList<PlaylistItem> list = (JList<PlaylistItem>)evt.getSource();
		        if (evt.getClickCount() >= 2) {
		            int index = list.locationToIndex(evt.getPoint());
		            if(index>=0){
		            	if(player!=null){
		            		player.play(playlist.getItem(index));
		            	}
		            }
		        }
		    }
		});
	}
	private void ridisegna(){
		p_graphic.setListData(playlist.getArray());
		p_graphic.revalidate();
		p_graphic.repaint();
	}
	public void stampaPlaylist(){
		System.out.println(playlist);
	}
	public Playlist getPlaylist(){
		return playlist;
	}
	public void setPlayer(Player p){
		player=p;
	}
}
