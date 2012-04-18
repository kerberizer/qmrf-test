package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;
import net.idea.rest.protocol.DBProtocol;

public class PublishProtocol extends AbstractObjectUpdate<DBProtocol>{
	
	public static final String[] publish_sql = new String[] { 
		"update protocol set published=true, qmrf_number=\n"+
		"(select concat(\"Q\",substr(year(now()),3,2),\"-\",p.endpoint,\"-\",lpad(n.nperyear,4,'0')) from\n"+
		"(select count(idprotocol)+1 as nperyear from protocol where published=true and  year(updated)=year(now())) as n\n"+
		"join (select\n"+
		"concat(upper(substr(md5(trim(extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@group'))),1,2)),\n"+
		"upper(substr(md5(trim(extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name'))),1,2))) as endpoint\n"+
		"from protocol where idprotocol=? and version=?) as p\n"+
		") where idprotocol=? and version=? and published=false\n" 
	};

	public PublishProtocol(DBProtocol ref) {
		super(ref);
	}
	public PublishProtocol() {
		this(null);
	}			
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		if (getObject()==null) throw new AmbitException("Empty protocol");
		if (getObject().getID()<=0) throw new AmbitException("Invalid document ID");
		if (getObject().getVersion()<=0) throw new AmbitException("Invalid document version");
	
		params1.add(ReadProtocol.fields.idprotocol.getParam(getObject()));
		params1.add(ReadProtocol.fields.version.getParam(getObject()));
		params1.add(ReadProtocol.fields.idprotocol.getParam(getObject()));
		params1.add(ReadProtocol.fields.version.getParam(getObject()));
		
		return params1;
		
	}
	public String[] getSQL() throws AmbitException {
		return publish_sql;
	}
	public void setID(int index, int id) {
			
	}
}