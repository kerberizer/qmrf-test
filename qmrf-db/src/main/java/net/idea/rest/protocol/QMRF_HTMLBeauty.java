package net.idea.rest.protocol;

import java.io.IOException;
import java.io.Writer;

import net.idea.qmrf.client.QMRFRoles;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.attachments.DBAttachment.attachment_type;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.resource.db.ProtocolDBResource.SearchMode;
import net.idea.restnet.c.AbstractResource;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.security.Role;

public class QMRF_HTMLBeauty extends HTMLBeauty {
	
	final static String qmrfEditorDownloadLink = "http://ambit.uni-plovdiv.bg/downloads/qmrf/QMRFEditor-v2.0.0-setup.exe";
	final static String qmrfEditorEmail = "JRC-IHCP-COMPUTOX@ec.europa.eu";
	final static String QMRF_title = "(Q)SAR Model Reporting Format Inventory";
	final static String[] css = new String[] {
		"<link href=\"%s/style/ambit.css\" rel=\"stylesheet\" type=\"text/css\">\n",
		"<link href=\"%s/style/jquery-ui-1.8.18.custom.css\" rel=\"stylesheet\" type=\"text/css\">\n",
		"<link href=\"%s/style/jquery.dataTables.css\" rel=\"stylesheet\" type=\"text/css\">\n",
		"<link href=\"%s/images/favicon.ico\" rel=\"shortcut icon\" type=\"image/ico\">\n"
	
	};

	final static String[] js = new String[] {
		"<script type='text/javascript' src='%s/jquery/jquery-1.7.1.min.js'></script>\n",
		"<script type='text/javascript' src='%s/jquery/jquery-ui-1.8.18.custom.min.js'></script>\n",
		"<script type='text/javascript' charset='utf8' src='%s/jquery/jquery.dataTables-1.9.0.min.js'></script>\n",
		"<script type='text/javascript' src='%s/jme/jme.js'></script>\n"
	};
	
	//meta
	final static String[] metaTag = new String[] {
		"<meta name=\"robots\" content=\"index,follow\"><META NAME=\"GOOGLEBOT\" CONTENT=\"index,FOLLOW\">\n",
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n",
		"<meta http-equiv='content-type' content='text/html; charset=iso-8859-1' />\n"
	};
	
	// Google +1 buttons
	final static String googlePlusInit =
		"<script type='text/javascript'>" +
		"(function() {" +
		"var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;" +
		"po.src = 'https://apis.google.com/js/plusone.js';" +
		"var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);" +
		"})();" +
		"</script>";

	// Facebook JavaScript SDK
	final static String facebookInit =
			"<div id=\"fb-root\"></div>\n" +
			"<script>(function(d, s, id) {\n" +
			"var js, fjs = d.getElementsByTagName(s)[0];\n" +
			"if (d.getElementById(id)) return;\n" +
			"js = d.createElement(s); js.id = id;\n" +
			"js.src = \"//connect.facebook.net/en_GB/all.js#xfbml=1\";\n" +
			"fjs.parentNode.insertBefore(js, fjs);\n" +
			"}(document, 'script', 'facebook-jssdk'));</script>\n";
	
	// Twitter JS
	final static String twitterInit =
			"<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id))" +
			"{js=d.createElement(s);js.id=id;js.src=\"//platform.twitter.com/widgets.js\";" +
			"fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>\n";

	// LinkedIn
	final static String linkedInInit = "<script src='http://platform.linkedin.com/in.js' type='text/javascript'></script>\n";
	
	
	final static String toggleDivScript =
			"<script type='text/javascript'>function toggleDiv(divId) {\n" +
			"$('#'+divId).toggle();\n" +
			"if ($('#'+divId+'_toggler').hasClass('togglerPlus')) {\n" +
			"$('#'+divId+'_toggler').removeClass('togglerPlus');\n" +
			"$('#'+divId+'_toggler').addClass('togglerMinus');\n" +
			"} else if ($('#'+divId+'_toggler').hasClass('togglerMinus')) {\n" +
			"$('#'+divId+'_toggler').removeClass('togglerMinus');\n" +
			"$('#'+divId+'_toggler').addClass('togglerPlus');\n" +
			"}\n" +
			"}</script>\n";	

	
	final static String hideFooterScript =
		"<script type='text/javascript'>\n" +
		"$(document).ready( function () {\n" +
		"$('div#footer').delay(%d).animate({bottom: '-20px'}, 'slow');\n" +
		"});\n" +
		"</script>\n";
	
