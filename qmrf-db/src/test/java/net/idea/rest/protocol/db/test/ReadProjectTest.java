package net.idea.rest.protocol.db.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.groups.DBProject;
import net.idea.rest.groups.db.ReadProject;
import net.toxbank.client.io.rdf.TOXBANK;


public class ReadProjectTest  extends QueryTest<ReadProject> {

	@Override
	protected ReadProject createQuery() throws Exception {
		DBProject p = new DBProject();
		p.setID(2);
		return new ReadProject(p);
	}

	@Override
	protected void verify(ReadProject query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProject group = query.getObject(rs);
			Assert.assertEquals(2,group.getID());
			Assert.assertEquals(String.format("%s%s", TOXBANK.URI,TOXBANK.SEURAT1),group.getCluster().toExternalForm());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}
