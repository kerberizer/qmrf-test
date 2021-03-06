<script type='text/javascript'>$(function() {$( ".tabs" ).tabs({cache: true});});</script>

<script type='text/javascript' src='/qmrf/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='/qmrf/scripts/qmrf_documents.js'></script>
<script type='text/javascript' src='/qmrf/scripts/qmrfsearchform.js'></script>
<script type='text/javascript'>

$(document).ready( function () {
	validateQMRFSearchForm();
	$("#submit").button();
});

$(document).ready( function () {
	$('#toTop').click( function () {
		$('html, body').animate({scrollTop: '0'}, 1000);
	});
	$(window).scroll( function () {
		var h = $('#header').height();
		var p = $(window).scrollTop();
		if ( p > (h + 100) ) {
			$('#toTop').stop().animate({left: '-5px'}, 'fast');
		} else if ( p < (h + 50) ) {
			$('#toTop').stop().animate({left: '-30px'}, 'slow');
		}
	});
});

$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	//headers
	var isAdmin = ${editorRole};

	$('#protocols .qmrfOwner').html(function() { return isAdmin?"Owner":""; });
	$('#protocols #manageHeader').html(function() { return isAdmin?"Manage":""; });
	var oTable = defineQMRFDocumentsTable("${qmrf_root}","${qmrf_request_json}",isAdmin,'#protocols',true,true);
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
		sOut = sOut + "<div class='tabs2top'><a href='#toploc_"+qmrfDocument["identifier"]+"'><span class='upArrow'>&Delta;</span>&nbsp;TOP&nbsp;<span class='upArrow'>&Delta;</span></a></div>";
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


</script>