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

package net.idea.ambit.swing.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import ambit2.base.io.MyIOUtilities;

/**
 * Holds some default data for a database connection. Can save and load this setting into/from XML file.
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> 2005-12-18
 */
public class DefaultData extends Hashtable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -859541296839458311L;
	public static transient String TEMPLATES_DIR = "Templates";
    public static transient String DEFAULT_DIR = "Directory";
    public static transient String DATABASE = "Database";
    public static transient String HOST = "Host";
    public static transient String USER = "User";
    public static transient String PORT = "Port";
    public static transient String MYSQL_START = "StartMySQL";
    public static transient String PASSWORD = "Password";
    public static transient String FRAGMENTS = "";
    public static transient String TMPFILE = "TMPFILE";
    
    
    /**
     * 
     */
    public DefaultData() {
        super();
        put(DEFAULT_DIR,"");
        put(TEMPLATES_DIR,"data/templates");
        put(DATABASE,"ambit");
        put(HOST,"localhost");
        put(USER,"root");
        put(PORT,"33060");
        put(MYSQL_START,"True");
        put(FRAGMENTS,"");
        put(TMPFILE,"QUERY.TXT");
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        Enumeration e = keys();
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            Object value = get(key);
    		out.writeObject(key);
    		out.writeChar('\t');
    		out.writeObject(value);
    		out.writeChar('\n');
        }
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	    clear();
		    while (true) {
		        try {
			        Object key = in.readObject();
			        if (key == null) break;
			        in.readChar();
			        Object value = in.readObject();
			        in.readChar();
			        put(key,value);
			    } catch (Exception x) {
			        //x.printStackTrace();
			        break;
			    }		        
		    }
		    in.close();

	}	 
	public void readXML(InputStream in) {
	    DataInputStream data = new DataInputStream(in);
	    XMLReader parser = initParser();
        try {
            parser.setFeature("http://xml.org/sax/features/validation", false);
        } catch (SAXException e) {

        }
        parser.setContentHandler(new DefaultHandler() {
            /* (non-Javadoc)
             * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
             */
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes);
                if (localName.equals("property")) {
                    String key = attributes.getValue("name");
                    String value = attributes.getValue("value");
                    put(key,value);
                }
            }
        });
        try {
            parser.parse(new InputSource(in));
            in.close();
            parser = null;
        } catch (SAXException x) {
            x.printStackTrace();
        } catch (IOException x) {
            x.printStackTrace();
        }

	}


	public void writeXML(OutputStream out) {
	    DataOutputStream data = new DataOutputStream(out);
        Enumeration e = keys();
        
        try {
            data.writeBytes("<project name=\"ambit\">");
	        while (e.hasMoreElements()) {
	            Object key = e.nextElement();
	            Object value = get(key);
	            data.writeBytes("\n<property ");
	            data.writeBytes("name=\"");
	    		data.writeBytes(key.toString());
	    		data.writeBytes("\" value=\"");
	    		data.writeBytes(value.toString());
	    		data.writeBytes("\"/>");
	        }	 
	        data.writeBytes("\n</project>");
	        out.flush();
        } catch (IOException x) {
            
        }
	}
	
    public static XMLReader initParser() {
        Logger logger = Logger.getLogger(MyIOUtilities.class.getName());
        XMLReader parser = null;

        boolean success = false;
        // If JAXP is prefered (comes with Sun JVM 1.4.0 and higher)
        if (!success) {
            try {
                javax.xml.parsers.SAXParserFactory spf = javax.xml.parsers.SAXParserFactory.newInstance();
                spf.setNamespaceAware(true);
                javax.xml.parsers.SAXParser saxParser = spf.newSAXParser();
                parser = saxParser.getXMLReader();
                logger.info("Using JAXP/SAX XML parser.");
                success = true;
            } catch (Exception e) {
                logger.log(Level.WARNING,"Could not instantiate JAXP/SAX XML reader: "+e.getMessage());
                logger.log(Level.FINER,e.getMessage(),e);
            }
        }
        // Aelfred is first alternative.
        if (!success) {
            try {
                parser = (XMLReader)MyIOUtilities.class.getClassLoader().
                        loadClass("gnu.xml.aelfred2.XmlReader").
                        newInstance();
                logger.fine("Using Aelfred2 XML parser.");
                success = true;
            } catch (Exception e) {
                logger.fine("Could not instantiate Aelfred2 XML reader!");
            }
        }
        // Xerces is second alternative
        if (!success) {
            try {
                parser = (XMLReader)MyIOUtilities.class.getClassLoader().
                        loadClass("org.apache.xerces.parsers.SAXParser").
                        newInstance();
                logger.info("Using Xerces XML parser.");
                success = true;
            } catch (Exception e) {
                logger.fine("Could not instantiate Xerces XML reader!");
            }
        }
        if (!success) {
        	logger.log(Level.FINER,"Could not instantiate any XML parser!");
        }
        return parser;
    }	
}
