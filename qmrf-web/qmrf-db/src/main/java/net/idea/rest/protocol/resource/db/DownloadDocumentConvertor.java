package net.idea.rest.protocol.resource.db;

import java.io.File;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.r.QueryReporter;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.restnet.db.convertors.AbstractObjectConvertor;
import net.idea.restnet.db.convertors.QueryRepresentationConvertor;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;

public class DownloadDocumentConvertor extends QueryRepresentationConvertor<DBAttachment, IQueryRetrieval<DBAttachment>,FileRepresentation> {

	public DownloadDocumentConvertor(
			QueryReporter<DBAttachment, IQueryRetrieval<DBAttachment>, FileRepresentation> reporter,MediaType media,String fileNamePrefix) {
		super(reporter,media,fileNamePrefix);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1979008352251532084L;

	protected File getFile(DBAttachment item) throws Exception  {
		return new File(item.getResourceURL().toURI());
	}
/*
	public Representation process(DBAttachment item) throws AmbitException {
		try {
			if (item==null) throw new AmbitException("No attachment!");

			File file = getFile(item);
			if (!file.exists()) throw new AmbitException("No file!");
			return new FileRepresentation(file, MediaType.APPLICATION_PDF);
			
		} catch (AmbitException x) {
			throw x;
		} catch (Exception x) {
			throw new AmbitException(x);
		}

	}
	*/
	
	
	@Override
	public Representation process(IQueryRetrieval<DBAttachment> query)
			throws Exception {
		//reporter.setOutput(createOutput(query));
		Representation r =  processDoc(reporter.process(query));
		try { reporter.close(); } catch (Exception x) {}
		return r;
	};	
	
	public Representation processDoc(FileRepresentation doc) throws AmbitException {
		return doc;
	}


}
