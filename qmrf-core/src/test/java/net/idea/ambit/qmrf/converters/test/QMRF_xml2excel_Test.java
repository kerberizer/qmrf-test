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

package net.idea.ambit.qmrf.converters.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.Assert;
import net.idea.qmrf.converters.QMRF_xml2excel;

import org.junit.Test;
import org.xml.sax.InputSource;

public class QMRF_xml2excel_Test {

    @Test
    public void testXLS() throws  Exception {
	         QMRF_xml2excel qpdf = new QMRF_xml2excel();
	     	InputStream in = getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf.xml"); 

	         File xls = new File("qmrf_1_2.xls");
	         xls.deleteOnExit();
	         if (xls.exists()) xls.delete();
	         qpdf.xml2excel(new InputSource(in),new FileOutputStream(xls));	      
             Assert.assertTrue(new File("qmrf_1_2.xls").exists());
    } 
}
