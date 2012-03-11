package net.idea.rest.user.resource;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.user.DBUser;
import net.idea.restnet.c.AbstractResource;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;

public class UserHTMLReporter extends QMRFHTMLReporter<DBUser, IQueryRetrieval<DBUser>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	DBUser.fields[] entryFields = DBUser.fields.values();
	DBUser.fields[] displayFields = DBUser.fields.values();
	
	public UserHTMLReporter() {
		this(null,true,false,new UserHTMLBeauty());
	}
	public UserHTMLReporter(Request baseRef, boolean collapsed,boolean editable,UserHTMLBeauty htmlBeauty) {
		super(baseRef,collapsed,null,htmlBeauty);
		setTitle("User");
	}
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new UserURIReporter<IQueryRetrieval<DBUser>>(request);
	}
	@Override
	protected boolean printAsTable() {
		return true; //collapsed || !editable;
	}

	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		output.write("<table width='90%'>\n");

		output.write("<tr bgcolor='FFFFFF' >\n");	
		output.write(String.format("<th width='50%'>%s</th>","Name"));
		output.write(String.format("<th width='25%'>%s</th>",DBUser.fields.homepage.toString()));
		output.write(String.format("<th width='25%'>%s</th>","Documents"));
		output.write("</tr>\n");
		
	}

	@Override
	protected void printForm(Writer output, String uri, DBUser user, boolean editable) {
		try {
			
			DBUser.fields[] fields = editable?entryFields:displayFields;
			for (DBUser.fields field : fields) {
				output.write("<tr bgcolor='FFFFFF'>\n");	
				Object value = user==null?null:field.getValue(user);

				if (editable) {
					value = field.getHTMLField(user);
				} else 
					if (value==null) value = "";
							
				switch (field) {
				case iduser: {
					if (!editable)
						output.write(String.format("<th>%s</th><td align='left'><a href='%s'>%s</a></td>\n",
							field.toString(),
							uri,
							uri));		
					break;
				}	
				default :
					output.write(String.format("<th>%s</th><td align='left'>%s</td>\n",
						field.toString(),value));
				}
							
				output.write("</tr>\n");				
			}
			output.write("<tr bgcolor='FFFFFF'>\n");
			output.write("</tr>\n");
			output.flush();
		} catch (Exception x) {x.printStackTrace();} 
	}	
	@Override
	protected void printTable(Writer output, String uri, DBUser user) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");	
			output.write(String.format("<td>%s</td>",renderItem(user)));			
			output.write("</tr>\n");
		} catch (Exception x) {
			x.printStackTrace();
		} 
	}
	
	
	public String renderItem(DBUser item) {
		
		String protocolURI = String.format(
				"<a href=\"%s%s/U%d%s?headless=true&details=false&media=text/html\" title=\"QMRF documents\">QMRF documents</a>",
				uriReporter.getRequest().getRootRef(),
				Resources.user,
				item.getID(),
				Resources.protocol);
		
		String orgURI = String.format(
				"<a href=\"%s%s/U%d%s?headless=true&details=false&media=text/html\" title=\"Affiliation\">Affiliation</a>",
				uriReporter.getRequest().getRootRef(),
				Resources.user,
				item.getID(),
				Resources.organisation);
		
		StringBuilder rendering = new StringBuilder();
			//tab headers
			rendering.append(String.format(
			"<div class='protocol'>\n"+					
			"<div class='tabs'>\n"+
			"<ul>"+
			"<li><a href='#tabs-id'>%s&nbsp;%s&nbsp%s</a></li>"+
			"<li>%s<span></span></li>"+
			"<li>%s<span></span></li>"+
			"</ul>",
			item.getTitle(),item.getFirstname(),item.getLastname(),
			orgURI,
			protocolURI
			));
			//identifiers
			rendering.append(String.format(
			"<div id='tabs-id'>"+
			"<span class='summary'><table width='80%%'>\n"+ 
			"<tr><th colspan='2'><a href='%s%s/U%d'>%s&nbsp;%s&nbsp%s</a></th></tr>"+
			"<tr><td>%s</td><th align='left'>%s</th></tr>"+
			"<tr><td width='10%%'>%s</td><th align='left'>%s</th></tr>"+
			"</table></span>"+
			"</div>",
			uriReporter.getRequest().getRootRef(),Resources.user,item.getID(),
			item.getTitle().trim(),item.getFirstname().trim(),item.getLastname().trim(),
			item.getUserName()==null?"":"Username:&nbsp;",item.getUserName()==null?"":item.getUserName(),
			item.getHomepage()==null?"":"WWW:&nbsp;",
			item.getHomepage()==null?"":String.format("<a href='%s'>%s</a>",item.getHomepage(),item.getHomepage())					
			));
			rendering.append(String.format("<div id='Affiliation'>%s</div>",orgURI));				
			rendering.append(String.format("<div id='QMRF_documents'>%s</div>",protocolURI));

			rendering.append("</div>\n</div>\n");//tabs , protocol

			return rendering.toString();
		

	}

}

class UserHTMLBeauty extends QMRF_HTMLBeauty {
	
	public UserHTMLBeauty() {
		super();
		setSearchTitle("Search for users and authors");
		setSearchURI(Resources.user);
	}
	@Override
	protected String searchMenu(Reference baseReference,Form form)  {
		String pageSize = "10";
		String userNameQuery = "";
		try {
			if ((form != null) && (form.size()>0)) {
				searchQuery = form.getFirstValue(AbstractResource.search_param)==null?"":form.getFirstValue(AbstractResource.search_param);
				pageSize = form.getFirstValue("pagesize")==null?"10":form.getFirstValue("pagesize");
				userNameQuery = form.getFirstValue("username")==null?"":form.getFirstValue("username");
			}
		} catch (Exception x) {
			searchQuery = "";
			pageSize = "10";
		}
			return
		   String.format(		
		   "<div class='search ui-widget'>\n"+
		   "<p>%s</p>\n"+
		   "<form method='GET' action='%s%s?pagesize=10'>\n"+
		   "<table width='200px'>\n"+
		   "<tr><td colspan='2'><input type='text' name='search' size='20' value='%s' tabindex='0' title='Search by first or last name'></td></tr>\n"+
		   "<tr><td colspan='2'><input type='text' name='username' size='20' value='%s' tabindex='0' title='Search by user name'></td></tr>\n"+
		   "<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value='%s'></td></tr>\n"+
		   "<tr><td></td><td align='left'><input type='submit' tabindex='4'  value='Search'/></td></tr>\n"+
		   "</table>\n"+			   
		   "</form> \n"+
		   "&nbsp;\n"+
		   "</div>\n",

		   getSearchTitle(),
		   baseReference,
		   getSearchURI(),
		   searchQuery==null?"":searchQuery,
		   userNameQuery==null?"":userNameQuery,				   
		   pageSize
		  
		   );
	}
}
