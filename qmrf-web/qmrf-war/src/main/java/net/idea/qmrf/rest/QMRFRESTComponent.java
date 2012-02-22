package net.idea.qmrf.rest;

import net.idea.restnet.c.RESTComponent;

import org.restlet.Application;
import org.restlet.Context;

/**
 * This is used as a servlet component instead of the core one, to be able to attach protocols 
 * @author nina
 *
 */
public class QMRFRESTComponent extends RESTComponent {
		public QMRFRESTComponent() {
			this(null);
		}
		public QMRFRESTComponent(Context context,Application[] applications) {
			super(context,applications);
			
		
		}
		public QMRFRESTComponent(Context context) {
			this(context,new Application[]{new QMRFApplication()});
		}
		
		

}
