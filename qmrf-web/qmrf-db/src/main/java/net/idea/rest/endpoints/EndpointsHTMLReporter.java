package net.idea.rest.endpoints;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.DisplayMode;
import net.idea.restnet.db.QueryURIReporter;
import net.toxbank.client.Resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Reference;

import ambit2.base.data.Dictionary;
import ambit2.base.data.LiteratureEntry;
import ambit2.base.data.Property;

/**
 * Reporter for {@link Dictionary} or {@link Property}
 * @author nina
 *
 */
public class EndpointsHTMLReporter<D extends Dictionary> extends QMRFHTMLReporter<D, IQueryRetrieval<D>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4281274169475316720L;
	protected int count = 0;
	public enum search_features {

		feature_name {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setName(arg1.toString());
			}
		},
		feature_sameas {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setLabel(arg1.toString());
			}
		},
		feature_hassource {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setReference(new LiteratureEntry(arg1.toString(),""));

				
			}
		},
		feature_type {
			@Override
			public void setProperty(Property p, Object arg1) {
				p.setClazz(arg1.toString().equals("STRING")?String.class:Number.class);
				
			}
		},
		feature_id {
			@Override
			public void setProperty(Property p, Object arg1) {
				try {
					p.setId(Integer.parseInt(arg1.toString()));
				} catch (Exception x) {
					
				}
			}
		}

		;	
		public abstract void setProperty(Property p, Object value);
	}
	
	public EndpointsHTMLReporter(Request reference,DisplayMode _dmode,HTMLBeauty htmlBeauty) {
		super(reference,DisplayMode.table.equals(_dmode),null,htmlBeauty);

	}	
	@Override
	protected QueryURIReporter createURIReporter(Request request, ResourceDoc doc) {
		return new DictionaryURIReporter(request,doc);
	}
	
	public String toURI(D record) {
		count++;
		//if (count==1) return ""; 
		
		String w = uriReporter.getURI(record);
		
		return String.format(
				"<img src=\"%s/images/%s\">%s<a href=\"%s\">%s</a>&nbsp;<br>",

				uriReporter.getBaseReference().toString(),
				"qmrf/chapter3.png",
				"",						
				w,
				record.getTemplate()
						);

		
	}	
	

	@Override
	public Object processItem(D record) throws AmbitException {


		try {
			output.write("<tr>");
			output.write("<td>");
			if (((EndpointTest)record).getCode()!=null)
				output.write(((EndpointTest)record).getCode());
			output.write("</td>");
			output.write(String.format("<td>%s</td>",toURI(record)));
			output.write("<td>");
			output.write(String.format("<a href='%s%s?option=endpoint&search=%s'>%s</a>", 
				getUriReporter().getBaseReference(),
				Resources.protocol,
				Reference.encode(record.getTemplate()),
				"(Q)MRF documents"
			));
			
			output.write("</td>");
			output.write("</tr>");
		} catch (Exception x) {
			StringWriter w = new StringWriter();
			x.printStackTrace(new PrintWriter(w));
			Context.getCurrentLogger().severe(w.toString());
		}
		return null;
	}
	
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		try {
			output.write("<div class='dataTableWrapper'>");
			output.write("<table class='datatable'>\n");
			output.write("<thead>\n");	
			output.write(String.format("<th>%s</th>", "Code"));
			output.write(String.format("<th>%s</th>", "Name"));
			output.write(String.format("<th>%s</th>", "(Q)MRF documents"));
			output.write("</thead>\n");
			output.write("<tbody>\n");
		} catch (Exception x) {
			
		}	
		
	}
	@Override
	protected void printTable(Writer output, String uri, D item) {

		
	}
	@Override
	protected void printForm(Writer output, String uri, D item,
			boolean editable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void printPageNavigator(IQueryRetrieval<D> query)
			throws Exception {

	}
	@Override
	public void header(Writer w, IQueryRetrieval<D> query) {
		super.header(w, query);
	}
	@Override
	public void footer(Writer output, IQueryRetrieval<D> query) {
		try {
			output.write("</tbody></table></div>");
			if (!headless) {
				if (htmlBeauty == null) htmlBeauty = new HTMLBeauty();
				htmlBeauty.writeHTMLFooter(output, "", uriReporter.getRequest());			
			}
			output.flush();
		} catch (Exception x) {
			
		}
	}
	
}
