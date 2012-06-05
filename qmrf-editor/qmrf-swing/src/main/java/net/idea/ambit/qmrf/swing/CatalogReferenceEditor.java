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


package net.idea.ambit.qmrf.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import net.idea.ambit.qmrf.catalogs.Catalog;
import net.idea.ambit.qmrf.catalogs.CatalogEntry;
import net.idea.ambit.qmrf.catalogs.CatalogReference;
import net.idea.ambit.swing.actions.AbstractActionWithTooltip;
import net.idea.ambit.swing.common.AmbitListOneItemEditor;
import net.idea.ambit.swing.common.UITools;
import ambit2.base.interfaces.IAmbitEditor;

public class CatalogReferenceEditor extends CatalogEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1792610661338754378L;
	/*
	public CatalogReferenceEditor(CatalogReference list, boolean editable) {
		super(list,editable);
		setPreferredSize(new Dimension(200,300));
	}
	*/
	public CatalogReferenceEditor(CatalogReference list,
			boolean searchPanel) {
		super(list,false,new Dimension(200,58));
		setPreferredSize(new Dimension(200,250));
	}
	@Override
	public JToolBar createJToolbar() {
		JToolBar toolbar = super.createJToolbar();
		Catalog external = ((CatalogReference) list).getExternal_catalog();
		if ((external == null) || (external.size()==0)) return toolbar; 
		toolbar.setFocusable(false);
		JButton b = new JButton(new AbstractActionWithTooltip("Lookup",UITools.createImageIcon("ambit/ui/images/search.png"),
				"Find item in the " + list.toString() +" catalog and add it here") {
			public void actionPerformed(ActionEvent e) {
                Catalog catalog = ((CatalogReference) list).getCatalog();
                Catalog external_catalog = ((CatalogReference) list).getExternal_catalog();                
                if (catalog != null) {
                    CatalogEntry c =selectEntries(external_catalog);
                    if (c != null) {
                		c.setproperty("id", "");
                		//((CatalogReference) list).addReference(c);
                		((CatalogReference) list).addItem(c);
                    }
                    selectItem(((CatalogReference) list).size()-1,true);
                };   
				
			}
			
		});
		b.setFocusable(false);
		toolbar.add(b);
		return toolbar;
	}
    protected CatalogEntry selectEntries(Catalog catalog) {
    	IAmbitEditor e = catalog.editor(false);
        if (JOptionPane.showConfirmDialog(this,e.getJComponent(),catalog.toString(),JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)
            == JOptionPane.OK_OPTION ) {
        	int index = ((AmbitListOneItemEditor)e).getCurrentIndex();
        	return (CatalogEntry)catalog.getItem(index);
        } else return null;
    }
 
}



