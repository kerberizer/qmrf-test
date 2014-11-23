package net.idea.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.idea.qmrf.client.Resources;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.resource.CatalogResource;

import org.restlet.data.CacheDirective;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.ServerInfo;
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
		Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getResponse().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.add("X-Frame-Options", "SAMEORIGIN");
		List<CacheDirective> cache = new ArrayList<CacheDirective>();
		cache.add(new CacheDirective("Cache-Control","max-age=2700, private"));
		getResponse().setCacheDirectives(cache);
		ServerInfo si = getResponse().getServerInfo();si.setAgent("Restlet");getResponse().setServerInfo(si);
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
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
		super.configureTemplateMap(map);
		map.put(Resources.Config.qmrf_disclaimer.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_disclaimer.name()));
		map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
	}
}
