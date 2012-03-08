package net.idea.qmrf.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.rest.db.DatabaseResource;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.qmrf.admin.QMRFCatalogHTMLReporter;
import net.idea.rest.qmrf.admin.QMRFUploadUIResource;
import net.idea.restnet.aa.opensso.policy.OpenSSOPoliciesResource;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.idea.restnet.c.resource.TaskResource;
import net.idea.restnet.rdf.reporter.TaskRDFReporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ResourceException;


public class QMRFAdminResource extends CatalogResource<AdminItem> {
	public static final String resource = "admin";
	protected List<AdminItem> topics = new ArrayList<AdminItem>();
	public QMRFAdminResource() {
		super();
		topics.clear();
		//topics.add(String.format("%s/%s",resource,DatabaseResource.resource));
		topics.add(new AdminItem("QMRF upload",String.format("%s/%s",resource,QMRFUploadUIResource.resource),"Upload new QMRF document"));

		topics.add(new AdminItem("Database admin",String.format("%s/%s",resource,DatabaseResource.resource),"Create or inspect database"));
		topics.add(new AdminItem("System jobs",String.format("%s","task"),"Browse jobs status"));
		topics.add(new AdminItem("OpenSSO policies",String.format("%s/%s",resource,OpenSSOPoliciesResource.resource),"Create or inspect access rights"));
	}
	@Override
	protected Iterator<AdminItem> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		return topics.iterator();
	}
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
	
	protected Reporter createHTMLReporter() {
		return new QMRFCatalogHTMLReporter<AdminItem>(getRequest(),getDocumentation(),getHTMLBeauty(),"QMRF Administration action") {
			@Override
			public String renderItemTitle(AdminItem item) {
				return String.format("%d.%s",record,item.getName());
			}
			@Override
			public String renderItem(AdminItem item) {
				String uri = super.getURI(item).trim();
				return(String.format("<a href='%s'>%s</a>", uri,item.getDescription()));
			}			
			@Override
			protected String printPageNavigator() {
				return "";
			}
		};
	}
}

class AdminItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1844938624688575591L;
	protected String name;
	protected String relativeUri;
	protected String description;
	
	public AdminItem(String name,String relativeURI,String description) {
		setName(name);
		setRelativeUri(relativeURI);
		setDescription(description);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRelativeUri() {
		return relativeUri;
	}
	public void setRelativeUri(String relativeUri) {
		this.relativeUri = relativeUri;
	}
	@Override
	public String toString() {
		return getRelativeUri();
	}
}