package net.idea.qmrf.aa;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.local.UserLogoutPOSTResource;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.security.User;

public class QMRFLogoutPOSTResource<U extends User> extends UserLogoutPOSTResource<U> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.protocol);
	}
	
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
			
	     this.getResponse().redirectSeeOther(String.format("%s/",getRequest().getRootRef()));
	     return null;
	}
	
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Form headers = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getRequest().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Frame-Options", "SAMEORIGIN");
		return super.get(variant);
	}
}
