package net.idea.qmrf.aa;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.local.UserLogoutPOSTResource;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.security.User;

public class QMRFLogoutPOSTResource<U extends User> extends UserLogoutPOSTResource<U> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.protocol);
	}
}
