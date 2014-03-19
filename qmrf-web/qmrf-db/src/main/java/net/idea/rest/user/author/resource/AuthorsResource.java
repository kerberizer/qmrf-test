package net.idea.rest.user.author.resource;

import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.UserHTMLBeauty;
import net.idea.rest.user.author.db.ReadAuthorXML;
import net.idea.rest.user.resource.UserDBResource;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;

import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class AuthorsResource extends  UserDBResource<DBProtocol> {

	public AuthorsResource() {
		super();
		readRegistrationStatus = false;
	}
	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.SERVER_ERROR_NOT_IMPLEMENTED);
	}
	
	@Override
	protected ReadUser getUserQuery(Object key, String search_name,
			Object search_value) throws ResourceException {
		Object protocolKey = getRequest().getAttributes().get(FileResource.resourceKey);
		DBProtocol protocol = null;
		if (protocolKey!=null) 
			protocol = new DBProtocol(Reference.decode(protocolKey.toString()));
		DBUser user = null;
		if (key!=null) {
			if (key.toString().startsWith("U")) {
				singleItem = true;
				user = new DBUser(new Integer(Reference.decode(key.toString().substring(1))));
			} else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}	
		return new ReadAuthorXML(protocol, user);
	}
		
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty =  new UserHTMLBeauty(Resources.authors);
		return htmlBeauty;
	}	
}
