package net.idea.ambit.annotation.test;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

import ambit2.base.io.DownloadTool;

public class ReadAnnotationsTest {
	
	
	@Test
	public void test() throws Exception {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		final IRI enmiri = IRI
				//dev version
				.create("https://raw.githubusercontent.com/enanomapper/ontologies/master/enanomapper-dev.owl");
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();

		OWLOntology o = m.loadOntologyFromOntologyDocument(enmiri);

		Set<OWLImportsDeclaration> importDeclarations = o.getImportsDeclarations();
		for (OWLImportsDeclaration declaration : importDeclarations) {
			try {
				m.loadOntology(declaration.getIRI());
				System.out.println("Loaded imported ontology: " + declaration.getIRI());
			} catch (Exception exception) {
				System.out.println("Failed to load imported ontology: " + declaration.getIRI());
			}
		}
		OWLOntologyMerger merger = new OWLOntologyMerger(m);
		String iri = o.getOntologyID().getOntologyIRI().toString();

		String mergedOntologyIRI = iri + "_merged.owl";

		OWLOntology mergedOntology = merger.createMergedOntology(m, IRI.create(mergedOntologyIRI));

		// Create the walker
		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(mergedOntology));
		// Now ask our walker to walk over the ontology
		OWLOntologyWalkerVisitor<Object> visitor = new OWLOntologyWalkerVisitor<Object>(walker) {
			@Override
			public Object visit(OWLObjectSomeValuesFrom desc) {
				System.out.println(desc);
				System.out.println(" " + getCurrentAxiom());
				return null;
			}
		};
		// Have the walker walk...
		walker.walkStructure(visitor);

		File output = File.createTempFile("merged_enm", ".rdf", baseDir);
		IRI documentIRI2 = IRI.create(output);
		m.saveOntology(mergedOntology, documentIRI2);
		// m.removeOntology(o);
	}

	public static File getTestFile(String remoteurl, String localname, String extension, File baseDir)
			throws Exception {
		URL url = new URL(remoteurl);
		boolean gz = remoteurl.endsWith(".gz");
		File file = new File(baseDir, localname + extension + (gz ? ".gz" : ""));
		if (!file.exists())
			DownloadTool.download(url, file);
		return file;
	}
}
