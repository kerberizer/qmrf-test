package net.idea.qmrf.aa;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.qmrf.admin.QMRFCatalogHTMLReporter;
import net.idea.restnet.aa.opensso.users.OpenSSOUserResource;
import net.idea.restnet.c.html.HTMLBeauty;


public class QMRFLoginResource extends OpenSSOUserResource {
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
	
	protected Reporter createHTMLReporter() {
		return new QMRFCatalogHTMLReporter<DBProtocol>(getRequest(),getDocumentation(),getHTMLBeauty(),"QMRF");
	}
}
