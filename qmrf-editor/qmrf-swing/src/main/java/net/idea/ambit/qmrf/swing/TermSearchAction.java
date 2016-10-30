package net.idea.ambit.qmrf.swing;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JEditorPane;
import javax.swing.SwingWorker;
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
			JEditorPane p = ((JEditorPane) e.getSource());
			TermSearcher searcher = new TermSearcher(p, qmrfData, e.getActionCommand());
			searcher.execute();
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
		}
	}

	public void copyTerm(String term, ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if (target != null) {
			Document doc = target.getDocument();
			try {
				doc.insertString(target.getCaretPosition(), term, null);
			} catch (Exception x) {
			}

		}
	}
}

class TermSearcher extends SwingWorker<String, Void> {

	protected String term;
	protected QMRFData qmrfdata;
	protected JEditorPane p;

	public TermSearcher(JEditorPane cmp, QMRFData qmrfdata, String term) {
		cmp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.p = cmp;
		this.term = term;
		this.qmrfdata = qmrfdata;
	}

	@Override
	public String doInBackground() throws IOException {
		try {
			p.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (!qmrfdata.getPath().toFile().exists())
				p.setText("<h3>Searching for `" + term
						+ "`...</h3><p>On first query the indexing may take a while, subsequent searches are fast.</p>");
			StringBuilder result = qmrfdata.searchTerm(term, 100);
			return result.toString();
		} catch (Exception x) {
			throw new IOException(x);
		}
	}

	@Override
	public void done() {
		try {
			String result = get();

			p.setText(String.format("<html><body><h1><a name='term'>%s</a></h1><pre>%s</pre></body></html>", term,
					result.toString()));
			p.scrollToReference("term");
		} catch (ExecutionException | InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			p.setCursor(Cursor.getDefaultCursor());
		}
	}

}