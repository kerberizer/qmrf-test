package net.idea.rest.protocol.attachments;

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
		},
		data_validation {
			@Override
			public String toString() {
				return "Validation dataset";
			}
		},
		document {
			@Override
			public String toString() {
				return "Document";
			}
		};
		
	}
	protected attachment_type type;
	protected String description;
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
		return getResourceURL().toExternalForm();
	}
}
