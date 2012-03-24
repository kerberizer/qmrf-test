package net.idea.rest.protocol.facet;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.facet.IFacet;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.db.facet.FacetResource;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
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
		return "conf/qmrf-db.pref";
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
}