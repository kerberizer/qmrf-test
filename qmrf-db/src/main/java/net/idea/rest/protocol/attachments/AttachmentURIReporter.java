package net.idea.rest.protocol.attachments;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.rest.protocol.db.ReadProtocolByAuthor;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;

public class AttachmentURIReporter <Q extends IQueryRetrieval<DBAttachment>> extends QueryURIReporter<DBAttachment, Q> {
	String prefix = "";


	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8868430033131766579L;
	public AttachmentURIReporter(Request baseRef,String prefix) {
		super(baseRef,null);	
		this.prefix = prefix;
	}
	public AttachmentURIReporter(Request baseRef) {
		this(baseRef,"");
	}
	public AttachmentURIReporter() {
		this(null);
	}	

	@Override
	public String getURI(String ref, DBAttachment item) {
		StringBuilder b = new StringBuilder();
		b.append(ref);
		b.append(prefix);
		b.append(Resources.attachment);
		b.append("/A");
		b.append(Integer.toString(item.getID()));
		return b.toString();
		
		//return String.format("%s%s%s/A%d",ref,prefix,Resources.attachment,item.getID());
	}

}