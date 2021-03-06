function defineStructuresTable(root,url, query_service, similarity, properties) {

	var proxyURI = (root + "/proxy?uri=" + encodeURIComponent(url));
	var oTable = $('#structures').dataTable( {
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false,
		"aoColumnDefs": [
				{ //0
					"aTargets": [ 0 ],	
					"sClass" : "center",
					"bSortable" : false,
					"bSearchable" : false,
					"mDataProp" : null,
					sWidth : "32px",
					"fnRender" : function(o,val) {
							return "<span class='zoomstruc' title='Click to show QMRF documents'>Show QMRF</span>";
					}
				},			
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 1 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "sHeight" : "155px",
				  "sWidth" : "155px",
				  "fnRender" : function(o,val) {
						var cmpURI = val;
						if (val.indexOf("/conformer")>=0) {
								cmpURI = val.substring(0,val.indexOf("/conformer"));
						}								
						cmpURI = cmpURI + "?media=image/png&w=150&h=150";
						var cmpProxy = (root + "/proxy/img?uri=" + encodeURIComponent(cmpURI));
						return '<img class="ui-widget-content" title="'+val+'" border="0" src="'+cmpProxy+'">';
				  }
				},
				{ "mDataProp": "compound.name" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "bUseRendered" : false,
				  "sClass" : "names",	
				  "sWidth" : "15%",
				  "fnRender" : function(o,val) {
					  	if ((val === undefined) || (val == ""))
					  		return formatValues(o.aData,"names");
					  	else return val;
				  },
				  "bVisible" : true
				},
				{ "mDataProp": "compound.cas" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "sWidth" : "10%",
					  "sClass" : "cas",	
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"cas");
						  	else return val;						  
					  },
					  "bVisible" : true
				},				
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 4 ],
					  "bSearchable" : false,
					  "bSortable" : false,
					  "bUseRendered" : false,
					  "sWidth" : "5%",
					  "fnRender" : function(o,val) {
							var uri = encodeURIComponent(val);
							var qmrf_query = "/qmrf/protocol?structure=" + uri + "&media=text%2Fcsv";
							return '<a href="'+qmrf_query+'" title="Download the QMRF list as CSV"><img class="draw" border="0" src="/qmrf/images/excel.png"></a>';
					  }
				},
				{ "mDataProp": "compound.metric" , "asSorting": [ "asc", "desc" ],
						  "aTargets": [ 5 ],
						  "sTitle" : "Similarity",
						  "sClass" : "similarity",
						  "bSearchable" : true,
						  "bSortable" : true,
						  "sWidth" : "5%",
						  "bVisible"  : similarity
				},
				{ "mDataProp": "values" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 6 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bVisible"  : properties,
						"bUseRendered" : false,
						"fnRender" : function(o,val) {
							var sOut = "<table style='border:1px dashed gray;'>";
							$.each(val,function(index, entry) {
				        		try {
				        			var v = entry;
				        			var name = o.aData.lookup["feature"][index].title;
				        			var units = o.aData.lookup["feature"][index].units;
				        			sOut += "<b>";
				        			sOut += name;
				        			sOut += "</b>&nbsp;  ";
				        			sOut += v;
				        			sOut += " ";				        				
				        			sOut += units;				        				
				        			sOut += "<br/>";
				        		} catch (err) {
				        		}
				        	});
							sOut += "</table>";
							return sOut;
						}					  
					  
				},	
				{ "mDataProp": null , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 7 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : true,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"smiles");
						  	else return val;						  
					  },
					  "bVisible" : false
				},		
				{ "mDataProp": null , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 8 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : true,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"inchi");
						  	else return val;						  
					  },
					  "bVisible" : false
				},	
				{ "mDataProp": null , "asSorting": [ "asc", "desc" ],
					  "sClass" : "inchikey",	
					  "aTargets": [ 9 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : true,
					  "fnRender" : function(o,val) {
						  	if ((val === undefined) || (val == ""))
						  		return formatValues(o.aData,"inchikey");
						  	else return val;						  
					  },
					  "bVisible" : false
				}							
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": proxyURI,
		"sAjaxDataProp" : "dataEntry",
		"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
		      oSettings.jqXHR = $.ajax( {
		        "type": "GET",
		        "url": sSource ,
		        "data": aoData,
		        "dataType": "json", 
		        "contentType" : "application/json",
		        "success": function(json) {
		        	identifiers(json);
		        	//_features = json.feature;
		        	fnCallback(json);
		        },
		        "cache": false,
		        "error" : function( xhr, textStatus, error ) {
		        	oSettings.oApi._fnProcessingDisplay( oSettings, false );
		        }
		      } );
		},
		"oLanguage": {
            "sProcessing": "<img src='/qmrf/images/progress.gif' border='0'>",
            "sLoadingRecords": "No structures found.",
            "sEmptyTable" : "No structures found.",
            "sInfo": "Showing _TOTAL_ structures (_START_ to _END_)",
        "sLengthMenu": 'Display <select>' +
          '<option value="10">10</option>' +
          '<option value="20">20</option>' +
          '<option value="50">50</option>' +
          '<option value="100">100</option>' +
          '<option value="-1">all</option>' +
          '</select> structures.'	   				            
		},	    
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
			//retrieve identifiers
			id_uri = query_service + "/query/compound/url/all?search=" + encodeURIComponent(aData.compound.URI) + "?max=1&media=application%2Fx-javascript";
			$.ajax({
			         dataType: "json",
			         url: (root + "/proxy?uri=" + encodeURIComponent(id_uri)),
			         success: function(data, status, xhr) {
			        	identifiers(data);
			        	$.each(data.dataEntry,function(index, entry) {
				        	aData.compound.name = formatValues(entry,"names");
				        	$('td:eq(2)', nRow).html(aData.compound.name);
				        	aData.compound.cas = formatValues(entry,"cas");
				        	$('td:eq(3)', nRow).html(aData.compound.cas);
				        	aData.compound['smiles'] = formatValues(entry,"smiles");
				        	$('td:eq(6)', nRow).html(aData.compound['smiles']);
				        	aData.compound['inchi'] = formatValues(entry,"inchi");
				        	$('td:eq(7)', nRow).html(aData.compound['inchi']);
				        	aData.compound['inchikey'] = formatValues(entry,"inchikey");
				        	$('td:eq(8)', nRow).html(aData.compound['inchikey']);
			        	});

			         },
			         error: function(xhr, status, err) { },
			         complete: function(xhr, status) { }
			});
			
		}
	} );
	return oTable;
}


function getID() {
	   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}			

	/* QMRF list per structure */
function fnStructureQMRFList(oTable, nTr, id,root) {
	
	var sOut = '<div class="ui-widget-content ui-corner-all" id="' + id + '">';
	//sOut = sOut + "<div id='" + id + "_qmrf' >Please wait while QMRF documents list is loading...</div>";
	
	sOut += "<table id='" + id + "_qmrf'  cellpadding='0' border='0' width='100%' cellspacing='0'>"+
		"<thead><tr><th ></th><th>QMRF Number</th><th>Title</th><th>Endpoint</th><th>Last updated</th><th>Download</th>"+
		"<th></th><th></th></tr></thead><tbody></tbody></table>";
	
	sOut += "</div>";	

	return sOut;
}


function formatValues(dataEntry,tag) {
	var sOut = "";
	$.each(dataEntry.lookup[tag], function(index, value) { 
	  if (dataEntry.values[value] != undefined) {
		  $.each(dataEntry.values[value].split("|"), function (index, v) {
			  if (v.indexOf(".mol")==-1) {
				sOut += v;
			  	sOut += "<br>";
		  	  }
		  });
		  //sOut += dataEntry.values[value];
	  }
	});
	return sOut;
}
