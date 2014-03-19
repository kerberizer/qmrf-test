package net.idea.rest.user;

import net.idea.restnet.u.RegistrationStatus;
import net.idea.restnet.user.DBUser;

public class QMRFUser extends DBUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1143144337574236826L;
	protected RegistrationStatus registrationStatus;
	public RegistrationStatus getRegistrationStatus() {
		return registrationStatus;
	}
	public void setRegistrationStatus(RegistrationStatus registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
}	
