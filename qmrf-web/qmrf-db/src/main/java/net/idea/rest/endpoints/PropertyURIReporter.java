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
public class PropertyURIReporter extends QueryURIReporter<Property, IQueryRetrieval<Property>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711954935147788056L;

	public PropertyURIReporter(Reference baseRef,ResourceDoc doc) {
		super(baseRef,doc);
	}
	public PropertyURIReporter(Request ref,ResourceDoc doc) {
		super(ref,doc);
	}
	public PropertyURIReporter() {
		this((Request)null,null);
	}
	@Override
	public String getURI(String ref, Property record) {
		
		if (record==null) return null;
		boolean isDictionary= record.getClazz().equals(Dictionary.class);
		
		if (isDictionary) {
			return String.format("%s%s/%s/%s%s",
					ref,
					EndpointsResource.resource,
					record.getReference()==null?"All":
					Reference.encode(record.getTitle()),
					Reference.encode(record.getName()),
					getDelimiter()
					);
		} else
		if (record.getId()>0)
			return String.format("%s%s/%d%s",ref,"/feature",record.getId(),getDelimiter());
		else
			return String.format("%s%s/%s%s",ref,"/feature",
					Reference.encode(record.getName()+record.getTitle()),
					getDelimiter());

	}

}
