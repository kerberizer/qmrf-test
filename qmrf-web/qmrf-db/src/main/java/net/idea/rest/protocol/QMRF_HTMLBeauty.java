package net.idea.rest.protocol;

import java.io.IOException;
import java.io.Writer;

import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.resource.db.ProtocolDBResource.SearchMode;
import net.idea.restnet.c.AbstractResource;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;

public class QMRF_HTMLBeauty extends HTMLBeauty {
	protected String searchURI = Resources.protocol;
	protected String searchTitle = "QMRF documents search";
	protected int page;
	protected long pageSize;

	public QMRF_HTMLBeauty() {
		super();

	};

		@Override
		protected String getHomeURI() {
			return "http://qsardb.jrc.it";
		}
		@Override
		protected String getLogoURI(String root) {
			return null;//String.format("%s/images/ambit-logo.png",root==null?"":root);
		}
		@Override
		public String getTitle() {
			return "(Q)SAR Model Reporting Format Inventory";
		}
		public void writeTopLinks(Writer w,String title,Request request,String meta,ResourceDoc doc, Reference baseReference) throws IOException {
/*
			w.write(String.format("<a href='%s%s'>Protocols</a>&nbsp;",baseReference,Resources.protocol));
			w.write(String.format("<a href='%s%s'>Organisations</a>&nbsp;",baseReference,Resources.organisation));
			w.write(String.format("<a href='%s%s'>Projects</a>&nbsp;",baseReference,Resources.project));
			
			w.write(String.format("<a href='%s%s'>Users</a>&nbsp;",baseReference,Resources.user));
*/
		}
	
