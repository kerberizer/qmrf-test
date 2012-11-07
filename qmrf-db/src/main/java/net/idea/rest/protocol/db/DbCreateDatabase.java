/* DbCreateDatabase.java
 * Author: Nina Jeliazkova
 * Date: May 6, 2008 
 * Revision: 0.1 
 * 
 * Copyright (C) 2005-2008  Nina Jeliazkova
 * 
 * Contact: nina@acad.bg
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

package net.idea.rest.protocol.db;

import java.sql.ResultSet;
import java.sql.Statement;

import net.idea.restnet.db.CreateDatabaseProcessor;

/**
 * Creates tables in an existing database. If the database does not exist, or has tables, the call will fail.
 * Note the behaviour is changed from ambit-db!
 * @author nina
 *
 */
public class DbCreateDatabase extends CreateDatabaseProcessor {
	
	public final static String version = "2.7";
	
    /**
	 */
	private static final long serialVersionUID = -335737998721944578L;
	public static final String SQLFile = "net/idea/rest/protocol/db/sql/qmrf.sql";
	
	public DbCreateDatabase() {
		super();
	}
	
	protected String getVersion() {
		return version;
	}
	
	@Override
	public synchronized String getSQLFile() {
		return SQLFile;
	}

	
	
	public String getDbVersion(String dbname) throws Exception {
		String version = null;
		ResultSet rs = null;
		Statement st = null;
		
		try {
			st = connection.createStatement();
			rs = st.executeQuery(String.format("Use `%s`",dbname)); //just in case
		} catch (Exception x) {
			throw x;			
		} finally {
			try {if (rs != null) rs.close();} catch (Exception x) {}
			try {if (st != null) st.close();} catch (Exception x) {}
		}			
		try {
			st = connection.createStatement();
			rs = st.executeQuery("select concat(idmajor,'.',idminor) from version");
			while (rs.next()) {
				version = rs.getString(1);
			}
			
		} catch (Exception x) {
			throw x;
		} finally {
			try {if (rs != null) rs.close();} catch (Exception x) {}
			try {if (st != null) st.close();} catch (Exception x) {}
		}
		return version;
	}		


}
