package net.idea.rest.structure.resource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.idea.opentox.cli.structure.Compound;
import net.idea.opentox.cli.structure.CompoundClient;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

public class Structure extends Compound {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6291149605331598909L;

	public Structure(URL resourceURL) {
		super(resourceURL);
	}

	public Structure() {
		this(null);
	}

	public Object[] parseURI(Reference baseReference)  {
		if (getResourceIdentifier().toString().indexOf("conformer")>0)
			return OpenTox.URI.conformer.getIds(getResourceIdentifier().toString(),baseReference);
		else //smth wrong with parsing compound uris
			return OpenTox.URI.conformer.getIds(getResourceIdentifier().toString()+"/conformer/0",baseReference);

	}
	
	
	public static List<Structure> retrieveStructures(String queryService, String ref)  throws Exception{
		Reference queryURI = new Reference(queryService);
		HttpClient httpcli = new DefaultHttpClient();

		CompoundClient cli = new CompoundClient(httpcli);
		cli.setHeaders(new Header[]{new BasicHeader("Referer","http://localhost")});
		List<Structure> records = new ArrayList<Structure>();
		try {
			List<URL> urls = cli.listURI(new URL(ref)); 
		
			for (URL url: urls) {
				Structure struc = new Structure(url);
				try {
					Object[] ids = struc.parseURI(queryURI);
					if (ids[0]!=null) struc.setIdchemical((Integer) ids[0]);
					if (ids[1]!=null) struc.setIdstructure((Integer) ids[1]);
				} catch (Exception x) {
				}
				records.add(struc);
			}
			return records;
		} catch (Exception x) {
			throw new ResourceException(HttpStatus.SC_BAD_GATEWAY, "Failed to retrieve chemical structures", String.format(
					"Error when contacting chemical structure service at %s",
					ref),
					"http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html", x);
		} finally {
			try {httpcli.getConnectionManager().shutdown(); } catch (Exception x) {}
		}

	}
}
