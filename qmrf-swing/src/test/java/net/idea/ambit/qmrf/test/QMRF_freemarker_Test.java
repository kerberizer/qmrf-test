package net.idea.ambit.qmrf.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.idea.ambit.qmrf.QMRFObject;

import org.junit.BeforeClass;
import org.junit.Test;

import com.joliciel.freemarker.rtf.RtfConverter;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.log.Logger;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

public class QMRF_freemarker_Test {
	static Configuration cfg;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Logger.selectLoggerLibrary(Logger.LIBRARY_JAVA);
		cfg = new Configuration();
		
		URL url = QMRF_freemarker_Test.class.getClassLoader().getResource("net/idea/ambit/qmrf/templates");

        FileTemplateLoader ftl = new FileTemplateLoader(new File(url.getFile()));
        ClassTemplateLoader ctl = new ClassTemplateLoader(RtfConverter.class, ""); // don't forget this to access rtf.ftl
        TemplateLoader[] loaders = new TemplateLoader[] { ftl, ctl };
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        cfg.setTemplateLoader(mtl);
        cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER); 
	}


	@Test
	public void testQMRF() throws Exception {
		Template temp = cfg.getTemplate("qmrf.ftl"); 
	//	Writer out = new OutputStreamWriter(System.out);
		QMRFObject qmrf = getQMRF();
		Writer w = new OutputStreamWriter(System.out, "UTF-8");
		Environment env = temp.createProcessingEnvironment(qmrf, w);
		env.setOutputEncoding("UTF-8");
		env.process();  
		w.flush();  
	}
	
	@Test
    public void testQMRF_RTF() throws Exception {

        Template temp = cfg.getTemplate("qmrf_rtf.ftl", Locale.UK);

        Map model = new HashMap();
        // make sure you add the RtfConverter, since this will be needed to escape interpolations as Rtf
        model.put("RtfConverter", new RtfConverter());
        QMRFObject qmrf = getQMRF();
        qmrf.setCleanTags(true);
        model.put("qmrf", qmrf);

     
        /* Merge data model with template */
        File result = new File("test.rtf");
        System.out.println(result.getAbsolutePath());
        Writer out = new FileWriter(result);
        temp.process(model, out);
        out.flush();
        out.close();
    }
	
	public QMRFObject getQMRF() throws Exception {
		URL url = getClass().getClassLoader().getResource("net/idea/ambit/qmrf/QMRF-NEW.xml");
		QMRFObject qmrf = new QMRFObject();
		
		FileInputStream in = new FileInputStream(new File(url.getFile()));
		qmrf.transform_and_read(new InputStreamReader(in,"UTF-8"),true);
		in.close();
		return qmrf;
	}

}

class Wrap {
	RtfConverter RtfConverter = new RtfConverter();
	QMRFObject qmrf;
	public RtfConverter getRtfConverter() {
		return RtfConverter;
	}
	public QMRFObject getQmrf() {
		return qmrf;
	}
	public Wrap(QMRFObject qmrf) {
		this.qmrf = qmrf;
	}
	
}
  