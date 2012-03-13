package net.idea.qmrf.task;

import net.idea.rest.FileResource;
import net.idea.rest.qmrf.admin.QMRFUploadUIResource;
import net.idea.restnet.aa.resource.AdminRouter;

import org.restlet.Context;

public class QMRFEditorRouter extends AdminRouter  {

	public QMRFEditorRouter(Context context) {
		super(context);
	}
	@Override
	protected void init() {
		attachDefault(QMRFUploadUIResource.class);
		attach(String.format("/{%s}",FileResource.resourceKey),QMRFUploadUIResource.class);
	}
}