	// the JRC IHCP logo
	final static String logoTopLeft =
			"<a href='http://ihcp.jrc.ec.europa.eu/'>\n" +
			"<img class='logo_top-left' src='%s/images/logo_jrc_ihcp.png' alt='JRC IHCP logo'>\n" +
			"</a>\n";
	
	final static String menuMouseOverScript =
		"<script>\n" +
		"$('a.selectable').mouseover(function () { $(this).addClass('hovered');    } );\n" +
		"$('a.selectable').mouseout(function  () { $(this).removeClass('hovered'); } );\n" +
		"</script>\n";

	// top links
	final static String topLinks =
			"<ul class='topLinks'>\n" +
			"<li class='topLinks'>\n" +
			"<a class='topLinks' href='%s'>Download QMRF Editor</a>\n" +
			"</li>\n" +
			"<li class='topLinks'>|</li>\n" +
			"<li class='topLinks'>\n" +
			"<a class='topLinks email' href='mailto:%s'>Submit QMRF by e-mail</a>\n" +
			"</li>\n" +
			"<li class='topLinks'>|</li>\n" +
			"<li class='topLinks'>\n" +
			"<a class='topLinks' href='%s'>Help</a>\n" +
			"</li>\n" +
			"%s" +
			"</ul>\n";
	
	// log in/out link
	final static String logInOutLinkTemplate =
			"<li class='topLinks'>|</li>\n" +
			"<li class='topLinks'>\n" +
			"<a class='topLinks' title='%s' href='%s%s'>%s</a>\n" +
			"</li>\n";

	final static String footerText =
		"<div id='footer'>\n" +
		"Developed by <b>IDEAconsult Ltd.</b> (2007-2012) on behalf of <b>JRC</b>\n" +
		"</div>\n";

	final static String _trStart = "<tr>\n";
	final static String _trEnd = "</tr>\n";
	final static String _tdStart = "<td>";
	final static String _tdEnd = "</td>";
	final static String _tableStart = "<table>\n";
	final static String _tableEnd = "</table>\n";
	final static String _divStart = "\n<div>\n";
	final static String _divEnd = "\n</div>\n";
	
	private boolean loadTabs;

	public enum update_mode {
		update {
			@Override
			public String toString() {
				return "Update ";
			}
		},
		attachments {
			@Override
			public String toString() {
				return "Add attachment(s)";
			}
		},
		newdocument {
			@Override
			public String toString() {
				return "Upload new ";
			}	
		};
	}

	protected String condition;
	protected SearchMode option;

