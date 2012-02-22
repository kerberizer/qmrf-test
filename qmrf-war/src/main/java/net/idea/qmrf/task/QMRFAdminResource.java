package net.idea.qmrf.task;

import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.resource.AdminResource;
import net.idea.restnet.c.html.HTMLBeauty;


public class QMRFAdminResource extends AdminResource {

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
}
