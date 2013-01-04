/*
Copyright (C) 2005-2013  

Contact: www.ideaconsult.net

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

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
	
	protected String rootURL="";
	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}
	
	//TODO global config
	Configuration configuration;
	
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public synchronized void initConfig() throws Exception {
		Logger.selectLoggerLibrary(Logger.LIBRARY_JAVA);
		configuration = new Configuration();
		
		URL url = getClass().getClassLoader().getResource("net/idea/ambit/qmrf/templates");

        FileTemplateLoader ftl = new FileTemplateLoader(new File(url.getFile()));
        ClassTemplateLoader ctl = new ClassTemplateLoader(RtfConverter.class, ""); // don't forget this to access rtf.ftl
        TemplateLoader[] loaders = new TemplateLoader[] { ftl, ctl };
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        configuration.setTemplateLoader(mtl);
        configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER); 
	}	
	public synchronized void  xml2rtf(Reader reader, Writer out) throws Exception {
		xml2rtf(getQMRF(reader),out);
	}
	public synchronized void  xml2rtf(QMRFObject qmrf, Writer out) throws Exception {
	       Template temp = configuration.getTemplate("qmrf_rtf.ftl", Locale.UK);

	        Map model = new HashMap();
	        // make sure you add the RtfConverter, since this will be needed to escape interpolations as Rtf
	        model.put("RtfConverter", new RtfConverter());

	        qmrf.setCleanTags(true);
	        model.put("qmrf", qmrf);
	        model.put("root", rootURL==null?"":rootURL);
	     
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