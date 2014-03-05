package net.idea.rest.user;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.c.exception.InvalidUserException;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.DeleteUser;

public class QMRFDeleteUser extends DeleteUser{
	protected static final String[] qmrf_sql = new String[] {
		"DELETE from user_organisation where iduser=?",
		"DELETE from user where iduser=?"
	};
	public QMRFDeleteUser(DBUser ref) {
		super(ref);
	}
	public QMRFDeleteUser() {
		this(null);
	}		
	public String[] getSQL() throws AmbitException {
		if (getObject()==null || getObject().getID()<=0) throw new InvalidUserException();
		return qmrf_sql;
	}

}