		@Override
		public String getLoginLink() {
			return "Login";
		}
		@Override
		public void writeTopHeader(Writer w,String title,Request request,String meta,ResourceDoc doc) throws IOException {
			Reference baseReference = request==null?null:request.getRootRef();

			w.write(
					"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
				);
			
			w.write(String.format("<html %s %s %s>",
					"xmlns=\"http://www.w3.org/1999/xhtml\"",
					"xmlns:dc=\"http://purl.org/dc/elements/1.1/\"",
					"xmlns:ot=\"http://opentox.org/api/1.1/\"")
					);
			
			w.write(String.format("<head> <meta property=\"dc:creator\" content=\"%s\"/> <meta property=\"dc:title\" content=\"%s\"/>",
					request.getResourceRef(),
					title
					)
					);
			
			Reference ref = request.getResourceRef().clone();
			ref.addQueryParameter("media", Reference.encode("application/rdf+xml"));
			w.write(String.format("<link rel=\"meta\" type=\"application/rdf+xml\" title=\"%s\" href=\"%s\"/>\n",
					title,
					ref
					)); 
			
			w.write(String.format("<link rel=\"primarytopic\" type=\"application/rdf+xml\" href=\"%s\"/>",
					ref
					)); 		
			
			w.write(String.format("<title>%s</title>\n",title));
			
			w.write(String.format("<script type=\"text/javascript\" src=\"%s/jquery/jquery-1.7.1.min.js\"></script>\n",baseReference));
			w.write(String.format("<script type=\"text/javascript\" src=\"%s/jquery/jquery-ui-1.8.18.custom.min.js\"></script>\n",baseReference));
			w.write(String.format("<script type=\"text/javascript\" src=\"%s/jquery/jquery.MultiFile.pack.js\"></script>\n",baseReference));
			
			//w.write(String.format("<script type=\"text/javascript\" src=\"%s/jquery/jquery.tablesorter.min.js\"></script>\n",baseReference));
			w.write(meta);
					
			w.write(String.format("<link href=\"%s/style/ambit.css\" rel=\"stylesheet\" type=\"text/css\">\n",baseReference));
			w.write(String.format("<link href=\"%s/style/jquery-ui-1.8.18.custom.css\" rel=\"stylesheet\" type=\"text/css\">\n",baseReference));
			
			w.write("<meta name=\"robots\" content=\"index,follow\"><META NAME=\"GOOGLEBOT\" CONTENT=\"index,FOLLOW\">\n");
			w.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
			w.write("<meta http-equiv='content-type' content='text/html; charset=iso-8859-1' />\n");
			w.write(String.format("<script type=\"text/javascript\" src=\"%s/jme/jme.js\"></script>\n",baseReference));
			w.write("<script>$(function() {$( \".accordion\" ).accordion({autoHeight: false,navigation: true});});</script>");
			//w.write("<script>$(function() {$( \".tabs\" ).tabs({event: \"mouseover\"});});</script>");
			//w.write("<script>$(function() {$( \".tabs\" ).tabs({event: \"mouseover\",cache: true, ajaxOptions: {error: function( xhr, status, index, anchor ) {$( anchor.hash ).html(status );}}});});</script>");
			w.write("<script>$(function() {$( \".tabs\" ).tabs({event: \"mouseover\",cache: true});});</script>");
			w.write("<script>$(function() {$( \"#selectable\" ).selectable();});</script>");
			w.write("<script type='text/javascript'>function toggleDiv(divId) {$('#'+divId).toggle();}</script>\n");
			w.write("</head>\n");
			w.write("<body>");
			w.write(String.format("<link rel=\"stylesheet\" href=\"%s/style/tablesorter.css\" type=\"text/css\" media=\"screen\" title=\"Flora (Default)\">\n",baseReference));
			w.write("\n");
			
			StringBuilder header = new StringBuilder();
			header.append(String.format("<a href='#'><img  border='0' class='floatleft' src='%s/images/IHCP_logo.jpg'></a>\n",baseReference));
			
			header.append(String.format(
					"<span style='margin: 5px 0 0 0;'><img src='%s/images/(q)sar_model_reporting_format_inventory.png' alt='%s' title='%s' ></span>",
					baseReference,getTitle(),getTitle()));
			header.append(String.format("<a href='#'><img  border='0' class='floatright' src='%s/images/JRC_logo.jpg'></a>\n",baseReference));
			
			w.write(String.format("<div id='wrap'><div id='header'>%s</div>\n",header));
			//menu
			w.write(
			   String.format(		
			   "<div id='inner-wrap'>\n"+
			   "  <div id='left'>\n"+
			   "	<div id='menu'>\n"+
			   "		<ul id='navmenu'>\n"+
			   "			<li><a href='%s/protocol?pagesize=10'>Documents</a></li>\n"+
			   "			<li><a href='%s/structure?pagesize=10'>Structures</a></li>\n"+
			   "			<li><a href='%s/endpoint'>Endpoints</a></li>\n"+
			   "			<li><a href='%s/organisation?pagesize=10'>Organisations</a></li>\n"+
			   "			<li><a href='%s/user?pagesize=10'>Users</a></li>\n"+			   
			   "			<li id='/help'><a href='/help'>Help</a></li></ul>\n"+
			   "		</div>\n"
			   ,				   
			   baseReference,
			   baseReference,
			   baseReference,
			   baseReference,
			   baseReference
			   )
			   );
				
			//followed by the search form
			
			/*
			left = "";
			middle = "";
			right = String.format("<a href='%s/%s'>%s</a>",
					baseReference.toString(),
					getLoginLink(),
					request.getClientInfo().getUser()==null?"Login":"My account");
			writeDiv3(w, left, middle, right);
			*/

		}

		
		@Override
		public void writeHTMLFooter(Writer output,String title,Request request) throws IOException {
			//div id=content
			output.write("\n</div>\n"); 
			//div inner-wrap
			output.write("\n</div>\n"); 
			//footer
			output.write("\n<div id='footer'>");
			output.write("Developed by Ideaconsult Ltd. (2007-2012) on behalf of JRC"); 
			output.write("</div>\n");
			output.write(jsGoogleAnalytics()==null?"":jsGoogleAnalytics());
			//div id=wrap
			output.write("\n</div>\n"); 
			output.write("\n</body>");
			output.write("</html>");

		}
		@Override
		public void writeSearchForm(Writer w,String title,Request request ,String meta,Method method,Form params) throws IOException {

			Reference baseReference = request.getRootRef();
			try {
				w.write(searchMenu(baseReference,getParams(params,request)));
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				w.write("</div>\n");
			}
		}	
	
