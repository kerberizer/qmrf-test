package net.idea.rest.user.db;

import net.idea.rest.user.DBUser;
import net.idea.restnet.u.AbstractUpdateCredentials;
import net.idea.restnet.u.UserCredentials;

public class UpdateCredentials extends AbstractUpdateCredentials<DBUser> {

	public UpdateCredentials(UserCredentials c,DBUser ref) {
		super(c,ref);
	}
	
	@Override
	public String getUserName(DBUser user) {
		return user.getUserName();
	}

}