	protected void setLoadTabs(boolean loadTabs) {
		this.loadTabs = loadTabs;
	}
	public boolean isLoadTabs() {
		return loadTabs;
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public QMRF_HTMLBeauty(String searchURI) {
		this(searchURI,!Resources.protocol.equals(searchURI));
	}
	public QMRF_HTMLBeauty(String searchURI,boolean loadTabs) {
		super(searchURI==null?Resources.protocol:searchURI);
		setSearchTitle("QMRF documents search");
		setLoadTabs(loadTabs);
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
			return QMRF_title;
		}
		public void writeTopLinks(Writer w,String title,Request request,String meta,ResourceDoc doc, Reference baseReference) throws IOException {
		}
	
		@Override
		public String getLoginLink() {
			return "Login";
		}
		@Override
		public void writeTopHeader(Writer w,String title,Request request,String meta,ResourceDoc doc) throws IOException {
			Reference baseReference = request==null?null:request.getRootRef();
			
			// Determine if the request is made by Microsoft Internet Explorer 7, as many elements on the page break on it.
			if (request!=null) {
				setMsie7(request.getClientInfo().getAgent().toLowerCase().indexOf("msie 7.")>=0?true:false);
			} else {
				setMsie7(false);
			}
			
			w.write(
					"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
					);
			
			w.write(String.format("<html %s %s %s>",
					"xmlns=\"http://www.w3.org/1999/xhtml\"",
					"xmlns:dc=\"http://purl.org/dc/elements/1.1/\"",
					"xmlns:ot=\"http://opentox.org/api/1.1/\"")
					);
			
			// HEAD starts here.
			w.write(String.format("<head> <meta property=\"dc:creator\" content=\"%s\"/> <meta property=\"dc:title\" content=\"%s\"/>",
					request.getResourceRef(),
					title
					));
			
			Reference ref = request.getResourceRef().clone();
			ref.addQueryParameter("media", Reference.encode("application/rdf+xml"));
			w.write(String.format("<link rel=\"meta\" type=\"application/rdf+xml\" title=\"%s\" href=\"%s\"/>\n",
					title,
					ref
					)); 
			w.write(String.format("<link rel=\"meta\" type=\"text/n3\" title=\"%s\" href=\"%s\"/>\n",
					title,
					ref
					)); 
			
			w.write(String.format("<link rel=\"primarytopic\" type=\"application/rdf+xml\" href=\"%s\"/>",
					ref
					)); 		
			w.write(String.format("<link rel=\"primarytopic\" type=\"text/n3\" href=\"%s\"/>",
					ref
					)); 			
			w.write(String.format("<title>%s</title>\n",title));
			
			// Initialize Google +1, FB, Twitter, Linked In buttons
			w.write(googlePlusInit);
			w.write(facebookInit);
			w.write(twitterInit);
			w.write(linkedInInit);

			w.write(meta);

			//meta		
			for (String tag : metaTag ) w.write(String.format(tag,baseReference));
			//css			
			for (String style : css ) w.write(String.format(style,baseReference));
			//js
			for (String script : js ) w.write(String.format(script,baseReference));



			w.write("<script>$(function() {$( \".accordion\" ).accordion({autoHeight: false,navigation: true});});</script>");
			w.write("<script>$(function() {$( \"#selectable\" ).selectable();});</script>");
			w.write("<script type='text/javascript'>function hideDiv(divId) {\n$('#'+divId).hide();}</script>\n");
			final String dtableOptions = "'bJQueryUI': true, "+
					//"'sPaginationType': 'full_numbers',"+
					"'bPaginate'      : true,"+
					"\"sDom\": 'T<\"clear\"><\"fg-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix\"lfr>t<\"fg-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix\"ip>'";

			w.write(String.format("<script>$(function() {$( \".datatable\" ).dataTable({%s });});</script>",dtableOptions));
			
			// Don't style the submit button with jQ if the browser is MSIE 7.
			if (!isMsie7()) w.write("<script>$(function() {$(\"#submit\").button();});</script>");
			
			// The next line is commented, because we MUST NOT initialise any div-tabs before they get populated.
			// But this is only true for documents, as the rest of the resources are loaded the usual way.
			if (isLoadTabs()) 
				w.write("<script>$(function() {$( \".tabs\" ).tabs({cache: true});});</script>");

			w.write(toggleDivScript);
			
			// Hide the footer N milliseconds after the page is loaded.

			w.write(String.format(hideFooterScript, 7000));
			
			// HEAD ends here.
			w.write("</head>\n");
			
			// HTML body begins here.
			w.write("<body>\n");
			
			w.write("<div id='wrap'>\n");
			
			w.write("<div id='header'>\n");
			
			String logInOutLink = "";
			
			if (!getSearchURI().equals(Resources.login)) {
				logInOutLink = String.format(logInOutLinkTemplate,
					// Log in/out hint
					request.getClientInfo().getUser()==null? 
							"Log in here to submit new documents (only required for editors)"
							:
							String.format("You are currently logged in as \"%s\". Click here to log out.", request.getClientInfo().getUser()),
					// Log in/out URL 1/2							
					baseReference.toString(), 
					 //Log in/out URL 2/2
					Resources.login, 
					// Log in/out text
					request.getClientInfo().getUser()==null? 
							"Log in":String.format("Log out [<b>%s</b>]", request.getClientInfo().getUser()));
			}
			
			w.write(String.format(
					topLinks,
					// Download QMRF Editor
					qmrfEditorDownloadLink, 
					//Sumbit QMRF by e-mail
					qmrfEditorEmail, 
					// Help
					baseReference.toString(),					
					logInOutLink
			));
			

			w.write(String.format(logoTopLeft, baseReference));
			
			w.write("</div>\n"); // header
			
			// left section
			w.write("<div id='inner-wrap'>\n" +
					"<div id='left'>\n"
			);
			
			//menu
			String[][] menu = {
					{ Resources.protocol, "Documents", "10", "All published QMRF documents" },
					{ Resources.chemical, "Structures", "10", "Chemical structures search" },
					{ Resources.endpoint, "Endpoints", null, "QMRF documents by endpoints" },
			};

			w.write(
					"<div id='menu'>\n" +
					"<ul id='navmenu'>\n"
			);
						
			// Documents, Structures, Endpoints
			for (String[] menuItem: menu) {
				w.write(printMenuItem(menuItem[0], menuItem[1], baseReference.toString(), menuItem[2], menuItem[3]));
			}
			
			// If user is logged in, show My profile.
			if (request.getClientInfo().getUser() != null) {
				w.write(printMenuItem(
						Resources.myaccount,
						"My profile",
						baseReference.toString(),
						null,
						String.format("%s's profile and documents.", request.getClientInfo().getUser())
				));
			}
			
			String myProfile=null;
			String unpublishedDoc = null;
			
			for (Role role: request.getClientInfo().getRoles()) try {
				QMRFRoles qmrfrole = QMRFRoles.valueOf(role.getName());
				if (qmrfrole.getURI()!=null)
					myProfile = printMenuItem(qmrfrole.getURI(), qmrfrole.toString(), baseReference.toString(),null,qmrfrole.getHint());
				switch (qmrfrole) {
					case qmrf_manager: {
						w.write(printMenuItem(Resources.user, "Users", baseReference.toString(),null,"All registered users."));
						w.write(printMenuItem(Resources.organisation, "Organisations", baseReference.toString(),null,"All registered user affiliations."));
						break;
					}
					case qmrf_admin: {
						unpublishedDoc = printMenuItem(Resources.unpublished, "Unpublished documents", baseReference.toString(),"10","All unpublished QMRF documents.");
						break;
					}
					case qmrf_editor: {
						unpublishedDoc = printMenuItem(Resources.unpublished, "Unpublished documents", baseReference.toString(),"10","All unpublished QMRF documents.");
						break;
					}
				}
			} catch (Exception x) {/* unknown role */}
				
			if (myProfile!=null) w.write(myProfile);
			
			if (unpublishedDoc!=null) w.write(unpublishedDoc);
			
			w.write("</ul>\n</div>\n"); // div id='menu'
		
			// Apply style for the hovered buttons sans (!) the currently selected one.
			// There are better ways to do it, but this should be okay for now.
			// However, this breaks MSIE 7. Moreover, this browser gets crazy even if
			// the change is implemented purely with simple CSS a:hover, and for this
			// reason, we simply disable the mousever effect for it.
			if (!isMsie7()) {
				w.write(menuMouseOverScript);
			}
				
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

		} // writeTopHeader()
		protected String printMenuItem(String relativeURI,String title,String baseReference,String pagesize) {
			return this.printMenuItem(relativeURI, title, baseReference, pagesize,"");
		}
		protected String printMenuItem(String relativeURI,String title,String baseReference,String pagesize,String hint) {
			return String.format("<li><a class='%s' title='%s' href='%s%s%s%s'>%s</a></li>\n",
					getSearchURI().equals(relativeURI)?"selected":"selectable",
					hint==null?title:hint,
					baseReference,relativeURI,
					pagesize==null?"":"?pagesize=",
					pagesize==null?"":pagesize,
					title);
		}
		
		@Override
		public void writeHTMLFooter(Writer output,String title,Request request) throws IOException {
			//div ui-widget
			output.write(_divEnd); 
			//div id=content
			output.write(_divEnd); 
			//div inner-wrap
			output.write(_divEnd);
			// Push the footer downwards, so that we don't accidentally step on it.
			output.write("\n<div class='pusher'></div>");
			//div id=wrap
			output.write(_divEnd); 
			//footer
			
			output.write(footerText);
			
			output.write(jsGoogleAnalytics()==null?"":jsGoogleAnalytics());
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
				w.write(_divEnd);
			}
		}	
	
