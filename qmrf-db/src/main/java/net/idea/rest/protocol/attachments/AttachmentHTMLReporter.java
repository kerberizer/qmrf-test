package net.idea.rest.protocol.attachments;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.Reference;

public class AttachmentHTMLReporter extends QMRFHTMLReporter<DBAttachment, IQueryRetrieval<DBAttachment>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5453025922862783009L;

	public AttachmentHTMLReporter(DBProtocol protocol) {
		this(protocol,null,true,null,null);
	}
	public AttachmentHTMLReporter(DBProtocol protocol,Request request, boolean collapsed,ResourceDoc doc) {
		this(protocol,request,collapsed,doc,null);
	}
	public AttachmentHTMLReporter(DBProtocol protocol,Request request, boolean collapsed,ResourceDoc doc, HTMLBeauty htmlBeauty) {
		this(protocol,request,collapsed,false);
	}
	public AttachmentHTMLReporter(DBProtocol protocol,Request request, boolean collapsed,boolean editable) {
		super(request,collapsed,editable);
		if (protocol!=null) {
			String qmrf = ReadProtocol.generateIdentifier(protocol);
			((AttachmentURIReporter)uriReporter).setPrefix(String.format("%s/%s",Resources.protocol,qmrf));
			setTitle(String.format("<a href='%s%s/%s'>%s</a> attachment",request.getRootRef(),Resources.protocol,qmrf,qmrf));
		} else 		setTitle("Attachment");
	}
	@Override
	protected void printTableHeader(Writer w) throws Exception {
		w.write("<table width='100%'>\n");
	
		
	}
	@Override
	protected void printPageNavigator(IQueryRetrieval<DBAttachment> query)
			throws Exception {

	}
	@Override
	protected boolean printAsTable() {
		return true;
	}
	public String printTable(DBAttachment attachment) {
		StringBuilder datasets = new StringBuilder();
		datasets.append("<tr>");
		
		datasets.append(String.format("<th>%s</th>",
					attachment.getDescription())
					);
		
		datasets.append(String.format("<td align='left' width='15%%'>%s</td>", attachment.getType()));
		datasets.append(String.format("<td align='left'><a href='%s?media=%s' target='_attachment' title='%s %s'>Download</a></td>",
				uriReporter.getURI(attachment),
				Reference.encode(attachment.getMediaType()),
				attachment.getTitle(),attachment.getMediaType()
				)
				);
	
		switch (attachment.getType()) {
		case document: {
			datasets.append("<td width='15%%'></td>");
			break;
		}
		default: {
			datasets.append(String.format("<td align='left' width='15%%'><a href='%s%s?option=dataset&search=%s' target='_structure'>Browse structures</a></td>",
						uriReporter.getBaseReference(),Resources.structure,Reference.encode(attachment.getTitle().trim())));
			break;
		}
		}
		//datasets.append(String.format("<td>%s</td>",attachment.getMediaType()));
		datasets.append("</tr>");
		return datasets.toString();
	}

	@Override
	public void footer(Writer output, IQueryRetrieval<DBAttachment> query) {
		try {
			output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidgetContentFooter()); 
			//output.write(printWidgetFooter());
		} catch (Exception x) {}
		super.footer(output, query);
	}
	@Override
	protected void printTable(Writer output, String uri, DBAttachment attachment) {
		try {
			
			output.write(printTable(attachment));
			
		} catch (Exception x) {}
	}

	@Override
	protected void printForm(Writer output, String uri, DBAttachment item,
			boolean editable) {

		
	}

	@Override
	protected QueryURIReporter createURIReporter(Request req, ResourceDoc arg1) {
		return new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(req);
	}

	
}
