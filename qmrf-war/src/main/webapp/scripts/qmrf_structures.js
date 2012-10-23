function defineStructuresTable(url, similarity) {

	
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
							return "<span class='zoomstruc'><img border='0' src='/qmrf/images/zoom_in.png' alt='zoom in' title='Click to show compound details'></span>";
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
						//if ((opentox["model_uri"]==null) || (opentox["model_uri"] == undefined)) {
								cmpURI = cmpURI + "?media=image/png";
						//} else {
						//		cmpURI = opentox["model_uri"] + "?dataset_uri=" + cmpURI + "&media=image/png";
						//}
						return '<img class="ui-widget-content" title="'+val+'" border="0" src="'+cmpURI+'&w=150&h=150">';
				  }
				},
				{ "mDataProp": "compound.name" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "bUseRendered" : false,
				  "fnRender" : function(o,val) {
					    return formatValues(o.aData,"names");
				  },
				  "bVisible" : true
				},
				{ "mDataProp": "compound.cas" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "sWidth" : "10%",
					  "fnRender" : function(o,val) {
						   return formatValues(o.aData,"cas");
					  },
					  "bVisible" : true
				},				
				{ "mDataProp": "compound.metric" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 4 ],
				  "sTitle" : "Similarity",
				  "sClass" : "similarity",
				  "bSearchable" : true,
				  "bSortable" : true,
				  "sWidth" : "5%",
				  "bVisible"  : similarity
				},
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 5 ],
					  "bSearchable" : false,
					  "bSortable" : false,
					  "bUseRendered" : false,
					  "sWidth" : "5%",
					  "fnRender" : function(o,val) {
							var uri = encodeURIComponent(val);
							var qmrf_query = "/qmrf/protocol?structure=" + uri + "&media=text%2Fcsv";
							return '<a href="'+qmrf_query+'" title="Download the QMRF list as CSV"><img class="draw" border="0" src="/qmrf/images/excel.png"></a>';
					  }
	
					}	
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
		"sAjaxDataProp" : "dataEntry",
		"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
		      oSettings.jqXHR = $.ajax( {
		        "type": "GET",
		        "url": sSource ,
		        "data": aoData,
		        "dataType": "jsonp", 
		        "contentType" : "application/x-javascript",
		        "success": function(json) {
		        	identifiers(json);
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
	            "sLoadingRecords": "No records found."
	    }	     
	} );
	return oTable;
}


function getID() {
	   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}			

	/* QMRF list per structure */
function fnStructureQMRFList(oTable, nTr, id) {
	var obj = oTable.fnGetData(nTr);
	var sOut = '<div class="ui-widget-content ui-corner-all" id="' + id + '">';
	sOut = sOut + "<div id='" + id + "_qmrf' >Please wait while QMRF documents list is loading...</div>";
	sOut += "</div>";	

	var uri = encodeURIComponent(obj["compound"]["URI"]);
	var qmrf_query = "/qmrf/protocol?structure=" + uri + "&headless=true&details=false&media=text%2Fhtml&"+ new Date().getTime();

      $.ajax({
          dataType: "html",
          url: qmrf_query,
          success: function(data, status, xhr) {
        	  $('div#' + id + '_qmrf').html(data);
          },
          error: function(xhr, status, err) {
          },
          complete: function(xhr, status) {
          }
       });
       
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
