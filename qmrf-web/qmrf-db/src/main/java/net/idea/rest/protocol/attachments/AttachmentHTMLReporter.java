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
	protected String uploadUI;
	protected boolean showDataset= false;
	
	public boolean isShowDataset() {
		return showDataset;
	}
	public void setShowDataset(boolean showDataset) {
		this.showDataset = showDataset;
	}
	public AttachmentHTMLReporter(DBProtocol protocol) {
		this(protocol,null,true,null,null);
	}
	public AttachmentHTMLReporter(DBProtocol protocol,Request request, boolean collapsed,ResourceDoc doc) {
		this(protocol,request,collapsed,doc,null);
	}
	public AttachmentHTMLReporter(DBProtocol protocol,Request request, boolean collapsed,ResourceDoc doc, HTMLBeauty htmlBeauty) {
		super(request,collapsed,doc,htmlBeauty);
		if (protocol!=null) {
			String qmrf = ReadProtocol.generateIdentifier(protocol);
			
			((AttachmentURIReporter)uriReporter).setPrefix(String.format("%s/%s",Resources.protocol,qmrf));
			setTitle(String.format("<a href='%s%s/%s'>%s</a> attachment",request.getRootRef(),Resources.protocol,qmrf,qmrf));
			uploadUI = String.format("<a href='%s%s/%s' target='upload'>%s</a>",request.getRootRef(),Resources.editor,qmrf,"Add attachment(s)");
		} else {
			setTitle("Attachment");
			uploadUI = String.format("<a href='%s%s'  target='upload'>%s</a>",request.getRootRef(),Resources.editor,"Add new QMRF");
		}
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

		String uri = uriReporter.getURI(attachment);
		StringBuilder datasets = new StringBuilder();
		datasets.append("<tr>");
		
		datasets.append(String.format("<th>%s</th>",
					attachment.getDescription())
					);
		
		datasets.append(String.format("<td align='left'>%s</td>", attachment.getType()));
		datasets.append(String.format("<td align='left'><a href='%s?media=%s' target='_attachment' title='%s %s'>Download</a></td>",
				uri,
				Reference.encode(attachment.getMediaType()),
				attachment.getTitle(),attachment.getMediaType()
				)
				);
	
		switch (attachment.getType()) {
		case document: {
			datasets.append("<td></td>");
			break;
		}
		default: {
			if (attachment.imported) {
				String datasetURI = String.format("%s%s/A%d",
						uriReporter.getBaseReference(),Resources.dataset,
						attachment.getID());
						
				datasets.append(String.format("<td align='left' ><a href='%s' target='_structure'>Browse structures</a></td>",
						datasetURI));

			} else {
				String form = String.format("<form method='POST' title='This dataset is not yet browsable and searchable' action='%s/dataset'><input type='hidden' value='true' name='import'><input type='submit' class='Draw' title='Convert to browsable and searchable dataset' value='Convert to browsable'></form>",uri);
				datasets.append(String.format("<td align='left'>%s</td>",form));
			}
			break;
		}
		}
		datasets.append("</tr>");

		return datasets.toString();
	}
	/*
	protected String showDataset(DBAttachment attachment) {
		
		try {
			List<Structure> records = Structure.retrieveStructures(queryService, ref.toString());
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST, search, option, ref.toString(), x);
		}
		String datasetURI = String.format(
				"<a href=\"%s%s?option=dataset&search=%s&headless=true&details=false&media=text/html\" title=\"%s\">%s</a>",
				uriReporter.getBaseReference(),Resources.chemical,Reference.encode(attachment.getTitle()),
				attachment.getDescription(),attachment.getDescription());
		datasetURI = String.format("<li>%s<span></span></li>\n",datasetURI);

		
		StringBuilder rendering = new StringBuilder();
		rendering.append(String.format(
				"<div class='protocol'>\n"+					
				"<div class='tabs'>\n<ul>" +
				"<li>%s<span></span></li>\n" +
			    "</ul></div>\n</div>\n",
			    datasetURI
				));
		
		
		rendering.append("</div>\n</div>\n");//tabs , protocol

		return rendering.toString();	
	}
	*/
	@Override
	public void footer(Writer output, IQueryRetrieval<DBAttachment> query) {
		try {
			if (uploadUI!=null)	output.write(uploadUI);
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
