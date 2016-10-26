package net.idea.ambit.annotation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFReader;

import ambit2.base.io.DownloadTool;

public class AnnotationTools {
	
	protected Path pathIndex = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir") + "/enmindez");
	public Path getPathIndex() {
		return pathIndex;
	}

	public void setPathIndex(Path pathIndex) {
		this.pathIndex = pathIndex;
	}

	protected IndexReader reader = null;
	protected IndexSearcher searcher = null;
	public IndexSearcher getSearcher() {
		return searcher;
	}

	protected Directory index = null;
	protected Analyzer analyzer = new StandardAnalyzer();

	public void open() throws Exception {
		if (!pathIndex.toFile().exists()) {
			//URL url = getClass().getClassLoader().getResource("net/idea/ambit/annotation/merged_enm.rdf.gz");
			File tmpfile = new File(System.getProperty("java.io.tmpdir"),"merged_enm.rdf.gz");

			DownloadTool.download("net/idea/ambit/annotation/merged_enm.rdf.gz", tmpfile);

			smash(tmpfile, true, pathIndex);
		} 
		if (index==null || reader==null || searcher==null) {
			try {close();} catch (Exception x) {}
			index = FSDirectory.open(pathIndex);
			reader = DirectoryReader.open(index);
			searcher = new IndexSearcher(reader);
		}
	}

	public void close() throws Exception {
		if (reader != null) {
			reader.close();
			reader=null;
		}	
		if (index != null) {
			index.close();
			index =null;
		}	
		searcher=null;
		
	}
	protected boolean isClosed() {
		return (index==null || reader==null || searcher==null);
	}
	public TopScoreDocCollector search(String query, int maxhits) throws Exception {
		if (isClosed()) open();
		org.apache.lucene.search.Query q = new QueryParser("_text", analyzer).parse(query.replace(":", " "));
		TopScoreDocCollector collector1 = TopScoreDocCollector.create(maxhits);
		searcher.search(q, collector1);
		return collector1;
		// "http://www.w3.org/2002/07/owl#Thing");
	}


	protected void printHits(String prefix, ScoreDoc[] hits) throws IOException {
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			for (IndexableField f : d.getFields()) {
				System.out.println(
						String.format("%s\t%s\t%s\t%s\t%s", hits[i].score, i, docId, f.name(), d.get(f.name())));
			}
			System.out.println();
		}
	}
	protected String printNode(RDFNode node) {
		return node == null ? null
				: node.isLiteral() ? (node.asLiteral().getString())
						: (node.isResource() ? (node.asResource().getLocalName()) : node.getClass().getName());
	}

	public void smash(File file, boolean zipped, Path indexed) throws Exception {

		Model jmodel = ModelFactory.createDefaultModel();

		InputStreamReader in = null;
		try {
			RDFReader reader = jmodel.getReader("RDF/XML");

			if (file.getName().endsWith(".gz")) {
				
				in = new InputStreamReader(new GZIPInputStream(new FileInputStream(file)));
			} else
				in = new InputStreamReader(new FileInputStream(file));
			
			reader.read(jmodel, in, "RDF/XML");
			System.out.println("Reading completed " + file.getAbsolutePath());

			String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT distinct ?s ?p (group_concat(distinct ?l;separator=';') as ?pl) ?o  where {?s ?p ?o. ?s a owl:Class. ?p a owl:AnnotationProperty. OPTIONAL {?p rdfs:label ?l.}. }  group by ?s ?p ?o order by ?s";

			Query qry = QueryFactory.create(sparql);
			QueryExecution qe = QueryExecutionFactory.create(qry, jmodel);
			ResultSet rs = qe.execSelect();

			Directory index = FSDirectory.open(indexed);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(index, config);

			String s_prev = null;
			Document doc = null;
			BufferedWriter w = new BufferedWriter(new FileWriter(new File("test.txt")));
			StringBuilder _catchall = new StringBuilder();
			while (rs.hasNext()) {
				QuerySolution sol = rs.nextSolution();
				String s = sol.get("s").asResource().getLocalName();
				String s_uri = sol.get("s").asResource().getURI();
				String p = printNode(sol.get("p"));
				String o = printNode(sol.get("o"));
				StringBuilder pb = null;
				try {
					String[] pls = sol.get("pl").asLiteral().getString().split(";");
					if (pls.length == 1) {
						pb = new StringBuilder();
						pb.append(pls[0]);
					} else {
						Arrays.sort(pls);
						String l1 = null;
						for (String l : pls) {
							if (!l.equals(l1)) {
								if (pb == null)
									pb = new StringBuilder();
								pb.append(l);
								pb.append(" ");
								l1 = l;
							}
						}
					}
				} catch (Exception x) {
					pb = null;
				}
				String pl = pb == null ? null : pb.toString();
				w.write(String.format("%s\t%s\t%s\t'%s'\t%s\n", s,s_uri, p, pl, o));
				if (!s.equals(s_prev)) {
					if (doc != null) {
						TextField tf = new TextField("_text", _catchall.toString(), Store.NO);
						doc.add(tf);
						writer.addDocument(doc);
						_catchall = new StringBuilder();

					}
					s_prev = s;
					doc = new Document();
					Field tf = new TextField("subject", s, Store.YES);
					doc.add(tf);
					tf = new TextField("subject_uri", s_uri, Store.YES);
					doc.add(tf);
					_catchall.append(s);
					_catchall.append(" ");
				}
				Field tf = new TextField(pl == null ? p : (p.equals(pl) ? p : pl), o, Store.YES);
				doc.add(tf);

				_catchall.append(o);
				_catchall.append(" ");

			}

			TextField tf = new TextField("_text", _catchall.toString(), Store.NO);
			doc.add(tf);
			writer.addDocument(doc);

			writer.close();

			qe.close();

			System.out.println("Indexing completed " + indexed.toString());
			w.close();

			// Resource root = jmodel.createResource(rootResource);

			try {
				// traverse(root, jmodel, 0, null);
			} finally {
				try {
					// out.close();
				} catch (Exception x) {
				}
			}

		} finally {
			jmodel.close();
			try {
				if (in != null)
					in.close();
			} catch (Exception x) {
			}

		}
	}

	public static void main(String[] args) {
		AnnotationTools tools = new AnnotationTools();
		try {
			//tools.open();
			TopScoreDocCollector collector = tools.search(args[0], 100);
			ScoreDoc[] hits1 = collector.topDocs().scoreDocs;
			tools.printHits("", hits1);
			System.out.println(String.format("Query completed %s\t%s", tools.getPathIndex().toString(), hits1.length));
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
