package net.idea.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocolByTextSearch;
import net.idea.rest.protocol.db.test.QueryTest;

public class ReadProtocolByFreeTextTest  extends QueryTest<ReadProtocolByTextSearch> {

	@Override
	protected ReadProtocolByTextSearch createQuery() throws Exception {
		ReadProtocolByTextSearch query = new ReadProtocolByTextSearch();
		query.setFieldname("mutagenicity");
		return query;
	}

	@Override
	protected void verify(ReadProtocolByTextSearch query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			Assert.assertEquals(83,protocol.getID());
			Assert.assertNotNull(protocol.getOwner());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}