package net.idea.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.i.task.Task;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public abstract class QMRFQueryResource<Q extends IQueryRetrieval<T>,T extends Serializable> extends QueryResource<Q,T>{
	protected boolean headless = false;
	protected QMRF_HTMLBeauty htmlBeauty;
	
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
		if (htmlBeauty==null) htmlBeauty = new QMRF_HTMLBeauty(Resources.protocol);
		return htmlBeauty;
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
	
	@Override
	protected Task<Reference, Object> addTask(
			ICallableTask callable,
			T item,
			Reference reference) throws ResourceException {

			return ((TaskApplication)getApplication()).addTask(
				String.format("%s %s %s",
						callable.toString(),
						item==null?"":item.toString(),
						reference==null?"":" "),									
				callable,
				getRequest().getRootRef(),
				getToken());		
		
	}
	
	
	@Override
	protected Representation getHTMLByTemplate(Variant variant) throws ResourceException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (getClientInfo().getUser()!=null) 
        	map.put("username", getClientInfo().getUser().getIdentifier());
	map.put("managerRole", "false");
	map.put("editorRole", "false");
	if ((getClientInfo()!=null)&& (getClientInfo().getRoles()!=null)) {
		if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.managerRole)>=0)
			map.put("managerRole", "true");
		if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.editorRole)>=0)
			map.put("editorRole", "true");
	}

        map.put("creator","IdeaConsult Ltd.");
        map.put(Resources.Config.qmrf_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_email.name()));
        map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
        map.put(Resources.Config.qmrf_template.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_template.name()));
        map.put(Resources.Config.qmrf_manual.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_manual.name()));
        map.put(Resources.Config.qmrf_faq.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_faq.name()));
        map.put(Resources.Config.qmrf_oecd.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_oecd.name()));
        map.put(Resources.Config.qmrf_jrc.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_jrc.name()));
        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name()));
        getRequest().getResourceRef().addQueryParameter("media", MediaType.APPLICATION_JSON.toString());
	//todo remove paging
        map.put("qmrf_request",getRequest().getResourceRef().toString());

        return toRepresentation(map, getTemplateName(), MediaType.TEXT_PLAIN);
	}

	
}
