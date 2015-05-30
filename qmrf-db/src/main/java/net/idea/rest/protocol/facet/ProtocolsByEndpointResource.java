package net.idea.rest.protocol.facet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import net.idea.modbcum.i.IDBProcessor;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.exceptions.BatchProcessingException;
import net.idea.modbcum.i.exceptions.NotFoundException;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFFreeMarkerApplicaton;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.RepresentationConvertor;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.exception.RResourceException;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.FactoryTaskConvertor;
import net.idea.restnet.c.task.TaskCreator;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.db.facet.FacetResource;
import net.idea.restnet.i.task.ITaskStorage;
import net.idea.restnet.i.task.Task;
import net.idea.restnet.i.task.TaskResult;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CacheDirective;
import org.restlet.data.CharacterSet;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.ServerInfo;
import org.restlet.data.Status;
import org.restlet.representation.ObjectRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class ProtocolsByEndpointResource extends FacetResource<IQueryRetrieval<IFacet<String>>> {
	public static final String resource = "/endpoint";
	

	
	@Override
	protected IQueryRetrieval<IFacet<String>> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		//get endpoints hierarchy
		//String endpoint = getParams().getFirstValue(MetadatasetResource.search_features.feature_sameas.toString());
		EndpointProtocolFacetQuery q = new EndpointProtocolFacetQuery(getResourceRef(getRequest()).toString());
		/*		
		StringCondition c = StringCondition.getInstance(StringCondition.C_REGEXP);
		String param = getParams().getFirstValue(QueryResource.condition.toString());
		try {
			if (param != null)	{
				if ("startswith".equals(param.toLowerCase()))
					q.setCondition(StringCondition.getInstance(StringCondition.C_STARTS_WITH));
				else
					c = StringCondition.getInstance(param);
			}
		} catch (Exception x) {	
			
		} finally {
			q.setCondition(c);
		}
		*/
		return q;
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.protocol);
	}

	@Override
	public String getConfigFile() {
		return ((QMRFFreeMarkerApplicaton)getApplication()).getConfigFile();
	}
	public Properties getDbConfig() {
		return ((QMRFFreeMarkerApplicaton)getApplication()).getDbConfig();
	}
	@Override
	protected boolean isAllowedMediaType(MediaType mediaType)
			throws ResourceException {
		return false;
	}
	
	@Override
	protected QueryHTMLReporter getHTMLReporter(Request request) {
		QMRF_HTMLBeauty hb = (QMRF_HTMLBeauty)getHTMLBeauty();
		hb.setSearchURI(Resources.endpoint);
		return new QMRFHTMLFacetReporter(request,true,null,hb);
	}
	
	@Override
	protected Representation getRepresentation(Variant variant) throws ResourceException {
		try {
			Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
			if (headers == null) {
				headers = new Form();
				getResponse().getAttributes().put("org.restlet.http.headers", headers);
			}
			headers.removeAll("X-Frame-Options");
			headers.add("X-Frame-Options", "SAMEORIGIN");
			getResponse().getCacheDirectives().add(CacheDirective.privateInfo());
			getResponse().getCacheDirectives().add(CacheDirective.maxAge(2700));
			ServerInfo si = getResponse().getServerInfo();si.setAgent("Restlet");getResponse().setServerInfo(si);
			
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
        	
	        	IProcessor<IQueryRetrieval<IFacet<String>>, Representation>  convertor = null;
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
	    			IProcessor<IQueryRetrieval<IFacet<String>>, Representation>  convertor = createConvertor(variant);
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
	
				IQueryRetrieval<IFacet<String>> query = createUpdateQuery(method,getContext(),getRequest(),getResponse());
				
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
}