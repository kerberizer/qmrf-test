package net.idea.rest.protocol.resource.db;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

import net.idea.modbcum.i.IQueryObject;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.rest.FileResource;
import net.idea.rest.QMRFQueryResource;
import net.idea.rest.protocol.CallableProtocolUpload;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.db.ReadProtocolByAuthor;
import net.idea.rest.protocol.db.ReadProtocolByEndpoint;
import net.idea.rest.protocol.db.ReadProtocolByStructure;
import net.idea.rest.structure.resource.Structure;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.db.ReadUser;
import net.idea.rest.user.resource.UserDBResource;
import net.idea.restnet.c.PageParams;
import net.idea.restnet.c.RepresentationConvertor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.c.task.TaskCreator;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.db.convertors.OutputStreamConvertor;
import net.idea.restnet.db.convertors.OutputWriterConvertor;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.db.convertors.RDFJenaConvertor;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;
import net.toxbank.client.io.rdf.TOXBANK;

import org.apache.commons.fileupload.FileItem;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * Protocol resource
 * @author nina
 *
 * @param <Q>
 */
public class ProtocolDBResource<Q extends IQueryRetrieval<DBProtocol>> extends QMRFQueryResource<Q,DBProtocol> {
	public enum SearchMode {
		text,
		endpoint,
		author
	}
	
