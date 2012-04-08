package net.idea.ambit.qmrf.converters;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.idea.ambit.qmrf.QMRFObject;

import com.joliciel.freemarker.rtf.RtfConverter;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.log.Logger;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;


public class QMRF_xml2rtf  {
	//TODO global config
	Configuration cfg;
	
	public synchronized void initConfig() throws Exception {
		Logger.selectLoggerLibrary(Logger.LIBRARY_JAVA);
		cfg = new Configuration();
		
		URL url = getClass().getClassLoader().getResource("net/idea/ambit/qmrf/templates");

        FileTemplateLoader ftl = new FileTemplateLoader(new File(url.getFile()));
        ClassTemplateLoader ctl = new ClassTemplateLoader(RtfConverter.class, ""); // don't forget this to access rtf.ftl
        TemplateLoader[] loaders = new TemplateLoader[] { ftl, ctl };
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        cfg.setTemplateLoader(mtl);
        cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER); 
	}	
	public synchronized void  xml2rtf(Reader reader, Writer out) throws Exception {
		xml2rtf(getQMRF(reader),out);
	}
	public synchronized void  xml2rtf(QMRFObject qmrf, Writer out) throws Exception {
	       Template temp = cfg.getTemplate("qmrf_rtf.ftl", Locale.UK);

	        Map model = new HashMap();
	        // make sure you add the RtfConverter, since this will be needed to escape interpolations as Rtf
	        model.put("RtfConverter", new RtfConverter());

	        qmrf.setCleanTags(true);
	        model.put("qmrf", qmrf);
	     
	        /* Merge data model with template */
	        temp.process(model, out);
	        out.flush();
	}
	
	
	public QMRFObject getQMRF(Reader reader) throws Exception {
		QMRFObject qmrf = new QMRFObject();
		qmrf.transform_and_read(reader,true);
		reader.close();
		return qmrf;
	}


}