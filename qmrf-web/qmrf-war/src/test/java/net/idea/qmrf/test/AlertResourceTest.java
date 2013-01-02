package net.idea.qmrf.test;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.restnet.user.alerts.db.DBAlert;
import net.idea.restnet.user.alerts.db.ReadAlert;
import net.toxbank.client.Resources;
import net.toxbank.client.io.rdf.AlertIO;
import net.toxbank.client.resource.Alert;
import net.toxbank.client.resource.Query;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
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

public class AlertResourceTest extends ResourceTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setUpDatabase(dbFile);
		
	}
	@Override
	public String getTestURI() {
		return String.format("http://localhost:%d%s/U3%s", port,Resources.user,Resources.alert);
	}
	
	@Test
	public void testURI() throws Exception {
		testGet(getTestURI(),MediaType.TEXT_URI_LIST);
	}
	/**
	 * The URI should be /protocol/P1/datatemplate
	 */
	@Override
	public boolean verifyResponseURI(String uri, MediaType media, InputStream in)
			throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int count = 0;
		while ((line = r.readLine())!= null) {
			Assert.assertTrue(line.startsWith(String.format("http://localhost:%d%s/U3%s/A",port,Resources.user,Resources.alert)));
			count++;
		}
		return count==2;
	}	
	
	
	public void testTXT() throws Exception {
		testGet(getTestURI(),MediaType.TEXT_PLAIN);
	}
	/**
	 * Reading the template and ensuring the same garbage we put in is being read
	 */
	@Override
	public boolean verifyResponseTXT(String uri, MediaType media, InputStream in)
			throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int count = 0;
		while ((line = r.readLine())!= null) {
			Assert.assertEquals("ABCDEFGH", line);
			count++;
		}
		return count==1;
	}
	
	@Test
	public void testRDF() throws Exception {
		testGet(String.format("http://localhost:%d%s/U3%s", port,Resources.user,Resources.alert),
												MediaType.APPLICATION_RDF_XML);
	}
	
	@Override
	public OntModel verifyResponseRDFXML(String uri, MediaType media,
			InputStream in) throws Exception {
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read(in,null);
		
		AlertIO ioClass = new AlertIO();
		List<Alert> alerts = ioClass.fromJena(model);
		Assert.assertEquals(2,alerts.size());
		Assert.assertTrue(
				alerts.get(0).getResourceURL().toString().startsWith(
				String.format("http://localhost:%d%s/U3%s/A",port,Resources.user,Resources.alert)
													));
		Assert.assertTrue("cell".equals(alerts.get(0).getTitle()) || "toxicity".equals(alerts.get(0).getTitle()) ) ;
		Assert.assertTrue("cell".equals(alerts.get(0).getQueryString()) || "toxicity".equals(alerts.get(0).getQueryString()) ) ;

		Assert.assertTrue(Alert.RecurrenceFrequency.daily.equals(alerts.get(0).getRecurrenceFrequency())
					|| Alert.RecurrenceFrequency.weekly.equals(alerts.get(0).getRecurrenceFrequency()));
		Assert.assertEquals(Query.QueryType.FREETEXT, alerts.get(0).getType());
		Assert.assertEquals(1, alerts.get(0).getRecurrenceInterval());
		Assert.assertNotNull(alerts.get(0).getUser());
		String user =  String.format("http://localhost:%d%s/U3",port,Resources.user);
		Assert.assertEquals(user, alerts.get(0).getUser().getResourceURL().toString());
		return model;
	}	
	
	@Override
	public Object verifyResponseJavaObject(String uri, MediaType media,
			Representation rep) throws Exception {
		Object o = super.verifyResponseJavaObject(uri, media, rep);
		Assert.assertTrue(o instanceof ReadAlert);

		return o;
	}
	
	@Test
	public void testCreateEntryFromWebForm() throws Exception {
		List<NameValuePair> form = new ArrayList<NameValuePair>();
		form.add(new BasicNameValuePair(DBAlert._fields.qformat.name(),Query.QueryType.FREETEXT.name()));
		form.add(new BasicNameValuePair(DBAlert._fields.query.name(),"carcinogenicity"));
		form.add(new BasicNameValuePair(DBAlert._fields.rfrequency.name(),Alert.RecurrenceFrequency.weekly.name()));
		
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM alert");
		Assert.assertEquals(2,table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(new Reference(String.format("http://localhost:%d%s/U3%s", port,
				Resources.user,Resources.alert)),
				MediaType.TEXT_URI_LIST, new UrlEncodedFormEntity(form,"UTF-8"),
				Method.POST);
		//wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		Assert.assertTrue(task.getResult().toString().startsWith(
				String.format("http://localhost:%d%s/U3%s/A",port,Resources.user,Resources.alert)));

        c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT idquery,name,query,qformat,rfrequency,rinterval,iduser,created FROM alert");
		Assert.assertEquals(3,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT idquery,iduser,query from alert where idquery>2");
		Assert.assertEquals(1,table.getRowCount());

		String expectedURI = String.format("http://localhost:%d%s/U3%s/A%s",port,Resources.user,Resources.alert,table.getValue(0,"idquery"));
		Assert.assertEquals(expectedURI,task.getResult().toString());
		c.close();

	}	
	
	@Test
	public void testDelete() throws Exception {
		String org = String.format("http://localhost:%d%s/U3%s/A1", port,Resources.user,Resources.alert);
		RemoteTask task = testAsyncPoll(new Reference(org),
				MediaType.TEXT_URI_LIST, null,
				Method.DELETE);
		Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
		//Assert.assertNull(task.getResult());
		IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM alert where idquery=1");
		Assert.assertEquals(0,table.getRowCount());
		c.close();			
	}
	
	
}
