package net.idea.rest.protocol.attachments.db;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.EQCondition;
import net.idea.modbcum.q.query.AbstractQuery;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.db.ReadProtocol.fields;
import net.idea.restnet.c.ChemicalMediaType;

import org.restlet.data.MediaType;


public class ReadAttachment extends AbstractQuery<DBProtocol, DBAttachment, EQCondition, DBAttachment>  implements IQueryRetrieval<DBAttachment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228939989116141217L;
	protected String dir = null;
	protected static String sql = 
		"SELECT idprotocol,version,idattachment,type,a.name,`format`,description FROM attachments a where %s";
	protected static String where_protocol = "idprotocol=? and version=?";
	protected static String where_attachment = "idattachment=?";
	
	/**
	 * get datasets by structure
SELECT * FROM attachments a, `ambit2-qmrf`.src_dataset d
join `ambit2-qmrf`.struc_dataset using(id_srcdataset)
join `ambit2-qmrf`.structure using(idstructure)
where a.name=d.name
and idchemical=282
	 */
	/**
	 * 
	 * @param protocol
	 */
	public ReadAttachment(DBProtocol protocol,String dir) {
		super();
		setFieldname(protocol);
		this.dir = dir;
	}
	public ReadAttachment(Integer id, Integer version,Integer year, String dir) {
		super();
		setFieldname(id==null?null:new DBProtocol(id,version,year));
		this.dir = dir;
	}
	public ReadAttachment(String dir) {
		this((DBProtocol)null,dir);
	}
		
	public double calculateMetric(DBAttachment object) {
		return 1;
	}

	public boolean isPrescreen() {
		return false;
	}

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) {
			params.add(fields.idprotocol.getParam(getFieldname()));
			params.add(fields.version.getParam(getFieldname()));
		} 
		if ((getValue()!=null)&&(getValue().getID()>0))
			params.add(new QueryParam<Integer>(Integer.class, getValue().getID()));
		if (params.size()==0) throw new AmbitException("No protocol or attachment id");
		return params;
	}

	public String getSQL() throws AmbitException {
		StringBuilder b = new StringBuilder();
		int c = 0;
		if (getFieldname()!=null) { b.append(where_protocol); c++;}
		if ((getValue()!=null)&&(getValue().getID()>0)) {
			if (c>0) b.append(" and ");
			b.append(where_attachment); 
		}
		return String.format(sql,b);			
	}

	public DBAttachment getObject(ResultSet rs) throws AmbitException {
		String url = "";
		try {
				String format = rs.getString("format");
				String name = rs.getString("name");
				String type = rs.getString("type");
				url = String.format("file://%s/%s/%s.%s",dir,type,name.replace(" ","%20"),format);
				DBAttachment attachment = new DBAttachment(new URL(url));
				if ("pdf".equals(format)) attachment.setMediaType(MediaType.APPLICATION_PDF.toString());
				else if ("sdf".equals(format)) attachment.setMediaType(ChemicalMediaType.CHEMICAL_MDLSDF.toString());
				else if ("mol".equals(format)) attachment.setMediaType(ChemicalMediaType.CHEMICAL_MDLMOL.toString());
				else if ("csv".equals(format)) attachment.setMediaType(MediaType.TEXT_CSV.toString());
				else if ("txt".equals(format)) attachment.setMediaType(MediaType.TEXT_PLAIN.toString());
				else if ("smi".equals(format)) attachment.setMediaType(ChemicalMediaType.CHEMICAL_SMILES.toString());
				else if ("doc".equals(format)) attachment.setMediaType(MediaType.APPLICATION_WORD.toString());
				else if ("docx".equals(format)) attachment.setMediaType(MediaType.APPLICATION_MSOFFICE_DOCX.toString());
				else attachment.setMediaType(MediaType.APPLICATION_ALL.toString());
				attachment.setID(rs.getInt("idattachment"));
				attachment.setTitle(name);
				attachment.setType(DBAttachment.attachment_type.valueOf(type));
				attachment.setDescription(rs.getString("description"));
				return attachment;

		} catch (Exception x) {
			throw new AmbitException(String.format("Error reading %s", url),x);
		}
	}
	@Override
	public String toString() {
		return getFieldname()==null?"All attachments":String.format("Attachments for QMRF-%s",getFieldname().getID());
	}
}