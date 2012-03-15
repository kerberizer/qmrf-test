package net.idea.rest.protocol.db;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol.fields;

public class ReadProtocolByEndpoint extends ReadProtocolAbstract<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 308150917300833831L;
	/**
	 * 
	 */

	protected static String sql = String.format(ReadProtocol.sql_nokeywords,
		"where ","published=true and trim(extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name')) %s ?");

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
	
	public DBProtocol getObject(ResultSet rs) throws AmbitException {
		DBProtocol p = null;
		try {
			p =  new DBProtocol();
			for (fields field:ReadProtocolByStructure.sqlFields) try {
				field.setParam(p,rs);
				
			} catch (Exception x) {
				x.printStackTrace();
			}
			try {
				Timestamp ts = rs.getTimestamp(fields.updated.name());
				p.setTimeModified(ts.getTime());
			} catch (Exception x) {}
			try {
				Timestamp ts = rs.getTimestamp(fields.created.name());
				p.setSubmissionDate(ts.getTime());
			} catch (Exception x) {
				x.printStackTrace();
				
			}
			return p;
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		} finally {
			if (p!=null) p.setIdentifier(ReadProtocol.generateIdentifier(p));
		}
	}	
}