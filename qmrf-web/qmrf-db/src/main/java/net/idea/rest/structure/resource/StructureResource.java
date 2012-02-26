package net.idea.rest.structure.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.idea.restnet.db.QueryResource;

import org.opentox.csv.CSVFeatureValuesIterator;
import org.opentox.dsl.OTCompound;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class StructureResource extends CatalogResource<Structure>{
	protected static String queryService="http://localhost:8080/ambit2";
	public enum SearchMode {
		auto,
		similarity,
		smarts
	}
	@Override
	protected Iterator<Structure> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		Form form = request.getResourceRef().getQueryAsForm();
		String search = form.getFirstValue(QueryResource.search_param)==null?"": form.getFirstValue(QueryResource.search_param);
		String pagesize = form.getFirstValue("pagesize");
		String page = form.getFirstValue("page");
		try { int psize = Integer.parseInt(pagesize); if (psize>100) pagesize="10";} catch (Exception x) { pagesize="10";}
		try { int p = Integer.parseInt(page); if ((p<0) || (p>100)) page="0";} catch (Exception x) { page="0";}
		String threshold = form.getFirstValue("threshold");
		SearchMode option = SearchMode.auto;
		try {
			option = SearchMode.valueOf(form.getFirstValue("option").toLowerCase());
		} catch (Exception x) {
			option = SearchMode.auto;
		}
		try {
			Reference ref = new Reference(String.format("%s/query/compound/search/all",queryService));
			switch (option) {
			case similarity: {
				ref = new Reference(String.format("%s/query/similarity?threshold=%s",queryService,threshold));
				break;
			}
			case smarts: {
				ref = new Reference(String.format("%s/query/smarts",queryService));
				break;
			}
			}
			ref.addQueryParameter("pagesize",pagesize);
			ref.addQueryParameter("page",page);
			ref.addQueryParameter(QueryResource.search_param,search);
			
			//ref.addQueryParameter("pagesize", "1");
			//ref.addQueryParameter("page", "0");
			
			List<Structure> records = new ArrayList<Structure>();
			CSVFeatureValuesIterator<Structure> i = new CSVFeatureValuesIterator<Structure>(ref.toString()) {
				@Override
				public Structure transformRawValues(List header, List values) {
					Structure r = new Structure();
				
					for (int i=0; i < header.size();i++)  try {
						String value = values.get(i)==null?"":values.get(i).toString().trim();
						if ("null".equals(value)) value="";
						if ("metric".equals(header.get(i))) {
							r.setSimilarity(value);
							continue;
						} 
						OTCompound._titles title = 	OTCompound._titles.valueOf(header.get(i).toString().replace("http://www.opentox.org/api/1.1#",""));
						//String[] v = value.split("|");
						switch (title) {
						case Compound: {
							r.setUri(value); break;
						}
						case CASRN: {
							r.setCas(value); break;
						}
						case ChemicalName: {
							r.setName(value.replace("|","<br>")); break;
						}
						case EINECS: {
							r.setEinecs(value); break;
						}
						case SMILES: {
							r.setSMILES(value); break;
						}
						case InChI_std: {
							r.setInChI(value); break;
						}
						case InChIKey_std: {
							r.setInChIKey(value); break;
						}
						}
					} catch (Exception x) {
						x.printStackTrace();
					}

					return r;
				}
			};
			try {
				while (i.hasNext())
					records.add(i.next());
			} finally {
				i.close();
			}
			return records.iterator();
		} catch (Exception x) {
			throw new ResourceException(x);
		}
	}
	
	@Override
	protected Reporter createHTMLReporter() {
		return new StructureHTMLReporter(getRequest(), null);
	}

	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}
	
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		QMRF_HTMLBeauty q = new QMRF_HTMLBeauty();
		q.setSearchTitle("Structure search");
		q.setSearchURI(Resources.structure);
		return q;
	}
}
