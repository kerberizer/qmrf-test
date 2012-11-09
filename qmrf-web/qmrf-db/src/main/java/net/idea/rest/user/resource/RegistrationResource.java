package net.idea.rest.user.resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.idea.qmrf.client.Resources;
import net.idea.qmrf.client.Resources.Config;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.user.CallableUserCreator;
import net.idea.rest.user.DBUser;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.i.task.ICallableTask;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class RegistrationResource extends CatalogResource<DBUser> {
	protected List<DBUser> dummyuser = new ArrayList<DBUser>();
	public RegistrationResource() {
		super();
		setHtmlbyTemplate(true);
		dummyuser.add(null);
	}
	
	@Override
	protected Iterator<DBUser> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		return dummyuser.iterator();
	}

	@Override
	public String getTemplateName() {
		return "register.ftl";
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
	protected Reference getSourceReference(Form form, DBUser model)
			throws ResourceException {
		return null;
	}
	
	@Override
	protected Representation put(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected Representation delete(Variant variant) throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	@Override
	protected ICallableTask createCallable(Method method, Form form, DBUser item)
			throws ResourceException {
		Connection conn = null;
		try {
			String usersdbname = getContext().getParameters().getFirstValue(Config.users_dbname.name());
			UserURIReporter reporter = new UserURIReporter(getRequest(),"");
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			return new CallableUserCreator(method,item,reporter, form,getRequest().getRootRef().toString(),
					conn,getToken(),false,usersdbname==null?"tomcat_users":usersdbname);
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	}
	
	public String getConfigFile() {
		return "conf/qmrf-db.pref";
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.register);
	}
	
	
}