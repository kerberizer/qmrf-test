package net.idea.qmrf.rest;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.Form;
import org.restlet.data.ServerInfo;
import org.restlet.resource.Directory;

public class ParanoidDirectory extends Directory {
    public ParanoidDirectory(Context context, String uri) {
	super(context, uri);
    }

    @Override
    public void handle(Request request, Response response) {
	Form headers = (Form) response.getAttributes().get("org.restlet.http.headers");
	if (headers == null) {
	    headers = new Form();
	    response.getAttributes().put("org.restlet.http.headers", headers);
	}
	headers.removeAll("X-Frame-Options");
	headers.add("X-Frame-Options", "SAMEORIGIN");
	response.getCacheDirectives().add(CacheDirective.noCache());
	ServerInfo si = response.getServerInfo();
	si.setAgent("I'm a teapot (RFC 2324)");
	super.handle(request, response);
    }
}
