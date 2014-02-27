package GUI.player;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;

public class PanelPlaylist extends JPanel{
	private static final long serialVersionUID = 1L;
	private Player player;
	private JButton btnAggiungi;
	private JButton btnRimuovi;
	private JList<ItemPlaylist> list;
	private JFileChooser fileChooser;
	

	public PanelPlaylist(){
		setLayout(new BorderLayout(0, 0));
		setBorder(new TitledBorder("Playlist"));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		btnAggiungi = new JButton("Aggiungi");
		panel.add(btnAggiungi);
		
		btnRimuovi = new JButton("Rimuovi");
		panel.add(btnRimuovi);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		list = new JList<ItemPlaylist>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		
		fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("Play");
        fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
        fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newAudioFileFilter());
        fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newPlayListFileFilter());
        FileFilter defaultFilter = SwingFileFilterFactory.newMediaFileFilter();
        fileChooser.addChoosableFileFilter(defaultFilter);
        fileChooser.setFileFilter(defaultFilter);
        fileChooser.setMultiSelectionEnabled(true);
		
		addActions();
	}
	private void addActions(){
		btnAggiungi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(player==null)
					player=Player.getInstance();
				if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(PanelPlaylist.this)) {
					File[] files=fileChooser.getSelectedFiles();
					for(int i=0;i<files.length;i++)
						player.add(files[i].getAbsolutePath());
                    ridisegna();
                }
			}
		});
		
		list.setDropMode(DropMode.ON);
		list.setDropTarget(new DropTarget(){
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) {
	            try {
	            	if(player==null)
	            		player=Player.getInstance();
	                evt.acceptDrop(DnDConstants.ACTION_COPY);
	                List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
	                if(droppedFiles!=null){
		                for (File file : droppedFiles) {
		                    /*
		                     * NOTE:
		                     *  When I change this to a println,
		                     *  it prints the correct path
		                     */
		                	System.out.println(file.getAbsolutePath());
		                	player.add(file.getAbsolutePath());
		                    
		                }
	                }
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
		});
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<ItemPlaylist> list = (JList<ItemPlaylist>)evt.getSource();
				if (evt.getClickCount() >= 2) {
					int index = list.locationToIndex(evt.getPoint());
					if(index>=0){
						Player.getInstance().playItem(index);
					}
				}
			}
		});
		btnRimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index=list.getSelectedIndex();
				if(index>=0)
					Player.getInstance().remove(index);
			}
		});
	}
	public void ridisegna(){
		if(player==null)
			player=Player.getInstance();
		list.setListData(player.getPlaylist().toArray(new ItemPlaylist[player.getPlaylist().size()]));
		list.revalidate();
		list.repaint();
	}
}
