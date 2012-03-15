package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;
import net.idea.rest.protocol.DBProtocol;


public class UpdateKeywords extends AbstractObjectUpdate<DBProtocol>{

	public static final String[] update_sql = {
		"update protocol set abstract=UpdateXML(abstract, '//keywords', concat('<keywords>',?,'</keywords>')) where idprotocol=? and version=?"		
		};

	public UpdateKeywords(DBProtocol ref) {
		super(ref);
	}
	public UpdateKeywords() {
		this(null);
	}			
	public List<QueryParam> getParameters(int index) throws AmbitException {
		
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class, 
						ReadProtocol.fields.xmlkeywords.getValue(getObject()).toString()));
		params.add(new QueryParam<Integer>(Integer.class, getObject().getID()));
		params.add(new QueryParam<Integer>(Integer.class, getObject().getVersion()));		
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		return update_sql;
	}
	public void setID(int index, int id) {
			
	}
}