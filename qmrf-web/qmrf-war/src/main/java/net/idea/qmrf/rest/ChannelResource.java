package net.idea.qmrf.rest;

import java.net.MalformedURLException;
import java.net.URL;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.c.AbstractResource;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;


public class ChannelResource<T> extends AbstractResource<URL,T,RemoteStreamConvertor>  {

	@Override
	public RemoteStreamConvertor createConvertor(Variant variant) throws AmbitException,
			ResourceException {
		return new RemoteStreamConvertor();
	}

	@Override
	protected URL createQuery(Context context, Request request, Response response)
			throws ResourceException {
		try {
			Form form = request.getResourceRef().getQueryAsForm();
			return new URL(form.getFirstValue("uri"));
		} catch (MalformedURLException x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x.getMessage());
		}
	}

	@Override
	protected Representation processAndGenerateTask(Method method,
			Representation entity, Variant variant, boolean async)
			throws ResourceException {
		return null;
	}

	@Override
	protected Representation get(Variant variant) throws ResourceException {

		return super.get(variant);
	}
	
	@Override
	protected Representation get() throws ResourceException {
		return get(new Variant(MediaType.APPLICATION_JSON));
	}
}
