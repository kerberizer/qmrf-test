package net.idea.qmrf.aa;

import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.opensso.policy.OpenSSOPolicyResource;
import net.idea.restnet.c.html.HTMLBeauty;


public class QMRFPolicyResource extends OpenSSOPolicyResource {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
		
	}
}
