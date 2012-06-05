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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Format;

import javax.swing.JFormattedTextField;

import net.idea.ambit.swing.interfaces.AmbitObject;



/**
 * Used in {@link ambit.ui.data.AmbitObjectPanel} 
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-7
 */
public class AmbitTextField extends JFormattedTextField {
	protected Method getMethod = null;
	protected Method setMethod = null;
	protected Class[] param = null;
	protected AmbitObject object = null;
	/**
	 * 
	 */
	public AmbitTextField() {
		super();
	}

	/**
	 * @param value
	 */
	public AmbitTextField(Object value) {
		super(value);
		
	}

	/**
	 * @param format
	 */
	public AmbitTextField(Format format) {
		super(format);
		
	}

	/**
	 * @param formatter
	 */
	public AmbitTextField(AbstractFormatter formatter) {
		super(formatter);
		
	}

	/**
	 * @param factory
	 */
	public AmbitTextField(AbstractFormatterFactory factory) {
		super(factory);
		
	}

	/**
	 * @param factory
	 * @param currentValue
	 */
	public AmbitTextField(AbstractFormatterFactory factory, Object currentValue) {
		super(factory, currentValue);
		
	}
	public void setMethods(AmbitObject object,
			Method getMethod, Method setMethod, Class[] param) {
		this.object = object;
		this.getMethod = getMethod;
		this.setMethod = setMethod;
		this.param = param;
	}
	public void updateValue(Object value) {
		try {
			if (param[0].getName().equals("int"))
				setMethod.invoke(object,new Object[] {new Integer(value.toString())});
			else
			if (param[0].getName().equals("double"))
				setMethod.invoke(object,new Object[] {new Double(value.toString())});			
			else
				setMethod.invoke(object,new Object[] {value});
//			String s = getMethod.invoke(object,null).toString();
		} catch (IllegalAccessException x) { x.printStackTrace(); 
		} catch (InvocationTargetException x) { x.printStackTrace();  
		} catch (NumberFormatException x ) {
			
		}
		
	}
	public Object getAmbitValue() {
		try {
			return getMethod.invoke(object,null);
		} catch (IllegalAccessException x) { x.printStackTrace(); 
			return null;
		} catch (InvocationTargetException x) { x.printStackTrace();
			return null;
		}		
	}
	public AmbitObject getObject() {
		return object;
	}
}
