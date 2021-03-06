package net.idea.rest.user.resource;

import java.sql.Connection;
import java.util.Map;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.exceptions.NotFoundException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.p.UpdateExecutor;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.client.Resources.Config;
import net.idea.rest.QMRFQueryResource;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.user.QMRFReadRegistration;
import net.idea.rest.user.QMRFUserRegistration;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.u.RegistrationJSONReporter;
import net.idea.restnet.u.UserRegistration;
import net.idea.restnet.u.db.ConfirmRegistration;
import net.idea.restnet.u.db.ReadRegistration;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class RegistrationConfirmResource extends  QMRFQueryResource<ReadRegistration,UserRegistration> {
	public static String confirmationCode = "code";
	public RegistrationConfirmResource() {
		super();
		setHtmlbyTemplate(true);
	}
	
	@Override
	public String getTemplateName() {
		return "register_confirm.ftl";
	}
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless)
			throws ResourceException {
		return null;
	}
	
	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		Map<String, Object> map =  super.getMap(variant);
		Object code = getRequest().getResourceRef().getQueryAsForm().getFirstValue(confirmationCode);
		if (code!=null) map.put("qmrf_reg_confirmed", code);
		map.put("searchURI",Resources.confirm);
		return map;
	}

	@Override
	public IProcessor<ReadRegistration, Representation> createConvertor(
			Variant variant) throws AmbitException, ResourceException {
		RegistrationJSONReporter r = new RegistrationJSONReporter(getRequest());
		return new StringConvertor(	r,MediaType.APPLICATION_JSON,"");
	}

	@Override
	protected ReadRegistration createQuery(Context context, Request request,
			Response response) throws ResourceException {
		Object code = getRequest().getResourceRef().getQueryAsForm().getFirstValue(confirmationCode);
		if (code==null) return null;
		ReadRegistration q = new QMRFReadRegistration(new QMRFUserRegistration(code.toString()));
		String usersdbname = getContext().getParameters().getFirstValue(Config.users_dbname.name());
		q.setDatabaseName(usersdbname==null?"tomcat_users":usersdbname);
		return q;
	}
	
	@Override
	protected Representation getRepresentation(Variant variant)
			throws ResourceException {
		//confirm the registration only if JSON requested; 
		//this means it is either requested by JS enabled browser or by a knowledgeable client :)
	
		if (MediaType.APPLICATION_JSON.equals(variant.getMediaType())) {
			Connection conn = null;
			UpdateExecutor exec = null;
			Object code = getRequest().getResourceRef().getQueryAsForm().getFirstValue(confirmationCode);
			if (code!=null) 
			try {
				String usersdbname = getContext().getParameters().getFirstValue(Config.users_dbname.name());
				if (usersdbname==null) usersdbname = "tomcat_users";
				UserURIReporter reporter = new UserURIReporter(getRequest(),"");
				DBConnection dbc = new QDBConnection(getApplication().getContext(),getDbConfig());
				conn = dbc.getConnection();
				UserRegistration reg = new  QMRFUserRegistration(code.toString());
				((QMRFUserRegistration)reg).setTitle("Confirm registration");
				ConfirmRegistration q = new ConfirmRegistration(reg);
				q.setDatabaseName(usersdbname);
				exec = new UpdateExecutor();
				exec.setConnection(conn);
				exec.process(q);
			} catch (Exception x) {
				try {if (conn!=null) {conn.close(); conn=null;} } catch (Exception xx) {}
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
			} finally {
				try {exec.close();} catch (Exception x) {}
				try {if (conn!=null) conn.close();} catch (Exception x) {}				
			}
		}
		return super.getRepresentation(variant);
	}
	
	@Override
	protected Representation processNotFound(NotFoundException x,
			Variant variant) throws Exception {
		return null;
	}

}