		protected String searchMenu(Reference baseReference,Form form)  {
			String searchQuery = "";
			String pageSize = "10";
			String structure = null;
			try {
				if ((form != null) && (form.size()>0)) {
					searchQuery = form.getFirstValue(AbstractResource.search_param)==null?"":form.getFirstValue(AbstractResource.search_param);
					pageSize = form.getFirstValue("pagesize")==null?"10":form.getFirstValue("pagesize");
					structure = form.getFirstValue("structure");
				}
			} catch (Exception x) {
				searchQuery = "";
				pageSize = "10";
			}
			SearchMode option = SearchMode.text;
			try {
				option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
			} catch (Exception x) {
				option = SearchMode.text;
			}
			String imgURI = (structure==null) || !structure.startsWith("http")?"":
				String.format("<img border='0' title='Showing QMRF documents for this chemical' width='150' height='150' src='%s?media=%s&w=150&h=150'><br>Showing QMRF documents\n",
						structure,Reference.encode("image/png"));
			
				return
			   String.format(		
			   "<div class='search ui-widget'>\n"+
			   "<p>%s</p>\n"+
			   "<form method='GET' action='%s%s?pagesize=10'>\n"+
			   "<table width='200px'>\n"+
			   "<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Enter search query'></td></tr>\n"+
			   "<tr><td colspan='2'><input %s tabindex='1' type='radio' value='text' name='option' title='Free text search' size='20'>Free text</td></tr>\n"+
			   "<tr><td><input %s type='radio' tabindex='2' name='option' value='endpoint' title='Search by endpoint'>Endpoint</td>\n"+
			   "<tr><td colspan='2'><input %s tabindex='3' type='radio' value='author' name='option' title='Search by author' size='20'>Author</td></tr>\n"+
			   "<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n"+
			   "<input type='hidden' name='structure' value='%s'>\n"+
			   "<tr><td></td><td align='left'><input type='submit' tabindex='4'  value='Search'/></td></tr>\n"+
			   "</table>\n"+			   
			   "</form> \n"+
			   "&nbsp;\n"+
			   "<div class='structureright'>%s</div>"+
			   "</div>\n",

			   getSearchTitle(),
			   baseReference,
			   getSearchURI(),
			   searchQuery,
			   SearchMode.text.equals(option)?"checked":"",
			   SearchMode.endpoint.equals(option)?"checked":"",
			   SearchMode.author.equals(option)?"checked":"",
			   pageSize,
			   structure,
			   imgURI
			   );
		}
		@Override
		public void writeHTMLHeader(Writer w,String title,Request request,String meta,ResourceDoc doc) throws IOException {

			writeTopHeader(w, title, request, meta,doc);
			writeSearchForm(w, title, request, meta);
			w.write("<div id='content'>\n");
		}
		
		public static String getPaging(int page,int start, int last, long pageSize) {
			String url = "<li><a href='?page=%d&pagesize=%d'>%s</a></li>";
		    StringBuilder b = new StringBuilder(); 
		    b.append("<div><ul id='hnavlist'>");
		    b.append(String.format(url,page,pageSize,"Pages:"));
		    b.append(String.format(url,0,pageSize,"<<"));
		    b.append(String.format(url,page==0?page:page-1,pageSize,"Prev"));
		    for (int i=start; i<= last; i++)
		    	b.append(String.format(url,i,pageSize,i+1)); //zero numbered pages
		    b.append(String.format(url,page+1,pageSize,"Next"));
		   // b.append(String.format("<li><label name='pageSize' value='%d' size='4' title='Page size'></li>",pageSize));
		    b.append("</ul></div><br>");
		    return b.toString();
		}
		
		public String getSearchURI() {
			return searchURI;
		}

		public void setSearchURI(String searchURI) {
			this.searchURI = searchURI;
		}

		
		public String getSearchTitle() {
			return searchTitle;
		}

		public void setSearchTitle(String searchTitle) {
			this.searchTitle = searchTitle;
		}
		
