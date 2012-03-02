/**
 * Created on 2005-3-28
 * Modified 2012-1-2
 *
 */
package net.idea.ambit.swing.interfaces;

import java.util.EventListener;

import net.idea.ambit.swing.events.AmbitObjectChanged;


/**
 * To receive notification of an {@link AmbitObject} change event
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-7
 */
public interface IAmbitObjectListener<T extends AmbitObject> extends EventListener {
    /**
     * Receives notification of an AmbitObject change event.
     *
     * @param event  information about the event.
     */
    public void ambitObjectChanged(AmbitObjectChanged<T> event);	
}
