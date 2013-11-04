package net.idea.rest.user;

import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.p.ProcessorException;
import net.idea.restnet.i.task.TaskResult;
import net.idea.restnet.user.CallableUserCreator;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;
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
		
		if (!passwordChange && Method.POST.equals(method) && (!"Accept".equals(input.getFirstValue("privacy")))) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Privacy statement not accepted");
		}
		
		if (credentials!=null) {
			if (credentials.getNewpwd()!=null && credentials.getOldpwd()!=null) { 
				if (!passwordChange && !credentials.getNewpwd().equals(credentials.getOldpwd())) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				if (credentials.getNewpwd().length()<8)throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			}
		}
		return user;
	}
	@Override
	protected String getConfig() {
		return "config/qmrf.properties";
	}
	
	@Override
	public TaskResult doCall() throws Exception {
		boolean save = connection.getAutoCommit();
		try {
			connection.setAutoCommit(isAutoCommit());
			DBUser target = getTarget(input);
			IQueryUpdate<? extends Object,DBUser> q = createUpdate(target);
			if (q!= null) {
				executeQuery(q);
				
				if (!isAutoCommit())
					connection.commit();
				
				if (Method.DELETE.equals(method)) 
					return new TaskResult(getURI(target,Method.DELETE),false);
				else
					return new TaskResult(getURI(target,method),isNewResource());
			} else
				return new TaskResult(getURI(target,method),false);
		} catch (ResourceException x) {
			x.printStackTrace();
			if (!isAutoCommit())
				try { connection.rollback();} catch (Exception xx) {}
				throw x;
		} catch (ProcessorException x) {
			if (!isAutoCommit())
				try { connection.rollback();} catch (Exception xx) {}
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);

		} catch (Exception x) {
			if (!isAutoCommit())
				try {connection.rollback();} catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		} finally {
			try {exec.close();} catch (Exception x) {}
			try {connection.setAutoCommit(save);} catch (Exception x) {}
			try {connection.close();} catch (Exception x) {}
		}
	}
}
