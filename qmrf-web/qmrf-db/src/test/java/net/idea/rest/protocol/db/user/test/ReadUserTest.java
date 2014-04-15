package net.idea.rest.protocol.db.user.test;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import net.idea.rest.JSONUtils;
import net.idea.rest.protocol.db.test.QueryTest;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.db.ReadUser;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;


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
	@Test
	public void testChars() {
		Assert.assertTrue(JSONUtils.acceptString("ab!c@%+$^?:.(){}[]~-_#A123def"));
		Assert.assertFalse(JSONUtils.acceptString("ab!c@%+$\u0001^?:.(){}[]~-_#A123def"));
	}

}
