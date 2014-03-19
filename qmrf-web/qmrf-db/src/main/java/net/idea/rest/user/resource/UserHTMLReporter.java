package net.idea.rest.user.resource;

import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import net.idea.modbcum.i.IQueryCondition;
import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.p.DefaultAmbitProcessor;
import net.idea.modbcum.p.MasterDetailsProcessor;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.UserHTMLBeauty;
import net.idea.rest.user.QMRFUser;
import net.idea.rest.user.ReadRegistrationStatus;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.db.ReadOrganisation;
import net.idea.restnet.groups.resource.GroupQueryURIReporter;
import net.idea.restnet.u.UserRegistration;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;
import net.toxbank.client.resource.Organisation;

import org.restlet.Request;

public class UserHTMLReporter extends QMRFHTMLReporter<DBUser, IQueryRetrieval<DBUser>> {
	protected String usersdbname;
	private static final long serialVersionUID = -7959033048710547839L;
	DBUser.fields[] entryFields = DBUser.fields.values();
	protected GroupQueryURIReporter groupURIReporter ;
	final static DBUser.fields[] displayFields = {
		DBUser.fields.email,
		DBUser.fields.username,
		DBUser.fields.homepage,
		DBUser.fields.keywords,
		DBUser.fields.reviewer
		
	};
	
	public UserHTMLReporter(String searchURI,String usersdbname) {
		this(null, true, false, new UserHTMLBeauty(searchURI),usersdbname);
	}
	
