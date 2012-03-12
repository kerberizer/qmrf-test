package net.idea.qmrf.rest;

import java.io.StringWriter;

import net.idea.modbcum.i.config.Preferences;
import net.idea.qmrf.aa.QMRFLoginFormResource;
import net.idea.qmrf.client.Resources;
import net.idea.qmrf.task.QMRFAdminResource;
import net.idea.qmrf.task.QMRFAdminRouter;
import net.idea.qmrf.task.QMRFTaskResource;
import net.idea.qmrf.task.QMRFTaskRouter;
import net.idea.rest.groups.OrganisationRouter;
import net.idea.rest.groups.ProjectRouter;
import net.idea.rest.protocol.ProtocolRouter;
import net.idea.rest.protocol.facet.ProtocolsByEndpointResource;
import net.idea.rest.structure.resource.StructureResource;
import net.idea.rest.user.UserRouter;
import net.idea.rest.user.resource.MyAccountResource;
import net.idea.restnet.aa.cookie.CookieAuthenticator;
import net.idea.restnet.aa.local.UserLoginPOSTResource;
import net.idea.restnet.aa.local.UserLogoutPOSTResource;
import net.idea.restnet.c.ChemicalMediaType;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.routers.MyRouter;
import net.idea.restnet.db.aalocal.DBRole;
import net.idea.restnet.db.aalocal.DBVerifier;
import net.idea.restnet.db.aalocal.DbEnroller;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.Authorizer;
import org.restlet.security.RoleAuthorizer;
import org.restlet.security.SecretVerifier;
import org.restlet.security.User;
import org.restlet.service.TunnelService;

/**
 * (Q)SAR Model Reporting Format web services / web application
 * 
 * @author nina
 * 
 */
public class QMRFApplication extends TaskApplication<String> {

	public QMRFApplication() {
		super();

		setName("(Q)SAR Model Reporting Format Inventory");
		setDescription("(Q)SAR Model Reporting Format Inventory");
		setOwner("Institute for Health and Consumer Protection, JRC");
		setAuthor("Developed by Ideaconsult Ltd. (2007-2012) on behalf of JRC");
		setConfigFile("config/qmrf.properties");

		setStatusService(new QMRFStatusService());
		setTunnelService(new TunnelService(true, true) {
			@Override
			public Filter createInboundFilter(Context context) {
				return new QMRFTunnelFilter(context);
			}
		});
		getTunnelService().setUserAgentTunnel(true);
		getTunnelService().setExtensionsTunnel(false);
		getTunnelService().setMethodTunnel(true);

		Preferences.setProperty(Preferences.MAXRECORDS, "0");

		getMetadataService().setEnabled(true);
		getMetadataService().addExtension("sdf",
				ChemicalMediaType.CHEMICAL_MDLSDF, true);
		getMetadataService().addExtension("mol",
				ChemicalMediaType.CHEMICAL_MDLMOL, true);
		getMetadataService().addExtension("inchi",
				ChemicalMediaType.CHEMICAL_INCHI, true);
		getMetadataService().addExtension("cml",
				ChemicalMediaType.CHEMICAL_CML, true);
		getMetadataService().addExtension("smiles",
				ChemicalMediaType.CHEMICAL_SMILES, true);

		if (isInsecure())
			insecureConfig();

	}

	@Override
	public Restlet createInboundRoot() {

		Router router = new MyRouter(this.getContext());

		/** /task */
		router.attach(QMRFTaskResource.resource, new QMRFTaskRouter(
				getContext()));
		/** QMRF documents **/
		ProtocolRouter protocols = new ProtocolRouter(getContext());
		OrganisationRouter org_router = new OrganisationRouter(getContext());
		ProjectRouter projectRouter = new ProjectRouter(getContext());
		Restlet protocolRouter;

		protocolRouter = protocols; // createProtectedResource(protocols,"protocol",new
									// ProtocolAuthorizer());
		router.attach(Resources.protocol, protocolRouter);
		router.attach(Resources.project, projectRouter);
		router.attach(Resources.organisation, org_router);
		router.attach(Resources.user, new UserRouter(getContext(), protocols,
				org_router, projectRouter));

		
		router.attach("/", protocolRouter);
		router.attach("", protocolRouter);

		router.attach(Resources.endpoint, ProtocolsByEndpointResource.class);
		router.attach(Resources.structure, StructureResource.class);

		/**
		 * Images, styles, favicons, applets
		 */
		attachStaticResources(router);

		Filter auth = createCookieAuthenticator(true);
		auth.setNext(QMRFLoginFormResource.class);

		router.attach(String.format("/%s", QMRFLoginFormResource.resource),auth);
		auth = createCookieAuthenticator(true);
		auth.setNext(MyAccountResource.class);
		router.attach("/myaccount",auth);
		
		Router protectedRouter = new MyRouter(getContext());
		protectedRouter.attach("/roles", QMRFLoginFormResource.class);
		protectedRouter.attach(String.format("/%s", UserLoginPOSTResource.resource),UserLoginPOSTResource.class);
		protectedRouter.attach(String.format("/%s", UserLogoutPOSTResource.resource),UserLogoutPOSTResource.class);

		auth = createCookieAuthenticator(false);
		auth.setNext(protectedRouter);
		router.attach("/protected", auth);
		router.attach(String.format("/%s", QMRFAdminResource.resource),
				createAdminRouter());
	
		router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
		router.setRoutingMode(Router.MODE_BEST_MATCH);

		StringWriter w = new StringWriter();
		QMRFApplication.printRoutes(router, ">", w);
		System.out.println(w.toString());

		return router;
	}

