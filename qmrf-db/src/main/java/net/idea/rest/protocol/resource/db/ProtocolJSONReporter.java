package net.idea.rest.protocol.resource.db;

import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.modbcum.r.QueryReporter;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.DBProject;
import net.idea.rest.groups.IDBGroup;
import net.idea.rest.groups.resource.GroupQueryURIReporter;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.attachments.AttachmentURIReporter;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.db.ReadAttachment;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.resource.UserURIReporter;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;

public class ProtocolJSONReporter extends QueryReporter<DBProtocol, IQueryRetrieval<DBProtocol>,Writer>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3537339785122677311L;
	protected String queryService;
	protected String comma = null;
	protected QueryURIReporter uriReporter;
	protected AttachmentURIReporter<IQueryRetrieval<DBAttachment>> attachmentReporter;
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
	
	public QueryURIReporter getUriReporter() {
		return uriReporter;
	}

	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;
	
	
	public ProtocolJSONReporter(Request request,String queryService) {
		super();
		this.queryService = queryService;
		uriReporter = new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(request);
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
		attachmentReporter = new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(request);
		getProcessors().clear();
		IQueryRetrieval<DBAttachment> queryP = new ReadAttachment(null,null); 
		MasterDetailsProcessor<DBProtocol,DBAttachment,IQueryCondition> attachmentReader = new MasterDetailsProcessor<DBProtocol,DBAttachment,IQueryCondition>(queryP) {
			@Override
			protected DBProtocol processDetail(DBProtocol target, DBAttachment detail)
					throws Exception {
				
				detail.setResourceURL(new URL(attachmentReporter.getURI(detail)));
				target.getAttachments().add(detail);
				return target;
			}
		};
		getProcessors().add(attachmentReader);		
		processors.add(new DefaultAmbitProcessor<DBProtocol,DBProtocol>() {
			@Override
			public DBProtocol process(DBProtocol target) throws AmbitException {
				try {
				processItem(target);
				} catch (AmbitException x) {
					throw x;
				} catch (Exception x) {
					throw new AmbitException(x);
				}
				return target;
			};
		});						
	}
	
	@Override
	public void header(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("{\"qmrf\": [");
		} catch (Exception x) {}
		
	}
	
	private static String format = "\n{\n\t\"uri\":\"%s\",\n\t\"visibleid\": \"%s\",\n\t\"identifier\": \"%s\",\n\t\"title\": \"%s\",\n\t\"published\": %s,\n\t\"endpoint\": {\n\t\t\"code\" :null, \"name\" :null\n\t},\n\t\"submitted\": \"%s\",\n\t\"updated\": \"%s\",\n\t\"owner\": {\n\t\t\"uri\" :\"%s\",\n\t\t\"username\": \"%s\",\n\t\t\"firstname\": \"%s\",\n\t\t\"lastname\": \"%s\"\n\t}";
	private static String formatAttachments =  "\n\t\t{\n\t\t\"uri\":\"%s\",\n\t\t\"title\":\"%s\",\n\t\t\"description\":\"%s\",\n\t\t\"type\":\"%s\",\n\t\t\t\"dataset\": {\"uri\": \"%s/dataset/%d\", \"structure\": null}\n\t\t}";
		
	@Override
	public Object processItem(DBProtocol item) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			String uri = uriReporter.getURI(item);
		
			if ((item.getProject()!=null) && (item.getProject().getResourceURL()==null))
				item.getProject().setResourceURL(new URL(groupReporter.getURI((DBProject)item.getProject())));
			if ((item.getOrganisation()!=null) && (item.getOrganisation().getResourceURL()==null))
				item.getOrganisation().setResourceURL(new URL(groupReporter.getURI((DBOrganisation)item.getOrganisation())));
			if ((item.getOwner()!=null) && (item.getOwner().getResourceURL()==null))
				item.getOwner().setResourceURL(new URL(userReporter.getURI((DBUser)item.getOwner())));
			

			getOutput().write(String.format(format,
					uri,
					item.getVisibleIdentifier(),
					item.getIdentifier(),
					item.getTitle().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
					item.isPublished(),
					dateFormat.format(new Date(item.getSubmissionDate())),
					dateFormat.format(new Date(item.getTimeModified())),
					item.getOwner().getResourceURL(),
					item.getOwner().getUserName()==null?"":item.getOwner().getUserName(),
					item.getOwner().getFirstname()==null?"":item.getOwner().getFirstname(),
					item.getOwner().getLastname()==null?"":item.getOwner().getLastname()
					));
			if (item.getAttachments()!=null){
				getOutput().write(",\n\t\"attachments\": [");
				for (int na = 0; na < item.getAttachments().size(); na++) {

					DBAttachment attachment = item.getAttachments().get(na);
					if (na>0)
						getOutput().write(",");
					getOutput().write(String.format(formatAttachments,
							attachment.getResourceURL(),
							attachment.getTitle().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
							attachment.getDescription().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
							attachment.getType().toString(),
							queryService,
							attachment.getIdquerydatabase()));
				}			
				getOutput().write("\n\t]\n");
			}
		//	\"substrate\":{\"uri\":\"%s\"},\n\"product\":{\"uri\":%s%s%s}			
			getOutput().write("\n}");
			comma = ",";
		} catch (Exception x) {
		}
		return item;
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBProtocol> query) {
		try {
			output.write("\n]\n}");
		} catch (Exception x) {}
	}
}
