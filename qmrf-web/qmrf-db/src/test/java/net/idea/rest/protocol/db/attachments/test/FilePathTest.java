package net.idea.rest.protocol.db.attachments.test;

import junit.framework.Assert;
import net.idea.rest.protocol.ProtocolFactory;

import org.junit.Test;

public class FilePathTest {
	@Test
	public void testWinFile() {
		Assert.assertEquals("test.pdf",ProtocolFactory.stripFileName("C:\\Documents and Settings\\tester\\Desktop\\test.pdf"));
	}
	
	@Test
	public void testUxFile() {
		Assert.assertEquals("test.pdf",ProtocolFactory.stripFileName("/srv/qmrf-docs/document/test.pdf"));
	}
	
}
