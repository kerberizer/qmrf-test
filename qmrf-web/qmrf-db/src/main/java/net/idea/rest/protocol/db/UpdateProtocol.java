/* UpdateReference.java
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

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.modbcum.q.update.AbstractObjectUpdate;
import net.idea.rest.protocol.DBProtocol;

/**
 * Update is one of the few queries, which still require idprotocol and version, instead of identifier.
 * The reason is we want to be able to change the identifier.
 * @author nina
 *
 */

public class UpdateProtocol extends AbstractObjectUpdate<DBProtocol>{
	private ReadProtocol.fields[] f = new ReadProtocol.fields[] {
			ReadProtocol.fields.title,
			ReadProtocol.fields.anabstract,
			ReadProtocol.fields.summarySearchable,
			ReadProtocol.fields.filename,
			ReadProtocol.fields.idproject,
			ReadProtocol.fields.idorganisation,
			ReadProtocol.fields.iduser,
			ReadProtocol.fields.status
	};
	public static final String update_sql = "update protocol set updated=now(),%s where idprotocol=? and version=? and published=false";


	public UpdateProtocol(DBProtocol ref) {
		super(ref);
	}
	public UpdateProtocol() {
		this(null);
	}			
	public List<QueryParam> getParameters(int index) throws AmbitException {
		List<QueryParam> params1 = new ArrayList<QueryParam>();
		if (getObject()==null) throw new AmbitException("Empty protocol");
		if (getObject().getID()<=0) throw new AmbitException("Invalid document ID");
		if (getObject().getVersion()<=0) throw new AmbitException("Invalid document version");
		for (ReadProtocol.fields field: f) 
			if (field.getValue(getObject())!=null)
				switch (field) {
				case identifier: {
					 if (getObject().isValidIdentifier()) {
						 params1.add(new QueryParam<String>(String.class, getObject().getIdentifier()));
					 } else 
						 throw new AmbitException(String.format("Invalid QMRF number %s",getObject().getIdentifier()));
					break;
				}
				default: {
					params1.add(field.getParam(getObject()));
				}
				}
		
		if (params1.size()==0) throw new AmbitException("Nothing to update!");
		params1.add(ReadProtocol.fields.idprotocol.getParam(getObject()));
		params1.add(ReadProtocol.fields.version.getParam(getObject()));
		return params1;
		
	}
	public String[] getSQL() throws AmbitException {
		StringBuilder b = new StringBuilder();
		String d = " ";
		for (ReadProtocol.fields field: f) 
			if (field.getValue(getObject())!=null) {
				switch (field) {
				case anabstract: {
					b.append(String.format("%s%s=?",d,"abstract"));
					break;
				}
				case identifier: {
					b.append(String.format("%s%s=?",d,"qmrf_number"));
					break;
				}
				default: {	
					b.append(String.format("%s%s=?",d,field.name()));
				}
				}
				d = ",";
			}
		String sql = String.format(update_sql,b.toString());
		return  new String[] { sql};
	}
	public void setID(int index, int id) {
			
	}
}
