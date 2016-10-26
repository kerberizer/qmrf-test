package net.idea.ambit.qmrf.swing;

import java.awt.event.ActionEvent;

import javax.swing.text.JTextComponent;

import net.idea.ambit.qmrf.QMRFData;

public class TermPanelAction extends TermSearchAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8727769178170002246L;
	protected boolean search;

	public TermPanelAction(String name, QMRFData qmrfData, boolean search) {
		super(name, qmrfData);
		this.search = search;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!search) {
			qmrfData.getTermsPanel().setVisible(!qmrfData.getTermsPanel().isVisible());
			return;
		}
		qmrfData.getTermsPanel().setVisible(true);
		try {
			JTextComponent target = getTextComponent(e);
			if (target != null) {
				try {
					String selection = target.getText(target.getSelectionStart(),
							target.getSelectionEnd() - target.getSelectionStart());
					if (selection != null) {
						ActionEvent e1 = new ActionEvent(qmrfData.getTermsPanel().getFt(), e.getID(), selection);
						super.actionPerformed(e1);
					}
				} catch (Exception x) {
				}

			}
		} catch (Exception x) {
			// x.printStackTrace();
		}

	}

}
