package net.idea.qmrf.aa;

import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.local.UserLoginPOSTResource;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.security.User;

public class QMRFLoginPOSTResource<U extends User> extends UserLoginPOSTResource<U> {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
}
