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

package net.idea.ambit.qmrf.converters.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.Assert;
import net.idea.qmrf.converters.QMRF_xml2pdf;

import org.junit.Test;
import org.xml.sax.InputSource;

public class QMRF_xml2pdf_Test {

	@Test
    public void testPDF() throws Exception {
    	//URL ttf = getClass().getClassLoader().getResource("ambit2/qmrfeditor/fonts/times.ttf");
    	
    	QMRF_xml2pdf qpdf = new QMRF_xml2pdf(null);
    	
         File pdf = new File("qmrf_1_2.pdf");
         pdf.deleteOnExit();
         if (pdf.exists()) pdf.delete();
         
         StringBuffer b = new StringBuffer();
         InputStream in = getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf.xml"); //"data/qmrf/test_1_2.xml");
         BufferedReader r = new BufferedReader(new InputStreamReader(in));
         String line;
         while ( (line = r.readLine()) != null)
        	 b.append(line);
         r.close();
         
         qpdf.xml2pdf(b.toString().trim(),new FileOutputStream(pdf));
        
         Assert.assertTrue(pdf.exists());
         Assert.assertTrue(pdf.length()>0);
    }   
	
    
    @Test
    public void testPDFReader() throws Exception {
    	//URL ttf = getClass().getClassLoader().getResource("ambit2/qmrfeditor/fonts/times.ttf");
    	QMRF_xml2pdf qpdf = new QMRF_xml2pdf("C:/Windows/Fonts/times.ttf");
         File pdf = new File("qmrf_1_2.pdf");
         pdf.deleteOnExit();
         if (pdf.exists()) pdf.delete();
         
         URL url = getClass().getClassLoader().getResource("ambit2/qmrfeditor/qmrf.xml");
         File xml = new File(url.getFile());
         qpdf.xml2pdf(new InputSource(new FileReader(xml)),new FileOutputStream(pdf));
         //qpdf.xml2pdf(new FileReader("data/qmrf/test_1_2.xml"),new FileOutputStream(pdf));
         Assert.assertTrue(pdf.exists());
         Assert.assertTrue(pdf.length()>0);
    }  
    
}
