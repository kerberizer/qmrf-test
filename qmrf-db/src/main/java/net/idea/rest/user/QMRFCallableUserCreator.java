package net.idea.rest.user;

import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.p.ProcessorException;
import net.idea.restnet.i.task.TaskResult;
import net.idea.restnet.user.CallableUserCreator;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class QMRFCallableUserCreator extends CallableUserCreator {
	private static final String[] numbers = {"0","1","2","3","4","5","6","7","8","9"};
	private static final String[] abc = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","W","X","Y","Z"};
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
				if (credentials.getNewpwd().length()<12) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				int nums = 0; //at least one number
				for (int i=0;i<numbers.length;i++) if (credentials.getNewpwd().indexOf(numbers[i])>=0) nums++;
				if (nums<0) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
				nums = 0; //at least one uppercase
				for (int i=0;i<abc.length;i++) if (credentials.getNewpwd().indexOf(abc[i])>=0) nums++;
				if (nums<0) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
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
