package net.idea.rest.user.alerts.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import net.idea.qmrf.client.Resources;
import net.idea.rest.user.DBUser;
import net.toxbank.client.resource.Alert;
import net.toxbank.client.resource.Query.QueryType;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

public class DBAlert extends Alert<DBUser> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4711297730366781673L;
	protected int ID;
	public enum _fields {
		idquery {
			@Override
			public void setParam(DBAlert alert, ResultSet rs) throws SQLException {
				alert.setID(rs.getInt(name()));
			}
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				if (value!=null)
					alert.setID(Integer.parseInt(value));
			}
		},
		name {
	
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getTitle();
			}
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				alert.setTitle(value);
			}
		},
		query {
	
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				alert.setQueryString(value);
			}
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getQueryString();
			}
		},
		qformat {
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				alert.setType(QueryType.valueOf(value));
				
			}
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getType();
			}
		},
		rfrequency {
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				alert.setRecurrenceFrequency(RecurrenceFrequency.valueOf(value));
				
			}
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getRecurrenceFrequency();
			}
		},
		rinterval {
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				alert.setRecurrenceInterval(Integer.parseInt(value));
			}
			@Override
			public void setParam(DBAlert alert, ResultSet rs) throws SQLException {
				alert.setRecurrenceInterval(rs.getInt(name()));
			}			
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getRecurrenceInterval();
			}
		},
		sent {
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				alert.setSentAt(Long.parseLong(value));
			}
			@Override
			public void setParam(DBAlert alert, ResultSet rs) throws SQLException {
				try {
					Timestamp date = rs.getTimestamp(name());
					alert.setSentAt(date.getTime());
				} catch (Exception x) {
					alert.setSentAt(0L);
				}
			}			
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getSentAt();
			}
		},		
		created {
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				alert.setCreated(Long.parseLong(value));
			}
			@Override
			public void setParam(DBAlert alert, ResultSet rs) throws SQLException {
				try {
					Timestamp date = rs.getTimestamp(name());
					alert.setCreated(date.getTime());
				} catch (Exception x) {
					alert.setCreated(0L);
				}
			}			
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getCreated();
			}
		},			
		iduser {
			@Override
			public void setValue(DBAlert alert, String value)
					throws SQLException {
				DBUser user = new DBUser(Integer.parseInt(value));
				alert.setUser(user);
			}
			@Override
			public void setParam(DBAlert alert, ResultSet rs) throws SQLException {
				DBUser user = new DBUser(rs.getInt(name()));
				user.setUserName(rs.getString("username"));
				user.setID(rs.getInt("iduser"));
				alert.setUser(user);
			}
			@Override
			public Object getValue(DBAlert alert) {
				return alert.getUser();
			}
		};
		public String getCondition() {
			return String.format("%s=?",name());
		}
		public void setParam(DBAlert alert, ResultSet rs) throws SQLException {
			setValue(alert, rs.getString(name()));
		}				
		public abstract void setValue(DBAlert alert, String value) throws SQLException;
		public Object getValue(DBAlert alert) {
			return null;
		}
		public String getDescription() { return toString();}
		public String getHTMLField(DBAlert alert) {
			Object value = getValue(alert);
			return String.format("<input name='%s' type='text' size='40' value='%s'>\n",
					name(),getDescription(),value==null?"":value.toString());
		}

	}
	
	public DBAlert() {
		super();
	}
			
	public DBAlert(int id) {
		this(id,null);
	}
	public DBAlert(DBUser user) {
		this(-1,user);
	}
	public DBAlert(int id, DBUser user) {
		super();
		setID(id);
		setUser(user);
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public void setQueryString(Reference queryURI) {
		Form form = queryURI.getQueryAsForm();
		form.removeAll("page");
		form.removeAll("pagesize");
		super.setQueryString(form.getQueryString());
	}

	public String getVisibleQuery() {
		Form form = new Form(getQueryString());
		String option = form.getFirstValue("option");
		String search = form.getFirstValue("search");
		return String.format("%s %s",option==null?"Free text":option,search==null?"All":search);
	}
	
	public String getRunnableQuery() {
		Form form = new Form(getQueryString());
		return String.format("%s?%s",Resources.protocol,form.getQueryString());
	}
	
}
