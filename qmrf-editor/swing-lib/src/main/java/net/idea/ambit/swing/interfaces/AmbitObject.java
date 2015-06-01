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


package net.idea.ambit.swing.interfaces;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.event.EventListenerList;

import net.idea.ambit.swing.common.AmbitObjectPanel;
import net.idea.ambit.swing.events.AmbitObjectChanged;
import net.idea.modbcum.i.exceptions.AmbitException;
import ambit2.base.exceptions.AmbitIOException;
import ambit2.base.interfaces.IAmbitEditor;

/**
 *
 * A parent object to all AMBIT basic data objects 
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-7
 */
public class AmbitObject implements  AmbitInterface,IReadWriteStream {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -7304304111875206489L;
	protected transient EventListenerList aoListeners = new EventListenerList();
	protected transient AmbitObjectChanged changeEvent = null;
	
	protected boolean editable = true;
	protected int id = -1;
	protected String name = "";
	protected boolean modified = false;
	protected boolean selected = false;
	/**
	 * 
	 */
	public AmbitObject() {
		this("");
	}
	public AmbitObject(String name) {
		this(name,-1);
	}
	public AmbitObject(String name, int id) {
		super();
		this.name = name;
		this.id = id;
		aoListeners = new EventListenerList();
	}	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if (equals(o)) return 0; else return 1;
	}
	public Object clone()  throws CloneNotSupportedException {
	    try {
			Object obj = createObject(getClass().getName());
			((AmbitObject) obj).id = id;
			((AmbitObject) obj).name = name;
			return obj;
	    } catch (AmbitException x) {
	        throw new CloneNotSupportedException(x.getCause().getMessage());
	    }
	}
	public String toString() {
		return name;
	}
	public boolean equals(Object obj) {
		if (obj == null) return false;
		else if (obj instanceof AmbitObject) return name.equalsIgnoreCase(((AmbitObject) obj).name);
		else return false;
	}
	
	public static Object createObject(String className) throws AmbitException {
	      Object object = null;
	      try {
	          Class classDefinition = Class.forName(className);
	          object = classDefinition.newInstance();
	      } catch (InstantiationException e) {
	          throw new AmbitException(e);
	      } catch (IllegalAccessException e) {
	          throw new AmbitException(e);
	      } catch (ClassNotFoundException e) {
	          throw new AmbitException(e);
	      }
	      return object;
	   }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		modified =  (this.id != id);
		this.id = id;
		if (id > -1) editable = false;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		boolean m =  !this.name.equals(name);
		this.name = name;
		setModified(m);
	}
	public boolean hasID() {
		return (id > -1);
	}
	public void clear() {
		id = -1;
		name="";
		setModified(true);
	}
	public boolean isPredefined() {
		return false;
	}
	public String[] predefinedvalues() {
		return null;
	}
	public void setStreamName(String name) {
		
	}
	public boolean save(OutputStream out) throws AmbitIOException {
		try {
			DataOutputStream ds = new DataOutputStream(out);
			ds.writeBytes(toString());
			return true;
		} catch ( IOException  x) {
			throw new AmbitIOException(this.getClass().getName(),x);
		}
	}
	public boolean load(InputStream in) throws AmbitIOException {
		return false;
	}
	
	public void setType(String type) {
	}
	public String getType() {
		return "";
	}
		
	/**
	 * @return Returns the editable.
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean m) {
		this.modified |= m;
		if (m) fireAmbitObjectEvent();
	}
	public void setNotModified() {
		this.modified = false;
	}
	public void fireAmbitObjectEvent() {
		fireAmbitObjectEvent(this);
	}
	 public void fireAmbitObjectEvent(AmbitObject object) {
	     // Guaranteed to return a non-null array
	     Object[] listeners = aoListeners.getListenerList();
	     // Process the listeners last to first, notifying
	     // those that are interested in this event
	     for (int i = listeners.length-1; i>=0; i-=1) {
	         if (listeners[i] instanceof IAmbitObjectListener) {
	             if (changeEvent == null)
	                 changeEvent = new AmbitObjectChanged(this,object);
	             ((IAmbitObjectListener)listeners[i]).ambitObjectChanged(changeEvent);
	         }
	     }
        changeEvent = null;
	 }
	 
	public synchronized void addAmbitObjectListener(IAmbitObjectListener listener) 
	{
	    	if (aoListeners == null) aoListeners = new EventListenerList();
		     aoListeners.add(IAmbitObjectListener.class, listener);
	}

	public void removeAmbitObjectListener(IAmbitObjectListener listener) 
	{
	    if (aoListeners == null) aoListeners = new EventListenerList();
			aoListeners.remove(IAmbitObjectListener.class, listener);
	}	
	
	public IAmbitEditor editor(boolean editable) {
		AmbitObjectPanel p =  new AmbitObjectPanel(getClass().getName(),this);
		p.setEditable(editable);
		return p;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
		setModified(true);
	}
	
	public static String toCamelCase(String value) {
		StringBuilder b = new StringBuilder();
		b.append(value.substring(0, 1).toUpperCase());
        b.append(value.substring(1).toLowerCase());
        return b.toString();
	}
}
