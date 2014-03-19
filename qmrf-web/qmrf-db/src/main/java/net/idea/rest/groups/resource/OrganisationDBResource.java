package net.idea.rest.groups.resource;

import java.sql.Connection;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.qmrf.client.Resources;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.user.resource.UserDBResource;
import net.idea.restnet.c.RepresentationConvertor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.convertors.OutputWriterConvertor;
import net.idea.restnet.db.convertors.RDFJenaConvertor;
import net.idea.restnet.groups.CallableGroupCreator;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.GroupType;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.db.ReadGroup;
import net.idea.restnet.groups.db.ReadOrganisation;
import net.idea.restnet.groups.resource.GroupJSONReporter;
import net.idea.restnet.groups.resource.GroupQueryURIReporter;
import net.idea.restnet.groups.resource.GroupRDFReporter;
import net.idea.restnet.user.DBUser;
import net.toxbank.client.io.rdf.TOXBANK;
import net.toxbank.client.resource.Group;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class OrganisationDBResource extends GroupDBResource<DBOrganisation> {

	@Override
	public ReadGroup<DBOrganisation> createGroupQuery(Integer key, String search, String groupName) {
		DBOrganisation p = new DBOrganisation();
		p.setTitle(search);
		p.setGroupName(groupName);
		if (key!=null) p.setID(key);
		ReadOrganisation q = new ReadOrganisation(p);
		return q;
	}
	@Override
	public String getGroupBackLink() {
		return  Resources.organisation;
	}
	@Override
	public String getGroupTitle() {
		return GroupType.ORGANISATION.toString();
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty =  new GroupHTMLBeauty(Resources.organisation);
		return htmlBeauty;
	}

	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			Form form, DBOrganisation item) throws ResourceException {
		Connection conn = null;
		try {
			DBUser user = null;
			Object userKey = getRequest().getAttributes().get(UserDBResource.resourceKey);		
			if ((userKey!=null) && userKey.toString().startsWith("U")) try {
				user = new DBUser(new Integer(Reference.decode(userKey.toString().substring(1))));
			} catch (Exception x) {}
			
			GroupQueryURIReporter r = new GroupQueryURIReporter(getRequest(),"");
			DBConnection dbc = new QDBConnection(getApplication().getContext(),getDbConfig());
			conn = dbc.getConnection();
			return new CallableGroupCreator(method,item,GroupType.ORGANISATION,user,r,form,getRequest().getRootRef().toString(),conn,getToken());
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	};
	
	@Override
	public RepresentationConvertor createConvertor(Variant variant)
			throws AmbitException, ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
				return new StringConvertor(	
						new GroupQueryURIReporter(getRequest())
						,MediaType.TEXT_URI_LIST,filenamePrefix);
		} else if (variant.getMediaType().equals(MediaType.TEXT_CSV)) {
			return new OutputWriterConvertor(
					new GroupCSVReporter<IQueryRetrieval<Group>>(getRequest().getResourceRef()),
					MediaType.TEXT_CSV);
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
			return new OutputWriterConvertor(
					new QMRFGroupJSONReporter<IQueryRetrieval<IDBGroup>>(getRequest()),
					MediaType.APPLICATION_JSON);					
		
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_RDF_XML) ||
					variant.getMediaType().equals(MediaType.APPLICATION_RDF_TURTLE) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_N3) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_NTRIPLES) 
					
					) {
				return new RDFJenaConvertor<IDBGroup, IQueryRetrieval<IDBGroup>>(
						new GroupRDFReporter<IQueryRetrieval<IDBGroup>>(
								getRequest(),variant.getMediaType(),getDocumentation())
						,variant.getMediaType(),filenamePrefix) {
					@Override
					protected String getDefaultNameSpace() {
						return TOXBANK.URI;
					}					
				};
		} else //if (variant.getMediaType().equals(MediaType.TEXT_HTML))
				return new OutputWriterConvertor(
						createHTMLReporter(headless),MediaType.TEXT_HTML);
	}

}
