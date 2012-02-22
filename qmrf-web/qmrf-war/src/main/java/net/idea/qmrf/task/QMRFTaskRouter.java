package net.idea.qmrf.task;

import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;

public class QMRFTaskRouter extends MyRouter {
	public QMRFTaskRouter(Context context) {
		super(context);
		attachDefault(QMRFTaskResource.class);
		attach(QMRFTaskResource.resourceID, QMRFTaskResource.class);
	}
}
