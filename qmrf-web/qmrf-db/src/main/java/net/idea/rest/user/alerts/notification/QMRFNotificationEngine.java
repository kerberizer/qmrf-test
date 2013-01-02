package net.idea.rest.user.alerts.notification;



import java.io.IOException;

import net.idea.rest.protocol.DBProtocol;
import net.idea.restnet.user.alerts.notification.SimpleNotificationEngine;
import net.toxbank.client.io.rdf.ProtocolIO;

import org.restlet.data.Reference;

public class QMRFNotificationEngine extends SimpleNotificationEngine<DBProtocol> {
	protected ProtocolIO ioClass = new ProtocolIO();
    public QMRFNotificationEngine(Reference root) throws IOException {
	    super(root,"config/qmrf.properties");
	    setNotificationSubject("QMRF Inventory Alert Updates");
	}
    /*
    @Override
    protected List<String> retrieveByRIAP(Reference ref) throws Exception {
  	  ClientResource cr = null;
  	  Representation repr = null;
  	  try {
  		cr = new ClientResource(ref);
  		repr = cr.get(MediaType.APPLICATION_RDF_XML);
  		if (org.restlet.data.Status.SUCCESS_OK.equals(cr.getStatus())) {
  			List<String> urls = new ArrayList<String>();
  			Model model = ModelFactory.createDefaultModel();
  			model.read(repr.getStream(), null, "RDF/XML");
  			List<Protocol> protocols = ioClass.fromJena(model);
  			model.close();
  			model = null;
  			for (Protocol p : protocols) {
  				urls.add(String.format("%s\t%s\n%s\r\n",p.getIdentifier(), p.getTitle(), p.getResourceURL() ));
  			}
  			return urls;
  		}
  		} catch (ResourceException x) {
  			if (x.getStatus().equals(org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND)) {
  				//skip, this is ok
  			} else 
  				log.log(Level.WARNING,String.format("Error reading URL %s\n%s",ref.toString(),cr.getStatus()),x);
  			x.printStackTrace();
  		} catch (Exception x) {	
  			throw x;
  		} finally {
  			try {repr.release();} catch (Exception x) {}
  			try {cr.release();} catch (Exception x) {}
  		}     
  		return null;
    }
    */
}
