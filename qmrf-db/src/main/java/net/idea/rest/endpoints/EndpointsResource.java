package  net.idea.rest.endpoints;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFQueryResource;
import net.idea.rest.endpoints.db.DictionaryObjectQuery;
import net.idea.rest.endpoints.db.DictionaryQuery;
import net.idea.rest.endpoints.db.QueryOntology;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.DisplayMode;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.db.convertors.OutputWriterConvertor;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import ambit2.base.data.Dictionary;

/**
 * 
 * A resource wrapper fot {@link QueryOntology}
 * @author nina
 *
 */
public class EndpointsResource extends QMRFQueryResource<IQueryRetrieval<Dictionary>, Dictionary> {
	
	public static String resource = "/endpoints";
	public static String resourceParent = "subject";
	public static String resourceKey = "object";
	public static String resourceID = String.format("/{%s}/{%s}",resourceParent,resourceKey);
	public static String resourceTree = String.format("/{%s}/{%s}/view/{tree}",resourceParent,resourceKey);
	protected boolean isRecursive = false;
	
	public EndpointsResource() {
		super();
	}
	public boolean isRecursive() {
		return isRecursive;
	}
	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		customizeVariants(new MediaType[] {
				MediaType.TEXT_HTML,
				MediaType.TEXT_URI_LIST,
				MediaType.APPLICATION_RDF_XML,
				MediaType.APPLICATION_RDF_TURTLE,
				MediaType.TEXT_RDF_N3,
				MediaType.TEXT_RDF_NTRIPLES,				
				MediaType.APPLICATION_JAVA_OBJECT
				});

				
	}

	@Override
	public IProcessor<IQueryRetrieval<Dictionary>, Representation> createConvertor(
			Variant variant)
			throws net.idea.modbcum.i.exceptions.AmbitException,
			ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		/*
		if (variant.getMediaType().equals(MediaType.APPLICATION_RDF_XML) ||
					variant.getMediaType().equals(MediaType.APPLICATION_RDF_TURTLE) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_N3) ||
					variant.getMediaType().equals(MediaType.TEXT_RDF_NTRIPLES) ||
					variant.getMediaType().equals(MediaType.APPLICATION_JSON)
					) {
				return new RDFJenaConvertor<Property, IQueryRetrieval<Property>>(
						new TemplateRDFReporter<IQueryRetrieval<Property>>(
								getRequest(),getDocumentation(),variant.getMediaType(),isRecursive())
						,variant.getMediaType(),filenamePrefix);		
				
				
		} else 
		*/
		if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
				DictionaryURIReporter r = new DictionaryURIReporter(getRequest(),null);
				r.setDelimiter("\n");
				return new StringConvertor(	r,MediaType.TEXT_URI_LIST,filenamePrefix);
				
		} else 
			return new OutputWriterConvertor(
					createHTMLReporter(headless)
					,MediaType.TEXT_HTML);
	}

	@Override
	protected QueryHTMLReporter createHTMLReporter(boolean headless)
			throws ResourceException {
		return new EndpointsHTMLReporter(getRequest(),!isRecursive()?DisplayMode.table:DisplayMode.singleitem,getHTMLBeauty());
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty==null) htmlBeauty = new QMRF_HTMLBeauty(Resources.endpoint);
		return htmlBeauty;
	}
	@Override
	protected IQueryRetrieval<Dictionary> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		try {
			Form form = getResourceRef(getRequest()).getQueryAsForm();
			form.getFirstValue(QueryResource.search_param);
		} catch (Exception x) {
			
		}			
		try {
			Object view = request.getAttributes().get("tree");
			setRecursive("tree".equals(view));	
		} catch (Exception x) {
			setRecursive(false);
		}		
		Object key = request.getAttributes().get(resourceKey);
		QueryOntology q = new QueryOntology();
		q.setIncludeParent(false);
		if (key != null) 	
			q.setValue(key==null?null:new Dictionary(Reference.decode(key.toString()),null));
		else {
			key =  request.getAttributes().get(resourceParent);
			DictionaryQuery qd = new DictionaryObjectQuery();
			qd.setCondition(StringCondition.getInstance(StringCondition.C_EQ));
			qd.setValue(key==null?null:Reference.decode(key.toString(),null));
			
			return qd;
		}
		return q;
	}
	
	
	/*

	protected Property create ObjectFromHeaders(Form requestHeaders,
			Representation entity) throws ResourceException {
		Object key = getRequest().getAttributes().get(resourceKey);
		if (key == null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		Object parent = getRequest().getAttributes().get(resourceParent);
		if (parent == null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
		Dictionary dictionary = new Dictionary(parent.toString(),key.toString());
		return dictionary;
		
	}
	
	protected AbstractUpdate createDeleteObject(Property entry) throws ResourceException {
		if (entry instanceof Dictionary) {
			DeleteDictionary delete = new DeleteDictionary((Dictionary)entry);
			return delete;
		} else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
				String.format("Expected %s instead of %s",Dictionary.class.getName(),entry.getClass().getName()));
	};
	*/
	@Override
	protected QueryURIReporter<Dictionary, IQueryRetrieval<Dictionary>> getURUReporter(
			Request baseReference) throws ResourceException {
		return new DictionaryURIReporter(getRequest(),getDocumentation());
	}
}
