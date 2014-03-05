package net.idea.rest.user.resource;

import java.util.Map;

import net.idea.qmrf.client.QMRFRoles;
import net.idea.qmrf.client.Resources;
import net.idea.rest.QMRFFreeMarkerApplicaton;
import net.idea.rest.QMRFHTMLReporter;
import net.idea.rest.protocol.QMRF_HTMLBeauty;
import net.idea.restnet.c.TaskApplication;
import net.idea.restnet.c.html.HTMLBeauty;
import net.idea.restnet.user.resource.RegistrationNotifyResource;

public class QMRFRegistrationNotifyResource  extends RegistrationNotifyResource {
	
	public QMRFRegistrationNotifyResource() {
		super();
	}

	@Override
	public String getTemplateName() {
		return "register_notify.ftl";
	}

	
	@Override
	public void configureTemplateMap(Map<String, Object> map) {
		map.put("managerRole", "false");
		map.put("editorRole", "false");
		if (getClientInfo()!=null) {
			if (getClientInfo().getUser()!=null)
				map.put("username", getClientInfo().getUser().getIdentifier());
			if (getClientInfo().getRoles()!=null) {
				if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.managerRole)>=0)
					map.put("managerRole", "true");
				if (getClientInfo().getRoles().indexOf(QMRFHTMLReporter.editorRole)>=0)
					map.put("editorRole", "true");
			}
		}
        map.put("creator","IdeaConsult Ltd.");
        map.put(Resources.Config.qmrf_email.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_email.name()));
        map.put(Resources.Config.qmrf_editor.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_editor.name()));
        map.put(Resources.Config.qmrf_template.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_template.name()));
        map.put(Resources.Config.qmrf_manual.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_manual.name()));
        map.put(Resources.Config.qmrf_faq.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_faq.name()));
        map.put(Resources.Config.qmrf_oecd.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_oecd.name()));
        map.put(Resources.Config.qmrf_jrc.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_jrc.name()));
        map.put(Resources.Config.qmrf_disclaimer.name(),((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_disclaimer.name()));

        
		map.put("searchURI",Resources.register);
		if (getClientInfo()!=null) {
			if (getClientInfo().getUser()!=null)
				map.put("username", getClientInfo().getUser().getIdentifier());
		}
        map.put("qmrf_root",getRequest().getRootRef().toString()) ;
	    map.put("queryService",((TaskApplication)getApplication()).getProperty(Resources.Config.qmrf_ambit_service.name()));

	}
	
	public String getConfigFile() {
		return ((QMRFFreeMarkerApplicaton)getApplication()).getConfigFile();
	}
	
	@Override
	protected HTMLBeauty getHTMLBeauty() {
		return new QMRF_HTMLBeauty(Resources.notify);
	}
	
	
}
