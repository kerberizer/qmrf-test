package net.idea.rest.structure.resource;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.rest.protocol.attachments.AttachmentDatasetResource;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.db.ReadAttachment;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class DatasetResource extends AttachmentDatasetResource {
    public static final String datasetKey = "datasetkey";
    public DatasetResource() {
	super();
	setHtmlbyTemplate(true);
    }
    @Override
    public String getTemplateName() {
        return "dataset.ftl";
    }
    @Override
    protected IQueryRetrieval<DBAttachment> createQuery(Context context, Request request, Response response)
	    throws ResourceException {
	try {
	    Object key = getRequest().getAttributes().get(DatasetResource.datasetKey);
	    if ((key == null) || "".equals(key))
		throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
	    String aKey = Reference.decode(key.toString());
	    ReadAttachment query;
	    DBAttachment attachment = null;
	    if ((aKey != null) && aKey.toString().startsWith("A")) {
		attachment = new DBAttachment(new Integer(Reference.decode(aKey.toString().substring(1))));
		query = new ReadAttachment(null, getAttachmentDir());
		query.setValue(attachment);
		return query;
	    }
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
	} catch (NumberFormatException x) {
	    return null;
	} catch (ResourceException x) {
	    throw x;
	} catch (Exception x) {
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, x);
	}
    }

    @Override
    protected Representation post(Representation entity) throws ResourceException {
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    protected Representation put(Representation entity, Variant variant) throws ResourceException {
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    protected Representation put(Representation representation) throws ResourceException {
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    protected Representation delete(Variant variant) throws ResourceException {
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    protected Representation delete() throws ResourceException {
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }
}
