package net.idea.rest.protocol;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import net.idea.modbcum.i.query.IQueryUpdate;
import net.idea.modbcum.p.ProcessorException;
import net.idea.modbcum.p.QueryExecutor;
import net.idea.modbcum.p.UpdateExecutor;
import net.idea.rest.groups.DBOrganisation;
import net.idea.rest.groups.DBProject;
import net.idea.rest.groups.IDBGroup;
import net.idea.rest.groups.db.CreateGroup;
import net.idea.rest.groups.db.ReadGroup;
import net.idea.rest.groups.db.ReadOrganisation;
import net.idea.rest.groups.db.ReadProject;
import net.idea.rest.policy.SimpleAccessRights;
import net.idea.rest.protocol.db.CreateProtocol;
import net.idea.rest.protocol.db.CreateProtocolVersion;
import net.idea.rest.protocol.db.DeleteProtocol;
import net.idea.rest.protocol.db.UpdateKeywords;
import net.idea.rest.protocol.db.UpdateProtocol;
import net.idea.rest.protocol.db.template.UpdateDataTemplate;
import net.idea.rest.protocol.resource.db.ProtocolQueryURIReporter;
import net.idea.rest.user.DBUser;
import net.idea.rest.user.author.db.AddAuthors;
import net.idea.rest.user.author.db.DeleteAuthor;
import net.idea.rest.user.db.CreateUser;
import net.idea.rest.user.db.ReadUser;
import net.idea.restnet.aa.opensso.OpenSSOServicesConfig;
import net.idea.restnet.c.task.CallableProtectedTask;
import net.idea.restnet.i.task.TaskResult;
import net.toxbank.client.Resources;
import net.toxbank.client.policy.AccessRights;
import net.toxbank.client.policy.GroupPolicyRule;
import net.toxbank.client.policy.PolicyRule;
import net.toxbank.client.policy.UserPolicyRule;
import net.toxbank.client.resource.Group;
import net.toxbank.client.resource.User;

import org.apache.commons.fileupload.FileItem;
import org.opentox.aa.opensso.OpenSSOToken;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;


public class CallableProtocolUpload extends CallableProtectedTask<String> {
	public enum UpdateMode {create,update,dataTemplateOnly,createversion}
	protected List<FileItem> input;
	protected ProtocolQueryURIReporter reporter;
	protected Connection connection;
	protected UpdateExecutor exec;
	protected String baseReference;
	protected DBUser user;
	protected File dir;
	protected DBProtocol protocol;
	protected Method method;
	protected UpdateMode updateMode = UpdateMode.create;
	
	
	public UpdateMode getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(UpdateMode updateMode) {
		this.updateMode = updateMode;
	}

	public boolean isSetDataTemplateOnly() {
		return UpdateMode.dataTemplateOnly.equals(updateMode);
	}

	public void setSetDataTemplateOnly(boolean setDataTemplateOnly) {
		this.updateMode = UpdateMode.dataTemplateOnly;
	}

