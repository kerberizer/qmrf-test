package net.idea.rest.protocol.db.protocol.test;

import java.sql.ResultSet;

import junit.framework.Assert;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocolAccessLocal;
import net.idea.rest.protocol.db.test.CRUDTest;
import net.idea.rest.protocol.db.test.QueryTest;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.policy.PolicyRule;

/**
 * Tests local access policy (based on published flag)
 * @author nina
 *
 */
public class ReadProtocolAccessLocalTest extends QueryTest<ReadProtocolAccessLocal> {

	@Override
	protected ReadProtocolAccessLocal createQuery() throws Exception {
		ReadProtocolAccessLocal q = new ReadProtocolAccessLocal();
		q.setFieldname(new DBProtocol(CRUDTest.id83v1));
		q.setValue("JRC QSAR");
		return q;
	}

	@Override
	protected void verify(ReadProtocolAccessLocal query, ResultSet rs) throws Exception {
		int records = 0;
		while (rs.next()) {
			AccessRights policy = query.getObject(rs);
			//Assert.assertEquals(1,user.getID());
			//Assert.assertEquals("guest",user.getUserName());
			System.out.println(policy);
			for (PolicyRule rule : policy.getRules()) {
				Assert.assertTrue(rule.allowsGET());
				Assert.assertFalse(rule.allowsPOST());
				Assert.assertFalse(rule.allowsPUT());
				Assert.assertFalse(rule.allowsDELETE());
			}
			
			records++;
		}
		Assert.assertEquals(1,records);
		
	}

}