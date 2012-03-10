package net.idea.rest.protocol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.idea.rest.protocol.attachments.DBAttachment;
import net.toxbank.client.resource.Protocol;




public class DBProtocol extends Protocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6632168193661223228L;
	protected int ID;
	protected int year;


	protected List<DBAttachment> attachments;
	public static String prefix = "QMRF";
	
	public List<DBAttachment> getAttachments() {
		if (attachments==null) attachments = new ArrayList<DBAttachment>();
		return attachments;
	}

	public DBProtocol() {
		
	}
	
	
	public DBProtocol(int id, int version, int year) {
		setID(id);
		setVersion(version);
		setYear(year);
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public String toString() {
		return String.format("<a href='%s'>%s</a>",getResourceURL(),getTitle()==null?getResourceURL():getTitle());
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public void setSubmissionDate(Long submissionDate) {
		super.setSubmissionDate(submissionDate);
		Date date = new Date(submissionDate);
		SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
		setYear(Integer.parseInt(simpleDateformat.format(date)));
	}
	
}
