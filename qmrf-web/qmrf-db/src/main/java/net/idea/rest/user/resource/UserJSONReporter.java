package net.idea.rest.user.resource;

import java.io.IOException;
import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.DbAmbitException;
import net.idea.modbcum.r.QueryReporter;
import net.idea.rest.user.DBUser;
import net.idea.restnet.db.QueryURIReporter;

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

	public UserJSONReporter(Request request) {
		this.baseReference = (request==null?null:request.getRootRef());
		setRequest(request);
		uriReporter = new UserURIReporter(request,"");
	}	

	private static String format = "\n{\n\t\"uri\":\"%s\",\n\t\"id\": \"U%s\",\n\t\"username\": \"%s\",\n\t\"title\": \"%s\",\n\t\"firstname\": \"%s\",\n\t\"lastname\": \"%s\",\n\t\"email\": \"%s\",\n\t\"homepage\": \"%s\",\n\t\"keywords\": \"%s\",\n\t\"reviewer\": %s\n\t}";
	//output.write("Title,First name,Last name,user name,email,Keywords,Reviewer\n");

	@Override
	public Object processItem(DBUser user) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			String uri = uriReporter.getURI(user);
			getOutput().write(String.format(format,
					uri,
					user.getID(),
					user.getUserName()==null?"":user.getUserName(),
					user.getTitle()==null?"":user.getTitle(),
					user.getFirstname()==null?"":user.getFirstname(),
					user.getLastname()==null?"":user.getLastname(),
					user.getEmail()==null?"":user.getEmail(),
					user.getHomepage()==null?"":user.getHomepage(),
					user.getKeywords()==null?"":user.getKeywords(),
					user.isReviewer()
					));
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