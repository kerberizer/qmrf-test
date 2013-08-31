function defineQMRFDocumentsTable(root,url,isAdmin,tableSelector,jQ,zoomEnabled) {
	var oTable = $(tableSelector).dataTable( {
		"sAjaxDataProp" : "qmrf",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": false, //if true, controlling the last two columns visibility behaves weird!
		"sAjaxSource": url,
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
		        "success": fnCallback

		      } );
		},

		"aoColumnDefs": [
				{ //0
					"aTargets": [ 0 ],	
					"sClass" : "center",
					"bSortable" : false,
					"mDataProp" : null,
					sWidth : "32px",
					"bUseRendered" : "true",
					"fnRender" : function(o,val) {
						return zoomEnabled?"<span class='zoom'><img border='0' src='/qmrf/images/zoom_in.png' alt='zoom in' title='Click to show compound details'></span>":"";
					}
				},			              
				{ "mDataProp": "visibleid" , "asSorting": [ "asc", "desc" ], "aTargets": [ 1 ], bUseRendered:false,	
				  "bSortable" : true,
				  sWidth : "15%",
 			      "fnRender": function ( o, val ) {
 			    	    return "<a name='toploc_"+o.aData["identifier"]+"'></a>" +
 			    	    	   "<a href='"+o.aData["uri"] + "'>" + o.aData["visibleid"] + "</a>";
        			}
				},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ], "aTargets": [ 2 ]
				},
				{ "mDataProp": "endpoint.name" , "bUseRendered" : "false",
				  "asSorting": [ "asc", "desc" ], 
				  "bSearchable" : true, 
				  "aTargets": [ 3 ],
			      "fnRender": function ( o, val ) {
			      		var e = (o.aData["endpoint"]["code"]==null?"": o.aData["endpoint"]["code"]) + val;
        				return renderEndpoint(e.length>42?e.substring(0,42):e,e);
      			  },
				  sWidth : "15%"
			    },
				{ "mDataProp": "updated", 
				  "asSorting": [ "asc", "desc" ],
				  "aTargets": [ 4 ],
				  sWidth : "10%"
				},
				{ "mDataProp": null, bSortable: false,"bUseRendered" : "true",
					"aTargets": [ 5 ],
				    "fnRender": function ( o, val ) {
			    	 var sOut = 
			    	 "<span>"+
			    	 "<a href='"+ o.aData["uri"] + "/document?media=application%2Fpdf'><img border='0' src='/qmrf/images/pdf.png' title='Download as PDF'></a>&nbsp;"+
			    	 "<a href='"+ o.aData["uri"] + "/document?media=application%2Frtf'><img border='0' src='/qmrf/images/word.png' title='Download as Rich Text Format (RTF)'></a>&nbsp;"+
			    	 "<a href='"+ o.aData["uri"] + "/document?media=application%2Fxml'><img border='0' src='/qmrf/images/xml.png' title='Download as QMRF XML'></a>&nbsp;"+
			    	 "</span>";
			    	 return sOut;
	        	   },
	        	   sWidth : "70px" 
				},
				{ "mDataProp": null,"bUseRendered" : "true",
				  sWidth : "5%",
				  "aTargets": [ 6 ],
				  "fnRender": function ( o, val ) {
					     if (isAdmin) {
					    	 var printName = (o.aData["owner"]["firstname"]=="") && (o.aData["owner"]["lastname"] == "")?
					    			 (o.aData["owner"]["username"]==""?"Owner":o.aData["owner"]["username"]):
					    			 o.aData["owner"]["firstname"] + " " + o.aData["owner"]["lastname"];	 
							 return "<a href='" + o.aData["owner"]["uri"] + "' target='user'>" + printName+  "</a>";
					     } else return "";
				  },
				  "bVisible" : isAdmin
			    },
				{ "mDataProp": null,"bUseRendered" : "true",
			      sWidth : "60px", 
				  "aTargets": [ 7 ],
				  "fnRender": function ( o, val ) {
					 return isAdmin?
							 publishString(o.aData["identifier"],o.aData["published"]) + 
							 updateString(o.aData["identifier"],o.aData["published"]) + 
							 deleteString(o.aData["identifier"]) 
							 :"";
				  },
				  "bVisible" : isAdmin
				}
			],
		"bJQueryUI" : jQ,
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
	            "sLoadingRecords": "No QMRF documents found.",
	            "sEmptyTable" : "No QMRF documents found.",
	            "sInfo": "Showing _TOTAL_ QMRF documents (_START_ to _END_)",
            "sLengthMenu": 'Display <select>' +
              '<option value="10">10</option>' +
              '<option value="20">20</option>' +
              '<option value="50">50</option>' +
              '<option value="100">100</option>' +
              '<option value="-1">all</option>' +
              '</select> QMRF documents.'	   				            
		}	
	} );
	return oTable;
}

function publishString(qmrf_id,isPublished) {
	if (isPublished) return "<span style='width:16px;'>&nbsp;</span>";
	var sOut = "";
	sOut = "<a href='/qmrf/editor/" + qmrf_id + "?mode=publish'>";
	sOut += "<img border='0' src='/qmrf/images/script_add.png'  title='Publish this document' alt='Publish'>";
	sOut += "</a> ";
	return sOut;
}
function deleteString(qmrf_id) {
	var sOut = "";
	sOut = "<a href='/qmrf/editor/" + qmrf_id + "?mode=delete'>";
	sOut += "<img border='0' src='/qmrf/images/script_delete.png'  title='Delete this document' alt='Delete'>";
	sOut += "</a> ";
	return sOut;
}
function updateString(qmrf_id,isPublished) {
	var sOut = "";
	if (isPublished) {
		sOut = "<a href='/qmrf/editor/" + qmrf_id + "?mode=newversion'>";
		sOut += "<img border='0'  src='/qmrf/images/script_edit.png'  title='Create new version of this document' alt='New version'>";
		sOut += "</a> ";
	} else {
		sOut = "<a href='/qmrf/editor/" + qmrf_id + "?mode=update'>";
		sOut += "<img border='0' src='/qmrf/images/script_edit.png'  title='Update this document' alt='Update'>";
		sOut += "</a> ";			
	}
	return sOut;
}	

function renderEndpoint(code,name) {
	return "<span title='"+ name +"'>" + code + "</span>";
}