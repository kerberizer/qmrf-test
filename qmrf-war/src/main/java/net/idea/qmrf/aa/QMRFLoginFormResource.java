package net.idea.qmrf.aa;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.local.UserLoginFormResource;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.security.User;


public class QMRFLoginFormResource extends UserLoginFormResource<User> {
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.login);
	}
	@Override
	protected Reporter createHTMLReporter(boolean headles) {
		return new QMRFLoginFormReporter(getRequest(),getDocumentation(),getHTMLBeauty());
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
			User user = getRequest().getClientInfo().getUser();
			if ((user!=null) && (user.getIdentifier()!=null)) {
				 this.getResponse().redirectSeeOther(String.format("%s%s",getRequest().getRootRef(),Resources.protocol));
				 return null;
			}	
		}
		return super.get(variant);
	}

}
