package net.idea.rest.user.resource;

import java.io.Writer;
import java.sql.Connection;
import java.util.Iterator;
import java.util.UUID;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources.Config;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.task.UserTaskHTMLReporter;
import net.idea.rest.user.QMRFCallableUserCreator;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.db.DBConnection;
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
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class PwdResetResource<T> extends MyAccountResource<T> {

	@Override
	public boolean isHtmlbyTemplate() {
		return true;
	}
	@Override
	public String getTemplateName() {
		return "pwd_body.ftl";
	}
	
	@Override
	protected ReadUser<T> createUpdateQuery(Method method, Context context,
			Request request, Response response) throws ResourceException {
		if (Method.PUT.equals(method)) {
			return createQuery(context, request, response);
		}
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	
	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			Form form, DBUser item) throws ResourceException {
		Connection conn = null;
		try {
			if (item.getUserName()==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			if ((getClientInfo()!=null) && 
				(getClientInfo().getUser()!=null) &&
				(getClientInfo().getUser().getIdentifier()!=null) && 
				getClientInfo().getUser().getIdentifier().equals(item.getUserName())
				) {
				String usersdbname = getContext().getParameters().getFirstValue(Config.users_dbname.name());

				UserURIReporter reporter = new UserURIReporter(getRequest(),"");
				DBConnection dbc = new QDBConnection(getApplication().getContext(),getDbConfig());
				conn = dbc.getConnection();
				return new QMRFCallableUserCreator(
							method,
							item,
							reporter, 
							form,
							getRequest().getRootRef().toString(),
							conn,
							getToken(),
							true,
							usersdbname==null?"tomcat_users":usersdbname
							);
			} 
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN);
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	}
	
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF<Object>(storage,getHTMLBeauty()) {
			@Override
			public synchronized Reporter<Iterator<UUID>, Writer> createTaskReporterHTML(
					Request request,ResourceDoc doc,HTMLBeauty htmlbeauty) throws AmbitException, ResourceException {
				UserTaskHTMLReporter reporter =	new UserTaskHTMLReporter(storage,request,doc,htmlbeauty);
				reporter.setTitle("Pasword reset");
				return reporter;
			}			
		};
	}
}
