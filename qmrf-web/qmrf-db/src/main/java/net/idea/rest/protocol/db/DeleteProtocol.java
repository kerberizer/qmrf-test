/* DeleteReference.java
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

package net.idea.rest.protocol.db;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.IStoredProcStatement;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;
import net.idea.rest.protocol.DBProtocol;

/**
 * 
 * @author nina
 *
 */
public class DeleteProtocol extends AbstractObjectUpdate<DBProtocol> implements IStoredProcStatement {

	protected String[] delete_sql = {"{CALL deleteProtocol(?)}"};
	public DeleteProtocol(DBProtocol ref) {
		super(ref);
	}
	public DeleteProtocol() {
		this(null);
	}		
	public List<QueryParam> getParameters(int index) throws AmbitException {
		if (getObject()==null || !getObject().isValidIdentifier()) throw new AmbitException("No protocol identifier!");
		List<QueryParam> params = new ArrayList<QueryParam>();
		params.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
		
		return params;
		
	}

	public String[] getSQL() throws AmbitException {
		return delete_sql;
	}
	public void setID(int index, int id) {
			
	}
	
	@Override
	public boolean isStoredProcedure() {
		return true;
	}
	@Override
	public void getStoredProcedureOutVars(CallableStatement statement)
			throws SQLException {
		
		
	}
}
