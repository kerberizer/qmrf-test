package net.idea.rest.protocol.resource.db;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.db.ReadProtocolByAuthor;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;


/**
 * Generates URI for {@link ReferenceResource}
 * @author nina
 *
 * @param <Q>
 */
public class ProtocolQueryURIReporter <Q extends IQueryRetrieval<DBProtocol>> extends QueryURIReporter<DBProtocol, Q> {
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
	public ProtocolQueryURIReporter(Request baseRef,String suffix) {
		super(baseRef,null);	
		this.suffix = suffix;
	}
	public ProtocolQueryURIReporter(Request baseRef) {
		this(baseRef,"");
	}
	public ProtocolQueryURIReporter() {
		this(null);
	}	

	@Override
	public String getURI(String ref, DBProtocol item) {
		StringBuilder b = new StringBuilder();
		b.append(ref);
		b.append(Resources.protocol);
		b.append("/");
		b.append(ReadProtocolByAuthor.generateIdentifier(item));
		b.append(suffix==null?"":suffix);
		return b.toString();
		/*
		return String.format("%s%s/%s%s",
				ref,
				Resources.protocol,
				ReadProtocolByAuthor.generateIdentifier(item),
				suffix==null?"":suffix);
				*/
	}

}
