package net.idea.rest.protocol.resource.db;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.r.QueryReporter;
import net.idea.qmrf.converters.QMRF_xml2excel;
import net.idea.qmrf.converters.QMRF_xml2html;
import net.idea.qmrf.converters.QMRF_xml2pdf;
import net.idea.rest.protocol.DBProtocol;

import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xml.sax.InputSource;

public class QMRFReporter<Q extends IQueryRetrieval<DBProtocol>>  extends QueryReporter<DBProtocol,Q,OutputStream> {
	protected MediaType media;
	protected String fileExtension= null;
	
	public MediaType getMedia() {
		return media;
	}

	public void setMedia(MediaType media)  throws ResourceException {
		this.media = media;
		if (MediaType.APPLICATION_XML.equals(media)) {
			this.media = media; fileExtension = "xml";
		} else if (MediaType.APPLICATION_PDF.equals(media)) {
			this.media = media; fileExtension = "pdf";
		} else if (MediaType.APPLICATION_EXCEL.equals(media)) {
			this.media = media; fileExtension = "xls";
		} else if (MediaType.APPLICATION_WORD.equals(media)) {
			this.media = media; fileExtension = "doc";
		}
		else throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE,media.toString());
	}

	public QMRFReporter(Request request, MediaType media) throws ResourceException {
		super();
		setMedia(media);
	
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8943416606448051891L;

	public void header(OutputStream output, Q query) {};
	
	public void footer(OutputStream output, Q query) {};

	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {

			String xml = item.getAbstract();
			if (MediaType.APPLICATION_XML.equals(media)) {
				
				getOutput().write(xml.getBytes());
			} else if (MediaType.APPLICATION_PDF.equals(media)) {
				 QMRF_xml2pdf qpdf = new QMRF_xml2pdf(null);
		         qpdf.xml2pdf(new InputSource(new StringReader(xml)),getOutput());	
			} else if (MediaType.APPLICATION_EXCEL.equals(media)) {
				 QMRF_xml2excel qexcel = new QMRF_xml2excel();
				 qexcel.xml2excel(new InputSource(new StringReader(xml)),getOutput());	      
		         
			} else if (MediaType.APPLICATION_WORD.equals(media)) {
				 QMRF_xml2html qhtml = new QMRF_xml2html();
				 OutputStreamWriter writer = new OutputStreamWriter(getOutput());
				 qhtml.xml2summary(new StreamSource(new StringReader(xml)),writer);	  
			}
			
		} catch (IOException x) {
			throw new AmbitException(x);
		} finally {
			return item;
		}
	}
	
	@Override
	public String getFileExtension() {
		return fileExtension;
	}
	

}
