<script type="text/javascript">
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	
	var url = "${qmrf_request_jsonp}";

  	var oTable = defineTable(url);
    <!-- Details panel -->	
	$('#structures tbody td .zoom img').live(
			'click',
			function() {
				var nTr = $(this).parents('tr')[0];
				if (oTable.fnIsOpen(nTr)) {
					this.src = "/qmrf/images/zoom_in.png";
					this.alt = "Zoom in";
					this.title='Click to show QMRF documents';
					oTable.fnClose(nTr);
				} else {
				    this.alt = "Zoom out";
					this.src = "/qmrf/images/zoom_out.png";
					this.title='Click to close QMRF documents list';
					var id = 'v'+getID();
					oTable.fnOpen(nTr, fnFormatDetails(nTr,id),	'details');
											       
				}
		});
		
	function getID() {
		   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}			
	
		/* Formating function for row details */
	function fnFormatDetails(nTr, id) {
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
} );

function defineTable(url) {

	
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
					"bUseRendered" : "true",
					"fnRender" : function(o,val) {
							return "<span class='zoom'><img border='0' src='/qmrf/images/zoom_in.png' alt='zoom in' title='Click to show compound details'></span>";
					},
				},			
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 1 ],	
				  "bSearchable" : true,
				  "bUseRendered" : false,
				  "bSortable" : true,
				  "sHeight" : "160px",
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
						return '<a href="'+val+'" title="'+cmpURI+'"><img class="ui-widget-content" border="0" src="'+cmpURI+'&w=150&h=150"></a>';
				  }
				},
				{ "mDataProp": "compound.name" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 2 ],
				  "bSearchable" : true,
				  "bSortable" : true	
				},
				{ "mDataProp": "compound.cas" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 3 ],
					  "bSearchable" : true,
					  "bSortable" : true	
				},				
				{ "mDataProp": "compound.metric" , "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 4 ],
				  "bSearchable" : true,
				  "bSortable" : true,
				  "bVisible"  : '${query.option!""}' == 'similarity'
				},
				{ "mDataProp": "compound.URI" , "asSorting": [ "asc", "desc" ],
					  "aTargets": [ 5 ],
					  "bSearchable" : true,
					  "bSortable" : true,
					  "bUseRendered" : false,
					  "fnRender" : function(o,val) {
							var uri = encodeURIComponent(val);
							var qmrf_query = "/qmrf/protocol?structure=" + uri + "&media=text%2Fcsv";
							return '<a href="'+qmrf_query+'" title="Download the QMRF list as CSV"><img class="draw" border="0" src="/qmrf/images/excel.png"> Download as CSV</a>';
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
		        "success": fnCallback,
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
</script>
