<script type="text/javascript">
$(document).ready(function() {
	var oTable = $('#protocols').dataTable( {
		"sAjaxDataProp" : "qmrf",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": true,
		"sAjaxSource": "${qmrf_request}",
		"aoColumns": [
				{ //0
					"aTargets": [ 0 ],	
					"sClass" : "center",
					"bSortable" : false,
					"mDataProp" : null,
					sWidth : "16px",
					"bUseRendered" : "true",
					"fnRender" : function(o,val) {
							return "<img src='/qmrf/images/zoom_in.png' alt='zoom in' title='Click to show compound details'>";
					},
				},			              
				{ "mDataProp": "identifier" , "asSorting": [ "asc", "desc" ], "aTargets": [ 1 ],	
				  sWidth : "20%",
 			      "fnRender": function ( o, val ) {
          				return "<a href='"+o.aData["uri"] + "'>" + o.aData["identifier"] + "</a>";
        			}
				},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ], "aTargets": [ 2 ]
				},
				{ "mDataProp": "endpoint.code" , 
				  "asSorting": [ "asc", "desc" ], 
				  "bSearchable" : true, 
				  "aTargets": [ 3 ],
				  sWidth : "10%"
			    },
				{ "mDataProp": "updated", 
				  "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 4 ],
				  sWidth : "10%"
				},
				{ "mDataProp": "owner.username" , 
				  "asSorting": [ "asc", "desc" ], 
				  "aTargets": [ 5 ],
				  sWidth : "10%" 
			   }
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
	            "sProcessing": "<img src='/qmrf/images/progress.gif' border='0'>"
	    }
	
	} );
	
	$('#protocols tbody td img').live(
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
					var id = 'values'+getID();
					oTable.fnOpen(nTr, fnFormatDetails(nTr,id),	'details');
											       
				}
		});
	
	function getID() {
		   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}	
	
	/* Formating function for row details */
	function fnFormatDetails(nTr, id) {
		var qmrfDocument = oTable.fnGetData(nTr);
		var sOut = "<span id='" + id + "'></span>";

	      $.ajax({
	          dataType: "html",
	          url: qmrfDocument.uri + "/chapters?headless=true&media=text%2Fhtml",
	          success: function(data, status, xhr) {
	        	  $('span#' + id ).replaceWith(data);
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