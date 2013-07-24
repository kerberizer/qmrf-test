package net.idea.rest;

import net.idea.restnet.c.resource.CatalogResource;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * Just adds a header
 * @author nina
 *
 * @param <T>
 */
public abstract class QMRFCatalogResource<T> extends CatalogResource<T> {

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Form headers = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getRequest().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Frame-Options", "SAMEORIGIN");
		return super.get(variant);
	}
}
