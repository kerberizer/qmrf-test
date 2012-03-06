package net.idea.rest.protocol.attachments;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;

public class AttachmentURIReporter <Q extends IQueryRetrieval<DBAttachment>> extends QueryURIReporter<DBAttachment, Q> {
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
	public AttachmentURIReporter(Request baseRef,String suffix) {
		super(baseRef,null);	
		this.suffix = suffix;
	}
	public AttachmentURIReporter(Request baseRef) {
		this(baseRef,"");
	}
	public AttachmentURIReporter() {
		this(null);
	}	

	@Override
	public String getURI(String ref, DBAttachment item) {
		return String.format("%s%s/A%d%s",ref,Resources.attachment,item.getID(),suffix);
	}

}