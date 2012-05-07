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
	protected String sqlParent = 
	
		"select 0,t2.name,t1.name,t2.code as category,t1.code as code\n"+
		"from ((`template` `t1`\n"+
		"join `dictionary` `d` on((`t1`.`idtemplate` = `d`.`idsubject`)))\n"+
		"join `template` `t2` on((`d`.`idobject` = `t2`.`idtemplate`)))\n"+
		"where t1.name %s ?\n"+
		"union\n";		
	protected String sqlChild = 	
		"select 1,t2.name,t1.name,t2.code as category,t1.code as code\n"+
		"from ((`template` `t1`\n"+
		"join `dictionary` `d` on((`t1`.`idtemplate` = `d`.`idsubject`)))\n"+
		"join `template` `t2` on((`d`.`idobject` = `t2`.`idtemplate`)))\n"+
		"where t2.name %s ?\n"
		;
	
	protected String sqlAll = 	
		"select 2,t2.name,t1.name,t2.code as category,t1.code as code\n"+
		"from ((`template` `t1`\n"+
		"join `dictionary` `d` on((`t1`.`idtemplate` = `d`.`idsubject`)))\n"+
		"join `template` `t2` on((`d`.`idobject` = `t2`.`idtemplate`)))\n"+
		"where\n"+
		"(\n"+
		"t1.code regexp ?\n"+
		"or t2.code regexp ?\n"+
		"or t1.name regexp ?\n"+
		"or t2.name regexp ?\n"+
		")\n"+
		"and relationship != \"same_as\"\n"+
		"order by t1.idtemplate\n";

	public enum RetrieveMode {
		child,
		childandarent,
		all
	}
	protected RetrieveMode includeParent = RetrieveMode.child;
	
	public RetrieveMode getIncludeParent() {
		return includeParent;
	}

	public void setIncludeParent(RetrieveMode includeParent) {
		this.includeParent = includeParent;
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
		if (RetrieveMode.all.equals(includeParent)) {
			String pattern = getValue().getTemplate();
			params.add(new QueryParam<String>(String.class, pattern));
			params.add(new QueryParam<String>(String.class, pattern));
			params.add(new QueryParam<String>(String.class, pattern));
			params.add(new QueryParam<String>(String.class, pattern));
		} else {
			String value = getFieldname()?getValue().getTemplate():getValue().getParentTemplate();
			if (RetrieveMode.childandarent.equals(includeParent))
				params.add(new QueryParam<String>(String.class, value));
			params.add(new QueryParam<String>(String.class, value));
		}	
		return params;
	}

	public String getSQL() throws AmbitException {
		if (RetrieveMode.all.equals(includeParent)) {
			return sqlAll;
		} else {
			String value = getFieldname()?getValue().getTemplate():getValue().getParentTemplate();
			String c = (value==null?"is":"=");
			return String.format(RetrieveMode.childandarent.equals(includeParent)?sqlParent+sqlChild:sqlChild,c,c,c);
		}
	}

	public D getObject(ResultSet rs) throws AmbitException {
		try {
			EndpointTest result = new EndpointTest(rs.getString(3),rs.getString(2));
			result.setCode(rs.getString("code"));
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
