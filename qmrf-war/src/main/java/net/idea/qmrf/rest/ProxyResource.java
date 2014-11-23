package net.idea.qmrf.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.qmrf.client.Resources;
import net.idea.restnet.c.AbstractResource;
import net.idea.restnet.c.TaskApplication;


import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.ServerInfo;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;


public class ProxyResource<T> extends AbstractResource<URL,T,RemoteStreamConvertor>  {
	protected enum supported_media {
		json,
		img {
			@Override
			public MediaType getMediaType() {
				return MediaType.IMAGE_PNG;
			}
		};
		public MediaType getMediaType() {
			return MediaType.APPLICATION_JSON;
		}
		
	};
	protected void doInit() throws ResourceException {
		super.doInit();
		customizeVariants(new MediaType[] {
				MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_JAVASCRIPT,
				MediaType.IMAGE_PNG});
	}	
	protected supported_media media = supported_media.json;
	@Override
	public RemoteStreamConvertor createConvertor(Variant variant) throws AmbitException,
			ResourceException {
		return new RemoteStreamConvertor(getQueryService(),media.getMediaType());
	}

	@Override
	protected URL createQuery(Context context, Request request, Response response)
			throws ResourceException {
		try {
			try {
				media = supported_media.valueOf(request.getAttributes().get("media").toString());
			} catch (Exception x) {
				media = supported_media.json;
			}	
			Form form = getParams(request.getResourceRef().getQueryAsForm());
			return new URL(form.getFirstValue("uri"));
		} catch (MalformedURLException x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	}

	protected Form getParams(Form params_get) {
			Iterator<Parameter> p = params_get.iterator();
			while (p.hasNext()) {
				Parameter param = p.next();
				String value = param.getValue();
				if (value==null) continue;
				if (value.contains(">") || value.contains("<")) param.setValue(""); 
				else param.setValue(value.replace("'","&quot;"));	
			}
			return params_get;
	}
	

	
	@Override
	protected Representation processAndGenerateTask(Method method,
			Representation entity, Variant variant, boolean async)
			throws ResourceException {
		return null;
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {
		Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getResponse().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.removeAll("X-Frame-Options");
		headers.add("X-Frame-Options", "SAMEORIGIN");
		getResponse().getCacheDirectives().add(CacheDirective.proxyMustRevalidate());
		ServerInfo si = getResponse().getServerInfo();si.setAgent("Restlet");getResponse().setServerInfo(si);
		return super.get(variant);
	}
	
	@Override
	protected Representation get() throws ResourceException {
		return get(new Variant(media.getMediaType()));
	}
	protected String getQueryService() {
		return ((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name());
	}
}
