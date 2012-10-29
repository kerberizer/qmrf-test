package net.idea.rest.user.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;
import net.idea.rest.user.DBUser;

public class UpdateCredentials extends AbstractUpdate<UserCredentials,DBUser>{
	protected DBUser.fields[] update_fields = {
			DBUser.fields.email,
			DBUser.fields.title,
			DBUser.fields.firstname,
			DBUser.fields.lastname,
			DBUser.fields.homepage,
			DBUser.fields.keywords,
			DBUser.fields.reviewer
	};
	
	private String sql = "update tomcat_users.users set user_pass = md5(?) where user_pass=md5(?) and user_name = ?";

	
	public UpdateCredentials(UserCredentials c,DBUser ref) {
		super(ref);
		this.setGroup(c);
	}
	public UpdateCredentials() {
		this(null,null);
	}			
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params = new ArrayList<QueryParam>();
		if (getObject().getUserName()==null) throw new AmbitException("Invalid input");
		if (getGroup().getOldpwd()==null) throw new AmbitException("Invalid input");
		if (getGroup().getNewpwd()==null) throw new AmbitException("Invalid input");
		params.add(new QueryParam<String>(String.class, getGroup().getNewpwd()));
		params.add(new QueryParam<String>(String.class, getGroup().getOldpwd()));
		params.add(new QueryParam<String>(String.class, getObject().getUserName()));
		return params;
	}

	public String[] getSQL() throws AmbitException {
		
		StringBuilder b = null;
		for (DBUser.fields field : update_fields) {
			if (field.getValue(getObject())!=null) {
				if (b==null) b = new StringBuilder();
				else b.append(", ");
				b.append(field.getSQL());
			}
		}
		
		return new String[] {String.format(sql, b)};
	}
	public void setID(int index, int id) {
			
	}
} 