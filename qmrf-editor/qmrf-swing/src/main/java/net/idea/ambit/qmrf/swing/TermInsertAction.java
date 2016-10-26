package net.idea.ambit.qmrf.swing;

import java.awt.event.ActionEvent;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import net.idea.ambit.qmrf.QMRFData;

public class TermInsertAction  extends TextAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6237484868921942017L;
	protected QMRFData qmrfData;

	public TermInsertAction(String name, QMRFData qmrfData) {
		super(name);
		this.qmrfData = qmrfData;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (qmrfData.getTerm_uri()==null) return;
		try {
			JTextComponent target = getTextComponent(e);
			if (target != null) {
				Document doc = target.getDocument();
				try {
					doc.insertString(target.getCaretPosition(), qmrfData.getTerm_uri(),null);
				} catch (Exception x) {
				}

			}	
		} catch (Exception x) {
			// x.printStackTrace();
		}
	}

}