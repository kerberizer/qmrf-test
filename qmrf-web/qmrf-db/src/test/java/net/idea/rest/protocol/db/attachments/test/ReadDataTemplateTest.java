package net.idea.rest.protocol.db.attachments.test;

import java.sql.ResultSet;

import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.template.ReadFilePointers;
import net.idea.rest.protocol.db.test.QueryTest;

/**
 * TODO: not used so far
 * @author nina
 *
 */

public class ReadDataTemplateTest  extends QueryTest<ReadFilePointers> {

	@Override
	protected ReadFilePointers createQuery() throws Exception {
		DBProtocol protocol = new DBProtocol(1,1);
		return new ReadFilePointers(protocol);
	}

	@Override
	protected void verify(ReadFilePointers query, ResultSet rs) throws Exception {
		/*
		int records = 0;
		while (rs.next()) {
			DBAttachment attachment = query.getObject(rs);
			Assert.assertEquals(1,attachment.getID());
			Assert.assertNotNull(attachment.getResourceURL());
			
			Assert.assertTrue(attachment.getResourceURL().toString().startsWith("http://example.com/protocol/P1/attachment/"));
			records++;
		}
		Assert.assertEquals(1,records);
		*/
	}

}
