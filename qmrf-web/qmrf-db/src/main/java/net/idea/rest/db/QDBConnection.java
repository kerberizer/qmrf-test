package net.idea.rest.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.db.DBConnection;

import org.restlet.Context;

public class QDBConnection extends DBConnection {

	public QDBConnection(Context context, Properties config) {
		super(context, null);
		properties = config;
		loginInfo = getLoginInfo(context);
	}

	@Override
	protected synchronized void loadProperties() {
		if (properties == null) properties = new Properties();
	}
	@Override
	public synchronized Connection getConnection() throws AmbitException, SQLException {
	    Connection c = super.getConnection();
	    return c;
	}
	
}