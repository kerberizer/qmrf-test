package net.idea.ambit.swing.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import net.idea.ambit.swing.common.JTermPanel;
import net.idea.ambit.swing.common.JUnicodePanel;

public class InsertTermAction extends TextAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8727769178170002246L;
	protected Component parent;

	public InsertTermAction(Component parent, String name) {
		super(name);
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if (target != null) {
			JTermPanel p = new JTermPanel();
			if (JOptionPane.showConfirmDialog(null, p, "Select term", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {

				Document doc = target.getDocument();
				try {
					doc.insertString(target.getCaretPosition(), JTermPanel.getTerm(),
							null);

				} catch (Exception x) {
					x.printStackTrace();
				}

				// target.paste();
			}
		}
	}

}
