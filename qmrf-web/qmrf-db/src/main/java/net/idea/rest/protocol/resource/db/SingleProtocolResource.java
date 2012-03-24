package net.idea.rest.protocol.resource.db;


import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class SingleProtocolResource  extends ProtocolDBResource<ReadProtocol> {
	protected String suffix ;
	
	public SingleProtocolResource(String suffix) {
		super();
		this.suffix = suffix;
		singleItem = true;
	}

	@Override
	protected ReadProtocol createQuery(Context context, Request request, Response response)
			throws ResourceException {
		final Object key = request.getAttributes().get(FileResource.resourceKey);		
		try {
			if (key==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			else {
				int id[] = ReadProtocol.parseIdentifier(Reference.decode(key.toString()));
				return new ReadProtocol(id[0],id[1],id[2]);
			}
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(
					Status.CLIENT_ERROR_BAD_REQUEST,
					String.format("Invalid protocol id %d",key),
					x
					);
		}
	}
	

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) {
			htmlBeauty = new QMRF_HTMLBeauty(Resources.protocol,true);
		}
		return htmlBeauty;
	}
}
