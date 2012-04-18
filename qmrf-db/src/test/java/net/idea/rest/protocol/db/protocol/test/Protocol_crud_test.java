/* ReferenceCRUDTest.java
 * Author: nina
 * Date: Mar 28, 2009
 * Revision: 0.1 
 * 
 * Copyright (C) 2005-2009  Ideaconsult Ltd.
 * 
 * Contact: nina
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

package net.idea.rest.protocol.db.protocol.test;

import java.io.InputStream;

import junit.framework.Assert;
import net.idea.ambit.qmrf.QMRFObject;
import net.idea.ambit.qmrf.chapters.QMRFSubChapterText;
import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.qmrf.converters.QMRFConverter;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.DBProject;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.CreateProtocol;
import net.idea.rest.protocol.db.DeleteProtocol;
import net.idea.rest.protocol.db.PublishProtocol;
import net.idea.rest.protocol.db.UpdateProtocol;
import net.idea.rest.protocol.db.test.CRUDTest;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.author.db.AddAuthor;
import net.toxbank.client.resource.Protocol.STATUS;

import org.apache.commons.io.IOUtils;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.junit.Test;

public final class Protocol_crud_test<T extends Object>  extends CRUDTest<T,DBProtocol>  {
	
	
	@Override
	protected IQueryUpdate<T,DBProtocol> createQuery() throws Exception {
		DBProtocol ref = new DBProtocol(); //id2v1);
		ref.setID(2);
		ref.setVersion(1);
		InputStream in = getClass().getClassLoader().getResourceAsStream("net/idea/qmrf/QMRF-NEW.xml");
		//ref.setAbstract(IOUtils.toString(in, "UTF-8"));
		QMRFObject qmrf = new QMRFObject();
		qmrf.read(in);in.close();
		ref.setTitle(QMRFConverter.replaceTags(((QMRFSubChapterText)qmrf.getChapters().get(0).getSubchapters().getItem(0)).getText()));
		//ref.setIdentifier(QMRFConverter.replaceTags(((QMRFSubChapterText)qmrf.getChapters().get(9).getSubchapters().getItem(0)).getText()));
		String keywords = QMRFConverter.replaceTags(((QMRFSubChapterText)qmrf.getChapters().get(9).getSubchapters().getItem(2)).getText());
		String[] keyword = keywords.split(",");
		for (String key:keyword) ref.addKeyword(key);
		ref.setIdentifier("Q-123456-789");
		ref.setPublished(true);
		/*
		Catalog authors = qmrf.getCatalogs().get("authors_catalog");
		for (int i=0; i < authors.size(); i++)
			System.out.println(authors.getItem(i));
		*/
		/*
		DBUser user = new DBUser();
		user.setID(1);
		ref.setOwner(user);
		ref.setProject(new DBProject(1));	
		ref.setOrganisation(new DBOrganisation(1));
		ref.setSearchable(true);
		ref.setDocument(new Document(new URL(file)));
		ref.setStatus(STATUS.SOP);
		*/
		return (IQueryUpdate<T,DBProtocol>)new UpdateProtocol(ref);
	}

	@Override
	protected void createVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,version,published,title,qmrf_number FROM protocol where idprotocol=2 and version=1"));
		
		Assert.assertEquals(1,table.getRowCount());
		//we ignore the published flag here! There is a special PublishProtocol query
		Assert.assertEquals(Boolean.FALSE,table.getValue(0,"published"));
		Assert.assertEquals("QSAR for acute toxicity to fish (Danio rerio)",table.getValue(0,"title"));
		//we change the identifier only on publishing
		Assert.assertEquals("8f0aba27-862e-11e1-ba85-00ff3739b863",table.getValue(0,"qmrf_number"));
	
		c.close();	
	}

	@Override
	protected IQueryUpdate<T,DBProtocol> deleteQuery() throws Exception {
		DBProtocol ref = new DBProtocol(id2v1);
		return ( IQueryUpdate<T,DBProtocol>)new DeleteProtocol(ref);
	}

	@Override
	protected void deleteVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idprotocol FROM protocol where idprotocol=2 and version=1");
		Assert.assertEquals(0,table.getRowCount());
		c.close();
		
	}
	
	/**
	 * Adds authors to a protocol
	 */
	@Override
	protected IQueryUpdate<T,DBProtocol> updateQuery() throws Exception {
		DBProtocol ref = new DBProtocol(id2v1);
		//ref.addAuthor(new DBUser(3));
		//ref.addAuthor(new DBUser(10));

		IQueryUpdate<T,DBProtocol> q = (IQueryUpdate<T,DBProtocol>)new AddAuthor(new DBUser(10),ref);
		return q;
	}

	@Override
	protected void updateVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED","SELECT idprotocol,version,iduser FROM protocol_authors where idprotocol=2");
		Assert.assertEquals(1,table.getRowCount());
		c.close();
	}

	protected static String newQMRFNumber = DBProtocol.generateIdentifier(); 
	@Override
	protected IQueryUpdate<T, DBProtocol> createQueryNew()
			throws Exception {
		DBProtocol ref = new DBProtocol();
		//ref.setID(3);
		//ref.setVersion(1);
		DBUser user = new DBUser();
		user.setID(3);
		ref.setOwner(user);
		ref.setProject(new DBProject(1));	
		ref.setOrganisation(new DBOrganisation(1));
		ref.setSearchable(true);
		ref.setStatus(STATUS.SOP);
		ref.setPublished(false);
		ref.setIdentifier(newQMRFNumber);
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("net/idea/qmrf/QMRF-NEW.xml");
		ref.setAbstract(IOUtils.toString(in, "UTF-8"));
		ref.setTitle("title");

		return (IQueryUpdate<T, DBProtocol>)new CreateProtocol(ref);
	}

	@Override
	protected void createVerifyNew(IQueryUpdate<T, DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
				String.format("SELECT idprotocol,qmrf_number,summarySearchable,status FROM protocol where title='title' and abstract regexp '<QMRF' and iduser='3' and idproject=1 and idorganisation=1"));
		
		Assert.assertEquals(1,table.getRowCount());
		Assert.assertEquals(Boolean.TRUE,table.getValue(0,"summarySearchable"));
		Assert.assertEquals(STATUS.SOP.toString(),table.getValue(0,"status"));
		Assert.assertEquals(newQMRFNumber,table.getValue(0,"qmrf_number"));
		c.close();		
	}


	@Test
	public void testPublish() throws Exception {
		IQueryUpdate<T,DBProtocol> query = publishQuery();
		setUpDatabase(dbFile);
		IDatabaseConnection c = getConnection();
		executor.setConnection(c.getConnection());
		executor.open();
		Assert.assertTrue(executor.process(query)>=1);
		publishVerify(query);
		c.close();
	}
	
	protected IQueryUpdate<T,DBProtocol> publishQuery() throws Exception {
		DBProtocol ref = new DBProtocol(); //id2v1);
		ref.setID(2);
		ref.setVersion(1);
		return (IQueryUpdate<T,DBProtocol>)new PublishProtocol(ref);
	}

	protected void publishVerify(IQueryUpdate<T,DBProtocol> query)
			throws Exception {
        IDatabaseConnection c = getConnection();	
		ITable table = 	c.createQueryTable("EXPECTED",
					"SELECT idprotocol,version,qmrf_number,published FROM protocol where idprotocol=2 and version=1");
		Assert.assertEquals(Boolean.TRUE,table.getValue(0,"published"));
		Assert.assertEquals("Q12-295A-0001",table.getValue(0,"qmrf_number"));
		c.close();
	}
}
