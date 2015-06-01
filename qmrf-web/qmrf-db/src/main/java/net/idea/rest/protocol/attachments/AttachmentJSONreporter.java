package net.idea.rest.protocol.attachments;

import java.io.IOException;
import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.r.QueryReporter;
import net.idea.rest.JSONUtils;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Reference;

public class AttachmentJSONreporter<Q extends IQueryRetrieval<DBAttachment>> extends
	QueryReporter<DBAttachment, Q, Writer> {
    protected String comma = null;
    protected Request request;
    protected String queryService;

    public Request getRequest() {
	return request;
    }

    public void setRequest(Request request) {
	this.request = request;
    }

    protected Reference baseReference;

    public Reference getBaseReference() {
	return baseReference;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3413963693803394393L;

    public AttachmentJSONreporter(Request request,String queryService) {
	super();
	this.baseReference = (request == null ? null : request.getRootRef());
	setRequest(request);
	this.queryService = queryService;

    }
    @Override
    public void footer(Writer output, Q query) {
	try {
	    output.write("\n]\n}");
	} catch (Exception x) {
	}
    };

    @Override
    public void header(Writer output, Q query) {
	try {
	    output.write("{\"attachment\": [");
	} catch (Exception x) {
	}

    };
    private static String format = "\n{\n\t\"visibleid\":%s,\n\t\"id\": %d,\n\t\"dataset\": %s,\n\t\"title\": %s,\n\t\"type\": %s,\n\t\"description\": %s}";
    @Override
    public Object processItem(DBAttachment item) throws Exception {
	try {
	    if (comma != null)
		getOutput().write(comma);

	    getOutput().write(
		    String.format(
			    format,
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(item.getQMRFDocument().getVisibleIdentifier())),
			    item.getID(),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(String.format("%s/dataset/%d",queryService,item.getIdquerydatabase()))),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(item.getTitle())),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(item.getType().name())),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(item.getDescription()))
			   ));
	    comma = ",";
	} catch (IOException x) {
	    Context.getCurrentLogger().severe(x.getMessage());
	}
	return item;
    }    

}
