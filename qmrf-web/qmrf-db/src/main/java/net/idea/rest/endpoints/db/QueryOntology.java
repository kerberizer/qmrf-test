package net.idea.rest.endpoints.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.conditions.StringCondition;
import net.idea.modbcum.q.query.AbstractQuery;
import net.idea.rest.endpoints.EndpointTest;
import ambit2.base.data.Dictionary;

public class QueryOntology<D extends Dictionary>  extends AbstractQuery<Boolean, D, StringCondition, D> implements
												IQueryRetrieval<D> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6711409305156505699L;
	public enum PROPERTY_TABLE {
		idproperty,
		name,
		units,
		comments,
		islocal,
		idreference
	}	
	private static String sqlParent = 
	
		"select 0,t2.name,t1.name,t2.code as category,t1.code as code\n"+
		"from ((`template` `t1`\n"+
		"join `dictionary` `d` on((`t1`.`idtemplate` = `d`.`idsubject`)))\n"+
		"join `template` `t2` on((`d`.`idobject` = `t2`.`idtemplate`)))\n"+
		"where lower(t1.name) %s lower(?)\n"+
		"union\n";		
	private static String sqlChild = 	
		"select 1,t2.name,t1.name,t2.code as category,t1.code as code\n"+
		"from ((`template` `t1`\n"+
		"join `dictionary` `d` on((`t1`.`idtemplate` = `d`.`idsubject`)))\n"+
		"join `template` `t2` on((`d`.`idobject` = `t2`.`idtemplate`)))\n"+
		"where lower(t2.name) %s lower(?)\n"
		;
	
	private static String sqlAll = 	
		"select 2,t2.name,t1.name,t2.code as category,t1.code as code\n"+
		"from ((`template` `t1`\n"+
		"join `dictionary` `d` on((`t1`.`idtemplate` = `d`.`idsubject`)))\n"+
		"join `template` `t2` on((`d`.`idobject` = `t2`.`idtemplate`)))\n"+
		"where\n"+
		"(\n"+
		"t1.code regexp ?\n"+
		"or t2.code regexp ?\n"+
		"or lower(t1.name) regexp lower(?)\n"+
		"or lower(t2.name) regexp lower(?)\n"+
		")\n"+
		"and relationship != \"same_as\"\n"+
		"order by t1.idtemplate\n";
	
	private static String sqlProtocol = 	
		"SELECT idprotocol,t2.name,t1.name,t2.code as category,t1.code as code,\n"+
		"extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@group') as endpointgroup,\n"+
		"extractvalue(abstract,'/QMRF/Catalogs/endpoints_catalog/endpoint/@name') as endpointname\n"+
		"FROM protocol\n"+
		"left join protocol_endpoints using(idprotocol,version)\n"+
		"left join template t1 using(idtemplate)\n"+
		"left join dictionary d on t1.idtemplate=d.idsubject\n"+
		"left join template t2 on t2.idtemplate=d.idobject\n"+
		"where qmrf_number=?";
	


	public enum RetrieveMode {
		child,
		childandarent,
		all,
		protocol
	}
	protected RetrieveMode retrieveMode = RetrieveMode.child;
	protected String qmrf_number = null;
	
	public String getQmrf_number() {
		return qmrf_number;
	}

	public void setQmrf_number(String qmrf_number) {
		this.qmrf_number = qmrf_number;
	}

	public RetrieveMode getIncludeParent() {
		return retrieveMode;
	}

	public void setIncludeParent(RetrieveMode includeParent) {
		this.retrieveMode = includeParent;
	}

	public QueryOntology(D dictionary) {
		setFieldname(true);
		setValue(dictionary);
	}
	
	public QueryOntology() {
		setFieldname(true);
	}
	public double calculateMetric(D object) {
		return 1;
	}

	public boolean isPrescreen() {
		return false;
	}

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params = new ArrayList<QueryParam>();
		switch (retrieveMode) {
		case all: {
			String pattern = getValue().getTemplate();
			params.add(new QueryParam<String>(String.class, pattern));
			params.add(new QueryParam<String>(String.class, pattern));
			params.add(new QueryParam<String>(String.class, pattern));
			params.add(new QueryParam<String>(String.class, pattern));
			break;
		}
		case protocol: {
			params.add(new QueryParam<String>(String.class, getQmrf_number()));
			break;
		}
		default: {
			String value = getFieldname()?getValue().getTemplate():getValue().getParentTemplate();
			if (RetrieveMode.childandarent.equals(retrieveMode))
				params.add(new QueryParam<String>(String.class, value));
			params.add(new QueryParam<String>(String.class, value));
		}	
		}
		return params;
	}

	public String getSQL() throws AmbitException {
		switch (retrieveMode) {
		case all: 
			return sqlAll;
		case protocol: 
			return sqlProtocol;
		default: {
			String value = getFieldname()?getValue().getTemplate():getValue().getParentTemplate();
			String c = (value==null?"is":"=");
			return String.format(RetrieveMode.childandarent.equals(retrieveMode)?sqlParent+sqlChild:sqlChild,c,c,c);
		}
		}
	}

	public D getObject(ResultSet rs) throws AmbitException {
		try {
			String[] endpoint = new String[] {
					rs.getString("code"),rs.getString(3)
			};
			String[] parent = new String[] {
					rs.getString("category"),rs.getString(2)
			};
			if ((endpoint[0]==null) || (endpoint[1]==null)) try {
				String label = rs.getString("endpointname");
				endpoint = EndpointTest.split(label);
			} catch (Exception x) { endpoint = new String[] {"",""};}
			if ((parent[0]==null) || (parent[1]==null)) try {
				String label = rs.getString("endpointgroup");
				parent = EndpointTest.split(label);
			} catch (Exception x) { parent = new String[] {"",""};}				
			
			EndpointTest result = new EndpointTest(null,null);
			result.setName(endpoint[1]);
			result.setCode(endpoint[0]);
			result.setParentCode(parent[0]);
			result.setParentTemplate(parent[1]);
			return (D)result;
		} catch (SQLException x) {
			throw new AmbitException(x);
		}
	}

	@Override
	public String toString() {
		return getValue()==null?"Directory":
			String.format("%s",getFieldname()?getValue().getTemplate():getValue().getParentTemplate());
	}
	
}
