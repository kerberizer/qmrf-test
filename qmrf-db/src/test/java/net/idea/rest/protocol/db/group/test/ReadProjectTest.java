package net.idea.rest.protocol.db.group.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.groups.DBProject;
import net.idea.rest.groups.db.ReadProject;
import net.idea.rest.protocol.db.test.QueryTest;


public class ReadProjectTest  extends QueryTest<ReadProject> {

	@Override
	protected ReadProject createQuery() throws Exception {
		DBProject p = new DBProject();
		p.setID(1);
		return new ReadProject(p);
	}

	@Override
	protected void verify(ReadProject query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProject group = query.getObject(rs);
			Assert.assertEquals(1,group.getID());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}
