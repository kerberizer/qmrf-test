package net.idea.qmrf.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.restnet.i.tools.DownloadTool;
import net.toxbank.client.io.rdf.ProtocolIO;
import net.toxbank.client.resource.Protocol;
import net.toxbank.client.resource.Protocol.STATUS;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.opentox.dsl.task.RemoteTask;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * test for {@link PropertyResource}
 * 
 * @author nina
 * 
 */
public class ProtocolResourceTest extends ProtectedResourceTest {

	@Override
	protected boolean isAAEnabled() {
		return false;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setUpDatabase(dbFile);

	}

	@Override
	public String getTestURI() {
		return String.format("http://localhost:%d%s/%s-2-2", port,
				Resources.protocol, DBProtocol.prefix);
	}

	@Test
	public void testURI() throws Exception {
		testGet(getTestURI(), MediaType.TEXT_URI_LIST);
	}

	@Override
	public boolean verifyResponseURI(String uri, MediaType media, InputStream in)
			throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int count = 0;
		while ((line = r.readLine()) != null) {
			Assert.assertEquals(String.format("http://localhost:%d%s/%s-2-2",
					port, Resources.protocol, DBProtocol.prefix), line);
			count++;
		}
		return count == 1;
	}

	@Test
	public void testRDF() throws Exception {
		testGet(getTestURI(), MediaType.APPLICATION_RDF_XML);
	}

	@Override
	public OntModel verifyResponseRDFXML(String uri, MediaType media,
			InputStream in) throws Exception {

		OntModel model = ModelFactory.createOntologyModel();
		model.read(in, null);

		ProtocolIO ioClass = new ProtocolIO();
		List<Protocol> protocols = ioClass.fromJena(model);
		Assert.assertEquals(1, protocols.size());
		Assert.assertEquals(String.format(
				"http://localhost:8181/protocol/%s-2-2", DBProtocol.prefix),
				protocols.get(0).getResourceURL().toString());
		Assert.assertEquals("QMRF-2-2", protocols.get(0).getIdentifier());
		Assert.assertEquals("QSAR model for narcosis", protocols.get(0)
				.getTitle());
		Assert.assertNotNull(protocols.get(0).getAbstract());
		//Assert.assertEquals(5, protocols.get(0).getAbstract().indexOf("\u2122")); // TM
																					// symbol

		Assert.assertFalse(protocols.get(0).isPublished());
		Assert.assertNotNull(protocols.get(0).getOwner());
		Assert.assertEquals(
				String.format("http://localhost:%d%s/U10", port, Resources.user),
				protocols.get(0).getOwner().getResourceURL().toString());
		// Assert.assertEquals("abcdef",
		// protocols.get(0).getOwner().getFirstname());
		return model;
	}

	/*
	 * @Test public void testQueryName() throws Exception { RDFPropertyIterator
	 * iterator = new RDFPropertyIterator(new Reference(
	 * String.format("http://localhost:%d%s?%s=%s", port,
	 * PropertyResource.featuredef,QueryResource.search_param,"Property") ));
	 * iterator.setCloseModel(true); iterator.setBaseReference(new
	 * Reference(String.format("http://localhost:%d", port))); while
	 * (iterator.hasNext()) { Property p = iterator.next();
	 * Assert.assertTrue(p.getName().startsWith("Property"));
	 * 
	 * } iterator.close(); }
	 * 
	 * @Test public void testRDFXML() throws Exception { RDFPropertyIterator
	 * iterator = new RDFPropertyIterator(new Reference(getTestURI()));
	 * iterator.setBaseReference(new
	 * Reference(String.format("http://localhost:%d", port))); while
	 * (iterator.hasNext()) {
	 * 
	 * Property p = iterator.next();
	 * Assert.assertEquals("Property 1",p.getName());
	 * Assert.assertEquals(1,p.getId()); } iterator.close(); }
	 * 
	 * /*
	 * 
	 * @Test public void testHTML() throws Exception {
	 * testGet(getTestURI(),MediaType.TEXT_HTML); }
	 * 
	 * @Override public boolean verifyResponseHTML(String uri, MediaType media,
	 * InputStream in) throws Exception { BufferedReader r = new
	 * BufferedReader(new InputStreamReader(in)); String line = null; int count
	 * = 0; while ((line = r.readLine())!= null) {
	 * 
	 * count++; } return count>1; }
	 */

	@Test
	public void testDelete() throws Exception {
		IDatabaseConnection c = getConnection();
		ITable table = c
				.createQueryTable("EXPECTED",
						"SELECT idprotocol,version FROM protocol where idprotocol=2 and version=2");
		Assert.assertEquals(new BigInteger("2"),table.getValue(0, "idprotocol"));
		c.close();
		String org = String.format("http://localhost:%d%s/%s-2-2", port,
				Resources.protocol, DBProtocol.prefix);
		RemoteTask task = testAsyncPoll(new Reference(org),	MediaType.TEXT_URI_LIST, null, Method.DELETE);
		Assert.assertEquals(Status.SUCCESS_OK, task.getStatus());
		// Assert.assertNull(task.getResult());
		c = getConnection();
		table = c.createQueryTable("EXPECTED",
				"SELECT * FROM protocol where idprotocol=2 and version=2");
		Assert.assertEquals(0, table.getRowCount());
		c.close();
	}

	@Override
	public Object verifyResponseJavaObject(String uri, MediaType media,
			Representation rep) throws Exception {
		Object o = super.verifyResponseJavaObject(uri, media, rep);
		Assert.assertTrue(o instanceof ReadProtocol);

		return o;
	}

	@Test
	public void testUpdateEntryFromMultipartWeb() throws Exception {
		String uri = String.format("http://localhost:%d%s/%s-2-2", port,
				Resources.protocol, DBProtocol.prefix);
		createEntryFromMultipartWeb(new Reference(uri), Method.PUT);

		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
		Assert.assertEquals(3, table.getRowCount());
		table = c
				.createQueryTable(
						"EXPECTED",
						"SELECT p.idprotocol,p.version,published from protocol p where p.idprotocol=2 and version=1");
		Assert.assertEquals(1, table.getRowCount());
		Assert.assertEquals(Boolean.TRUE, table.getValue(0, "published"));

		c.close();
	}

	@Test
	public void testCreateVersionEntryFromMultipartWeb() throws Exception {
		String url = createEntryFromMultipartWeb(new Reference(getTestURI()
				+ Resources.versions));
		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
		Assert.assertEquals(4, table.getRowCount());
		table = c
				.createQueryTable(
						"EXPECTED",
						"SELECT p.idprotocol,p.version,filename from protocol p where p.idprotocol=1 order by version");
		Assert.assertEquals(2, table.getRowCount());
		Assert.assertEquals(new BigInteger("1"), table.getValue(0, "version"));
		Assert.assertEquals(new BigInteger("2"), table.getValue(1, "version"));
		File f = new File(new URI(table.getValue(1, "filename").toString()));
		Assert.assertNotSame(getTestURI(), url);
		Assert.assertTrue(f.exists());
		f.delete();
		c.close();
	}

	@Test
	public void testCreateEntryFromMultipartWeb() throws Exception {
		String url = createEntryFromMultipartWeb(new Reference(String.format(
				"http://localhost:%d%s", port, Resources.protocol)));

		testGet(String.format("%s%s", url, Resources.document),
				MediaType.APPLICATION_PDF);

		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
		Assert.assertEquals(4, table.getRowCount());
		table = c
				.createQueryTable(
						"EXPECTED",
						"SELECT p.idprotocol,p.version,filename,pa.iduser,status from protocol p join protocol_authors pa where pa.idprotocol=p.idprotocol and p.version=pa.version and p.idprotocol>2 order by pa.iduser");
		Assert.assertEquals(2, table.getRowCount());
		Assert.assertEquals(new BigInteger("1"), table.getValue(0, "version"));
		Assert.assertEquals(new BigInteger("1"), table.getValue(0, "iduser"));
		Assert.assertEquals(new BigInteger("2"), table.getValue(1, "iduser"));
		Assert.assertEquals(STATUS.SOP.toString(), table.getValue(0, "status"));

		File f = new File(new URI(table.getValue(0, "filename").toString()));
		// System.out.println(f);
		Assert.assertTrue(f.exists());
		f.delete();
		c.close();
	}

	public String createEntryFromMultipartWeb(Reference uri) throws Exception {
		return createEntryFromMultipartWeb(uri, Method.POST);
	}

	public String createEntryFromMultipartWeb(Reference uri, Method method)
			throws Exception {
		URL url = getClass().getClassLoader().getResource(
				"net/idea/qmrf/protocol-sample.pdf");
		File file = new File(url.getFile());

		String[] names = new String[ReadProtocol.fields.values().length];
		String[] values = new String[ReadProtocol.fields.values().length];
		int i = 0;
		for (ReadProtocol.fields field : ReadProtocol.entryFields) {
			switch (field) {
			case idprotocol:
				continue;
			case filename:
				continue;
				/*
				 * case user_uri: { values[i] =
				 * String.format("http://localhost:%d%s/%s"
				 * ,port,Resources.user,"U1"); break; }
				 */
			case project_uri: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.project, "G1");
				break;
			}
			case organisation_uri: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.organisation, "G2");
				break;
			}
			case user_uri: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.user, "U1");
				break;
			}
			case author_uri: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.user, "U2");
				break;
			}
			case allowReadByGroup: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.organisation, "G1");
				break;
			}
			case allowReadByUser: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.user, "U2");
				break;
			}
			case status: {
				values[i] = STATUS.SOP.toString();
				break;
			}
			case anabstract: {
				values[i] = "My abstract\u2122";
				break;
			}
			case published: {
				values[i] = Boolean.TRUE.toString();
				break;
			}
			default: {
				values[i] = field.name();
			}
			}
			names[i] = field.name();

			i++;
		}
		// yet another author
		values[i] = String.format("http://localhost:%d%s/%s", port,
				Resources.user, "U1");
		names[i] = ReadProtocol.fields.author_uri.name();
		values[i + 1] = null;
		names[i + 1] = ReadProtocol.fields.author_uri.name();
		Representation rep = getMultipartWebFormRepresentation(names, values,
				file, MediaType.APPLICATION_PDF.toString());

		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
		Assert.assertEquals(3, table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(uri, MediaType.TEXT_URI_LIST, rep,
				method);
		// wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		if (!task.isCompletedOK())
			System.out.println(task.getError());
		Assert.assertTrue(task
				.getResult()
				.toString()
				.startsWith(
						String.format("http://localhost:%d/protocol/%s", port,
								DBProtocol.prefix)));

		return task.getResult().toString();

	}

	public void testDownloadFile() throws Exception {
		testGet(String.format("http://localhost:%d%s/%s-2-1%s", port,
				Resources.protocol, DBProtocol.prefix, Resources.document),
				MediaType.APPLICATION_PDF);
	}

	@Override
	public boolean verifyResponsePDF(String uri, MediaType media, InputStream in)
			throws Exception {

		File file = File.createTempFile("test", ".pdf");
		file.deleteOnExit();
		DownloadTool.download(in, file);
		// System.out.println(file.getAbsolutePath());
		return file.exists();
	}
}
