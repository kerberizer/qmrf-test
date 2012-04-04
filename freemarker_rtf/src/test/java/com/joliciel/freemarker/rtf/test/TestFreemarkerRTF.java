 /*
  * Written by Assaf Urieli, Joliciel Informatique (http://www.joli-ciel.com), assaf_at_joli-ciel_dot_com
  * Contributed to the Public Domain.
  */
package com.joliciel.freemarker.rtf.test;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import com.joliciel.freemarker.rtf.RtfConverter;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * A tester for the RTF producing template.
 * @author Assaf Urieli
 */
public class TestFreemarkerRTF {
    
	@Test
    public void test() throws Exception {
        /* Create and adjust the configuration */
        Configuration cfg = new Configuration();
        URL url = TestFreemarkerRTF.class.getClassLoader().getResource("templates");
        FileTemplateLoader ftl = new FileTemplateLoader(new File(url.getFile()));
        ClassTemplateLoader ctl = new ClassTemplateLoader(RtfConverter.class, ""); // don't forget this to access rtf.ftl
        TemplateLoader[] loaders = new TemplateLoader[] { ftl, ctl };
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        cfg.setTemplateLoader(mtl);
        
        /* Get or create a template */
        Template temp = cfg.getTemplate("rtfTest.ftl", Locale.FRENCH);

        /* Create a data model */
        Map model = new HashMap();
        // make sure you add the RtfConverter, since this will be needed to escape interpolations as Rtf
        model.put("RtfConverter", new RtfConverter());
        model.put("greeting", "RTF test.\n ×°ï¬¯×¡ ×žï¬®×›×˜ ï¬® ï¬�×™×“?");
        model.put("name", "Ã©h bien, ×�×¡×£ ÑƒÑ€Ð¸ÐµÐ»Ð¸");
        model.put("reply", "Ã§a va, Ã§a va. ØªØ§Ù„Ø¨ÙŠØ§Ù†Ø§Øª Ø§Ù„Ù…Ø·Ù„ÙˆØ¨Ø©. Un peu fatiguÃ©.\nA few funny letters: Ã¦,Å’,Å“,Å ,Å¡,Å½,Å¾\nNow some Greek: Î ÏŽÏ‚ Î»Î­Î³ÎµÏƒÏ„Îµ?");
        model.put("rtfEscapes", "a backslash: \\, a curley bracket {, and a close curley bracker }");
        model.put("russian", "Ñ€ÑƒÑ�Ñ�ÐºÐ¸Ð¹ Ñ�Ð·Ñ‹Ðº");
        
        // what we want next is a date that has an accent in french - how about fÃ©vrier
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.YEAR, 2005);
        Date februaryDate = cal.getTime();
        model.put("testDate", februaryDate);

        /* Merge data model with template */
        File result = new File("test.rtf");
        System.out.println(result.getAbsolutePath());
        Writer out = new FileWriter(result);
        temp.process(model, out);
        out.flush();
        out.close();
    }
}
