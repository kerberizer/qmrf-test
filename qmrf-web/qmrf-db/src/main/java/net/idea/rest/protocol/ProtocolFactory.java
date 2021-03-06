package net.idea.rest.protocol;

import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import net.idea.ambit.qmrf.QMRFObject;
import net.idea.ambit.qmrf.chapters.QMRFSubChapterText;
import net.idea.qmrf.client.PublishedStatus;
import net.idea.qmrf.converters.QMRFConverter;
import net.idea.rest.endpoints.EndpointTest;
import net.idea.rest.protocol.CallableProtocolUpload.UpdateMode;
import net.idea.rest.protocol.attachments.DBAttachment;
import net.idea.rest.protocol.attachments.DBAttachment.attachment_type;
import net.idea.rest.protocol.db.ReadProtocol;
import net.idea.restnet.groups.DBOrganisation;
import net.idea.restnet.groups.DBProject;
import net.idea.restnet.user.DBUser;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.resource.Organisation;
import net.toxbank.client.resource.Project;
import net.toxbank.client.resource.Protocol;
import net.toxbank.client.resource.Protocol.STATUS;
import net.toxbank.client.resource.User;

import org.apache.commons.fileupload.FileItem;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.xml.sax.EntityResolver;

public class ProtocolFactory {
	protected static final String msg_invalid_qmrf = "Invalid QMRF document!";
	protected static final String utf8= "UTF-8";
	public static DBProtocol getProtocol(DBProtocol protocol,
				List<FileItem> items, 
				long maxSize,
				File dir, 
				AccessRights accessRights,
				UpdateMode updateMode,
				EntityResolver dtdresolver) throws ResourceException {
		
		if (protocol==null) protocol = new DBProtocol();
		for (final Iterator<FileItem> it = items.iterator(); it.hasNext();) {
			FileItem fi = it.next();

			try {
				ReadProtocol.fields field  = null;
				try { 
					String fname = fi.getFieldName();
					if (fname!=null)
						field = ReadProtocol.fields.valueOf(fname);
					
				} catch (Exception x) {
					continue;
				}
				if (field==null) continue;
				switch (field) {
				case idprotocol: continue;
				case identifier: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s))
						protocol.setIdentifier(s);
					break;
				}
				case published_status: {
					String s = fi.getString(utf8);
					if (s==null)  {protocol.setPublished(false); break;}
					s = s.toLowerCase();
					try {
						if ("on".equals(s)) {protocol.setPublished(true); break;}
						else if ("".equals(s)) {protocol.setPublished(false); break;}
						else if ("true".equals(s)) {protocol.setPublished(true);  break;}
						else if ("false".equals(s)) {protocol.setPublished(false); break;}
						else protocol.setPublishedStatus(PublishedStatus.valueOf(s));
					} catch (Exception x) { 
						protocol.setPublished(false);
					}
					break;
				}
				/*
				case anabstract: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s))
					protocol.setAbstract(s);
					break;
				}
				*/
				case filename: {
					if (fi.isFormField()) {
						protocol.setAbstract(fi.getString(utf8));
						//protocol.setDocument(new Document(new URL(fi.getString(utf8))));
					} else {	
						if (fi.getSize()==0)  {
							switch (updateMode) {
							case create:
								throw new ResourceException(new Status(Status.CLIENT_ERROR_BAD_REQUEST,"Empty file!"));								
							case createversion:
								throw new ResourceException(new Status(Status.CLIENT_ERROR_BAD_REQUEST,"Empty file!"));								
							default: {
								continue; //ignore, not mandatory
							}
							}	
						}
						File file = null;
				        if (fi.getName()==null)
				           	throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"File name can't be empty!");
				        else {
				        	try { 
				        		if ((dir!=null) && !dir.exists())  dir.mkdir();
				        	} catch (Exception x) {dir = null; }
				          	file = new File(
				            		String.format("%s/%s",
				            				dir==null?System.getProperty("java.io.tmpdir"):dir,
				            				fi.getName()));
				        }
				        protocol.setAbstract(fi.getString(utf8));
				    	QMRFObject qmrf = new QMRFObject();
				    	try {
				    		qmrf.setDtdresolver(dtdresolver);
				    		qmrf.read(new StringReader(protocol.getAbstract()));
				    		qmrf.setSaveSelectedOnly(true);
				    		qmrf.transform_and_read(new StringReader(protocol.getAbstract()),false);
				    		StringWriter writer = new StringWriter();
				    		qmrf.write(writer);
				    		protocol.setAbstract(writer.toString());
				    	} catch (Exception x) {
				    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
				    				String.format(msg_invalid_qmrf,x));
				    	}
						if (qmrf.getChapters().size()<10) 
							throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,msg_invalid_qmrf);
						
