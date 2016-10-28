package net.idea.ambit.pmml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.dmg.pmml.PMML;
import org.junit.Test;

import ambit2.base.io.DownloadTool;

public class PMMLReaderTest {
	@Test
	public void test() throws Exception {
		File pmmlfile =  File.createTempFile("example", ".pmml");
		pmmlfile.deleteOnExit();
		DownloadTool.download(new URL("http://dmg.org/pmml/pmml_examples/KNIME_PMML_4.1_Examples/single_audit_kmeans.xml"),pmmlfile);
		PMML pmml ;
		try(InputStream is = new FileInputStream(pmmlfile)){
		    pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
		}
	}

}
