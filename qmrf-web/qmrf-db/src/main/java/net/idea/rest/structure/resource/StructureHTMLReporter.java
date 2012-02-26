package net.idea.rest.structure.resource;

import java.io.Writer;
import java.util.Iterator;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.structure.resource.StructureResource.SearchMode;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.CatalogHTMLReporter;
import net.idea.restnet.db.QueryResource;

import org.restlet.Request;
import org.restlet.data.Form;
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
				w.write(((QMRF_HTMLBeauty)getHtmlBeauty()).getPaging(0,2));
				
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
		StringBuilder protocols = new StringBuilder();
		try {/*
			for (Protocol protocol:item.getProtocols()) {
				protocols.append(String.format("<tr><td><a href='%s'>%s</a></td><td>%s</td></tr>",
						protocol.getResourceURL(),protocol.getIdentifier(),protocol.getTitle()));

			}
			*/
			protocols.append(String.format("<tr><td><a href='%s%s?structure=%s' target='_QMRF'>QMRFs</a></td></tr>",getRequest().getRootRef(),Resources.protocol,Reference.encode(item.getResourceURL().toString())));
		} catch (Exception x) {}
		try {
			output.write(String.format(
			"<div class='protocol'>\n"+					
			"<div class='structureright'>\n"+
			"<img src='%s?media=%s&w=150&h=150' alt='' width='150' height='150'><br>\n"+
			"%s\n"+
			"</div>\n"+
			"<p>\n"+
			"<label>CAS</label>&nbsp;%s<br>"+
			"<label>Name</label>&nbsp;%s<br>"+
			"<label>SMILES</label>&nbsp;%s<br>"+
			"<label>InChI</label>&nbsp;%s<br>"+
			"<label>InChI Key</label>&nbsp;%s<br>"+
			"<label></label>&nbsp;%s<br>"+
			"<table width='80%%'>%s</table>\n"+
			"</p></div>",
			item.getResourceURL(),
			Reference.encode("image/png"),
			item.cas==null?"":item.cas,
			item.cas==null?"":item.cas,
			item.name==null?"":item.name,
			item.SMILES==null?"":item.SMILES,
			item.InChI==null?"":item.InChI,
			item.InChIKey==null?"":item.InChIKey,
			item.getSimilarity()==null?"":item.getSimilarity(),
			protocols
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
	protected String searchMenu(Reference baseReference, Form form) {
		String searchQuery = form.getFirstValue(QueryResource.search_param);
		
		pageSize = 10;
		try { pageSize = Long.parseLong(form.getFirstValue("pagesize")); if (pageSize>100) pageSize=10;} catch (Exception x) { pageSize=10;}
		page = 0;
		try { page = Integer.parseInt(form.getFirstValue("page")); if ((page<0) || (page>100)) page=0;} catch (Exception x) { page=0;}
		String threshold = form.getFirstValue("threshold");
		SearchMode option = SearchMode.auto;
		try {
			option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
		} catch (Exception x) {
			option = SearchMode.auto;
		}
		String imgURI = searchQuery==null?"":
				String.format("<img title='Search query' border='1' width='150' height='150' src='%s/depict/cdk?search=%s&media=%s&w=150&h=150'>",
						 StructureResource.queryService,Reference.encode(searchQuery),Reference.encode("image/png"));
			return
		   String.format(		
		   "<div class='search'>\n"+
		   "%s\n"+
		   "<form method='GET' name='form' action='%s%s'>\n"+
		   "\n"+
		   "<input type='hidden' name='page' value='%s'>\n"+
		   "<input type='hidden' name='type' value='smiles'>\n"+
		   "<table width='200px'>\n"+
		   "<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.'></td></tr>\n"+
		   "<tr><td colspan='2'><input type='button' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor(\"%s\");'></td></tr>\n"+
		   "<tr><td colspan='2'><input %s type='radio' value='auto' name='option' title='Exact structure or search by identifier' size='20'>Auto</td></tr>\n"+
		   "<tr><td><input %s type='radio' name='option' value='similarity' title='Enter SMILES or draw structure'>Similarity</td>\n"+
		   "<td align='left'>\n"+
		   "<select title ='Tanimoto similarity threshold' name='threshold'><option value='0.9' checked>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5'>0.5</option></select>\n"+
		   "</td></tr>\n"+
		   "<tr><td colspan='2'><input %s type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure</td></tr>\n"+
		   "<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n"+
		   "<tr><td colspan='2' align='right'><input type='submit' value='Search'/></td></tr>\n"+
		   "</table>\n"+
		   "</form> \n"+
		   "&nbsp;\n"+
		   "<div class='structureright'>%s</div>"+
		   "</div>\n",	
		   getSearchTitle(),
		   baseReference,
		   getSearchURI(),
		   page,
		   searchQuery==null?"":searchQuery,
		   baseReference,
		   SearchMode.auto.equals(option)?"checked":"",
		   SearchMode.similarity.equals(option)?"checked":"",
		   SearchMode.smarts.equals(option)?"checked":"",
		   pageSize,
		   imgURI
		   );
	}
}