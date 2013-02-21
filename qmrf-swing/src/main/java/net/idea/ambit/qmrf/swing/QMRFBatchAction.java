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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import net.idea.ambit.qmrf.QMRFData;
import net.idea.ambit.swing.common.UITools;
import net.idea.qmrf.converters.QMRF_xml2pdf;

import org.xml.sax.InputSource;

import ambit2.base.io.MyIOUtilities;

public class QMRFBatchAction extends QMRFFileOpenAction {

	public QMRFBatchAction(QMRFData userData, Container mainFrame) {
		this(userData, mainFrame,"Batch PDF Generation");
	}


	public QMRFBatchAction(QMRFData userData, Container mainFrame, String name) {
		this(userData, mainFrame, name,UITools.createImageIcon("ambit/ui/images/disk_multiple.png"));
	}

	public QMRFBatchAction(QMRFData userData, Container mainFrame,
			String name, Icon icon) {
		super(userData, mainFrame, name, icon);
		putValue(SHORT_DESCRIPTION,"Creates .pdf files from multiple QMRF documents without loading the files into QMRF editor.");
	}
	@Override
	public void readFile(String defaultDir) {
        File[] files = MyIOUtilities.selectFiles(mainFrame,null,
                defaultDir,
                new String[] {".xml"},new String[]{"QMRF files (*.xml)"},true,null);
        if (files != null) {
        	int error = 0;
        	mainFrame.setCursor(hourglassCursor);
	        QMRF_xml2pdf converter = new QMRF_xml2pdf(((QMRFData)userData).getQmrf().getTtfFontUrl());
	        for (int i=0; i < files.length;i++) 
	        try {
	        	InputStreamReader reader = 	new InputStreamReader(new FileInputStream(files[i]),"UTF-8");
	        	String extension = ".pdf";
	    	    
	        	String suffix = null;
	    	    String fname= files[i].getAbsolutePath(); 
	    	    int p = fname.lastIndexOf('.');

	    	    File pdffile = null;
	    	    if(p > 0 && p < fname.length() - 1)
	    	    	pdffile = new File(fname.substring(0,p)+extension); 
	    	    else
	    	    	pdffile = new File(fname+extension);
	    	    
	        	OutputStream pdf = new FileOutputStream(pdffile);
	        	converter.xml2pdf(new InputSource(reader), pdf);
	        	reader.close();
	        } catch (Exception x) {
	        	error ++;
	        	logger.log(Level.WARNING,x.getMessage(),x);
	        }
	        StringBuffer b = new StringBuffer();
	        b.append(files.length);
	        b.append(" files processed; ");
	        b.append(files.length-error);
	        b.append(" pdf files generated.");
	        mainFrame.setCursor(normalCursor);
    		JOptionPane.showMessageDialog(mainFrame,
    				b.toString(),
    			    "PDF Generation ",						
    			    JOptionPane.INFORMATION_MESSAGE);        	        
        }
        
	}
}
