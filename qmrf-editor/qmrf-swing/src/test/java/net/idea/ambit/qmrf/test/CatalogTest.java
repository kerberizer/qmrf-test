/*
Copyright (C) 2005-2006  

Contact: nina@acad.bg

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

package net.idea.ambit.qmrf.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import junit.framework.Assert;
import net.idea.ambit.qmrf.catalogs.Catalog;
import net.idea.ambit.qmrf.catalogs.CatalogEntry;
import net.idea.ambit.qmrf.catalogs.Catalogs;

public class CatalogTest {
	@Test
	public void testReadURL() throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();

		// URL e_url = new
		// URL("http://qmrf.sourceforge.net/endpoints/endpoints-EC-OECD-QMRF.xml");
		URL e_url = new URL("http://qmrf.sourceforge.net/endpoints/qmrf_catalogs.xml");
		BufferedReader r = new BufferedReader(new InputStreamReader(e_url.openStream()));
		StringBuffer b = new StringBuffer();
		String l;
		while ((l = r.readLine()) != null) {
			b.append(l);
		} // end while
		r.close();

		// System.out.println(b.toString());
		Document doc = builder.parse(new InputSource(new StringReader(b.toString().trim())));
		Catalogs c = new Catalogs();
		c.fromXML(doc.getDocumentElement());
		Catalog catalog = c.get("endpoints_catalog");
		Assert.assertNotNull(catalog);
		for (int i = 0; i < catalog.size(); i++) {
			CatalogEntry e = catalog.getItem(i);
			String name = e.getProperty("name");
			if (name.indexOf("EC") == 0)
				System.out.println(catalog.getItem(i));
		}

	}

	@Test
	public void testExternalCatalogs() throws Exception {
		Catalog c = new Catalog(Catalog.catalog_names[3][0]);
		Catalog e = new Catalog(Catalog.catalog_names[3][0]);

		String src = "<endpoints_catalog>"
				+ "<endpoint id=\"endpoint1\" group=\"1.Physicochemical effects\" name=\"1.1.Melting point\" />"
				+ "<endpoint id=\"endpoint2\" group=\"1.Physicochemical effects\" name=\"1.2.Boiling point\" />"
				+ "<endpoint id=\"endpoint3\" group=\"1.Physicochemical effects\" name=\"1.3.Water solubility\" />"
				+ "<endpoint id=\"endpoint4\" group=\"1.Physicochemical effects\" name=\"1.4.Vapour pressure\" />"
				+ "</endpoints_catalog>";

		String src1 = "<endpoints_catalog>" +

				"<endpoint id=\"endpoint5\" group=\"1.Physicochemical effects\" name=\"1.5.Surface tension\" />"
				+ "<endpoint id=\"endpoint6\" group=\"1.Physicochemical effects\" name=\"1.6.1.Partition coefficients: Octanol-water (Kow)\" />"
				+ "<endpoint id=\"endpoint7\" group=\"1.Physicochemical effects\" name=\"1.6.2.Partition coefficients: Octanol-water (D)\" />"
				+ "<endpoint id=\"endpoint1\" group=\"1.Physicochemical effects\" name=\"1.1.Melting point\" />"
				+ "<endpoint id=\"endpoint2\" group=\"1.Physicochemical effects\" name=\"1.2.Boiling point\" />"
				+ "</endpoints_catalog>";
		c.from(src);
		Assert.assertEquals(4, c.size());
		e.from(src1);
		Assert.assertEquals(5, e.size());

		c.addCatalog(e);
		Assert.assertEquals(9, c.size());
		for (int i = 0; i < c.size(); i++) {
			// System.out.println(c.getItem(i));
			Assert.assertFalse(((CatalogEntry) c.getItem(i)).getProperty("name").equals(""));
		}

	}

}
