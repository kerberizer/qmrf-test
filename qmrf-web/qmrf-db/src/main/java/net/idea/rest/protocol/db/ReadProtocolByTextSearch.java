package net.idea.rest.protocol.db;

import java.util.ArrayList;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;

/**
 * Free text search, as supported byMySQL full text search. Relies on table keywords to be populated.
 * @author nina
 *
 */
public class ReadProtocolByTextSearch extends ReadProtocolByEndpointString {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5128395204960444566L;
	protected static String sql_natural = String.format(ReadProtocol.sql_withkeywords,
					"where ","published_status='published' and match (keywords.keywords) against (? IN NATURAL LANGUAGE MODE)");
	protected static String sql_boolean = String.format(ReadProtocol.sql_withkeywords,
					"where ","published_status='published' and match (keywords.keywords) against (? IN BOOLEAN MODE)");
	protected TextSearchMode mode = TextSearchMode.textnatural;
	
	public ReadProtocolByTextSearch() {
		this(TextSearchMode.textnatural);
	}
	public ReadProtocolByTextSearch(TextSearchMode mode) {
		super();
		setMode(mode);
	}
	public TextSearchMode getMode() {
		return mode;
	}

	public void setMode(TextSearchMode mode) {
		this.mode = mode;
	}

	public enum TextSearchMode {
		textnatural,
		textboolean;
	}

	public List<QueryParam> getParameters() throws AmbitException {
		List<QueryParam> params =  new ArrayList<QueryParam>();
		if (getFieldname()!=null) 
			params.add(new QueryParam<String>(String.class, getFieldname()));
		else throw new AmbitException("No search query name!");
		if (getValue()!=null && getValue().getTimeModified()!=null)
			params.add(new QueryParam<Long>(Long.class, getValue().getTimeModified()));
		return params;
	}

	@Override
	public String getLocalSQL() throws AmbitException {
		return TextSearchMode.textnatural.equals(mode)?sql_natural:sql_boolean;
	}

}
