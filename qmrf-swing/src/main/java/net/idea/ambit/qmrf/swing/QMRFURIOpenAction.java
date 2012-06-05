/*
Copyright (C) 2005-2012  

Contact: www.ideaconsult.net

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

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
			if (uri!=null && !"".equals(uri.trim())) {
				uri = uri.trim();
	     		getQMRFData().getQmrf().setSource(uri);
				getQMRFData().getQmrf().readURI(new URI(uri));
				getQMRFData().getQmrf().setModified(true);
	     		getQMRFData().getQmrf().setNotModified();
	      		getQMRFData().getJobStatus().setMessage(String.format("%s loaded.", uri));
			} else throw new Exception("Please enter a valid URL");
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

