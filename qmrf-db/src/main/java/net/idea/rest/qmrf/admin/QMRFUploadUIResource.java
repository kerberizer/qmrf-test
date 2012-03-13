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
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.toxbank.client.resource.Organisation;
import net.toxbank.client.resource.Project;
import net.toxbank.client.resource.User;

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
	
	public QMRFUploadUIResource() {
		super();
	}
	@Override
	protected Iterator<DBProtocol> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		try { 
			Object key = request.getAttributes().get(FileResource.resourceKey);
			if (key!=null)  
			try { //add attachments to a protocol
				int ids[] = ReadProtocol.parseIdentifier(key.toString());
				protocol = new DBProtocol(ids[0],ids[1],ids[2]);
				protocol.setIdentifier(ReadProtocol.generateIdentifier(protocol));
				protocol.setResourceURL(new URL(String.format("%s%s/%s", getRequest().getRootRef(),Resources.protocol, protocol.getIdentifier())));
			} catch (Exception x) {
				throw new InvalidQMRFNumberException(key.toString());
			}
			else protocol=null;
			//else //new protocol
		} catch (Exception x) {}
		
		return items.iterator();
	}
	protected Reporter createHTMLReporter() {
		return new QMRFCatalogHTMLReporter<DBProtocol>(getRequest(),getDocumentation(),getHTMLBeauty(),null) {
			@Override
			public void header(Writer w, Iterator<DBProtocol> query) {
				super.header(w, query);
				String uri = protocol==null?String.format("%s%s",getRequest().getRootRef().toString(), Resources.protocol):
					String.format("%s/%s",protocol.getResourceURL(),Resources.attachment);
				boolean attachments = true;
				try {
					if (protocol==null) {
						attachments = false;
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
					w.write(((QMRF_HTMLBeauty)htmlBeauty).printUploadForm(uri,uri, protocol,attachments));
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
			@Override
			protected String printPageNavigator() {
				return "";
			}
		};
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
}
