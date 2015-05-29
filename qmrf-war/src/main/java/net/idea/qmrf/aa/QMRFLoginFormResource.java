package net.idea.qmrf.aa;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFCatalogResource;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.aa.cookie.CookieAuthenticator;
import net.idea.restnet.aa.local.UserLoginURIReporter;
import net.idea.restnet.aa.opensso.users.SingleItemIterator;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.security.User;

public class QMRFLoginFormResource extends QMRFCatalogResource<User> {

    public QMRFLoginFormResource() {
	super();
	setHtmlbyTemplate(true);
    }

    @Override
    public String getTemplateName() {
	return "login.ftl";
    }

    /**
     * Intercepted by {@link CookieAuthenticator}
     */
    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {

	if (getRequest().getChallengeResponse() != null) {
	    /*
	     * CookieSetting cS = new CookieSetting(0,
	     * "Credentials",getRequest().getChallengeResponse().getRawValue());
	     * cS.setComment("CookieAuthenticator"); cS.setPath("/");
	     * this.getResponse().setStatus(Status.SUCCESS_OK);
	     * this.getResponse().getCookieSettings().removeAll("Credentials");
	     * this.getResponse().getCookieSettings().add(cS);
	     * this.getRequest().getCookies().add(
	     * "Credentials",getRequest().getChallengeResponse().getRawValue());
	     */
	}
	this.getResponse().redirectSeeOther(String.format("%s/login", getRequest().getRootRef()));
	return null;
    }

    @Override
    public IProcessor<Iterator<User>, Representation> createConvertor(Variant variant) throws AmbitException,
	    ResourceException {

	if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
	    return new StringConvertor(createHTMLReporter(false), MediaType.TEXT_HTML);
	} else if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
	    return new StringConvertor(new UserLoginURIReporter(getRequest(), getDocumentation()) {
		@Override
		public void processItem(Object item, Writer output) {
		    super.processItem(item, output);
		    try {
			output.write('\n');
		    } catch (Exception x) {
		    }
		}
	    }, MediaType.TEXT_URI_LIST);

	} else
	    // html
	    return new StringConvertor(createHTMLReporter(false), MediaType.TEXT_HTML);

    }

    @Override
    protected Iterator<User> createQuery(Context context, Request request, Response response) throws ResourceException {

	User user = request.getClientInfo().getUser();
	if (user == null) {
	    user = new User();
	}
	if (user instanceof User)
	    return new SingleItemIterator(user);
	else
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Override
    protected HTMLBeauty getHTMLBeauty() {
	return new QMRF_HTMLBeauty(Resources.login);
    }

    @Override
    protected Reporter createHTMLReporter(boolean headles) {
	return new QMRFLoginFormReporter(getRequest(), getDocumentation(), getHTMLBeauty());
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
	getResponse().getCacheDirectives().add(CacheDirective.noCache());
	if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
	    User user = getRequest().getClientInfo().getUser();
	    if ((user != null) && (user.getIdentifier() != null)) {
		this.getResponse().redirectSeeOther(
			String.format("%s%s", getRequest().getRootRef(), Resources.protocol));
		return null;
	    }
	}
	return super.get(variant);
    }
    @Override
    public void configureTemplateMap(Map<String, Object> map) {
        super.configureTemplateMap(map);
        map.put("searchURI",Resources.register);
    }
}
