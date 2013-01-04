package net.idea.rest.qmrf.admin;

import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.db.exceptions.InvalidQMRFNumberException;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.QMRF_HTMLBeauty.update_mode;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.toxbank.client.resource.Organisation;
import net.toxbank.client.resource.Project;
import net.toxbank.client.resource.User;

import org.apache.log4j.Level;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ResourceException;

public class QMRFUploadUIResource extends CatalogResource<DBProtocol> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1658864353613787327L;
	protected List<DBProtocol> items = new ArrayList<DBProtocol>();
	protected DBProtocol protocol = null;
	protected update_mode mode = update_mode.newdocument;
	
	public QMRFUploadUIResource() {
		super();
	}
	@Override
	protected Iterator<DBProtocol> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		try {
			mode = update_mode.valueOf(request.getResourceRef().getQueryAsForm().getFirstValue("mode").toString());
		} catch (Exception x) {mode = update_mode.newdocument;}		
		try { 
			Object key = request.getAttributes().get(FileResource.resourceKey);
			if (key!=null)  
			try { //add attachments to a protocol
				protocol = new DBProtocol(key.toString());
				protocol.setResourceURL(new URL(String.format("%s%s/%s", getRequest().getRootRef(),Resources.protocol, protocol.getIdentifier())));
				if (update_mode.newdocument.equals(mode)) mode = update_mode.attachments;
			} catch (Exception x) {
				throw new InvalidQMRFNumberException(key.toString());
			}
			else { protocol=null; mode = update_mode.newdocument;}
			//else //new protocol
		} catch (Exception x) {}


		return items.iterator();
	}
	protected Reporter createHTMLReporter(boolean headles) {
		return new QMRFCatalogHTMLReporter<DBProtocol>(getRequest(),getDocumentation(),getHTMLBeauty(),null) {
			@Override
			public void header(Writer w, Iterator<DBProtocol> query) {
				super.header(w, query);
				String uri = protocol==null?String.format("%s%s",getRequest().getRootRef().toString(), Resources.protocol):
					String.format("%s%s",protocol.getResourceURL(),Resources.attachment);
				boolean attachments = true;
				try {
					if (protocol==null) {

						protocol= new DBProtocol();
						protocol.setPublished(true);
						protocol.setOrganisation(new Organisation(new URL(String.format("%s%s",getRequest().getRootRef(),
						((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_default_organisation.name())))));
						protocol.setProject(new Project(new URL(String.format("%s%s",getRequest().getRootRef(),
								((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_default_project.name())))));
						protocol.setOwner(new User(new URL(String.format("%s%s",getRequest().getRootRef(),
								((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_default_owner.name())))));	
					}
				} catch (Exception x) {
					logger.debug(x);
					protocol = null;
				}
				try {
					w.write(((QMRF_HTMLBeauty)htmlBeauty).printUploadForm(uri,uri, protocol,mode,request.getRootRef().toString()));
				} catch (Exception x) {
					logger.error(x.getMessage(),x);
				}
			}
			@Override
			protected String printPageNavigator() {
				// want to mimic the paging style, probably could be done in a better way
				return	String.format(
						"<div style='background-color: #109DFF;font-weight:bold;border: 1px solid #109DFF;'><p style='color:#ffffff;'>%s QMRF document</p></div>\n",
						mode.toString());
			
			}
		};
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.editor);
	}
}