		protected String searchMenu(Reference baseReference,Form form)  {
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
			option = SearchMode.text;
			try {
				option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
			} catch (Exception x) {
				option = SearchMode.text;
			}
			condition = "";
			try {
				condition = form.getFirstValue("condition").toLowerCase();
			} catch (Exception x) {
				condition = "";
			}
			String hint = "";
			String imgURI = (structure==null) || !structure.startsWith("http")?"":
				String.format("<img border='0' title='Showing QMRF documents for this chemical' width='150' height='150' src='%s?media=%s&w=150&h=150'><br>Showing QMRF documents\n",
						structure,Reference.encode("image/png"));
			
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("<div class='search ui-widget'>\n");
				stringBuilder.append("<p title='%s'>%s</p>\n");
				stringBuilder.append("<form method='GET' action='%s%s?pagesize=10'>\n");
				stringBuilder.append("<table width='200px'>\n");
				stringBuilder.append("<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Enter search query'></td></tr>\n");
				stringBuilder.append("<tr><td colspan='2'><input %s tabindex='1' type='radio' value='title' name='option' title='Title' size='20'>Title</td></tr>\n");
				stringBuilder.append("<tr><td colspan='2'><input %s tabindex='1' type='radio' value='text' name='option' title='Free text search' size='20'>Free text</td></tr>\n");
				stringBuilder.append("<tr><td><input %s type='radio' tabindex='2' name='option' value='endpoint' title='Search by endpoint'>Endpoint</td>\n");
				stringBuilder.append("<tr><td colspan='2'><input %s tabindex='3' type='radio' value='author' name='option' title='Search by author' size='20'>Author</td></tr>\n");
				stringBuilder.append("<tr><td><input %s type='radio' tabindex='4' name='option' value='qmrfnumber' title='Search by QMRF number'>QMRF number</td>\n");
				stringBuilder.append("<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n");
				stringBuilder.append("<input type='hidden' name='structure' value='%s'>\n");
				stringBuilder.append("<tr><td colspan='2' align='center'><input type='submit' id='submit' tabindex='4' value='Search'/></td></tr>\n");
				stringBuilder.append("</table>\n");
				stringBuilder.append("</form> \n");
				stringBuilder.append("&nbsp;\n");
				stringBuilder.append("<div class='structureright'>%s</div>");
				stringBuilder.append(_divEnd);
				
				return String.format(
						stringBuilder.toString(),
						hint,
						getSearchTitle(),
						baseReference,
						getSearchURI(),
						searchQuery==null?"":searchQuery,
						SearchMode.title.equals(option)?"checked":"",
						SearchMode.text.equals(option)?"checked":"",
						SearchMode.endpoint.equals(option)?"checked":"",
						SearchMode.author.equals(option)?"checked":"",
						SearchMode.qmrfnumber.equals(option)?"checked":"",
						pageSize,
						structure==null?"":structure,
						imgURI
			   );
		}
		