	public UserHTMLReporter(Request request, boolean collapsed, boolean editable, UserHTMLBeauty htmlBeauty,String usersdbname) {
		super(request, collapsed, null, htmlBeauty);
		this.usersdbname = usersdbname;
		setTitle("User");
		getProcessors().clear();
		groupURIReporter = new GroupQueryURIReporter(request);
		IQueryRetrieval<DBOrganisation> queryO = new ReadOrganisation(new DBOrganisation()); 
		
		MasterDetailsProcessor<DBUser, DBOrganisation, IQueryCondition> orgReader = new MasterDetailsProcessor<DBUser, DBOrganisation, IQueryCondition>(queryO) {
			@Override
			protected DBUser processDetail(DBUser target, DBOrganisation detail) throws Exception {
				detail.setResourceURL(new URL(groupURIReporter.getURI(detail)));
				target.addOrganisation(detail);
				return target;
			}
		};
		getProcessors().add(orgReader);		
		if (usersdbname!=null) { 
			final IQueryRetrieval<UserRegistration> queryR = new ReadRegistrationStatus();
			((ReadRegistrationStatus)queryR).setDatabaseName(usersdbname);
			MasterDetailsProcessor<DBUser, UserRegistration, IQueryCondition> regReader = new MasterDetailsProcessor<DBUser, UserRegistration, IQueryCondition>(queryR) {
				@Override
				public DBUser process(DBUser target) throws Exception {
					if (target==null || target.getUserName()==null) return target;
					return super.process(target);
				}
				@Override
				protected DBUser processDetail(DBUser target, UserRegistration detail) throws Exception {
					if (detail!=null) {
						QMRFUser user = new QMRFUser();
						user.setEmail(target.getEmail());
						user.setID(target.getID());
						user.setFirstname(target.getFirstname());
						user.setLastname(target.getLastname());
						user.setKeywords(target.getKeywords());
						user.setReviewer(target.isReviewer());
						user.setTitle(target.getTitle());
						user.setHomepage(target.getHomepage());
						user.setOrganisations(target.getOrganisations());
						user.setRegisteredAt(detail.getTimestamp_created());
						user.setRegistrationStatus(detail.getStatus());
						user.setUserName(target.getUserName());
						return user;
					} else 	
						return target;
				}
			};
			getProcessors().add(regReader);
		}


		processors.add(new DefaultAmbitProcessor<DBUser, DBUser>() {
			public DBUser process(DBUser target) throws AmbitException {
				processItem(target);
				return target;
			};
		});				
	}
	
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new UserURIReporter<IQueryRetrieval<DBUser>>(request);
	}
	
	@Override
	protected boolean printAsTable() {
		return collapsed;
	}

	@Override
	protected void printPageNavigator(IQueryRetrieval<DBUser> query)
		throws Exception {
		// print nothing, DataTables have their own paging
	}
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		if (printAsTable()) {
			output.write("<div style='float:left; width:100%; align:center'>\n");
			output.write("<table class='datatable' cellpadding='0' border='0' width='100%' cellspacing='0'>\n");
			//output.write("<caption><h3>Users</h3></caption>\n");	
			output.write("<thead>\n");	
			output.write(String.format("<th>%s</th>", "Name"));
			output.write(String.format("<th>%s</th>", "Affiliation"));
			output.write(String.format("<th>%s</th>", "E-mail"));
			output.write(String.format("<th>%s</th>", "Keywords"));
			output.write(String.format("<th>%s</th>", "Reviewer"));
			if (usersdbname!=null)
			output.write(String.format("<th>%s</th>", "Registration status"));
			output.write("</thead>\n");
			output.write("<tbody>\n");
		}
	}
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBUser> query) {
		try {
			if (printAsTable()) {
				output.write("</tbody></table></div>\n");	
			}
		} catch (Exception x) {}
		if (!headless)
			super.footer(output, query);
	}
	
	@Override
	protected void printForm(Writer output, String uri, DBUser user, boolean editable) {
		try {
			StringBuilder rendering = new StringBuilder();
			
			String protocolURI = String.format(
					"<a href=\"%s%s/U%d%s?headless=true&details=false&media=text/html\" title=\"QMRF documents\">QMRF documents</a>",
					uriReporter.getRequest().getRootRef(),
					Resources.user,
					user.getID(),
					Resources.protocol);
			
			String alertURI = String.format(
					"<a href=\"%s%s/U%d%s?headless=true&details=false&media=text/html\" title=\"alerts\">Saved searches</a>",
					uriReporter.getRequest().getRootRef(),
					Resources.user,
					user.getID(),
					Resources.alert);

			
			// tab headers
			final String tabHeaders =
				"<div class='protocol'>\n"+					
				"<div class='tabs'>\n"+
				"<ul>\n"+
				"<li><a href='#tabs-id'>%s</a></li>\n"+
				"<li>%s<span></span></li>\n"+
				"<li>%s<span></span></li>\n"+
				"</ul>\n";
			rendering.append(String.format(tabHeaders, "User Profile", protocolURI,alertURI));
				
			// identifiers
			rendering.append("<div id='tabs-id'><span class='summary'>");
			rendering.append("<table width='80%%'>");
			rendering.append(String.format("<tr><th colspan='2'><a href='%s%s/U%d'>%s&nbsp;%s&nbsp%s</a></th></tr>",
							uriReporter.getRequest().getRootRef(),Resources.user,user.getID(),
							user.getTitle()==null?"":user.getTitle().trim(),
							user.getFirstname()==null?"":user.getFirstname().trim(),
							user.getLastname()==null?"":user.getLastname().trim()));
			
			if (user.getOrganisations()!=null)
			for (Organisation org : user.getOrganisations())
				rendering.append(String.format("<tr><th>%s</th><td align='left'>%s</td></tr>", "Affiliation", 
						org.getTitle()));					
			
			for (DBUser.fields field : displayFields) {
					rendering.append(String.format("<tr><th>%s</th><td align='left'>%s</td></tr>", 
							field.toString(),
							field.getValue(user)==null?"":field.getValue(user)
					));
			}		
	
			rendering.append("</table>");
			rendering.append("</span></div>");
			
			rendering.append(String.format("<div id='QMRF_documents'>%s</div>",protocolURI));
			rendering.append(String.format("<div id='alerts'>%s</div>",alertURI));

			rendering.append("</div>\n</div>\n"); // tabs, protocol

			output.write(rendering.toString());
		} catch (Exception x) {logger.log(Level.WARNING,x.getMessage(),x);} 
	}	

	@Override
	protected void printTable(Writer output, String uri, DBUser user) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");	
			output.write(renderItem(user));			
			output.write("</tr>\n");
		} catch (Exception x) {
			logger.log(Level.WARNING,x.getMessage(),x);
		} 
	}
	
	public String renderItem(DBUser item) {
		StringBuilder rendering = new StringBuilder();

		rendering.append(String.format("<td>%s %s %s %s</td>",
			item.getTitle()==null?"":item.getTitle(),
			item.getFirstname(),
			item.getLastname(),
			item.getID()<=0?"":
			String.format("<br>[<a href='%s%s/U%d'>%s</a>]",
				uriReporter.getBaseReference(),
				Resources.user,
				item.getID(),
				(item.getUserName()==null)?("U"+item.getID()):item.getUserName()
			)
		));
		
		rendering.append(String.format("<td>%s%s</td>",
			item.getOrganisations()==null?"":item.getOrganisations().get(0).getTitle(),
			item.getHomepage()==null?"":
				String.format("<br><a href='%s'>%s</a>",
					item.getHomepage(),
					item.getHomepage()
				)							
		));

		rendering.append(String.format("<td>%s</td>",
			item.getEmail()==null?"":
				String.format("<a href='mailto:%s'>%s</a>",URLEncoder.encode(item.getEmail()), item.getEmail())
		));
		rendering.append(String.format("<td>%s</td>", item.getKeywords()==null?"":item.getKeywords()));
		rendering.append(String.format("<td>%s</td>", item.isReviewer()?"Yes":""));
		
		if (usersdbname!=null) {
			rendering.append("<td>");
			if (item instanceof QMRFUser) rendering.append(((QMRFUser)item).getRegistrationStatus());
			rendering.append("</td>");					
		}
		rendering.append("</tr>");
		
		
		return rendering.toString();
	}
	
	
}
