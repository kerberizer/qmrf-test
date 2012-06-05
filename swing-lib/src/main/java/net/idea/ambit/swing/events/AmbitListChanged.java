/*
Copyright (C) 2005-2012  

Contact: www.ideaconsult.net

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

package net.idea.ambit.swing.events;

import net.idea.ambit.swing.interfaces.AmbitList;
import net.idea.ambit.swing.interfaces.AmbitObject;


/**
 * AmbitListChanged a descendant on {@link ambit.data.AmbitObjectChanged}
 * stores informaton of the List which had fired the event. <br>
 * Use getList() to obtain the studyList
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-4-7
 */
public class AmbitListChanged<T extends AmbitObject> extends AmbitObjectChanged<T>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2238157904990620982L;
	protected AmbitList<T> aL = null;
	/**
	 * @param source
	 */
	public AmbitListChanged(Object source, AmbitList<T> al, T object) {
		super(source,object);
		this.aL = al;
		this.ao = object;
	}
	
	public AmbitList<T> getList() {
		return aL;
	}
}
