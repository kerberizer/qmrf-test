package net.idea.rest.protocol.attachments;

import java.io.File;
import java.net.URL;

import net.toxbank.client.resource.Document;

public class DBAttachment extends Document {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1141527775736093041L;
	
	public enum attachment_type {
		data_training {
			@Override
			public String toString() {
				return "Training dataset";
			}
			@Override
			public String getDescription() {
				return "SDF, MOL, CSV, XLS formats";
			}
		},
		data_validation {
			@Override
			public String toString() {
				return "Validation dataset";
			}
			@Override
			public String getDescription() {
				return "SDF, MOL, CSV, XLS formats";
			}			
		},
		document {
			@Override
			public String toString() {
				return "Document";
			}
			@Override
			public String getDescription() {
				return "PDF";
			}	
			public String acceptFormats() { return "pdf|doc|xls"; };
		};
		public String getDescription() { return toString();}
		public int maxFiles() { return 3;}
		public String acceptFormats() { return "sdf|mol|csv|xls"; };
		
	}
	
	protected int idquerydatabase= -1;
	protected String protocol = null;
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int getIdquerydatabase() {
		return idquerydatabase;
	}
	public void setIdquerydatabase(int idquerydatabase) {
		this.idquerydatabase = idquerydatabase;
	}
	protected attachment_type type;
	protected String description;
	protected String format;
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public boolean isImported() {
		return imported;
	}
	public void setImported(boolean imported) {
		this.imported = imported;
	}
	protected String originalFileName;
	protected boolean imported;
	protected int ID;
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public DBAttachment(int id) {
		this.ID = id;
	}
	public DBAttachment() {
		this(null);
	}
	public DBAttachment(URL resourceURL) {
		super(resourceURL);
	}
	public DBAttachment(URL resourceURL, String mediaType) {
		super(resourceURL);
		setMediaType(mediaType);
	}
	
	public attachment_type getType() {
		return type;
	}
	public void setType(attachment_type type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return String.format("%s %s",getType().getDescription(),getTitle()==null?"":getTitle());
	}
	
	public static DBAttachment file2attachment(File file, String description, attachment_type type) {
		DBAttachment attachment = new DBAttachment();
		int extindex = file.getName().lastIndexOf(".");
		if (extindex>0) {
			attachment.setFormat(file.getName().substring(extindex+1));
			attachment.setTitle(file.getName().substring(0,extindex));
		} else {
			attachment.setTitle(file.getName());
			attachment.setFormat("");
		}
		attachment.setDescription(description);
		attachment.setType(type);
		attachment.setOriginalFileName(file.getAbsolutePath());
		attachment.setImported(false);
		return attachment;
	}
	

}
