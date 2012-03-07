package net.idea.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.db.test.QueryTest;


public class ReadProtocolByTimeStampTest extends QueryTest<ReadProtocol> {

	@Override
	protected ReadProtocol createQuery() throws Exception {
		ReadProtocol q = new ReadProtocol();
		DBProtocol p = new DBProtocol();
		p.setTimeModified(1254757473000L);//in msec
		/**
		 * in DB: 1241795264, 1254757473 
		 */
		q.setValue(p);
		return q;
	}

	@Override
	protected void verify(ReadProtocol query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			DBProtocol protocol = query.getObject(rs);
			//System.out.println(String.format("%d\t%s\t%s",protocol.getID(),protocol.getTimeModified(),new Date(protocol.getTimeModified())));
			//Assert.assertEquals(2,protocol.getID());
			//Assert.assertNotNull(protocol.getKeywords());
			//Assert.assertEquals(new Long(1326699051000L),protocol.getTimeModified());
			records++;
		}
		Assert.assertEquals(2,records);
		
	}
	
}	
