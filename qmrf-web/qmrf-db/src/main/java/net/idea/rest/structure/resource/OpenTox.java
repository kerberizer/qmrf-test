package net.idea.rest.structure.resource;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.routing.Template;


/**
 * Convenience class for classes from OpenTox API {@link http://opentox.org/dev/apis/api-1.1} and
 * ontology {@link http://opentox.org/api/1.1/opentox.owl}
 * @author nina
 *
 */
public class OpenTox {
	public static enum URI {
		query,
		missingValues,
		task,
		algorithm,
		model,
		dataset {
			@Override
			public String getKey() {
				return "dataset_id";
			}
		},
		feature,
		reference,
		compound {
			
			public Object getId(String uri,Template template)  {
				return super.getIds(removeDatasetFragment(uri), template);
			}
		},
		conformer {
			@Override
			public String getResourceID() {
				return String.format("%s%s/{%s}",OpenTox.URI.compound.getResourceID(),getURI(),getKey());
			}
			public Object[] getIds(String uri,Reference baseReference)  {
				return getIds(removeDatasetFragment(uri),getTemplate(baseReference));
			}
			public Object[] getIds(String uri,Template template)  {
				uri = removeDatasetFragment(uri);
				Map<String, Object> vars = new HashMap<String, Object>();
				Object[] ids = new Object[2];
				try { 
					template.parse(uri, vars);
					try {
					ids[0] = Integer.parseInt(vars.get(OpenTox.URI.compound.getKey()).toString()); } 
					catch (Exception x) { ids[0] = null;};
				
					try { ids[1] = Integer.parseInt(vars.get(OpenTox.URI.conformer.getKey()).toString()); } 
					catch (Exception x) { ids[1] = null;};
				} catch (Exception x) {};
				return ids;
			}			
		};
		public String getURI() {
			return String.format("/%s",toString());
		}
		public String getKey() {
			return String.format("id%s",toString());
		}		
		public String getResourceID() {
			return String.format("%s/{%s}",getURI(),getKey());
		}			
		public String getValue(Request request) throws Exception {
			Object o = request.getAttributes().get(getKey());
			return o==null?null:Reference.decode(o.toString());
		}
		public int getIntValue(Request request) throws Exception {
			return Integer.parseInt(getValue(request));
		}		
		public Template getTemplate(Reference baseReference) {
			return new Template(String.format("%s%s",baseReference==null?"":baseReference,getResourceID()));
		}
		public Object getId(String uri, Reference baseReference) {
			return getId(uri, getTemplate(baseReference));
		}
		public Object getId(String uri,Template template)  {
			Map<String, Object> vars = new HashMap<String, Object>();
			
			try {
				template.parse(uri, vars);
				return Integer.parseInt(vars.get(getKey()).toString()); 
			} catch (Exception x) { return null; }
		}
		public Object[] getIds(String uri,Reference baseReference)  {
			return null;
		}
		public Object[] getIds(String uri,Template template)  { return null; }
	};
	
	public enum params  {
		filter {
			@Override
			public String getDescription() {
				return "Filtered URI(s)";
			}
		},		
		condition {
			@Override
			public String getDescription() {
				return "Filtered URI(s) condition";
			}
		},			
		feature_uris {
			@Override
			public String toString() {
				return String.format("%s[]",super.toString());
			}
			@Override
			public String getDescription() {
				return "Feature URI(s)";
			}
		},
		sameas {

			@Override
			public String getDescription() {
				return "sameas";
			}
		},		
		model_uri {
			@Override
			public String getDescription() {
				return "Model URI";
			}
		},		
		algorithm_uri {
			@Override
			public String getDescription() {
				return "Algorithm URI";
			}
		},	
		appdomain_alg {
			@Override
			public String getDescription() {
				return "App domain algorithms";
			}
		},	
		appdomain_model {
			@Override
			public String getDescription() {
				return "App domain model";
			}
		},			
		compound_uris {
			public boolean isMandatory() {
				return false;
			}
			@Override
			public String toString() {
				return String.format("%s[]",super.toString());
			}			
			public String getDescription() {
				return "Compound URIs";
			}			
		},	
		delay {
			public boolean isMandatory() {
				return false;
			}
			public String getDescription() {
				return "Delay,ms for /algorithm/mockup";
			}			
		},		
		error {
			public boolean isMandatory() {
				return false;
			}
			public String getDescription() {
				return "Error to be thrown by /algorithm/mockup";
			}			
		},			
		dataset_uri {
			public boolean isMandatory() {
				return true;
			}
			public String getDescription() {
				return "Dataset URI to be used as input to model generation (Algorithm POST) or prediction (Model POST)";
			}			
		},
		target {
			public boolean isMandatory() {
				return false;
			}			
			public String getDescription() {
				return "Feature URI, target variable for model generation of classification and regression models";
			}	
			@Override
			public String toString() {
				return "prediction_feature";
			}
		},
		parameters {
			public boolean isMandatory() {
				return false;
			}			
			public String getDescription() {
				return "Algorithm parameters";
			}			
		},		
		source_uri {
			public String getDescription() {
				return "either use ?source_uri=URI, or POST with text/uri-list or RDF representation of the object to be created";
			}
		},
		dataset_service {
			public String getDescription() {
				return "Destination dataset service, used by Algorithms and Models to store results";
			}
		},
		page {
			public String getDescription() {
				return "Page";
			}
		},
		pagesize {
			public String getDescription() {
				return "Page size";
			}
		};
		public boolean isMandatory() {
			return true;
		}
		public String getDescription() {
			return "either use ?source_uri=URI, or POST with text/uri-list or RDF representation of the object to be created";
		}
		public Object getFirstValue(Form form) {
			return form.getFirstValue(toString());
		}
		public String[] getValuesArray(Form form) {
			return form.getValuesArray(toString());
		}
	};
	
	
	private OpenTox() {
	}
	
	public static String removeDatasetFragment(String uri) {
		if (uri==null) return null;
		int posDataset = uri.indexOf("/dataset/");
		if (posDataset<0) return uri;
		
		int posCompound = uri.indexOf("/compound/");
		if ((posCompound > 0) && (posCompound>posDataset)) return String.format("%s%s", uri.substring(0,posDataset),uri.substring(posCompound));
		return uri;
	}
	
	
}
