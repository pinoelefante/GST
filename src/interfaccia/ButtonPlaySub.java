package interfaccia;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class ButtonPlaySub extends DefaultCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JButton button;

	public ButtonPlaySub(JCheckBox checkBox) {
		super(checkBox);
		button = new JButton("Play");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Action play sub");
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return button;
	}

	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
	
}
