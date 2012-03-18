package net.idea.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.db.test.QueryTest;

public class ReadProtocolByTitle  extends QueryTest<ReadProtocol> {

	@Override
	protected ReadProtocol createQuery() throws Exception {
		DBProtocol p = new DBProtocol();
		p.setTitle("mutagenicity");
		ReadProtocol query = new ReadProtocol();
		query.setValue(p);
		return query;
	}

	@Override
	protected void verify(ReadProtocol query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			Assert.assertEquals(83,protocol.getID());
			Assert.assertNotNull(protocol.getKeywords());
			Assert.assertEquals(4,protocol.getKeywords().size());
			Assert.assertNotNull(protocol.getOwner());
			//Assert.assertNotNull(protocol.getOwner().getFirstname());
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}