package net.idea.rest.qmrf.admin;

import java.io.Writer;

import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.CatalogHTMLReporter;

import org.restlet.Request;

public class QMRFCatalogHTMLReporter<T> extends CatalogHTMLReporter<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3212894867577087481L;

	
	public QMRFCatalogHTMLReporter(Request request, ResourceDoc doc,
			HTMLBeauty htmlbeauty,String title) {
		super(request, doc, htmlbeauty, false);
		setTitle(title);
		record = 0;
	}
	
	
	@Override
	public HTMLBeauty getHtmlBeauty() {
		return htmlBeauty;
	}
	
	protected boolean printAsTable() {
		return false;
	}
	
	
	protected String printPageNavigator() {
		if (singleItem || headless) return "";
		int page = ((QMRF_HTMLBeauty)htmlBeauty).getPage();
		long pageSize = ((QMRF_HTMLBeauty)htmlBeauty).getPageSize();
		return (((QMRF_HTMLBeauty)htmlBeauty).getPaging(page, page-5, page, pageSize));
	}
	
	@Override
	public void processItem(T item, Writer output) {
		try {
			output.write(
			((QMRF_HTMLBeauty)htmlBeauty).printWidget(renderItemTitle(item), renderItem(item))
			);
			record++;
		} catch (Exception x) {
			logger.warn(x);
		}
	}
	public String renderItemTitle(T item) {
		return String.format("%d.",record+1);
	}
	public String renderItem(T item) {
		String uri = super.getURI(item).trim();
		return(String.format("<a href='%s'>%s</a>", uri,item));
	}


}
