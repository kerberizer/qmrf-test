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

import java.io.InputStream;
import java.io.OutputStream;

import ambit2.base.exceptions.AmbitIOException;

/**
 * @deprecated
 * This is an interface to be used in data reading and writing
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-7
 * See also {@link ambit.io.MyIOUtilities} and {@link ambit.domain.AllData}
 */
public interface IReadWriteStream {
	public void setStreamName(String name);
	public boolean save(OutputStream out) throws AmbitIOException;
	public boolean load(InputStream in) throws AmbitIOException;	
	public void setType(String type);
	public String getType() ;
	
}
