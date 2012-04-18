package net.idea.rest.user.author.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.rest.db.exceptions.InvalidProtocolException;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.db.ReadUser;


public class ReadAuthor extends ReadUser<DBProtocol> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2677470406987442304L;
	private static final String join = "\njoin protocol_authors using(iduser)\njoin protocol using(idprotocol,version) where ";
	public ReadAuthor(DBProtocol protocol,DBUser user) {
		super(user);
		setFieldname(protocol);
	}
	

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params = null;
		if (getFieldname()==null || getFieldname().getIdentifier()==null) throw new InvalidProtocolException();
		params = new ArrayList<QueryParam>();
		params.add(ReadProtocol.fields.identifier.getParam(getFieldname()));
		if ((getValue()!=null) && getValue().getID()>0)
			params.add(fields.iduser.getParam(getValue()));
		
		return params;
	}

	public String getSQL() throws AmbitException {
		if ((getValue()!=null) && (getValue().getID()>0))
			return String.format(sql,join,
				   String.format("%s and %s",
						   ReadProtocol.fields.identifier.getCondition(),
						   fields.iduser.getCondition()));
		else 
			return String.format(sql,join,
					String.format("%s ",
							ReadProtocol.fields.identifier.getCondition()
							));
			
	}	
}
