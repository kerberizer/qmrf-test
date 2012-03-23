package net.idea.rest.protocol.resource.db;

import java.io.StringReader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.converters.QMRF_xml2html;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.DBProtocol;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class QMRFChaptersHTMLReporter extends QMRFHTMLReporter<DBProtocol, IQueryRetrieval<DBProtocol>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6689564780234513147L;

	protected DocumentBuilder builder;
	protected DocumentBuilderFactory factory;
	protected QMRF_xml2html qhtml;
	protected EntityResolver dtdresolver;
	public EntityResolver getDtdresolver() {
		return dtdresolver;
	}
	public void setDtdresolver(EntityResolver dtdresolver) {
		this.dtdresolver = dtdresolver;
	}
	protected final static String[][] chapters = {
		//	{"QSAR identifier","Identifier"},
			{"General information","General"},
			{"Defining the endpoint - OECD Principle 1","Endpoint"},
			{"Defining the algorithm - OECD Principle 2","Algorithm"},
			{"Defining the applicability domain - OECD Principle 3","App. domain"},
			{"Internal validation - OECD Principle 4","Robustness"},
			{"External validation - OECD Principle 4","Predictivity"},
			{"Providing a mechanistic interpretation - OECD Principle 5","Interpretation"},
			{"Miscellaneous information (comments, bibliography)","Bibliography"},
	};	
	
	public QMRFChaptersHTMLReporter(Request request, boolean collapsed,
			ResourceDoc doc, HTMLBeauty htmlBeauty) {
		super(request, collapsed, doc, htmlBeauty);
	}

	public QMRFChaptersHTMLReporter(Request request, boolean collapsed,boolean editable,boolean paging, boolean details) {
		super(request,collapsed,null,null);
		setTitle(!collapsed?null:"QMRF document");
	}	

	protected DOMSource getDOMSource(DBProtocol item) throws Exception {
		  String xml = item.getAbstract();
		  if (factory==null) {
			    factory = DocumentBuilderFactory.newInstance();
		       // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		        factory.setValidating(true);
		  }
		   if (builder==null) {
			   builder = factory.newDocumentBuilder();
			   builder.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					
				}
				
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					
				}
				
				@Override
				public void error(SAXParseException exception) throws SAXException {
					
				}
			});
			   builder.setEntityResolver(dtdresolver);
		   }
		   Document xmlDocument = builder.parse( new InputSource(new StringReader(xml)));
		   
		   return new DOMSource(xmlDocument);
	}
	@Override
	protected boolean printAsTable() {
		return false;
	}
	
	@Override
	protected void printPageNavigator(IQueryRetrieval<DBProtocol> query)
			throws Exception {
	
	}
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		
	}

	@Override
	protected void printTable(Writer output, String uri, DBProtocol item) {
		
	}

	@Override
	protected void printForm(Writer output, String uri, DBProtocol item,
			boolean editable) {

		try {
			output.write("<div class='tabs'>\n");
			if (qhtml==null) qhtml = new QMRF_xml2html();
			
			String attachmentURI = String.format(
					"<a href=\"%s%s?headless=true&media=text/html\" title=\"Attachments\">Attachments</a>",
					uri,Resources.attachment);		
			
			String baseRef = uriReporter.getBaseReference().toString();
	
			String imgstyle = "style='float:left; margin-left:5px; margin-top:5px; margin-right:0px;'";
			output.write("<ul>\n");
			for (int i=0; i < chapters.length; i++) {
				output.write(String.format(
				"<li><img src='%s/images/qmrf/chapter%d.png' %s><a style='margin-left:0px;' href='#tabs-%d' title='%d.%s'>%s</a></li>",
				baseRef,
				(i+2),
				imgstyle,
				(i+2),
				(i+2),
				chapters[i][0],
				chapters[i][1]
				));
			}
			output.write(String.format("<li><img src='%s/images/qmrf/attachments.png' %s> %s<span></span></li>",
					baseRef,imgstyle,attachmentURI));
			output.write("</ul>\n");
			long now = System.currentTimeMillis();
			try {
				qhtml.xml2summary(getDOMSource(item),output);
			} catch (Exception x) {
				x.printStackTrace();
			}
			System.err.println(System.currentTimeMillis()-now);
			String uploadUI = String.format("<a href='%s%s/%s' target='upload' title='Upload training and test datasets and related documents''>%s</a>",
					uriReporter.getBaseReference(),Resources.editor,item.getIdentifier(),"Add attachment(s)");
			output.write(String.format("<div id='Attachments'><span class='summary'>N/A<br>%s</span></div>",uploadUI));
			
			output.write("\n</div>\n"); //tabs		
			
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(request);
	}

}
