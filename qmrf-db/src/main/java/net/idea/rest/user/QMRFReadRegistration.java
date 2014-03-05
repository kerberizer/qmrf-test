package net.idea.rest.user;

import java.sql.ResultSet;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.u.RegistrationStatus;
import net.idea.restnet.u.UserRegistration;
import net.idea.restnet.u.db.ReadRegistration;

public class QMRFReadRegistration extends ReadRegistration {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6558532044289433277L;

	public QMRFReadRegistration(String code) {
		super(code);
	}
	
	public QMRFReadRegistration(QMRFUserRegistration registration) {
		super(registration);
	}
	
	@Override
	public UserRegistration getObject(ResultSet rs) throws AmbitException {
		try {
			UserRegistration p =  new QMRFUserRegistration();
			p.setConfirmationCode(rs.getString("code"));
			p.setStatus(RegistrationStatus.valueOf(rs.getString("status")));
			p.setTimestamp_confirmed(rs.getLong("confirmed"));
			p.setTimestamp_created(rs.getLong("created"));
			return p;
		} catch (Exception x) {
			return null;
		}
	}
}
