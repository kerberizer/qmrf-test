package net.idea.ambit.qmrf.swing;

import java.awt.event.ActionEvent;

import javax.swing.text.TextAction;

import net.idea.ambit.qmrf.QMRFData;

public class TermPanelAction extends TextAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8727769178170002246L;
	protected QMRFData qmrfData;
	public TermPanelAction(String name, QMRFData qmrfData) {
		super(name);
		this.qmrfData= qmrfData;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		qmrfData.getTermsPanel().setVisible(!qmrfData.getTermsPanel().isVisible());
		
	}

}
