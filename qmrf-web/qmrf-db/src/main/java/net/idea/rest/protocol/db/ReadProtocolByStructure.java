package net.idea.rest.protocol.db;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol.fields;
import net.idea.rest.structure.resource.Structure;

public class ReadProtocolByStructure extends ReadProtocolAbstract<Structure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2670988238522573149L;
	public static final ReadProtocol.fields[] sqlFields = new ReadProtocol.fields[] {
		fields.idprotocol,
		fields.version,
		fields.title,
		fields.anabstract,
		fields.iduser,
		fields.summarySearchable,
		fields.idproject,
		fields.idorganisation,
		fields.filename,
		fields.template,
		fields.status,
		fields.published
		
		//ReadProtocol.fields.accesslevel
	};	
	protected static String sql = 
		"select protocol.idprotocol,protocol.version,protocol.title,abstract as anabstract,iduser,\n"+
		"summarySearchable,idproject,idorganisation,filename,template,protocol.updated,status,\n"+
		"protocol.`created`,published\n"+
		"from protocol,attachments a, `ambit2-qmrf`.src_dataset q\n"+
		"join `ambit2-qmrf`.struc_dataset using(id_srcdataset)\n"+
		"join `ambit2-qmrf`.structure using(idstructure)\n"+
		"where idchemical=?\n"+
		"and a.name=q.name and a.idprotocol=protocol.idprotocol and a.version=protocol.version\n";

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if ((getFieldname()!=null) && (getFieldname().getIdchemical()>0)) 
			params.add(new QueryParam<Integer>(Integer.class, getFieldname().getIdchemical()));
		else throw new AmbitException("No structure!");
		return params;
	}

	public String getSQL() throws AmbitException {
		System.out.println(sql);
		return sql;

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
			if (p!=null) p.setIdentifier(String.format("QMRF-%d-%d", p.getID(),p.getVersion()));
		}
	}	
}
