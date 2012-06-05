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



/**
 * A class to provide application status.
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Aug 29, 2006
 */
public class JobStatus extends DefaultStatus {
	protected int persentage = 0;
	protected String message = "";
	protected Object job = null;
	protected boolean indeterminated = true;
	public JobStatus() {
		super();
	}
	/**
	 * 
	 * @return percent of completed task
	 */
	public int getPersentage() {
		return persentage;
	}
	/**
	 * @param persentage completed task
	 */
	public void setPersentage(int persentage) {
		this.persentage = persentage;
		setChanged();
		notifyObservers();		
	}
	/**
	 * 
	 * @return String representation of the status
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * 
	 * @param message sets String representation of the status
	 */
	public void setMessage(String message) {
		this.message = message;
		setChanged();
		notifyObservers(message);		
	}
	/**
	 * 
	 * @return current job. Generally this is {@link ambit.io.batch.IBatch}
	 */
	public Object getJob() {
		return job;
	}
	/**
	 * @param job  Generally this is {@link ambit.io.batch.IBatch}
	 */
	public void setJob(Object job) {
		this.job = job;
		setChanged();
		notifyObservers(job);				
	}
	/**
	 * 
	 * @return true if the length is indeterminated
	 */
	public boolean isIndeterminated() {
		return indeterminated;
	}
	/**
	 * 
	 * @param indeterminated  true if we can't determine the length of the task
	 */
	public void setIndeterminated(boolean indeterminated) {
		this.indeterminated = indeterminated;
	}

}
