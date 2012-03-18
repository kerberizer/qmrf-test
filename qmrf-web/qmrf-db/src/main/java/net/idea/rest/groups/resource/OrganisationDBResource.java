package net.idea.rest.groups.resource;

import java.sql.Connection;

import net.idea.qmrf.client.Resources;
import net.idea.rest.groups.CallableGroupCreator;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.GroupType;
import net.idea.rest.groups.db.ReadGroup;
import net.idea.rest.groups.db.ReadOrganisation;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.resource.UserDBResource;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.db.DBConnection;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class OrganisationDBResource extends GroupDBResource<DBOrganisation> {

	@Override
	public ReadGroup<DBOrganisation> createGroupQuery(Integer key, String search, String groupName) {
		DBOrganisation p = new DBOrganisation();
		p.setTitle(search);
		p.setGroupName(groupName);
		if (key!=null) p.setID(key);
		ReadOrganisation q = new ReadOrganisation(p);
		return q;
	}
	@Override
	public String getGroupBackLink() {
		return  Resources.organisation;
	}
	@Override
	public String getGroupTitle() {
		return GroupType.ORGANISATION.toString();
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty =  new GroupHTMLBeauty(Resources.organisation);
		return htmlBeauty;
	}

	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			Form form, DBOrganisation item) throws ResourceException {
		Connection conn = null;
		try {
			DBUser user = null;
			Object userKey = getRequest().getAttributes().get(UserDBResource.resourceKey);		
			if ((userKey!=null) && userKey.toString().startsWith("U")) try {
				user = new DBUser(new Integer(Reference.decode(userKey.toString().substring(1))));
			} catch (Exception x) {}
			
			GroupQueryURIReporter r = new GroupQueryURIReporter(getRequest(),"");
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			return new CallableGroupCreator(method,item,GroupType.ORGANISATION,user,r,form,getRequest().getRootRef().toString(),conn,getToken());
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	};

}
