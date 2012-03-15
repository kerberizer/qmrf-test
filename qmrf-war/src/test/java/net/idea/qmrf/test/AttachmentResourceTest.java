package net.idea.qmrf.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;

import junit.framework.Assert;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.attachments.DBAttachment.attachment_type;
import net.idea.rest.protocol.attachments.db.ReadAttachment;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.opentox.dsl.task.RemoteTask;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;

public class AttachmentResourceTest extends ResourceTest {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setUpDatabase(dbFile);
		
	}
	@Override
	public String getTestURI() {
		return String.format("http://localhost:%d%s/%s-2009-83-1%s", port,Resources.protocol,
					DBProtocol.prefix,Resources.attachment);
	}
	
	@Test
	public void testURI() throws Exception {
		testGet(getTestURI(),MediaType.TEXT_URI_LIST);
	}
	/**
	 * The URI should be /protocol/QMRF-200983-1/attachment
	 */
	@Override
	public boolean verifyResponseURI(String uri, MediaType media, InputStream in)
			throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int count = 0;
		while ((line = r.readLine())!= null) {
			System.out.println(line);
			Assert.assertTrue(line.startsWith(
					String.format("http://localhost:%d%s/%s-2009-83-1%s",port,Resources.protocol,
							DBProtocol.prefix,Resources.attachment)
							));
			count++;
		}
		return count==4;
	}	
	
	@Test
	public void testCreateEntryFromMultipartWeb() throws Exception {
		String url = createEntryFromMultipartWeb(new Reference(getTestURI()));
		
		Assert.assertEquals(String.format("http://localhost:%d/protocol/QMRF-2009-83-1/attachment",port),url);

		
   	    IDatabaseConnection c = getConnection();	
		ITable  table = 	c.createQueryTable("EXPECTED","SELECT * FROM protocol");
		Assert.assertEquals(5,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT idattachment,idprotocol,version from attachments p where p.idprotocol=83 and p.version=1");
		Assert.assertEquals(5,table.getRowCount());
		Assert.assertEquals(new BigInteger("1"),table.getValue(0,"version"));
		Assert.assertEquals(new BigInteger("83"),table.getValue(0,"idprotocol"));
		c.close();
	}
	
	public String createEntryFromMultipartWeb(Reference uri) throws Exception {

		URL url = getClass().getClassLoader().getResource("net/idea/qmrf/protocol-sample.pdf");
		Assert.assertNotNull(url);
		File file = new File(url.getFile());
		
		String[] names = new String[0];
		String[] values = new String[0];
		Representation rep = getMultipartWebFormRepresentation(names,values,attachment_type.document.name(),file,MediaType.APPLICATION_PDF.toString());
		
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM protocol");
		Assert.assertEquals(5,table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(uri,
				MediaType.TEXT_URI_LIST, rep,
				Method.POST);
		//wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		if (!task.isCompletedOK())
			System.out.println(task.getError());
		Assert.assertTrue(task.getResult().toString().startsWith(
							String.format("http://localhost:%d/protocol/%s",port,DBProtocol.prefix)));
		
		return task.getResult().toString();


	}		
	@Override
	public Object verifyResponseJavaObject(String uri, MediaType media,
			Representation rep) throws Exception {
		Object o = super.verifyResponseJavaObject(uri, media, rep);
		Assert.assertTrue(o instanceof ReadAttachment);

		return o;
	}
	
	
	@Test
	public void testImportAttachment() throws Exception {
		Reference uri = new Reference(String.format("http://localhost:%d/protocol/QMRF-2009-83-1/attachment/A108/dataset",port));

		IDatabaseConnection c = getConnection();	
		ITable  table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported,name FROM attachments where idattachment=108");
		Assert.assertEquals(Boolean.FALSE,table.getValue(0,"imported"));
		c.close();
		
		Form form = new Form();
		form.add("import", "true");
		
		RemoteTask task = testAsyncPoll(
				uri,
				MediaType.TEXT_URI_LIST, form.getWebRepresentation(),
				Method.POST);
		//wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		if (!task.isCompletedOK())
			System.out.println(task.getError());
		Assert.assertEquals(uri.toString(),task.getResult().toString());

		c = getConnection();	
		 table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported,name FROM attachments where idattachment=108");
		Assert.assertEquals(Boolean.TRUE,table.getValue(0,"imported"));
		c.close();

	}
	
	
}
