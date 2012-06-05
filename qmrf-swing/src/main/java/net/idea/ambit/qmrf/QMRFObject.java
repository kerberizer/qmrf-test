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

package net.idea.ambit.qmrf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.idea.ambit.qmrf.attachments.QMRFAttachments;
import net.idea.ambit.qmrf.catalogs.Catalog;
import net.idea.ambit.qmrf.catalogs.CatalogReference;
import net.idea.ambit.qmrf.catalogs.Catalogs;
import net.idea.ambit.qmrf.chapters.AbstractQMRFChapter;
import net.idea.ambit.qmrf.chapters.QMRFChapter;
import net.idea.ambit.qmrf.chapters.QMRFSubChapterReference;
import net.idea.ambit.qmrf.chapters.QMRFSubChapterText;
import net.idea.ambit.qmrf.swing.QMRFWelcomePanel;
import net.idea.ambit.qmrf.xml.InterfaceQMRF;
import net.idea.ambit.qmrf.xml.QMRFSchemaResolver;
import net.idea.ambit.qmrf.xml.XMLException;
import net.idea.ambit.swing.events.AmbitObjectChanged;
import net.idea.ambit.swing.interfaces.AmbitList;
import net.idea.ambit.swing.interfaces.AmbitObject;
import net.idea.ambit.swing.interfaces.IAmbitObjectListener;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import ambit2.base.interfaces.IAmbitEditor;
import ambit2.base.io.SimpleErrorHandler;
import ambit2.base.log.AmbitLogger;

/**
 * QMRF document.
 * <pre>
<!ELEMENT QMRF (QSAR_identifier,QSAR_General_information,QSAR_Endpoint,
QSAR_Algorithm,QSAR_Applicability_domain+,QSAR_Robustness,QSAR_Predictivity+,
QSAR_Interpretation,QSAR_Miscelaneous,Catalogs)>   
<!ATTLIST QMRF 
          version CDATA  #FIXED "1.2"
          name CDATA #FIXED "(Q)SAR Model Reporting Format" 
          author CDATA #FIXED "European Chemicals Bureau" 
          date CDATA #FIXED "July 2007" 
          contact CDATA #FIXED "European Chemicals Bureau, IHCP, Joint Research Centre, European Commission" 
          email CDATA #FIXED "JRC-IHCP-COMPUTOX@ec.europa.eu" 
          www CDATA #FIXED "http://ihcp.jrc.ec.europa.eu/our_labs/computational_toxicology" 
          >
   </pre>       
   
 * @author Nina Jeliazkova
 *
 */
