package net.idea.rest.user.resource;

public class PwdForgottenFailedResource extends QMRFRegistrationNotifyResource {

	public PwdForgottenFailedResource() {
		super();
	}

	@Override
	public String getTemplateName() {
		return "pwd_forgotten_failed.ftl";
	}

	
}
