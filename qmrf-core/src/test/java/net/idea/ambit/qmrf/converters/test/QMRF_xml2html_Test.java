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
