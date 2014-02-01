/*
Copyright (C) 2005-2012  

Contact: www.ideaconsult.net

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

package net.idea.ambit.qmrf.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class QMRFSchemaResolver implements EntityResolver {
	public static final String defaultLocation="http://qmrf.sourceforge.net/qmrf.dtd";
	protected transient Logger logger = Logger.getLogger(getClass().getName());
    protected String location;
    protected boolean ignoreSystemID = false;
    public QMRFSchemaResolver(String location) {
        super();
        this.location = location;
    }
    
    private final static String _l_system = "systemID";
    private final static String _l_public = "publicID";
    private final static String _l_predefined = "predefined location";
    private final static String _l_internal = "internal";
    private final static String internal_dtd =  "ambit2/qmrfeditor/qmrf.dtd";
    
    // This method is called whenever an external entity is accessed
    // for the first time.
    public InputSource resolveEntity(String publicId, String systemId)
    {
        try
        {
            
        	InputStream in = null;
            try {
            	if (ignoreSystemID || (systemId==null)) throw new Exception("Ignore systemID="+systemId);
            	dtdlog(_l_system,systemId);
                
                in = new URL(systemId).openStream();
                if (in == null) throw new LocationNotFoundException(_l_system,systemId);                
            } catch (Exception x) {
                try {
                	if (publicId==null) throw new Exception("PublicID not defined");
                	dtdlog(_l_public,publicId);
                    in = new URL(publicId).openStream();
                    if (in == null) throw new LocationNotFoundException(_l_public,publicId);
            
                } catch (Exception e) {
                    try {
                    	if (location==null) throw new Exception("The predefined location is not defined");
                    	dtdlog(_l_predefined, location);
                        in = new URL(location).openStream();
                        if (in == null) throw new LocationNotFoundException(_l_predefined, location);
                    } catch (Exception xx) {
                    	dtdlog(_l_internal,internal_dtd);
                        String filename = internal_dtd;
                        in = this.getClass().getClassLoader().getResourceAsStream(filename);
 
                    }
                }
            }
            if (in != null)
            	return new InputSource(in);
            else return null;
        }
        catch (Exception e) { 
        	if (logger != null)
        		logger.log(Level.SEVERE,e.getMessage(),e);
        }
        
        // Returning null causes the caller to try accessing the systemid
        return null;
    }
    
    private void dtdlog(String location, String message) {
    	if (location==null) logger.info(message);
    	else if (location.startsWith("http")) logger.warning(message + " " + location);
    	else logger.fine("trying DTDSchema " + message + " " + location);
    }
    
	public boolean isIgnoreSystemID() {
		return ignoreSystemID;
	}

	public void setIgnoreSystemID(boolean ignoreSystemID) {
		this.ignoreSystemID = ignoreSystemID;
	}
    
}

class LocationNotFoundException extends Exception {
	public LocationNotFoundException(String locationtype,String location) {
		super(locationtype + " " + location + " not found");
	}
}
