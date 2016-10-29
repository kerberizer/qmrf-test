package net.idea.ambit.qmrf.app;

import net.idea.ambit.qmrf.QMRFData;
import net.idea.ambit.qmrf.QMRFObject;
import net.idea.ambit.qmrf.pmml.QMRFObjectExtended;
import net.idea.ambit.qmrf.swing.QMRFEditor;

public class QMRFEditorApp extends QMRFEditor {

	public QMRFEditorApp(String title, int w, int h, String[] args) {
		super(title, w, h, args);
	}

	@Override
	protected void initSharedData(String[] args) {
		
		qmrfData = new QMRFData(new QMRFObjectExtended(args,false), "qmrfeditor.xml");
		qmrfData.addObserver(this);

	}
	
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
				QMRFEditorApp app = new QMRFEditorApp("QMRF Editor ", 580, 360, args);
			}
		});
	}


}
