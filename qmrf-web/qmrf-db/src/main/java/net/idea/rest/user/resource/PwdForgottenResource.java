package net.idea.rest.user.resource;

import java.sql.Connection;

import net.idea.qmrf.client.Resources.Config;
import net.idea.rest.QMRFFreeMarkerApplicaton;
import net.idea.rest.db.CustomDBConnection;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class PwdForgottenResource extends RegistrationResource {

	@Override
	public String getTemplateName() {
		return "pwd_forgotten.ftl";
	}

	@Override
	protected ICallableTask createCallable(Method method, Form form, DBUser item)
			throws ResourceException {
		Connection conn = null;
		try {
			String usersdbname = getContext().getParameters().getFirstValue(Config.users_dbname.name());
			UserURIReporter reporter = new UserURIReporter(getRequest(),"");
			CustomDBConnection dbc = new CustomDBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			return new CallablePasswordReset(method,null,reporter, form,getRequest().getRootRef().toString(),
					conn,getToken(),usersdbname==null?"tomcat_users":usersdbname);
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	}
	
	public String getConfigFile() {
		return ((QMRFFreeMarkerApplicaton)getApplication()).getConfigFile();
	}


}
