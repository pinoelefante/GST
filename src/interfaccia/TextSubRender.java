package interfaccia;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class TextSubRender extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;

	public TextSubRender(JTextField arg0) {
		super(arg0);
		arg0.setEnabled(false);
	}

}
