package net.idea.rest.structure.resource;

import java.io.Writer;
import java.util.Enumeration;

import net.idea.qmrf.client.Resources;
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
	protected String tabID = "Molecule";
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
						item.getCas()!=null && !"".equals(item.getCas())?item.getCas():
						item.getName()!=null && !"".equals(item.getName())?item.getName():
						item.getInChIKey()!=null && !"".equals(item.getInChIKey())?item.getInChIKey():"Molecule",
						renderItem(item)));

			}	
			record++;
		} catch (Exception x) {
			logger.warn(x);
		}
	}
	
	public String renderItem(Structure item) {
		StringBuilder properties=null;
		try {
			Enumeration<String> keys = item.getProperties().keys();
			while (keys.hasMoreElements()) {
				if (properties==null) properties = new StringBuilder();
				String key = keys.nextElement();
				properties.append(String.format("<tr><th>%s</th><td>%s</td></tr>",key,item.getProperties().get(key)));

			}
		} catch (Exception x) {}
		//TODO smarts highlight
		
		String cmpProxy = String.format("%s/proxy/img?uri=%s",
				baseReference,
				Reference.encode(String.format("%s?media=%s&w=150&h=150",item.getResourceIdentifier(),Reference.encode("image/png"))));
		
		String structure = 	String.format(
					"<div class='structureright'><a href='%s%s%s%s%s%s%s' >" +
					"<img src='%s' alt='%s' title='%s' width='150' height='150'></a><br>%s\n</div>\n",
					baseReference,
					Resources.chemical,
					item.getIdchemical()>0?"/":"",
					item.getIdchemical()>0?Integer.toString(item.getIdchemical()):"",
					item.getIdstructure()>0?Resources.structure:"",
					item.getIdstructure()>0?"/":"",
					item.getIdstructure()>0?Integer.toString(item.getIdstructure()):"",
					cmpProxy,
					item.getCas()==null?"":item.getCas(),
					item.getName()==null?"":item.getName(),
					item.getCas()==null?"":item.getCas()
					);
		
		StringBuilder rendering = new StringBuilder();
		
			if (!headless) rendering.append("<div >");
			//identifiers
			rendering.append(String.format(
			"<div id='%s' style='min-height:200px'>"+
			"%s\n"+ //structure
			"<span class='summary'><table>\n"+ 
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th>%s</th><td>%s</td></tr>"+
			"<tr><th></th><td>%s</td></tr>"+
			"<tr><th></th><td>%s</td></tr>"+
			"</table></span>"+
			"</div>",
			tabID,
			structure,
			(item.getCas()==null)||"".equals(item.getCas())?"":"CAS RN",
			item.getCas()==null?"":item.getCas(),
			(item.getName()==null)||"".equals(item.getName())?"":"Name",
			item.getName()==null?"":item.getName(),
			(item.getSMILES()==null)||"".equals(item.getSMILES())?"":"SMILES",
			item.getSMILES()==null?"":item.getSMILES(),
			(item.getInChI()==null)||"".equals(item.getInChI())?"":"InChI",
			item.getInChI()==null?"":item.getInChI(),
			(item.getInChIKey()==null)||"".equals(item.getInChIKey())?"":"InChI Key",
			item.getInChIKey()==null?"":item.getInChIKey(),
			properties==null?"":properties,
			item.getSimilarity()==null?"":item.getSimilarity()
			));
			/*
			if (properties!=null)
				rendering.append(String.format(
						"<div id='Properties' style='min-height:200px'>"+
						"%s\n"+ //structure
						"<span class='summary'><table>%s</table></span>\n"+
						"</div>",
						structure,
						properties
						));
			*/
			
		
				
			if (!headless) rendering.append("</div>");
			return rendering.toString();
	}
}
