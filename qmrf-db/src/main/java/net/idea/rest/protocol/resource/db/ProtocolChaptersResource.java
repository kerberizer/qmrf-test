package net.idea.rest.protocol.resource.db;

import net.idea.qmrf.client.Resources;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.resource.ResourceException;

public class ProtocolChaptersResource extends SingleProtocolResource {

	public ProtocolChaptersResource() {
		super(Resources.chapter);
	}
	

	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException {
		QMRFChaptersHTMLReporter rep = new QMRFChaptersHTMLReporter(getRequest(),!singleItem,isEditable(),structure==null,details);
		rep.setHeadless(headless);
		rep.setHtmlBeauty(getHTMLBeauty());
		rep.setDtdresolver(((TaskApplication)getApplication()).getResolver());
		return rep;
	}
}	