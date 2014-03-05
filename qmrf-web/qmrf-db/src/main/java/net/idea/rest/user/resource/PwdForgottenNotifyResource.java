package net.idea.rest.user.resource;


public class PwdForgottenNotifyResource  extends QMRFRegistrationNotifyResource {
	
	public PwdForgottenNotifyResource() {
		super();
	}

	@Override
	public String getTemplateName() {
		return "pwd_forgotten_notify.ftl";
	}

	
	
}