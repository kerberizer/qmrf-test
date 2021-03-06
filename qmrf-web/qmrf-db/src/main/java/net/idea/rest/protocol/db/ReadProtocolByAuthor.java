package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

public class ReadProtocolByAuthor extends ReadProtocolByEndpointString {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308150917300833831L;
	/**
	 * 
	 */

	protected static String sql = String.format(ReadProtocol.sql_withabstract,
		"where ","published_status='published' and extractvalue(abstract,'/QMRF/Catalogs/authors_catalog/author/@name') regexp ?");

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) 
			params.add(new QueryParam<String>(String.class, getFieldname()));
		else throw new AmbitException("No author name!");
		if (getValue()!=null && getValue().getTimeModified()!=null)
			params.add(new QueryParam<Long>(Long.class, getValue().getTimeModified()));		
		return params;
	}

	@Override
	public String getLocalSQL() throws AmbitException {
		return sql;
	}
	
}