public class QMRFObject extends AmbitObject implements InterfaceQMRF, IAmbitObjectListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6799167225641068546L;
	protected static AmbitLogger logger = new AmbitLogger(QMRFObject.class);
	protected final static String qmrf_version = "1.2";
    protected final static String qmrf_chapters = "QMRF_chapters";
    protected final static String qmrf_catalogs = "Catalogs";    
	protected QMRFAttributes attributes;
	protected ArrayList<QMRFChapter> chapters;
	public ArrayList<QMRFChapter> getChapters() {
		return chapters;
	}
	protected Catalogs catalogs;
	
	protected Catalogs external_catalogs;
    protected String dtdSchema = "http://ambit.sourceforge.net/qmrf/qmrf.dtd";
    protected String xmlSample = "http://ambit.sourceforge.net/qmrf/qmrf.xml";
    protected String ttfFontUrl = "http://ambit.sourceforge.net/qmrf/jws/times.ttf";
    protected boolean adminUser = false;
    protected String source = "New";
    protected QMRFChapter selectedChapter = null;
    protected boolean saveSelectedOnly = false;
    protected boolean attachmentReadOnly = false;
	
	protected static final String[] attrNames = {"name","version","author","date","contact","email","url"};
	protected String[] attrValues = {"(Q)SAR Model Reporting Format",
			"1.3","Joint Research Centre, European Commission","May 2012",
			"EUROPEAN COMMISSION, DIRECTORATE GENERAL, JOINT RESEARCH CENTRE, Institute for Health and Consumer Protection, Toxicology and Chemical Substances Unit",
			"JRC-IHCP-COMPUTOX@ec.europa.eu","http://ihcp.jrc.ec.europa.eu/our_labs/computational_toxicology"};	
    
    protected String[][] chaptersID = {
            {"QSAR_identifier","QSAR identifier"},
            {"QSAR_General_information","General information"},
            {"QSAR_Endpoint","Defining the endpoint - OECD Principle 1"},
            {"QSAR_Algorithm","Defining the algorithm - OECD Principle 2"},
            {"QSAR_Applicability_domain","Defining the applicability domain - OECD Principle 3"},
            {"QSAR_Robustness","Defining goodness-of-fit and robustness - OECD Principle 4"},
            {"QSAR_Predictivity","Defining predictivity - OECD Principle 4"},
            {"QSAR_Interpretation","Providing a mechanistic interpretation - OECD Principle 5"},
            {"QSAR_Miscelaneous","Miscellaneous information"},
            {"QMRF_Summary","Summary (JRC Inventory)"}
    };

    public void setCleanTags(boolean cleanTags) {
        for (int i=0; i < chapters.size();i++) 
        	chapters.get(i).setCleanTags(cleanTags);
    }
    
	public QMRFObject() {
    	this(null,false);
    }
	public QMRFObject(String[] args, boolean adminUser) {
		super("QMRF");
/*
        System.setProperty("javax.xml.datatype.DatatypeFactory", "org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.validation.SchemaFactory", "org.apache.xerces.jaxp.validation.XMLSchemaFactory");
        System.setProperty("org.w3c.dom.DOMImplementationSourceList", "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
        System.setProperty("org.xml.sax.driver", "org.apache.xerces.parsers.SAXParser");
        System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema","org.apache.xerces.jaxp.validation.XMLSchemaFactory");
*/
		setAdminUser(adminUser);
		external_catalogs = new Catalogs();
		external_catalogs.setEditable(false);
		attributes = new QMRFAttributes();//attrNames,attrValues);
		chapters = new ArrayList<QMRFChapter>();
		catalogs = new Catalogs();
		setParameters(args);

        setModified(true);
	}
	public QMRFObject(String[] args, InputStream in, boolean adminUser) throws Exception {
		this(args,adminUser);
		
		read(in);
	}
	
	public String getNumber() {
		try {
			QMRFSubChapterText ch10 = (QMRFSubChapterText)chapters.get(chapters.size()-1).getSubchapters().getItem(0);
			return ch10.getText();
		} catch (Exception x) {
			return "";
		}
	}
	
	public String getQmrftitle() {
		try {
			QMRFSubChapterText ch1 = (QMRFSubChapterText)chapters.get(0).getSubchapters().getItem(0);
			return ch1.getText();
		} catch (Exception x) {
			return "";
		}
	}
	public void init() {

		try {
	        String filename = "ambit2/qmrfeditor/qmrf.xml";
	        try {
	        	InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
	        	transform_and_read(new InputStreamReader(in,"UTF-8"),true);	
	        	in.close();
	        } catch (Exception x) {
	        	x.printStackTrace();
	        	
	        	InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
				read(in);
		        in.close();
		        setNotModified();
	        }
            
		} catch (Exception x) {
			x.printStackTrace();
        	clear();
        }		
	}
	public void readDefaultCatalogs(Catalogs external) {
		try {
			
			external.clear();
	        String filename = "ambit2/qmrfeditor/catalogs.xml";
	        try {
	        	InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
	        	
	        	external.read(new InputSource(in));
	        } catch (Exception x) {
	        	x.printStackTrace();
	        	
	        	InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
				read(in);
		        in.close();
	        }
            setModified(true);
		} catch (Exception x) {
			x.printStackTrace();
        	
        }		
	}	
	public void clear() {
		setSelectedChapter(null);
		if (catalogs!=null) catalogs.clear();
		if (chapters!=null) {
	        for (int i=0; i < chapters.size();i++)
	            chapters.get(i).removeAmbitObjectListener(this);
	        chapters.clear();
		} 
		if (attributes!=null) attributes.clear();
        setModified(true);
	}
	public AbstractQMRFChapter findChapter(Object query) {
	    int i = chapters.indexOf(query);
        if (i != -1) return chapters.get(i); else return null;
    }
	public Iterator<QMRFChapter> chaptersIterator() {
		return chapters.iterator();
	}
	@Override
	public String toString() {
		return "Welcome";
	}
	public static String getBackground() {
		return "The set of information that you provide will be used to facilitate regulatory considerations of (Q)SARs. For this purpose, the structure of the QMRF is devised to reflect as much as possible the OECD principles for the validation, for regulatory purposes, of (Q)SAR models. <br>You are invited to consult the OECD <i>\"Guidance Document on the Validation of (Quantitative) Structure-Activity Relationship Models\"</i> that can aid you in filling in a number of fields of the QMRF (visit the following webpage for downloading the proper documentation: <a href=\"http://ihcp.jrc.ec.europa.eu/our_labs/computational_toxicology/background/oecd-principles\">OECD principles</a>)</html>";
	}
	protected String getProperty(String property) {
		Object o = attributes.get(property);
		if (o==null) return ""; 
		else return o.toString();
	}

	protected void setProperty(String property, String version) {
		attributes.put(property, version);
        setModified(true);
	}
	
	public String getVersion() {
		return getProperty("version");
	}

	public void setVersion(String version) {
		setProperty("version", version);
	}
	
	public String getTitle() {
		return getProperty("name");
	}
	
	public void setTitle(String title) {
		setProperty("name",title);
	}
	
	public void setAuthor(String author) {
		setProperty("author",author);
	}
	public String getAuthor() {
		return getProperty("author");
	}	
	public void setDate(String date) {
		setProperty("date",date);
	}
	public String getDate() {
		return getProperty("date");
	}	
	public void setContact(String property) {
		setProperty("contact",property);
	}
	public String getContact() {
		return getProperty("contact");
	}	
	public void setEmail(String property) {
		setProperty("email",property);
	}
	public String getEmail() {
		return getProperty("email");
	}	
	public void setWWW(String property) {
		setProperty("url",property);
	}		
	
	public String getWWW() {
		return getProperty("url");
	}	
	@Override
	public IAmbitEditor editor(boolean editable) {
		return new QMRFWelcomePanel(this,false);
	}
    
    protected void addChapter(QMRFChapter chapter) {
        chapter.addAmbitObjectListener(this);
        chapters.add(chapter);
    }
	/*
	 * (non-Javadoc)
	 * @see ambit.data.XMLSerializable#fromXML(org.w3c.dom.Element)
	 */
	public void fromXML(Element xml) throws XMLException {
		clear();
		attributes.fromXML(xml);
        logger.info("Reading catalogs");
        catalogs.fromXML(xml);
        Enumeration<String> tags = catalogs.keys();
        while (tags.hasMoreElements()) {
        	String tag = tags.nextElement();
        	Catalog ct = catalogs.get(tag);
            if (ct != null) try {
            	ct.setSaveSelectedOnly(saveSelectedOnly);
                ct.setExternal_catalog(external_catalogs.get(tag));
            } catch (Exception x) {
            	x.printStackTrace();
            }
        }

        logger.info("Reading chapters");
        for (int c=0; c < chaptersID.length; c++) {
            NodeList nodes = xml.getElementsByTagName(chaptersID[c][0]);
            for (int i=0; i < nodes.getLength(); i++) 
                if (Node.ELEMENT_NODE==nodes.item(i).getNodeType()) {

                    QMRFChapter chapter = new QMRFChapter(nodes.item(i).getNodeName());
                    chapter.setCatalogs(catalogs);
                    //System.out.println(chapter.getName());
                    chapter.fromXML((Element)nodes.item(i));
                    if ("10".equals(chapter.getChapter())) 
                    	chapter.setEditable(adminUser);
                    else 
                    	chapter.setEditable(true);
                    AmbitList subchapters = chapter.getSubchapters();
                    for (int j=0; j < subchapters.size();j++) {
                    	if (subchapters.getItem(j) instanceof QMRFAttachments)
                    		((QMRFAttachments)subchapters.getItem(j)).setReadOnly(isAttachmentReadOnly());	
                    }
                    
                    		
                    addChapter(chapter);
                }
        }            

        setModified(true);
       
	}

	public Element toXML(Document document) throws XMLException {
		Element top = document.createElement(getName());
		attributes.toXML(top);
        Element chapters = document.createElement(qmrf_chapters);
        top.appendChild(chapters);
		Iterator<QMRFChapter> i = chaptersIterator();
		while (i.hasNext()) 
			chapters.appendChild(i.next().toXML(document));
		
		Element e_catalogs = document.createElement(qmrf_catalogs);
		top.appendChild(e_catalogs);
		
		cleanCatalogs();
        for (int c=0; c < Catalog.catalog_names.length; c++) {
            Catalog cc = catalogs.get(Catalog.catalog_names[c][0]);
            if (cc != null) {
                e_catalogs.appendChild(cc.toXML(document));
            }
        }
     
		return top;
	}
    /**
     * Writes current QMRF document into XML file.
     * @param out
     * @throws Exception
     */
	public void write(OutputStream out) throws Exception {
        write(buildDocument(),out);
    }
	public void write(Writer out) throws Exception {
        write(buildDocument(),out);
    }	
	/**
	 * Writes QMRF document into XML file. 
     * @param doc
     * @param out
     * @throws Exception
	 */
	protected void write(Document doc, OutputStream out) throws Exception {
		write(doc,new OutputStreamWriter(out,"UTF-8"));
    }	
	protected void write(Document doc, Writer out) throws Exception {
		Source source;
		try {
			DOMResult result = transform_help(doc, false);
			Document newdoc = (Document) result.getNode();
	        newdoc.normalize();
			source = new DOMSource(newdoc);	        
		} catch (Exception x) {
			source = new DOMSource(doc);
		}

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, dtdSchema);
        xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "qmrf.dtd");
        xformer.setOutputProperty(OutputKeys.INDENT,"Yes");
        xformer.setOutputProperty(OutputKeys.STANDALONE,"No");

    
        Result result = new StreamResult(out);
        xformer.transform(source, result);
        out.flush();
        setNotModified();
        fireAmbitObjectEvent();
    }		
    /**
     * Inserts strings into help attribute of QMRF chapters. Done by XSLT transformation.
     * The stylesheets are ambit2/qmrfeditor/qmrf_insert_help.xsl and ambit/data/qmrf/qmrf_delete_help.xsl.
     * @param doc
     * @param appendHelp
     * @return
     * @throws Exception
     */
    public DOMResult transform_help(Document doc, boolean appendHelp ) throws Exception {
		DOMResult result = new DOMResult();
		String filename = "ambit2/qmrfeditor/qmrf_insert_help.xsl";
		if (!appendHelp)
			filename = "ambit2/qmrfeditor/qmrf_delete_help.xsl";		
        InputStream xslt = this.getClass().getClassLoader().getResourceAsStream(filename);
		transform(new DOMSource(doc), new StreamSource(xslt), result);
		xslt.close();
		return result;
    }

    protected Document buildDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        factory.setNamespaceAware(true);      
        factory.setValidating(true);        
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler( new SimpleErrorHandler(builder.getClass().getName()) );
        Document doc = builder.newDocument();
        Element e = toXML(doc);
        doc.appendChild(e);
        return doc;
    }

	/**
     * Feeds current document into xslt transform. 
     * @param xslt  XSLT stylesheet (e.g. XSL file generating HTML from QMRF)
     * @param out   The result (e.g. HTML presentation of QMRF document).
     * @throws IOException
     * @throws TransformerException
	 */
	public void xsltTransform(InputStream xslt,OutputStream out) 
	    throws IOException, TransformerException {
		try {
            transform(new DOMSource(buildDocument()), new StreamSource(xslt), new StreamResult(out));
		} catch (Exception x) {
			throw new TransformerException(x);
		}
	}
    /**
     * Runs XSLT Transformation.
     * @param sourceDocument The document to be transformed.
     * @param xslt  XSLT stylesheet.
     * @param result  The transformed document.
     * @throws IOException
     * @throws TransformerException
     */
	protected void transform(Source sourceDocument, Source xslt, Result result)   throws IOException, TransformerException {
		try {
				//Setup XSLT
			TransformerFactory xfactory = TransformerFactory.newInstance();
			Transformer transformer = xfactory.newTransformer(xslt);
			transformer.transform(sourceDocument, result);
	
		} catch (Exception x) {
			throw new TransformerException(x);
		}
	}	
	
	public void xsltTransform(Reader input, InputStream xslt, Result result)   throws IOException, TransformerException {
		try {
            transform(new DOMSource(readDocument(new InputSource(input),true,new QMRFSchemaResolver(dtdSchema,logger))), 
                    new StreamSource(xslt), result);

		} catch (Exception x) {
			throw new TransformerException(x);
		}
	}	
    /*
    public void export2PDF(InputStream xsltfo, OutputStream pdf) throws Exception {
        Document doc = buildDocument();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xslt = builder.parse(xsltfo);     
        DOMResult result = new DOMResult();
        transform(new DOMSource(doc),new DOMSource(xslt), result);
        Document foDom = (Document) result.getNode();
        convertDOM2PDF(foDom, pdf);
    }
    */
	/**
     * Converts FO DOM Document into PDF (uses Apache FOP).
     * @param foDom  Document FO namespace xmlns:fo="http://www.w3.org/1999/XSL/Format"
     * @param pdf

    protected void convertDOM2PDF(Document foDom, OutputStream pdf) {
        try {
        	
            
            // configure fopFactory as desired
            FopFactory fopFactory = FopFactory.newInstance();
            
            // xsl-fo namespace URI 
            String foNS = "http://www.w3.org/1999/XSL/Format";        	
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
    
            // Setup output
            
            BufferedOutputStream  out = new java.io.BufferedOutputStream(pdf);
    
            try {
                // Construct fop with desired output format and output stream
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                
                // Setup Identity Transformer
                TransformerFactory xfactory = TransformerFactory.newInstance();
                Transformer transformer = xfactory.newTransformer(); // identity transformer
                
                // Setup input for XSLT transformation
                Source src = new DOMSource(foDom);
                
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
                
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            } finally {
                out.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }

    }
    */
    
    public Document readDocument(InputSource source, boolean validating, EntityResolver resolver ) throws Exception {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        /*
        factory.setNamespaceAware(true);      
        factory.setValidating(validating);
        */
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        builder.setErrorHandler( new SimpleErrorHandler(getClass().getName()) );
                
        // Install the entity resolver
        if (resolver != null)
            builder.setEntityResolver(resolver);
        
        
        return builder.parse(source);

    }    
    public void transform_and_read(Reader in, boolean appendHelp) throws Exception {

		DOMResult result = new DOMResult();
		String filename = "ambit2/qmrfeditor/qmrf_insert_help.xsl";
		if (!appendHelp)
			filename = "ambit2/qmrfeditor/qmrf_delete_help.xsl";		
        InputStream xslt = this.getClass().getClassLoader().getResourceAsStream(filename);
		xsltTransform(in, xslt, result);
		xslt.close();
		
		Document doc = (Document) result.getNode();
        doc.normalize();

		fromXML(doc.getDocumentElement());
        setNotModified();
        fireAmbitObjectEvent();		
    }
    
    public void read(InputStream in) throws Exception {
        read(new InputStreamReader(in,"UTF-8"));
        
    }
    public void read(Reader reader) throws Exception {
        
    	QMRFSchemaResolver resolver = new QMRFSchemaResolver(dtdSchema,logger);
    	resolver.setIgnoreSystemID(true);
        Document doc = readDocument(new InputSource(reader),false,resolver);
        
        //Schema schema = factory.getSchema();
        fromXML(doc.getDocumentElement());
        
        setNotModified();
        fireAmbitObjectEvent();
    }
    
    /**
     * Example:
     * <pre>
     * -e http://nina.acad.bg/qmrf/catalogs_xml.jsp?all=true  -c
     * </pre>
     * @return
     */
    protected Options createOptions() {
    	
        Options options = new Options();
        Option dtdschema   = OptionBuilder.withLongOpt("dtd")
                            .withArgName( "url" )
                            .hasArg()
                            .withDescription(  "DTD schema location - URL where DTD schema resides (e.g. -dfile:///D:/src/ambit/qmrf.dtd or -dhttp://ambit.sourceforge.net/qmrf/qmrf.dtd ) ")
                            .create( "d" );

        Option content   = OptionBuilder.withLongOpt("xmlcontent")
        .withArgName( "URL" )
        .hasArg()
        .withDescription(  "URL to retrieve XML content")
        .create( "x" );
        
        Option ttf   = OptionBuilder.withLongOpt("ttf")
        .withArgName( "URL" )
        .hasArg()
        .withDescription(  "URL to retrieve TrueType font")
        .create( "t" );
        
        Option help   = OptionBuilder.withLongOpt("help")
        .withDescription(  "This screen")
        .create( "h" );     
        

        Option admin   = OptionBuilder.withLongOpt("user")
        .withArgName( "username" )
        .hasArg()
        .withDescription(  "User (if -u admin then Chapter 10 will be editable, otherwise readonly")
        .create( "u" );
        
        Option endpoints   = OptionBuilder.withLongOpt("external")
        .withArgName( "URL" )
        .hasArg()
        .withDescription(  "URL to retrieve external catalogs in XML format as defined by <!ELEMENT Catalogs > in QMRF DTD schema")
        .create( "e" );        

        Option catalog_cleanup   = OptionBuilder.withLongOpt("cleancatalogs")
        .withDescription(  "When saving as XML, include only catalog entries which have an idref reference")
        .create( "c" );
        
        Option readonlyAttachments   = OptionBuilder.withLongOpt("readonly-attachments")
        .withDescription(  "Forbids adding/deleting attachments")
        .create( "r" );                  
        
        options.addOption(dtdschema);
        options.addOption(content);
        options.addOption(admin);
        options.addOption(help);
        options.addOption(endpoints);
        options.addOption(catalog_cleanup);
        options.addOption(ttf);
        options.addOption(readonlyAttachments);
        

        return options;
    }    
    public void setParameters(String[] args) {
        try {
        	if (args == null) return;
    	
            Options options = createOptions();
            CommandLineParser parser = new PosixParser();
            CommandLine line = parser.parse( options, args,false );
            if (line.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "QMRFEditor", options );
                Runtime.getRuntime().runFinalization();                      
                Runtime.getRuntime().exit(0);
            }
            setOptions(line);
        }
        
        catch( ParseException exp ) {
            logger.error( exp);
        }
    }
    
    protected void setOptions(CommandLine line) {
        String url ;

        Option[] o = line.getOptions();

        if( line.hasOption( "d" ) ) {
            url = line.getOptionValue( "d" );
            try {
                dtdSchema = url;
            } catch (Exception x) {
                 logger.error(x);
                 dtdSchema = "http://ambit.acad.bg/qmrf/qmrf.dtd"; 
                 logger.info("Will be using DTD schema at "+dtdSchema);
            }
        } 
        if( line.hasOption( "t" ) ) {
        	setTtfFontUrl(line.getOptionValue( "t" ));
        }
        if( line.hasOption( "r" ) ) {
        	setAttachmentReadOnly(true);
        }   else 
        	setAttachmentReadOnly(false);
        
        if( line.hasOption( "u" ) ) {
        	adminUser = "admin".equals(line.getOptionValue( "u" ));
        }        
        if( line.hasOption( "c" ) ) {
        	setSaveSelectedOnly(true);
        }
        if( line.hasOption( "e" ) ) {
        	url = line.getOptionValue( "e" );
        	
        	try {
        		System.out.println("reading catalogs from URL "+url);
        		external_catalogs.read(new InputSource(new InputStreamReader(new URL(url).openStream(),"UTF-8")));
    	    
        	} catch (Exception x) {
        		System.err.println(x.getMessage());
        		readDefaultCatalogs(external_catalogs);
        	} finally {

        	}

        }        
        if (external_catalogs.size() == 0)
        	readDefaultCatalogs(external_catalogs);
        
        if( line.hasOption( "x" ) ) {
        	URI remoteFile  = null;
        	try {
	        	remoteFile = new URI(line.getOptionValue( "x" ));
	        	init();
	        	readURI(remoteFile);
        	} catch (MalformedURLException x) {
        		System.err.println(String.format("Invalid URL %s: %s", line.getOptionValue( "x" ),x.getMessage()));
        	}  catch (Exception x) {
        		x.printStackTrace();
        		System.err.println(String.format("Error reading URL %s: %s", remoteFile,x.getMessage()));
	        } finally {
	        	
	        }
        }
     
    }

    public void readURI(URI remoteFile) throws Exception {
    	HttpClient cli = new DefaultHttpClient();
    		System.out.println(String.format("Reading QMRF XML from %s", remoteFile));
    		HttpGet httpGet = new HttpGet(remoteFile);
    		httpGet.addHeader("Accept","application/xml");
    		httpGet.addHeader("Accept-Charset", "utf-8");

    		InputStream in = null;
    		try {
    			HttpResponse response = cli.execute(httpGet);
    			HttpEntity entity  = response.getEntity();
    			in = entity.getContent();
    			if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
    				read(new InputStreamReader(in,"UTF-8"));
    	        	System.out.println(String.format("Reading %s completed.", remoteFile));

    			} else 	
    				 throw new IOException(String.format("Error reading URL %s\n%s",remoteFile,response.getStatusLine()));
    		} catch (Exception x) {	
    			throw x;
    		} finally {
    			try {if (in != null) in.close();} catch (Exception x) {}
    		}        		
        	

    }
	public QMRFAttributes getAttributes() {
		return attributes;
	}
	public void setAttributes(QMRFAttributes attributes) {
		this.attributes = attributes;
	}
    public synchronized Hashtable<String, Catalog> getCatalogs() {
        return catalogs;
    }
    public synchronized void setCatalogs(Catalogs catalogs) {
        this.catalogs = catalogs;
    }
    public synchronized String getSource() {
        return source;
    }
    public synchronized void setSource(String source) {
        this.source = source;
    }
    public void ambitObjectChanged(AmbitObjectChanged event) {
        setModified(event.getObject().isModified());
        
    }
    @Override
    public void setNotModified() {
        for (int i=0; i < chapters.size();i++)
            chapters.get(i).setNotModified();
        
        super.setNotModified();
    }
	public boolean isAdminUser() {
		return adminUser;
	}
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}
    /**
     * Masks as selected only items with a idref somwehere
     *
     */
	public void cleanCatalogs() {
		Hashtable<String,Catalog> c = getCatalogs();
		Iterator<Catalog> cc = c.values().iterator();
		while (cc.hasNext()) {
			Catalog a = cc.next();
			for (int k=0; k < a.size();k++)
				a.getItem(k).setSelected(false);
		}
		
		for (int i=0; i < chapters.size();i++) {
			AmbitList subchapters = chapters.get(i).getSubchapters();
			for (int j=0; j < subchapters.size();j++) 
				if (subchapters.getItem(j) instanceof QMRFSubChapterReference) {
					CatalogReference ref = ((QMRFSubChapterReference) subchapters.getItem(j)).getCatalogReference();
					for (int k=0; k < ref.size();k++) {
						ref.getItem(k).setSelected(true);
						
					}	
				}
		}	
		
		/*
		cc = c.values().iterator();
		while (cc.hasNext()) {
			Catalog a = cc.next();
			for (int k=a.size()-1; k >= 0;k--)
				if (!a.getItem(k).isSelected()) {
					a.remove(k);
				}	
		}
		*/
	}
	public QMRFChapter getSelectedChapter() {
		return selectedChapter;
	}
	public void setSelectedChapter(QMRFChapter selectedChapter) {
		this.selectedChapter = selectedChapter;
	}
	public boolean isSaveSelectedOnly() {
		return saveSelectedOnly;
	}
	public void setSaveSelectedOnly(boolean saveSelectedOnly) {
		this.saveSelectedOnly = saveSelectedOnly;
		if (catalogs != null)
        for (int c=0; c < Catalog.catalog_names.length; c++) {
            Catalog ct = catalogs.get(Catalog.catalog_names[c][0]);
            if (ct != null) ct.setSaveSelectedOnly(saveSelectedOnly);
        }
	}
	public String getTtfFontUrl() {
		return ttfFontUrl;
	}
	public void setTtfFontUrl(String ttfFontUrl) {
		this.ttfFontUrl = ttfFontUrl.trim();
		if (ttfFontUrl.indexOf("http://") != -1) {
			System.out.println("font have to be retrieved from URL '"+ttfFontUrl+"'");
		}	
	}
	public boolean isAttachmentReadOnly() {
		return attachmentReadOnly;
	}
	public void setAttachmentReadOnly(boolean attachmentReadOnly) {
		this.attachmentReadOnly = attachmentReadOnly;
	}
}


