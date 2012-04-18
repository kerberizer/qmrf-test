package net.idea.rest.user.alerts.resource;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.rest.user.alerts.db.DBAlert;
import net.idea.restnet.db.QueryURIReporter;
import net.toxbank.client.Resources;

import org.restlet.Request;

/**
 * Generates URI for {@link AlertDBResource}
 * @author nina
 *
 * @param <Q>
 */
public class AlertURIReporter <Q extends IQueryRetrieval<DBAlert>> extends QueryURIReporter<DBAlert, Q> {
	String suffix = "";
	

	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8868430033131766579L;
	public AlertURIReporter(Request baseRef,String suffix) {
		super(baseRef,null);	
		this.suffix = suffix;
	}
	public AlertURIReporter(Request baseRef) {
		this(baseRef,"");
	}
	public AlertURIReporter() {
		this(null);
	}	

	@Override
	public String getURI(String ref, DBAlert item) {

		return String.format("%s%s/U%d%s/A%d%s",ref,Resources.user,item.getUser().getID(),Resources.alert,item.getID(),suffix);
	}

}
