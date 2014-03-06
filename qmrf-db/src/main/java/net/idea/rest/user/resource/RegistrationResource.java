package net.idea.rest.user.resource;

import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import net.idea.modbcum.i.IQueryObject;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.modbcum.p.QueryExecutor;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.client.Resources.Config;
import net.idea.rest.QMRFCatalogResource;
import net.idea.rest.QMRFFreeMarkerApplicaton;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.task.RegistrationTaskHTMLReporter;
import net.idea.rest.user.QMRFCallableUserCreator;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;
import net.idea.restnet.user.resource.UserURIReporter;

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

public class RegistrationResource extends QMRFCatalogResource<DBUser> {
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
	public void configureTemplateMap(Map<String, Object> map) {
	
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
		map.put(Resources.Config.qmrf_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_email.name()));
		map.put("creator","Ideaconsult Ltd.");
	    map.put("qmrf_root",getRequest().getRootRef());
	    map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name()));
	    map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
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
			DBConnection dbc = new QDBConnection(getApplication().getContext(),getDbConfig());
			conn = dbc.getConnection();
			
			QueryExecutor qx = null;
			ResultSet rs = null;
			boolean found = false;
			try {
				String userName = form.getFirstValue("username");
				DBUser user = new DBUser();
				user.setUserName(userName);
				ReadUser q = new ReadUser(user);
				qx = new QueryExecutor<IQueryObject>();
				qx.setConnection(conn);
				qx.setCloseConnection(false);
				rs = qx.process(q);
				while (rs.next()) {
					user = q.getObject(rs);
					found = true;
				}
			} catch (Exception x) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);	
			} finally {
				try { rs.close();} catch (Exception x) {}
				try { qx.close();} catch (Exception x) {}
			}

			if (found) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"This username already exist.");
			
			return new QMRFCallableUserCreator(method,item,reporter, form,getRequest().getRootRef().toString(),
					conn,getToken(),false,usersdbname==null?"tomcat_users":usersdbname);
			
		} catch (ResourceException x) {
			try { conn.close(); } catch (Exception xx) {}
			throw x;
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	}
	
	public Properties getDbConfig() {
		return ((QMRFFreeMarkerApplicaton)getApplication()).getDbConfig();
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.register);
	}
	
	@Override
	public IProcessor<Iterator<DBUser>, Representation> createJSONConvertor(
			Variant variant, String filenamePrefix) throws AmbitException,
			ResourceException {
		return super.createJSONConvertor(variant, filenamePrefix);
	}
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF<Object>(storage,getHTMLBeauty()) {
			@Override
			public synchronized Reporter<Iterator<UUID>, Writer> createTaskReporterHTML(
					Request request,ResourceDoc doc,HTMLBeauty htmlbeauty) throws AmbitException, ResourceException {
				return	new RegistrationTaskHTMLReporter(storage,request,doc,htmlbeauty);
			}			
		};
	}
	
	@Override
	protected String getTaskTitle(DBUser item, Reference source) {
		return "New user registration";
	}
}
