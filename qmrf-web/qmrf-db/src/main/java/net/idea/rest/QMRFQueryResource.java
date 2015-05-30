package net.idea.rest;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import net.idea.modbcum.i.IDBProcessor;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.exceptions.BatchProcessingException;
import net.idea.modbcum.i.exceptions.NotFoundException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.RepresentationConvertor;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.exception.RResourceException;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.c.task.TaskCreator;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.i.task.Task;
import net.idea.restnet.i.task.TaskResult;
import net.idea.restnet.rdf.FactoryTaskConvertorRDF;

import org.owasp.encoder.Encode;
import org.restlet.Context;
import org.restlet.data.CacheDirective;
import org.restlet.data.CharacterSet;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.data.ServerInfo;
import org.restlet.data.Status;
import org.restlet.representation.ObjectRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public abstract class QMRFQueryResource<Q extends IQueryRetrieval<T>,T extends Serializable> extends QueryResource<Q,T>{
	protected boolean headless = false;
	protected QMRF_HTMLBeauty htmlBeauty;
	protected transient Logger logger = Logger.getLogger(getClass().getName());
	protected Form params_get;
	public QMRFQueryResource() {
		super();
		
	}
	
	protected String getQueryService() {
		return ((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name());
	}
	protected String getAttachmentDir() {
		String dir = ((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_attachments_dir.name());
		return dir==null?System.getProperty("java.io.tmpdir"):dir;
	}
	public boolean isHeadless() {
		return headless;
	}


	public void setHeadless(boolean headless) {
		this.headless = headless;
	}

	@Override
	public String getConfigFile() {
		return ((QMRFFreeMarkerApplicaton)getApplication()).getConfigFile();
	}

	public Properties getDbConfig() {
		return ((QMRFFreeMarkerApplicaton)getApplication()).getDbConfig();
	}
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		headless = getHeadlessParam();
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty = new QMRF_HTMLBeauty(Resources.protocol);
		return htmlBeauty;
	}
	
	protected boolean getHeadlessParam() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		try {
			return Boolean.parseBoolean(form.getFirstValue("headless").toString());
		} catch (Exception x) {
			return false;
		}	
	}
	
	@Override
	protected FactoryTaskConvertor getFactoryTaskConvertor(ITaskStorage storage)
			throws ResourceException {
		return new FactoryTaskConvertorRDF(storage,getHTMLBeauty());
	}
	
	protected abstract QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException;
	
	@Override
	protected Task<Reference, Object> addTask(
			ICallableTask callable,
			T item,
			Reference reference) throws ResourceException {

			return ((TaskApplication)getApplication()).addTask(
				String.format("%s %s %s",
						callable.toString(),
						item==null?"":item.toString(),
						reference==null?"":" "),									
				callable,
				getRequest().getRootRef(),
				getToken());		
		
	}
	
	protected Map<String, Object> getMap(Variant variant) throws ResourceException {
		   Map<String, Object> map = new HashMap<String, Object>();

			map.put("managerRole", "false");
			map.put("editorRole", "false");
			if (getClientInfo()!=null) {
				if (getClientInfo().getUser()!=null)
					map.put("username", getClientInfo().getUser().getIdentifier());
				if (getClientInfo().getRoles()!=null) {
					if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.managerRole)>=0)
						map.put("managerRole", "true");
					if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.editorRole)>=0)
						map.put("editorRole", "true");
				}
			}

		        map.put("creator","IdeaConsult Ltd.");
		        map.put(Resources.Config.qmrf_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_email.name()));
		        map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
		        map.put(Resources.Config.qmrf_template.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_template.name()));
		        map.put(Resources.Config.qmrf_manual.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_manual.name()));
		        map.put(Resources.Config.qmrf_faq.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_faq.name()));
		        map.put(Resources.Config.qmrf_oecd.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_oecd.name()));
		        map.put(Resources.Config.qmrf_jrc.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_jrc.name()));
		        map.put(Resources.Config.qmrf_disclaimer.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_disclaimer.name()));
		        map.put("searchURI",htmlBeauty==null || htmlBeauty.getSearchURI()==null?"":htmlBeauty.getSearchURI());
		        map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name()));
		        //remove paging
		        Form query = getParams();
		        //query.removeAll("page");query.removeAll("pagesize");query.removeAll("max");
		        query.removeAll("media");
		        Reference r = cleanedResourceRef(getRequest().getResourceRef());
		        r.setQuery(query.getQueryString());
		        map.put("qmrf_root",getRequest().getRootRef().toString()) ;
		        map.put("qmrf_request",r.toString()) ;
		        if (query.size()>0)
		        	map.put("qmrf_query",query.getQueryString()) ;
		        //json
		        query.removeAll("media");query.add("media", MediaType.APPLICATION_JSON.toString());
		        r.setQuery(query.getQueryString());
		        map.put("qmrf_request_json",r.toString());
		        //csv
		        query.removeAll("media");query.add("media", MediaType.TEXT_CSV.toString());
		        r.setQuery(query.getQueryString());
		        map.put("qmrf_request_csv",r.toString());
		        return map;
	}
	
	protected Reference cleanedResourceRef(Reference ref) {
		return new Reference(Encode.forJavaScriptSource(ref.toString()));
	}	
	@Override
	protected Representation getHTMLByTemplate(Variant variant) throws ResourceException {
		getHTMLBeauty();
        return toRepresentation(getMap(variant), getTemplateName(), MediaType.TEXT_PLAIN);
	}
	
	
	protected void setCacheHeaders() {
		getResponse().getCacheDirectives().add(CacheDirective.privateInfo());
		getResponse().getCacheDirectives().add(CacheDirective.maxAge(2700));
	}
	protected void setXHeaders() {
		Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
		if (headers == null) {
			headers = new Form();
			getResponse().getAttributes().put("org.restlet.http.headers", headers);
		}
		headers.removeAll("X-Frame-Options");
		headers.add("X-Frame-Options", "SAMEORIGIN");
		ServerInfo si = getResponse().getServerInfo();si.setAgent("Restlet");getResponse().setServerInfo(si);
	}
	@Override
	protected Representation get(Variant variant) throws ResourceException {
		setXHeaders();
		setCacheHeaders();
		return super.get(variant);
	}

	@Override
	protected Form getParams() {
		if (params_get == null) 
			params_get = getResourceRef(getRequest()).getQueryAsForm();
			Iterator<Parameter> p = params_get.iterator();
			while (p.hasNext()) {
				Parameter param = p.next();
				String value = param.getValue();
				if (value==null) continue;
				if (value.contains("script") || value.contains(">") || value.contains("<")) param.setValue(""); 
				else param.setValue(value.replace("'","&quot;"));	
			}
		return params_get;
	}
	
	@Override
	protected Representation getRepresentation(Variant variant) throws ResourceException {
		try {
			CookieSetting cS = new CookieSetting(0, "subjectid", getToken());
			cS.setPath("/");
			cS.setAccessRestricted(true);
	        this.getResponse().getCookieSettings().add(cS);
	        
	        /*
			if (variant.getMediaType().equals(MediaType.APPLICATION_WADL)) {
				return new WadlRepresentation();
			} else
			*/	
        	if (MediaType.APPLICATION_JAVA_OBJECT.equals(variant.getMediaType())) {
        		if ((queryObject!=null) && (queryObject instanceof Serializable))
        		return new ObjectRepresentation((Serializable)returnQueryObject(),MediaType.APPLICATION_JAVA_OBJECT);
        		else throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE);        		
        	}				
	        if (queryObject != null) {
        	
	        	IProcessor<Q, Representation>  convertor = null;
	        	Reporter reporter = null;
	        	Connection connection = null;
	        	int retry=0;
	        	while (retry <maxRetry) {
	        		DBConnection dbc = null;
		        	try {
		        		dbc = new QDBConnection(getContext(),getDbConfig());
		        		configureRDFWriterOption(dbc.rdfWriter());
		        		configureDatasetMembersPrefixOption(dbc.dataset_prefixed_compound_uri());
		        		convertor = createConvertor(variant);
		        		if (convertor instanceof RepresentationConvertor)
		        			((RepresentationConvertor)convertor).setLicenseURI(getLicenseURI());
		        		
		        		connection = dbc.getConnection();
		        		reporter = ((RepresentationConvertor)convertor).getReporter();
			        	if (reporter instanceof IDBProcessor)
			        		((IDBProcessor)reporter).setConnection(connection);
			        	Representation r = convertor.process(queryObject);
			        	r.setCharacterSet(CharacterSet.UTF_8);
			        	return r;
			        	
		        	} catch (ResourceException x) {
		        		try { if ((reporter !=null) && (reporter !=null)) reporter.close(); } catch (Exception ignored) {}
		        		try { if (connection !=null) connection.close(); } catch (Exception ignored) {};
		    			throw x;			        	
		        	} catch (NotFoundException x) {
		        		Representation r = processNotFound(x,retry);
		        		try { if ((reporter !=null) && (reporter !=null)) reporter.close(); } catch (Exception ignored) {}
		        		try { if (connection !=null) connection.close(); } catch (Exception ignored) {};
		        		if (r!=null) return r;
		    			
		        	} catch (SQLException x) {
		        		Representation r = processSQLError(x,retry,variant);
		        		try { if ((reporter !=null) && (reporter !=null)) reporter.close(); } catch (Exception ignored) {}
		        		try { if (connection !=null) connection.close(); } catch (Exception ignored) {};
		        		if (r==null) continue; else return r;
		        	} catch (BatchProcessingException x) {
		        		Representation r = null;
		        		Exception batchException = null;
		        		if (x.getCause() instanceof NotFoundException) {
		        			r = processNotFound((NotFoundException)x.getCause(),retry);
		        			if (r==null) batchException = new ResourceException(Status.CLIENT_ERROR_NOT_FOUND,x.getCause().getMessage(),x.getCause());
		        		}
		        		try { if ((reporter !=null) && (reporter !=null)) reporter.close(); } catch (Exception ignored) {}
		        		try { if (connection !=null) connection.close(); } catch (Exception ignored) {};	
		        		if (r!=null) return r;
		        		batchException = batchException==null?new RResourceException(Status.SERVER_ERROR_INTERNAL,x,variant):batchException;
		        		Context.getCurrentLogger().severe(x.getMessage());
		    			throw batchException;
		        	} catch (Exception x) {
		        		try { if ((reporter !=null) && (reporter !=null)) reporter.close(); } catch (Exception ignored) {}
		        		try { if (connection !=null) connection.close(); } catch (Exception ignored) {};
		        		Context.getCurrentLogger().severe(x.getMessage());
		    			throw new RResourceException(Status.SERVER_ERROR_INTERNAL,x,variant);
	
		        	} finally {
		        		dbc = null;
		        		//if no exceptions, will be closed by reporters	
		        	}
	        	}
    			return null;	        	
	        	
	        } else {
	        	if (variant.getMediaType().equals(MediaType.TEXT_HTML)) try {
	    			IProcessor<Q, Representation>  convertor = createConvertor(variant);
	    			Representation r = convertor.process(null);
	            	return r;			
	    		} catch (Exception x) { 
	    			throw new RResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x,variant); 
	    		}  else {
	    			throw new RResourceException(Status.CLIENT_ERROR_BAD_REQUEST,error,variant);
	    		}
    	
	        }
		} catch (RResourceException x) {
			throw x;	        
		} catch (ResourceException x) {
			throw new RResourceException(x.getStatus(),x,variant);
		} catch (Exception x) {
			throw new RResourceException(Status.SERVER_ERROR_INTERNAL,x,variant);
		} finally {
			
		}
	}
	
	@Override
	protected Representation processAndGenerateTask(final Method method,
			Representation entity, Variant variant, boolean async)
			throws ResourceException {
			
			Connection conn = null;
			try {
	
				IQueryRetrieval<T> query = createUpdateQuery(method,getContext(),getRequest(),getResponse());
				
				TaskCreator taskCreator = getTaskCreator(entity, variant, method, async);
			
				List<UUID> r = null;
				if (query==null) { //no db querying, just return the task
					r =  taskCreator.process(null);
				} else {
					DBConnection dbc = new QDBConnection(getApplication().getContext(),getDbConfig());
					conn = dbc.getConnection();	
					
					
					try {
						taskCreator.setConnection(conn);
						r =  taskCreator.process(query);
					} catch (ResourceException x) {
						throw x;
					} catch (Exception x) {
						throw x;
					} finally {
						try {
				    		taskCreator.setConnection(null);
				    		taskCreator.close();
						} catch (Exception x) {}
			    		try { conn.close(); conn=null;} catch  (Exception x) {}
					}
				}
				
				if ((r==null) || (r.size()==0)) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
				else {
					ITaskStorage storage = ((TaskApplication)getApplication()).getTaskStorage();
					FactoryTaskConvertor<Object> tc = getFactoryTaskConvertor(storage);
					if (r.size()==1) {
						Task<TaskResult,Object> task = storage.findTask(r.get(0));
						task.update();
						if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
							getResponse().redirectSeeOther(task.getUri().getUri());
							return null;
						} else {
							setStatus(task.isDone()?Status.SUCCESS_OK:Status.SUCCESS_ACCEPTED);
							return tc.createTaskRepresentation(r.get(0), variant,getRequest(), getResponse(),getDocumentation());
						}
					} else 
						return tc.createTaskRepresentation(r.iterator(), variant,getRequest(), getResponse(),getDocumentation());
					
				}
			} catch (RResourceException x) {				
				throw x;
			} catch (ResourceException x) {				
				throw new RResourceException(x.getStatus(),x, variant);
			} catch (AmbitException x) {
				throw new RResourceException(new Status(Status.SERVER_ERROR_INTERNAL,x),variant);
			} catch (SQLException x) {
				throw new RResourceException(new Status(Status.SERVER_ERROR_INTERNAL,x),variant);		
			} catch (Exception x) {
				throw new RResourceException(new Status(Status.SERVER_ERROR_INTERNAL,x),variant);				
			} finally {
				try { if (conn != null) conn.close(); } catch  (Exception x) {}
			}
		
	}
	
	@Override
	protected Representation toRepresentation(Map<String, Object> map,
			String templateName, MediaType mediaType) {
		
		return super.toRepresentation(map, templateName, mediaType);
	}
}
