package net.idea.rest.protocol.db.user.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.db.test.QueryTest;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.db.ReadUser;


public class ReadUserTest  extends QueryTest<ReadUser> {

	@Override
	protected ReadUser createQuery() throws Exception {
		DBUser user = new DBUser(3);
		return new ReadUser(user);
	}

	@Override
	protected void verify(ReadUser query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBUser user = query.getObject(rs);
			Assert.assertEquals(3,user.getID());
			Assert.assertEquals("guest",user.getUserName());
			Assert.assertEquals("abcdef",user.getFirstname());
			Assert.assertEquals("ABCDEF",user.getLastname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}


}
