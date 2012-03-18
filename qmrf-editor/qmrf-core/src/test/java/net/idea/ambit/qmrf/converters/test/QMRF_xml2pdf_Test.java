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
