package net.idea.qmrf.aa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.idea.modbcum.p.QueryExecutor;
import net.idea.qmrf.client.QMRFRoles;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.rest.QMRFApplication;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.user.author.db.VerifyUser;
import net.idea.rest.user.resource.UserDBResource;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.aalocal.DBRole;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import org.restlet.routing.Template;
import org.restlet.security.RoleAuthorizer;

/**
 * The editor and manager roles have full access, 
 * the user has access to its own piece only
 * @author nina
 *
 */
public class UserAuthorizer extends RoleAuthorizer {

	protected VerifyUser query;
	protected QueryExecutor<VerifyUser> executor;
	
	public UserAuthorizer() {
		super();
		getAuthorizedRoles().add(new DBRole(QMRFRoles.qmrf_editor.name(),QMRFRoles.qmrf_editor.toString()));
		getAuthorizedRoles().add(new DBRole(QMRFRoles.qmrf_manager.name(),QMRFRoles.qmrf_manager.toString()));
	}

	@Override
	public boolean authorize(Request request, Response response) {
		if ((request.getClientInfo()==null) 
				|| (request.getClientInfo().getUser()==null)
				|| (request.getClientInfo().getUser().getIdentifier()==null)) return false;
		//role based
		if (super.authorize(request, response)) return true;
		
		Template template1 = new Template(String.format("%s%s/{%s}",request.getRootRef(),Resources.user,UserDBResource.resourceKey));
		Template template2 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.user,UserDBResource.resourceKey,Resources.protocol));
		Template template3 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.user,UserDBResource.resourceKey,Resources.project));
		Template template4 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.user,UserDBResource.resourceKey,Resources.organisation));
		Template template5 = new Template(String.format("%s%s/{%s}%s",request.getRootRef(),Resources.user,UserDBResource.resourceKey,Resources.alert));
		Map<String, Object> vars = new HashMap<String, Object>();
		Reference ref = request.getResourceRef().clone();
		ref.setQuery(null);
		template1.parse(ref.toString(),vars);
		template2.parse(ref.toString(),vars);
		template3.parse(ref.toString(),vars);
		template4.parse(ref.toString(),vars);
		template5.parse(ref.toString(),vars);
		if (vars.get(UserDBResource.resourceKey)==null) return false;
		Integer iduser  = null;
		try {
			iduser = Integer.parseInt(vars.get(UserDBResource.resourceKey).toString().substring(1));
			return verify(iduser,request.getClientInfo().getUser().getIdentifier());
		} catch (Exception x) {	}
		return false; 
	}
	public boolean verify(Integer iduser, String identifier) throws Exception {
		//TODO make use of same connection for performance reasons
		Connection c = null;
		ResultSet rs = null;
		try {
			if (query==null) query = new VerifyUser();
			query.setFieldname(iduser);
			query.setValue(identifier);
			DBConnection dbc = new QDBConnection(getApplication().getContext(),((QMRFApplication)getApplication()).getDbConfig());
			c = dbc.getConnection();
			if (executor==null)  executor = new QueryExecutor<VerifyUser>();
			executor.setConnection(c);
			rs = executor.process(query);
			boolean ok = false;
			while (rs.next()) {
				ok = query.getObject(rs);
				break;
			}
			return ok;
		} catch (Exception x) {
			throw x;
		} finally {
			try {executor.close();} catch (Exception x) {};
			try {if (rs!=null) rs.close();} catch (Exception x) {};
			try {if (c!=null) c.close();} catch (Exception x) {};
		}
	}
	
}
