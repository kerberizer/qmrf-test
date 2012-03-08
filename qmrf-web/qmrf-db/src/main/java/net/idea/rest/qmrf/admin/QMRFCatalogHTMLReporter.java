package net.idea.rest.qmrf.admin;

import java.io.Writer;
import java.util.Iterator;

import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.CatalogHTMLReporter;

import org.restlet.Request;
import org.restlet.data.Reference;

public class QMRFCatalogHTMLReporter<T> extends CatalogHTMLReporter<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3212894867577087481L;
	protected String title;
	protected long record = 0;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}	
	
	public QMRFCatalogHTMLReporter(Request request, ResourceDoc doc) {
		this(request, doc, new QMRF_HTMLBeauty(),"QMRF");

	}
	
	public QMRFCatalogHTMLReporter(Request request, ResourceDoc doc,
			HTMLBeauty htmlbeauty,String title) {
		super(request, doc, htmlbeauty);
		setTitle(title);
	}
	@Override
	public HTMLBeauty getHtmlBeauty() {
		return new QMRF_HTMLBeauty();
	}
	
	protected boolean printAsTable() {
		return false;
	}
	@Override
	public void header(Writer w, Iterator<T> query) {
		super.header(w, query);
		record = 0;
		
		Reference uri = getRequest().getResourceRef().clone();
		uri.setQuery(null);
		
		try {
			if (printAsTable()) {
				if (getTitle()!=null)
				w.write(String.format("<div class='ui-widget' style='margin-top:18px'><p><strong>%ss</strong></p></div>",getTitle()));
				w.write(printPageNavigator());
				
			} else {
				if (getTitle()!=null)
				w.write(String.format("<div class='ui-widget' style='margin-top:18px'><p><strong>%ss</strong></p></div>",getTitle()));
				w.write(printPageNavigator());
			}
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				w.write("<div class='.ui-widget'>\n");
			} catch (Exception x) {}
		}
		
	}		
	
	protected String printPageNavigator() {
		return ((QMRF_HTMLBeauty)htmlBeauty).getPaging(0,2);
	}
	
	@Override
	public void processItem(T item, Writer output) {
		try {
			record++;
			output.write(
			((QMRF_HTMLBeauty)htmlBeauty).printWidget(renderItemTitle(item), renderItem(item))
			);

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	public String renderItemTitle(T item) {
		return String.format("%d.",record);
	}
	public String renderItem(T item) {
		String uri = super.getURI(item).trim();
		return(String.format("<a href='%s'>%s</a>", uri,item));
	}
	@Override
	public void footer(Writer output, Iterator<T> query) {
		try {
			output.write("</div");
		} catch (Exception x) {}
		super.footer(output, query);
	}

}
