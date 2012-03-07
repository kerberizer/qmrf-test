package net.idea.rest.protocol.db.group.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.db.ReadOrganisation;
import net.idea.rest.protocol.db.test.QueryTest;


public class ReadOrganisationTest extends QueryTest<ReadOrganisation> {
	@Override
	protected ReadOrganisation createQuery() throws Exception {
		DBOrganisation p = new DBOrganisation();
		p.setID(5);
		return new ReadOrganisation(p);
	}

	@Override
	protected void verify(ReadOrganisation query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBOrganisation group = query.getObject(rs);
			Assert.assertEquals(5,group.getID());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}
