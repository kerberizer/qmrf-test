package net.idea.rest.user;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.p.ProcessorException;
import net.idea.rest.JSONUtils;
import net.idea.restnet.i.task.TaskResult;
import net.idea.restnet.resources.Resources;
import net.idea.restnet.u.UserRegistration;
import net.idea.restnet.user.CallableUserCreator;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.CreateUser;
import net.idea.restnet.user.db.UpdateCredentials;
import net.idea.restnet.user.db.UpdateUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class QMRFCallableUserCreator extends CallableUserCreator {
    protected transient Logger logger = Logger.getLogger(getClass().getName());
    private static final String[] numbers = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    private static final String[] abc = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
	    "P", "Q", "R", "S", "T", "U", "W", "X", "Y", "Z" };

    protected String getSenderName() {
	return "The QMRF Database Support Team";
    };

    @Override
    protected String getSender() {
	return "JRC-COMPUTOX@ec.europa.eu";
    }

    @Override
    protected String getSystemName() {
	return "QMRF Database";
    }

    public QMRFCallableUserCreator(Method method, DBUser item, UserURIReporter<IQueryRetrieval<DBUser>> reporter,
	    Form input, String baseReference, Connection connection, String token, boolean passwordChange,
	    String usersdbname) {
	super(method, item, reporter, input, baseReference, connection, token, passwordChange, usersdbname);
	subject = "QMRF Database User Activation";
    }

    @Override
    protected DBUser getTarget(Form input) throws Exception {
	DBUser user = super.getTarget(input);

	if (!passwordChange) {
	    if (Method.POST.equals(method) && (!"Accept".equals(input.getFirstValue("privacy"))))
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Privacy statement not accepted");
	    if (Method.PUT.equals(method)) {
		user.setUserName(this.user.getUserName());
	    }
	    if (Method.DELETE.equals(method)) {
		if (user.getUserName() != this.user.getUserName())
		    throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
	    }
	    if (!JSONUtils.acceptString(user.getUserName()))
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
	}

	if (credentials != null) {
	    if (credentials.getNewpwd() != null && credentials.getOldpwd() != null) {
		if (!passwordChange && !credentials.getNewpwd().equals(credentials.getOldpwd()))
		    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		if (!JSONUtils.acceptString(credentials.getNewpwd()))
		    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		if (credentials.getNewpwd().length() < 12)
		    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		int nums = 0; // at least one number
		for (int i = 0; i < numbers.length; i++)
		    if (credentials.getNewpwd().indexOf(numbers[i]) >= 0)
			nums++;
		if (nums < 0)
		    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		nums = 0; // at least one uppercase
		for (int i = 0; i < abc.length; i++)
		    if (credentials.getNewpwd().indexOf(abc[i]) >= 0)
			nums++;
		if (nums < 0)
		    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
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
	    IQueryUpdate<? extends Object, DBUser> q = createUpdate(target);
	    if (q != null) {
		executeQuery(q);

		if (!isAutoCommit())
		    connection.commit();

		if (Method.DELETE.equals(method))
		    return new TaskResult(getURI(target, Method.DELETE), false);
		else
		    return new TaskResult(getURI(target, method), isNewResource());
	    } else
		return new TaskResult(getURI(target, method), false);
	} catch (ResourceException x) {
	    logger.log(Level.SEVERE, method.getName() + "\t " + x.getMessage());
	    if (!isAutoCommit())
		try {
		    connection.rollback();
		} catch (Exception xx) {
		}
	    throw x;
	} catch (ProcessorException x) {
	    logger.log(Level.SEVERE, method.getName() + "\t " + x.getMessage());
	    if (!isAutoCommit())
		try {
		    connection.rollback();
		} catch (Exception xx) {
		}
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, x);
	} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException x) {
	    logger.log(Level.SEVERE, method.getName() + "\t " + x.getMessage());
	    if (!isAutoCommit())
		try {
		    connection.rollback();
		} catch (Exception xx) {
		}
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, x);
	} catch (com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException x) {
	    logger.log(Level.SEVERE, method.getName() + "\t " + x.getMessage());
	    if (!isAutoCommit())
		try {
		    connection.rollback();
		} catch (Exception xx) {
		}
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, x);
	} catch (Exception x) {
	    logger.log(Level.SEVERE, method.getName() + "\t " + x.getMessage());
	    if (!isAutoCommit())
		try {
		    connection.rollback();
		} catch (Exception xx) {
		}
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, x);
	} finally {
	    try {
		exec.close();
	    } catch (Exception x) {
	    }
	    try {
		connection.setAutoCommit(save);
	    } catch (Exception x) {
	    }
	    try {
		connection.close();
	    } catch (Exception x) {
	    }
	}
    }

    @Override
    protected IQueryUpdate<? extends Object, DBUser> createUpdate(DBUser user) throws Exception {
	if (passwordChange)
	    return new UpdateCredentials(credentials, user, getDatabaseName());
	if (Method.POST.equals(method)) {
	    registration = new UserRegistration();
	    return new CreateUser(user, registration, getDatabaseName());
	} else if (Method.DELETE.equals(method))
	    return new QMRFDeleteUser(user, getDatabaseName());
	else if (Method.PUT.equals(method))
	    return new UpdateUser(user);
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    public String toString() {
	if (passwordChange)
	    return String.format("Password change");
	else if (Method.POST.equals(method)) {
	    return String.format("Create user");
	} else if (Method.PUT.equals(method)) {
	    return String.format("Update user");
	} else if (Method.DELETE.equals(method)) {
	    return String.format("Delete user");
	}
	return "Read user";
    }

    @Override
    protected String getURI(DBUser target, Method method) throws Exception {
	if (Method.DELETE.equals(method)) {
	    return String.format("%s%s", baseReference, Resources.user);
	} else
	    return super.getURI(target, method);
    }
}
