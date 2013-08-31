package net.idea.rest;

import java.util.Properties;

import net.idea.ambit.qmrf.converters.QMRF_xml2rtf;
import net.idea.restnet.c.freemarker.FreeMarkerApplicaton;

import org.restlet.ext.freemarker.ContextTemplateLoader;

import com.joliciel.freemarker.rtf.RtfConverter;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;

public class QMRFFreeMarkerApplicaton<USERID> extends FreeMarkerApplicaton<USERID> {
	protected Properties dbConfig;

	protected static final String[] dbProps = new String[] {	
				"Host","Port","Database","User","Password",
				"database.user.root.password","database.test","database.test.port","database.user.test","database.user.test.password"
	};
	

	public QMRFFreeMarkerApplicaton() {
		super();
	}
	
	@Override
	public void setConfigFile(String configFile) {
		super.setConfigFile(configFile);
		//hack over private TaskApplication properties 
		dbConfig = new Properties();
		for (String dbprop : dbProps)
			dbConfig.put(dbprop,getProperty(dbprop));
	}
	@Override
	protected void initFreeMarkerConfiguration() {
			setConfiguration(new Configuration());
			
	        ContextTemplateLoader templatesLoader = new ContextTemplateLoader(getContext(),"war:///WEB-INF/templates/");
	        //configuration.setTemplateLoader(loader);
	        ClassTemplateLoader ctl = new ClassTemplateLoader(RtfConverter.class, ""); // don't forget this to access rtf.ftl
	        ClassTemplateLoader qmrf = new ClassTemplateLoader(QMRF_xml2rtf.class, ""); // don't forget this to access rtf.ftl
	        TemplateLoader[] loaders = new TemplateLoader[] { templatesLoader, ctl ,qmrf};
	        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
	        getConfiguration().setTemplateLoader(mtl);
	        getConfiguration().setObjectWrapper(ObjectWrapper.BEANS_WRAPPER); 
	}
	public Properties getDbConfig() {
		return dbConfig;
	}

}
