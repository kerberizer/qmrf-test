<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='/qmrf/scripts/jendpoints.js'></script>
<script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	
  	var oTable = defineEndpointsTable("${qmrf_request_json}");

    <!-- Details panel -->	
	$('#endpoints tbody td .zoomqmrf img').live(
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
					oTable.fnOpen(nTr, fnEndpointQMRFList(oTable,nTr,id),	'details');
											       
				}
		});
} );
</script>
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div><ul id='hnavlist' >
		<li><a class='pselectable' style='width: 25em;' href='/qmrf/catalog' title='All endpoints'>All endpoints</a></li>
		</div>
		<p class='w_p'>
		<div>
		<table id='endpoints'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<th></th>
		<th>Code</th>
		<th>Name</th>
		<th>QMRF Documents</th>
		</thead>
		<tbody></tbody>
		</table>
		</div>
		   		</p>
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>