package net.idea.ambit.qmrf.annotation;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import net.idea.ambit.annotation.AnnotationTools;
import net.idea.ambit.qmrf.catalogs.Catalog;
import net.idea.ambit.qmrf.catalogs.CatalogEntry;
import net.idea.ambit.qmrf.catalogs.Catalogs;

public class QMRFAnnotationTools extends AnnotationTools {

	@Override
	protected Path createPath() {
		return FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir") + "/enmindeq");
	}

	@Override
	protected IndexWriter process(IndexWriter writer, File tmpfile) throws Exception {
		try {
			super.process(writer, tmpfile);
		} finally {
		}
		try {
			processCatalogs(writer);
			writer.flush();
			writer.commit();
		} finally {
			writer.close();
		}
		return null;
	}

	protected void processCatalogs(IndexWriter writer) throws Exception {
		System.out.println("Indexing catalogs ");
		InputStream in = getClass().getClassLoader().getResourceAsStream("ambit2/qmrfeditor/qmrf_catalogs.xml");
		if (in == null)
			return;

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(in));
			Catalogs c = new Catalogs();
			c.fromXML(doc.getDocumentElement());
			Catalog catalog = c.get("endpoints_catalog");
			for (int i = 0; i < catalog.size(); i++) {
				CatalogEntry e = catalog.getItem(i);
				String name = e.getProperty("protocol");
				String[] names = name.split(" ");
				org.apache.lucene.document.Document ldoc = null; 
				if (name.indexOf("EC") == 0) {
					StringBuilder b = new StringBuilder();
					for (int j = 3; j < names.length; j++) {
						b.append(names[j]);
						b.append(" ");
					}
					ldoc = createDocument(String.format("%s %s %s", names[0], names[1], names[2]),
							b.toString().trim(), e);
				} else if (name.indexOf("OECD") >= 0) {
					StringBuilder b = new StringBuilder();
					for (int j = 2; j < names.length; j++) {
						b.append(names[j]);
						b.append(" ");
					}
					ldoc = 
							createDocument(String.format("%s %s", names[0].replaceAll("OECD ", "OECD TG "), names[1]),
									b.toString().trim(), e);
				} else if (name.indexOf("SOP N")>=0) {
					StringBuilder b = new StringBuilder();
					for (int j = 3; j < names.length; j++) {
						b.append(names[j]);
						b.append(" ");
					}
					e.getAttributes().put("protocol_uri", "http://www.nanopartikel.info/files/methodik/NANOMMUNE-Quality-Handbook-SOPs.pdf");
					ldoc = 
							createDocument(String.format("%s %s %s", names[0], names[1].toUpperCase(),names[2].toLowerCase()),
									b.toString().trim(), e);
				}
				if (ldoc!=null && writer!=null)
					writer.addDocument(ldoc);

			}
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			if (in != null)
				in.close();
		}
		System.out.println("Indexing catalogs completed");
	}

	protected org.apache.lucene.document.Document createDocument(String tag, String name, CatalogEntry e)
			throws Exception {
		
		String topcategory = e.getProperty("group");
		String endpoint = e.getProperty("name");

		String term = e.getProperty("ontology_term");
		String protocoluri = e.getProperty("protocol_uri");
		/*
		try {
			term = null;
			TopScoreDocCollector collector = search(endpoint.toString().replaceAll(":", " "), 1);
			ScoreDoc[] hits1 = collector.topDocs().scoreDocs;
			if (hits1 != null && hits1.length > 0) {
				org.apache.lucene.document.Document d = searcher.doc(hits1[0].doc);
				term = d.get("subject_uri");
				term_label = d.get("label");
				//printHits("", hits1);
			}
		} catch (Exception x) {
			term = null;
			term_label = null;
		}
		 */
		org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

		Field tf = new TextField("subject", tag, Store.YES);
		doc.add(tf);
		tf = new TextField("label", name, Store.YES);
		doc.add(tf);
		if (endpoint!=null && !"".equals(endpoint))
			tf = new TextField("endpoint", endpoint.toString(), Store.YES);
		doc.add(tf);
		if (term != null && !"".equals(term)) {
			tf = new TextField("ontology_term", term, Store.YES);
			doc.add(tf);
		}

		if (protocoluri != null && !"".equals(protocoluri)) {
			tf = new TextField("protocol_uri", protocoluri, Store.YES);
			doc.add(tf);
		}

		StringBuilder _catchall = new StringBuilder();
		_catchall.append(tag + " " + name + " " + endpoint);

		tf = new TextField("_text", _catchall.toString(), Store.NO);
		doc.add(tf);

		//String catalog = String.format("<endpoint id='%s' group='%s' protocol='%s %s' name='%s' ontology_term='%s' protocol_uri='' />",	UUID.nameUUIDFromBytes((tag+name).getBytes()),topcategory,tag,name.trim(),endpoint.toString().trim(),term==null?"":term	);
		//System.out.println(catalog);
		return doc;
	}

	public static void main(String[] args) {
		QMRFAnnotationTools tools = new QMRFAnnotationTools();
		try {
			tools.processCatalogs(null);
			// tools.open();
			/*
			 * TopScoreDocCollector collector = tools.search(args[0], 1);
			 * ScoreDoc[] hits1 = collector.topDocs().scoreDocs;
			 * tools.printHits("", hits1);
			 * System.out.println(String.format("Query completed %s\t%s",
			 * tools.getPathIndex().toString(), hits1.length));
			 */
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				tools.close();
			} catch (Exception x) {
			}
		}
	}
}
