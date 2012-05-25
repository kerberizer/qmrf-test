package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

public class ReadProtocolByEndpointString extends ReadProtocolAbstract<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308150917300833831L;
	/**
	 * 
	 */

	protected static String sql = String.format(ReadProtocol.sql_nokeywords,
		"where ","published_status='published' and trim(extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name')) %s ?");

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) 
			params.add(new QueryParam<String>(String.class, getFieldname()));
		else throw new AmbitException("No endpoint!");
		return params;
	}

	public String getSQL() throws AmbitException {
		return String.format(sql,getCondition().getSQL());

	}
	
	
}