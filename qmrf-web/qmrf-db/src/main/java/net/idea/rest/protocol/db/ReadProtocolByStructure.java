package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
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
		fields.status,
		fields.published_status
		
		//ReadProtocol.fields.accesslevel
	};	
	protected static String sql = 
		"select protocol.idprotocol,protocol.version,protocol.title,protocol.qmrf_number,abstract as anabstract,iduser,\n"+
		"summarySearchable,idproject,idorganisation,filename,template,protocol.updated,status,\n"+
		"protocol.`created`,published_status,extractvalue(abstract,'//keywords') as xmlkeywords,\n"+
		"extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@group') as endpointgroup,\n"+
		"extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name') as endpointname\n"+		
		"from protocol,attachments a, `ambit2-qmrf`.src_dataset q\n"+
		"join `ambit2-qmrf`.struc_dataset using(id_srcdataset)\n"+
		"join `ambit2-qmrf`.structure using(idstructure)\n"+
		"where idchemical=?\n"+
		"and published_status='published' and a.name=q.name and a.idprotocol=protocol.idprotocol and a.version=protocol.version\n";

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if ((getFieldname()!=null) && (getFieldname().getIdchemical()>0)) 
			params.add(new QueryParam<Integer>(Integer.class, getFieldname().getIdchemical()));
		else throw new AmbitException("No structure!");
		return params;
	}

	public String getSQL() throws AmbitException {
		return sql;

	}

}
