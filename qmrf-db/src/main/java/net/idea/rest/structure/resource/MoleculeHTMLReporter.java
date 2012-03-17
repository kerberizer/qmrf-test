package net.idea.rest.structure.resource;

import java.io.Writer;
import java.util.Enumeration;

import net.idea.rest.qmrf.admin.QMRFCatalogHTMLReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Reference;

public class MoleculeHTMLReporter extends QMRFCatalogHTMLReporter<Structure> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1154097135680514626L;

	public MoleculeHTMLReporter(Request request, ResourceDoc doc,
			HTMLBeauty htmlbeauty) {
		super(request, doc, htmlbeauty,null);
		
	}
	
	
	@Override
	protected String printPageNavigator() {
		if (singleItem) return "";
		return super.printPageNavigator();
	}
	protected boolean printAsTable() {
		return false;
	}
	
	@Override
	public void processItem(Structure item, Writer output) {
		try {
			if (headless) output.write(renderItem(item));
			else {
				output.write(htmlBeauty.printWidget(
						item.cas!=null?item.cas:
						item.name!=null?item.name:
						item.InChIKey!=null?item.InChIKey:"Molecule",
						renderItem(item)));

			}	
			record++;
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	public String renderItem(Structure item) {
		StringBuilder properties=null;
		try {
			Enumeration<String> keys = item.getProperties().keys();
			while (keys.hasMoreElements()) {
				if (properties==null) properties = new StringBuilder();
				String key = keys.nextElement();
				properties.append(String.format("<tr><th align='left' width='60%%'>%s</th><td align='center' width='40%%'>%s</td></tr>",key,item.getProperties().get(key)));

			}
		} catch (Exception x) {}
		//TODO smarts highlight
		String structure = 	String.format(
					"<div class='structureright'><img src='%s?media=%s&w=150&h=150' alt='%s' title='%s' width='150' height='150'><br>%s\n</div>\n",
					item.getResourceURL(),
					Reference.encode("image/png"),
					item.cas==null?"":item.cas,
					item.name==null?"":item.name,
					item.cas==null?"":item.cas
					);
		
		StringBuilder rendering = new StringBuilder();
		
			if (!headless) rendering.append("<div >");
			//identifiers
			rendering.append(String.format(
			"<div id='Molecule' style='min-height:200px'>"+
			"%s\n"+ //structure
			"<span class='summary'><table>\n"+ 
			"<tr><th>CAS</th><td>%s</td></tr>"+
			"<tr><th>Name</th><td>%s</td></tr>"+
			"<tr><th>SMILES</th><td>%s</td></tr>"+
			"<tr><th>InChI</th><td>%s</td></tr>"+
			"<tr><th>InChI Key</th><td>%s</td></tr>"+
			"<tr><th></th><td>%s</td></tr>"+
			"</table></span>"+
			"</div>",
			structure,
			item.cas==null?"":item.cas,
			item.name==null?"":item.name,
			item.SMILES==null?"":item.SMILES,
			item.InChI==null?"":item.InChI,
			item.InChIKey==null?"":item.InChIKey,
			item.getSimilarity()==null?"":item.getSimilarity()
			));
			
			if (properties!=null)
				rendering.append(String.format(
				"<div id='tabs-prop'>"+
				"%s<span class='summary'><table>%s</table></span>\n"+
				"</div>",structure,properties));			
				
			if (!headless) rendering.append("</div>");
			return rendering.toString();
	}
}
