package net.idea.qmrf.aa;

import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.opensso.users.OpenSSOUserResource;
import net.idea.restnet.c.html.HTMLBeauty;


public class QMRFLoginResource extends OpenSSOUserResource {
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
}
