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
import net.idea.rest.JSONUtils;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.attachments.AttachmentURIReporter;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.DBAttachment.attachment_type;
import net.idea.rest.protocol.attachments.db.ReadAttachment;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.resource.GroupQueryURIReporter;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

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
	
	private static String format = "\n{\n\t\"uri\":\"%s\",\n\t\"visibleid\": \"%s\",\n\t\"identifier\": \"%s\",\n\t\"title\": \"%s\",\n\t\"published\": %s,\n\t\"endpoint\": {\n\t\t\"parentCode\" :\"%s\",\"parentName\" :\"%s\",\"code\" :\"%s\", \"name\" :\"%s\"\n\t},\n\t\"submitted\": \"%s\",\n\t\"updated\": \"%s\",\n\t\"owner\": {\n\t\t\"uri\" :\"%s\",\n\t\t\"username\": \"%s\",\n\t\t\"firstname\": \"%s\",\n\t\t\"lastname\": \"%s\"\n\t}";
	private static String formatAttachments =  "\n\t\t{\n\t\t\"uri\":\"%s\",\n\t\t\"title\":\"%s\",\n\t\t\"description\":\"%s\",\n\t\t\"type\":\"%s\",\n\t\t\t\"dataset\": {\"uri\": \"%s/dataset/%d\", \"structure\": null}\n\t\t}";
	private static String formatAttachmentDocument =  "\n\t\t{\n\t\t\"uri\":\"%s\",\n\t\t\"title\":\"%s\",\n\t\t\"description\":\"%s\",\n\t\t\"type\":\"%s\"\n\t\t}";
		
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
			
			//private static String format = "\n{\n\t\"uri\":\"%s\",\n\t\"visibleid\": \"%s
			getOutput().write("\n{\n\t\"uri\":\"");
			getOutput().write(uri);
			getOutput().write("\",\n\t\"visibleid\": \"");
			getOutput().write(item.getVisibleIdentifier());
			getOutput().write("\",\n\t\"identifier\": \"");
			getOutput().write(item.getIdentifier());
			getOutput().write("\",\n\t\"title\": \"");
			getOutput().write(JSONUtils.jsonEscape(item.getTitle()));
			getOutput().write("\",\n\t\"published\": ");
			getOutput().write(Boolean.toString(item.isPublished()));
			getOutput().write(",\n\t\"endpoint\": {\n\t\t\"parentCode\" :\"");
			getOutput().write(item.getEndpoint().getParentCode()==null?"":item.getEndpoint().getParentCode());
			getOutput().write("\",\"parentName\" :\"");
			getOutput().write(item.getEndpoint().getParentTemplate());
			getOutput().write("\",\"code\" :\"");
			getOutput().write(item.getEndpoint().getCode()==null?"":item.getEndpoint().getCode());
			getOutput().write("\", \"name\" :\"");
			getOutput().write(item.getEndpoint().getName());
			getOutput().write("\"\n\t},\n\t\"submitted\": \"");
			getOutput().write(dateFormat.format(new Date(item.getSubmissionDate())));
			getOutput().write("\",\n\t\"updated\": \"");
			getOutput().write(dateFormat.format(new Date(item.getTimeModified())));
			getOutput().write("\",\n\t\"owner\": {\n\t\t\"uri\" :\"");
			getOutput().write(item.getOwner().getResourceURL().toExternalForm());
			getOutput().write("\",\n\t\t\"username\": \"");
			getOutput().write(item.getOwner().getUserName()==null?"":JSONUtils.jsonEscape(item.getOwner().getUserName()));
			getOutput().write("\",\n\t\t\"firstname\": \"");
			getOutput().write(item.getOwner().getFirstname()==null?"":JSONUtils.jsonEscape(item.getOwner().getFirstname()));
			getOutput().write("\",\n\t\t\"lastname\": \"");
			getOutput().write(item.getOwner().getLastname()==null?"":JSONUtils.jsonEscape(item.getOwner().getLastname()));
			getOutput().write("\"\n\t}");
			/*
			getOutput().write(String.format(format,
					uri,
					item.getVisibleIdentifier(),
					item.getIdentifier(),
					item.getTitle().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
					item.isPublished(),
					item.getEndpoint().getParentCode()==null?"":item.getEndpoint().getParentCode(),
					item.getEndpoint().getParentTemplate(),
					item.getEndpoint().getCode()==null?"":item.getEndpoint().getCode(),
					item.getEndpoint().getName(),
					dateFormat.format(new Date(item.getSubmissionDate())),
					dateFormat.format(new Date(item.getTimeModified())),
					item.getOwner().getResourceURL(),
					item.getOwner().getUserName()==null?"":item.getOwner().getUserName(),
					item.getOwner().getFirstname()==null?"":item.getOwner().getFirstname(),
					item.getOwner().getLastname()==null?"":item.getOwner().getLastname()
					));
					*/
			if (item.getAttachments()!=null){
				getOutput().write(",\n\t\"attachments\": [");
				for (int na = 0; na < item.getAttachments().size(); na++) {

					DBAttachment attachment = item.getAttachments().get(na);
					if (na>0)
						getOutput().write(",");
					if (attachment_type.document.equals(attachment.getType())) {
						getOutput().write("\n\t\t{\n\t\t\"uri\":\"");
						getOutput().write(attachment.getResourceURL().toExternalForm());
						getOutput().write("\",\n\t\t\"title\":\"");
						getOutput().write(JSONUtils.jsonEscape(attachment.getTitle()));
						getOutput().write("\",\n\t\t\"description\":\"");
						getOutput().write(JSONUtils.jsonEscape(attachment.getDescription()));
						getOutput().write("\",\n\t\t\"type\":\"");
						getOutput().write(JSONUtils.jsonEscape(attachment.getType().toString()));
						getOutput().write("\"\n\t\t}");
						/*
						getOutput().write(String.format(formatAttachmentDocument,
								attachment.getResourceURL(),
								attachment.getTitle().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
								attachment.getDescription().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
								attachment.getType().toString()));
								*/						
					} else {	
						getOutput().write("\n\t\t{\n\t\t\"uri\":\"" );
						getOutput().write(attachment.getResourceURL().toExternalForm());
						getOutput().write("\",\n\t\t\"title\":\"" );
						getOutput().write(JSONUtils.jsonEscape(attachment.getTitle()));
						getOutput().write("\",\n\t\t\"description\":\"");
						getOutput().write(JSONUtils.jsonEscape(attachment.getDescription()));
						getOutput().write("\",\n\t\t\"type\":\"");
						getOutput().write(JSONUtils.jsonEscape(attachment.getType().toString()));
						getOutput().write("\",\n\t\t\t\"dataset\": {\"uri\": \"");
						getOutput().write(queryService);
						getOutput().write("/dataset/");
						getOutput().write(Integer.toString(attachment.getIdquerydatabase()));
						getOutput().write("\", \"structure\": null}\n\t\t}");
						/*
						getOutput().write(String.format(formatAttachments,
								attachment.getResourceURL(),
								attachment.getTitle().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
								attachment.getDescription().replace("\n", " ").replace("\r", " ").replace("\"", "'"),
								attachment.getType().toString(),
								queryService,
								attachment.getIdquerydatabase()));
								*/
					}
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
