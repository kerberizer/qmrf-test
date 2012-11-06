package net.idea.rest.user.resource;

import java.util.Iterator;
import java.util.Map;

import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.user.DBUser;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.resource.CatalogResource;
import net.idea.restnet.i.task.ICallableTask;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.resource.ResourceException;

public class RegistrationResource extends CatalogResource<DBUser> {

	public RegistrationResource() {
		super();
		setHtmlbyTemplate(true);
	}
	

	@Override
	public String getTemplateName() {
		return "register.ftl";
	}
	@Override
	protected Iterator<DBUser> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		return null;
	}
	
	@Override
	protected void configureTemplateMap(Map<String, Object> map) {
	
		map.put("searchURI",Resources.register);
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
		map.put("creator","Ideaconsult Ltd.");
	    map.put("qmrf_root",getRequest().getRootRef());
	    map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name()));
	    
	}
	
	@Override
	protected ICallableTask createCallable(Method method, Form form, DBUser item)
			throws ResourceException {
		// TODO create user entry with unconfirmed status
		return super.createCallable(method, form, item);
	}

}
