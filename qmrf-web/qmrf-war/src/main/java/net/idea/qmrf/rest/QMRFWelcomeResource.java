package net.idea.qmrf.rest;

import java.util.HashMap;
import java.util.Map;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * A welcome/introductory page
 * @author nina
 *
 */
public class QMRFWelcomeResource extends ServerResource {
	protected QMRF_HTMLBeauty htmlBeauty;

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		if (getRequest().getResourceRef().toString().endsWith("/")) {
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("text", "This is a sample text.");
	        return toRepresentation(map, "welcome.ftl", MediaType.TEXT_PLAIN);
		} else {
			//if no slash, all the styles etc. paths are broken...
			redirectSeeOther(String.format("%s/",getRequest().getResourceRef()));
			return null;
		}
	}
	

    protected Representation toRepresentation(Map<String, Object> map,
            String templateName, MediaType mediaType) {
        
        return new TemplateRepresentation(
        		templateName,
        		((QMRFApplication)getApplication()).getConfiguration(),
        		MediaType.TEXT_HTML);
    }
    
	@Override
	protected Representation get() throws ResourceException {
		return this.get(null);
	}
	protected QMRF_HTMLBeauty createHtmlBeauty() {
		return new QMRF_HTMLBeauty(Resources.welcome);
	}
	
	public QMRF_HTMLBeauty getHtmlBeauty() {
		if (htmlBeauty==null) htmlBeauty = createHtmlBeauty();
		return htmlBeauty;
	}

	public void setHtmlBeauty(QMRF_HTMLBeauty htmlBeauty) {
		this.htmlBeauty = htmlBeauty;
	}

}