	protected boolean singleItem = false;
	protected boolean version = false;
	protected boolean editable = true;
	protected boolean details = true;
	protected Object structure;

	
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		getVariants().clear();
		customizeVariants(new MediaType[] {
				MediaType.TEXT_HTML,
				MediaType.APPLICATION_XML,
				MediaType.TEXT_URI_LIST,
				MediaType.APPLICATION_RDF_XML,
				MediaType.APPLICATION_RDF_TURTLE,
				MediaType.TEXT_RDF_N3,
				MediaType.APPLICATION_PDF,
				MediaType.APPLICATION_EXCEL,
				MediaType.APPLICATION_JAVA_OBJECT
		});		
	}

	@Override
	public RepresentationConvertor createConvertor(Variant variant)
			throws AmbitException, ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
				return new StringConvertor(	
						new ProtocolQueryURIReporter(getRequest())
						,MediaType.TEXT_URI_LIST,filenamePrefix);
				
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_RDF_XML) ||
					variant.getMediaType().equals(MediaType.APPLICATION_RDF_TURTLE) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_N3) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_NTRIPLES)
					
					) {
				return new RDFJenaConvertor<DBProtocol, IQueryRetrieval<DBProtocol>>(
						new ProtocolRDFReporter<IQueryRetrieval<DBProtocol>>(
								getRequest(),variant.getMediaType(),getDocumentation())
						,variant.getMediaType(),filenamePrefix) {
					@Override
					protected String getDefaultNameSpace() {
						return TOXBANK.URI;
					}					
				};
		} else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
			return new OutputWriterConvertor(createHTMLReporter(headless),MediaType.TEXT_HTML);				
		} else if (singleItem && (structure==null)) {
			return new OutputStreamConvertor(new QMRFReporter(getRequest(),variant.getMediaType()),variant.getMediaType());		
		}
		throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
	}
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException {
		ProtocolQueryHTMLReporter rep = new ProtocolQueryHTMLReporter(getRequest(),!singleItem,isEditable(),structure==null,details);
		rep.setHeadless(headless);
		return rep;
	}
	protected boolean isEditable() {
		return editable
			   ?version?true:!singleItem	
			   :false;
	}

	protected Q getProtocolQuery(Object key,int userID,Object search, Object modified, boolean showCreateLink) throws ResourceException {
		
		if (key==null) {
			ReadProtocol query = new ReadProtocol();
			
			if (search != null) {
				DBProtocol p = new DBProtocol();
				p.setTitle(search.toString());
				query.setValue(p);
			} else if (modified != null) try {
				DBProtocol p = new DBProtocol();
				p.setTimeModified(Long.parseLong(modified.toString()));
				query.setValue(p);
			} catch (Exception x) {x.printStackTrace();}
//			query.setFieldname(search.toString());
			editable = showCreateLink;
			if (userID>0) {
				query.setFieldname(new DBUser(userID));
			} else query.setShowUnpublished(false);
			return (Q)query;
		}			
		else {
			editable = showCreateLink;
			singleItem = true;
			int id[] = ReadProtocol.parseIdentifier(Reference.decode(key.toString()));
			ReadProtocol query =  new ReadProtocol(id[0],id[1],id[2]);
			query.setShowUnpublished(true);
			if (userID>0) query.setFieldname(new DBUser(userID));
			return (Q)query;
		}
	}
	

	@Override
	protected void setPaging(Form form, IQueryObject queryObject) {
		String max = form.getFirstValue(max_hits);
		String page = form.getFirstValue(PageParams.params.page.toString());
		String pageSize = form.getFirstValue(PageParams.params.pagesize.toString());
		if (max != null)
		try {
			queryObject.setPage(0);
			queryObject.setPageSize(Long.parseLong(form.getFirstValue(max_hits).toString()));
			return;
		} catch (Exception x) {
			
		}
		try {
			queryObject.setPage(Integer.parseInt(page));
		} catch (Exception x) {
			queryObject.setPageSize(0);
		}
		try {
			queryObject.setPageSize(Long.parseLong(pageSize));
		} catch (Exception x) {
			queryObject.setPageSize(10);
		}			
	}
	
	@Override
	protected Q createQuery(Context context, Request request, Response response)
			throws ResourceException {
		
		Form form = request.getResourceRef().getQueryAsForm();

		Object search = null;
		try {
			search = form.getFirstValue("search").toString();
		} catch (Exception x) {
			search = null;
		}		
		details = true;
		try {
			details = Boolean.parseBoolean(form.getFirstValue("details").toString());
		} catch (Exception x) {
			details = true;
		}				
		Object modified = null;
		try {
			modified = form.getFirstValue("modifiedSince").toString();
		} catch (Exception x) {
			modified = null;
		}			
		boolean showCreateLink = false;
		try {
			String n = form.getFirstValue("new");
			showCreateLink = n==null?false:Boolean.parseBoolean(n);
		} catch (Exception x) {
			showCreateLink = false;
		}
		structure = null;
		try {
			structure = form.getFirstValue("structure").toString();
		} catch (Exception x) {
			structure = null;
		}			
		SearchMode option = SearchMode.text;
		try {
			option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
		} catch (Exception x) {
			option = SearchMode.text;
		}		
			
		Object key = request.getAttributes().get(FileResource.resourceKey);
		int userID = -1;
		try {
			Object userKey = request.getAttributes().get(UserDBResource.resourceKey);
			if (userKey!=null)
				userID = ReadUser.parseIdentifier(userKey.toString());
		} catch (Exception x) {}

		StringCondition c = StringCondition.getInstance(StringCondition.C_REGEXP);
		String param = getParams().getFirstValue(QueryResource.condition.toString());
		try {
			if (param != null)	{
				if ("startswith".equals(param.toLowerCase()))
					c= StringCondition.getInstance(StringCondition.C_STARTS_WITH);
				else
					c = StringCondition.getInstance(param);
			}
		} catch (Exception x) {	
		} finally {
		}		
		try {
			if (search!=null)
				switch (option) {
				case author: {
					IQueryRetrieval<DBProtocol> query = new ReadProtocolByAuthor();
					
					((ReadProtocolByAuthor)query).setFieldname(search.toString().trim());
					editable = showCreateLink;
					singleItem = false;				
					return (Q)query;
				}
				case endpoint: {
					IQueryRetrieval<DBProtocol> query = new ReadProtocolByEndpoint();
					
					((ReadProtocolByEndpoint)query).setFieldname(search.toString().trim());
					((ReadProtocolByEndpoint)query).setCondition(c);
					editable = showCreateLink;
					singleItem = false;				
					return (Q)query;
				}
				}
			if ((structure!=null) && structure.toString().startsWith("http")) {
				IQueryRetrieval<DBProtocol> query = new ReadProtocolByStructure();
				Structure record = new Structure();
				record.setResourceURL(new URL(structure.toString()));

				Object[] ids = record.parseURI(new Reference(getQueryService()));
				record.setIdchemical((Integer)ids[0]);
				record.setIdstructure((Integer)ids[1]);
				((ReadProtocolByStructure)query).setFieldname(record);
				editable = showCreateLink;
				singleItem = false;				
				return (Q)query;
			} else return getProtocolQuery(key,userID,search,modified,showCreateLink);
		}catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(
					Status.CLIENT_ERROR_BAD_REQUEST,
					String.format("Invalid protocol id %s",key),
					x
					);
		}
	} 

	@Override
	protected QueryURIReporter<DBProtocol, Q> getURUReporter(
			Request baseReference) throws ResourceException {
		return new ProtocolQueryURIReporter(getRequest());
	}

	
	@Override
	protected boolean isAllowedMediaType(MediaType mediaType)
			throws ResourceException {
		return MediaType.MULTIPART_FORM_DATA.equals(mediaType);
	}

	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			Form form, DBProtocol item) throws ResourceException {
		if (Method.DELETE.equals(method))
			return createCallable(method,(List<FileItem>) null, item);
		else throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED,method.toString());
	}
	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			List<FileItem> input, DBProtocol item) throws ResourceException {
		/*
		if ((getRequest().getClientInfo().getUser()==null) ||
				getRequest().getClientInfo().getUser().getIdentifier()==null)
				throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
			
		DBUser user = new DBUser();
		user.setUserName(getRequest().getClientInfo().getUser().getIdentifier());
		*/
		Connection conn = null;
		try {
			ProtocolQueryURIReporter r = new ProtocolQueryURIReporter(getRequest(),"");
			class TDBConnection extends DBConnection {
				public TDBConnection(Context context,String configFile) {
					super(context,configFile);
				}
				public String getDir() {
					loadProperties();
					return getAttachmentDir();
				}
			};
			TDBConnection dbc = new TDBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection(getRequest());

			String dir = dbc.getDir();
			if ("".equals(dir)) dir = null;
			return new CallableProtocolUpload(method,item,null,input,conn,r,getToken(),getRequest().getRootRef().toString(),
						dir==null?null:new File(dir)
			);
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}

	}
	
	@Override
	protected Q createUpdateQuery(Method method, Context context,
			Request request, Response response) throws ResourceException {
		Object key = request.getAttributes().get(FileResource.resourceKey);
		if (Method.POST.equals(method)) {
			if (key==null) return null;//post allowed only on /protocol level, not on /protocol/id
		} else if (Method.DELETE.equals(method)) {
			if (key!=null) return super.createUpdateQuery(method, context, request, response);
		} else if (Method.PUT.equals(method)) {
			if (key!=null) {
				int id[] = ReadProtocol.parseIdentifier(Reference.decode(key.toString()));
				return (Q)new ReadProtocol(id[0],id[1],id[2]);
			}
		}
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);		
	}
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF(storage,getHTMLBeauty());
	}
	
	
	protected TaskCreator getTaskCreator(Form form, final Method method, boolean async, final Reference reference) throws Exception {
		if (Method.DELETE.equals(method))
			return super.getTaskCreator(form, method, async, reference);
		else
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Not multipart web form!");
	}
}
