package net.idea.qmrf.rest;

import java.io.PrintWriter;
import java.io.StringWriter;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.exception.RResourceException;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.service.StatusService;

public class QMRFStatusService extends StatusService {
	protected HTMLBeauty htmlBeauty;
	public HTMLBeauty getHtmlBeauty() {
		return htmlBeauty;
	}

	public void setHtmlBeauty(HTMLBeauty htmlBeauty) {
		this.htmlBeauty = htmlBeauty;
	}

	public QMRFStatusService() {
		super();
		setContactEmail("jeliazkova.nina@gmail.com");
		
	}

	@Override
	public Representation getRepresentation(Status status, Request request,
			Response response) {
		try {
			boolean wrapInHTML = true;
			
			if ((status.getThrowable() !=null) && (status.getThrowable() instanceof RResourceException)) 
				wrapInHTML = ((RResourceException)status.getThrowable()).getVariant().equals(MediaType.TEXT_HTML);
			else {
				//MSIE Accept headers are completely ridiculous
				if ((request.getClientInfo() != null) && "MSIE".equals(request.getClientInfo().getAgentName())) 
					wrapInHTML = true;
				else {
					Form headers = (Form) request.getAttributes().get("org.restlet.http.headers"); 
					String acceptHeader = headers.getValues("accept");
					if (acceptHeader!=null)
						wrapInHTML = acceptHeader.contains("text/html");
				}
			}
			
			if (wrapInHTML) {
				StringWriter w = new StringWriter();
				
				if(htmlBeauty==null) htmlBeauty = new QMRF_HTMLBeauty(Resources.protocol);
				htmlBeauty.writeHTMLHeader(w, status.getName(), request,null);
				
				StringWriter details = null;

				if (status.getThrowable()!= null) {
					 details = new StringWriter();
					status.getThrowable().printStackTrace(new PrintWriter(details) {
						@Override
						public void print(String s) {
							super.print(String.format("%s<br>", s));
							
						}
					});
				} 
				
				String detailsDiv = details==null?"":
					String.format("<a href=\"#\" style=\"background-color: #fff; padding: 5px 10px;\" onClick=\"toggleDiv('%s'); return false;\">Details</a>\n",
							"details");
						
				String errName = status.getName();
				String errDescription = status.getDescription();
				if (Status.CLIENT_ERROR_BAD_REQUEST.equals(status)) errName = "Invalid input";
				if (Status.CLIENT_ERROR_UNAUTHORIZED.equals(status)) {
					errName = "Invalid user name or password";
					errDescription = String.format(
							"The user name or password you provided does not match the QMRF Database records.<br><br>Please try to <a href='%s%s' title='Login to submit new documents'>login</a> again, or <a href='%s%s' title='Register'>register</a> if you are a new user.<br><br>Log in is only required for editors!",
							request.getRootRef(),Resources.login,
							request.getRootRef(),Resources.register
							);
					
				}
				if (Status.CLIENT_ERROR_FORBIDDEN.equals(status)) {
					details = new StringWriter();
					details.append(errDescription);
					errName = "You are not allowed to access this page";
					errDescription = String.format(
							"Only editors are allowed to submit new documents and edit existing ones.<br><br>Please try to <a href='%s%s' title='Login to submit new documents'>login</a> again, or <a href='%s%s' title='Register'>register</a> if you are a new user.<br><br>Log in is only required for editors!",
							request.getRootRef(),Resources.login,
							request.getRootRef(),Resources.register
							);					
				}
				
				w.write(
						String.format(		
						"<div class=\"ui-widget \" style=\"margin-top: 20px; padding: 0 .7em;\">\n"+
						"<div class=\"ui-widget-header ui-corner-top\">" +
						"<span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin-right: .3em;\" title='%s'></span>\n"+
						"<p><a href='%s'>%s</a></p></div>\n"+
						"<div class=\"ui-widget-content ui-corner-bottom \">\n"+
						"<p>%s</p><p>"+
						"%s\n"+	
						"</p>\n"+
						"<div class=\"ui-widget\" style='display: none;' id='details'><p>%s</p></div>\n"+
						"</div></div>\n",
						status.getName(),
						status.getUri(),
						errName,
						errDescription,
						detailsDiv,
						details==null?"":details
						)
				);
				
				if(htmlBeauty==null) htmlBeauty = new QMRF_HTMLBeauty(Resources.protocol);
				htmlBeauty.writeHTMLFooter(w, status.getName(), request);
				return new StringRepresentation(w.toString(),MediaType.TEXT_HTML);
			} else {
				if ((status.getThrowable() !=null) && (status.getThrowable() instanceof RResourceException)) 
					return ((RResourceException)status.getThrowable()).getRepresentation();
			}
			/*
			w.write(String.format("ERROR :<br>Code: %d<br>Name: %s<br>URI: %s<br>Description: %s<br>",
					status.getCode(),
					status.getName(),
					status.getUri(),
					status.getDescription()
					));
	*/

		} catch (Exception x) {
			
		}
		if (status.getThrowable()==null)
			return new StringRepresentation(status.toString(),MediaType.TEXT_PLAIN);
		else {
	    	StringWriter w = new StringWriter();
	    	status.getThrowable().printStackTrace(new PrintWriter(w));

	    	return new StringRepresentation(w.toString(),MediaType.TEXT_PLAIN);	
		}
	}
	@Override
	public Status getStatus(Throwable throwable, Request request,
			Response response) {
		if (throwable == null) return response.getStatus();
		else if (throwable instanceof ResourceException) {
			return ((ResourceException)throwable).getStatus();
		} else return new Status(Status.SERVER_ERROR_INTERNAL,throwable);
	}
}
