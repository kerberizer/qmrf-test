package net.idea.rest;

import java.util.Iterator;

import net.idea.restnet.c.resource.CatalogResource;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
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
	protected Form params;
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
	
	protected Form getParams() {
		if (params == null) 
			if (Method.GET.equals(getRequest().getMethod())) {
				params = getResourceRef(getRequest()).getQueryAsForm();
				Iterator<Parameter> p = params.iterator();
				while (p.hasNext()) {
					Parameter param = p.next();
					String value = param.getValue();
					if (value==null) continue;
					if (value.contains("script") || value.contains(">") || value.contains("<")) param.setValue(""); 
					//else param.setValue(value.replace("'","&quot;"));	
				}
			}	
			//if POST, the form should be already initialized
			else params = getRequest().getEntityAsForm();
		return params;
	}
}
