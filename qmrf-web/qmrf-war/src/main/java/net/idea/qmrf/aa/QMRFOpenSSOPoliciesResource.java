package net.idea.qmrf.aa;

import java.util.Iterator;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.opensso.policy.OpenSSOPoliciesResource;
import net.idea.restnet.aa.opensso.policy.Policy;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ResourceException;

public class QMRFOpenSSOPoliciesResource extends OpenSSOPoliciesResource {

	@Override
	protected Iterator<Policy> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		return super.createQuery(context, request, response);
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.protocol);
	}
}
