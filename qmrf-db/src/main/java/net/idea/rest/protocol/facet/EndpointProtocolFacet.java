package net.idea.rest.protocol.facet;

import java.net.URLEncoder;

import net.idea.modbcum.q.facet.PropertyDatasetFacet;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.resource.db.ProtocolDBResource.SearchMode;


public class EndpointProtocolFacet extends PropertyDatasetFacet<String,String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3196764532235094256L;
	
	public EndpointProtocolFacet(String url) {
		super(url);
	}
	/**
	 * params[0] root URL 
	 * params[1] full compound URL
	 */
	@Override
	public String getResultsURL(String... params) {
		return 
			   String.format("%s%s?option=%s&%s=%s",
				(params.length>=1)?params[0]:"",
				Resources.protocol,
				SearchMode.endpointcode,
				"search",
				getProperty2()==null?"undefined":URLEncoder.encode(getProperty2())
				);
	}

}