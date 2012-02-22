package net.idea.rest.user.resource;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
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
		
	}
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new UserURIReporter<IQueryRetrieval<DBUser>>(request);
	}
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		output.write("<table width='100%'>\n");

		output.write("<tr bgcolor='FFFFFF' >\n");	
		for (DBUser.fields field : DBUser.fields.values()) {
			output.write(String.format("<th>%s</th>",field.toString()));
		}
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
			for (DBUser.fields field : DBUser.fields.values()) {

				Object value = field.getValue(user);
				switch (field) {
				case iduser: {
					output.write(String.format("<td><a href='%s'>%s</a></td>",uri,uri));
					break;
				}	
		
				default:
					output.write(String.format("<td>%s</td>",value==null?"":value.toString().length()>40?value.toString().substring(0,40):value.toString()));
				}
			}
			output.write("</tr>\n");
		} catch (Exception x) {} 
	}

}
