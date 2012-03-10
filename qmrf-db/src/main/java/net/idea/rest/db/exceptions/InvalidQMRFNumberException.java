package net.idea.rest.db.exceptions;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 * Invalid QMRF number exception
 * @author nina
 *
 */
public class InvalidQMRFNumberException extends ResourceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7218438568085514503L;

	public InvalidQMRFNumberException(String qmrfnumber) {
		super(Status.CLIENT_ERROR_BAD_REQUEST.getCode(),
			String.format("Invalid QMRF number %s",qmrfnumber),
			String.format("The QMRF number <i>%s</i> is not valid. The QMRF numbers should have format QMRF-year-number-version",qmrfnumber),
			null
			);
	}
}
