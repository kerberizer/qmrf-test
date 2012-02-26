package net.idea.rest.structure.resource;

import java.io.Writer;
import java.util.Iterator;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
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
	
	public StructureHTMLReporter(Request request, ResourceDoc doc) {
		this(request, doc, new StructureHTMLBeauty());

	}
	
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
		return false;
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
			"<a href='http://localhost:8081/qmrf/protocol/SEURAT-Protocol-53-1'>QMRF-1-2-3</a><br>"+
			"<a href='http://localhost:8081/qmrf/protocol/SEURAT-Protocol-121-1'>QMRF-4-5-6</a><br>"+
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

		return new StructureHTMLBeauty();
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
		output.write(String.format("<th width='50%%'>%s</th>","Chemical compound"));
		output.write(String.format("<th width='25%%'>%s</th>","Structure diagram"));
		output.write("</tr>\n");
		
	}
}

class StructureHTMLBeauty extends QMRF_HTMLBeauty {
	
	public StructureHTMLBeauty() {
		super();
	}
	@Override
	public String getSearchURI() {
		return Resources.structure;
	}
	@Override
	public String getSearchTitle() {
		return "Structure search";
	}
	
	@Override
	protected String searchMenu(Reference baseReference, String searchQuery,
			String pageSize) {

			return
		   String.format(		
		   "<div class='search'>\n"+
		   "%s\n"+
		   "<form method='GET' name='form' action='%s%s'>\n"+
		   "\n"+
		   "<input type='hidden' name='pagesize' value='%s'>\n"+
		   "<input type='hidden' name='type' value=''>\n"+
		   "<table width='100%%'>\n"+
		   "<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'></td></tr>\n"+
		   "<tr><td colspan='2'><input type='button' value='Draw substructure' title='Launches structure diagram editor' onClick='startEditor(\"%s\");'></td></tr>\n"+
		   "<tr><td colspan='2'><input type='radio' name='option' checked size='20'>Auto</td></tr>\n"+
		   "<tr><td colspan='2'><input type='radio' name='option' size='20'>Structure</td></tr>\n"+
		   "<tr><td><input  type='radio' name='option' value='on'>Similarity</td>\n"+
		   "<td align='right'>\n"+
		   "<select title ='Tanimoto similarity threshold'><option value='0.9'>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option></select>\n"+
		   "</td></tr>\n"+
		   "<tr><td colspan='2' align='right'><input type='submit' value='Search'/></td></tr>\n"+
		   "</table>\n"+
		   "</form> \n"+
		   "&nbsp;\n"+
		   "</div>\n",	
		   getSearchTitle(),
		   baseReference,
		   getSearchURI(),
		   pageSize,
		   searchQuery,
		   baseReference
		   );
	}
}