package net.idea.qmrf.aa;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.idea.qmrf.client.QMRFRoles;
import net.idea.restnet.aa.local.UserLoginHTMLReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Reference;
import org.restlet.security.Role;
import org.restlet.security.User;

public class QMRFLoginFormReporter<U extends User> extends UserLoginHTMLReporter<U> {
	protected transient Logger logger = Logger.getLogger(getClass().getName());
	private String js_validate;
    public QMRFLoginFormReporter(Request ref, ResourceDoc doc) {
    	this(ref,doc,null);
    }
	public QMRFLoginFormReporter(Request ref, ResourceDoc doc,HTMLBeauty htmlBeauty) {
		super(ref,doc,htmlBeauty);
		js_validate = String.format("<script type='text/javascript' src='%s/jquery/jquery.validate.min.js'></script>\n",ref.getRootRef());
	}
	
	
	@Override
	public void header(Writer output, Iterator<U> query) {
		try {
			if (htmlBeauty==null) htmlBeauty = new HTMLBeauty();
			String meta = js_validate ;
			htmlBeauty.writeHTMLHeader(output, htmlBeauty.getTitle(), getRequest(),meta,getDocumentation()
					);
		} catch (Exception x) {
			
		}
	}
	public void processItem(U item, Writer output) {
		
		try {
			String redirect = Reference.encode(String.format("%s/login",baseReference));
			String header = "";
			StringWriter writer = new StringWriter();
			writer.write("<table width='80%' id='users' border='0' cellpadding='0' cellspacing='1'>");
			writer.write("<tbody>");			
			if (item.getIdentifier()==null) {
				header = "Sign In";
	
				writer.write(String.format("<form method='post' action='%s/protected/signin?targetUri=%s' id='loginForm' autocomplete='off'>",baseReference,redirect));
					
				writer.write(String.format("<tr><th align='right'>%s</th><td><input type='text' size='40' id='login' name='%s' value='' required minLength='3'></td><td></td></tr>",
							"User name:&nbsp;","login"));
				writer.write(String.format("<tr><th align='right'>%s</th><td><input type='password' size='40' id='password' name='%s' value='' required></td><td><a href='%s/forgotten' title='Click to request one time password reset'>Forgotten password?</a></td></tr>",
						"Password:&nbsp;","password",baseReference));
				writer.write("<tr><td></td><td><input align='bottom' class='submit' type=\"submit\" value=\"Log in\"></td></tr>");
				//writer.write(String.format("<tr><th align='right'></th><td><input type='hidden' size='40' name='targetURI' value='%s/login'></td></tr>",baseReference));
				
				writer.write("</form>");

			} else {
				header = "&nbsp;";
				writer.write(String.format("<form method='post' action='%s/protected/signout?targetUri=%s'>",baseReference,redirect));
				writer.write(String.format("<tr><td width='25%%' align='right'>%s</td><th align='left'>%s</th></tr>","You are logged in as&nbsp;",item.getIdentifier()));
				
				for (Role role:getRequest().getClientInfo().getRoles()) {
					QMRFRoles qmrfrole = QMRFRoles.valueOf(role.getName());
					if (qmrfrole.getURI()==null) continue;
					writer.write(String.format("<tr><td align='right'>%s</td><th>%s&nbsp;(%s)</th></tr>","Role:&nbsp;",qmrfrole.toString(),qmrfrole.getHint()));
				}
				writer.write("<tr><td align='right'></td><td align='left'><input align='bottom' type=\"submit\" value=\"Log out\"></td></tr>");
				writer.write("</form>");
				
				writer.write("</tbody></table>");
				
		     }
			writer.write("</tbody></table>");
			output.write(htmlBeauty.printWidget(header, writer.toString()));
		} catch (Exception x) {
			logger.log(Level.WARNING,x.getMessage(),x);
		}
		
	};
	
	protected String myWorkspaceLinks() {
		return null;
	}	

}
