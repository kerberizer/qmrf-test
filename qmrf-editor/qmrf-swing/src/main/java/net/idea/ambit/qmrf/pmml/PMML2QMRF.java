package net.idea.ambit.qmrf.pmml;

import org.dmg.pmml.DataField;
import org.dmg.pmml.PMML;

import com.hp.hpl.jena.rdf.model.Model;

import net.idea.ambit.qmrf.QMRFObject;
import net.idea.ambit.qmrf.catalogs.Catalog;
import net.idea.ambit.qmrf.catalogs.CatalogEntry;
import net.idea.ambit.qmrf.chapters.AbstractQMRFChapter;
import net.idea.ambit.qmrf.chapters.QMRFSubChapterReference;
import net.idea.ambit.qmrf.chapters.QMRFSubChapterText;

public class PMML2QMRF {
	public void insert(PMML pmml, QMRFObject qmrf) throws Exception {

		try {
		setText(getSubchapter(qmrf, 3, 0), String.format("%s %s", pmml.getHeader().getApplication().getName(),
				pmml.getHeader().getApplication().getVersion()));
		} catch (Exception x) {
			
		}
		if (pmml.getDataDictionary().hasDataFields()) {
			AbstractQMRFChapter descr = getSubchapter(qmrf, 3, 2);
			descr.clear();
			int id = 1;
			for (DataField f : pmml.getDataDictionary().getDataFields()) {
				CatalogEntry entry = new CatalogEntry(Catalog.catalog_names[2][0], Catalog.attribute_names[2]);
				entry.setproperty("id", String.format("d%d", id));
				entry.setproperty("name", f.getKey().getValue());
				entry.setproperty("description",
						String.format("%s %s", f.getOpType().toString(), f.getDataType().toString()));
				setText((QMRFSubChapterReference) descr, entry);
				id++;
			}
		}
		int id = 0;
		if (pmml.getModels() != null) {
			AbstractQMRFChapter model = getSubchapter(qmrf, 0, 2);
			AbstractQMRFChapter algorithm = getSubchapter(qmrf, 3, 1);
			model.clear();
			for (org.dmg.pmml.Model m : pmml.getModels()) {
				CatalogEntry entry = new CatalogEntry(Catalog.catalog_names[0][0], Catalog.attribute_names[0]);
				entry.setproperty("id", String.format("m%d", id));
				entry.setproperty("name", m.getModelName());
				if (m.getModelExplanation() != null)
					entry.setproperty("description", m.getModelExplanation().toString());
				// m.getMiningFunction().
				// {"name","url","description","contact","number","ontology_term","id"},
				setText((QMRFSubChapterReference) model, entry);

				try {
					entry = new CatalogEntry(Catalog.catalog_names[1][0], Catalog.attribute_names[1]);
					entry.setproperty("id", String.format("a%d", id));
					entry.setproperty("name", m.getMiningFunction().toString());
					setText((QMRFSubChapterReference) algorithm, entry);
				} catch (Exception x) {
				}
				id++;
			}
		}

	}

	protected AbstractQMRFChapter getSubchapter(QMRFObject qmrf, int chapter, int subchapter) {
		return qmrf.getChapters().get(chapter).getSubchapters().getItem(subchapter);
	}

	protected void setText(QMRFSubChapterReference subchapter, CatalogEntry entry) throws Exception {
		if (subchapter != null) {
			subchapter.getCatalogReference().addItem(entry);
		}
	}

	protected void setText(QMRFSubChapterText subchapter, String value) throws Exception {
		if (subchapter != null)
			subchapter.setText(value);
	}

	protected void setText(AbstractQMRFChapter subchapter, String value) throws Exception {
		if (subchapter instanceof QMRFSubChapterText)
			setText((QMRFSubChapterText) subchapter, value);
	}
}
