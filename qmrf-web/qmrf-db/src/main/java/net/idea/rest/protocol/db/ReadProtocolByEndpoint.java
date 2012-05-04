package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.rest.endpoints.EndpointTest;

public class ReadProtocolByEndpoint extends ReadProtocolAbstract<EndpointTest> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308150917300833831L;
	/**
	 * 
	 */

	protected static String sql_join = 	
		"join protocol_endpoints using(idprotocol)\n join template using(idtemplate) where ";
	protected static String sql_join_null = 	
		"left join protocol_endpoints using(idprotocol) where ";
	protected static String sql = String.format(ReadProtocol.sql_nokeywords,sql_join,
			"published=true and code = ?");
	protected static String sql_null = String.format(ReadProtocol.sql_nokeywords,sql_join_null,
			"published=true and idtemplate is null");	

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null && getFieldname().getCode()!=null)
			params.add(new QueryParam<String>(String.class, getFieldname().getCode()));

		return params;
	}

	public String getSQL() throws AmbitException {
		if (getFieldname()!=null && getFieldname().getCode()!=null)
			return sql;
		else 
			return sql_null;

	}
	
	
}