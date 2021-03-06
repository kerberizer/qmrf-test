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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import net.idea.ambit.qmrf.QMRFData;
import net.idea.ambit.qmrf.converters.QMRF_xml2rtf;
import net.idea.ambit.swing.common.UITools;
import net.idea.ambit.swing.interfaces.ISharedData;
import net.idea.qmrf.converters.QMRF_xml2excel;
import net.idea.qmrf.converters.QMRF_xml2pdf;

import org.xml.sax.InputSource;

import ambit2.base.io.MyIOUtilities;

public class QMRFFileSaveAsAction extends QMRFAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5151814164781462257L;
	public QMRFFileSaveAsAction(QMRFData userData, Container mainFrame) {
		this(userData, mainFrame,"Save As");
	}

	public QMRFFileSaveAsAction(QMRFData userData, Container mainFrame, String name) {
		this(userData, mainFrame, name,UITools.createImageIcon("ambit/ui/images/save_16.png"));
	}

	public QMRFFileSaveAsAction(QMRFData userData, Container mainFrame,
			String name, Icon icon) {
		super(userData, mainFrame, name, icon);
		putValue(SHORT_DESCRIPTION,"Saves QMRF document to .xml file");

	}
	@Override
	public void run(ActionEvent arg0) {
		super.run(arg0);
		String defaultDir = "";
		if (userData instanceof ISharedData) defaultDir = ((ISharedData) userData).getDefaultDir();

         saveFile(defaultDir);		
        
	}	
    public File selectFile(String defaultDir) {
        return  MyIOUtilities.selectFile(mainFrame,null,
                defaultDir,
                new String[] {".xml",".pdf",".rtf",".xls"},
                new String[]{"QMRF files (*.xml)","PDF files (*.pdf)","Rich Text Format files (*.rtf)","XLS files (*.xls)"},false);
        
    }
	public void saveFile(String defaultDir) {
		File file = selectFile(defaultDir);
     
        if (file == null) {
            getQMRFData().getJobStatus().setMessage("No file selected.");
            return;
        }
        try {
			String s = file.getAbsolutePath();
			int p = s.lastIndexOf(File.separator);
			if (p > 0)
			    defaultDir = s.substring(0,p);
			else defaultDir = s;            	
        	FileOutputStream out = new FileOutputStream(file);
            String filename = file.getAbsolutePath().toLowerCase(); 
        	if (filename.endsWith(".xml")) {
        		if (userData instanceof ISharedData) 
        			((ISharedData) userData).setDefaultDir(defaultDir);        		
	            getQMRFData().getQmrf().write(out);
                getQMRFData().getQmrf().setSource(s);
        	} else if (filename.endsWith("html")) {
                
            } else if (filename.endsWith("pdf")) {
            	  QMRF_xml2pdf x = new QMRF_xml2pdf(((QMRFData)userData).getQmrf().getTtfFontUrl());
            	
            	  StringWriter w = new StringWriter();	
            	  getQMRFData().getQmrf().write(w);
            	  x.xml2pdf(w.toString(),out);
            } else  if (filename.endsWith("rtf")) {
				 QMRF_xml2rtf qhtml = new QMRF_xml2rtf();
				 qhtml.initConfig();
				 OutputStreamWriter writer = new OutputStreamWriter(out);
				 qhtml.xml2rtf(getQMRFData().getQmrf(),writer);	            	
            } else if (filename.endsWith("xls")) {
            	QMRF_xml2excel x = new QMRF_xml2excel();
            	/*
            	PipedInputStream in = new PipedInputStream();
	          	  final PipedOutputStream pipeout = new PipedOutputStream(in);
	          	  new Thread(
	          	    new Runnable(){
	          	      public void run(){
	          	    	  try {
	          	    		  getQMRFData().getQmrf().write(pipeout);
	          	    	  } catch (Exception x) {
	          	    		  x.printStackTrace();
	          	    	  }
	          	      }
	          	    }
	          	  ).start();
	          	  */
            	  StringWriter w = new StringWriter();	
            	  getQMRFData().getQmrf().write(w);
     			  x.xml2excel(new InputSource(new StringReader(w.toString())),out);
            	
            }	

            out.close();
    	} catch (Exception x) {
    		JOptionPane.showMessageDialog(mainFrame,
    				x.getMessage(),
    			    "Error when saving file ",						
    			    JOptionPane.INFORMATION_MESSAGE);                
    	}
	}		
}


