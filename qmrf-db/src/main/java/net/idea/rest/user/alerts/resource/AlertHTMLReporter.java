package net.idea.rest.user.alerts.resource;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.alerts.db.DBAlert;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryURIReporter;
import net.toxbank.client.Resources;

import org.restlet.Request;

public class AlertHTMLReporter extends QMRFHTMLReporter<DBAlert, IQueryRetrieval<DBAlert>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	DBUser.fields[] entryFields = DBUser.fields.values();
	DBUser.fields[] displayFields = DBUser.fields.values();
	
	protected boolean editable = false;
	public AlertHTMLReporter() {
		this(null,true,false,new QMRF_HTMLBeauty(Resources.alert));
	}
	public AlertHTMLReporter(Request baseRef, boolean collapsed,boolean editable, HTMLBeauty htmlBeauty) {
		super(baseRef,collapsed,null,htmlBeauty);
		this.editable = editable;
		
	}
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new AlertURIReporter<IQueryRetrieval<DBAlert>>(request);
	}
	
	
	
	@Override
	protected boolean printAsTable() {
		return collapsed;
	}
	@Override
	protected void printPageNavigator(IQueryRetrieval<DBAlert> query)
			throws Exception {
	
	}
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		if (printAsTable()) {
			output.write("<div style='float:left; width:100%; align:center'>\n");
			output.write("<table class='datatable' cellpadding='0' border='0' width='100%' cellspacing='0'>\n");
			//output.write("<caption><h3>Users</h3></caption>\n");	
			output.write("<thead>\n");	
			output.write(String.format("<th>%s</th>", "Query"));
			output.write(String.format("<th>%s</th>", "Frequency"));
			output.write(String.format("<th>%s</th>", "Type"));
			output.write("</thead>\n");
			output.write("<tbody>\n");
		}
	}
	@Override
	protected void printTable(Writer output, String uri, DBAlert alert) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");	
			output.write(renderItem(alert));			
			output.write("</tr>\n");
		} catch (Exception x) {
			x.printStackTrace();
		} 
	}
	
	public String renderItem(DBAlert alert) {
		StringBuilder rendering = new StringBuilder();

		rendering.append(String.format("<td>%s</td>",alert.getQueryString()));

		rendering.append(String.format("<td>%s</td>",alert.getRecurrenceFrequency()));
		rendering.append(String.format("<td>%s</td>",alert.getType()));

		
		return rendering.toString();
	}
	
	
	@Override
	public void footer(Writer output, IQueryRetrieval<DBAlert> query) {
		try {
			if (printAsTable()) output.write("</tbody></table></div>\n");	
		} catch (Exception x) {}
		try {
		if (!headless) {
			if (htmlBeauty == null) htmlBeauty = new HTMLBeauty();
			htmlBeauty.writeHTMLFooter(output, "", uriReporter.getRequest());			

			output.flush();			
		}
		} catch (Exception x) {}
	}
	
	protected void printForm(Writer output, String uri, DBAlert alert, boolean editable) {
		try {
			DBUser.fields[] fields = editable?entryFields:displayFields;
			for (DBAlert._fields field : DBAlert._fields.values()) {
				output.write("<tr bgcolor='FFFFFF'>\n");	
				Object value = alert==null?null:field.getValue(alert);

				if (editable) {
					value = field.getHTMLField(alert);
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
	protected HTMLBeauty createHTMLBeauty() {
		return new QMRF_HTMLBeauty(net.idea.qmrf.client.Resources.alert);
	}
}
