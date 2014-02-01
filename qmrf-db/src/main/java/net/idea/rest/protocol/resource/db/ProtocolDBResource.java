package net.idea.rest.protocol.resource.db;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import net.idea.modbcum.i.IQueryObject;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.QMRFQueryResource;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.db.exceptions.InvalidQMRFNumberException;
import net.idea.rest.db.exceptions.MethodNotAllowedException;
import net.idea.rest.endpoints.EndpointTest;
import net.idea.rest.protocol.CallableProtocolUpload;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.db.ReadProtocolByAuthor;
import net.idea.rest.protocol.db.ReadProtocolByEndpoint;
import net.idea.rest.protocol.db.ReadProtocolByEndpointString;
import net.idea.rest.protocol.db.ReadProtocolByStructure;
import net.idea.rest.protocol.db.ReadProtocolByTextSearch;
import net.idea.rest.protocol.db.ReadProtocolByTextSearch.TextSearchMode;
import net.idea.rest.structure.resource.Structure;
import net.idea.rest.user.resource.UserDBResource;
import net.idea.restnet.c.PageParams;
import net.idea.restnet.c.RepresentationConvertor;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.freemarker.FreeMarkerApplicaton;
import net.idea.restnet.c.html.HTMLBeauty;
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
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;
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
import org.restlet.representation.Representation;
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
		title,
		text,
		textboolean,
		endpoint,
		endpointcode,
		author,
		software,
		descriptor,
		qmrfnumber
	}
	
	protected boolean singleItem = false;
	protected boolean version = false;
	protected boolean editable = true;
	protected boolean details = true;
	protected Object structure;

	public ProtocolDBResource() {
		super();
		setHtmlbyTemplate(true);
	}
	

	@Override
	public String getTemplateName() {
		return singleItem?"protocols_body.ftl":"protocols_body.ftl";
	}
	
	@Override
	public boolean isHtmlbyTemplate() {
		// TODO Auto-generated method stub
		return singleItem || headless?false:freeMarkerSupport.isHtmlbyTemplate();
	}
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		getVariants().clear();
		customizeVariants(new MediaType[] {
				MediaType.TEXT_HTML,
				MediaType.APPLICATION_XML,
				MediaType.APPLICATION_JSON,
				MediaType.TEXT_URI_LIST,
				MediaType.TEXT_CSV,
				MediaType.APPLICATION_RDF_XML,
				MediaType.APPLICATION_RDF_TURTLE,
				MediaType.TEXT_RDF_N3,
				MediaType.APPLICATION_PDF,
				MediaType.APPLICATION_EXCEL,
				MediaType.APPLICATION_WORD,
				MediaType.APPLICATION_RTF,
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
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
			String queryService = ((TaskApplication) getApplication())
									.getProperty(Resources.Config.qmrf_ambit_service.name());
			ProtocolJSONReporter r = new ProtocolJSONReporter(getRequest(),queryService);
			return new StringConvertor(	r,MediaType.APPLICATION_JSON,filenamePrefix);
		} else if (variant.getMediaType().equals(MediaType.TEXT_CSV)) {
			String queryService = ((TaskApplication) getApplication())
									.getProperty(Resources.Config.qmrf_ambit_service.name());
			ProtocolCSVReporter r = new ProtocolCSVReporter(getRequest(),variant.getMediaType(),queryService);
			return new StringConvertor(	r,MediaType.TEXT_CSV,filenamePrefix);
											
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
		} else if (variant.getMediaType().equals(MediaType.APPLICATION_WORD)) {
			QMRFChaptersHTMLReporter rep = new QMRFChaptersHTMLReporter(getRequest(),false,false,structure==null,details);
			rep.setHeadless(true);
			rep.setHtmlBeauty(getHTMLBeauty());
			rep.setDtdresolver(((TaskApplication)getApplication()).getResolver());
			rep.setPlainHtmlHeader(true);
			return new OutputWriterConvertor(rep,MediaType.APPLICATION_WORD);
		} else if (singleItem && (structure==null)) {
			
			Object fileNamePrefix = getRequest().getAttributes().get(FileResource.resourceKey);
			return new OutputStreamConvertor(new QMRFReporter(getRequest(),variant.getMediaType(),
					((FreeMarkerApplicaton<String>)getApplication()).getConfiguration(),
					((TaskApplication)getApplication()).getResolver()
					),
					variant.getMediaType(),filenamePrefix);		
		} else return new OutputWriterConvertor(createHTMLReporter(headless),MediaType.TEXT_HTML);		
	}
	
	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException {
		ProtocolQueryHTMLReporter rep = new ProtocolQueryHTMLReporter(getRequest(),!singleItem,isEditable(),structure==null,details);
		rep.setHeadless(headless);
		rep.setHtmlBeauty(getHTMLBeauty());
		return rep;
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) {
			htmlBeauty = new QMRF_HTMLBeauty(Resources.protocol);
		}
		return htmlBeauty;
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
			} catch (Exception x) {logger.log(Level.SEVERE,x.getMessage(),x);}
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
			/*
			int id[] = ReadProtocol.parseIdentifier(Reference.decode(key.toString()));
			ReadProtocol query =  new ReadProtocol(id[0],id[1],id[2]);
			*/
			ReadProtocol query = new ReadProtocol(Reference.decode(key.toString()));
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
			queryObject.setPageSize(100);
		}			
	}
	
	protected Map<String,Object> query2map(Request request) {
		Form form = getParams();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String value = form.getFirstValue("search").toString();
			if (value.contains("script")) map.remove("search"); 
			else map.put("search",value);
		} catch (Exception x) {	
			map.remove("search");
		}		
		try {
			String value = form.getFirstValue("structure").toString();
			if (value.contains("script")) map.remove("structure"); 
			else map.put("structure",value);
		} catch (Exception x) { map.remove("structure"); }
		try {
			map.put("option",SearchMode.valueOf(form.getFirstValue("option").toLowerCase()).name());
		} catch (Exception x) {
			map.put("option",SearchMode.text.name());
		}			
		try {
			map.put("pagesize",queryObject.getPageSize());
		} catch (Exception x) { }
		try {
			map.put("page",queryObject.getPage());
		} catch (Exception x) {}					
		return map;
	}
	
	@Override
	protected Map<String, Object> getMap(Variant variant)
			throws ResourceException {
		Map<String, Object> map =  super.getMap(variant);
		map.put("query", query2map(getRequest()));
		return map;
	}
	@Override
	protected Q createQuery(Context context, Request request, Response response)
			throws ResourceException {
		
		Form form = request.getResourceRef().getQueryAsForm();
		DBProtocol queryObject = null;
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
			if (modified != null) {
				queryObject = new DBProtocol();
				queryObject.setTimeModified(Long.parseLong(modified.toString()));		
			}	
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
					((ReadProtocolByAuthor)query).setValue(queryObject);
					editable = showCreateLink;
					singleItem = false;				
					return (Q)query;
				}
				case text: {
					IQueryRetrieval<DBProtocol> query = new ReadProtocolByTextSearch();
					((ReadProtocolByTextSearch)query).setFieldname(search.toString().trim());
					((ReadProtocolByTextSearch)query).setValue(queryObject);
					editable = showCreateLink;
					singleItem = false;				
					return (Q)query;
				}	
				case textboolean: {
					IQueryRetrieval<DBProtocol> query = new ReadProtocolByTextSearch(TextSearchMode.textboolean);
					((ReadProtocolByTextSearch)query).setFieldname(search.toString().trim());
					((ReadProtocolByTextSearch)query).setValue(queryObject);
					editable = showCreateLink;
					singleItem = false;				
					return (Q)query;
				}				
				case endpointcode: {
					IQueryRetrieval<DBProtocol> query = new ReadProtocolByEndpoint();
					EndpointTest endpointTest = new EndpointTest(null,null);
					endpointTest.setCode("undefined".equals(search)?null:search.toString().trim());
					((ReadProtocolByEndpoint)query).setFieldname(endpointTest);
					((ReadProtocolByEndpoint)query).setValue(queryObject);
					editable = showCreateLink;
					singleItem = false;				
					return (Q)query;
				}				
				case endpoint: {
					IQueryRetrieval<DBProtocol> query = new ReadProtocolByEndpointString();
					
					((ReadProtocolByEndpointString)query).setFieldname(search.toString().trim());
					((ReadProtocolByEndpointString)query).setCondition(c);
					((ReadProtocolByEndpointString)query).setValue(queryObject);
					editable = showCreateLink;
					singleItem = false;				
					return (Q)query;
				}
				case qmrfnumber: {
					try {
						IQueryRetrieval<DBProtocol> query = new ReadProtocol(search.toString().trim());
						editable = showCreateLink;
						singleItem = false;				
						return (Q)query;						
					} catch (Exception x) {
						throw new InvalidQMRFNumberException(search.toString());

					}

				}		
				}
			if ((structure!=null) && structure.toString().startsWith("http")) {
				IQueryRetrieval<DBProtocol> query = new ReadProtocolByStructure();
				Structure record = new Structure();
				record.setResourceIdentifier(new URL(structure.toString()));

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
			throw new InvalidQMRFNumberException(key==null?"":key.toString());
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
		else throw new MethodNotAllowedException(getRequest().getResourceRef(),method);

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
		DBUser user = null;
		
		if ((getRequest().getClientInfo().getUser()==null) || (getRequest().getClientInfo().getUser().getIdentifier()==null)) {
			user = null;
			//we have default user and POST is protected by a filter
			/*
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED.getCode(),
						"Upload not allowed",
						"Only logged in users with editor rights may upload new documents",
						Status.CLIENT_ERROR_UNAUTHORIZED.getUri());
						*/
		} else {
			user = new DBUser();
			user.setUserName(getRequest().getClientInfo().getUser().getIdentifier());
		}	
		Connection conn = null;
		DBConnection dbc = null;
		try {
			ProtocolQueryURIReporter r = new ProtocolQueryURIReporter(getRequest(),"");
			class TDBConnection extends QDBConnection {
				public TDBConnection(Context context,Properties configFile) {
					super(context,configFile);
				}
				public String getDir() {
					loadProperties();
					return getAttachmentDir();
				}
			};
			dbc = new TDBConnection(getApplication().getContext(),getDbConfig());
			conn = dbc.getConnection();

			String dir = ((TDBConnection)dbc).getDir();
			if ("".equals(dir)) dir = null;
			return new CallableProtocolUpload(method,item,user,input,conn,r,getToken(),getRequest().getRootRef().toString(),
						dir==null?null:new File(dir),
						((TaskApplication)getApplication()).getResolver()
			);
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		} finally {
			dbc = null;
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
				return (Q)new ReadProtocol(Reference.decode(key.toString()));
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
		if (Method.DELETE.equals(method)) {
			TaskCreator taskCreator = super.getTaskCreator(form, method, async, reference);
			taskCreator.getProcessors().setAbortOnError(true);
			return taskCreator;
		} else
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Not multipart web form!");
	}
	
	@Override
	protected TaskCreator getTaskCreator(Representation entity,
			Variant variant, Method method, boolean async) throws Exception {
		TaskCreator taskCreator = super.getTaskCreator(entity,variant,method,async);
		taskCreator.getProcessors().setAbortOnError(true);
		return taskCreator;
	}

}
