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

package net.idea.ambit.swing.actions;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JFrame;

import net.idea.ambit.swing.common.GUIWorker;
import net.idea.ambit.swing.interfaces.IJobStatus;
import net.idea.ambit.swing.interfaces.ISharedData;


/**
 * 
 * An abstract action that executes in a separate thread. 
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> 2005-12-4
 */
public abstract class AmbitAction extends AbstractAction {
	protected static Logger logger = Logger.getLogger(AmbitAction.class.getName());
	protected GUIWorker worker = null;
	protected Object userData = null;
	protected Object actionName ="";
	protected Container mainFrame = null; //used as parent for child dialogs
	protected ActionMap actions = null;
    protected static Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    protected static Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);

	/**
	 * 
	 */
	private static final long serialVersionUID = 5979583679466214875L;

	/**
	 * 
	 * @param userData Arbitrary data
	 * @param mainFrame {@link JFrame} parent frame
	 */
	public AmbitAction(Object userData, Container mainFrame) {
		this(userData,mainFrame,"Go!",null);
		
	}
	/**
	 * 
	 * @param userData Arbitrary data
	 * @param mainFrame {@link JFrame} parent frame
	 * @param name {@link javax.swing.Action} name
	 */
	public AmbitAction(Object userData, Container mainFrame, String name) {
		this(userData,mainFrame,name,null);
	}

	/**
	 * 
	 * @param userData Arbitrary data
	 * @param mainFrame {@link JFrame} parent frame
	 * @param name {@link javax.swing.Action} name
	 * @param icon
	 */
	public AmbitAction(Object userData, Container mainFrame, String name, Icon icon) {
		super(name, icon);
		setUserData(userData);
		setMainFrame(mainFrame);
		worker = null;
	}

	/**
	 * This is where actual processing should take place. Override this method instead of actionPerformed method.
	 * This method sets {@link ambit.data.JobStatus} to {@link IJobStatus#STATUS_RUNNING}; 
	 * @param arg0
	 */
	public void run(ActionEvent arg0) {
		
		if (userData instanceof ISharedData) {
			((ISharedData) userData).getJobStatus().setStatus(IJobStatus.STATUS_RUNNING);
			((ISharedData) userData).getJobStatus().setMessage(actionName + " started");
		}		
	}
	/**
	 * This method is invoked when processing is completed (after {@link #run(ActionEvent)} is finished
	 * This method sets {@link ambit.data.JobStatus} to {@link IJobStatus#STATUS_DONE};
	 */
	public void done() {
		if (userData instanceof ISharedData) {
			((ISharedData) userData).getJobStatus().setStatus(IJobStatus.STATUS_DONE);
			((ISharedData) userData).getJobStatus().setMessage(actionName + " completed.");
		}		
	}
	/**
	 * Changes {@link AbstractAction} name to {@link AbstractAction#getValue(NAME)}
	 *
	 */
	protected void changeName() {
		actionName = getValue(NAME);
		setEnabled(false);
		setEnabledAll(false);
	    mainFrame.setCursor(hourglassCursor);

		//putValue(NAME,"Stop");
	}
	/**
	 * Restores action name
	 *
	 */
	public void restoreName() {
		putValue(NAME,actionName);
		setEnabled(true);
		setEnabledAll(true);
	    mainFrame.setCursor(normalCursor);

	}
	/**
	 * Spans a thread 
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (worker != null) {
			//worker.interrupt();
			//worker = null;
    		//restoreName();
    		return;
		}
		
		
        worker = new GUIWorker() {
            public Object construct() {
            	try {
            		changeName();
            		run(null);
            	} catch (Exception x) {
            		((ISharedData) userData).getJobStatus().setError(x);
            		//JOptionPane.showMessageDialog(mainFrame,x);
                    //x.printStackTrace();
            		worker = null;
            		restoreName();
            	}               	
                return null; //TODO return smth reasonable
            }
            //Runs on the event-dispatching thread.
            public void finished() {
                done();
                worker = null;
                restoreName();
            }
        };
        worker.start(); 
    }

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

	public Container getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(Container mainFrame) {
		this.mainFrame = mainFrame;
	}


	

    public synchronized ActionMap getActions() {
        return actions;
    }
    public synchronized void setActions(ActionMap actions) {
        this.actions = actions;
    }

    public void setEnabledAll(boolean newValue) {
        if (actions != null) {
            Object[] keys = actions.allKeys();
            if (keys !=null)
            for (int i=0;i< keys.length;i++)
                actions.get(keys[i]).setEnabled(newValue);
         }

    }
}
