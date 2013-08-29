package net.idea.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

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
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public abstract class QMRFQueryResource<Q extends IQueryRetrieval<T>,T extends Serializable> extends QueryResource<Q,T>{
	protected boolean headless = false;
	protected QMRF_HTMLBeauty htmlBeauty;
	protected transient Logger logger = Logger.getLogger(getClass().getName());
	protected Form params;
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
	
	protected Map<String, Object> getMap(Variant variant) throws ResourceException {
		   Map<String, Object> map = new HashMap<String, Object>();

			map.put("managerRole", "false");
			map.put("editorRole", "false");
			if (getClientInfo()!=null) {
				if (getClientInfo().getUser()!=null)
					map.put("username", getClientInfo().getUser().getIdentifier());
				if (getClientInfo().getRoles()!=null) {
					if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.managerRole)>=0)
						map.put("managerRole", "true");
					if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.editorRole)>=0)
						map.put("editorRole", "true");
				}
			}

		        map.put("creator","IdeaConsult Ltd.");
		        map.put(Resources.Config.qmrf_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_email.name()));
		        map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
		        map.put(Resources.Config.qmrf_template.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_template.name()));
		        map.put(Resources.Config.qmrf_manual.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_manual.name()));
		        map.put(Resources.Config.qmrf_faq.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_faq.name()));
		        map.put(Resources.Config.qmrf_oecd.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_oecd.name()));
		        map.put(Resources.Config.qmrf_jrc.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_jrc.name()));
		        map.put("searchURI",htmlBeauty==null || htmlBeauty.getSearchURI()==null?"":htmlBeauty.getSearchURI());
		        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name()));
		        //remove paging
		        Form query = getRequest().getResourceRef().getQueryAsForm();
		        //query.removeAll("page");query.removeAll("pagesize");query.removeAll("max");
		        query.removeAll("media");
		        Reference r = getRequest().getResourceRef().clone();
		        r.setQuery(query.getQueryString());
		        map.put("qmrf_root",getRequest().getRootRef().toString()) ;
		        map.put("qmrf_request",r.toString()) ;
		        if (query.size()>0)
		        	map.put("qmrf_query",query.getQueryString()) ;
		        //json
		        query.removeAll("media");query.add("media", MediaType.APPLICATION_JSON.toString());
		        r.setQuery(query.getQueryString());
		        map.put("qmrf_request_json",r.toString());
		        //csv
		        query.removeAll("media");query.add("media", MediaType.TEXT_CSV.toString());
		        r.setQuery(query.getQueryString());
		        map.put("qmrf_request_csv",r.toString());
		        return map;
	}

	@Override
	protected Representation getHTMLByTemplate(Variant variant) throws ResourceException {
		getHTMLBeauty();
        return toRepresentation(getMap(variant), getTemplateName(), MediaType.TEXT_PLAIN);
	}
	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Form headers = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getRequest().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Frame-Options", "SAMEORIGIN");
		return super.get(variant);
	}

	@Override
	protected Form getParams() {
		if (params == null) 
			if (Method.GET.equals(getRequest().getMethod())) {
				params = getResourceRef(getRequest()).getQueryAsForm();
				Iterator<Parameter> p = params.iterator();
				while (p.hasNext()) {
					Parameter param = p.next();
					String value = param.getValue();
					if (value==null) continue;
					if (value.contains("script") || value.contains(">") || value.contains("<")) param.setValue(""); 
					else param.setValue(value.replace("'","&quot;"));	
				}
			}	
			//if POST, the form should be already initialized
			else params = getRequest().getEntityAsForm();
		return params;
	}
}
