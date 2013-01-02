package net.idea.rest.user.author.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractUpdate;
import net.idea.rest.db.exceptions.InvalidProtocolException;
import net.idea.rest.db.exceptions.InvalidUserException;
import net.idea.rest.protocol.DBProtocol;
import net.idea.restnet.user.DBUser;


/**
 * Adds a protocol author
 * @author nina
 *
 */
public class AddAuthor  extends AbstractUpdate<DBUser,DBProtocol> {
	public static final String[] sql_addAuthor = new String[] { 
		"insert ignore into protocol_authors (idprotocol,version,iduser) select idprotocol,version,? from protocol where qmrf_number=?"
	};
	
	public AddAuthor(DBUser author,DBProtocol protocol) {
		super(protocol);
		setGroup(author);
	}
	public AddAuthor() {
		this(null,null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getGroup()==null || getGroup().getID()<=0) throw new InvalidUserException();
		if (getObject()==null || getObject().getIdentifier()==null) throw new InvalidProtocolException();
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<Integer>(Integer.class, getGroup().getID()));
		params.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		return sql_addAuthor;
	}
	public void setID(int index, int id) {
			
	}
}