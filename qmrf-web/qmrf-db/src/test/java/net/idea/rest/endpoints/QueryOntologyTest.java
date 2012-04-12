package net.idea.rest.endpoints;

import java.sql.ResultSet;

import junit.framework.Assert;

import net.idea.rest.endpoints.db.QueryOntology;
import net.idea.rest.protocol.db.test.QueryTest;
import ambit2.base.data.Dictionary;

public class QueryOntologyTest extends QueryTest<QueryOntology> {

	@Override
	protected QueryOntology createQuery() throws Exception {
		QueryOntology q = new QueryOntology();
		q.setValue(new Dictionary("Physical Chemical Properties",null));
		return q;
	}

	@Override
	protected void verify(QueryOntology query, ResultSet rs) throws Exception {
		int count=0;
		while (rs.next()) {
			count++;
		}
		Assert.assertEquals(5,count);
	}

}
