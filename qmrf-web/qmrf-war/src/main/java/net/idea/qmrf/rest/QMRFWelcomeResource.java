package net.idea.qmrf.rest;

import java.util.HashMap;
import java.util.Map;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.TaskApplication;

import org.restlet.data.Form;
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
			Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
			if (headers == null) {
				headers = new Form();
				getResponse().getAttributes().put("org.restlet.http.headers", headers);
			}
			headers.add("X-Frame-Options", "SAMEORIGIN");		
	        Map<String, Object> map = new HashMap<String, Object>();
	        if (getClientInfo().getUser()!=null) 
	        	map.put("username", getClientInfo().getUser().getIdentifier());
	        map.put("creator","IdeaConsult Ltd.");
	        map.put(Resources.Config.qmrf_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_email.name()));
	        map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
	        map.put(Resources.Config.qmrf_template.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_template.name()));
	        map.put(Resources.Config.qmrf_manual.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_manual.name()));
	        map.put(Resources.Config.qmrf_faq.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_faq.name()));
	        map.put(Resources.Config.qmrf_oecd.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_oecd.name()));
	        map.put(Resources.Config.qmrf_jrc.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_jrc.name()));
	        map.put(Resources.Config.qmrf_disclaimer.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_disclaimer.name()));
	        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name()));
	        map.put("qmrf_root",getRequest().getRootRef().toString()) ;
	        return toRepresentation(map, "body-welcome.ftl", MediaType.TEXT_PLAIN);
	}
	

    protected Representation toRepresentation(Map<String, Object> map,
            String templateName, MediaType mediaType) {
        
        return new TemplateRepresentation(
        		templateName,
        		((QMRFApplication)getApplication()).getConfiguration(),
        		map,
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
