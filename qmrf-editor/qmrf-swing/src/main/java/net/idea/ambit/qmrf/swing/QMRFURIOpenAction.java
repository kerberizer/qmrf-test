package net.idea.ambit.qmrf.swing;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URI;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import net.idea.ambit.qmrf.QMRFData;
import net.idea.ambit.swing.common.UITools;

/**
 * Loads remote QMRF document
 * @author nina
 *
 */
public class QMRFURIOpenAction extends QMRFAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6551730271676956215L;
	public QMRFURIOpenAction(QMRFData userData, Container mainFrame) {
		this(userData, mainFrame,"Open Location");
	}

	public QMRFURIOpenAction(QMRFData userData, Container mainFrame, String name) {
		this(userData, mainFrame, name,UITools.createImageIcon("ambit/ui/images/open_document_16.png"));
	}

	public QMRFURIOpenAction(QMRFData userData, Container mainFrame,
			String name, Icon icon) {
		super(userData, mainFrame, name, icon);
		putValue(SHORT_DESCRIPTION,"Loads remote QMRF document from web address");
	}
	@Override
	public void run(ActionEvent arg0) {
		super.run(arg0);
        readURI();
	}
	public void readURI() {
		if (getQMRFData().getQmrf().isModified()) {
    		if (JOptionPane.showConfirmDialog(mainFrame,
    				"<html>QMRF document <b>\"" + getQMRFData().getQmrf().getSource() + "\"</b> has been modified. <br>Discard changes?</html>",
    			    "Confirm",		
    			    JOptionPane.OK_CANCEL_OPTION,
    			    JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION)
    			return;
		}
		String uri = JOptionPane.showInputDialog(null, "Type the internet address of QMRF document:","", 1);
		try {
     		getQMRFData().getQmrf().setSource(uri);
			getQMRFData().getQmrf().readURI(new URI(uri));
			getQMRFData().getQmrf().setModified(true);
     		getQMRFData().getQmrf().setNotModified();
      		getQMRFData().getJobStatus().setMessage(String.format("%s loaded.", uri));
		} catch (MalformedURLException x) {
    		JOptionPane.showMessageDialog(mainFrame,
    				x.getMessage(),
    			    String.format("Invalid URI %s",uri),						
    			    JOptionPane.ERROR_MESSAGE);     
    	} catch (Exception x) {
    		JOptionPane.showMessageDialog(mainFrame,
    				x.getMessage(),
    			    String.format("Error when loading %s",uri),						
    			    JOptionPane.INFORMATION_MESSAGE);                
    	}
	}	
}

