package net.idea.rest.protocol.db.attachments.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.db.ReadAttachment;
import net.idea.rest.protocol.db.test.QueryTest;

/**
 * Attachments
 * @author nina
 *
 */

public class ReadAttachmentsTest  extends QueryTest<ReadAttachment> {

	@Override
	protected ReadAttachment createQuery() throws Exception {
		DBProtocol protocol = new DBProtocol(83,1);
		return new ReadAttachment(protocol);
	}

	@Override
	protected void verify(ReadAttachment query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBAttachment attachment = query.getObject(rs);
			switch ( attachment.getID()) {
			case 108: break;
			case 109: break;
			case 110: break;
			case 111: break;
			default: Assert.fail(String.format("Wrong id %d", attachment.getID()));
			}

			Assert.assertNotNull(attachment.getResourceURL());
			System.out.println(attachment.getResourceURL());
			Assert.assertTrue(attachment.getResourceURL().toString().startsWith("file:/Z:/QMRF/attachments/qmrf/"));
			records++;
		}
		Assert.assertEquals(4,records);

	}

}
