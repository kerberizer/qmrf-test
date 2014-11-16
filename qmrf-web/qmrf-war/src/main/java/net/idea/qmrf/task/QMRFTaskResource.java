package net.idea.qmrf.task;

import java.io.Writer;
import java.util.Iterator;
import java.util.UUID;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.rest.QMRFApplication;
import net.idea.qmrf.rest.QMRFStatusService.REPORT_LEVEL;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.task.QMRFTaskHTMLReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.TaskResource;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.ServerInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class QMRFTaskResource extends TaskResource<String> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.protocol);
	}
	
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		
		final REPORT_LEVEL reportLevel = ((QMRFApplication) getApplication()).getStatusReportLevel();
		return new FactoryTaskConvertorRDF<Object>(storage,getHTMLBeauty()) {
			@Override
			public synchronized Reporter<Iterator<UUID>, Writer> createTaskReporterHTML(
					Request request,ResourceDoc doc,HTMLBeauty htmlbeauty) throws AmbitException, ResourceException {
				return	new QMRFTaskHTMLReporter<Object>(storage,request,doc,htmlbeauty,reportLevel.equals(REPORT_LEVEL.debug));
			}			
		};
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getResponse().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Frame-Options", "SAMEORIGIN");
		ServerInfo si = getResponse().getServerInfo();si.setAgent("Restlet");getResponse().setServerInfo(si);
		return super.get(variant);
	}

	@Override
	protected Representation get() throws ResourceException {
		Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getResponse().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Frame-Options", "SAMEORIGIN");
		ServerInfo si = getResponse().getServerInfo();si.setAgent("Restlet");getResponse().setServerInfo(si);
		return super.get();
	}

}
