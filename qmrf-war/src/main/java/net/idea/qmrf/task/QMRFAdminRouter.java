package net.idea.qmrf.task;

import net.idea.qmrf.client.Resources;
import net.idea.rest.db.DatabaseResource;
import net.idea.rest.user.alerts.notification.NotificationResource;
import net.idea.restnet.aa.resource.AdminRouter;

import org.restlet.Context;

public class QMRFAdminRouter extends AdminRouter  {

	public QMRFAdminRouter(Context context) {
		super(context);
	}
	@Override
	protected void init() {
		attachDefault(QMRFAdminResource.class);
		attach(String.format("/%s",DatabaseResource.resource),DatabaseResource.class);
		attach(Resources.notification, NotificationResource.class);
				
	}
}