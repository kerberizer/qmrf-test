package net.idea.rest.user;

import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.user.CallableUserCreator;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class QMRFCallableUserCreator extends CallableUserCreator {
	
	protected String getSenderName() { return "The QMRF Database Support Team"; };
	@Override
	protected String getSender() {
		return "JRC-IHCP-COMPUTOX@ec.europa.eu";
	}
	@Override
	protected String getSystemName() {
		return "QMRF Database";
	}
	public QMRFCallableUserCreator(Method method,DBUser item,
						UserURIReporter<IQueryRetrieval<DBUser>> reporter,
						Form input,
						String baseReference,
						Connection connection,
						String token,
						boolean passwordChange,
						String usersdbname)  {
		super(method,item,reporter, input,baseReference, connection,token,passwordChange,usersdbname);
		subject = "QMRF Database User Activation";
	}

	@Override
	protected DBUser getTarget(Form input) throws Exception {
		DBUser user = super.getTarget(input);
		if (credentials!=null) {
			if (credentials.getNewpwd()==null || credentials.getOldpwd()==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			if (!credentials.getNewpwd().equals(credentials.getOldpwd())) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			if (credentials.getNewpwd().length()<8)throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
		return user;
	}
	@Override
	protected String getConfig() {
		return "config/qmrf.properties";
	}
}
