package net.idea.rest.user;

import net.idea.restnet.u.UserRegistration;

public class QMRFUserRegistration extends UserRegistration {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7094866632321494221L;
	protected String title = "User registration";
	public QMRFUserRegistration(String code) {
		super(code);
	}
	public QMRFUserRegistration() {
		super();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return getTitle();
	}
}
