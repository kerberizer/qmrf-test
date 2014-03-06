package net.idea.rest.user;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.c.exception.InvalidUserException;
import net.idea.restnet.db.aalocal.user.IDBConfig;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.DeleteUser;

public class QMRFDeleteUser extends DeleteUser implements  IDBConfig {
	protected static final String[] qmrf_sql = new String[] {
		"delete u from %s.user_registration u,user q where q.username=u.user_name and iduser=?",
		"delete u from %s.user_roles u,user q where q.username=u.user_name and iduser=?",
		"delete u from %s.users u,user q where q.username=u.user_name and iduser=?",
		"DELETE from user_organisation where iduser=?",
		"DELETE from user where iduser=?"
	};
	public QMRFDeleteUser(DBUser ref,String databaseName) {
		super(ref);
		setDatabaseName(databaseName);
	}
	
	public String[] getSQL() throws AmbitException {
		if (getObject()==null || getObject().getID()<=0) throw new InvalidUserException();
		return qmrf_sql;
	}
	protected String databaseName = null;
	@Override
	public void setDatabaseName(String name) {
		databaseName = name;
		for (int i=0; i < 3; i++) qmrf_sql[i] = String.format(qmrf_sql[i],databaseName);
			
	}
	@Override
	public String getDatabaseName() {
		return databaseName;
	}

}
