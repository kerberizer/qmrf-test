package net.idea.qmrf.task;

import net.idea.rest.FileResource;
import net.idea.rest.db.DatabaseResource;
import net.idea.rest.qmrf.admin.QMRFUploadUIResource;
import net.idea.restnet.aa.resource.AdminRouter;

import org.restlet.Context;

public class QMRFAdminRouter extends AdminRouter  {

	public QMRFAdminRouter(Context context) {
		super(context);
	}
	@Override
	protected void init() {
		attachDefault(QMRFAdminResource.class);
		attach(String.format("/%s",QMRFUploadUIResource.resource),QMRFUploadUIResource.class);
		attach(String.format("/%s/{%s}",QMRFUploadUIResource.resource,FileResource.resourceKey),QMRFUploadUIResource.class);
		attach(String.format("/%s",DatabaseResource.resource),DatabaseResource.class);
		
	}
}