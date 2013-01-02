package net.idea.rest.protocol.db.group.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.rest.protocol.db.test.CRUDTest;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.user.db.AddGroupsPerUser;
import net.idea.restnet.groups.user.db.DeleteGroupsPerUser;
import net.idea.restnet.user.DBUser;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;

public class Group_user_crud_test  extends CRUDTest<DBUser,List<IDBGroup>> {

	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> createQuery()
			throws Exception {
		return new AddGroupsPerUser(new DBUser(3),new DBProject(1));
	}

	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> createQueryNew()
			throws Exception {
		return new AddGroupsPerUser<IDBGroup>(new DBUser(3),new DBOrganisation(1));
	}

	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> updateQuery()
			throws Exception {
		return null;
	}

	@Override
	protected IQueryUpdate<DBUser, List<IDBGroup>> deleteQuery()
			throws Exception {
		DBUser ref = new DBUser(3);
		List<IDBGroup> p = new ArrayList<IDBGroup>();
		p.add(new DBProject(3));
		DeleteGroupsPerUser q = new DeleteGroupsPerUser();
		q.setGroup(ref);
		q.setObject(p);
		return q;
	}

	@Override
	protected void createVerify(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idproject from user_project where iduser=3 and idproject=1");
		
		Assert.assertEquals(1,table.getRowCount());
		c.close();
		
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idorganisation from user_organisation where iduser=3 and idorganisation=1");
		
		Assert.assertEquals(1,table.getRowCount());
		c.close();
		
	}

	@Override
	protected void updateVerify(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
		
	}

	@Override
	protected void deleteVerify(IQueryUpdate<DBUser, List<IDBGroup>> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				"SELECT idproject from user_project where iduser=1 and idproject=1");
		
		Assert.assertEquals(0,table.getRowCount());
		c.close();
	}

	@Override
	public void testUpdate() throws Exception {
	}
}