		@Override
		public void writeHTMLHeader(Writer w,String title,Request request,String meta,ResourceDoc doc) throws IOException {

			writeTopHeader(w, title, request, meta,doc);
			writeSearchForm(w, title, request, meta,null);
			w.write("<div id='content'>\n");
		}
		
		// Paging
		public String getPaging(int page, int start, int last, long pageSize) {

			// Having a constant number of pages display on top is convenient for the users and provides more consistent
			// overall look. But this would require the function to define different input parameters. In order to not
			// break it, implement a workaround, by calculating how many pages the caller (likely) intended to be shown.
			int total = last - start;

			// Normalization
			start = start<0?0:start; // don't go beyond first page
			last = start + total;

			String search = searchQuery==null?"":Reference.encode(searchQuery);
			String cond = condition==null?"":Reference.encode(condition);
			String url = "<li><a class='%s' href='?page=%d&pagesize=%d&search=%s&option=%s&condition=%s'>%s</a></li>";

			StringBuilder b = new StringBuilder(); 
			b.append("<div><ul id='hnavlist'>");

			// Disable this for the time being as it seems to not fit well into the overall look.
			//b.append(String.format("<li id='pagerPages'>Pages</li>"));

			// Display "first" and "previous" for the first page as inactive.
			if (page > 0) {
				b.append(String.format(url, "pselectable", 0, pageSize, search, option==null?"":option.name(), cond, "&lt;&lt;"));
				b.append(String.format(url, "pselectable", page-1, pageSize, search, option==null?"":option.name(), cond, "&lt;"));
			} else {
				b.append(String.format("<li class='inactive'>&lt;&lt;</li>"));
				b.append(String.format("<li class='inactive'>&lt;</li>"));
			}

			// Display links to pages. Pages are counted from zero! Hence why we display "i+1".
			for (int i=start; i<= last; i++)
				b.append(String.format(url, i==page?"current":"pselectable", i, pageSize, search, option==null?"":option.name(), cond, i+1)); 
			b.append(String.format(url, "pselectable", page+1, pageSize, search, option==null?"":option.name(), cond, "&gt;"));
			b.append("</ul></div><br>");

			// Apply style for the hovered buttons sans (!) the currently selected one.
			// There are better ways to do it, but this should be okay for now.
			// However, this breaks MSIE 7. Moreover, this browser gets crazy even if
			// the change is implemented purely with simple CSS a:hover, and for this
			// reason, we simply disable the mousever effect for it.
			if (!isMsie7()) {
				b.append("<script>\n");
				b.append("$('a.pselectable').mouseover(function () { $(this).addClass('phovered');    } );\n");
				b.append("$('a.pselectable').mouseout(function  () { $(this).removeClass('phovered'); } );\n");
				b.append("</script>\n");
			}

			return b.toString();
		}

