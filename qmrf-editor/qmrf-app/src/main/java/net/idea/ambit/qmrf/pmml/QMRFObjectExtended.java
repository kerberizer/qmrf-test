package net.idea.ambit.qmrf.pmml;

import java.io.InputStream;

import org.dmg.pmml.PMML;

import net.idea.ambit.qmrf.QMRFObject;

public class QMRFObjectExtended extends QMRFObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3540609208866402969L;


	public QMRFObjectExtended(String[] args, boolean adminUser) {
		super(args,adminUser);
	}
	
	@Override
	public void fromStream(InputStream in, String type) throws Exception {
		if ("pmml".equals(type.toLowerCase()))
			fromPMML(in);
		else
			super.fromStream(in, type);
	}

	public void fromPMML(InputStream in) throws Exception {

		clear();
		setSource("New");
		init();

		try (InputStream is = in) {
			PMML pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
			PMML2QMRF p2q = new PMML2QMRF();
			p2q.insert(pmml, this);
		}
		setNotModified();
		fireAmbitObjectEvent();
	}
}
