package interfaccia;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class JPanelManagerLista extends JPanel {
	private static final long serialVersionUID = 1L;
	private GridLayout layout;

	public JPanelManagerLista() {
		super(new GridLayout(1,1));
		layout=(GridLayout)super.getLayout();
		setLayout(layout);
	}
	public Component add(Component c){
		int shift=(getComponentCount()<3?2:getComponentCount());
		layout.setRows(shift+1);
		super.add(c);
		revalidate();
		repaint();
		return c;
	}
	public void remove(Component c){
		int shift=(getComponentCount()<=3?3:getComponentCount()-1);
		layout.setRows(shift);
		super.remove(c);
		revalidate();
		repaint();
	}
	public void removeAll() {
		super.removeAll();
		layout.setRows(3);
		revalidate();
		repaint();
	}
}
