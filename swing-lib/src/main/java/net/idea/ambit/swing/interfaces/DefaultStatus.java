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

import java.util.Observable;


/**
 * Default implementation of {@link ambit.io.batch.IJobStatus}
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Aug 29, 2006
 */
public class DefaultStatus extends Observable implements IJobStatus {
	protected int status;
	protected boolean modified;
	protected Exception error = null;
	
	public DefaultStatus() {
		super();
		setStatus(STATUS_NOTSTARTED);
		modified = true;
	}

	public boolean isRunning() {
		return status == STATUS_RUNNING;
	}

	public boolean isPaused() {
		return status == STATUS_PAUSED;
	}

	public boolean isCancelled() {
		return status == STATUS_ABORTED;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		if (this.status != status) {
			this.status = status;
			if (status == STATUS_RUNNING)
				error = null;
			setChanged();
			notifyObservers();
		}

	}
	public String toString() {
		return statusMsg[status];
	}
	public boolean isStatus(int status) {
		return this.status == status;
	}
	/* (non-Javadoc)
     * @see ambit.io.batch.IJobStatus#isModified()
     */
    public boolean isModified() {
        return modified;
    }
    /* (non-Javadoc)
     * @see ambit.io.batch.IJobStatus#setModified(boolean)
     */
    public void setModified(boolean value) {
        if (modified != value) {
            modified = value;
            setChanged();
            notifyObservers();
        }
    }

	public Exception getError() {
		return error;
	}

	public void setError(Exception x) {
		this.error = x;
        setChanged();
        notifyObservers();		
	}
}
