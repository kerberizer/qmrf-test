package net.idea.rest.structure.resource;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.reporter.Reporter;
import net.idea.modbcum.p.QueryExecutor;
import net.idea.qmrf.client.Resources;
import net.idea.rest.prediction.DBModel;
import net.idea.rest.prediction.ReadModel;
import net.idea.rest.prediction.ReadModelQuery;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.db.ReadAttachment;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.resource.CatalogResource;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.QueryResource;

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
		//parameters.setDatasets(form.getValuesArray("dataset"));
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
		
		try {
			String[] datasets = form.getValuesArray("dataset");
			parameters.setDatasets(verifyDataset(datasets));
		} catch (Exception x) { parameters.setDatasets(null); x.printStackTrace();}
		
		try {
			String[] models = form.getValuesArray("model");
			if ((models!=null) && (models.length>0))
				parameters.setModels(verifyModels(models));
		} catch (Exception x) { parameters.setModels(null); x.printStackTrace();}
	}
	/*
	protected String name2Structure(String name) {
		try {
			if (nameToStructure == null) nameToStructure = NameToStructure.getInstance();
			return nameToStructure.parseToSmiles(name);
		} catch (Exception x) {
			return null;
		}
	}
	*/
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
				//String smiles = name2Structure(parameters.getSearchQuery());
				//if (smiles!=null) parameters.setSearchQuery(smiles);
				ref = new Reference(String.format(
						"%s/query/similarity?threshold=%3.2f", queryService,
						parameters.threshold));
				break;
			}
			case smarts: {
				//String smiles = name2Structure(parameters.getSearchQuery());
				//if (smiles!=null) parameters.setSearchQuery(smiles);
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
	

	public String getConfigFile() {
		return "conf/qmrf-db.pref";
	}
	
	protected String getAttachmentDir() {
		String dir = ((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_attachments_dir.name());
		return dir==null?System.getProperty("java.io.tmpdir"):dir;
	}
	
	
	protected List<DBAttachment> verifyDataset(String[] attachmentKey) throws Exception {
		if (attachmentKey==null || attachmentKey.length==0) return null;
		List<DBAttachment> results = new ArrayList<DBAttachment>();
		Connection conn = null;
		QueryExecutor  exec = new QueryExecutor();
		try {
			ReadAttachment query;
			DBAttachment attachment = new DBAttachment();
			query = new ReadAttachment(null,getAttachmentDir());
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			exec.setConnection(conn);
			for (String aKey : attachmentKey) {
				if (aKey==null) continue;
				if (aKey.toString().startsWith("A")) {
					attachment.setID(new Integer(Reference.decode(aKey.toString().substring(1))));
					query.setFieldname(null);
					query.setValue(attachment);
				} else {
					query.setFieldname(new DBProtocol(Reference.decode(aKey.toString())));
					query.setValue(null);
				} 
				ResultSet rs = exec.process(query);
				while (rs.next()) {
					DBAttachment a = query.getObject(rs);
					if (a.isImported()) 
						results.add(a);
				}
				rs.close();

			}
			return results;
		} catch (NumberFormatException x) {
			return null;
		} catch (Exception x) {
			try { if (exec!=null) exec.close(); } catch (Exception xx) {}
			try { if (conn!=null) conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
		
	}
	
	public final static String algorithmURI = "/algorithm/fptanimoto";
	
	protected List<DBModel> verifyModels(String[] modelsKey) throws Exception {
		if (modelsKey==null || modelsKey.length==0) return null;
		List<DBModel> results = new ArrayList<DBModel>();
		Connection conn = null;
		QueryExecutor  exec = new QueryExecutor();
		try {
			IQueryRetrieval<DBModel>  query;
			DBModel model = new DBModel();
			
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			exec.setConnection(conn);
			for (String aKey : modelsKey) {
				if (aKey==null) continue;
				if (aKey.toString().startsWith("M")) {
					query = new ReadModel();
					model.setID(new Integer(Reference.decode(aKey.toString().substring(1))));
					((ReadModel)query).setValue(model);
					((ReadModel)query).setModelRoot(queryService+"/model");
				} else  {
					query = new ReadModelQuery();
					((ReadModelQuery)query).setFieldname(new DBProtocol(aKey));
					((ReadModelQuery)query).setValue(null);
					((ReadModelQuery)query).setModelRoot(queryService+"/model");
				}
				ResultSet rs = exec.process(query);
				while (rs.next()) {
					DBModel a = query.getObject(rs);
					results.add(a);
				}
				rs.close();

			}
			return results;
		} catch (NumberFormatException x) {
			return null;
		} catch (Exception x) {
			try { if (exec!=null) exec.close(); } catch (Exception xx) {}
			try { if (conn!=null) conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
		
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
				Structure._titles title = Structure._titles.valueOf(header
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
