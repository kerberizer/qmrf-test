package net.idea.rest.protocol;

import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.protocol.resource.db.ProtocolDBResource;
import net.idea.rest.protocol.resource.db.ProtocolDocumentResource;
import net.idea.rest.protocol.resource.db.ProtocolPreviousVersionDBResource;
import net.idea.rest.protocol.resource.db.ProtocolVersionDBResource;
import net.idea.rest.protocol.resource.db.template.DataTemplateResource;
import net.idea.rest.user.author.resource.ProtocolAuthorsResource;
import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;

public class ProtocolRouter extends MyRouter {
	public ProtocolRouter(Context context) {
		super(context);
		attachDefault(ProtocolDBResource.class);
		attach(String.format("/{%s}",FileResource.resourceKey), ProtocolDBResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.document), ProtocolDocumentResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.versions), ProtocolVersionDBResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.authors), ProtocolAuthorsResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.datatemplate), DataTemplateResource.class);
		
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.previous), ProtocolPreviousVersionDBResource.class);
		
	}
}
