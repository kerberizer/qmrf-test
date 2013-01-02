package net.idea.qmrf.rest;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;

/**
 * This is used as a servlet component instead of the core one, to be able to attach protocols 
 * @author nina
 *
 */
public class QMRFRESTComponent extends Component {

		
	public QMRFRESTComponent() {
			this(null);
	}

	public QMRFRESTComponent(Context context,Application[] applications) {
		super();
		this.getClients().add(Protocol.FILE);
		this.getClients().add(Protocol.HTTP);
		this.getClients().add(Protocol.HTTPS);
		this.getClients().add(Protocol.RIAP);

		getServers().add(Protocol.RIAP);
		
		for (Application application: applications) {
			application.setContext(context==null?getContext().createChildContext():context);
		    getDefaultHost().attach(application);
		}
	    getInternalRouter().attachDefault(applications[0]);	

	}
	
	public QMRFRESTComponent(Context context) {
		this(context,new Application[]{new QMRFApplication()});
	}
	
	
}
