package net.idea.rest.user.resource;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.user.DBUser;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;

public class UserHTMLReporter extends QMRFHTMLReporter<DBUser, IQueryRetrieval<DBUser>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	DBUser.fields[] entryFields = DBUser.fields.values();
	DBUser.fields[] displayFields = DBUser.fields.values();
	
	public UserHTMLReporter() {
		this(null,true,false);
	}
	public UserHTMLReporter(Request baseRef, boolean collapsed,boolean editable) {
		super(baseRef,collapsed,editable);
		setTitle("User");
	}
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new UserURIReporter<IQueryRetrieval<DBUser>>(request);
	}
	@Override
	protected boolean printAsTable() {
		return collapsed || !editable;
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
			output.write(String.format("<td><a href='%s'>%s %s %s</a></td>",uri,user.getTitle(),user.getFirstname(),user.getLastname()));
			if (DBUser.fields.homepage.getValue(user)!=null)
				output.write(String.format("<td><a href='%s' target='user'>%s</a></td>",DBUser.fields.homepage.getValue(user),DBUser.fields.homepage.getValue(user)));
			else
				output.write("<td></td>");
			output.write(String.format("<td><a href='%s%s'>%s</a></td>",uri,Resources.protocol,"QMRF documents"));			
			output.write("</tr>\n");
		} catch (Exception x) {} 
	}

}
