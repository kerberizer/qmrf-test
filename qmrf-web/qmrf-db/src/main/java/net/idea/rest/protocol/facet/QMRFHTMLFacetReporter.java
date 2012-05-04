package net.idea.rest.protocol.facet;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.endpoints.EndpointsResource;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
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
			w.write(String.format("<div class='ui-widget' style='margin-top:18px'><p><strong>%s<strong><span style='float:right;'><a href='%s%s'>%s</a></span></p></div>",
					"QMRF documents by endpoints",
					uriReporter.getBaseReference(),
					EndpointsResource.resource,
					"Endpoints catalog"
					));
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
					output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidgetContentFooter());
					output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidgetFooter());
				}
				output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidgetHeader(
						(item.getProperty1()==null) || "".equals(item.getProperty1())?
								"Undefined endpoint":item.getProperty1()));
				endpoint = item.getProperty1()==null?"Undefined endpoint":item.getProperty1().trim();
				output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidgetContentHeader(""));
			}
			
			String d = uri==null?"":uri.indexOf("?")>0?"&":"?";
			output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidgetContentContent(String.format(
						"<a href=\"%s%spage=0&pagesize=10\">%s&nbsp;%s</a>&nbsp;(%d)<br>",
						uri==null?"":uri,d,
						item.getProperty2()==null?"":item.getProperty2().trim(),
						item.getValue()==null?"Undefined endpoint":item.getValue().toString().trim(),
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
