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
import net.idea.qmrf.converters.QMRF_xml2html;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.DBProject;
import net.idea.rest.groups.IDBGroup;
import net.idea.rest.groups.resource.GroupQueryURIReporter;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.resource.UserURIReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class ProtocolQueryHTMLReporter extends QMRFHTMLReporter<DBProtocol, IQueryRetrieval<DBProtocol>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	protected boolean paging = true;
	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;
	protected DocumentBuilder builder;
	protected DocumentBuilderFactory factory;
	protected QMRF_xml2html qhtml;
	
	public ProtocolQueryHTMLReporter() {
		this(null,true,false,true);
	}
	public ProtocolQueryHTMLReporter(Request request, boolean collapsed,boolean editable,boolean paging) {
		super(request,collapsed,editable);
		setTitle(!collapsed?null:"QMRF document");
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
		this.paging = !collapsed?false:paging;
		
	}
	@Override
	protected boolean printAsTable() {
		return collapsed;
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
				printHTML(output, uri, item, false);

			}

		} catch (Exception x) {
			
		} finally {
			record++;
		}
		return null;
	}
	@Override
	protected void printTableHeader(Writer w) throws Exception {
		w.write("<table width='100%'>\n");
		w.write("<tr><th width='5%'>#</th><th width='10%'>QMRF number</th><th colspan='2'>Title</th><th width='10%'>Published</th><th width='10%'>Download</th></tr>");			
		
	}
	protected DOMSource getDOMSource(DBProtocol item) throws Exception {
		  String xml = item.getAbstract().replace("&lt;html&gt;","").replace("&lt;/html&gt;","")
   		.replace("&lt;body&gt;","").replace("&lt;/body&gt;","")
   		.replace("&lt;head&gt;","").replace("&lt;/head&gt;","").replace("&#13;","\n")
   		.replace("&lt;p style=\"margin-top: 0\"&gt;","").replace("&lt;/p&gt;","<br/>");
		  if (factory==null) {
			    factory = DocumentBuilderFactory.newInstance();
		        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		        factory.setValidating(false);
		  }
		   if (builder==null) builder = factory.newDocumentBuilder();
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
        qhtml.xml2html(source,output);
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
					printDownloadLinks(uri)
					);
			
			if (qhtml==null) qhtml = new QMRF_xml2html();

	        
	        
			output.write(String.format(
			"<div id='%s' class='protocol'  style='display: %s;''>\n"+					
			"<div class='tabs'>\n"+
			"<ul>"+
			//"<li><a href='#tabs-id'>%s</a></li>"+
			"<li><a href='#tabs-3'>Endpoint</a></li>"+
			"<li><a href='#tabs-4'>Algorithm</a></li>"+
			"<li><a href='#tabs-5'>App. domain</a></li>"+
			"<li><a href='#tabs-6'>Robustness</a></li>"+
			"<li><a href='#tabs-7'>Predictivity</a></li>"+
			"<li><a href='#tabs-8'>Interpretation</a></li>"+
			"<li><a href='#tabs-9'>Misc</a></li>"+
			"<li><a href='#tabs-dataset'>Download</a></li>"+
			
			"</ul>",item.getIdentifier(),hidden?"none":""));
	
			qhtml.xml2summary(getDOMSource(item),output);
		
		} catch (Exception x) {
			x.printStackTrace();
		} 
		
		try {
		//	output.write("</div>");
					
			output.write(String.format(
			"<div id='tabs-dataset'>"+
			"%s<table>%s</table>\n"+
			"</div>\n" + //tabs-qmrf
			"</div>\n" + //tabs
			"</div>\n", //protocol
			qmrf_number,
			"datasets"
			)
			);
		} catch (Exception x) {
			x.printStackTrace();
		} 
	}
	/*
	protected void printForm(Writer output, String uri, DBProtocol protocol, boolean editable) {
		try {
			ReadProtocol.fields[] fields = editable?ReadProtocol.entryFields:ReadProtocol.displayFields;
			for (ReadProtocol.fields field : fields) {
				output.write("<tr bgcolor='FFFFFF'>\n");	
				Object value = null;
				
				try { value = protocol==null?field.getExampleValue(uri):field.getValue(protocol);} catch (Exception x) {}

				if (editable) {
					value = field.getHTMLField(protocol);
				} else 
					if (value==null) value = "";
							
				switch (field) {
				case idprotocol: {
					if (!editable)
						output.write(String.format("<th title='%s'>%s</th><td align='left'><a href='%s'>%s</a></td><td align='left'></td>\n",
							field.name(),	
							field.toString(),
							uri,
							uri));		
					break;
				}	
				case updated: {
					output.write(String.format("<th title='%s'>%s</th><td align='left'>%s</td><td align='left'></td>\n",
						field.name(),	
						field.toString(),
						protocol.getTimeModified()==null?"":simpleDateFormat.format(new Date(protocol.getTimeModified()))
						));		
					break;
				}					
				case filename: {
					if (editable)
					output.write(String.format("<th title='%s'>%s</th><td align='left'><input type=\"file\" name=\"%s\" title='%s' size=\"60\"></td><td align='left'></td>",
							field.name(),	
							field.toString(),
							field.name(),
							"PDF|MS Word file")); 					
					else 
						if ((protocol.getDocument()==null) || (protocol.getDocument().getResourceURL()==null))
							output.write(String.format("<th title='%s'>%s</th><td align='left'>N/A</td><td></td>",
									field.name(),	
									field.toString()));							
						else
						output.write(String.format("<th title='%s'>%s</th><td align='left'><a href='%s%s?media=%s'>Download</a></td><td></td>",
									field.name(),	
									field.toString(),
									uri,
									Resources.document,
									Reference.encode(MediaType.APPLICATION_ALL.toString())));

					break;
				}	
				case template: {
					if (editable)
					output.write(String.format("<th title='%s'>%s</th><td align='left'><input type=\"file\" name=\"%s\" title='%s' size=\"60\"></td><td align='left'></td>",
							field.name(),	
							field.toString(),
							field.name(),
							"ISA-TAB template")); 					
					else 
						if (protocol.getDataTemplate()==null)
							output.write(String.format("<th title='%s'>%s</th><td align='left'><a href='%s%s?media=text/html'>Create data template</a></td><td></td>",
									field.name(),	
									field.toString(),
									uri,
									Resources.datatemplate		
							));
							
						else
						output.write(String.format("<th title='%s'>%s</th><td align='left'><a href='%s%s?media=text/plain'>Download</a></td><td></td>",
									field.name(),	
									field.toString(),
									uri,
									Resources.datatemplate));

					break;
				}					
				case author_uri: {
					if (!editable) {
						output.write(String.format("<th>%s</th><td><a href='%s%s'>Authors</a></td>",
								field.toString(),uri,Resources.authors));
						break;
					}
				}
				case allowReadByGroup: {
					if (!editable) {
						output.write(String.format("<th>%s</th><td><a href='%s%s'>Allow read by</a></td>",
								field.toString(),uri,Resources.organisation));
						break;
					}
				}			
				case allowReadByUser: {
					if (!editable) {
						output.write(String.format("<th>%s</th><td><a href='%s%s'>Allow read by</a></td>",
								field.toString(),uri,Resources.user));
						break;
					}
				}		
				case published :  {
					String help = field.getHelp(uriReporter.getRequest().getRootRef().toString());
					output.write(String.format("<th>%s</th><td align='left'>%s</td><td align='left'>%s</td>\n",
									field.toString(),
									value,
									help==null?"":help));
					break;
				}				
				default :  {
					String help = field.getHelp(uriReporter.getRequest().getRootRef().toString());
					output.write(String.format("<th>%s</th><td align='left'>%s</td><td align='left'>%s</td>\n",
									field.toString(),
									value,
									help==null?"":help));
				}
				}
							
				output.write("</tr>\n");				
			}
			output.flush();
		} catch (Exception x) {x.printStackTrace();} 
	}	
	*/
	protected String printDownloadLinks(String uri) throws Exception {
		StringBuilder b = new StringBuilder();
		MediaType[] mimes = {
				MediaType.APPLICATION_PDF,
				MediaType.APPLICATION_XML,
				MediaType.APPLICATION_EXCEL
				};
		
		String[] image = {
				"pdf.png",
				"xml.png",
				"excel.png"
		};	
		
		String[] description = {
				"PDF",
				"QMRF XML",
				"MS Excel"
		};			
		for (int i=0;i<mimes.length;i++) {
			MediaType mime = mimes[i];
				
			b.append(String.format(
					"<a href=\"%s?media=%s\" target='_blank'><img src=\"%s/images/%s\" alt=\"%s\" title=\"%s\" border=\"0\"/></a>\n",
					uri,
					Reference.encode(mime.toString()),
					getUriReporter().getBaseReference().toString(),
					image[i],
					mime,
					String.format("Download as %s", description[i])
					));
		}
		return b.toString();
	}
	protected void printTable(Writer output, String uri, DBProtocol item) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");	
			output.write(String.format("<td>%d.</td>",record+1 ));
			output.write(String.format("<td width='15em'><a href='%s'>%s</a></td>",uri,ReadProtocol.fields.identifier.getValue(item)));			
			output.write(String.format("<td>%s</td><td>&nbsp;<a href=\"javascript:toggleDiv('%s');\" style=\"background-color: #fff; padding: 5px 10px;\">More</a></td>",item.getTitle(),item.getIdentifier()));
			output.write(String.format("<td width='8em'>%s</td>",simpleDateFormat.format(new Date(item.getTimeModified()))));
			output.write(String.format("<td width='50px'>%s</td>",printDownloadLinks(uri)));
			output.write("</tr>\n");

			if (collapsed) {
				output.write("<tr bgcolor='FFFFFF'><td colspan='5'>\n");
				//printHTML(output, uri, item, true);
				printForm(output,uri,item,true);
				output.write("</td></tr>\n");
			}

		} catch (Exception x) {
			x.printStackTrace();
		} 
	}

}
