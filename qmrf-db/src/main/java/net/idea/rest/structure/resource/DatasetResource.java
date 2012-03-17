package net.idea.rest.structure.resource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.idea.modbcum.p.QueryExecutor;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.db.ReadAttachment;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.db.DBConnection;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class DatasetResource extends StructureResource {
	public static String datasetKey = "datasetKey";
	@Override	
	protected Iterator<Structure> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		Form form = request.getResourceRef().getQueryAsForm();
		((StructureHTMLBeauty)getHTMLBeauty()).setDatasets(form.getValuesArray("dataset"));
		Object key = getRequest().getAttributes().get(datasetKey);
		if ((key == null) || "".equals(key))	return Collections.EMPTY_LIST.iterator();
		String search = Reference.decode(key.toString());
		DBAttachment attachment = null;
		try {
			attachment = verifyDataset(search);
			if (attachment==null) return null;
		} catch (Exception x) {return Collections.EMPTY_LIST.iterator();}
		((StructureHTMLBeauty)getHTMLBeauty()).setAttachment(attachment);
		
		String pagesize = form.getFirstValue("pagesize");
		String page = form.getFirstValue("page");
		try {
			int psize = Integer.parseInt(pagesize);
			if (psize > 100)
				pagesize = "10";
		} catch (Exception x) {
			pagesize = "10";
		}
		try {
			int p = Integer.parseInt(page);
			if ((p < 0) || (p > 100))
				page = "0";
		} catch (Exception x) {
			page = "0";
		}
		Reference ref = null;
		
		try {
			ref = new Reference(String.format("%s/dataset/%s",queryService, 
					attachment.getIdquerydatabase()>0?Integer.toString(attachment.getIdquerydatabase()):
					Reference.encode(attachment.getTitle())));
			ref.addQueryParameter("pagesize", pagesize);
			ref.addQueryParameter("page", page);

			try {
				List<Structure> records = Structure.retrieveStructures(
						queryService, ref.toString());
				return records.iterator();
			} catch (Exception x) {
				throw createException(Status.CLIENT_ERROR_BAD_REQUEST, search,null, ref.toString(), x);
			}
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw createException(Status.CLIENT_ERROR_BAD_REQUEST, search,
					null, ref.toString(), x);
		}
	

	}
	
	protected DBAttachment verifyDataset(String aKey) throws Exception {
		DBAttachment result = null;
		Connection conn = null;
		QueryExecutor  exec = new QueryExecutor();
		try {
			ReadAttachment query;
			DBAttachment attachment = null;
			if ((aKey!=null) && aKey.toString().startsWith("A")) {
				attachment = new DBAttachment(new Integer(Reference.decode(aKey.toString().substring(1))));
				query = new ReadAttachment(null,getAttachmentDir());
				query.setValue(attachment);
				DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
				conn = dbc.getConnection();
				exec.setConnection(conn);
				ResultSet rs = exec.process(query);
				while (rs.next()) {
					result = query.getObject(rs);
				}
				rs.close();
				return result;
			} else return null;
		} catch (NumberFormatException x) {
			return null;
		} catch (Exception x) {
			try { if (exec!=null) exec.close(); } catch (Exception xx) {}
			try { if (conn!=null) conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
		
	}
	

	public String getConfigFile() {
		return "conf/qmrf-db.pref";
	}
	
	protected String getAttachmentDir() {
		String dir = ((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_attachments_dir.name());
		return dir==null?System.getProperty("java.io.tmpdir"):dir;
	}
}
