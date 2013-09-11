package Programma;

import java.awt.GridLayout;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.PanelFileCopy;

public class FileManager {
	private static ArrayList<Download> file_queue;
	private static ArrayList<PanelFileCopy> panel_list;
	private static JPanel panel_download;
	private static JScrollPane scroll;
	private static int current_file;
	
	
	public static void instance(){
		panel_download=new JPanel();
		panel_download.setLayout(new GridLayout(4, 1));
		scroll=new JScrollPane(panel_download);
		file_queue=new ArrayList<Download>(1);
		panel_list=new ArrayList<PanelFileCopy>(1);
		
		addListener();
	}
	
	private static void addListener() {
		panel_download.addContainerListener(new ContainerListener() {
			public void componentRemoved(ContainerEvent arg0) {
				GridLayout lay=(GridLayout) panel_download.getLayout();
				
				if(panel_download.getComponentCount()>4)
					lay.setRows(lay.getRows()-1);
				else
					lay.setRows(4);
				
				panel_download.revalidate();
				panel_download.repaint();
				
			}
			
			public void componentAdded(ContainerEvent arg0) {
				GridLayout lay=(GridLayout) panel_download.getLayout();
				
				if(panel_download.getComponentCount()>4)
					lay.setRows(lay.getRows()+1);
				else
					lay.setRows(4);
				
				panel_download.revalidate();
				panel_download.repaint();
				
			}
		});
	}

	public static void addDownloadFile(Download d){
		try {
			file_queue.add(d);
			PanelFileCopy p=new PanelFileCopy(d);
			panel_list.add(p);
			panel_download.add(p);
		}
		catch(NullPointerException e){
			instance();
			addDownloadFile(d);
		}
	}
	public static void removeDownload(Download d){
		if(!d.isComplete() && d.isStarted())
			d.getDownloadThread().interrupt();
		file_queue.remove(d);
		
	}
	public static boolean isEmpty(){
		if(current_file==file_queue.size())
			return true;
		return false;
	}
	public static JComponent getPanel(){
		return scroll;
	}
	public static void removePanel(JPanel p){
		panel_download.remove(p);
	}
}
