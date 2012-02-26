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
	@Override
	protected Iterator<Structure> createQuery(Context context,
			Request request, Response response) throws ResourceException {
		Form form = request.getResourceRef().getQueryAsForm();
		String search = form.getFirstValue(QueryResource.search_param);
		try {
			Reference ref = new Reference(String.format("%s/query/compound/search/all",queryService));
			ref.addQueryParameter(QueryResource.search_param,search);
			//ref.addQueryParameter("pagesize", "1");
			//ref.addQueryParameter("page", "0");
			
			List<Structure> records = new ArrayList<Structure>();
			CSVFeatureValuesIterator<Structure> i = new CSVFeatureValuesIterator<Structure>(ref.toString()) {
				@Override
				public Structure transformRawValues(List header, List values) {
					Structure r = new Structure();
				
					for (int i=0; i < header.size();i++)  try {
						OTCompound._titles title = 	OTCompound._titles.valueOf(header.get(i).toString().replace("http://www.opentox.org/api/1.1#",""));
						switch (title) {
						case Compound: {
							r.setUri(values.get(i).toString()); break;
						}
						case CASRN: {
							r.setCas(values.get(i).toString()); break;
						}
						case ChemicalName: {
							r.setName(values.get(i).toString()); break;
						}
						case EINECS: {
							r.setEinecs(values.get(i).toString()); break;
						}
						case SMILES: {
							r.setSMILES(values.get(i).toString()); break;
						}
						case InChI_std: {
							r.setInChI(values.get(i).toString()); break;
						}
						case InChIKey_std: {
							r.setInChIKey(values.get(i).toString()); break;
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