		public String getPaging(int start, int last) {
			return getPaging(page, start, last, pageSize);
		}
		
		public String printUploadForm(String uri, DBProtocol protocol,boolean attachments) throws Exception {
			return printUploadForm("",uri, protocol,attachments);
		}
		public String printUploadForm(String action, String uri, DBProtocol protocol,boolean attachments) throws Exception {

				StringBuilder content = new StringBuilder();

				content.append(String.format("<form method='POST' action=\"%s\" ENCTYPE=\"multipart/form-data\">",action));

				content.append("<div class='ui-widget-content'><p><strong>QMRF XML file</strong> (1 file) </p></div>");
				content.append(String.format("<p><input type=\"file\" class='multi max-1 accept-xml' name=\"%s\" title='%s' size=\"60\"></p>",
						ReadProtocol.fields.filename.name(),
						"QMRF XML")); 	
				if (attachments) {
				content.append("<div class='ui-widget-content'><p>Attachments: Training dataset(s) - SDF, MOL, CSV, XLS formats, 3 files max</p></div>");
				content.append(String.format("<p><input type=\"file\"  class='multi' maxlength='3' accept='sdf|mol|csv|xls' name=\"%s\" title='%s' size=\"60\"></p>",
						"data_training",
						"Training dataset(s) - SDF, MOL, CSV, XLS formats")); 		
				content.append("<div class='ui-widget-content'><p>Attachments: Test dataset(s) - SDF, MOL, CSV, XLS formats, 3 files max</p></div>");
				content.append(String.format("<p><input type=\"file\"  class='multi' maxlength='3' accept='sdf|mol|csv|xls' name=\"%s\" title='%s' size=\"60\"></p>",
						"data_validation",
						"Test dataset(s) - SDF, MOL, CSV, XLS formats")); 			
				content.append("<div class='ui-widget-content'><p>Attachments: Related document(s) - PDF,3 files max</p></div>");
				content.append(String.format("<p><input type=\"file\"  class='multi' maxlength='3' accept='pdf|doc|xls' name=\"%s\" title='%s' size=\"60\"></p>",
						"document",
						"Related documents - PDF"));
				}
				content.append("<div class='ui-widget-content'><p>Options</p></div>");
				content.append(String.format("<p><strong>%s</strong>%s</p>",
						"Publish immediately",
						ReadProtocol.fields.published.getHTMLField(null)
						));					
				
				content.append("<div  class='ui-widget-header ui-corner-bottom'><p><input type='submit' enabled='false' value='Submit'></p></div>");
//				content.append("<input type='submit' enabled='false' value='Submit'>");
				content.append("</form>");

				

				return printWidget(
							String.format("Add new %s %s",getTitle(),uri.toString().contains("versions")?"version":""),
							content.toString(),
							"");

		}	
		
		public String printWidgetHeader(String header) {
			return	String.format(
					"<div class=\"ui-widget \" style=\"margin-top: 20px; padding: 0 .7em;\">\n"+
					"<div class=\"ui-widget-header ui-corner-top\"><p>%s</p></div>\n",header);
		}
		public String printWidgetFooter() {
			return	String.format("</div>\n");
		}
		public String printWidgetContentHeader(String style) {
			return	String.format("<div class=\"ui-widget-content ui-corner-bottom %s\">\n",style);
		}
		public String printWidgetContentFooter() {
			return	String.format("</div>\n");
		}	
		public String printWidgetContentContent(String content) {
			return
			String.format("<p>%s</p>\n",content);
		}	
		public String printWidgetContent(String content,String style) {
			return String.format("%s\n%s\n%s",
					printWidgetContentHeader(style),
					printWidgetContentContent(content),
					printWidgetContentFooter());
		}
		
		
		public String printWidget(String header,String content,String style) {
			return String.format("%s\n%s\n%s",
					printWidgetHeader(header),
					printWidgetContent(content,style),
					printWidgetFooter());

		}
		
		public String printWidget(String header,String content) {
			return String.format("%s\n%s\n%s",
					printWidgetHeader(header),
					printWidgetContent(content,""),
					printWidgetFooter());

		}		
}
