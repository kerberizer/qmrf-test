package net.idea.rest.protocol.db.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.db.ReadOrganisation;
import net.idea.rest.user.DBUser;


public class ReadGroupByUserTest extends QueryTest<ReadOrganisation> {
	@Override
	protected ReadOrganisation createQuery() throws Exception {
		DBOrganisation p = new DBOrganisation();
		DBUser user = new DBUser(1);
		ReadOrganisation q = new ReadOrganisation(p);
		q.setFieldname(user);
		return q;
	}

	@Override
	protected void verify(ReadOrganisation query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBOrganisation group = query.getObject(rs);
			Assert.assertEquals(2,group.getID());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}