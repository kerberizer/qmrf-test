package net.idea.rest.groups.resource;

import java.io.IOException;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.rest.JSONUtils;
import net.idea.restnet.groups.IDBGroup;
import net.idea.restnet.groups.resource.GroupJSONReporter;

import org.restlet.Context;
import org.restlet.Request;

/**
 * Workaround to properly encode json
 * @author nina
 *
 * @param <Q>
 */
public class QMRFGroupJSONReporter <Q extends IQueryRetrieval<IDBGroup>>  extends GroupJSONReporter<Q> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4929869701603276094L;
	public QMRFGroupJSONReporter(Request request) {
		super(request);
	}
	
	private static String format1 = "\n{\n\t\"uri\":\"%s\",\n\t\"id\": %s,\n\t\"groupname\": \"%s\",\n\t\"title\": \"%s\"\n}";
	@Override
	public Object processItem(IDBGroup user) throws Exception {
		try {
			if (comma!=null) getOutput().write(comma);
			
			String uri = user.getID()>0?uriReporter.getURI(user):"";
			
			getOutput().write(String.format(format1,
					uri,
					(user.getID()>0)?String.format("\"G%d\"",user.getID()):null,
					user.getGroupName()==null?"":JSONUtils.jsonEscape(user.getGroupName()),
					user.getTitle()==null?"":JSONUtils.jsonEscape(user.getTitle())
					));
			comma = ",";
		} catch (IOException x) {
			Context.getCurrentLogger().severe(x.getMessage());
		}
		return null;
	}	
}