		/*
		public String getPaging(int page,int start, int last, long pageSize) {
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
		*/
		
		public String getSearchURI() {
			return searchURI;
		}

		/*
		public void setSearchURI(String searchURI) {
			this.searchURI = searchURI;
		}
		*/
		
		public String getSearchTitle() {
			return searchTitle;
		}

		public void setSearchTitle(String searchTitle) {
			this.searchTitle = searchTitle;
		}
		
		public String getPaging(int start, int last) {
			return getPaging(page, start, last, pageSize);
		}
		
		public String printUploadForm(String uri, DBProtocol protocol, update_mode mode) throws Exception {
			return printUploadForm("",uri, protocol,mode);
		}
		public String printUploadForm(String action, String uri, DBProtocol protocol,update_mode mode) throws Exception {

				StringBuilder content = new StringBuilder();
				String form = null;
				String header = null;
				String hint = "<p>Upload a QMRF XML file, complying to <a href='http://qmrf.sf.net/qmrf.dtd' target='help'>QMRF DTD</a> schema. The QMRF Editor can be downloaded from <a href='http://qmrf.sf.net' target='help'>http://qmrf.sf.net</a>. </p> ";
				switch (mode) {
				case attachments: {
					header = String.format("%s to <a href='%s' target='_blank'>%s</a>",mode.toString(),protocol.getResourceURL(),protocol.getIdentifier());
					form = String.format("<form method='%s' action=\"%s\" ENCTYPE=\"multipart/form-data\">","POST",action);
					hint = "<p>All files are optional, you could select any combination of attachment type(s) to upload.</p>";
					break;
				}
				case update : {
					header = String.format("%s QMRF XML file of <a href='%s' target='_blank'>%s</a>",mode.toString(),protocol.getResourceURL(),protocol.getIdentifier());
					form = String.format("<form method='%s' action=\"%s\" ENCTYPE=\"multipart/form-data\">","PUT",protocol.getResourceURL());
					break;
				}
				case newdocument : {
					
					form = String.format("<form method='%s' action=\"%s\" ENCTYPE=\"multipart/form-data\">","POST",action);
					header = String.format("%s %s %s",mode.toString(),"QMRF document",uri.toString().contains("versions")?" version":"");
					break;
				}
				}
				
				content.append(printWidgetHeader(header));
				content.append(printWidgetContentHeader(""));
				content.append(form);
				content.append(_tableStart);
				content.append(_trStart);
				
				try {
					content.append(String.format("<p><input type='hidden' name='%s' title='%s' value='%s' size=\"30\"></p>",
							ReadProtocol.fields.user_uri.name(),"Owner",protocol==null?"":protocol.getOwner()==null?"":protocol.getOwner().getResourceURL()));
					content.append(String.format(
							"<p><input type='hidden' name='%s' title='%s' value='%s' size=\"30\"></p>",
							ReadProtocol.fields.organisation_uri.name(),"Organisation",protocol==null?"":protocol.getOrganisation()==null?"":protocol.getOrganisation().getResourceURL()));
					content.append(String.format("<p><input type='hidden' name='%s' title='%s' value='%s' size=\"30\"></p>",
							ReadProtocol.fields.project_uri.name(),"Project",protocol==null?"":protocol.getProject()==null?"":protocol.getProject().getResourceURL()));
					} catch (Exception x) {x.printStackTrace(); /*ok, no defaults if anything goes wrong */ }	
				//The XMLf
				content.append(hint);
				
				switch (mode) {
				case attachments: {
					for (attachment_type atype: attachment_type.values()) {
						if (atype.ordinal() % 2 ==0) {
							content.append(_trEnd);
							content.append(_trStart);
						}
						String title= String.format("Attachments: %s(s) - %s, %s files max", atype.toString(),atype.getDescription(),atype.maxFiles());
						content.append(_tdStart);
						content.append(printWidget(title,
								String.format("<p><input type=\"file\"  class='multi' maxlength='%d' accept='%s' name=\"%s\" title='%s' size=\"30\"></p>",
										atype.maxFiles(),
										atype.acceptFormats(),
										atype.name(),
										title),"box"
								));
						content.append(_tdEnd);
					}
				
				} default: {
				
					content.append(_tdStart);
					content.append(printWidget(String.format("%s QMRF XML file ",update_mode.attachments.equals(mode)?update_mode.update:mode.toString()), 
						String.format("<p><input type=\"file\" class='multi max-1' accept='xml' name=\"%s\" title='%s' size=\"30\"></p>",
								ReadProtocol.fields.filename.name(),
								"QMRF XML"),"box"
						));
				
					content.append(_tdEnd);					
				}
				}
				content.append(_trEnd);
				content.append(_trStart);
				content.append("<td colspan='1'>");	
				content.append(printWidget("Options",
						String.format("<strong>%s</strong>%s",
						"Publish immediately",	ReadProtocol.fields.published.getHTMLField(protocol)),"box"
						));	
				content.append("</td></tr>"); // </tr> ne e li izlishno tuk?					
				content.append(_trEnd);
				content.append(_trStart);
				content.append("<td colspan='2' align='center'><input type='submit' id='submit' enabled='false' value='Upload'></td>");
//				content.append("<input type='submit' enabled='false' value='Submit'>");

				content.append(_trEnd);
				content.append(_tableEnd);
				content.append("</form>");
				content.append(printWidgetContentFooter());
				content.append(printWidgetFooter());
				return	content.toString();

		}	
		
}
