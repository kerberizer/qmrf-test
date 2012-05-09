package net.idea.rest.endpoints.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;
import net.idea.rest.db.exceptions.InvalidEndpointException;
import net.idea.rest.db.exceptions.InvalidProtocolException;
import net.idea.rest.endpoints.EndpointTest;
import net.idea.rest.protocol.DBProtocol;

public class AddProtocolEndpoint  extends AbstractUpdate<EndpointTest,DBProtocol> {
	public static final String[] sql_addEndpoints = new String[] { 
			"insert ignore into protocol_endpoints select idprotocol,version,idtemplate from protocol join template where code=? and name=? and qmrf_number = ? "
	};
	
	public AddProtocolEndpoint(EndpointTest endpoint,DBProtocol protocol) {
		super(protocol);
		setGroup(endpoint);
	}
	public AddProtocolEndpoint() {
		this(null,null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup()==null || getGroup().getCode()==null || getGroup().getName()==null) throw new InvalidEndpointException();
		if (getObject()==null || getObject().getIdentifier()==null) throw new InvalidProtocolException();
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class, getGroup().getCode()));
		params.add(new QueryParam<String>(String.class, getGroup().getName()));
		params.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
		return params;
	}

	public String[] getSQL() throws AmbitException {
		return sql_addEndpoints;
	}
	public void setID(int index, int id) {
			
	}
}