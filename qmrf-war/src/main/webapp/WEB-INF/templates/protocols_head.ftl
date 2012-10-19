<script>$(function() {$( ".tabs" ).tabs({cache: true});});</script>

<script type="text/javascript">
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	//headers
	var isAdmin = "${editorRole}" || "${managerRole}";
	$('#protocols .qmrfOwner').html(function() { return isAdmin=="true"?"Owner":""; });
	$('#protocols #manageHeader').html(function() { return isAdmin=="true"?"Manage":""; });
	
	var oTable = $('#protocols').dataTable( {
		"sAjaxDataProp" : "qmrf",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": true,
		"sAjaxSource": "${qmrf_request_json}&" + new Date().getTime() ,
		"fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
		      oSettings.jqXHR = $.ajax( {
		        "dataType": 'json',
		        "type": "GET",
		        "url": sSource,
		        "data": aoData,
		        statusCode: {
		            404: function() {
		            	oSettings.oLanguage.sLoadingRecords  = xhr.statusText;
		            }
		        },
		        "error" : function( xhr, textStatus, error ) {
		        	oSettings.oApi._fnProcessingDisplay( oSettings, false );
		        },		        
		        "success": fnCallback,

		      } );
		},

		"aoColumns": [
				{ //0
					"aTargets": [ 0 ],	
					"sClass" : "center",
					"bSortable" : false,
					"mDataProp" : null,
					sWidth : "32px",
					"bUseRendered" : "true",
					"fnRender" : function(o,val) {
							return "<span class='zoom'><img src='/qmrf/images/zoom_in.png' alt='zoom in' title='Click to show compound details'></span>";
					},
				},			              
				{ "mDataProp": "identifier" , "asSorting": [ "asc", "desc" ], "aTargets": [ 1 ],	
				  sWidth : "15%",
 			      "fnRender": function ( o, val ) {
          				return "<a href='"+o.aData["uri"] + "'>" + o.aData["identifier"] + "</a>";
        			}
				},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ], "aTargets": [ 2 ]
				},
				{ "mDataProp": "endpoint.code" , "bUseRendered" : "true",
				  "asSorting": [ "asc", "desc" ], 
				  "bSearchable" : true, 
				  "aTargets": [ 3 ],
				  "fnRender": function ( o, val ) {
					return "TODO";  
				  },
				  sWidth : "10%"
			    },
				{ "mDataProp": "updated", 
				  "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 4 ],
				  sWidth : "10%"
				},
				{ "mDataProp": null, bSortable: false,"bUseRendered" : "true",
				    "fnRender": function ( o, val ) {
			    	 var sOut = 
			    	 "<span>"+
			    	 "<a href='"+ o.aData["uri"] + "/document?media=application%2Fpdf'><img src='/qmrf/images/pdf.png' title='Download as PDF'></a>&nbsp;"+
			    	 "<a href='"+ o.aData["uri"] + "/document?media=application%2Fexcel'><img src='/qmrf/images/excel.png' title='Download as MS Excel'></a>&nbsp;"+
			    	 "<a href='"+ o.aData["uri"] + "/document?media=application%2Frtf'><img src='/qmrf/images/word.png' title='Download as Rich Text Format (RTF)'></a>&nbsp;"+
			    	 "<a href='"+ o.aData["uri"] + "/document?media=application%2Fxml'><img src='/qmrf/images/xml.png' title='Download as QMRF XML'></a>&nbsp;"+
			    	 "</span>";
			    	 return sOut;
	        	   },
	        	   sWidth : "80px" 
				},
				{ "mDataProp": "owner.username" , 
				  "asSorting": [ "asc", "desc" ], 
				  "aTargets": [ 6 ],
				  sWidth : "10%",
				  "bVisible": false
			   },
				{ "mDataProp": null, bSortable: false,
				      "fnRender": function ( o, val ) {
	          				return isAdmin=="true"?"TODO: Manage links":"";
	        			},
	        	   "sWidth" : "5%",
	     		   "bVisible" : function(o,val) { return isAdmin;}
				},			   
			],
		"bJQueryUI" : true,
		"bPaginate" : true,
		"bDeferRender": true,
		"bSearchable": true,
		/*
		"sDom": '<"H"fr>tC<"F"ip>', //ColVis 
		"oColVis": {
			"buttonText": "&nbsp;",
			"bRestore": true,
			"sAlign": "left"
		},
		"fnDrawCallback": function (o) {
			var nColVis = $('div.ColVis', o.nTableWrapper)[0];
			nColVis.style.width = o.oScroll.iBarWidth+"px";
			nColVis.style.top = ($('div.dataTables_scroll', o.nTableWrapper).position().top)+"px";
			nColVis.style.height = ($('div.dataTables_scrollHead table', o.nTableWrapper).height())+"px";
		},		
		*/
		"oLanguage": {
	            "sProcessing": "<img src='/qmrf/images/progress.gif' border='0'>",
	            "sLoadingRecords": "No records found."
	    }	
	} );
	
	$('#protocols tbody td .zoom img').live(
			'click',
			function() {
				var nTr = $(this).parents('tr')[0];
				if (oTable.fnIsOpen(nTr)) {
					this.src = "/qmrf/images/zoom_in.png";
					this.alt = "Zoom in";
					this.title='Click to show QMRF document details';
					oTable.fnClose(nTr);
				} else {
				    this.alt = "Zoom out";
					this.src = "/qmrf/images/zoom_out.png";
					this.title='Click to close QMRF document details panel';
					var id = 'v'+getID();
					oTable.fnOpen(nTr, fnFormatDetails(nTr,id),	'details');
											       
				}
		});
	
	function getID() {
		   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}	
	
	/* Formating function for row details */
	function fnFormatDetails(nTr, id) {
		var qmrfDocument = oTable.fnGetData(nTr);
		var sOut = '<div id="' + id + '" style="display: block;">';
		sOut = sOut + "<div id='" + id + "_tabs' class='tabs'>Please wait while QMRF document chapters are loading...</div>";
		sOut = sOut + "</div>";	

	      $.ajax({
	          dataType: "html",
	          url: qmrfDocument.uri + "/chapters?headless=true&media=text%2Fhtml&"+ new Date().getTime(),
	          success: function(data, status, xhr) {
	        	  $('div#' + id + '_tabs' ).html(data);
	        	  $('#' + id + ' .tabs').tabs('destroy').tabs({cache: true});
	          },
	          error: function(xhr, status, err) { 
	          },
	          complete: function(xhr, status) { 
	          }
	       });
		return sOut;
	}

} );

function cmp2image(val) {
		if (val.indexOf("/conformer")>=0) {
			cmpURI = val.substring(0,val.indexOf("/conformer"));
		}								
		//if ((opentox["model_uri"]==null) || (opentox["model_uri"] == undefined)) {
			cmpURI = cmpURI + "?media=image/png";
		//} else {
		//		cmpURI = opentox["model_uri"] + "?dataset_uri=" + cmpURI + "&media=image/png";
		//}
		return '<a href="'+val+'" title="'+cmpURI+'"><img border="0" src="'+cmpURI+'&w=150&h=150"></a><div>Test</div>';
}

function renderEndpoint(code,name) {
	return "<span title='"+ name +"'>" + code + "</span>";
}
</script>