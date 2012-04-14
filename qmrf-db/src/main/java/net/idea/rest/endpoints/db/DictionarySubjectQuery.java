/* DictionarySubjectQuery.java
 * Author: nina
 * Date: Feb 6, 2009
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

package net.idea.rest.endpoints.db;

import ambit2.base.data.Dictionary;


public class DictionarySubjectQuery<D extends Dictionary> extends DictionaryQuery<D> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4370465438653357188L;
	public DictionarySubjectQuery(D value) {
		super((value==null)?null:value.getParentTemplate());
	}	
	public DictionarySubjectQuery() {
		super(null);
	}	

	public DictionarySubjectQuery(String value) {
		super(value);
	}	

	@Override
	protected String getTemplateName() {
		return "tSubject";
	}

}
