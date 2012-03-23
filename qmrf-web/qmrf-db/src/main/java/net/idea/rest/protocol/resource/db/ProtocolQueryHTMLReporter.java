package net.idea.rest.protocol.resource.db;

import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.qmrf.client.QMRFRoles;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.converters.QMRF_xml2html;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.DBProject;
import net.idea.rest.groups.IDBGroup;
import net.idea.rest.groups.resource.GroupQueryURIReporter;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.QMRF_HTMLBeauty.update_mode;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.resource.UserURIReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.ClientInfo;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.security.Role;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class ProtocolQueryHTMLReporter extends QMRFHTMLReporter<DBProtocol, IQueryRetrieval<DBProtocol>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	protected final static String[][] chapters = {
		//	{"QSAR identifier","Identifier"},
			{"General information","General"},
			{"Defining the endpoint - OECD Principle 1","Endpoint"},
			{"Defining the algorithm - OECD Principle 2","Algorithm"},
			{"Defining the applicability domain - OECD Principle 3","App. domain"},
			{"Internal validation - OECD Principle 4","Robustness"},
			{"External validation - OECD Principle 4","Predictivity"},
			{"Providing a mechanistic interpretation - OECD Principle 5","Interpretation"},
			{"Miscellaneous information (comments, bibliography)","Bibliography"},
	};	
	protected boolean paging = true;
	protected boolean details = true;
	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;

	protected DocumentBuilder builder;
	protected DocumentBuilderFactory factory;
	protected QMRF_xml2html qhtml;
	protected EntityResolver dtdresolver;
	public EntityResolver getDtdresolver() {
		return dtdresolver;
	}
	public void setDtdresolver(EntityResolver dtdresolver) {
		this.dtdresolver = dtdresolver;
	}
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

	protected DOMSource getDOMSource(DBProtocol item) throws Exception {
		  String xml = item.getAbstract();
		  if (factory==null) {
			    factory = DocumentBuilderFactory.newInstance();
		       // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		        factory.setValidating(true);
		  }
		   if (builder==null) {
			   builder = factory.newDocumentBuilder();
			   builder.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					
				}
				
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					
				}
				
				@Override
				public void error(SAXParseException exception) throws SAXException {
					
				}
			});
			   builder.setEntityResolver(dtdresolver);
		   }
		   Document xmlDocument = builder.parse( new InputSource(new StringReader(xml)));
		   
		   return new DOMSource(xmlDocument);
	}
	protected void printHTML(Writer output, String uri, DBProtocol item, boolean hidden) throws Exception {
		output.write(String.format("<div id='%s' class='documentheader' style='display: %s;''>",item.getIdentifier(),hidden?"none":""));
		if (!hidden) {
			output.write(String.format("<a href='%s'>%s</a>&nbsp;<label>%s</label>&nbsp;<br>%s",
						uri,ReadProtocol.fields.identifier.getValue(item),item.getTitle(),printDownloadLinks(uri)));
		}
		output.write("<div class='accordion'>");
		if (qhtml==null) qhtml = new QMRF_xml2html();
        DOMSource source = getDOMSource(item);
        qhtml.xml2summary(source,output);
		output.write("</div>");

		output.write("</div>");
	}
	
	protected void printForm(Writer output, String uri, DBProtocol item, boolean hidden) {
		String attachmentURI = String.format(
				"<a href=\"%s%s?headless=true&media=text/html\" title=\"Attachments\">Attachments</a>",
				uri,Resources.attachment);
		
		String qmrf_number = "";
		try {
			qmrf_number = 	String.format(
					"<div class='structureright'><a href='%s'>%s</a><br>%s\n</div>\n",
					uri,
					item.getIdentifier(),
					printDownloadLinks(uri)
					);
			
			if (qhtml==null) qhtml = new QMRF_xml2html();
	        
			output.write(String.format(
			"<div id='%s' style='display: %s;'>\n", item.getIdentifier(), hidden?"none":""));

			// The social panel. Don't display on MSIE 7, because it breaks there. No surprise.
			
			if (!((QMRF_HTMLBeauty)htmlBeauty).isMsie7()) {
			
				output.write("<div class='socialPanel'><table class='socialTable'><tr><td class='socialTool'>\n"); // begin social

				// Google +1
				output.write(String.format("<g:plusone href='%s'></g:plusone>\n", uri));

				output.write("</td><td class='socialTool'>"); // next cell

				// Facebook
				output.write(String.format("<div class='fb-like' data-href='%s' data-send='true' data-layout='button_count' " +
							"data-show-faces='false' data-action='recommend' data-font='tahoma'></div>\n", uri));

				output.write("</td><td class='socialTool'>"); // next cell

				// Twitter
				output.write(String.format("<a href='https://twitter.com/share' class='twitter-share-button'" +
							"data-url='%s' data-text='%s' data-hashtags='qmrf' " +
							"data-related='EC_JRC_IHCPnews'>Tweet</a>\n",
							uri, item.getIdentifier()
				));
			
				output.write("</td><td class='socialTool'>"); // next cell

				// LinkedIn
				output.write(String.format("<script type='IN/Share' data-url='%s' data-counter='right' data-showzero='true'></script>\n", uri));
			
				output.write("</td></tr></table></div>\n"); // end social

			} // social IE7 if
								
			output.write("<div class='tabs'>\n");
			
			String baseRef = uriReporter.getBaseReference().toString();

			String imgstyle = "style='float:left; margin-left:5px; margin-top:5px; margin-right:0px;'";
			output.write("<ul>\n");
			for (int i=0; i < chapters.length; i++) {
				output.write(String.format(
				"<li><img src='%s/images/qmrf/chapter%d.png' %s><a style='margin-left:0px;' href='#tabs-%d' title='%d.%s'>%s</a></li>",
				baseRef,
				(i+2),
				imgstyle,
				(i+2),
				(i+2),
				chapters[i][0],
				chapters[i][1]
				));
			}
			output.write(String.format("<li><img src='%s/images/qmrf/attachments.png' %s> %s<span></span></li>",
					baseRef,imgstyle,attachmentURI));
			output.write("</ul>\n");
			long now = System.currentTimeMillis();
			try {
				qhtml.xml2summary(getDOMSource(item),output);
			} catch (Exception x) {
				x.printStackTrace();
			}
			System.err.println(System.currentTimeMillis()-now);
			String uploadUI = String.format("<a href='%s%s/%s' target='upload' title='Upload training and test datasets and related documents''>%s</a>",
					uriReporter.getBaseReference(),Resources.editor,item.getIdentifier(),"Add attachment(s)");
			output.write(String.format("<div id='Attachments'><span class='summary'>N/A<br>%s</span></div>",uploadUI));
			
			output.write("\n</div>\n"); //tabs
			output.write("</div>\n"); // protocol
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			
		}
	
	}
	
	@Override
	protected void printUploadForm(Writer output, String uri, DBProtocol protocol) {
		try {
			output.write(((QMRF_HTMLBeauty)htmlBeauty).printUploadForm("", uri, protocol,update_mode.attachments));
		} catch (Exception x) {x.printStackTrace();} 
		
	}	
	
	protected String printDownloadLinks(String uri) throws Exception {
		StringBuilder b = new StringBuilder();
		MediaType[] mimes = {
				MediaType.APPLICATION_PDF,
				MediaType.APPLICATION_EXCEL,
				MediaType.APPLICATION_XML,
				null
				};
		
		String[] image = {
				"pdf.png",
				"excel.png",
				"xml.png",
				"qmrf/attachments.png"
		};	
		
		String[] description = {
				"Download as PDF",
				"Download as MS Excel",
				"Download as QMRF XML",
				"Browse attachments"
		};			
		for (int i=0;i<mimes.length-1;i++) {
			MediaType mime = mimes[i];
				
			b.append(String.format(
					"<a href=\"%s?media=%s\" target='_blank'><img src=\"%s/images/%s\" alt=\"%s\" title=\"%s\" border=\"0\"/></a>\n",
					uri,
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
				output.write(String.format("<td id='%s_toggler' class='togglerPlus' onClick=\"javascript:toggleDiv('%s');\">%s</td>",
						item.getIdentifier(), item.getIdentifier(),
						((QMRF_HTMLBeauty)htmlBeauty).isMsie7()?"<div>&nbsp;&nbsp;&nbsp;</div>":""));
			else 
				output.write("<td class='contentTable'></td>");
			/*
			String showProperties = headless?
					String.format("<form method='GET' action='%s'><input type='hidden' name='dataset' value='%s'><input type='submit' class='draw' value='*'></form>",
										uriReporter.getRequest().getResourceRef(),item.getIdentifier()):
					"";
					*/
			
			output.write(String.format("<td class='contentTable qmrfNumber'><a href='%s'>%s</a>&nbsp;%s%s</td>",
						uri,
						ReadProtocol.fields.identifier.getValue(item),
						"",
						((QMRF_HTMLBeauty)htmlBeauty).isMsie7()?"<div></div>":""
			));			
			
			output.write(String.format("<td class='contentTable'>%s</td>",item.getTitle()));
			output.write(String.format("<td class='contentTable'>%s</td>",simpleDateFormat.format(new Date(item.getTimeModified()))));
			output.write(String.format("<td class='contentTable'>%s</td>",printDownloadLinks(uri)));
			
			String owner = !item.isPublished() || isAdminOrEditor()?
							String.format("%s %s",item.getOwner().getFirstname(),item.getOwner().getLastname()):"";
			
			output.write(String.format("<td class='contentTable'>%s</td>", owner));
			
			if (!item.isPublished()) {
				output.write(String.format("<td class='contentTableManageL'>%s</td>" +
						"<td class='contentTableManageM'>%s</td>" +
						"<td class='contentTableManageR'>%s</td>",
						getPublishString(uriReporter.getRequest().getRootRef(), uri),
						getUpdateString(uriReporter.getRequest().getRootRef(), item),
						getDeleteString(uriReporter.getRequest().getRootRef(), uri)
				));
			} else {
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
			x.printStackTrace();
		} 
	}

	protected String getPublishString(Reference baseRef, String uri) {
		return
		String.format("<form action='%s?method=PUT' method='POST' ENCTYPE=\"multipart/form-data\">" +
				"<input  type='hidden' name='published' value='true'/>" +
				"<input  title='This document is NOT published' class='draw' " +
				"type='image' src='%s/images/script_add.png' value='Publish'></form>",
				uri,
				baseRef
		);
	}
	
	protected String getDeleteString(Reference baseRef, String uri) {
		return
		String.format("<form action='%s?method=DELETE' method='POST' ENCTYPE=\"multipart/form-data\">" +
				"<input  type='hidden' name='published' value='true'/>" +
				"<input  title='Delete document' class='draw'" +
				"type='image' src='%s/images/script_delete.png' value='Delete'></form>",
				uri,
				baseRef);
	}	

	protected String getUpdateString(Reference baseRef, DBProtocol item) {
		return
		String.format("<a href='%s%s/%s?mode=%s' target='upload'><img %s src='%s/images/script_edit.png' title='%s'></a>",
				baseRef,
				Resources.editor,
				item.getIdentifier(),
				update_mode.update.name(),
				((QMRF_HTMLBeauty)htmlBeauty).isMsie7()?"class='ieSux'":"",
				baseRef,
				"Update");
	}		
}
