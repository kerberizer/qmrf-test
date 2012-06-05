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

package net.idea.qmrf.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class QMRF_xml2html {
	//Templates are thread safe
	protected static Templates templates=null;
	protected Transformer transformer=null;
	
	public QMRF_xml2html() {
	
	}
	protected InputStream getQMRF2HTML_XSL() throws IOException {
		return getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf2div.xsl");
	}
	protected InputStream getQMRF2SUMMARY_XSL() throws IOException {
		return getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf2summary.xsl");
	}
	public synchronized void  xml2summary(Source sourceDocument, Writer html) throws IOException, TransformerException {
		InputStream xslt = null;
		try {
			if (templates==null) {
				xslt = getQMRF2SUMMARY_XSL();
				templates = getTemplates(new StreamSource(xslt));
			}
			if (transformer==null) transformer = templates.newTransformer();
			transformer.transform(sourceDocument,  new StreamResult(html));			
		} finally {
			try {if (xslt!=null) xslt.close();} catch (Exception x) {}
		}
	}
	
	
	public synchronized void xml2html(Source sourceDocument, Writer html) throws IOException, TransformerException {
		InputStream xslt = getQMRF2HTML_XSL();
		try {
			Transformer transformer = getTransformer(new StreamSource(xslt));
			transformer.transform(sourceDocument,  new StreamResult(html));
		} finally {
			try {xslt.close();} catch (Exception x) {}
		}
	}

	public synchronized void xml2html(Source sourceDocument, OutputStream html) throws IOException, TransformerException {
		InputStream xslt = getQMRF2HTML_XSL();
		try {
			Transformer transformer = getTransformer(new StreamSource(xslt));
			transformer.transform(sourceDocument,  new StreamResult(html));
		} finally {
			try {xslt.close();} catch (Exception x) {}
		}
	}
	
	public synchronized void  xsltTransform(InputStream xml, InputStream xslt,OutputStream out) throws IOException, TransformerException {
		Transformer transformer = getTransformer(new StreamSource(xslt));
		transformer.transform(new StreamSource(xml),  new StreamResult(out));
	}
	
	protected synchronized Templates getTemplates(Source xslt)   throws IOException, TransformerException {
		TransformerFactory xfactory = TransformerFactory.newInstance();
		return xfactory.newTemplates(xslt);
	}

	protected synchronized Transformer getTransformer(Source xslt)   throws IOException, TransformerException {
		TransformerFactory xfactory = TransformerFactory.newInstance();
		Transformer transformer = xfactory.newTransformer(xslt);
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "qmrf.dtd");
		return transformer;
	}

}
