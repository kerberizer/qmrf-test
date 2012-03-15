package net.idea.rest.protocol.attachments;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.c.task.TaskCreator;
import net.idea.restnet.c.task.TaskCreatorForm;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.i.task.ICallableTask;
import net.idea.restnet.i.task.Task;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.record.formula.functions.T;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class AttachmentDatasetResource extends ProtocolAttachmentResource {

	Object key ;
	Object aKey ;
	
	@Override
	protected IQueryRetrieval<DBAttachment> createUpdateQuery(Method method,
			Context context, Request request, Response response)
			throws ResourceException {
		key = request.getAttributes().get(FileResource.resourceKey);
		aKey = request.getAttributes().get(ProtocolAttachmentResource.resourceKey);	
		if (key==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,String.format("No protocol ID"));
		if (Method.POST.equals(method)) {
			if ((key==null)||(aKey==null) )  //post allowed only on /protocol/id/attachment/a1/dataset
				return null;
			else {
				return super.createQuery(context, request, response);
			}
		} //POST only so far
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);		
	}
	

	@Override
	protected boolean isAllowedMediaType(MediaType mediaType)
			throws ResourceException {
		return MediaType.APPLICATION_WWW_FORM.equals(mediaType);
	}
	@Override
	protected String getObjectURI(Form queryForm) throws ResourceException {
		return null;
	}
	
	@Override
	protected TaskCreator getTaskCreator(Form form, final Method method, boolean async, final Reference reference) throws Exception {
		return new TaskCreatorForm<Object,DBAttachment>(form,async) {
			@Override
			protected ICallableTask getCallable(Form form,
					DBAttachment item) throws ResourceException {
				return createCallable(method,form,item);
			}
			@Override
			protected Task<Reference, Object> createTask(
					ICallableTask callable,
					DBAttachment item) throws ResourceException {
					return addTask(callable, item,reference);
				}
		};
	}
	
	@Override
	protected TaskCreator getTaskCreator(List<FileItem> fileItems,
			Method method, boolean async) throws Exception {
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Not a web form!");
	}
	
	@Override
	protected TaskCreator getTaskCreator(File file, MediaType mediaType,
			Method method, boolean async) throws Exception {
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Not a web form!");
	}
	
	@Override
	protected CallableProtectedTask<String> createCallable(Method method,
			Form form, DBAttachment item) throws ResourceException {
		Connection conn = null;
		try {
			AttachmentURIReporter r = new AttachmentURIReporter(getRequest(),String.format("%s/%s",Resources.protocol,key.toString()));
			DBConnection dbc = new DBConnection(getApplication().getContext(),getConfigFile());
			conn = dbc.getConnection();
			return new CallableAttachmentImporter(method,getRequest().getRootRef(), r,item, form,getQueryService(), conn,getToken());
		} catch (Exception x) {
			try { conn.close(); } catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		}
	};

}
