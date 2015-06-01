package net.idea.qmrf.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.attachments.DBAttachment.attachment_type;
import net.idea.rest.protocol.attachments.db.ReadAttachment;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;

public class AttachmentResourceTest extends ResourceTest {
    protected final static String id83 = "8f0adb53-862e-11e1-ba85-00ff3739b863";

    @Override
    public void setUp() throws Exception {
	super.setUp();
	setUpDatabase(dbFile);

    }

    @Override
    public String getTestURI() {
	return String.format("http://localhost:%d%s/%s%s", port, Resources.protocol, id83, Resources.attachment);
    }

    @Test
    public void testURI() throws Exception {
	testGet(getTestURI(), MediaType.TEXT_URI_LIST);
    }

    @Override
    public void testJSON() throws Exception {

    }
    /**
     * The URI should be /protocol/{id83}/attachment
     */
    @Override
    public boolean verifyResponseURI(String uri, MediaType media, InputStream in) throws Exception {
	BufferedReader r = new BufferedReader(new InputStreamReader(in));
	String line = null;
	int count = 0;
	while ((line = r.readLine()) != null) {
	    System.out.println(line);
	    Assert.assertTrue(line.startsWith(String.format("http://localhost:%d%s/%s%s", port, Resources.protocol,
		    id83, Resources.attachment)));
	    count++;
	}
	return count == 4;
    }

    @Test
    public void testDeleteAttachment() throws Exception {

	Reference uri = new Reference(String.format("http://localhost:%d/protocol/%s/attachment/A110", port, id83));

	RemoteTask task = testAsyncPoll(uri, MediaType.TEXT_URI_LIST, null, Method.DELETE);
	// wait to complete
	while (!task.isDone()) {
	    task.poll();
	    Thread.sleep(100);
	    Thread.yield();
	}
	if (!task.isCompletedOK())
	    System.out.println(task.getError());
	System.out.println(task.getResult());

	IDatabaseConnection c = getConnection();
	ITable table = c
		.createQueryTable("EXPECTED",
			"SELECT idattachment,idprotocol,version from attachments p where p.idprotocol=83 and p.version=1 and idattachment=110");
	Assert.assertEquals(0, table.getRowCount());
	c.close();
    }

    @Test
    public void testCreateEntryFromMultipartWeb() throws Exception {
	String url = createEntryFromMultipartWeb(new Reference(getTestURI()));

	Assert.assertEquals(String.format("http://localhost:%d/unpublished/%s/attachment", port, id83), url);

	IDatabaseConnection c = getConnection();
	ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
	Assert.assertEquals(5, table.getRowCount());
	table = c.createQueryTable("EXPECTED",
		"SELECT idattachment,idprotocol,version from attachments p where p.idprotocol=83 and p.version=1");
	Assert.assertEquals(5, table.getRowCount());
	Assert.assertEquals(new BigInteger("1"), table.getValue(0, "version"));
	Assert.assertEquals(new BigInteger("83"), table.getValue(0, "idprotocol"));
	c.close();
    }

    public String createEntryFromMultipartWeb(Reference uri) throws Exception {

	URL url = getClass().getClassLoader().getResource("net/idea/qmrf/protocol-sample.pdf");
	Assert.assertNotNull(url);
	File file = new File(url.getFile());

	String[] names = new String[0];
	String[] values = new String[0];
	MultipartEntity rep = getMultipartWebFormRepresentation(names, values, attachment_type.document.name(), file,
		MediaType.APPLICATION_PDF.toString());

	IDatabaseConnection c = getConnection();
	ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
	Assert.assertEquals(5, table.getRowCount());
	c.close();

	RemoteTask task = testAsyncPoll(uri, MediaType.TEXT_URI_LIST, rep, Method.POST);
	// wait to complete
	while (!task.isDone()) {
	    task.poll();
	    Thread.sleep(100);
	    Thread.yield();
	}
	if (!task.isCompletedOK())
	    System.out.println(task.getError());

	Assert.assertTrue(task.getResult().toString().startsWith(String.format("http://localhost:%d/unpublished/", port)));

	return task.getResult().toString();

    }

    @Override
    public Object verifyResponseJavaObject(String uri, MediaType media, Representation rep) throws Exception {
	Object o = super.verifyResponseJavaObject(uri, media, rep);
	Assert.assertTrue(o instanceof ReadAttachment);

	return o;
    }

    @Test
    public void testImportAttachment() throws Exception {
	Reference uri = new Reference(String.format("http://localhost:%d/protocol/%s/attachment/A108/dataset", port,
		id83));

	IDatabaseConnection c = getConnection();
	ITable table = c.createQueryTable("EXPECTED",
		"SELECT idattachment,imported,name FROM attachments where idattachment=108");
	Assert.assertEquals(Boolean.FALSE, table.getValue(0, "imported"));
	c.close();

	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	formparams.add(new BasicNameValuePair("import", "true"));

	RemoteTask task = testAsyncPoll(uri, MediaType.TEXT_URI_LIST, new UrlEncodedFormEntity(formparams, "UTF-8"),
		Method.POST);
	// wait to complete
	while (!task.isDone()) {
	    task.poll();
	    Thread.sleep(100);
	    Thread.yield();
	}
	if (!task.isCompletedOK())
	    System.out.println(task.getError());
	Assert.assertEquals(uri.toString(), task.getResult().toString());

	c = getConnection();
	table = c.createQueryTable("EXPECTED",
		"SELECT idattachment,imported,name FROM attachments where idattachment=108");
	Assert.assertEquals(Boolean.TRUE, table.getValue(0, "imported"));
	c.close();

    }

}
