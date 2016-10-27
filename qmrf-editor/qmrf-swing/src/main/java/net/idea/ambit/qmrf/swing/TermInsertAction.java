package net.idea.ambit.qmrf.swing;

import java.awt.event.ActionEvent;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import net.idea.ambit.qmrf.QMRFData;

public class TermInsertAction extends TextAction {

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
		if (qmrfData.getTerm_uri() == null && qmrfData.getTerm() == null)
			return;
		try {
			JTextComponent target = getTextComponent(e);
			if (target != null) {
				Document doc = target.getDocument();
				try {
					if (qmrfData.getTerm_uri() == null)
						doc.insertString(target.getCaretPosition(), qmrfData.getTerm(), null);
					else
						doc.insertString(target.getCaretPosition(),
								String.format("[%s](%s)", qmrfData.getTerm() == null ? "" : qmrfData.getTerm(),
										qmrfData.getTerm_uri() == null ? "" : qmrfData.getTerm_uri()),
								null);
				} catch (Exception x) {
					x.printStackTrace();
				}

			}
		} catch (Exception x) {
			// x.printStackTrace();
		}
	}

}