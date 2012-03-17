package net.idea.rest.structure.resource;

import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.qmrf.admin.QMRFCatalogHTMLReporter;
import net.idea.rest.structure.resource.StructureResource.SearchMode;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;

public class StructureHTMLReporter extends QMRFCatalogHTMLReporter<Structure> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -244654733174669345L;
	
	public StructureHTMLReporter(Request request, ResourceDoc doc,
			HTMLBeauty htmlbeauty) {
		super(request, doc, htmlbeauty,null);
	}

	
	protected boolean printAsTable() {
		return false;
	}
	
	@Override
	public void processItem(Structure item, Writer output) {
		try {
			output.write(renderItem(item));
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
		

		String query = ((StructureHTMLBeauty)htmlBeauty).getSearchQuery();
		String smartsOption = ((StructureHTMLBeauty)htmlBeauty).getSmartsOption();
		//TODO smarts highlight
		String structure = 	String.format(
					"<div class='structureright'><img src='%s?media=%s&w=150&h=150' alt='%s' title='%s' width='150' height='150'><br>%s\n</div>\n",
					item.getResourceURL(),
					Reference.encode("image/png"),
					query,query,
					item.cas==null?"":item.cas
					);
		
		String protocolURI = String.format(
				"<a href=\"%s%s?structure=%s&headless=true&details=false&media=text/html\" title=\"QMRF documents\">QMRF documents</a>",
				getRequest().getRootRef(),Resources.protocol,Reference.encode(item.getResourceURL().toString()));
		
		String moleculeURI = String.format(
				"<a href=\"%s%s/%d?headless=true&details=false&media=text/html\" title=\"Molecule\">Molecule</a>",
				getRequest().getRootRef(),Resources.chemical,item.getIdchemical());
		
		StringBuilder rendering = new StringBuilder();
		rendering.append(String.format(
				"<div class='protocol'>\n"+					
				"<div class='tabs'>\n<ul>" +
				"<li>%s<span></span></li>\n" +
				"<li>%s<span></span></li>\n",
			    moleculeURI,protocolURI
				));
		
		
		if (htmlBeauty instanceof StructureHTMLBeauty) {
			DBAttachment attachment = ((StructureHTMLBeauty)htmlBeauty).getAttachment();
			if ((attachment!=null) && (attachment.getTitle()!=null)) {
				String datasetURI = String.format("%s%s/%d?dataset=%s&headless=true&details=false&media=text/html", getRequest().getRootRef(),Resources.chemical,item.getIdchemical(),attachment.getTitle());
				String uri = String.format(
						"<a href=\"%s\" title=\"%s\">%s&nbsp;%s</a>",
						datasetURI,attachment.getDescription(),attachment.getProtocol(),attachment.getType().toString());
				rendering.append(String.format("<li>%s<span></span></li>\n",uri));
			}
			String[] datasets = ((StructureHTMLBeauty)htmlBeauty).datasets;
			if (datasets !=null) {
				//datasetURI = new ArrayList<String>();
			
				for (String dataset: datasets) {
					String uri = String.format(
					"<a href=\"%s%s/%d?dataset=%s&headless=true&details=false&media=text/html\" alt='Other datasets' title=\"%s\">%s</a>",
					getRequest().getRootRef(),Resources.chemical,item.getIdchemical(),dataset,dataset,dataset);
					rendering.append(String.format("<li>%s<span></span></li>\n",uri));
					
				}
			}
				
		}
	
		

		
		rendering.append("</ul></div>\n</div>\n");//tabs , protocol

		return rendering.toString();		

	}
	

	@Override
	public void close() throws Exception {
		super.close();
	}
	@Override
	public HTMLBeauty getHtmlBeauty() {
		return htmlBeauty;
	}
	
	@Override
	public void footer(Writer output, Iterator<Structure> query) {
		try {
			if (printAsTable()) output.write("</table>\n");		

			if (record==(((QMRF_HTMLBeauty)htmlBeauty).getPage()*((QMRF_HTMLBeauty)htmlBeauty).getPageSize())) {
				if (((QMRF_HTMLBeauty)htmlBeauty).getSearchQuery()==null) {
					 output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidget("You haven't specified a structure search query", "Please try the structure search menu."));
				} else  
					output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidget(
							record==0?"Query returns no results":"No more results", 
							"Please try a different query"));
			}			
		} catch (Exception x) {}
		super.footer(output, query);
	}
	
	protected void printStructureDiagramEditor() throws Exception {
		
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
	protected String queryService;
	protected DBAttachment attachment = null;
    public DBAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(DBAttachment attachment) {
		this.attachment = attachment;
	}
	protected String[] datasets = null;
	public String[] getDatasets() {
		return datasets;
	}

	public void setDatasets(String[] datasets) {
		this.datasets = datasets;
	}
	protected Double threshold; 
	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}
	protected SearchMode option;

	public SearchMode getOption() {
		return option;
	}

	public void setOption(SearchMode option) {
		this.option = option;
	}

	public StructureHTMLBeauty(String queryService) {
		super();
		this.queryService = queryService;
		setSearchURI(Resources.chemical);
	}

	@Override
	public String getSearchTitle() {
		return "Structure search";
		
	}
	
	public String getSmartsOption() {
		return SearchMode.smarts.equals(option)?String.format("&smarts=%s", Reference.encode(searchQuery)):"";
	}


	@Override
	protected String searchMenu(Reference baseReference, Form form) {
		
		
		searchTitle = attachment==null?getSearchTitle():
				String.format("<a href='%s%s/%s' title='QMRF document'>%s</a>&nbsp;<a href='%s%s/%s%s/A%d' title='%s'>%s</a>",
							baseReference,Resources.protocol,attachment.getProtocol(),attachment.getProtocol(),
							baseReference,Resources.protocol,attachment.getProtocol(),Resources.attachment,attachment.getID(),
							attachment.getDescription(),attachment.getType().toString());
		/*already set by the resource
		searchQuery = form.getFirstValue(QueryResource.search_param);
		pageSize = 10;
		try { pageSize = Long.parseLong(form.getFirstValue("pagesize")); if ((pageSize<1) && (pageSize>100)) pageSize=10;} catch (Exception x) { pageSize=10;}
		page = 0;
		try { page = Integer.parseInt(form.getFirstValue("page")); if ((page<0) || (page>100)) page=0;} catch (Exception x) { page=0;}
		threshold = form.getFirstValue("threshold");
		option = SearchMode.auto;
		try {
			option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
		} catch (Exception x) {
			option = SearchMode.auto;
		}
		*/
		String hint = "Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.";
		
		String imgURI = searchQuery==null?"":
				String.format("<img title='%s' alt='%s' border='1' width='150' height='150' src='%s/depict/cdk?search=%s&media=%s&w=150&h=150' onError=\"hideDiv('querypic')\">",
						getSearchQuery(),getSearchQuery(),
						 queryService,Reference.encode(searchQuery),Reference.encode("image/png"));
			return
		   String.format(		
		   "<div class='search ui-widget'>\n"+
		   "<p title='%s'>%s</p>\n"+
		   "<form method='GET' name='form' action='%s%s'>\n"+
		   "\n"+
		   "<input type='hidden' name='page' value='%s'>\n"+
		   "<input type='hidden' name='type' value='smiles'>\n"+
		   "<table width='100%%'>\n"+
		   "<tr><td colspan='2' align='center'><input type='button' class='draw' tabindex='0' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor(\"%s\");'></td></tr>\n"+
		   "<tr><td colspan='2' align='center'><input type='text' name='search' size='20' value='%s' tabindex='1' title='%s'></td></tr>\n"+
		   "<tr><td colspan='2'><input %s type='radio' value='auto' name='option' title='Exact structure or search by identifier' size='20'>Auto</td></tr>\n"+
		   "<tr><td><input %s type='radio' name='option' value='similarity' title='Enter SMILES or draw structure'>Similarity</td>\n"+
		   "<td align='left'>\n"+
		   "<select title ='Tanimoto similarity threshold' name='threshold'><option value='0.9' checked>0.9</option><option value='0.8'>0.8</option><option value='0.7'>0.7</option><option value='0.6'>0.6</option><option value='0.5'>0.5</option></select>\n"+
		   "</td></tr>\n"+
		   "<tr><td colspan='2'><input %s type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure</td></tr>\n"+
		   "<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n"+
		   "<tr><td colspan='2' align='center'><input tabindex='2' id='submit' type='submit' value='Search'/></td></tr>\n"+		   
		   "</div>\n"+
		   "</table>\n"+
		   "</form> \n"+
		   "&nbsp;\n"+
		   "<div id='querypic' class='structureright'>%s</div>"+
		   "</div>\n",	
		   hint,
		   searchTitle,
		   baseReference,
		   getSearchURI(),
		   page,
			//JME		   
		   baseReference,		   
		   //search
		   searchQuery==null?"":searchQuery,
		   hint,
		   SearchMode.auto.equals(option)?"checked":"",
		   SearchMode.similarity.equals(option)?"checked":"",
		   SearchMode.smarts.equals(option)?"checked":"",
		   pageSize,
		   imgURI
		   );
	}
	@Override
	public String getPaging(int page, int start, int last, long pageSize) {

		// Having a constant number of pages display on top is convenient for the users and provides more consistent
		// overall look. But this would require the function to define different input parameters. In order to not
		// break it, implement a workaround, by calculating how many pages the caller (likely) intended to be shown.
		int total = last - start;

		// Normalization
		start = start<0?0:start; // don't go beyond first page
		last = start + total;

		String url = "<li><a class='%s' href='?page=%d&pagesize=%d&search=%s&option=%s&threshold=%3.2f'>%s</a></li>";

		StringBuilder b = new StringBuilder(); 
		b.append("<div><ul id='hnavlist'>");

		// Disable this for the time being as it seems to not fit well into the overall look.
		//b.append(String.format("<li><a href='#'>Pages:</a></li>"));

		// Display "first" and "previous" for the first page as inactive.
		if (page > 0) {
			b.append(String.format(url, "pselectable", 0, pageSize,
					searchQuery==null?"":searchQuery, option==null?"":option.name(),
					threshold==null?"":threshold, "&lt;&lt;"));
			b.append(String.format(url, "pselectable", page-1, pageSize,
					searchQuery==null?"":searchQuery, option==null?"":option.name(),
					threshold==null?"":threshold, "&lt;"));
		} else {
			b.append(String.format("<li class='inactive'>&lt;&lt;</li>"));
			b.append(String.format("<li class='inactive'>&lt;</li>"));
		}

		// Display links to pages. Pages are counted from zero! Hence why we display "i+1".
		for (int i=start; i<= last; i++)
			b.append(String.format(url, i==page?"current":"pselectable", i, pageSize,
					searchQuery==null?"":searchQuery, option==null?"":option.name(), threshold==null?"":threshold, i+1)); 
		b.append(String.format(url, "pselectable", page+1, pageSize,
					searchQuery==null?"":searchQuery, option==null?"":option.name(), threshold==null?"":threshold,"&gt;"));
		// b.append(String.format("<li><label name='pageSize' value='%d' size='4' title='Page size'></li>",pageSize));
		b.append("</ul></div><br>");

		// Apply style for the hovered buttons sans (!) the currently selected one.
		// There are better ways to do it, but this should be okay for now.
		b.append(String.format(
			"<script>\n" +

			"$('a.pselectable').mouseover(function () { $(this).addClass('phovered');    } );\n" +
			"$('a.pselectable').mouseout(function  () { $(this).removeClass('phovered'); } );\n" +

			"</script>\n"
		));

		return b.toString();

	} // getPaging()

}
