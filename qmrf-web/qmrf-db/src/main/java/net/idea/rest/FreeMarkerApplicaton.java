package net.idea.rest;

import net.idea.ambit.qmrf.converters.QMRF_xml2rtf;
import net.idea.restnet.c.TaskApplication;

import org.restlet.ext.freemarker.ContextTemplateLoader;

import com.joliciel.freemarker.rtf.RtfConverter;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;

public class FreeMarkerApplicaton<USERID> extends TaskApplication<USERID> {
	   private Configuration configuration;
	   

	    
		public Configuration getConfiguration() {
			return configuration;
		}

		public void setConfiguration(Configuration configuration) {
			this.configuration = configuration;
		}
		protected void initFreeMarkerConfiguration() {
			configuration = new Configuration();
			
	        ContextTemplateLoader templatesLoader = new ContextTemplateLoader(getContext(),"war:///WEB-INF/templates/");
	        //configuration.setTemplateLoader(loader);
	        ClassTemplateLoader ctl = new ClassTemplateLoader(RtfConverter.class, ""); // don't forget this to access rtf.ftl
	        ClassTemplateLoader qmrf = new ClassTemplateLoader(QMRF_xml2rtf.class, ""); // don't forget this to access rtf.ftl
	        TemplateLoader[] loaders = new TemplateLoader[] { templatesLoader, ctl ,qmrf};
	        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
	        configuration.setTemplateLoader(mtl);
	        configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER); 
		}
}
