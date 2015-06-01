package net.idea.rest.user.alerts.resource;

import java.io.IOException;
import java.io.Writer;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.DbAmbitException;
import net.idea.modbcum.r.QueryReporter;
import net.idea.rest.JSONUtils;
import net.idea.restnet.user.DBUser;
import net.idea.restnet.user.alerts.db.DBAlert;
import net.idea.restnet.user.alerts.resource.AlertURIReporter;
import net.idea.restnet.user.resource.UserURIReporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.Reference;

public class AlertJSONReporter<Q extends IQueryRetrieval<DBAlert>> extends QueryReporter<DBAlert, Q, Writer> {
    /**
	 * 
	 */
    private static final long serialVersionUID = -4566136103208284105L;
    protected String comma = null;
    protected Request request;
    protected AlertURIReporter<IQueryRetrieval<DBAlert>> uriReporter;
    protected UserURIReporter<IQueryRetrieval<DBUser>> userReporter;

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

    public AlertJSONReporter(Request request) {
	this.baseReference = (request == null ? null : request.getRootRef());
	setRequest(request);
	uriReporter = new AlertURIReporter<IQueryRetrieval<DBAlert>>(request);
	userReporter = new UserURIReporter<IQueryRetrieval<DBUser>>(request);
    }

    /**
     * <http://ambit.uni-plovdiv.bg:8080/qmrf/user/U113/alert/A2> a ncal:Event ;
     * dcterms:subject "pagesize=100" ; dcterms:title "/protocol" ;
     * ncal:attendee tbu:U113 ; ncal:categories "FREETEXT" ; ncal:rrule [ a
     * ncal:RecurrenceRule ; ncal:freq ncal:monthly ; ncal:interval "1"^^xsd:int
     * ] .
     * 
     * ncal:monthly a ncal:RecurrenceFrequency .
     * 
     * tbu:U113 a ncal:Attendee , tb:User .
     */
    private static String format = "\n{\n\t\"uri\":%s,\n\t\"id\": %s,\n\t\"attendee\": %s,\n\t\"title\": %s,\n\t\"categories\": %s,\n\t\"freq\": %s,\n\t\"interval\": %d}";

    // output.write("Title,First name,Last name,user name,email,Keywords,Reviewer\n");

    @Override
    public Object processItem(DBAlert alert) throws Exception {
	try {
	    if (comma != null)
		getOutput().write(comma);

	    String uri = alert.getID() > 0 ? uriReporter.getURI(alert) : "";

	    getOutput().write(
		    String.format(
			    format,
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(uri)),
			    alert.getID(),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(userReporter.getURI(alert.getUser()))),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(alert.getTitle())),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(alert.getQuery().getType().name())),
			    JSONUtils.jsonQuote(JSONUtils.jsonEscape(alert.getRecurrenceFrequency().name())),
			    alert.getRecurrenceInterval()
			   ));
	    comma = ",";
	} catch (IOException x) {
	    Context.getCurrentLogger().severe(x.getMessage());
	}
	return alert;
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
	    output.write("{\"alert\": [");
	} catch (Exception x) {
	}

    };

    public void open() throws DbAmbitException {

    }

    @Override
    public void close() throws Exception {
	setRequest(null);
	super.close();
    }

    @Override
    public String getFileExtension() {
	return null;
    }
}