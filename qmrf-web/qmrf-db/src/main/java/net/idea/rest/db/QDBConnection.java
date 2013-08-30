package net.idea.rest.db;

import java.util.Properties;

import net.idea.restnet.db.DBConnection;

import org.restlet.Context;

public class QDBConnection extends DBConnection {

	public QDBConnection(Context context, Properties config) {
		super(context, null);
		properties = config;
	}

}
