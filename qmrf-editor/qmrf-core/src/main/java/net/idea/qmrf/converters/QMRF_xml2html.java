package net.idea.qmrf.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class QMRF_xml2html {
	protected Transformer transformer;
	public QMRF_xml2html() {
	
	}
	protected InputStream getQMRF2HTML_XSL() throws IOException {
		return getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf2div.xsl");
	}
	protected InputStream getQMRF2SUMMARY_XSL() throws IOException {
		return getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf2summary.xsl");
	}
	public void xml2summary(Source sourceDocument, Writer html) throws IOException, TransformerException {
		InputStream xslt = null;
		try {
			if (transformer==null) {
				xslt = getQMRF2SUMMARY_XSL();
				transformer = getTransformer(new StreamSource(xslt));
			}
			transformer.transform(sourceDocument,  new StreamResult(html));			
		} finally {
			try {if (xslt!=null) xslt.close();} catch (Exception x) {}
		}
	}
	
	
	public void xml2html(Source sourceDocument, Writer html) throws IOException, TransformerException {
		InputStream xslt = getQMRF2HTML_XSL();
		try {
			Transformer transformer = getTransformer(new StreamSource(xslt));
			transformer.transform(sourceDocument,  new StreamResult(html));
		} finally {
			try {xslt.close();} catch (Exception x) {}
		}
	}
	public void xml2html(Source sourceDocument, OutputStream html) throws IOException, TransformerException {
		InputStream xslt = getQMRF2HTML_XSL();
		try {
			Transformer transformer = getTransformer(new StreamSource(xslt));
			transformer.transform(sourceDocument,  new StreamResult(html));
		} finally {
			try {xslt.close();} catch (Exception x) {}
		}
	}
	
	public void xsltTransform(InputStream xml, InputStream xslt,OutputStream out) throws IOException, TransformerException {
		Transformer transformer = getTransformer(new StreamSource(xslt));
		transformer.transform(new StreamSource(xml),  new StreamResult(out));
	}
	
	protected Transformer getTransformer(Source xslt)   throws IOException, TransformerException {
		TransformerFactory xfactory = TransformerFactory.newInstance();
		Transformer transformer = xfactory.newTransformer(xslt);
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "qmrf.dtd");
		return transformer;
	}


}
