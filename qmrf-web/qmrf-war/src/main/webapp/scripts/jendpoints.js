function defineEndpointsTable(url) {

	
	var oTable = $('#endpoints').dataTable( {
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
							return "<span class='zoomqmrf'><img border='0' src='/qmrf/images/zoom_in.png' alt='zoom in' title='Click to show compound details'></span>";
					}
				},			
				{ "mDataProp": "code" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 1 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  sWidth : "10%"
				},
				{ "mDataProp": "name" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "fnRender" : function(o,val) {
					  var parent = o.aData["parentName"] == ""?"All":encodeURIComponent(o.aData["parentName"]);	
					  return "<a href='/qmrf/catalog/"+ parent +"/"+ encodeURIComponent(o.aData["name"]) +"'>" + o.aData["name"] + "</a>";
				  }
				}					
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		"sAjaxSource": url,
		"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
		      oSettings.jqXHR = $.ajax( {
		        "type": "GET",
		        "url": sSource ,
		        "data": aoData,
		        "dataType": "json", 
		        "contentType" : "application/json",
		        "success": function(json) {
		        	var a = { "aaData" : json };
		        	fnCallback(a);
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

/* QMRF list per structure */
function fnEndpointQMRFList(oTable, nTr, id) {
	var obj = oTable.fnGetData(nTr);
	var sOut = '<div class="ui-widget-content ui-corner-all" id="' + id + '">';
	sOut = sOut + "<div id='" + id + "_qmrf' >Please wait while QMRF documents list is loading...</div>";
	sOut += "</div>";	

	var code = encodeURIComponent(obj["code"]);
	var qmrf_query = "/qmrf/protocol?option=endpointcode&search=" + code + "&headless=true&details=false&media=text%2Fhtml&"+ new Date().getTime();
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
function getID() {
	   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}	

