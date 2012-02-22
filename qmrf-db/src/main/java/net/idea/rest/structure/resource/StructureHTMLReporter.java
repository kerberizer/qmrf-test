package net.idea.rest.structure.resource;

import java.io.Writer;
import java.util.Iterator;

import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.user.DBUser;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.CatalogHTMLReporter;

import org.restlet.Request;
import org.restlet.data.Reference;

public class StructureHTMLReporter extends CatalogHTMLReporter<Structure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -244654733174669345L;
	protected long record = 0;
	
	public StructureHTMLReporter(Request request, ResourceDoc doc,
			HTMLBeauty htmlbeauty) {
		super(request, doc, htmlbeauty);
		setTitle("Structure");
	}
	protected String title;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}	
	protected boolean printAsTable() {
		return true;
	}
	@Override
	public void header(Writer w, Iterator<Structure> query) {

		super.header(w, query);
		
		record = 0;//query.getPage()*query.getPageSize();
		
		Reference uri = getRequest().getResourceRef().clone();
		uri.setQuery(null);
		
	    
		try {
			if (printAsTable()) {
				w.write(String.format("<h3>%ss</h3>",getTitle()));
				//w.write(QMRF_HTMLBeauty.getPaging(query.getPage(),0,2,query.getPageSize()));
				
			} else {
				w.write(String.format("<h3>%s</h3>",getTitle()));
			}
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				w.write("<div class='protocol'>\n");
				if (printAsTable()) {
					printTableHeader(w);
				}
			} catch (Exception x) {}
		}
		
	}		
	@Override
	public void processItem(Structure item, Writer output) {

		try {
			output.write(String.format(
			"<div class='protocol'>\n"+					
			"<div class='structureright'>\n"+
			"<img src='%s?media=image/png&w=150&h=150' alt='' width='150' height='150'><br>\n"+
			"%s\n"+
			"</div>\n"+
			"<p>\n"+
			"<label>CAS</label>&nbsp;%s<br>"+
			"<label>Name</label>&nbsp;%s<br>"+
			"<label>SMILES</label>&nbsp;%s<br>"+
			"<label>InChI</label>&nbsp;%s<br>"+
			"<label>InChI Key</label>&nbsp;%s<br>"+
			"</p></div>",
			item.uri,
			item.cas,
			item.cas,
			item.name,
			item.SMILES,
			item.InChI,
			item.InChIKey
			)
			);
		} catch (Exception x) {
			x.printStackTrace();
		}
		

	}
	@Override
	public void close() throws Exception {
		super.close();
	}
	@Override
	public HTMLBeauty getHtmlBeauty() {

		return new QMRF_HTMLBeauty();
	}
	
	@Override
	public void footer(Writer output, Iterator<Structure> query) {
		try {
			if (printAsTable()) output.write("</table>\n");				
			output.write("</div>\n");
		} catch (Exception x) {}
		super.footer(output, query);
	}
	
	protected void printTableHeader(Writer output) throws Exception {
		output.write("<table width='90%'>\n");

		output.write("<tr bgcolor='FFFFFF' >\n");	
		output.write(String.format("<th width='50%'>%s</th>","Name"));
		output.write(String.format("<th width='25%'>%s</th>",DBUser.fields.homepage.toString()));
		output.write(String.format("<th width='25%'>%s</th>","Documents"));
		output.write("</tr>\n");
		
	}
}
