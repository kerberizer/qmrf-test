package net.idea.rest.user;

import java.net.URL;
import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.DBProject;
import net.idea.rest.groups.user.db.AddGroupsPerUser;
import net.idea.rest.user.db.CreateUser;
import net.idea.rest.user.db.DeleteUser;
import net.idea.rest.user.db.ReadUser;
import net.idea.rest.user.db.UpdateCredentials;
import net.idea.rest.user.db.UpdateUser;
import net.idea.rest.user.resource.UserURIReporter;
import net.idea.restnet.db.update.CallableDBUpdateTask;
import net.idea.restnet.u.UserCredentials;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class CallableUserCreator extends CallableDBUpdateTask<DBUser,Form,String> {
	protected UserURIReporter<IQueryRetrieval<DBUser>> reporter;
	protected DBUser user;
	protected boolean passwordChange;
	protected UserCredentials credentials;
	
	public CallableUserCreator(Method method,DBUser item,UserURIReporter<IQueryRetrieval<DBUser>> reporter,
						Form input,
						String baseReference,
						Connection connection,
						String token,
						boolean passwordChange)  {
		super(method, input,connection,token);
		this.reporter = reporter;
		this.user = item;
		this.baseReference = baseReference;
		this.passwordChange = passwordChange;
	}

	@Override
	protected DBUser getTarget(Form input) throws Exception {
		if (passwordChange) {
			if (Method.PUT.equals(method)) {
				credentials = new UserCredentials(
						input.getFirstValue("pwdold"),
						input.getFirstValue("pwd1")
						);
						
				return user;
			}
			else throw new Exception("User empty");
		}
		if (input==null) return user;
		
		DBUser user = new DBUser();
		if (Method.PUT.equals(method)) user.setID(this.user.getID());
		user.setUserName(input.getFirstValue(ReadUser.fields.username.name()));
		user.setFirstname(input.getFirstValue(ReadUser.fields.firstname.name()));
		user.setLastname(input.getFirstValue(ReadUser.fields.lastname.name()));
		user.setTitle(input.getFirstValue(ReadUser.fields.title.name()));
		user.setKeywords(input.getFirstValue(ReadUser.fields.keywords.name()));
		user.setEmail(input.getFirstValue(ReadUser.fields.email.name()));
		try {user.setHomepage(new URL(input.getFirstValue(ReadUser.fields.homepage.name()))); } catch (Exception x) {}
		try {user.setWeblog(new URL(input.getFirstValue(ReadUser.fields.weblog.name())));} catch (Exception x) {}
		
		String[] values = input.getValuesArray("organisation_uri");
		if (values != null)
			for (String value:values) try { 
				DBOrganisation org = new DBOrganisation();
				org.setResourceURL(new URL(value));
				org.setID(org.parseURI(baseReference));
				if (org.getID()>0) user.addOrganisation(org);
			} catch (Exception x) {}

		values = input.getValuesArray("project_uri");	
		if (values != null)
			for (String value:values) try { 
				DBProject org = new DBProject();
				org.setResourceURL(new URL(value));
				org.setID(org.parseURI(baseReference));
				if (org.getID()>0) user.addProject(org);
			} catch (Exception x) {}		
 		return user;
	}

	
	@Override
	protected IQueryUpdate<? extends Object, DBUser> createUpdate(DBUser user)
			throws Exception {
		if (passwordChange) return new UpdateCredentials(credentials,user);
		if (Method.POST.equals(method)) return  new CreateUser(user);
		else if (Method.DELETE.equals(method)) return  new DeleteUser(user);
		else if (Method.PUT.equals(method)) return new  UpdateUser(user);
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}

	@Override
	protected String getURI(DBUser user) throws Exception {
		return reporter.getURI(user);
	}

	
	@Override
	protected Object executeQuery(IQueryUpdate<? extends Object, DBUser> query)
			throws Exception {
		Object result = super.executeQuery(query);
		if (Method.POST.equals(method)) {
			DBUser user = query.getObject();
			if ((user.getOrganisations()!=null) && (user.getOrganisations().size()>0)) {
				AddGroupsPerUser q = new AddGroupsPerUser(user,user.getOrganisations());
				exec.process(q);
			}
			if ((user.getProjects()!=null) && (user.getProjects().size()>0)) {
				AddGroupsPerUser q = new AddGroupsPerUser(user,user.getProjects());
				exec.process(q);
			}			
		}
		return result;
	}

	@Override
	protected String getURI(DBUser target, Method method) throws Exception {
		if (passwordChange)
			return String.format("%s/myaccount", baseReference);
		else
			return super.getURI(target, method);
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
}
