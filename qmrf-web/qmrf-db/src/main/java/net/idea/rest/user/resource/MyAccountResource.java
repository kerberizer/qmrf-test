package net.idea.rest.user.resource;

import java.util.Map;
import java.util.logging.Level;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.client.Resources.Config;
import net.idea.rest.protocol.UserHTMLBeauty;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class MyAccountResource<T> extends UserDBResource<T> {

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		editable = false;
		singleItem = true;
		setHtmlbyTemplate(true);
	}
	
	@Override
	public boolean isHtmlbyTemplate() {
		return headless?false:freeMarkerSupport.isHtmlbyTemplate();
	}
	@Override
	public String getTemplateName() {
		return "myprofile_body.ftl";
	}
	
	@Override
	protected ReadUser createQuery(Context context, Request request, Response response)
			throws ResourceException {
		String search_name = null;
		Object search_value = null;

		try {
			search_name = "username";
			search_value = getClientInfo().getUser().getIdentifier();
		} catch (Exception x) {
			search_value = null;
			logger.log(Level.WARNING,x.getMessage(),x);
		}				
		if (search_value == null) throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED,"Not logged in!");
		Object key = request.getAttributes().get(UserDBResource.resourceKey);		
		try {
			return getUserQuery(key,search_name,search_value);
		}catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(
					Status.CLIENT_ERROR_BAD_REQUEST,
					String.format("Invalid protocol id %d",key),
					x
					);
		}
	} 
	

	@Override
	protected ReadUser<T> createUpdateQuery(Method method, Context context,
			Request request, Response response) throws ResourceException {
		if (Method.PUT.equals(method) || Method.DELETE.equals(method)) {
			return createQuery(context, request, response);
		}
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless)
			throws ResourceException {
		String usersdbname = getContext().getParameters().getFirstValue(Config.users_dbname.name());
		if (usersdbname==null) usersdbname = "tomcat_users";
		
		UserHTMLReporter rep = new UserHTMLReporter(getRequest(),!singleItem,editable,(UserHTMLBeauty)getHTMLBeauty(),usersdbname) {
			@Override
			public String getTitle() {
				return null;
			}
			@Override
			protected void printPageNavigator(IQueryRetrieval<DBUser> query)
					throws Exception {
			}
		};
		
		rep.setHeadless(headless);
		return rep;
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new UserHTMLBeauty() {
			@Override
			public String getSearchURI() {
				return Resources.myaccount;
			}
		};
	}
	
	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		Map<String, Object> map = super.getMap(variant);
		map.put("myprofile", true);
		map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
		return map;
	}
}
