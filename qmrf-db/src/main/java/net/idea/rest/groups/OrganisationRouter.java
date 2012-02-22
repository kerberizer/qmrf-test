package net.idea.rest.groups;

import net.idea.rest.groups.resource.OrganisationDBResource;
import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;

public class OrganisationRouter extends MyRouter {
	public OrganisationRouter(Context context) {
		super(context);
		attachDefault(OrganisationDBResource.class);
		attach(String.format("/{%s}",OrganisationDBResource.resourceKey), OrganisationDBResource.class);
	
	}
}
