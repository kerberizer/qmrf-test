package net.idea.rest.protocol.resource.db;

import java.io.Writer;
import java.net.URL;
import java.util.Date;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.qmrf.client.Resources;
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


public class ProtocolQueryHTMLReporter extends QMRFHTMLReporter<DBProtocol, IQueryRetrieval<DBProtocol>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	protected boolean paging = true;
	protected GroupQueryURIReporter<IQueryRetrieval<IDBGroup>> groupReporter;
	protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;

	public ProtocolQueryHTMLReporter() {
		this(null,true,false,true);
	}
	public ProtocolQueryHTMLReporter(Request request, boolean collapsed,boolean editable,boolean paging) {
		super(request,collapsed,editable);
		setTitle("QMRF document");
		groupReporter = new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
		userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
		this.paging = paging;
		
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
			
			if (collapsed) 
				printTable(output, uri,item);
			else printForm(output,uri,item,false);

		} catch (Exception x) {
			
		} finally {
			record++;
		}
		return null;
	}
	@Override
	protected void printTableHeader(Writer w) throws Exception {
		w.write("<table width='100%'>\n");
		w.write("<tr><th width='5%'>#</th><th width='10%'>QMRF number</th><th>Title</th><th width='10%'>Published</th><th width='10%'>Download</th></tr>");			

		
	}
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
					"<a href=\"%s?media=%s\"><img src=\"%s/images/%s\" alt=\"%s\" title=\"%s\" border=\"0\"/></a>\n",
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
	protected void printTable(Writer output, String uri, DBProtocol protocol) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");	
		
			output.write(String.format("<td>%d.</td>",record+1 ));
			output.write(String.format("<td width='15em'><a href='%s'>%s</a></td>",uri,ReadProtocol.fields.identifier.getValue(protocol)));			
			output.write(String.format("<td>%s</td>",protocol.getTitle()));
			output.write(String.format("<td width='8em'>%s</td>",simpleDateFormat.format(new Date(protocol.getTimeModified()))));
			output.write(String.format("<td width='50px'>%s</td>",printDownloadLinks(uri)));
			
			/*
			for (ReadProtocol.fields field : ReadProtocol.displayFields) {

				Object value = null; 
				try { value = field.getValue(protocol);} catch (Exception x) {}
				switch (field) {
				case idprotocol: {
					//output.write(String.format("<td><a href='%s'>%s</a></td>",uri,uri));
					break;
				}	
				case updated: {
					
					output.write(String.format("<td><a href='%s%s?%s=%s' title='Find protocols modified since this one (Unix time stamp, ms=%s)'>%s</a></td>",
							uriReporter.getRequest().getRootRef(),Resources.protocol,"modifiedSince",protocol.getTimeModified(),
							protocol.getTimeModified(),
							protocol.getTimeModified()==null?"":new Date(protocol.getTimeModified())));
					break;
				}
				case identifier: {
					output.write(String.format("<td><a href='%s'>%s</a></td>",uri,value));
					break;
				}
				case filename: {
					if ((protocol.getDocument()==null) || (protocol.getDocument().getResourceURL()==null))
						output.write("<td>N/A</td>");
					else					
						output.write(String.format("<td><a href='%s%s?media=%s'>Download</a></td>",
								uri,Resources.document,Reference.encode(MediaType.APPLICATION_ALL.toString())));
					break;
				}	
				case template: {
					if ((protocol.getDataTemplate()==null) || (protocol.getDataTemplate().getResourceURL()==null))
						output.write("<td>N/A</td>");
					else
					output.write(String.format("<td><a href='%s%s'>Download</a></td>",uri,Resources.datatemplate));
					break;
				}						
				case author_uri: {
					output.write(String.format("<td><a href='%s%s'>Authors</a></td>",uri,Resources.authors));
					break;
				}				
				case user_uri: {
					output.write(String.format("<td><a href='%s'>%s</a></td>",value.toString(),
							protocol.getOwner().getUserName()==null?"Owner":protocol.getOwner().getUserName()));
					break;
				}
				case idorganisation: {
					output.write(String.format("<td>%s</td>",value.toString()));
					break;
				}
				case idproject: {
					output.write(String.format("<td>%s</td>",value.toString()));
					break;
				}
				default:
					output.write(String.format("<td>%s</td>",value==null?"":
								value.toString().length()>40?value.toString().substring(0,40):value.toString()));
				}
			}
			*/
			output.write("</tr>\n");
		} catch (Exception x) {
			x.printStackTrace();
		} 
	}

}
