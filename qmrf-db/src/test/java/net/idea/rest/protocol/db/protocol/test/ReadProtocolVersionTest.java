package net.idea.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.db.ReadProtocolVersions;
import net.idea.rest.protocol.db.test.CRUDTest;
import net.idea.rest.protocol.db.test.QueryTest;


public class ReadProtocolVersionTest extends QueryTest<ReadProtocol> {


	@Override
	protected ReadProtocol createQuery() throws Exception {
		return new ReadProtocolVersions(CRUDTest.id2v1);
	}

	@Override
	protected void verify(ReadProtocol query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			Assert.assertEquals(2,protocol.getID());
			Assert.assertNotNull(protocol.getKeywords());
			Assert.assertEquals(1,protocol.getKeywords().size());
			Assert.assertNotNull(protocol.getOwner());
			//Assert.assertNotNull(protocol.getOwner().getFirstname());
			records++;
		}
		Assert.assertEquals(2,records);
		
	}

}
