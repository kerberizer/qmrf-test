package net.idea.rest.protocol.db.attachments.test;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.DBAttachment.attachment_type;
import net.idea.rest.protocol.attachments.db.AddAttachment;
import net.idea.rest.protocol.attachments.db.DeleteAttachment;
import net.idea.rest.protocol.attachments.db.UpdateAttachment;
import net.idea.rest.protocol.db.test.CRUDTest;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;

/**
 * @author nina
 *
 */
public class DBAttachments_crud_test  extends CRUDTest<DBProtocol,DBAttachment> {



	@Override
	protected IQueryUpdate<DBProtocol,DBAttachment> deleteQuery()
			throws Exception {
		return new DeleteAttachment(new DBAttachment(108),new DBProtocol(83,1,2009));
	}

	@Override
	protected void createVerify(IQueryUpdate<DBProtocol,DBAttachment> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idattachment,idprotocol,version,name,description,type,format,original_name,imported FROM attachments where idprotocol=2 and version=1");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("test",table.getValue(0,"name"));
		Assert.assertEquals("Description",table.getValue(0,"description"));
		Assert.assertEquals("",table.getValue(0,"format"));
		Assert.assertEquals(attachment_type.document.name(),table.getValue(0,"type"));
		//URL url = getClass().getClassLoader().getResource("net/idea/qmrf/Training.sdf");
		//Assert.assertEquals(url.getFile(),table.getValue(0,"original_name"));
		c.close();
		
	}

	@Override
	protected IQueryUpdate<DBProtocol, DBAttachment> createQuery()
			throws Exception {
		DBProtocol protocol = new DBProtocol(2,1,2009);
		URL url = getClass().getClassLoader().getResource("net/idea/qmrf/test");
		DBAttachment attachment = DBAttachment.file2attachment(new File(url.getFile()), "Description", url.getFile(), attachment_type.document);
		return new AddAttachment(protocol, attachment);
	}

	@Override
	protected IQueryUpdate<DBProtocol, DBAttachment> createQueryNew()
			throws Exception {
		DBProtocol protocol = new DBProtocol(2,1,2009);
		URL url = getClass().getClassLoader().getResource("net/idea/qmrf/Training.sdf");
		DBAttachment attachment = DBAttachment.file2attachment(new File(url.getFile()), "Description", url.getFile(), attachment_type.data_training);
		return new AddAttachment(protocol, attachment);
	}

	@Override
	protected IQueryUpdate<DBProtocol, DBAttachment> updateQuery()
			throws Exception {
		return new UpdateAttachment(null,new DBAttachment(180));
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<DBProtocol, DBAttachment> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idattachment,idprotocol,version,name,description,type,format,original_name,imported FROM attachments where idprotocol=2 and version=1");
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals("Training",table.getValue(0,"name"));
		Assert.assertEquals("Description",table.getValue(0,"description"));
		Assert.assertEquals("sdf",table.getValue(0,"format"));
		Assert.assertEquals(attachment_type.data_training.name(),table.getValue(0,"type"));
		//URL url = getClass().getClassLoader().getResource("net/idea/qmrf/Training.sdf");
		//Assert.assertEquals(url.getFile(),table.getValue(0,"original_name"));
		c.close();
	}

	@Override
	protected void updateVerify(IQueryUpdate<DBProtocol, DBAttachment> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idattachment,imported from attachments where idattachment=180");
		Assert.assertEquals(1,table.getRowCount());
		c.close();		
	}

	@Override
	protected void deleteVerify(IQueryUpdate<DBProtocol, DBAttachment> query)
			throws Exception {
	
		IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT * FROM attachments where idprotocol=83 and version=1 and idattachment=108");
		Assert.assertEquals(0,table.getRowCount());
		table = 	c.createQueryTable("EXPECTED","SELECT * FROM attachments where idprotocol=83 and version=1");
		Assert.assertEquals(3,table.getRowCount());		
	}

	
}
