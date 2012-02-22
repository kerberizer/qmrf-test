package net.idea.rest.protocol;

import java.io.IOException;
import java.io.Writer;

import net.idea.qmrf.client.Resources;
import net.idea.restnet.c.AbstractResource;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryResource;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;

public class QMRF_HTMLBeauty extends HTMLBeauty {
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
			w.write(String.format("<link rel=\"meta\" type=\"application/rdf+xml\" title=\"%s\" href=\"%s\"/>",
					title,
					ref
					)); 
			
			w.write(String.format("<link rel=\"primarytopic\" type=\"application/rdf+xml\" href=\"%s\"/>",
					ref
					)); 		
			
			w.write(String.format("<title>%s</title>",title));
			
			w.write(String.format("<script type=\"text/javascript\" src=\"%s/jquery/jquery-1.4.2.min.js\"></script>\n",baseReference));
			w.write(String.format("<script type=\"text/javascript\" src=\"%s/jquery/jquery.tablesorter.min.js\"></script>\n",baseReference));
			w.write(meta);
					
			w.write(String.format("<link href=\"%s/style/ambit.css\" rel=\"stylesheet\" type=\"text/css\">",baseReference));
			w.write("<meta name=\"robots\" content=\"index,follow\"><META NAME=\"GOOGLEBOT\" CONTENT=\"index,FOLLOW\">");
			w.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
			w.write("<meta http-equiv='content-type' content='text/html; charset=iso-8859-1' />");
			w.write("</head>\n");
			w.write("<body>");
			w.write(String.format("<link rel=\"stylesheet\" href=\"%s/style/tablesorter.css\" type=\"text/css\" media=\"screen\" title=\"Flora (Default)\">",baseReference));
			w.write("\n");
			
			StringBuilder header = new StringBuilder();
			header.append(String.format("<a href='#'><img  border='0' class='floatleft' src='%s/images/IHCP_logo.jpg'></a>\n",baseReference));
			header.append("<label>(Q)SAR Model Reporting Format Inventory</label>\n");
			header.append(String.format("<a href='#'><img  border='0' class='floatright' src='%s/images/JRC_logo.jpg'></a>\n",baseReference));
			
			w.write(String.format("<div id='wrap'><div id='header'>%s</div>\n",header));
			//menu
			w.write(
			   String.format(		
			   "<div id='inner-wrap'>\n"+
			   "  <div id='left'>\n"+
			   "	<div id='menu'>\n"+
			   "		<ul id='navmenu'>\n"+
			   "			<li><a href='~home'>Home</a></li>\n"+
			   "			<li><a href='%s/protocol?pagesize=10'>Documents</a></li>\n"+
			   "			<li><a href='%s/organisation?pagesize=10'>Organisations</a></li>\n"+
			   "			<li><a href='%s/project'>Projects</a></li>\n"+
			   "			<li><a href='%s/user?pagesize=10'>User</a></li>\n"+			   
			   "			<li><a id='js_sub' class='sub thispage' href='#'>Documents</a></li>\n"+
			   "			<li><a id='php_sub' class='sub thispage' href='#'>Structure</a></li>\n"+
			   "			<li id='/help'><a href='/help'>Help</a></li></ul>\n"+
			   "		</div>\n"
			   ,				   
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

			String searchQuery = "";
			String pageSize = "10";
			try {
				Form form = getParams(params,request);
				if ((form != null) && (form.size()>0)) {
					searchQuery = form.getFirstValue(AbstractResource.search_param)==null?"":form.getFirstValue(AbstractResource.search_param);
					pageSize = form.getFirstValue("pagesize")==null?"10":form.getFirstValue("pagesize");

				}
			} catch (Exception x) {
				searchQuery = "";
				pageSize = "10";
			}
			Reference baseReference = request.getRootRef();
			w.write(
					   String.format(		
					   "<div class='search'>\n"+
					   "QMRF free text search\n"+
					   "<form method='GET' action='%s/protocol?pagesize=10'>\n"+
					   "<input type='text' name='search' size='20' value='%s' tabindex='0'>\n"+
					   "<a href='%s/protocol?mode=advanced' title='Advanced QMRF search'>Advanced</a>\n"+
					   "<input type='hidden' name='pagesize' value='%s'>\n"+
					   "<input type='submit' value='Search'>\n"+
					   "</form> \n"+
					   "&nbsp;\n"+
					   "</div>\n"+
						
					   "<div class='search'>\n"+
					   "Structure search\n"+
					   "<form method='GET' action='%s/structure?pagesize=10'>\n"+
					   "<input type='text' name='search' size='20' value='' value='' tabindex='1' title='Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically. Use the Advanced link to launch structure diagram editor.'>\n"+
					   "<a href='%s/structure?mode=advanced' title='Advanced structure search'>Advanced</a>\n"+
					   "<input type='hidden' name='pagesize' value='%s'>\n"+
					   "<input type='submit' value='Search'>\n"+
					   "</form> \n"+
					   "&nbsp;\n"+
					   "</div>\n"+			   
					   "</div>\n",				   
					   baseReference,
					   searchQuery,
					   baseReference,
					   pageSize,
					   baseReference,
					   baseReference,
					   pageSize
					   )
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
		    b.append("</ul></div>");
		    return b.toString();
		}
		
}
