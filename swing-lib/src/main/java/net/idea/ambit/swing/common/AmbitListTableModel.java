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

package net.idea.ambit.swing.common;

import javax.swing.table.AbstractTableModel;

import net.idea.ambit.swing.interfaces.AmbitList;


/**
 * A table model for (@link ambit.data.AmbitList) 
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-7
 */
public class AmbitListTableModel extends AbstractTableModel {
	protected AmbitList list = null;
	protected String[] columns = {"#","Name"};
	
	/**
	 * 
	 * @param list
	 */
	public AmbitListTableModel(AmbitList list) {
		super();
		this.list = list;
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columns.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return list.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch (columnIndex) {
		case (0): {return Integer.toString(rowIndex+1);}
		case (1): {return Integer.toString(list.getRowID(rowIndex));}	
		case (2): {
		    Object o = list.getItem(rowIndex); 
			if (o != null) return o; else return "NA";}
		default: return "";
		}
		
	}

	public String getColumnName(int col) {
        return columns[col].toString();
    }


    public Class getColumnClass(int column) {
        return getValueAt(0,column).getClass();
      }	
}
