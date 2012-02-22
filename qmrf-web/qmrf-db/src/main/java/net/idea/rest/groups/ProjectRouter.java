package net.idea.rest.groups;

import net.idea.rest.groups.resource.ProjectDBResource;
import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;

public class ProjectRouter extends MyRouter {
	
	public ProjectRouter(Context context) {
		super(context);
		attachDefault(ProjectDBResource.class);
		attach(String.format("/{%s}",ProjectDBResource.resourceKey), ProjectDBResource.class);
	
	}
}
