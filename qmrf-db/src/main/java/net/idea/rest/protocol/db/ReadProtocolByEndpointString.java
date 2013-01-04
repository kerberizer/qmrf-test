package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.rest.protocol.db.ReadProtocolByTextSearch.TextSearchMode;

public class ReadProtocolByEndpointString extends ReadProtocolAbstract<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308150917300833831L;
	/**
	 * 
	 */

	protected static String sql = String.format(ReadProtocol.sql_withabstract,
		"where ","published_status='published' and trim(extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name')) %s ?");

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) 
			params.add(new QueryParam<String>(String.class, getFieldname()));
		else throw new AmbitException("No endpoint!");
		if (getValue()!=null && getValue().getTimeModified()!=null)
			params.add(new QueryParam<Long>(Long.class, getValue().getTimeModified()));		
		return params;
	}

	public String getLocalSQL() throws AmbitException {
		return String.format(sql,getCondition().getSQL());
	}
	public String getSQL() throws AmbitException {
		String sql = getLocalSQL();
		if (getValue()!=null && getValue().getTimeModified()!=null) { 
			StringBuilder b = new StringBuilder();
			b.append(sql);
			b.append(" and ");
			b.append(ReadProtocol.fields.updated.getCondition());
			return b.toString();
		} else return sql;


	}
	
	
}