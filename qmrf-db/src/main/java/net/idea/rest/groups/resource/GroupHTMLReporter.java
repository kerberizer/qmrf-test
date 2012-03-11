package net.idea.rest.groups.resource;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.groups.DBGroup;
import net.idea.rest.groups.IDBGroup;
import net.idea.rest.user.DBUser;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;


public abstract class GroupHTMLReporter extends QMRFHTMLReporter<IDBGroup, IQueryRetrieval<IDBGroup>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959033048710547839L;
	protected DBUser user = null;
	
	protected boolean editable = false;
	public GroupHTMLReporter() {
		this(null,true,false);
	}
	public GroupHTMLReporter(Request baseRef, boolean collapsed,boolean editable) {
		super(baseRef,collapsed,editable);
	}
	
	
	public abstract String getBackLink();
	public abstract String getTitle();
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new GroupQueryURIReporter<IQueryRetrieval<IDBGroup>>(request);
	}
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		output.write("<table width='100%'>\n");
		if (!headless) {
			output.write("<tr bgcolor='FFFFFF' >\n");	
			output.write(String.format("<th>%s</th>",DBGroup.fields.name.toString()));
			output.write("</tr>\n");
		}
		
	}

	@Override
	public Object processItem(IDBGroup protocol) throws AmbitException  {
		try {
			String uri = uriReporter.getURI(protocol);
			if (collapsed) 
				printTable(output, uri,protocol);
			else  printTable(output, uri,protocol);// printForm(output,uri,protocol,false);
			record++;
		} catch (Exception x) {
			
		}
		return null;
	}
	
	protected void printForm(Writer output, String uri, IDBGroup group, boolean editable) {
		try {
			if (user!=null) {
				output.write("<tr bgcolor='FFFFFF'>\n");	
				output.write(String.format("<th>%s URI</th><td align='left'><input name='%s_uri' value='' size='80'></td>\n",
									getTitle(),getTitle().toLowerCase()));
				output.write("</tr>\n");			
			} else
			for (DBGroup.fields field : DBGroup.fields.values()) {
				output.write("<tr bgcolor='FFFFFF'>\n");	
				Object value = field.getValue(group);

				if (editable) {
					value = field.getHTMLField(group);
				} else 
					if (value==null) value = "";
							
				switch (field) {
				case idgroup: {
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
			output.flush();
		} catch (Exception x) {x.printStackTrace();} 
	}	
	
	protected void printTable(Writer output, String uri, IDBGroup item) {
		try {
			output.write("<tr bgcolor='FFFFFF'>\n");		
			output.write(String.format("<td><a href='%s'>%s</a></td>",uri,item.getTitle()));
			output.write("</tr>\n");
		} catch (Exception x) {} 
	}

}
