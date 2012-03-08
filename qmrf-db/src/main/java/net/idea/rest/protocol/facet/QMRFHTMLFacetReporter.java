package net.idea.rest.protocol.facet;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.db.facet.FacetURIReporter;

import org.restlet.Request;

public class QMRFHTMLFacetReporter extends QMRFHTMLReporter<EndpointProtocolFacet,IQueryRetrieval<EndpointProtocolFacet>> {
	protected String endpoint = null;
	public QMRFHTMLFacetReporter(Request request, boolean collapsed,ResourceDoc doc, HTMLBeauty htmlBeauty) {
		super(request,collapsed,doc,htmlBeauty);
		
	}
	
	@Override
	protected void printPageNavigator(
			IQueryRetrieval<EndpointProtocolFacet> query) throws Exception {
		//super.printPageNavigator(query);
	}
	@Override
	protected boolean printAsTable() {
		return true;
	}
	@Override
	protected void printTableHeader(Writer w) throws Exception {
		try {
			w.write(String.format("<div class='ui-widget' style='margin-top:18px'><p><strong>%s<strong></p></div>","QMRF documents by endpoints"));
			//w.write("<div class='protocol'>");
			//w.write("<div class='tabs'>");
		} catch (Exception x) {
			x.printStackTrace();
		}
		
	}


	@Override
	protected QueryURIReporter createURIReporter(Request request,
			ResourceDoc doc) {
		return new FacetURIReporter<IQueryRetrieval<IFacet>>(request);
	}



	@Override
	protected void printTable(Writer output, String uri,
			EndpointProtocolFacet item) {
		try {
			if ((endpoint==null) || !endpoint.equals(item.getProperty1().trim())) {
				if (endpoint!=null) {
					output.write(printWidgetContentFooter());
					output.write(printWidgetFooter());
				}
				output.write(printWidgetHeader("".equals(item.getProperty1())?"Undefined endpoint":item.getProperty1()));
				endpoint = item.getProperty1().trim();
				output.write(printWidgetContentHeader(""));
			}
			
			String d = uri.indexOf("?")>0?"&":"?";
			output.write(printWidgetContentContent(String.format(
						"<a href=\"%s%spage=0&pagesize=10\">%s&nbsp;%s</a>&nbsp;(%d)<br>",
						uri,d,item.getProperty2().trim(),item.getValue().toString().trim(),
						item.getCount())));
		} catch (Exception x) {
			x.printStackTrace();
		}

		
	
		
	}
	
	
	
	@Override
	public void footer(Writer w,
			IQueryRetrieval<EndpointProtocolFacet> query) {
		try {
			w.write("<br>");
			w.write("</div>");
			//w.write(String.format("</div>")); //accordion
			w.write("</div>");
		} catch (Exception x) {}
		super.footer(w, query);
	}

	@Override
	protected void printForm(Writer output, String uri,
			EndpointProtocolFacet item, boolean editable) {
		
	}

}
