package net.idea.rest;

import java.io.Serializable;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.attachments.AttachmentHTMLReporter;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

public abstract class QMRFQueryResource<Q extends IQueryRetrieval<T>,T extends Serializable> extends QueryResource<Q,T>{
	protected boolean headless = false;
	
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
	
	
	protected abstract QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException;
}
