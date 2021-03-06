package net.idea.rest.protocol.attachments;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.modbcum.r.QueryReporter;
import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.QMRFQueryResource;
import net.idea.rest.db.QDBConnection;
import net.idea.rest.protocol.CallableProtocolUpload;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.attachments.db.ReadAttachment;
import net.idea.rest.protocol.resource.db.DownloadDocumentConvertor;
import net.idea.rest.protocol.resource.db.FileReporter;
import net.idea.rest.protocol.resource.db.ProtocolQueryURIReporter;
import net.idea.restnet.c.ChemicalMediaType;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.c.task.TaskCreator;
import net.idea.restnet.db.DBConnection;
import net.idea.restnet.db.convertors.QueryHTMLReporter;
import net.idea.restnet.user.DBUser;

import org.apache.commons.fileupload.FileItem;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class ProtocolAttachmentResource extends QMRFQueryResource<IQueryRetrieval<DBAttachment>, DBAttachment> {
    public static final String resourceKey = "aid";
    protected DBProtocol protocol = null;

    public ProtocolAttachmentResource() {
	super();
    }

    @Override
    protected void customizeVariants(MediaType[] mimeTypes) {
	super.customizeVariants(mimeTypes);
	getVariants().add(new Variant(MediaType.APPLICATION_POWERPOINT));
	getVariants().add(new Variant(MediaType.APPLICATION_MSOFFICE_DOCX));
	getVariants().add(new Variant(MediaType.APPLICATION_MSOFFICE_PPTX));
	getVariants().add(new Variant(MediaType.APPLICATION_WORD));
	getVariants().add(new Variant(MediaType.APPLICATION_RTF));
	getVariants().add(new Variant(MediaType.APPLICATION_OPENOFFICE_ODT));
	getVariants().add(new Variant(MediaType.APPLICATION_TEX));
	getVariants().add(new Variant(MediaType.APPLICATION_TAR));
	getVariants().add(new Variant(MediaType.APPLICATION_ZIP));
	getVariants().add(new Variant(MediaType.APPLICATION_ALL));
	getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_MDLSDF));
	getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_MDLMOL));
	getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_SMILES));
	getVariants().add(new Variant(ChemicalMediaType.WEKA_ARFF));
	getVariants().add(new Variant(ChemicalMediaType.CHEMICAL_CML));
	getVariants().add(new Variant(MediaType.TEXT_CSV));
	getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }

    @Override
    public IProcessor<IQueryRetrieval<DBAttachment>, Representation> createConvertor(Variant variant)
	    throws AmbitException, ResourceException {
	String filenamePrefix = getRequest().getResourceRef().getPath();
	if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST))
	    return new StringConvertor(new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(getRequest(),
		    protocol == null ? "" : String.format("%s/%s", Resources.protocol, protocol.getIdentifier())),
		    MediaType.TEXT_URI_LIST, filenamePrefix);
	else if (variant.getMediaType().equals(MediaType.TEXT_HTML))
	    return new StringConvertor(createHTMLReporter(headless), MediaType.TEXT_HTML, filenamePrefix);
	else if (variant.getMediaType().equals(MediaType.APPLICATION_JSON))
	    return new StringConvertor(createJSONReporter(), MediaType.APPLICATION_JSON);
	else
	    return new DownloadDocumentConvertor(createFileReporter(), null, filenamePrefix);
    }

    @Override
    protected QueryHTMLReporter createHTMLReporter(boolean headless) throws ResourceException {
	AttachmentHTMLReporter rep = new AttachmentHTMLReporter(protocol, getRequest(), true, null, getHTMLBeauty());
	rep.setHeadless(headless);
	return rep;
    }

    @Override
    protected QueryReporter createJSONReporter() throws ResourceException {
	return new AttachmentJSONreporter<>(getRequest(),getQueryService());
    }

    protected FileReporter createFileReporter() throws ResourceException {
	return new FileReporter();
    }

    @Override
    protected IQueryRetrieval<DBAttachment> createQuery(Context context, Request request, Response response)
	    throws ResourceException {
	final Object key = request.getAttributes().get(FileResource.resourceKey);
	final Object aKey = request.getAttributes().get(ProtocolAttachmentResource.resourceKey);
	try {
	    ReadAttachment query = null;
	    DBAttachment attachment = null;
	    if ((aKey != null) && aKey.toString().startsWith("A"))
		try {
		    attachment = new DBAttachment(new Integer(Reference.decode(aKey.toString().substring(1))));
		} catch (Exception x) {
		    attachment = null;
		}
	    if ((key == null) && (attachment == null))
		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
	    else if (key != null) {
		/*
		 * int id[] =
		 * ReadProtocol.parseIdentifier(Reference.decode(key.toString
		 * ())); protocol = new DBProtocol(id[0],id[1],id[2]);
		 * protocol.setIdentifier
		 * (ReadProtocol.generateIdentifier(protocol));
		 */
		protocol = new DBProtocol(Reference.decode(key.toString()));
		query = new ReadAttachment(protocol, getAttachmentDir());

	    } else
		query = new ReadAttachment(getAttachmentDir());
	    query.setValue(attachment);
	    return query;
	} catch (ResourceException x) {
	    throw x;
	} catch (Exception x) {
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, String.format("Invalid protocol id %d", key),
		    x);
	}
    }

    @Override
    protected String getExtension(MediaType mediaType) {
	String ext = super.getExtension(mediaType);
	if (ext == null) {
	    if (MediaType.APPLICATION_WORD.equals(mediaType))
		return ".doc";
	    if (MediaType.APPLICATION_RTF.equals(mediaType))
		return ".rtf";
	    if (MediaType.APPLICATION_EXCEL.equals(mediaType))
		return ".xls";
	    else if (MediaType.APPLICATION_GNU_TAR.equals(mediaType))
		return ".tar";
	    else if (MediaType.APPLICATION_ZIP.equals(mediaType))
		return ".zip";
	    else if (MediaType.APPLICATION_MSOFFICE_DOCX.equals(mediaType))
		return ".docx";
	    else if (MediaType.APPLICATION_MSOFFICE_PPTX.equals(mediaType))
		return ".pptx";
	    else if (MediaType.APPLICATION_MSOFFICE_XLSX.equals(mediaType))
		return ".xslx";
	    else if (MediaType.APPLICATION_OPENOFFICE_ODT.equals(mediaType))
		return ".odt";
	    else if (MediaType.APPLICATION_LATEX.equals(mediaType))
		return ".tex";

	    else
		return "";
	}
	return ext;
    }

    @Override
    protected TaskCreator getTaskCreator(Form form, final Method method, boolean async, final Reference reference)
	    throws Exception {
	if (Method.DELETE.equals(method)) {
	    TaskCreator taskCreator = super.getTaskCreator(form, method, async, reference);
	    taskCreator.getProcessors().setAbortOnError(true);
	    return taskCreator;
	} else
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Not multipart web form!");
    }

    @Override
    protected IQueryRetrieval<DBAttachment> createUpdateQuery(Method method, Context context, Request request,
	    Response response) throws ResourceException {
	Object key = request.getAttributes().get(FileResource.resourceKey);
	Object aKey = request.getAttributes().get(ProtocolAttachmentResource.resourceKey);
	if (key == null)
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, String.format("No protocol ID"));
	if (Method.POST.equals(method)) {
	    if (aKey == null) // post allowed only on /protocol/id/attachment
		return null;

	} else if (Method.DELETE.equals(method)) {
	    if (key != null)
		return super.createUpdateQuery(method, context, request, response);
	    // if (key!=null) return super.createUpdateQuery(method, context,
	    // request, response);
	}
	throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    @Override
    protected boolean isAllowedMediaType(MediaType mediaType) throws ResourceException {
	return MediaType.MULTIPART_FORM_DATA.equals(mediaType);
    }

    @Override
    protected CallableProtectedTask<String> createCallable(Method method, Form form, DBAttachment item)
	    throws ResourceException {
	AttachmentURIReporter reporter = new AttachmentURIReporter(getRequest(), "");
	Connection conn = null;
	try {
	    DBConnection dbc = new QDBConnection(getApplication().getContext(), getDbConfig());
	    conn = dbc.getConnection();
	    String ambituser = ((TaskApplication) getApplication()).getProperty(Resources.AMBIT_LOCAL_USER);
	    String ambitpass = ((TaskApplication) getApplication()).getProperty(Resources.AMBIT_LOCAL_PWD);
	    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(ambituser, ambitpass);
	    return new CallableAttachmentImporter(method, getRequest().getRootRef(), reporter, item, form, null, conn,
		    creds);
	} catch (Exception x) {
	    try {
		conn.close();
	    } catch (Exception xx) {
	    }
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, x);
	}
    }

    @Override
    protected CallableProtectedTask<String> createCallable(Method method, List<FileItem> input, DBAttachment item)
	    throws ResourceException {
	DBUser user = null;
	if ((getRequest().getClientInfo().getUser() == null)
		|| (getRequest().getClientInfo().getUser().getIdentifier() == null)) {
	    user = null;
	    // DELETE is protected by a filter
	    /*
	     * throw new
	     * ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED.getCode(),
	     * "Upload not allowed",
	     * "Only logged in users with editor rights may upload new documents"
	     * , Status.CLIENT_ERROR_UNAUTHORIZED.getUri());
	     */
	} else {
	    user = new DBUser();
	    user.setUserName(getRequest().getClientInfo().getUser().getIdentifier());
	}
	if (protocol == null)
	    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "No protocol id");

	Connection conn = null;
	try {

	    ProtocolQueryURIReporter r = new ProtocolQueryURIReporter(getRequest(), "");
	    class TDBConnection extends QDBConnection {
		public TDBConnection(Context context, Properties configFile) {
		    super(context, configFile);
		}

		public String getDir() {
		    loadProperties();
		    return getAttachmentDir();
		}
	    }
	    ;
	    TDBConnection dbc = new TDBConnection(getApplication().getContext(), getDbConfig());
	    conn = dbc.getConnection();

	    String dir = dbc.getDir();
	    if ("".equals(dir))
		dir = null;
	    CallableProtocolUpload callable = new CallableProtocolUpload(method, protocol, user, input, conn, r,
		    getToken(), getRequest().getRootRef().toString(), dir == null ? null : new File(dir),
		    ((TaskApplication) getApplication()).getResolver());
	    callable.setSetDataTemplateOnly(true);
	    return callable;
	} catch (ResourceException x) {
	    throw x;
	} catch (Exception x) {
	    try {
		conn.close();
	    } catch (Exception xx) {
	    }
	    throw new ResourceException(Status.SERVER_ERROR_INTERNAL, x);
	}

    }

    @Override
    protected HTMLBeauty getHTMLBeauty() {
	if (htmlBeauty == null)
	    htmlBeauty = new QMRF_HTMLBeauty(Resources.attachment);
	return htmlBeauty;
    }
}
