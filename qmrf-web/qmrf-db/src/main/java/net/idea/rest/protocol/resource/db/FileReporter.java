package net.idea.rest.protocol.resource.db;

import java.io.File;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.r.QueryReporter;
import net.idea.rest.protocol.attachments.DBAttachment;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;


public class FileReporter extends QueryReporter<DBAttachment, IQueryRetrieval<DBAttachment>, FileRepresentation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1470164118404153388L;

	public FileReporter() {
		super();
	}
	
	@Override
	public void header(FileRepresentation output,
			IQueryRetrieval<DBAttachment> query) {
	}

	@Override
	public void footer(FileRepresentation output,
			IQueryRetrieval<DBAttachment> query) {
	}

	@Override
	public Object processItem(DBAttachment item) throws AmbitException {
		try {
			System.out.println(item.getResourceURL().getFile());
			System.out.println(item.getResourceURL().toExternalForm());
			System.out.println(item.getResourceURL().toURI());
			File file = new File(item.getResourceURL().toURI());
			setOutput(new FileRepresentation(file, new MediaType(item.getMediaType())));
			return item;
		} catch (Exception x) {
			x.printStackTrace();
			throw new AmbitException(item.getResourceURL().toExternalForm(),x);
		}
	}

}
