package net.idea.rest.endpoints;

import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.structure.resource.OpenTox;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.reporters.DisplayMode;
import net.idea.restnet.db.QueryURIReporter;
import net.idea.restnet.rdf.ns.OTEE;

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
public class EndpointsHTMLReporter extends QMRFHTMLReporter<Property, IQueryRetrieval<Property>> {

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
		return new PropertyURIReporter(request,doc);
	}
	
	public String toURI(Property record) {
		count++;
		//if (count==1) return ""; 
		
		String w = uriReporter.getURI(record);
		
		boolean isDictionary= record.getClazz().equals(Dictionary.class);
		return String.format(
				"<img src=\"%s/images/%s\">%s<a href=\"%s\">%s</a>&nbsp;<br>",

				uriReporter.getBaseReference().toString(),
				isDictionary?"folder.png":"feature.png",
				isDictionary?"":"&nbsp;Feature:&nbsp;",						
				w,
				isDictionary?((Dictionary)record).getTemplate():record.toString()
						);

		
	}	
	

	@Override
	public Object processItem(Property record)
			throws net.idea.modbcum.i.exceptions.AmbitException {


		try {
			output.write("<tr>");
			output.write(String.format("<td>%s</td>",toURI(record)));
			output.write("<td>");
			if ( record.getClazz().equals(Dictionary.class) ){
				output.write(String.format("<a href='%s/%s?%s=%s'>%s</a>", 
						getUriReporter().getBaseReference(),
						OpenTox.URI.dataset.name(),
						search_features.feature_sameas.name(),
						Reference.encode(String.format("%s%s",OTEE.NS,record.getName())),
						"Datasets"
						));
			} else 
				output.write(String.format("<a href='%s/%s?%s=%s'>%s</a>", 
						getUriReporter().getBaseReference(),
						OpenTox.URI.dataset.name(),
						search_features.feature_id.name(),
						Reference.encode(String.format("%s%s",OTEE.NS,record.getId())),
						"Datasets"
						));
			
			output.write("</td>");
			output.write("</tr>");
		} catch (Exception x) {
			Context.getCurrentLogger().severe(x.getMessage());
		}
		return null;
	}
	
	@Override
	protected void printTableHeader(Writer output) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void printTable(Writer output, String uri, Property item) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void printForm(Writer output, String uri, Property item,
			boolean editable) {
		// TODO Auto-generated method stub
		
	}
	
}
