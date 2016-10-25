package net.idea.ambit.qmrf.swing;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import net.idea.ambit.qmrf.QMRFData;

public class TermSearchAction extends TextAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6297449071244811698L;
	protected QMRFData qmrfData;

	public TermSearchAction(String name, QMRFData qmrfData) {
		super(name);
		this.qmrfData = qmrfData;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			StringBuilder result = qmrfData.searchTerm(e.getActionCommand(), 100);
			JEditorPane p = ((JEditorPane) e.getSource());
			p.setText(String.format("<html><body><h1><a name='term'>%s</a></h1><pre>%s</pre></body></html>",
					e.getActionCommand(), result.toString()));
			p.scrollToReference("term");
			/*
			 * JScrollPane sp = (JScrollPane) ((JComponent)
			 * e.getSource()).getParent(); JViewport viewport =
			 * sp.getViewport(); viewport.setViewPosition(new Point(0, 0));
			 */

			// scrollToReference("term");
		} catch (Exception x) {
			// x.printStackTrace();
		}
	}

	public void copyTerm(String term) {
		JTextComponent target = getTextComponent(null);
		if (target != null) {
			Document doc = target.getDocument();
			try {
				doc.insertString(target.getCaretPosition(), term,null);
			} catch (Exception x) {
			}

		}
	}
}