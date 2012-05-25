package net.idea.rest.protocol.db.protocol.test;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.CreateProtocolVersion;
import net.idea.rest.protocol.db.test.CRUDTest;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;

public class ProtocolVersion_crud_test<T extends Object>  extends CRUDTest<T,DBProtocol>  {
	

	@Override
	protected IQueryUpdate<T,DBProtocol> createQuery() throws Exception {
		DBProtocol ref = new DBProtocol(id2v1);
		ref.setAbstract("abstrakt");
		ref.setTitle("title");
		return (IQueryUpdate<T,DBProtocol>)new CreateProtocolVersion(DBProtocol.generateIdentifier(),ref);
	}

	@Override
	protected void createVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,version,published_status,title FROM protocol where idprotocol=2"));
		
		Assert.assertEquals(3,table.getRowCount());
		c.close();	
	}

	@Override
	public void testDelete() throws Exception {
	
	}

	@Override
	public void testCreateNew() throws Exception {
	
	}
	@Override
	public void testUpdate() throws Exception {
	
	}

	@Override
	protected IQueryUpdate<T, DBProtocol> createQueryNew() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IQueryUpdate<T, DBProtocol> updateQuery() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IQueryUpdate<T, DBProtocol> deleteQuery() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateVerify(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteVerify(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}