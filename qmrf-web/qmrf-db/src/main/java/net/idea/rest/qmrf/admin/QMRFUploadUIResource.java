package net.idea.rest.qmrf.admin;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ResourceException;

public class QMRFUploadUIResource extends CatalogResource<DBProtocol> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1658864353613787327L;
	protected List<DBProtocol> items = new ArrayList<DBProtocol>();
	public static final String resource = "new";
	public boolean attachments = true;
	
	public QMRFUploadUIResource() {
		super();
	}
	@Override
	protected Iterator<DBProtocol> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		try { 
			attachments = Boolean.parseBoolean(getRequest().getResourceRef().getQueryAsForm().getFirstValue("attachments"));
		} catch (Exception x) {attachments = true;}
		
		return items.iterator();
	}
	protected Reporter createHTMLReporter() {
		return new QMRFCatalogHTMLReporter<DBProtocol>(getRequest(),getDocumentation(),getHTMLBeauty(),null) {
			@Override
			public void header(Writer w, Iterator<DBProtocol> query) {
				super.header(w, query);
				String uri = String.format("%s%s",getRequest().getRootRef().toString(), Resources.protocol);
				try {
					w.write(((QMRF_HTMLBeauty)htmlBeauty).printUploadForm(uri,uri, null,attachments));
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
			@Override
			protected String printPageNavigator() {
				return "";
			}
		};
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
}