	/*
	 * protected Restlet createLocalAAVerifiedResource(Class clazz) {
	 * 
	 * Filter userAuthn = new
	 * ChallengeAuthenticatorDBLocal(getContext(),true,"conf/qmrf-db.pref"
	 * ,"tomcat_users"); userAuthn.setNext(clazz); return userAuthn; }
	 */
	protected Filter createCookieAuthenticator(boolean optional) {
		CookieAuthenticator cookieAuth 	= new CookieAuthenticator(getContext(),
				"tomcat_users", "encryptSecretKey".getBytes());
		
		if (!optional) {
			//cookieAuth.setCookieName("subjectId");
			cookieAuth.setLoginFormPath("/login");
			cookieAuth.setLoginPath("/signin");
			cookieAuth.setLogoutPath("/signout");			

			cookieAuth.setVerifier(new DBVerifier(getContext(),
					"config/qmrf.properties", "tomcat_users"));
			cookieAuth.setEnroler(new DbEnroller(getContext(),
					"config/qmrf.properties", "tomcat_users"));
			return cookieAuth;
		} else {
			cookieAuth.setVerifier(new SecretVerifier() {
		
			
			@Override
		    public int verify(Request request, Response response) {
		        int result = RESULT_VALID;
		         
		        if (request.getChallengeResponse() != null) {
		            String identifier = getIdentifier(request, response);
		            char[] secret = getSecret(request, response);
		            if (verify(identifier, secret)) {
		                request.getClientInfo().setUser(new User(identifier));
		            }
		        }
		        return result;
		    }	
			@Override
			public boolean verify(String identifier, char[] secret) {
				return true;
			}
			
		});
		}
		return cookieAuth;
	}

		/*
		MethodAuthorizer methodAuthorizer = new MethodAuthorizer();
			methodAuthorizer.getAnonymousMethods().add(Method.GET);
			methodAuthorizer.getAnonymousMethods().add(Method.POST);
			methodAuthorizer.getAnonymousMethods().add(Method.PUT);
			methodAuthorizer.getAnonymousMethods().add(Method.DELETE);
		 */
	protected Restlet createProtected(Restlet router, String prefix,
			Authorizer authz) {
		return router;
		/*
		 * Filter authN = new
		 * ChallengeAuthenticatorDBLocal(getContext(),true,"conf/qmrf-db.pref"
		 * ,"tomcat_users"); if (authz!=null) { //authz.setPrefix(prefix);
		 * authN.setNext(authz); authz.setNext(router); } else {
		 * authN.setNext(router); } return authN;
		 */
	}

	/**
	 * Resource /bookmark
	 * 
	 * @return
	 * 
	 *         protected Restlet createBookmarksRouter() { BookmarksRouter
	 *         bookmarkRouter = new BookmarksRouter(getContext());
	 * 
	 *         Filter bookmarkAuth = new
	 *         OpenSSOAuthenticator(getContext(),false,"opentox.org"); Filter
	 *         bookmarkAuthz = new BookmarksAuthorizer();
	 *         bookmarkAuth.setNext(bookmarkAuthz);
	 *         bookmarkAuthz.setNext(bookmarkRouter); return bookmarkAuth; }
	 */
	/**
	 * Resource /admin
	 * 
	 * @return
	 */
	protected Restlet createAdminRouter() {
		return new QMRFAdminRouter(getContext());
	}

	/**
	 * Images, styles, icons Works if packaged as war only!
	 * 
	 * @return
	 */
	protected void attachStaticResources(Router router) {
		/*
		 * router.attach("/images",new Directory(getContext(),
		 * LocalReference.createFileReference("/webapps/images")));
		 */

		Directory metaDir = new Directory(getContext(), "war:///META-INF");
		Directory imgDir = new Directory(getContext(), "war:///images");
		Directory jmolDir = new Directory(getContext(), "war:///jmol");
		Directory jmeDir = new Directory(getContext(), "war:///jme");
		Directory styleDir = new Directory(getContext(), "war:///style");
		Directory jquery = new Directory(getContext(), "war:///jquery");

		router.attach("/meta/", metaDir);
		router.attach("/images/", imgDir);
		router.attach("/jmol/", jmolDir);
		router.attach("/jme/", jmeDir);
		router.attach("/jquery/", jquery);
		router.attach("/style/", styleDir);
		router.attach("/favicon.ico", FavIconResource.class);
		router.attach("/favicon.png", FavIconResource.class);
	}

	/**
	 * Standalone, for testing mainly
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Create a component
		Component component = new QMRFRESTComponent();
		final Server server = component.getServers().add(Protocol.HTTP, 8080);
		component.start();

		System.out.println("Server started on port " + server.getPort());
		System.out.println("Press key to stop server");
		System.in.read();
		System.out.println("Stopping server");
		component.stop();
		System.out.println("Server stopped");
	}

}

/**
 * GET allowed always; POST/PUT/DELETE depends on roles
 * 
 * @author nina
 * 
 */
class SimpleRoleAndMethodAuthorizer extends RoleAuthorizer {
	public SimpleRoleAndMethodAuthorizer(DBRole... roles) {
		super();
		for (DBRole role : roles)
			getAuthorizedRoles().add(role);
	}

	@Override
	public boolean authorize(Request request, Response response) {
		if (Method.GET.equals(request.getMethod()))
			return true;
		return super.authorize(request, response);
	}

}
