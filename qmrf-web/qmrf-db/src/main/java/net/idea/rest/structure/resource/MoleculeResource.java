package net.idea.rest.structure.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.i.reporter.Reporter;

import org.opentox.rdf.OpenTox;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 * One molecule resource
 * @author nina
 *
 */
public class MoleculeResource extends StructureResource { 
	protected static String chemicalKey = "idchemical";
	protected static String structureKey = "idstructure";
	protected String[] dataset;
	protected String tabID="Molecule";
	
	@Override
	protected Iterator<Structure> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		StringBuilder url = new StringBuilder();
		url.append(queryService);
		
		Form form = request.getResourceRef().getQueryAsForm();
		dataset = form.getValuesArray("dataset");
		
		Object idchemical = request.getAttributes().get(chemicalKey);
		if (idchemical==null) return Collections.EMPTY_LIST.iterator();
		try { 
			int id = Integer.parseInt(idchemical.toString().trim());
			url.append(String.format("/%s/%d",OpenTox.URI.compound.name(),id));
			singleItem = true;
		} catch (Exception x) { return Collections.EMPTY_LIST.iterator(); }

		if (getRequest().getResourceRef().getLastSegment().equals("structure")) {
			url.append("/");
			url.append(OpenTox.URI.conformer.name());
			singleItem = false;
		} else {
			Object idstructure = request.getAttributes().get(structureKey);
			try { 
				int id = Integer.parseInt(idstructure.toString().trim());
				if (id>0)
					url.append(String.format("/%s/%d",OpenTox.URI.conformer.name(),id));
				singleItem = true;
			} catch (Exception x) { }
		}
		String query;
		if ((dataset!=null) && (dataset.length>0)) {
			query = String.format("%s?feature_uris[]=%s%s/%s%s",url,
					queryService,OpenTox.URI.dataset.getURI(),Reference.encode(dataset[0]),OpenTox.URI.feature.getURI());
			System.out.println(query);
			tabID = "Properties";
		} else 
			query = String.format("%s/query/compound/url/all?search=%s",queryService,Reference.encode(url.toString()));
		List<Structure> records = new ArrayList<Structure>();
		try {
			PropertiesIterator i = new PropertiesIterator(query);
			Reference queryURI = new Reference(queryService);
			try {
				while (i.hasNext()) {
					Structure struc = i.next();
					try {
						Object[] ids = struc.parseURI(queryURI);
						if (ids[0]!=null) struc.setIdchemical((Integer) ids[0]);
						if (ids[1]!=null) struc.setIdstructure((Integer) ids[1]);
					} catch (Exception x) {}					
					records.add(struc);
				}
			} catch (Throwable x) {
				throw createException(Status.SERVER_ERROR_BAD_GATEWAY, "Molecule", SearchMode.auto, url.toString(), x);					
			} finally {
				i.close();
			}
			return records.iterator();
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST, "Molecule", SearchMode.auto, url.toString(), x);			
		}
		
		
	}
	
	@Override
	protected Reporter createHTMLReporter(boolean headles) {
		MoleculeHTMLReporter reporter = new MoleculeHTMLReporter(getRequest(), null, new StructureHTMLBeauty(queryService));
		reporter.setSingleItem(singleItem);
		reporter.setHeadless(headless);
		reporter.tabID = tabID;
		return reporter;
	}
	


}
