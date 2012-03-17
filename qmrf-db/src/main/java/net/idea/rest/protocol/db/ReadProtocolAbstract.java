package net.idea.rest.protocol.db;

import java.sql.ResultSet;
import java.sql.Timestamp;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.modbcum.q.query.AbstractQuery;
import net.idea.rest.protocol.DBProtocol;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public abstract class ReadProtocolAbstract<T> extends AbstractQuery<T, DBProtocol, StringCondition, DBProtocol>  implements IQueryRetrieval<DBProtocol> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228939989116141217L;
	protected Boolean showUnpublished = true;
	protected Boolean onlyUnpublished = false;
	public Boolean getOnlyUnpublished() {
		return onlyUnpublished;
	}
	public void setOnlyUnpublished(Boolean onlyUnpublished) {
		this.onlyUnpublished = onlyUnpublished;
	}
	public Boolean getShowUnpublished() {
		return showUnpublished;
	}
	public void setShowUnpublished(Boolean showUnpublished) {
		this.showUnpublished = showUnpublished;
	}

	
	protected static String sql_withkeywords =  //for text search
		"select idprotocol,version,protocol.title,abstract as anabstract,iduser,summarySearchable," +
		"idproject," +
		"idorganisation,user.username,user.firstname,user.lastname," +
		"filename,extractvalue(abstract,'//keywords') as xmlkeywords,updated,status,`created`,published\n" +
		"from protocol join user using(iduser)\n" +
		"left join keywords using(idprotocol,version) %s %s";

	protected static String sql_nokeywords = 
		"select idprotocol,version,protocol.title,abstract as anabstract,iduser,summarySearchable," +
		"idproject," +
		"idorganisation,user.username,user.firstname,user.lastname," +
		"filename,extractvalue(abstract,'//keywords') as xmlkeywords,updated,status,`created`,published\n" +
		"from protocol join user using(iduser)\n" +
		" %s %s order by idprotocol desc";	
	
	public ReadProtocolAbstract(Integer id) {
		this(id,null);
	}
	public ReadProtocolAbstract(Integer id, Integer version) {
		super();
		setValue(id==null?null:new DBProtocol(id,version,2009));
		setFieldname(null);
	}
	public ReadProtocolAbstract() {
		this(null,null);
	}
		
	public double calculateMetric(DBProtocol object) {
		return 1;
	}

	public boolean isPrescreen() {
		return false;
	}


	public DBProtocol getObject(ResultSet rs) throws AmbitException {
		DBProtocol p = null;
		try {
			p =  new DBProtocol();
			for (ReadProtocol.fields field:ReadProtocol.sqlFields) try {
				field.setParam(p,rs);
				
			} catch (Exception x) {
				x.printStackTrace();
			}
			try {
				Timestamp ts = rs.getTimestamp(ReadProtocol.fields.updated.name());
				p.setTimeModified(ts.getTime());
			} catch (Exception x) {}
			try {
				Timestamp ts = rs.getTimestamp(ReadProtocol.fields.created.name());
				p.setSubmissionDate(ts.getTime());
			} catch (Exception x) {
				x.printStackTrace();
				
			}
			return p;
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		} finally {
			if (p!=null) p.setIdentifier(generateIdentifier(p));
		}
	}
	@Override
	public String toString() {
		return getValue()==null?"All protocols":String.format("Protocol id=P%s",getValue().getID());
	}
	
	public static String generateIdentifier(DBProtocol protocol) throws ResourceException {
		return String.format("QMRF-%d-%d-%d", protocol.getYear(),protocol.getID(),protocol.getVersion());
	}
	public static int[] parseIdentifier(String identifier) throws ResourceException {
		String ids[] = identifier.split("-");
		if ((ids.length!=4) || !identifier.startsWith(DBProtocol.prefix)) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Invalid format");
		
		int[] id = new int[3];
		id[2] = Integer.parseInt(ids[1]); //year ; added last for compatibility
		for (int i=0; i < 2; i++)
			try {
				id[i] = Integer.parseInt(ids[i+2]);
			} catch (NumberFormatException x) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
			}
		return id;
	}
}
