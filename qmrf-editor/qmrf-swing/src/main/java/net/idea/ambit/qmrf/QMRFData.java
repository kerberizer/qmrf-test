/*
Copyright (C) 2005-2012  

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

package net.idea.ambit.qmrf;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;

import net.idea.ambit.qmrf.annotation.QMRFAnnotationTools;
import net.idea.ambit.qmrf.swing.JTermPanel;
import net.idea.ambit.swing.events.AmbitObjectChanged;
import net.idea.ambit.swing.interfaces.DefaultSharedData;
import net.idea.ambit.swing.interfaces.IAmbitObjectListener;
import net.idea.ambit.swing.interfaces.JobStatus;

public class QMRFData<OBJECT, LIST> extends DefaultSharedData<OBJECT, LIST>
		implements IAmbitObjectListener<QMRFObject> {
	protected QMRFObject qmrf;
	protected JTermPanel termsPanel;
	protected QMRFAnnotationTools tools = new QMRFAnnotationTools();
	protected String term = null;

	public Path getPath() {
		return tools.getPathIndex();
	}
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	protected String term_uri = null;

	public String getTerm_uri() {
		return term_uri;
	}

	public JTermPanel getTermsPanel() {
		return termsPanel;
	}

	public void setTermsPanel(JTermPanel termsPanel) {
		this.termsPanel = termsPanel;
	}

	public QMRFData(String[] args, String configFile, boolean adminUser) {
		super(configFile);

		qmrf = createQMRFObject(args, adminUser);
		if (qmrf.getChapters().size() == 0)
			qmrf.init();
		qmrf.addAmbitObjectListener(this);
	}
	public QMRFData(QMRFObject qmrfobject, String configFile) {
		super(configFile);

		qmrf = qmrfobject;
		if (qmrf.getChapters().size() == 0)
			qmrf.init();
		qmrf.addAmbitObjectListener(this);		
	}
	protected QMRFObject createQMRFObject(String[] args, boolean adminUser) {
		return new QMRFObject(args, adminUser);
	}

	protected void init() {
		loadConfiguration();
		jobStatus = new JobStatus();
		jobStatus.setModified(true);
	}

	public void close() {
		try {
			tools.close();
		} catch (Exception x) {
		}
	}

	public OBJECT getMolecule() {
		// TODO Auto-generated method stub
		return null;
	}

	public LIST getMolecules() {
		// TODO Auto-generated method stub
		return null;
	}

	public LIST getQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	public OBJECT getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public void newMolecule() {
		// TODO Auto-generated method stub

	}

	public void newQuery() {
		// TODO Auto-generated method stub

	}

	public void setMolecule(OBJECT molecule) {
		// TODO Auto-generated method stub

	}

	public void setQuery(OBJECT molecule) {
		// TODO Auto-generated method stub

	}

	public QMRFObject getQmrf() {
		return qmrf;
	}

	public void setQmrf(QMRFObject qmrf) {
		this.qmrf = qmrf;
	}

	public void setParameters(String[] args) {
		qmrf.setParameters(args);
	}

	public void ambitObjectChanged(AmbitObjectChanged event) {
		setChanged();
		notifyObservers(event.getObject());

	}

	public void setAdminUser(boolean adminUser) {
		qmrf.setAdminUser(adminUser);
	}

	public StringBuilder searchTerm(String query, int maxhits) throws Exception {
		StringBuilder b = new StringBuilder();
		if (query == null || "".equals(query.trim()) || "help".equals(query.toLowerCase())) {
			b.append("<h1>Help</h1>");
			b.append(
					"<table><tr><td><h3>Enter text in the box above to search in a local copy of the eNanoMapper <a href='https://github.com/enanomapper/ontologies'>ontology</a></h3></td></tr><tr><td><i>Example:</i> <a href='#toxicity'>toxicity</a></td></tr><tr><td>Alternatively, select text in any of the editable boxes and click <i>Shift-F7</i>. This will initiate search with the selected text.</td></tr><tr><td>Clicking <i>Shift-F8</i> will copy the first ontology term in the text box.</td></tr><tr><td><a href='https://github.com/enanomapper/ontologies/issues'><br/>Report ontology issues</a></td></tr></table>");
			return b;
		}

		TopScoreDocCollector collector = tools.search(query, maxhits);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		if (hits.length == 0) {
			b.append("Not found.");

		} else {
			b.append("<table>");
			for (int i = 0; i < hits.length; ++i) {
				b.append("<tr>");
				int docId = hits[i].doc;
				Document d = tools.getSearcher().doc(docId);
				b.append("<td>");
				String label = d.get("label");
				String subject = d.get("subject");
				String subject_uri = d.get("subject_uri");
				if (i == 0) {
					term = label;
					term_uri = subject_uri;
				}

				b.append(String.format("<h3><a name='result%s'>%s.</a> <a href='#%s'>%s</a> (%s)", (i + 1), (i + 1),
						label, label, subject));
				b.append("</h3>");
				if (subject_uri != null)
					b.append(String.format("<p><a class='help' href='%s'>%s</a></p>", subject_uri, subject_uri));

				List<String> sorted = new ArrayList<String>();

				for (IndexableField f : d.getFields())
					if (!"subject".equals(f.name()) && !"label".equals(f.name()) && !"subject_uri".equals(f.name())) {

						String v = d.get(f.name());
						if (v.startsWith("http")) {
							v = String.format("<a href='%s'>%s</a>",v,v);
						}
						String s = (String.format("<b>%s</b> <span>%s</span><br>", f.name().replaceAll("_", " "), v));
						if (sorted.contains(s))
							continue;
						else
							sorted.add(s);
					}
				Collections.sort(sorted);
				for (String s : sorted)
					b.append(s);
				b.append("</p></td>");
				b.append("</tr>");
			}
			b.append("</table>");
		}
		b.append("<h3><a href='#'>Help</a></h3>");
		return b;
	}
}
