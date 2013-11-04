package net.idea.qmrf.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.opentox.cli.task.RemoteTask;
import net.idea.qmrf.client.Resources;
import net.idea.restnet.user.db.ReadUser;
import net.toxbank.client.io.rdf.UserIO;
import net.toxbank.client.resource.User;

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

public class UserResourceTest extends ResourceTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setUpDatabase(dbFile);
		
	}
	@Override
	public String getTestURI() {
		return String.format("http://localhost:%d%s", port,Resources.user);
	}
	
	@Test
	public void testURI() throws Exception {
		testGet(getTestURI(),MediaType.TEXT_URI_LIST);
	}
	/**
	 * The URI should be /user/U*
	 */
	@Override
	public boolean verifyResponseURI(String uri, MediaType media, InputStream in)
			throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = null;
		int count = 0;
		while ((line = r.readLine())!= null) {
			Assert.assertTrue(line.startsWith(String.format("http://localhost:%d%s/U",port,Resources.user)));
			count++;
		}
		return count==5;
	}	
	
	
	@Test
	public void testRDF() throws Exception {
		testGet(String.format("http://localhost:%d%s/U3", port,Resources.user),MediaType.APPLICATION_RDF_XML);
	}
	
	@Override
	public OntModel verifyResponseRDFXML(String uri, MediaType media,
			InputStream in) throws Exception {
		
		OntModel model = ModelFactory.createOntologyModel();
		model.read(in,null);
		
		UserIO ioClass = new UserIO();
		List<User> users = ioClass.fromJena(model);
		Assert.assertEquals(1,users.size());
		Assert.assertEquals(String.format("http://localhost:%d%s/U3",port,Resources.user),
													users.get(0).getResourceURL().toString());
		Assert.assertEquals("", users.get(0).getTitle());
		
		Assert.assertEquals("http://qsardb.jrc.it", users.get(0).getHomepage().toString());
		Assert.assertEquals("abcdef", users.get(0).getFirstname());
		Assert.assertEquals("ABCDEF", users.get(0).getLastname());
		Assert.assertNotNull(users.get(0).getOrganisations());
		Assert.assertEquals(1,users.get(0).getOrganisations().size());
		Assert.assertEquals(1,users.get(0).getProjects().size());
		return model;
	}	
	
	@Override
	public Object verifyResponseJavaObject(String uri, MediaType media,
			Representation rep) throws Exception {
		Object o = super.verifyResponseJavaObject(uri, media, rep);
		Assert.assertTrue(o instanceof ReadUser);

		return o;
	}
	
	protected void cleanUsers() throws Exception {
        IDatabaseConnection c = getConnection();	
        Statement st = c.getConnection().createStatement();
        st.addBatch("USE aalocal_test;");
        st.addBatch("DELETE FROM aalocal_test.users;");
        st.addBatch("DELETE FROM aalocal_test.user_registration;");
        st.addBatch("DELETE FROM aalocal_test.user_roles;");
        st.executeBatch();
        st.close();
        c.close();
	}
	
	@Test
	public void testCreateUserFromName() throws Exception {
		cleanUsers();
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(ReadUser.fields.firstname.name(),  "Alice"));
		formparams.add(new BasicNameValuePair(ReadUser.fields.lastname.name(),  "B."));
		formparams.add(new BasicNameValuePair("pwd1",  "testtest"));
		formparams.add(new BasicNameValuePair("pwd2",  "testtest"));
		//formparams.add(new BasicNameValuePair("affiliation",  "test"));
		formparams.add(new BasicNameValuePair(ReadUser.fields.username.name(),  "test"));
		formparams.add(new BasicNameValuePair(ReadUser.fields.email.name(),  "noone@example.org"));
		formparams.add(new BasicNameValuePair("privacy",  "Accept"));
		
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM user");
		Assert.assertEquals(5,table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(new Reference(String.format("http://localhost:%d%s", port,
				Resources.user)),
				MediaType.TEXT_URI_LIST, new UrlEncodedFormEntity(formparams,"UTF-8"),
				Method.POST);
		//wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		Assert.assertTrue(task.getResult().toString().startsWith(String.format("http://localhost:%d/register/notify",port)));

        c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM user");
		Assert.assertEquals(6,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT iduser,title from user where iduser>5 and firstName='Alice' and lastName='B.'" );
		Assert.assertEquals(1,table.getRowCount());
		c.close();

	}		
	@Test
	public void testRegisterUser() throws Exception {
		testCreateEntryFromWebForm(Resources.register);
	}
	
	@Test
	public void testCreateUser() throws Exception {
		testCreateEntryFromWebForm(Resources.user);
	}
	

	public void testCreateEntryFromWebForm(String resource) throws Exception {
		cleanUsers();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(ReadUser.fields.firstname.name(),  "Alice"));
		formparams.add(new BasicNameValuePair(ReadUser.fields.lastname.name(),  "B."));
		formparams.add(new BasicNameValuePair("pwd1",  "testtest"));
		formparams.add(new BasicNameValuePair("pwd2",  "testtest"));
		formparams.add(new BasicNameValuePair("username",  "username"));
		formparams.add(new BasicNameValuePair(ReadUser.fields.email.name(),  "noone@example.org"));
		if (Resources.register.equals(resource)) {
			formparams.add(new BasicNameValuePair("privacy",  "Accept"));
		}
		for (ReadUser.fields field : ReadUser.fields.values()) {
			switch (field) {
			case iduser: continue;
			default: {
				formparams.add(new BasicNameValuePair(field.name(), field.name()));
			}
			}
		}
		formparams.add(new BasicNameValuePair("affiliation",  "My new affiliation"));
		formparams.add(new BasicNameValuePair("organisation_uri",  String.format("http://localhost:%d%s/G1",port,Resources.organisation)));
		//formparams.add(new BasicNameValuePair("organisation_uri",  String.format("http://localhost:%d%s/G2",port,Resources.organisation)));
		formparams.add(new BasicNameValuePair("project_uri",  String.format("http://localhost:%d%s/G2",port,Resources.project)));

        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM user");
		Assert.assertEquals(5,table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(new Reference(String.format("http://localhost:%d%s", port,
				resource)),
				MediaType.TEXT_URI_LIST, new UrlEncodedFormEntity(formparams, "UTF-8"),
				Method.POST);
		//wait to complete
		task.waitUntilCompleted(Integer.MAX_VALUE);

		Assert.assertTrue(task.getResult().toString().startsWith(String.format("http://localhost:%d/register/notify",port)));

        c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM user");
		Assert.assertEquals(6,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT iduser,title from user where iduser>88 and username='username'");
		Assert.assertEquals(1,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT iduser,idorganisation from user_organisation where iduser>88 and idorganisation=1");
		Assert.assertEquals(1,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT iduser,idorganisation,name from user_organisation join organisation using(idorganisation) where iduser>88 and name='My new affiliation'");
		Assert.assertEquals(1,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT iduser,idproject from user_project where iduser>88 and idproject=2");
		Assert.assertEquals(1,table.getRowCount());		
		c.close();

	}	
	

	@Test
	public void testDelete() throws Exception {
		IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT iduser FROM user where iduser=4");
		Assert.assertEquals(new BigInteger("4"),table.getValue(0,"iduser"));
		c.close();		
		String org = String.format("http://localhost:%d%s/U4", port,Resources.user);
		RemoteTask task = testAsyncPoll(new Reference(org),
				MediaType.TEXT_URI_LIST, null,
				Method.DELETE);
		Assert.assertEquals(Status.SUCCESS_OK.getCode(), task.getStatus());
		//Assert.assertNull(task.getResult());
		c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM user where iduser=4");
		Assert.assertEquals(0,table.getRowCount());
		c.close();			
	}
	
	@Test
	public void testUpdateUser() throws Exception {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(ReadUser.fields.firstname.name(),  "Alice"));
		formparams.add(new BasicNameValuePair(ReadUser.fields.lastname.name(),  "B."));
		formparams.add(new BasicNameValuePair(ReadUser.fields.email.name(),  "email@example.org"));
		
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM user where iduser=3");
		Assert.assertEquals(1,table.getRowCount());
		c.close();

		RemoteTask task = testAsyncPoll(new Reference(String.format("http://localhost:%d%s/U3", port,
				Resources.user)),
				MediaType.TEXT_URI_LIST, new UrlEncodedFormEntity(formparams,"UTF-8"),
				Method.PUT);
		//wait to complete
		while (!task.isDone()) {
			task.poll();
			Thread.sleep(100);
			Thread.yield();
		}
		System.out.println(task.getResult());
		Assert.assertTrue(task.getResult().toString().startsWith(String.format("http://localhost:%d/user/U3",port)));

        c = getConnection();	
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM user");
		Assert.assertEquals(5,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT iduser,title from user where iduser=3 and firstName='Alice' and lastName='B.'" );
		Assert.assertEquals(1,table.getRowCount());
		c.close();

	}		
}
