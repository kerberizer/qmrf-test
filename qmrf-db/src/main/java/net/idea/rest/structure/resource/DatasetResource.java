package net.idea.rest.structure.resource;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.idea.rest.protocol.attachments.DBAttachment;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class DatasetResource extends StructureResource {
	public static String datasetKey = "datasetKey";
	@Override	
	protected Iterator<Structure> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		Form form = request.getResourceRef().getQueryAsForm();
		((StructureHTMLBeauty)getHTMLBeauty()).setDatasets(form.getValuesArray("dataset"));
		Object key = getRequest().getAttributes().get(datasetKey);
		if ((key == null) || "".equals(key))	return Collections.EMPTY_LIST.iterator();
		String search = Reference.decode(key.toString());
		
		DBAttachment attachment = new DBAttachment();
		attachment.setTitle(search);
		((StructureHTMLBeauty)getHTMLBeauty()).setAttachment(attachment);
		
		String pagesize = form.getFirstValue("pagesize");
		String page = form.getFirstValue("page");
		try {
			int psize = Integer.parseInt(pagesize);
			if (psize > 100)
				pagesize = "10";
		} catch (Exception x) {
			pagesize = "10";
		}
		try {
			int p = Integer.parseInt(page);
			if ((p < 0) || (p > 100))
				page = "0";
		} catch (Exception x) {
			page = "0";
		}
		Reference ref = null;
		
		try {
			ref = new Reference(String.format("%s/dataset/%s",queryService, Reference.encode(search)));
			ref.addQueryParameter("pagesize", pagesize);
			ref.addQueryParameter("page", page);

			try {
				List<Structure> records = Structure.retrieveStructures(
						queryService, ref.toString());
				return records.iterator();
			} catch (Exception x) {
				throw createException(Status.CLIENT_ERROR_BAD_REQUEST, search,null, ref.toString(), x);
			}
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST, search,
					null, ref.toString(), x);
		}
		

	}
}
