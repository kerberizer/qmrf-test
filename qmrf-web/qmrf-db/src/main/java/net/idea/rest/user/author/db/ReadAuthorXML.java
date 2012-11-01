package net.idea.rest.user.author.db;

import java.sql.ResultSet;
import java.util.List;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.modbcum.i.query.QueryParam;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.protocol.DBProtocol;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.db.ReadUser;

public class ReadAuthorXML extends ReadUser<DBProtocol> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7580441130508605768L;
	/**
	 * 
	 */
	private static final String sql_xml = 
		"SELECT -1 as iduser,null as username,'' as title,'' as firstname,\n"+
		"getAuthorDetails(x,idprotocol,version,\"name\") as lastname,\n"+
		"getAuthorDetails(x,idprotocol,version,\"affiliation\") as affiliation,\n"+
		"null as weblog,\n"+
		"getAuthorDetails(x,idprotocol,version,\"url\") as homepage,\n"+
		"getAuthorDetails(x,idprotocol,version,\"email\") as email,\n"+
		"'' as keywords,false as reviewer from protocol join (\n"+
		"SELECT idprotocol,int_col,\n"+
		"trim(substring(SUBSTRING_INDEX(s, \" \", int_col+1), length(substring_index(s, \" \", int_col))+1)) x\n"+
		"FROM (SELECT 0 int_col union SELECT 1  union select 2 union select 3 union select 4 union select 5 union\n"+
		"select 6 union select 7 union select 8 union select 9 union select 10) vars,\n"+
		"(SELECT idprotocol, extractvalue(abstract,\"//authors_catalog/author/@id\") as s from protocol ) s\n"+
		"WHERE int_col < char_length(s)\n"+
		"and length(substring_index(s, \" \", int_col))+1 < length(s)\n"+
		") a using(idprotocol) group by lastname";
	
	public ReadAuthorXML(DBProtocol protocol,DBUser user) {
		super(user);
		setFieldname(protocol);
	}
	

	public List<QueryParam> getParameters() throws AmbitException {
		return null;
	}

	public String getSQL() throws AmbitException {
		return sql_xml;
			
	}	
	
	@Override
	public DBUser getObject(ResultSet rs) throws AmbitException {
		DBUser user = super.getObject(rs);
		try {
			DBOrganisation org = new DBOrganisation();
			org.setTitle(rs.getString("affiliation"));
			user.addOrganisation(org);
		} catch (Exception x) {
			
		}
		return user;
	}
}
