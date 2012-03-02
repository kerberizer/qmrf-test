package net.idea.ambit.qmrf.converters.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.Assert;
import net.idea.qmrf.converters.QMRF_xml2excel;

import org.junit.Test;
import org.xml.sax.InputSource;

public class QMRF_xml2excel_Test {

    @Test
    public void testXLS() throws  Exception {
	         QMRF_xml2excel qpdf = new QMRF_xml2excel();
	     	InputStream in = getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf.xml"); 

	         File xls = new File("qmrf_1_2.xls");
	         xls.deleteOnExit();
	         if (xls.exists()) xls.delete();
	         qpdf.xml2excel(new InputSource(in),new FileOutputStream(xls));	      
             Assert.assertTrue(new File("qmrf_1_2.xls").exists());
    } 
}
