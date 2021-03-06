package net.idea.rest.protocol.resource.db;

import java.io.Writer;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.QMRF_HTMLBeauty.update_mode;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.resource.GroupQueryURIReporter;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.Request;
import org.restlet.data.ClientInfo;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;


public class ProtocolQueryHTMLReporter extends QMRFHTMLReporter<DBProtocol, IQueryRetrieval<DBProtocol>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;

	protected boolean paging = true;
	protected boolean details = true;
	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;

	public ProtocolQueryHTMLReporter() {
		this(null,true,false,true,false);
	}

	public ProtocolQueryHTMLReporter(Request request, boolean collapsed,boolean editable,boolean paging, boolean details) {
		super(request,collapsed,null,null);
		setTitle(!collapsed?null:"QMRF document");
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
		this.paging = !collapsed?false:paging;
		this.details = !collapsed?true:details;


	}	
	@Override
	protected boolean printAsTable() {
		return true;//collapsed;
	}
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new ProtocolQueryURIReporter<IQueryRetrieval<DBProtocol>>(request);
	}
	@Override
	protected void printPageNavigator(IQueryRetrieval<DBProtocol> query)
			throws Exception {
		if (paging)
			super.printPageNavigator(query);
	}
	
	
	@Override
	public Object processItem(DBProtocol item) throws AmbitException  {

		try {
			if ((item.getProject()!=null) && (item.getProject().getResourceURL()==null))
				item.getProject().setResourceURL(new URL(groupReporter.getURI((DBProject)item.getProject())));
			if ((item.getOrganisation()!=null) && (item.getOrganisation().getResourceURL()==null))
				item.getOrganisation().setResourceURL(new URL(groupReporter.getURI((DBOrganisation)item.getOrganisation())));
			if ((item.getOwner()!=null) && (item.getOwner().getResourceURL()==null))
				item.getOwner().setResourceURL(new URL(userReporter.getURI((DBUser)item.getOwner())));
							
			String uri = uriReporter.getURI(item);
			
			if (printAsTable())
				//printForm(output,uri,item,false);
				printTable(output, uri,item);
			else { 
				//printForm(output,uri,item,false);
				printForm(output, uri, item, false);

			}

		} catch (Exception x) {
			
		} finally {
			record++;
		}
		return null;
	}

	protected boolean isAdminOrEditor() {
		ClientInfo clientInfo = uriReporter.getRequest().getClientInfo();
		if (clientInfo==null) return false;
		
		return clientInfo.getRoles().indexOf(managerRole)>=0
			   || 
			   clientInfo.getRoles().indexOf(editorRole) >=0;

	}
	@Override
	protected void printTableHeader(Writer w) throws Exception {
		boolean isAdminOrEditor = isAdminOrEditor();
		w.write("<table width='100%'>\n");
		w.write(String.format("<tr>\n" +
				"<th></th>\n" +
				"<th class='contentTable'>%s</th>\n" +
				"<th class='contentTable'>%s</th>\n" +
				"<th class='contentTable'>%s</th>\n" +
				"<th class='contentTable'>%s</th>\n" +
				"<th class='contentTable'>%s</th>\n" +
				"<th class='contentTableManage' colspan='3'>%s</th>\n" +
				"</tr>\n",
				collapsed?"QMRF number":"",
				collapsed?"Title":"",
				collapsed?"Last updated":"",
				collapsed?"Download":"",
				collapsed?isAdminOrEditor?"Owner":"":"",
				collapsed?isAdminOrEditor?"Manage":"":""
		));
	}

	protected void printHTML(Writer output, String uri, DBProtocol item, boolean hidden) throws Exception {
		output.write(String.format("<div id='%s' class='documentheader' style='display: %s;''>",item.getIdentifier(),hidden?"none":"block"));
		if (!hidden) {
			output.write(String.format("<a href='%s'>%s</a>&nbsp;<textarea>%s</textarea>&nbsp;<br>%s",
						uri,ReadProtocol.fields.identifier.getValue(item),"XXX" 
						//item.getTitle()
						,printItemDownloadLinks(uri)));
		}
		output.write("<div class='accordion'>");
		//tabs
		output.write("</div>");

		output.write("</div>");
	}
	
	protected void printForm(Writer output, String uri, DBProtocol item, boolean hidden) {

		String qmrf_number = "";
		try {
			
			qmrf_number = 	String.format(
					"<div class='structureright'><a href='%s'>%s</a><br>%s\n</div>\n",
					uri,
					item.getIdentifier(),
					printItemDownloadLinks(uri)
					);
			

	        
			output.write(String.format(
			"<div id='%s' style='display: %s;'>\n", item.getIdentifier(), hidden?"none":"block"));

			// The social panel. Don't display on MSIE 7, because it breaks there. No surprise.
			
			//share only if published. Also helps with embedding into /editor page, otherwise VKontakte breaks everything 
			if (!((QMRF_HTMLBeauty)htmlBeauty).isMsie7() && item.isPublished()) {  
			
				output.write("<div class='socialPanel'><table class='socialTable'><tr><td class='socialTool'>\n"); // begin social

				output.write("</td><td class='socialTool'>"); // next cell

				output.write("</td><td class='socialTool'>"); // next cell

				output.write("</td><td class='socialTool'>"); // next cell

		
				output.write("</td><td class='socialToolVK'>"); // next cell, but VKontakte requires special styling
				
				output.write("</td></tr></table></div>\n"); // end social

			} // social IE7 if
								
			//output.write(String.format("<div class='tabs' id='%s_tabs'>\n",item.getIdentifier()));
			output.write(String.format("<div id='%s_tabs' class='tabs'>\n",item.getIdentifier())); //tabs
			
			// This will get replaced once AJAX kicks in.
			output.write("<div class='loading'>Please wait while QMRF document chapters are loading...</div>\n");
			
			output.write("\n</div>\n"); //tabs
			
			output.write(String.format("<div class='tabs2top'><a href='#toploc_%s'><span class='upArrow'>&Delta;</span>&nbsp;TOP&nbsp;<span class='upArrow'>&Delta;</span></a></div>",
						item.getIdentifier()));
			
			if (!hidden) {
				output.write(String.format(
						"<script>\n" +
						"$('#%s_tabs').load('%s/chapters?headless=true&media=text/html', function() {\n" +
						"\t$('#%s .tabs').tabs('destroy').tabs({cache: true});\n" +
						"});\n" +				
						"</script>",
						item.getIdentifier(),
						uri,
						item.getIdentifier(),
						item.getIdentifier()
				));
			}
		
		} catch (Exception x) {
			logger.log(Level.WARNING,x.getMessage(),x);
		} finally {
			
		}
	
	}
	
	@Override
	protected void printUploadForm(Writer output, String uri, DBProtocol protocol) {
		try {
			output.write(((QMRF_HTMLBeauty)htmlBeauty).printUploadForm("", uri, protocol,update_mode.attachments,uriReporter.getBaseReference().toString()));
		} catch (Exception x) {logger.log(Level.WARNING,x.getMessage(),x);} 
		
	}	
	
	protected String printItemDownloadLinks(String uri) throws Exception {
		StringBuilder b = new StringBuilder();
		MediaType[] mimes = {
				MediaType.APPLICATION_PDF,
				MediaType.APPLICATION_EXCEL,
				MediaType.APPLICATION_RTF,
				MediaType.APPLICATION_XML,
				null
				};
		
		String[] image = {
				"pdf.png",
				"excel.png",
				"word.png",
				"xml.png",
				"qmrf/attachments.png"
		};	
		
		String[] description = {
				"Download as PDF",
				"Download as MS Excel",
				"Download as Rich Text Format (RTF)",
				"Download as QMRF XML",
				"Browse attachments"
		};			
		for (int i=0;i<mimes.length-1;i++) {
			MediaType mime = mimes[i];
				
			b.append(String.format(
					"<a href=\"%s%s?media=%s\"><img src=\"%s/images/%s\" alt=\"%s\" title=\"%s\" border=\"0\"/></a>\n",
					uri,
					Resources.document,
					Reference.encode(mime.toString()),
					getUriReporter().getBaseReference().toString(),
					image[i],
					mime,
					description[i]
					));
		}
		/*
		b.append(String.format(
				"<a href=\"%s%s\"><img src=\"%s/images/%s\" title=\"%s\" border=\"0\"/></a>\n",
				uri,
				Resources.attachment,
				getUriReporter().getBaseReference().toString(),
				image[3],
				description[3]
				));		
				*/
		return b.toString();
	}
	protected void printTable(Writer output, String uri, DBProtocol item) {
		try {
			output.write("<tr>\n");	
			if (details & collapsed)
				output.write(String.format("<td id='%s_toggler' class='togglerPlus' title='Show/Hide details'>" +
						"<script>\n" +
						"$('#%s_toggler').one('click', function() { $('#%s_tabs').load('%s/chapters?headless=true&media=text/html'," +
						"	function() { $('#%s .tabs').tabs({cache: true}); }); });\n" +
						"$('#%s_toggler').click( function() { toggleDiv('%s'); });\n" +
						"</script>%s</td>",
						item.getIdentifier(), 
						item.getIdentifier(), 
						item.getIdentifier(), 
						uri,
						item.getIdentifier(),
						item.getIdentifier(),
						item.getIdentifier(),
						((QMRF_HTMLBeauty)htmlBeauty).isMsie7()?"<div>&nbsp;&nbsp;&nbsp;</div>":""));
			else 
				output.write("<td class='contentTable'></td>");
			/*
			String showProperties = headless?
					String.format("<form method='GET' action='%s'><input type='hidden' name='dataset' value='%s'><input type='submit' class='draw' value='*'></form>",
										uriReporter.getRequest().getResourceRef(),item.getIdentifier()):
					"";
					*/
			
			output.write(String.format("<td class='contentTable qmrfNumber' title='Show only this document'><a name='toploc_%s'></a><a href='%s'>%s</a>&nbsp;%s</td>",
						item.getIdentifier(),
						uri,
						item.getVisibleIdentifier(),
						""
			));			
			
			output.write(String.format("<td class='contentTable qmrfTitle'>%s</td>", item.getTitle()));
			output.write(String.format("<td class='contentTable qmrfDate'>%s</td>", simpleDateFormat.format(new Date(item.getTimeModified()))));
			output.write(String.format("<td class='contentTable qmrfDownloadLinks'>%s</td>", printItemDownloadLinks(uri)));
			
			String owner = !item.isPublished() || isAdminOrEditor()?
							String.format("%s %s",item.getOwner().getFirstname()==null?"":item.getOwner().getFirstname(),
												  item.getOwner().getLastname()==null?"":item.getOwner().getLastname()):"";
			
			output.write(String.format("<td class='contentTable qmrfOwner'>%s</td>", owner));
			

			/**
			 * 
			 */
			if (isAdminOrEditor()) {
				/**
				 * If published, don't show "publish" link
				 */
				output.write(String.format("<td class='contentTableManageL'>%s</td>",
						item.isPublished()?"":
						getPublishString(uriReporter.getRequest().getRootRef(), item)
				));		
				/**
				 * Published docs could be modified, but it results in creating a new version
				 */
				output.write(String.format(
						"<td class='contentTableManageM'>%s</td>" +
						"<td class='contentTableManageR'>%s</td>",
						item.isPublished()?
								getNewVersionString(uriReporter.getRequest().getRootRef(), item):
								getUpdateString(uriReporter.getRequest().getRootRef(), item),
						getDeleteString(uriReporter.getRequest().getRootRef(), item)
				));
			} else {
				/**
				 * 	 regular users should not modify anything.
				 */
 				output.write(String.format("<td class='contentTableManageL'></td>" +
						"<td class='contentTableManageM'></td>" +
						"<td class='contentTableManageR'></td>"
				));
			}
			
			output.write("</tr>\n");

			if (details) {
				output.write("<tr><td colspan='9'>\n");
				//printHTML(output, uri, item, true);
				printForm(output,uri,item,collapsed);
				output.write("</td></tr>\n");
			}

		} catch (Exception x) {
			logger.log(Level.WARNING,x.getMessage(),x);
		} 
	}

	protected String getPublishString(Reference baseRef, DBProtocol item) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<form action='%s%s/%s' method='GET'>");
		stringBuilder.append("<input type='hidden' name='mode' value='%s'>");
		stringBuilder.append("<input title='Publish this document' class='draw' "); // cont'd
		stringBuilder.append("type='image' src='%s/images/script_add.png' value='Publish'>");
		stringBuilder.append("</form>");
		
		return String.format(stringBuilder.toString(), baseRef, Resources.editor, item.getIdentifier(), update_mode.publish.name(), baseRef);
	}	
	
	protected String getDeleteString(Reference baseRef, DBProtocol item) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<form action='%s%s/%s' method='GET'>");
		stringBuilder.append("<input type='hidden' name='mode' value='%s'>");
		stringBuilder.append("<input title='Delete this document' class='draw' "); // cont'd
		stringBuilder.append("type='image' src='%s/images/script_delete.png' value='Delete'>");
		stringBuilder.append("</form>");
		
		return String.format(stringBuilder.toString(), baseRef, Resources.editor, item.getIdentifier(), update_mode.delete.name(), baseRef);
	}
	
	protected String getUpdateString(Reference baseRef, DBProtocol item) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<form action='%s%s/%s' method='GET'>");
		stringBuilder.append("<input type='hidden' name='mode' value='%s'>");
		stringBuilder.append("<input title='Update this document' class='draw' "); // cont'd
		stringBuilder.append("type='image' src='%s/images/script_edit.png' value='Update'>");
		stringBuilder.append("</form>");
		
		return String.format(stringBuilder.toString(), baseRef, Resources.editor, item.getIdentifier(), update_mode.update.name(), baseRef);
	}		
	protected String getNewVersionString(Reference baseRef, DBProtocol item) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<form action='%s%s/%s' method='GET'>");
		stringBuilder.append("<input type='hidden' name='mode' value='%s'>");
		stringBuilder.append("<input title='Create new version of this document' class='draw' "); // cont'd
		stringBuilder.append("type='image' src='%s/images/script_edit.png' value='New version'>");
		stringBuilder.append("</form>");
		
		return String.format(stringBuilder.toString(), baseRef, Resources.editor, item.getIdentifier(), update_mode.newversion.name(), baseRef);
	}	
	
}
