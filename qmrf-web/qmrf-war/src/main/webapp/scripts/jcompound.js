/**
 *  Input: 
 *  opentox object, with opentox.feature array nonempty
 */
function identifiers(opentox) {
	var lookup = {
		cas    : [],
		names  : [],
		einecs : [],
	    reachdate : []
	};
	//names
	var count = [0,0,0,0,6];
	//opentox['feature'].sort(function(a,b){return a['order'] -b['order']});
	
    $.each(opentox.feature, function(k, value) {
    		if (value.sameAs == "http://www.opentox.org/api/1.1#IUPACName") {
    			if (opentox.feature[k]) { lookup.names.push(k); count[0]++; }
    		} else if (value.sameAs == "http://www.opentox.org/api/1.1#ChemicalName") {
	        	if (opentox.feature[k]) { lookup.names.push(k); count[0]++; }
	        } else if (value.sameAs == "http://www.opentox.org/api/1.1#CASRN") { 
	        	if (opentox.feature[k]) { lookup.cas.push(k); count[1]++;  }
	        } else if (value.sameAs == "http://www.opentox.org/api/1.1#EINECS") { 
	        	if (opentox.feature[k]) {lookup.einecs.push(k); count[2]++;  }	        
	        } else if (value.sameAs == "http://www.opentox.org/api/1.1#REACHRegistrationDate") { 
	        	if (opentox.feature[k]) {lookup.reachdate.push(k); count[3]++;  }
	        } else {
	        	//console.log(k);
	        	count[4]++;
	        	if (count[4]<=200) { //TODO check how it works with many columns
		        	var thclass = "property";
		        	var visible = false;
		        	if (value.sameAs == "http://www.opentox.org/api/1.1#SMILES") { thclass = " smiles"; visible = false; }
		        	else if (value.sameAs == "http://www.opentox.org/api/1.1#InChI") { thclass = " inchi"; visible = false; }
		        	else if (value.sameAs.indexOf("http://www.opentox.org/echaEndpoints.owl")>=0) { thclass += " endpoint"; visible = opentox["showEndpoints"]; }	
					var source = opentox.feature[k]["source"]["type"];
					if (source=="Algorithm" || source=="Model") { thclass += " calculated"; visible |= opentox["showCalculated"]; }	
	        	}
	        }
	        //be quick, we only need something to display in the table, detailed info will be on expand
	        //if (count[0]>0 && count[1]>0 && count[2]>0 && count[3]>0) 	return;
	    });
    
    	$.each(opentox.dataEntry, function(k, value) {
    		value["lookup"] = lookup;
    	    value["getValue"] =  function(dataEntry,type) {
    			var sOut = "";
    			$.each(this[type], function(index, value) { 
    			  sOut += dataEntry.values[value];
    			  sOut += " ";
    			});
    			return sOut;
    	    };    		
    	});

}
