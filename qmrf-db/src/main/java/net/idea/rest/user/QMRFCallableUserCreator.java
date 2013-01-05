package net.idea.rest.user;

import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.user.CallableUserCreator;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.data.Form;
import org.restlet.data.Method;

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
	protected String getConfig() {
		return "config/qmrf.properties";
	}
}
