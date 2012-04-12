package net.idea.rest.endpoints;

import net.idea.modbcum.i.IQueryRetrieval;
import net.idea.restnet.c.ResourceDoc;
import net.idea.restnet.db.QueryURIReporter;

import org.restlet.Request;
import org.restlet.data.Reference;

import ambit2.base.data.Dictionary;
import ambit2.base.data.Property;

/**
 * Generates uri of {@link PropertyResource}
 * @author nina
 *
 */
public class DictionaryURIReporter extends QueryURIReporter<Dictionary, IQueryRetrieval<Dictionary>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711954935147788056L;

	public DictionaryURIReporter(Reference baseRef,ResourceDoc doc) {
		super(baseRef,doc);
	}
	public DictionaryURIReporter(Request ref,ResourceDoc doc) {
		super(ref,doc);
	}
	public DictionaryURIReporter() {
		this((Request)null,null);
	}
	@Override
	public String getURI(String ref, Dictionary record) {
		
		if (record==null) return null;
		
		return String.format("%s%s/%s/%s%s",
			ref,
			EndpointsResource.resource,
			record.getReference()==null?"All":
			Reference.encode(record.getTitle()),
			Reference.encode(record.getName()),
			getDelimiter()
		);
		

	}

}
