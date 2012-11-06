package net.idea.rest.user.resource;

import java.util.Map;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFQueryResource;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.u.RegistrationJSONReporter;
import net.idea.restnet.u.UserRegistration;
import net.idea.restnet.u.db.ReadRegistration;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class RegistrationConfirmResource extends  QMRFQueryResource<ReadRegistration,UserRegistration> {
	public static String confirmationCode = "code";
	public RegistrationConfirmResource() {
		super();
		setHtmlbyTemplate(true);
	}
	
	@Override
	public String getTemplateName() {
		return "register_confirm.ftl";
	}
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless)
			throws ResourceException {
		return null;
	}
	
	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		Map<String, Object> map =  super.getMap(variant);
		Object code = getRequest().getResourceRef().getQueryAsForm().getFirstValue(confirmationCode);
		if (code!=null) map.put("qmrf_reg_confirmed", code);
		map.put("searchURI",Resources.confirm);
		return map;
	}

	@Override
	public IProcessor<ReadRegistration, Representation> createConvertor(
			Variant variant) throws AmbitException, ResourceException {
		RegistrationJSONReporter r = new RegistrationJSONReporter(getRequest());
		return new StringConvertor(	r,MediaType.APPLICATION_JSON,"");
	}

	@Override
	protected ReadRegistration createQuery(Context context, Request request,
			Response response) throws ResourceException {
		Object code = getRequest().getResourceRef().getQueryAsForm().getFirstValue(confirmationCode);
		if (code==null) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		return new ReadRegistration(code.toString());
	}

}
