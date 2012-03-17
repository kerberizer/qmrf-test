package net.idea.rest.structure.resource;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.idea.modbcum.i.reporter.Reporter;
import net.idea.qmrf.client.Resources;
import net.idea.restnet.c.TaskApplication;
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

public class StructureResource extends CatalogResource<Structure> {
	protected String queryService;
	protected boolean singleItem = false;
	protected HTMLBeauty htmlBeauty = null;

	public StructureResource() {
		super();
		queryService = ((TaskApplication) getApplication())
				.getProperty(Resources.Config.qmrf_ambit_service.name());
	}

	public enum SearchMode {
		auto, similarity, smarts
	}
	protected void parseParameters(Context context, Request request,Response response) throws ResourceException {
		Form form = request.getResourceRef().getQueryAsForm();
		
		StructureHTMLBeauty parameters = ((StructureHTMLBeauty)getHTMLBeauty());
		parameters.setDatasets(form.getValuesArray("dataset"));
		String search = form.getFirstValue(QueryResource.search_param) == null ? ""
				: form.getFirstValue(QueryResource.search_param);
		
		if ((search == null) || "".equals(search))	search="benzene"; //let's have a default
		
		String pagesize = form.getFirstValue("pagesize");
		String page = form.getFirstValue("page");
		try {
			int psize = Integer.parseInt(pagesize);
			if (psize > 100)parameters.setPageSize(psize);
			else parameters.setPageSize(psize);
		} catch (Exception x) {
			parameters.setPageSize(10);
		}
		try {
			int p = Integer.parseInt(page);
			if ((p < 0) || (p > 100))	parameters.setPage(0);
			else 			parameters.setPage(p);
		} catch (Exception x) {
			parameters.setPage(0);
		}
		String threshold = form.getFirstValue("threshold");
		try {
			parameters.setThreshold(Double.parseDouble(threshold));
		}catch (Exception x) {
			parameters.setThreshold(null);
		}
		SearchMode option = SearchMode.auto;
		try {
			option = SearchMode.valueOf(form.getFirstValue("option")
					.toLowerCase());
		} catch (Exception x) {
			option = SearchMode.auto;
		}
		parameters.setOption(option);
		
		if (search != null)
			search = search.replace("<", "").replace(">", "");
		parameters.setSearchQuery(search);
	}

	@Override
	protected Iterator<Structure> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		parseParameters(context,request,response);
		StructureHTMLBeauty parameters = ((StructureHTMLBeauty)getHTMLBeauty());
		Reference ref = null;
		try {
			ref = new Reference(String.format("%s/query/compound/search/all",
					queryService));
			switch (parameters.option) {
			case similarity: {
				ref = new Reference(String.format(
						"%s/query/similarity?threshold=%3.2f", queryService,
						parameters.threshold));
				break;
			}
			case smarts: {
				ref = new Reference(String.format("%s/query/smarts",
						queryService));
				break;
			}
			}
			ref.addQueryParameter("pagesize", Long.toString(parameters.getPageSize()));
			ref.addQueryParameter("page", Integer.toString(parameters.getPage()));
			if (parameters.getSearchQuery() != null)
				ref.addQueryParameter(QueryResource.search_param, parameters.getSearchQuery());

			try {
				List<Structure> records = Structure.retrieveStructures(
						queryService, ref.toString());
				return records.iterator();
			} catch (Exception x) {
				throw createException(Status.CLIENT_ERROR_BAD_REQUEST, parameters.getSearchQuery(),
						parameters.option, ref.toString(), x);
			}
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST,  parameters.getSearchQuery(),
					parameters.option, ref.toString(), x);
		}
	}

	protected ResourceException createException(Status status, String search,
			SearchMode option, String ref, Throwable x) {
		String message = String.format("Search query '%s' failed", search);
		switch (option) {
		case similarity: {
			message = String.format("SMILES or InChI expected instead of '%s'",
					search);
			break;
		}
		case smarts: {
			message = String.format("SMARTS expected instead of '%s'", search);
			break;
		}

		}
		throw new ResourceException(status.getCode(), message, String.format(
				"Error when contacting (%s) structure search service at %s",
				option.toString(), ref),
				"http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html", x);
	}

	@Override
	protected Reporter createHTMLReporter(boolean headles) {
		StructureHTMLReporter reporter = new StructureHTMLReporter(getRequest(), null, getHTMLBeauty());
		reporter.setSingleItem(singleItem);
		reporter.setHeadless(headless);
		return reporter;
	}

	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
	}

	@Override
	protected HTMLBeauty getHTMLBeauty() {
		if (htmlBeauty == null) htmlBeauty = new StructureHTMLBeauty(queryService);
		return htmlBeauty;
	}
}

class PropertiesIterator extends CSVFeatureValuesIterator<Structure> {
	protected Structure structure = null;

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public PropertiesIterator(String url) throws Exception {
		super(url);
	}

	@Override
	public Structure transformRawValues(List header, List values) {
		Structure r = structure == null ? new Structure() : structure;
		String value = null;
		for (int i = 0; i < header.size(); i++)
			try {
				value = values.get(i) == null ? "" : values.get(i).toString()
						.trim();
				if ("null".equals(value))
					value = "";
				if ("metric".equals(header.get(i))) {
					r.setSimilarity(value);
					continue;
				}
				OTCompound._titles title = OTCompound._titles.valueOf(header
						.get(i).toString()
						.replace("http://www.opentox.org/api/1.1#", ""));
				// String[] v = value.split("|");
				switch (title) {
				case Compound: {
					r.setResourceURL(new URL(value));
					break;
				}
				case CASRN: {
					r.setCas(value);
					break;
				}
				case ChemicalName: {
					r.setName(value.replace("|", "<br>"));
					break;
				}
				case EINECS: {
					r.setEinecs(value);
					break;
				}
				case SMILES: {
					r.setSMILES(value);
					break;
				}
				case InChI_std: {
					r.setInChI(value);
					break;
				}
				case InChIKey_std: {
					r.setInChIKey(value);
					break;
				}
				}
			} catch (Exception x) {
				if (header.get(i).toString().toUpperCase().startsWith("CAS"))
					r.setCas(value);
				else if ("NAME".equals(header.get(i).toString().toUpperCase()))
					r.setName(value);
				else
					try {
						NumberFormat nf = java.text.DecimalFormat
								.getNumberInstance(Locale.ENGLISH);
						r.getProperties().put(header.get(i).toString(),
								nf.format(Double.parseDouble(value)));
					} catch (Exception e) {
						r.getProperties().put(header.get(i).toString(), value);
					}
			}

		return r;
	}
};
