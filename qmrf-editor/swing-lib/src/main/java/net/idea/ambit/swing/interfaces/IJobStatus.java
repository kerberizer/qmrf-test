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

import java.io.Serializable;

/**
 * 
 * Interface for job status
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Aug 29, 2006
 */
public interface IJobStatus extends Serializable {
	public final static int STATUS_NOTSTARTED = 0;
	public final static int STATUS_RUNNING = 1;
	public final static int STATUS_DONE = 2;
	public final static int STATUS_PAUSED = 3;
	public final static int STATUS_ABORTED = 4;
	public final static int STATUS_NOTINITIALIZED = 5;
	public static String[] statusMsg = {
		"Batch processing not started", "Batch running",
		"Batch processing finished", "Batch processing paused",
		"Batch processing aborted", "Batch processing not configured" };

	public boolean isRunning();
	public boolean isPaused();
	public boolean isCancelled();
	public boolean isStatus(int status);
	public int getStatus();
	public void setStatus(int status);
	public String toString();
	public boolean isModified();
	public void setModified(boolean value);
	
	Exception getError();
	void setError(Exception x);
}
