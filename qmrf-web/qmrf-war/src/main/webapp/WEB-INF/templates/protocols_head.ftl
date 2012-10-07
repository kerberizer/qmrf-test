<script type="text/javascript">
$(document).ready(function() {
	$('#protocols').dataTable( {
		"sAjaxDataProp" : "qmrf",
		"bProcessing": true,
		"bServerSide": false,
		"bStateSave": true,
		"sAjaxSource": "${qmrf_request}",
		"aoColumns": [
				{ "mDataProp": "identifier" , "asSorting": [ "asc", "desc" ],
 			      "fnRender": function ( o, val ) {
          				return "<a href='"+o.aData["uri"] + "'>" + o.aData["identifier"] + "</a>";
        			}
				},
				{ "mDataProp": "title" , "asSorting": [ "asc", "desc" ]
				},
				{ "mDataProp": "endpoint.code" , "asSorting": [ "asc", "desc" ], "bSearchable" : true	},
				{ "mDataProp": "updated", "asSorting": [ "asc", "desc" ] },
				{ "mDataProp": "owner.username" , "asSorting": [ "asc", "desc" ] }
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
	            "sProcessing": "<img src='images/progress.gif' border='0'>"
	    }
	
	} );
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