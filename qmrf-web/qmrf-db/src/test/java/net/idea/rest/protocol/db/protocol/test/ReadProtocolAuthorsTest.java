package net.idea.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.test.CRUDTest;
import net.idea.rest.protocol.db.test.QueryTest;
import net.idea.rest.user.author.db.ReadAuthor;
import net.idea.restnet.user.DBUser;


public class ReadProtocolAuthorsTest extends QueryTest<ReadAuthor> {

	@Override
	protected ReadAuthor createQuery() throws Exception {
		return new ReadAuthor(new DBProtocol(CRUDTest.id83v1),null);
	}

	@Override
	protected void verify(ReadAuthor query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBUser user = query.getObject(rs);
			Assert.assertEquals(10,user.getID());
			Assert.assertEquals("TestQMRF",user.getFirstname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}