package net.idea.rest;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.qmrf.client.QMRFRoles;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.aalocal.DBRole;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;

public abstract class QMRFHTMLReporter<T,Q extends IQueryRetrieval<T>>  extends QueryHTMLReporter<T,Q>  {
	public static final DBRole managerRole = new DBRole(QMRFRoles.qmrf_manager.name(),QMRFRoles.qmrf_manager.toString());
	public static final DBRole editorRole = new DBRole(QMRFRoles.qmrf_editor.name(),QMRFRoles.qmrf_editor.toString());
	protected transient Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 3253360243151440939L;
	protected long record = 0;
	protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

	public QMRFHTMLReporter(Request request, boolean collapsed,ResourceDoc doc, HTMLBeauty htmlBeauty) {
		super(request,collapsed,doc,htmlBeauty);
		this.htmlBeauty = htmlBeauty;
	}

	protected void printUploadForm(Writer output, String uri, T item) {
		
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
			if (editable) 
					printUploadForm(output,uri.toString(),null);
			
	    } catch (Exception x) {}
		
	    
		try {
			if (printAsTable() && !headless) printPageNavigator(query);
		} catch (Exception x) {
			logger.log(Level.WARNING,x.getMessage(),x);
		} finally {
			try {
				if (!headless)
					w.write("<div class='ui-widget'>\n");
				if (printAsTable()) {
					printTableHeader(w);
				}
			} catch (Exception x) {}
		}
		
	}	

	protected void printPageNavigator(Q query) throws Exception {
		getOutput().write(((QMRF_HTMLBeauty)htmlBeauty).getPaging(query.getPage(), query.getPage()-4, query.getPage(), query.getPageSize()));
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
			record++;
		} catch (Exception x) {
			
		}
		return null;
	}
	@Override
	public void footer(Writer output, Q query) {
		try {
			if (printAsTable()) {
				output.write("</table>\n");
				output.write("<div style='float:right; width:100%; align:center; margin:20px 0 0 0;'>\n");
				output.write("<p>Download as&nbsp;");
				output.write(printDownloadLinks(uriReporter.getRequest().getResourceRef()));
				output.write("</p></div>");
			}
			
			if (!headless && (record==(query.getPage()*query.getPageSize()))) {
				if (((QMRF_HTMLBeauty)htmlBeauty).getSearchQuery()==null) {
					 output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidget("You haven't specified a QMRF document search query", "Please try the documents search menu."));
				} else  
					output.write(((QMRF_HTMLBeauty)htmlBeauty).printWidget(
							record==0?"Query returns no results":"No more results", 
							"Please try a different query"));
			}
		} catch (Exception x) {}
		if (!headless)
			super.footer(output, query);
	}
	@Override
	protected HTMLBeauty createHTMLBeauty() {
		return htmlBeauty;
	}
	
	abstract protected void printTableHeader(Writer output) throws Exception;
	abstract protected void printTable(Writer output, String uri, T item);
	abstract protected void printForm(Writer output, String uri, T item, boolean editable);
	

	protected String printDownloadLinks(Reference uri) throws Exception {
		StringBuilder b = new StringBuilder();
		MediaType[] mimes = {
				MediaType.TEXT_CSV			
		};
		
		String[] image = {
				"excel.png"
		};	
		
		String[] description = {
				"Download as MS Excel"
		};			
		for (int i=0;i<mimes.length;i++) {
			MediaType mime = mimes[i];
			Reference downloadURI = uri.clone();
			downloadURI.addQueryParameter("media", mime.toString());
			b.append(String.format(
					"<a href=\"%s\"><img src=\"%s/images/%s\" alt=\"%s\" title=\"%s\" border=\"0\"/></a>\n",
					downloadURI,
					getUriReporter().getBaseReference().toString(),
					image[i],
					mime,
					description[i]
					));
		}
		return b.toString();
	}
}