						try {
							protocol.setTitle(QMRFConverter.replaceTags(((QMRFSubChapterText)qmrf.getChapters().get(0).getSubchapters().getItem(0)).getText()));
							//protocol.setIdentifier(QMRFConverter.replaceTags(((QMRFSubChapterText)qmrf.getChapters().get(9).getSubchapters().getItem(0)).getText()));
							String keywords = QMRFConverter.replaceTags(((QMRFSubChapterText)qmrf.getChapters().get(9).getSubchapters().getItem(2)).getText());
							String[] keyword = keywords.split(",|;");
							protocol.getKeywords().clear();
							for (String key:keyword) {
								String trimmedKey = key.trim();
								if (protocol.getKeywords().indexOf(trimmedKey)<0)
									protocol.addKeyword(trimmedKey);
							}
						} catch (Exception x) {
							throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,msg_invalid_qmrf,x);
						}

					}
					
					protocol.setDocument(null);
			        break;
				}
				case data_training :{
					DBAttachment attachment = createAttachment(fi,protocol,attachment_type.data_training,dir);
					if (attachment!=null) protocol.getAttachments().add(attachment);
					break;
				}
				case data_validation :{
					DBAttachment attachment = createAttachment(fi,protocol,attachment_type.data_validation,dir);
					if (attachment!=null) protocol.getAttachments().add(attachment);
					break;
				}
				case document :{
					DBAttachment attachment = createAttachment(fi,protocol,attachment_type.document,dir);
					if (attachment!=null) protocol.getAttachments().add(attachment);
					break;
				}
					
				case project_uri: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s)) {
						Project p = protocol.getProject();
						if (p==null) { p = new DBProject(); protocol.setProject(p);}
						if (s.startsWith("http"))
							p.setResourceURL(new URL(s));
						else p.setTitle(s);
					}
					break;					
				}
				case user_uri: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s)) {
						User p = protocol.getOwner();
						if (p==null) { p = new DBUser(); protocol.setOwner(p);}
						if (s.startsWith("http"))
							p.setResourceURL(new URL(s));
						else p.setUserName(s);
					}
					break;					
				}				
				case organisation_uri: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s)) {
						Organisation p = protocol.getOrganisation();
						if (p==null) { p = new DBOrganisation(); protocol.setOrganisation(p);}
						if (s.startsWith("http"))
							p.setResourceURL(new URL(s));
						else p.setTitle(fi.getString());
					}
					break;					
				}		
				case author_uri: {
					String s = fi.getString(utf8);
					if ((s!=null) && s.startsWith("http"))
						 protocol.addAuthor(new DBUser(new URL(s)));
					break;	
				}
				case title: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s)) 
						protocol.setTitle(s);
					break;
				}
				case iduser: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s)) {
						DBUser user = new DBUser();
						if (s.startsWith("http"))
							user.setResourceURL(new URL(s));
						else user.setTitle(s);
						protocol.setOwner(user);
					}
					break;
				}
				case summarySearchable: {
					try {
						protocol.setSearchable(Boolean.parseBoolean(fi.getString(utf8)));
					} catch (Exception x) { protocol.setSearchable(false);}
					break;					
				}
				case status: {
					try {
						protocol.setStatus(Protocol.STATUS.valueOf(fi.getString(utf8)));
					} catch (Exception x) { protocol.setStatus(STATUS.RESEARCH);}
					break;					
				}
				case xmlkeywords: {
					try {
						if ((fi.getString()!=null) && !"".equals(fi.getString(utf8)))
							protocol.addKeyword(fi.getString().trim());
						} catch (Exception x) { }
						break;	
				}
				case endpoint: {
					try {
						if (protocol.getEndpoint()==null) protocol.setEndpoint(new EndpointTest(null,null));
						protocol.getEndpoint().setCode(fi.getString(utf8));
					} catch (Exception x) { }
					break;					
				}	
				case endpointName: {
					try {
						if (protocol.getEndpoint()==null) protocol.setEndpoint(new EndpointTest(null,null));
						protocol.getEndpoint().setName(fi.getString(utf8));
					} catch (Exception x) { }
					break;					
				}		
				case endpointParentCode: {
					try {
						if (protocol.getEndpoint()==null) protocol.setEndpoint(new EndpointTest(null,null));
						protocol.getEndpoint().setParentCode(fi.getString(utf8));
					} catch (Exception x) { }
					break;					
				}					
				case endpointParentName: {
					try {
						if (protocol.getEndpoint()==null) protocol.setEndpoint(new EndpointTest(null,null));
						protocol.getEndpoint().setParentTemplate(fi.getString(utf8));
					} catch (Exception x) { }
					break;					
				}				
				case allowReadByUser: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s))
						try {
							DBUser user = null;
							//a bit of heuristic
							if (s.startsWith("http")) { user = new DBUser(new URL(s.trim())); } 
							else { user = new DBUser(); user.setUserName(s.trim()); }	
							accessRights.addUserRule(user,true,null,null,null);
						} catch (Exception x) { 
							x.printStackTrace(); 
						}
					break;						
				}
				case allowReadByGroup: {
					String s = fi.getString(utf8);
					if ((s!=null) && !"".equals(s))
						try {
							String uri = s.trim();
							//hack to avoid queries...
							if (uri.indexOf("/organisation")>0) {
								DBOrganisation org = null;
								if (s.startsWith("http")) { org = new DBOrganisation(new URL(s.trim())); } 
								else { org = new DBOrganisation(); org.setGroupName(s.trim()); }	
								accessRights.addGroupRule(org,true,null,null,null);
								
							} else if (uri.indexOf("/project")>0) {
								DBProject org = null;
								if (s.startsWith("http")) { org = new DBProject(new URL(s.trim())); } 
								else { org = new DBProject(); org.setGroupName(s.trim()); }
								accessRights.addGroupRule(org,true,null,null,null);
							}
						} catch (Exception x) { x.printStackTrace(); }
					break;	
				}				
				} //switch
			} catch (ResourceException x) {
				throw x;
			} catch (Exception x) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
			} 

		}
		return protocol;
	}
	
	protected static DBAttachment createAttachment(FileItem fi, DBProtocol protocol, attachment_type type, File dir) throws Exception {
			
			if (fi.isFormField()) {
//				protocol.setDataTemplate(new Template(new URL(fi.getString(utf8))));
			} else {	
				String originalName = "";
				String description = "";
				if (fi.getSize()==0) return null;
				File file = null;
		        if (fi.getName()==null)
		           	throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"File name can't be empty!");
		        else {
		        	try { 
		        		if ((dir!=null) && !dir.exists())  dir.mkdir();
		        	} catch (Exception x) {dir = null; }
		        	try { 
		        		dir = new File(dir==null?new File(System.getProperty("java.io.tmpdir")):dir,type.name());
		        		if ((dir!=null) && !dir.exists())  dir.mkdir();
		        	} catch (Exception x) {dir = null; }
		        	//stupid File class ... 
		        	int lastIndex = fi.getName().lastIndexOf("\\");
		        	if (lastIndex < 0) lastIndex = fi.getName().lastIndexOf("/");
		        	description = stripFileName(fi.getName());
		        	int extIndex = fi.getName().lastIndexOf(".");
		        	String ext = extIndex>0?fi.getName().substring(extIndex):"";
		        	
		        	//generate new file name
		        	originalName = fi.getName();
		        	String newName = String.format("qmrf%d_%s_%s%s", protocol.getID()>0?protocol.getID():0,
		        								type.name(),DBProtocol.generateIdentifier(),ext);
		          	file = new File(String.format("%s/%s",dir==null?System.getProperty("java.io.tmpdir"):dir,newName));
		        }
		        fi.write(file);
		       
		        return DBAttachment.file2attachment(file, description,originalName, type);
		        
			}
			return null;
		}	

	public static String stripFileName(String fileName) {
    	int lastIndex = fileName.lastIndexOf("\\");
    	if (lastIndex < 0) lastIndex = fileName.lastIndexOf("/");
    	return lastIndex>0?fileName.substring(lastIndex+1):fileName;
    	
	}
}
