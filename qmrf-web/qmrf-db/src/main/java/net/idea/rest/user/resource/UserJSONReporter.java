package net.idea.rest.user.resource;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.DbAmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.modbcum.r.QueryReporter;
import net.idea.rest.JSONUtils;
import net.idea.rest.user.QMRFUser;
import net.idea.rest.user.ReadRegistrationStatus;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.db.ReadOrganisation;
import net.idea.restnet.groups.resource.GroupQueryURIReporter;
import net.idea.restnet.u.UserRegistration;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;
import net.toxbank.client.resource.Organisation;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Reference;

public class UserJSONReporter <Q extends IQueryRetrieval<DBUser>>  extends QueryReporter<DBUser,Q,Writer>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4566136103208284105L;
	protected String comma = null;
	protected Request request;
	protected QueryURIReporter uriReporter;
	protected GroupQueryURIReporter groupURIReporter ;
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	protected Reference baseReference;
	public Reference getBaseReference() {
		return baseReference;
	}

	public UserJSONReporter(Request request,String usersdbname) {
		this.baseReference = (request==null?null:request.getRootRef());
		setRequest(request);
		uriReporter = new UserURIReporter(request,"");
		
		getProcessors().clear();
		groupURIReporter = new GroupQueryURIReporter(request);
		IQueryRetrieval<DBOrganisation> queryO = new ReadOrganisation(new DBOrganisation()); 
		
		MasterDetailsProcessor<DBUser, DBOrganisation, IQueryCondition> orgReader = new MasterDetailsProcessor<DBUser, DBOrganisation, IQueryCondition>(queryO) {
			@Override
			public DBUser process(DBUser target) throws Exception {
				if (target==null || target.getUserName()==null) return target;
				return super.process(target);
			}
			@Override
			protected DBUser processDetail(DBUser target, DBOrganisation detail) throws Exception {
				if (target.getID()>0) {
					detail.setResourceURL(new URL(groupURIReporter.getURI(detail)));
					target.addOrganisation(detail);
				}
				return target;
			}
		};
		getProcessors().add(orgReader);		
		if (usersdbname!=null) {
			final IQueryRetrieval<UserRegistration> queryR = new ReadRegistrationStatus();
			((ReadRegistrationStatus)queryR).setDatabaseName(usersdbname);
			MasterDetailsProcessor<DBUser, UserRegistration, IQueryCondition> regReader = new MasterDetailsProcessor<DBUser, UserRegistration, IQueryCondition>(queryR) {
				@Override
				protected DBUser processDetail(DBUser target, UserRegistration detail) throws Exception {
					if (detail!=null) {
						QMRFUser user = new QMRFUser();
						user.setEmail(target.getEmail());
						user.setID(target.getID());
						user.setFirstname(target.getFirstname());
						user.setLastname(target.getLastname());
						user.setKeywords(target.getKeywords());
						user.setReviewer(target.isReviewer());
						user.setTitle(target.getTitle());
						user.setHomepage(target.getHomepage());
						user.setOrganisations(target.getOrganisations());
						user.setRegisteredAt(detail.getTimestamp_created());
						user.setRegistrationStatus(detail.getStatus());
						user.setUserName(target.getUserName());
						return user;
					} else 	
						return target;
				}
			};
			getProcessors().add(regReader);
		}
		
		processors.add(new DefaultAmbitProcessor<DBUser, DBUser>() {
			public DBUser process(DBUser target) throws Exception {
				processItem(target);
				return target;
			};
		});			
	}	

	private static String format = "\n{\n\t\"uri\":\"%s\",\n\t\"id\": %s,\n\t\"username\": \"%s\",\n\t\"title\": \"%s\",\n\t\"firstname\": \"%s\",\n\t\"lastname\": \"%s\",\n\t\"email\": \"%s\",\n\t\"homepage\": \"%s\",\n\t\"keywords\": \"%s\",\n\t\"reviewer\": %s,\n\t\"status\": %s,\n\t\"organisation\": [\n\t\t%s\n\t]\n}";
	private static String formatGroup = "{\n\t\t\"uri\":\"%s\",\n\t\t\"title\": \"%s\"\n\t\t}";
	//output.write("Title,First name,Last name,user name,email,Keywords,Reviewer\n");

	@Override
	public Object processItem(DBUser user) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			
			StringBuilder group = null;
			if (user.getOrganisations()!=null)
				for (Organisation org : user.getOrganisations()) {
					if (group == null) group = new StringBuilder();
					else group.append(",");
					group.append(String.format(formatGroup,
							groupURIReporter.getURI(org),
							JSONUtils.jsonEscape(org.getTitle())
							));
				}

			String uri = user.getID()>0?uriReporter.getURI(user):"";
			
			getOutput().write(String.format(format,
					uri,
					(user.getID()>0)?String.format("\"U%s\"",user.getID()):null,
					user.getUserName()==null?"":JSONUtils.jsonEscape(user.getUserName()),
					user.getTitle()==null?"":JSONUtils.jsonEscape(user.getTitle()),
					user.getFirstname()==null?"":JSONUtils.jsonEscape(user.getFirstname()),
					user.getLastname()==null?"":JSONUtils.jsonEscape(user.getLastname()),
					user.getEmail()==null?"":JSONUtils.jsonEscape(user.getEmail()),
					user.getHomepage()==null?"":JSONUtils.jsonEscape(user.getHomepage().toExternalForm()),
					user.getKeywords()==null?"":JSONUtils.jsonEscape(user.getKeywords()),
					user.isReviewer(),
					JSONUtils.jsonQuote(JSONUtils.jsonEscape((user instanceof QMRFUser)?((QMRFUser)user).getRegistrationStatus().name():null)),
					group==null?"":group.toString()
					));
			comma = ",";
		} catch (IOException x) {
			Context.getCurrentLogger().severe(x.getMessage());
		}
		return null;
	}	
	@Override
	public void footer(Writer output, Q query) {
		try {
			output.write("\n]\n}");
		} catch (Exception x) {}
	};
	@Override
	public void header(Writer output, Q query) {
		try {
			output.write("{\"user\": [");
		} catch (Exception x) {}
		
	};
	
	public void open() throws DbAmbitException {
		
	}
	@Override
	public void close() throws Exception {
		setRequest(null);
		super.close();
	}
	@Override
	public String getFileExtension() {
		return null;
	}
}