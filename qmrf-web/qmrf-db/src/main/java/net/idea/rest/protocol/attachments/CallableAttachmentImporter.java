package net.idea.rest.protocol.attachments;

import java.sql.Connection;

import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.rest.protocol.attachments.db.UpdateAttachment;
import net.idea.restnet.db.update.CallableDBUpdateTask;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Reference;

/**
 * Imports structure files into the remote ambit service via OpenTox API
 * @author nina
 *
 */
public class CallableAttachmentImporter extends  CallableDBUpdateTask<DBAttachment,Form,String> {
	protected AttachmentURIReporter reporter;
	protected DBAttachment attachment;
	public CallableAttachmentImporter(Method method, Reference baseReference,
			AttachmentURIReporter reporter,
			DBAttachment attachment,
			Form input,Connection connection, String token) {
		super(method, input, connection, token);
		this.reporter = reporter;
		this.attachment = attachment;
	}

	@Override
	protected DBAttachment getTarget(Form input) throws Exception {
		try {
			boolean result = remoteImport(attachment);
			attachment.setImported(result);
			return attachment;
		} catch (Exception x) {
			throw x;
		}
	}

	@Override
	protected IQueryUpdate<Object, DBAttachment> createUpdate(
			DBAttachment target) throws Exception {
		return new UpdateAttachment(null,target);
	}

	@Override
	protected String getURI(DBAttachment target) throws Exception {
		return String.format("%s/dataset",reporter.getURI(target));
	}
	
	@Override
	protected boolean isNewResource() {
		return false;
	}

	protected boolean remoteImport(DBAttachment target) throws Exception {
		//TODO
		System.out.println(target.getTitle());
		return false;
	}
	
	@Override
	public String toString() {
		if (Method.POST.equals(method)) {
			return String.format("Import dataset");
		} else if (Method.PUT.equals(method)) {
			return String.format("Update dataset");
		} else if (Method.DELETE.equals(method)) {
			return String.format("Delete dataset");
		}
		return "Read dataset";
	}
}