class SimpleDeclHandler implements org.xml.sax.ext.DeclHandler {
    protected QMRFObject o;
    public SimpleDeclHandler(QMRFObject o) {
        this.o = o;
    }
    public void attributeDecl(java.lang.String elementName,
                              java.lang.String attributeName,
                              java.lang.String type,
                              java.lang.String valueDefault,
                              java.lang.String value)
    {
    /*
       System.out.println("ATTRIBUTE: ");
       System.out.println("Element Name: " + elementName);
       System.out.println("Attribute Name: " + attributeName);
       System.out.println("Type: " + type);
       System.out.println("Default Value: " + valueDefault);
       System.out.println("Value: " + value);
       System.out.println();
       */
    }
    
    public void elementDecl(java.lang.String name,
                            java.lang.String model)
    {
    /*
       System.out.println("ELEMENT: ");
       System.out.println("Name: " + name);
       System.out.println("Model: " + model);
       System.out.println();
       */
    }
    
    public void externalEntityDecl(java.lang.String name,
                                   java.lang.String publicId,
                                   java.lang.String systemId)
    {
      System.out.println("EXTERNAL ENTITY: " + name + publicId + systemId);
    }
    
    public void internalEntityDecl(java.lang.String name,
                                  java.lang.String value)
    {
      /*
      if (name.startsWith("help")) {
          System.out.println("INTERNAL ENTITY HELP: " + name + value);
      } else System.out.println("INTERNAL ENTITY: " + name + value);
      */
    }

}

