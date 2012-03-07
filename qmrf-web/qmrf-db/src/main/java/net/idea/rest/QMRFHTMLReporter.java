package net.idea.rest;

import java.io.Writer;
import java.text.SimpleDateFormat;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.Request;
import org.restlet.data.Reference;

public abstract class QMRFHTMLReporter<T,Q extends IQueryRetrieval<T>>  extends QueryHTMLReporter<T,Q>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3253360243151440939L;
	protected long record = 0;
	protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
	protected boolean editable = false;
	protected String title;
	protected boolean headless = false;
	
	public boolean isHeadless() {
		return headless;
	}
	public void setHeadless(boolean headless) {
		this.headless = headless;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public QMRFHTMLReporter() {
		this(null,true,null,null);
	}
	public QMRFHTMLReporter(Request request, boolean collapsed,ResourceDoc doc) {
		this(request,collapsed,doc,null);
	}
	public QMRFHTMLReporter(Request request, boolean collapsed,ResourceDoc doc, HTMLBeauty htmlBeauty) {
		super(request,collapsed,doc);
	}
	public QMRFHTMLReporter(Request request, boolean collapsed,boolean editable) {
		super(request,collapsed,null);
		this.editable = editable;
	}
	

	@Override
	public void header(Writer w, Q query) {
		if (!headless)
			super.header(w, query);
		
		record = query.getPage()*query.getPageSize();
		
		Reference uri = uriReporter.getRequest().getResourceRef().clone();
		uri.setQuery(null);
		
		try {
			//
			if (editable) {
					w.write("<div class='protocol'><table>\n");
					w.write(String.format("<h3>Add new %s %s</h3>",getTitle(),uri.toString().contains("versions")?"version":""));
					printForm(output,uri.toString(),null,true);
					output.write(
							"<tr><td></td><td><input type='submit' enabled='false' value='Submit'></td></tr>"
							);
					w.write("</table>\n");
					output.write("</form>");
					output.write("</div>");	
				
			}
	    } catch (Exception x) {}
		
	    
		try {
			if (printAsTable()) {
				if (!headless && getTitle()!=null)
					w.write(String.format("<h3>%ss</h3>",getTitle()));
				printPageNavigator(query);
				
			} else {
				if (!headless && getTitle()!=null)
					w.write(String.format("<h3>%s</h3>",getTitle()));
			}
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				w.write("<div class='protocol'>\n");
				if (printAsTable()) {
					printTableHeader(w);
				}
			} catch (Exception x) {}
		}
		
	}	
	protected void printPageNavigator(Q query) throws Exception {
		getOutput().write(QMRF_HTMLBeauty.getPaging(query.getPage(),0,2,query.getPageSize()));
	}
	protected boolean printAsTable() {
		return collapsed;
	}
	@Override
	public Object processItem(T protocol) throws AmbitException  {
		try {
			String uri = uriReporter.getURI(protocol);
			if (printAsTable()) 
				printTable(output, uri,protocol);
			else printForm(output,uri,protocol,false);

		} catch (Exception x) {
			
		}
		return null;
	}
	@Override
	public void footer(Writer output, Q query) {
		try {
			if (printAsTable()) output.write("</table>\n");				
			output.write("</div>\n");
		} catch (Exception x) {}
		if (!headless)
			super.footer(output, query);
	}
	@Override
	protected HTMLBeauty createHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
	abstract protected void printTableHeader(Writer output) throws Exception;
	abstract protected void printTable(Writer output, String uri, T item);
	abstract protected void printForm(Writer output, String uri, T item, boolean editable);
}
