package net.idea.rest.user;

import net.idea.qmrf.client.Resources;
import net.idea.rest.groups.OrganisationRouter;
import net.idea.rest.groups.ProjectRouter;
import net.idea.rest.protocol.ProtocolRouter;
import net.idea.rest.protocol.resource.db.ProtocolDBResource;
import net.idea.rest.user.alerts.resource.AlertRouter;
import net.idea.rest.user.resource.UserDBResource;
import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;

public class UserRouter extends MyRouter {
	public UserRouter(Context context,ProtocolRouter protocols,OrganisationRouter orgRouter, 
				ProjectRouter projectRouter, AlertRouter alertRouter) {
		super(context);
		attachDefault(UserDBResource.class);
		attach(String.format("/{%s}",UserDBResource.resourceKey), UserDBResource.class);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.protocol), ProtocolDBResource.class);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.project),projectRouter);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.organisation), orgRouter);
		attach(String.format("/{%s}%s",UserDBResource.resourceKey,Resources.alert), alertRouter);
	}
}
