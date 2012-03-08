package net.idea.rest.protocol;

import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.protocol.attachments.ProtocolAttachmentResource;
import net.idea.rest.protocol.resource.db.ProtocolDBResource;
import net.idea.rest.protocol.resource.db.ProtocolVersionDBResource;
import net.idea.rest.user.author.resource.ProtocolAuthorsResource;
import net.idea.restnet.c.routers.MyRouter;

import org.restlet.Context;

public class ProtocolRouter extends MyRouter {
	public ProtocolRouter(Context context) {
		super(context);
		attachDefault(ProtocolDBResource.class);
		attach(String.format("/{%s}",FileResource.resourceKey), ProtocolDBResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.attachment), ProtocolAttachmentResource.class);
		attach(String.format("/{%s}%s/{%s}",FileResource.resourceKey,Resources.attachment,ProtocolAttachmentResource.resourceKey), ProtocolAttachmentResource.class);
		//attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.attachment), ProtocolAttachmentResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.versions), ProtocolVersionDBResource.class);
		attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.authors), ProtocolAuthorsResource.class);

		
	//	attach(String.format("/{%s}%s",FileResource.resourceKey,Resources.previous), ProtocolPreviousVersionDBResource.class);
		
	}
}
