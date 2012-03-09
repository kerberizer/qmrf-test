package net.idea.rest;

import java.io.Serializable;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

public abstract class QMRFQueryResource<Q extends IQueryRetrieval<T>,T extends Serializable> extends QueryResource<Q,T>{
	protected boolean headless = false;
	
	public QMRFQueryResource() {
		super();
		
	}
	
	protected String getQueryService() {
		return ((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name());
	}
	protected String getAttachmentDir() {
		String dir = ((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_attachments_dir.name());
		return dir==null?System.getProperty("java.io.tmpdir"):dir;
	}
	public boolean isHeadless() {
		return headless;
	}


	public void setHeadless(boolean headless) {
		this.headless = headless;
	}


	@Override
	public String getConfigFile() {
		return "conf/qmrf-db.pref";
	}
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		headless = getHeadlessParam();
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
	
	protected boolean getHeadlessParam() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		try {
			return Boolean.parseBoolean(form.getFirstValue("headless").toString());
		} catch (Exception x) {
			return false;
		}	
	}
	
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF(storage,getHTMLBeauty());
	}
	
	protected abstract QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException;
}
