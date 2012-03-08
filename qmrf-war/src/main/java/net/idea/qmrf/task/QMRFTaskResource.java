package net.idea.qmrf.task;

import java.io.Writer;
import java.util.Iterator;
import java.util.UUID;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.TaskResource;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;

import org.restlet.Request;
import org.restlet.resource.ResourceException;

public class QMRFTaskResource extends TaskResource<String> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
	
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF<Object>(storage,getHTMLBeauty()) {
			@Override
			public synchronized Reporter<Iterator<UUID>, Writer> createTaskReporterHTML(
					Request request,ResourceDoc doc,HTMLBeauty htmlbeauty) throws AmbitException, ResourceException {
				return	new QMRFTaskHTMLReporter<Object>(storage,request,doc,htmlbeauty);
			}			
		};
	}
}