	/**
	 * 
	 * @param protocol  NULL if a new protocol, otherwise the protocol which version to be created
	 * @param user
	 * @param input
	 * @param connection
	 * @param r
	 * @param token
	 * @param baseReference
	 * @param dir
	 */
	public CallableProtocolUpload(Method method,DBProtocol protocol,DBUser user,List<FileItem> input,
					Connection connection,
					ProtocolQueryURIReporter r,
					String token,
					String baseReference,
					File dir) {
		super(token);
		this.method = method;
		this.protocol = protocol;
		this.connection = connection;
		this.input = input;
		this.reporter = r;
		this.baseReference = baseReference;
		this.user = user;
		this.dir = dir;
	}
	@Override
	public TaskResult doCall() throws Exception {
		if (Method.POST.equals(method)) return create();
		else if (Method.PUT.equals(method)) return update();
		else if (Method.DELETE.equals(method)) return delete();
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED,method.toString());
	}	
	
	public TaskResult delete() throws ResourceException {
		try {
			connection.setAutoCommit(false);
			//protocol.setOwner(user);
			exec = new UpdateExecutor<IQueryUpdate>();
			exec.setConnection(connection);
			if (isSetDataTemplateOnly()) {
				//DeleteProtocol k = new DeleteProtocol(protocol);
				//exec.process(k);				
			} else {
				DeleteProtocol k = new DeleteProtocol(protocol);
				exec.process(k);
				connection.commit();
				
			}
			try {
				deletePolicy(new URL(reporter.getURI(protocol)));
			} catch (Exception x) {
				x.printStackTrace();
				//ok, what do we do here?
			}
			return new TaskResult(null,false);
		} catch (ResourceException x) {
			try {connection.rollback();} catch (Exception xx) {}
			throw x;
		} catch (Exception x) {
			try {connection.rollback();} catch (Exception xx) {}
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
		} finally {
			try {exec.close();} catch (Exception x) {}
			try {connection.setAutoCommit(true);} catch (Exception x) {}
			try {connection.close();} catch (Exception x) {}
		}
	}

	public TaskResult create() throws ResourceException {
		boolean existing = protocol!=null&&protocol.getID()>0;
		AccessRights policy = new AccessRights(null);
		try {
			protocol = ProtocolFactory.getProtocol(protocol,input, 10000000,dir,policy);
		} catch (ResourceException x) {
			throw x;
		} catch (Exception x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
		}
		//now write
		switch (updateMode) {
		case dataTemplateOnly:  {
			try {
				if ((protocol.getDataTemplate()!=null) && protocol.getDataTemplate().getResourceURL().toString().startsWith("file:")) {
					connection.setAutoCommit(false);
					//protocol.setOwner(user);
					exec = new UpdateExecutor<IQueryUpdate>();
					exec.setConnection(connection);					
					UpdateDataTemplate k = new UpdateDataTemplate(protocol);
					exec.process(k);
					connection.commit();
					String uri = String.format("%s%s",reporter.getURI(protocol),Resources.datatemplate);
					return new TaskResult(uri,false);
				} else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Data template");
			} catch (ProcessorException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
			} catch (ResourceException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw x;
			} catch (Exception x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
			} finally {
				try {exec.close();} catch (Exception x) {}
				try {connection.setAutoCommit(true);} catch (Exception x) {}
				try {connection.close();} catch (Exception x) {}
			}
//			break;
		}
		default: {
			try {
				connection.setAutoCommit(false);
				//protocol.setOwner(user);
				exec = new UpdateExecutor<IQueryUpdate>();
				exec.setConnection(connection);
				
				CreateUser quser = new CreateUser(null);
				//user
				DBUser user = protocol.getOwner() instanceof DBUser?
							(DBUser)protocol.getOwner():
							new DBUser(protocol.getOwner());
			    protocol.setOwner(user);
			    if (user.getID()<=0) user.setID(user.parseURI(baseReference));
				if (user.getID()<=0) {
					quser.setObject(user);
					exec.process(quser);
				}	
				
				for (User u: protocol.getAuthors()) { 
					DBUser author =u instanceof DBUser?(DBUser)u:new DBUser(u);
	 			    if (author.getID()<=0) author.setID(author.parseURI(baseReference));
						if (author.getID()<=0) {
							quser.setObject(author);
							exec.process(quser);
						}	
				}
				//project
				DBProject p = protocol.getProject() instanceof DBProject?
							(DBProject)protocol.getProject():
							new DBProject(protocol.getProject());
			    protocol.setProject(p);
			    if (p.getID()<=0) p.setID(p.parseURI(baseReference));
				if (p.getID()<=0) {
					CreateGroup q1 = new CreateGroup(p);
					exec.process(q1);
				}
				//organisation
				DBOrganisation o = protocol.getOrganisation() instanceof DBOrganisation?
						(DBOrganisation)protocol.getOrganisation():
						new DBOrganisation(protocol.getOrganisation());
				protocol.setOrganisation(o);
			    if (o.getID()<=0) o.setID(o.parseURI(baseReference));
				if (o.getID()<=0) {
					CreateGroup q2 = new CreateGroup(o);
					exec.process(q2);
				}
				
				if (existing) {
					CreateProtocolVersion q = new CreateProtocolVersion(protocol);
					exec.process(q);
				} else {
					CreateProtocol q = new CreateProtocol(protocol);
					exec.process(q);
				}
				
				String uri = reporter.getURI(protocol);
				
				if (protocol.getKeywords().size()>0) {
					UpdateKeywords k = new UpdateKeywords(protocol);
					exec.process(k);
				}
				
				if ((protocol.getAuthors()!=null) && protocol.getAuthors().size()>0) {
					AddAuthors k = new AddAuthors(protocol);
					exec.process(k);
				}
				
				if ((protocol.getDataTemplate()!=null) && 
						(protocol.getDataTemplate().getResourceURL()!=null) &&
						 protocol.getDataTemplate().getResourceURL().toString().startsWith("file:")) {
					UpdateDataTemplate k = new UpdateDataTemplate(protocol);
					exec.process(k);
				}	
				
				connection.commit();
				TaskResult result = new TaskResult(uri,true);
				try {
					//adding the owner and the authors
					addDefaultProtocolRights(policy,protocol.getOwner(),true,true,true,true);
					//for (User u: protocol.getAuthors())	addDefaultProtocolRights(policy,u,true,true,true,true);
					if ((policy.getRules()!=null) && (policy.getRules().size()>0)) {
						retrieveAccountNames(policy,connection);
						policy.setResource(new URL(uri));
						result.setPolicy(generatePolicy(protocol,policy));
					} else result.setPolicy(null);
				} 
				catch (Exception x) { result.setPolicy(null);}
			
				return result;
			} catch (ProcessorException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
			} catch (ResourceException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw x;
			} catch (Exception x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
			} finally {
				try {exec.close();} catch (Exception x) {}
				try {connection.setAutoCommit(true);} catch (Exception x) {}
				try {connection.close();} catch (Exception x) {}
			}
		}
		} //switch

	}

	public TaskResult update() throws ResourceException {
		if ((protocol==null)||(protocol.getID()<=0)) 
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Can't update: Not an existing protocol!");
		AccessRights policy = new AccessRights(null);
		try {
			//get only fields from the web form
			DBProtocol newProtocol = ProtocolFactory.getProtocol(null,input, 10000000,dir,policy);
			newProtocol.setID(protocol.getID());
			newProtocol.setVersion(protocol.getVersion());
			newProtocol.setIdentifier(null);
			if (newProtocol.getProject() != null) {
				DBProject p = (DBProject) newProtocol.getProject();
				p.setID(p.parseURI(baseReference));
			}
			if (newProtocol.getOrganisation() != null) {
				DBOrganisation p = (DBOrganisation) newProtocol.getOrganisation();
				p.setID(p.parseURI(baseReference));
			}		
			if (newProtocol.getOwner() != null) {
				DBUser p = (DBUser) newProtocol.getOwner();
				p.setID(p.parseURI(baseReference));
			}					
			if (newProtocol.getAuthors()!=null)
				for (User u: newProtocol.getAuthors()) { 
					DBUser author =u instanceof DBUser?(DBUser)u:new DBUser(u);
	 			    if (author.getID()<=0) author.setID(author.parseURI(baseReference));
				}
			
			protocol = newProtocol;
		} catch (Exception x) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
		}
		
			try {
				connection.setAutoCommit(false);
				//protocol.setOwner(user);
				exec = new UpdateExecutor<IQueryUpdate>();
				exec.setConnection(connection);
				
				UpdateProtocol q = new UpdateProtocol(protocol);
				exec.process(q);
				
				String uri = reporter.getURI(protocol);
				
				if (protocol.getKeywords().size()>0) {
					UpdateKeywords k = new UpdateKeywords(protocol);
					exec.process(k);
				}
				
				if (protocol.getAuthors()!=null) {
					DeleteAuthor da = new DeleteAuthor(protocol, null);
					exec.process(da);
					if (protocol.getAuthors().size()>0) {
						AddAuthors k = new AddAuthors(protocol);
						exec.process(k);
					}
				}
				
				if ((protocol.getDataTemplate()!=null) && 
						(protocol.getDataTemplate().getResourceURL()!=null) &&
						 protocol.getDataTemplate().getResourceURL().toString().startsWith("file:")) {
					UpdateDataTemplate k = new UpdateDataTemplate(protocol);
					exec.process(k);
				}	
				
				connection.commit();
				TaskResult result = new TaskResult(uri,false);
				try {
					addDefaultProtocolRights(policy,protocol.getOwner(),true,true,true,true);
					if ((policy.getRules()!=null) && (policy.getRules().size()>0)) {
						retrieveAccountNames(policy,connection);
						policy.setResource(new URL(uri));
						result.setPolicy(generatePolicy(protocol,policy));
					} else result.setPolicy(null);
				} 
				catch (Exception x) { result.setPolicy(null);}
			
			
				return result;
			} catch (ProcessorException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,x);
			} catch (ResourceException x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw x;				
			} catch (Exception x) {
				try {connection.rollback();} catch (Exception xx) {}
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,x);
			} finally {
				try {exec.close();} catch (Exception x) {}
				try {connection.setAutoCommit(true);} catch (Exception x) {}
				try {connection.close();} catch (Exception x) {}
			}

	}
	
	protected void addDefaultProtocolRights(AccessRights accessRights, User owner, Boolean get, Boolean post, Boolean put, Boolean delete ) throws Exception {
		/*
		boolean added = false;
		for (PolicyRule rule: accessRights.getRules()) { 
			if (rule instanceof UserPolicyRule)  
				if (rule.hasSubject(owner)) added = true;
		}
		if (!added)
		*/
		//it may be added alreayd, but with different rights. We enforce the owner always having full rights
		accessRights.addUserRule(owner, get,post,put,delete);
	}

	protected void retrieveAccountNames(AccessRights accessRights,Connection connection) throws Exception {
		QueryExecutor qexec = new QueryExecutor();
		try {
			
			qexec.setConnection(connection);
			ReadUser getUser = new ReadUser();
			for (PolicyRule rule: accessRights.getRules()) 
				if (rule instanceof UserPolicyRule)  {
					DBUser u = ((UserPolicyRule<? extends DBUser>) rule).getSubject();
				    if (u.getID()<=0) u.setID(u.parseURI(baseReference));
					if (u.getUserName()==null) {
						getUser.setValue(u);
						ResultSet rs = null;
						try { 
							rs = qexec.process(getUser); 
							while (rs.next()) { u.setUserName(getUser.getObject(rs).getUserName()); }
						} catch (Exception x) { if (rs!=null) rs.close(); }
					}	
			}
			ReadGroup getGroup = null;
			ReadOrganisation readOrg = new ReadOrganisation(null);
			ReadProject readProject = new ReadProject(null);
			for (PolicyRule rule: accessRights.getRules()) 
				if (rule instanceof GroupPolicyRule)  {
					Group u = ((GroupPolicyRule<? extends Group>) rule).getSubject();
					
					if (u instanceof IDBGroup) {
						IDBGroup u_id = (IDBGroup) u;
						if (u_id.getID()<=0) u_id.setID(u_id.parseURI(baseReference));
					}
					if (u.getGroupName()==null) {
						getGroup = u instanceof DBOrganisation?readOrg:readProject;
						getGroup.setValue(u);
						ResultSet rs = null;
						try { 
							rs = qexec.process(getGroup); 
							while (rs.next()) { u.setGroupName(getGroup.getObject(rs).getGroupName()); }
						} catch (Exception x) { if (rs!=null) rs.close(); }
					}
			}
		}
		finally { try {qexec.close(); } catch (Exception x) {}}			
	}
	protected List<String> generatePolicy(DBProtocol protocol, AccessRights policy) throws Exception {
		OpenSSOServicesConfig config = OpenSSOServicesConfig.getInstance();
		SimpleAccessRights policyTools = new SimpleAccessRights(config.getPolicyService());
		List<String> policies = policyTools.createPolicyXML(policy);
		return policies.size()>0?policies:null;
	}
	
	protected void deletePolicy(URL url) throws Exception {
		if (getToken()==null) return;
		try {
			OpenSSOServicesConfig config = OpenSSOServicesConfig.getInstance();
			SimpleAccessRights policyTools = new SimpleAccessRights(config.getPolicyService());
			OpenSSOToken ssoToken = new OpenSSOToken(config.getOpenSSOService());
			ssoToken.setToken(getToken());
			policyTools.deleteAllPolicies(ssoToken,url);
		} catch (Exception x) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY,String.format("Error deleting policies for %s",url),x);
		}
	}
}
