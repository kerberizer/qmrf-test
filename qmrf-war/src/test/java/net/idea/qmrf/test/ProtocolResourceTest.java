package net.idea.qmrf.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.qmrf.client.PublishedStatus;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.restnet.i.tools.DownloadTool;
import net.toxbank.client.io.rdf.ProtocolIO;
import net.toxbank.client.resource.Protocol;
import net.toxbank.client.resource.Protocol.STATUS;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.tools.ant.types.resources.PropertyResource;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;
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

	protected static String id2v1 = "8f0aba27-862e-11e1-ba85-00ff3739b863";
	protected static String id2v2 = "8f0afddb-862e-11e1-ba85-00ff3739b863";
	protected static String id119v1 = "Q2-10-14-119";
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
		return String.format("http://localhost:%d%s/%s", port,
							Resources.protocol, id2v2);
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
			Assert.assertEquals(String.format("http://localhost:%d%s/%s",
					port, Resources.protocol, id2v2), line);
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
				"http://localhost:8181/protocol/%s",id2v2),
				protocols.get(0).getResourceURL().toString());
		Assert.assertEquals(id2v2, protocols.get(0).getIdentifier());
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
		/* TODO refactor IBatchStatistics to keep track of exceptions in a batch */
		IDatabaseConnection c = getConnection();
		ITable table = c
				.createQueryTable("EXPECTED",
						"SELECT idprotocol,version FROM protocol where idprotocol=121 and version=1");
		Assert.assertEquals(new BigInteger("121"),table.getValue(0, "idprotocol"));
		c.close();
		String org = String.format("http://localhost:%d%s/%s", port,
				Resources.protocol, "Q8-10-13-121");
		RemoteTask task = testAsyncPoll(new Reference(org),	MediaType.TEXT_URI_LIST, null, Method.DELETE);
		Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
		// Assert.assertNull(task.getResult());
		c = getConnection();
		table = c.createQueryTable("EXPECTED",
				"SELECT * FROM protocol where idprotocol=121 and version=1");
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
		String uri = String.format("http://localhost:%d%s/%s", port,
										Resources.protocol, id2v2);
		String newURI = createEntryFromMultipartWeb(new Reference(uri), Method.PUT);
		String newQMRF = "Q12-14-0001";
		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
		Assert.assertEquals(5, table.getRowCount());
		table = c.createQueryTable("EXPECTED",
						"SELECT p.idprotocol,p.version,published_status,qmrf_number from protocol p where p.idprotocol=2 and version=2");
		Assert.assertEquals(1, table.getRowCount());
		Assert.assertEquals(PublishedStatus.published.name(), 
				table.getValue(0, ReadProtocol.fields.published_status.name())
				);
		Assert.assertEquals(newQMRF, table.getValue(0, "qmrf_number"));
		table = c.createQueryTable("EXPECTED",
		"SELECT p.idprotocol,p.version,idtemplate from protocol_endpoints p where p.idprotocol=2 and version=2");
		Assert.assertEquals(1, table.getRowCount());
		Assert.assertNotNull(table.getValue(0, "idtemplate"));
		c.close();

		Assert.assertEquals(String.format("http://localhost:%d/protocol/%s",port,newQMRF),newURI);
		System.out.println(newURI);
	}

	@Test
	public void testCreateVersionEntryFromMultipartWeb() throws Exception {
		String url = createEntryFromMultipartWeb(new Reference(String.format(
				"http://localhost:%d%s/%s%s", port,
				Resources.protocol, id119v1 , Resources.versions)));
		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
		Assert.assertEquals(6, table.getRowCount());
		table = c
				.createQueryTable(
						"EXPECTED",
						"SELECT p.idprotocol,p.version,filename,title,abstract,qmrf_number,published_status from protocol p where p.idprotocol=119 order by version");
		Assert.assertEquals(2, table.getRowCount());
		Assert.assertEquals(new BigInteger("1"), table.getValue(0, "version"));
		Assert.assertEquals(new BigInteger("2"), table.getValue(1, "version"));
		Assert.assertNotSame(getTestURI(), url);
		Assert.assertEquals("QSAR for acute toxicity to fish (Danio rerio)",table.getValue(1, "title"));
		Assert.assertNotNull(table.getValue(1, "abstract"));
		Assert.assertNotSame("Q2-10-14-119-v1", table.getValue(0, "qmrf_number"));
		Assert.assertNotSame("Q2-10-14-119", table.getValue(1, "qmrf_number"));
		Assert.assertEquals(PublishedStatus.archived.name(), table.getValue(0, ReadProtocol.fields.published_status.name()));
		Assert.assertEquals(PublishedStatus.published.name(), table.getValue(1, ReadProtocol.fields.published_status.name()));
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
		Assert.assertEquals(6, table.getRowCount());
		table = c
				.createQueryTable(
						"EXPECTED",
						"SELECT abstract,p.idprotocol,p.version,filename,p.iduser,status,title,abstract,qmrf_number from protocol p where p.idprotocol>121 order by p.iduser");
		Assert.assertEquals(1, table.getRowCount());
		//Assert.assertEquals(new BigInteger("1"), table.getValue(0, "version"));
		//Assert.assertEquals(new BigInteger("3"), table.getValue(0, "iduser"));
		Assert.assertEquals(new BigInteger("88"), table.getValue(0, "iduser"));
		Assert.assertEquals(STATUS.RESEARCH.toString(), table.getValue(0, "status"));
		Assert.assertEquals("QSAR for acute toxicity to fish (Danio rerio)",table.getValue(0, "title"));
		Assert.assertNotNull(table.getValue(0, "abstract"));
		Assert.assertNotSame("Q-1234-5678", table.getValue(0, "qmrf_number"));
		c.close();
	}

	public String createEntryFromMultipartWeb(Reference uri) throws Exception {
		return createEntryFromMultipartWeb(uri, Method.POST);
	}

	public String createEntryFromMultipartWeb(Reference uri, Method method)
			throws Exception {
		URL url = getClass().getClassLoader().getResource(
				"net/idea/qmrf/QMRF-NEW.xml");
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
						Resources.organisation, "G1");
				break;
			}
			case user_uri: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.user, "U88");
				break;
			}
			case author_uri: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.user, "U88");
				break;
			}
			case allowReadByGroup: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.organisation, "G1");
				break;
			}
			case allowReadByUser: {
				values[i] = String.format("http://localhost:%d%s/%s", port,
						Resources.user, "U3");
				break;
			}
			case status: {
				values[i] = STATUS.RESEARCH.toString();
				break;
			}
			case anabstract: {
				values[i] = null;
				break;
			}
			case published_status: {
				values[i] = PublishedStatus.published.name();
				break;
			}
			case title: {
				values[i] = null;
				break;
			}
			case endpoint: {
				values[i] = "QMRF 1.4.";
				break;
			}
			case endpointName: {
				values[i] = "Vapour Pressure";
				break;
			}
			case endpointParentName: {
				values[i] = "Physical Chemical Properties";
				break;
			}			
			case endpointParentCode: {
				values[i] = "QMRF 1.";
				break;
			}					
			case identifier: {
				values[i] = "Q-1234-5678";
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
				Resources.user, "U3");
		names[i] = ReadProtocol.fields.author_uri.name();
		values[i + 1] = null;
		names[i + 1] = ReadProtocol.fields.author_uri.name();
		MultipartEntity rep = getMultipartWebFormRepresentation(names, values,
				file, MediaType.APPLICATION_PDF.toString());

		IDatabaseConnection c = getConnection();
		ITable table = c.createQueryTable("EXPECTED", "SELECT * FROM protocol");
		Assert.assertEquals(5, table.getRowCount());
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
						String.format("http://localhost:%d/protocol/", port)));

		return task.getResult().toString();

	}

	public void testDownloadFile() throws Exception {
		testGet(String.format("http://localhost:%d%s/%s%s", port,
				Resources.protocol, id2v1, Resources.document),
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
