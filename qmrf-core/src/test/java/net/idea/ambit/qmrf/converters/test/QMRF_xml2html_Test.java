package net.idea.ambit.qmrf.converters.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import junit.framework.Assert;
import net.idea.qmrf.converters.QMRF_xml2html;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class QMRF_xml2html_Test {
	
	   @Test
	    public void testHTML() throws Exception {
	    	QMRF_xml2html qhtml = new QMRF_xml2html();
	         File html = new File("qmrf_1_2.html");
	         html.deleteOnExit();
	         if (html.exists()) html.delete();
	         
	         InputStream xml = getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf.xml");
	         
	         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	         factory.setValidating(false);
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         InputSource src = new InputSource(xml);
	         Document xmlDocument = builder.parse(src.getByteStream());
	         DOMSource source = new DOMSource(xmlDocument);
	         
	        // File xml = new File(url.getFile());
	         qhtml.xml2html(source,new FileOutputStream(html));
	         Assert.assertTrue(html.exists());
	         Assert.assertTrue(html.length()>0);
	    }  
}
