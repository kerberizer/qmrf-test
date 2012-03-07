package net.idea.rest.protocol.attachments;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.processors.IProcessor;
import net.idea.qmrf.client.Resources;
import net.idea.rest.FileResource;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.rest.protocol.db.template.ReadFilePointers;
import net.idea.rest.protocol.resource.db.DownloadDocumentConvertor;
import net.idea.rest.protocol.resource.db.FileReporter;
import net.idea.restnet.c.ChemicalMediaType;
import net.idea.restnet.c.StringConvertor;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.db.QueryResource;
import net.idea.restnet.db.convertors.QueryHTMLReporter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class ProtocolAttachmentResource extends QueryResource<IQueryRetrieval<DBAttachment>,DBAttachment> {
	public static final String resourceKey = "aid";
	protected String suffix = Resources.document;
	public static final String documentType = "documentType";
	protected DBProtocol protocol = null;
	
	public ProtocolAttachmentResource() {
		this(Resources.document);
	}
	public ProtocolAttachmentResource(String suffix) {
		super();
		this.suffix = suffix;
	}
	
	@Override
	protected void customizeVariants(MediaType[] mimeTypes) {
		super.customizeVariants(mimeTypes);
		getVariants().add(new Variant(MediaType.APPLICATION_POWERPOINT));
		getVariants().add(new Variant(MediaType.APPLICATION_MSOFFICE_DOCX));
		getVariants().add(new Variant(MediaType.APPLICATION_MSOFFICE_PPTX));
		getVariants().add(new Variant(MediaType.APPLICATION_WORD));
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
	}
	@Override
	public IProcessor<IQueryRetrieval<DBAttachment>, Representation> createConvertor(
			Variant variant) throws AmbitException, ResourceException {
		String filenamePrefix = getRequest().getResourceRef().getPath();
		if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) 
			return new StringConvertor(	
					new AttachmentURIReporter<IQueryRetrieval<DBAttachment>>(getRequest(),suffix)
					,MediaType.TEXT_URI_LIST,filenamePrefix);
		if (variant.getMediaType().equals(MediaType.TEXT_HTML)) 
			return new StringConvertor(createHTMLReporter(),MediaType.TEXT_HTML,filenamePrefix);	
			else	
				return new DownloadDocumentConvertor(createFileReporter(),null,filenamePrefix);
	}
	protected QueryHTMLReporter createHTMLReporter() throws ResourceException {
		return new AttachmentHTMLReporter(protocol,getRequest(),true,null);
	}
	
	protected FileReporter createFileReporter() throws ResourceException {
		return new FileReporter();
	}
	
	@Override
	protected IQueryRetrieval<DBAttachment> createQuery(Context context, Request request,
			Response response) throws ResourceException {
		final Object key = request.getAttributes().get(FileResource.resourceKey);	
		Object aKey = request.getAttributes().get(ProtocolAttachmentResource.resourceKey);	
		try {
			ReadFilePointers query = null;
			DBAttachment attachment = null;
			if ((aKey!=null) && aKey.toString().startsWith("A")) try {
				attachment = new DBAttachment(new Integer(Reference.decode(aKey.toString().substring(1))));
			} catch (Exception x) { attachment = null;}
			if ((key==null)&&(attachment==null)) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
			else if (key!=null) {
				int id[] = ReadProtocol.parseIdentifier(Reference.decode(key.toString()));
				protocol = new DBProtocol(id[0],id[1]);
				query = new ReadFilePointers(protocol);
				
			} else query = new ReadFilePointers(); 
			query.setValue(attachment);
			return query;
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(
					Status.CLIENT_ERROR_BAD_REQUEST,
					String.format("Invalid protocol id %d",key),
					x
					);
		}
	}
	@Override
	protected String getExtension(MediaType mediaType) {
		String ext = super.getExtension(mediaType);
		if (ext == null) {
			if (MediaType.APPLICATION_WORD.equals(mediaType))
				return ".doc";
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
			
			else return "";
		}
		return ext;
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty();
	}
	
	@Override
	public String getConfigFile() {
		return "conf/qmrf-db.pref";
	}
}