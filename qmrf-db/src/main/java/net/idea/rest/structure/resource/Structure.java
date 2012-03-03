package net.idea.rest.structure.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.toxbank.client.resource.AbstractToxBankResource;
import net.toxbank.client.resource.Protocol;

import org.opentox.rdf.OpenTox;
import org.restlet.data.Reference;

public class Structure extends AbstractToxBankResource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6291149605331598909L;
	protected int idchemical;
	protected int idstructure;
	protected Hashtable<String, String> properties; 
	public Hashtable<String, String> getProperties() {
		if (properties==null) properties = new Hashtable<String, String>();
		return properties;
	}
	public void setProperties(Hashtable<String, String> properties) {
		this.properties = properties;
	}
	public int getIdchemical() {
		return idchemical;
	}
	public void setIdchemical(int idchemical) {
		this.idchemical = idchemical;
	}

	public int getIdstructure() {
		return idstructure;
	}
	public void setIdstructure(int idstructure) {
		this.idstructure = idstructure;
	}
	String name;
	String cas;
	String einecs;
	String SMILES;
	String InChI;
	String InChIKey;
	
	List<Protocol> protocols;
	
	public List<Protocol> getProtocols() {
		if (protocols==null) protocols=new ArrayList<Protocol>();
		return protocols;
	}
	public void setProtocols(List<Protocol> protocols) {
		this.protocols = protocols;
	}
	protected String similarity = null;
	public String getSimilarity() {
		return similarity;
	}
	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCas() {
		return cas;
	}
	public void setCas(String cas) {
		if (cas==null) this.cas = null;
		else {
			int index = cas.indexOf("|");
			this.cas = index>0?cas.substring(0,index):cas;
		}
	}
	public String getEinecs() {
		return einecs;
	}
	public void setEinecs(String einecs) {
		this.einecs = einecs;
	}
	public String getSMILES() {
		return SMILES;
	}
	public void setSMILES(String sMILES) {
		SMILES = sMILES;
	}
	public String getInChI() {
		return InChI;
	}
	public void setInChI(String inChI) {
		InChI = inChI;
	}
	
	public String getInChIKey() {
		return InChIKey;
	}
	public void setInChIKey(String inChIKey) {
		InChIKey = inChIKey;
	}
	
	public Object[] parseURI(Reference baseReference)  {
		return OpenTox.URI.conformer.getIds(getResourceURL().toString(),baseReference);

	}
}
