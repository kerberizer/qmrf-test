package net.idea.rest.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.idea.modbcum.c.DatasourceFactory;
import net.idea.modbcum.i.exceptions.AmbitException;
import net.idea.restnet.db.DBConnection;

import org.restlet.Context;

public class CustomDBConnection extends DBConnection{
    protected int maxRetry = 30;
    protected boolean testConnection = true;
    protected int timeout = 5;

    public int getTimeout() {
	return timeout;
    }

    public void setTimeout(int timeout) {
	this.timeout = timeout;
    }

    public boolean isTestConnection() {
	return testConnection;
    }

    public void setTestConnection(boolean testConnection) {
	this.testConnection = testConnection;
    }
    public CustomDBConnection(Context context,String configFile) {
	super(context,configFile);
    }
    
    @Override
    public synchronized Connection getConnection(String connectionURI) throws AmbitException, SQLException {
	SQLException error = null;
	Connection c = null;

	for (int retry = 0; retry < maxRetry; retry++)
	    try {
		error = null;
		DataSource ds = DatasourceFactory.getDataSource(connectionURI);

		c = ds.getConnection();
		if (testConnection) {
		    if (!c.isValid(timeout))
			throw new SQLException(String.format("Invalid connection on attempt %d", retry));
		}
		return c;
	    } catch (SQLException x) {
		error = x;
		Context.getCurrentLogger().warning(x.getMessage());
		// remove the connection from the pool
		try {
		    if (c != null)
			c.close();
		} catch (Exception e) {
		}
	    } catch (Throwable x) {
		Context.getCurrentLogger().severe(x.getMessage());
		try {
		    if (c != null)
			c.close();
		} catch (Exception e) {
		}
	    } finally {

	    }
	if (error != null)
	    throw error;
	else
	    throw new SQLException("Can't establish connection " + connectionURI);
    }
}
