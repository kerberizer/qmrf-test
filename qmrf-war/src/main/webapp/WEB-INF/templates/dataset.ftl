<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<!--
<script type='text/javascript' src='/qmrf/scripts/jdataset.js'></script>
-->
<script type='text/javascript' src='/qmrf/scripts/jcompound.js'></script>
<script type='text/javascript' src='/qmrf/scripts/qmrf_structures.js'></script>
<script type='text/javascript' src='/qmrf/scripts/qmrf_documents.js'></script>
<script>$(function() {$( ".tabs" ).tabs({cache: true});});</script>

<script type='text/javascript'>
$(document).ready(function() {
			
			$.ajax({
			         dataType: "json",
			         url: "${qmrf_request_json}",
			         success: function(data, status, xhr) {
			        	$.each(data,function(index, entry) {
			        		try {
			        			var name = entry[0].visibleid;
			        			if (entry[0].type == "data_validation") name += " Validation";
			        			else if (entry[0].type == "data_training") name += " Training";
			        			name += " dataset";
			        			$("span#datasetname").html(name);			        			
			        		} catch (err) {}
			        		try {
			        			var oTable = defineStructuresTable("${qmrf_root}",entry[0].dataset+"?media=application/json","${queryService}",false,true);
			        			<!-- Details panel -->	
			        			$('#structures tbody td .zoomstruc').live(
			        					'click',
			        					function() {
			        						var nTr = $(this).parents('tr')[0];
			        						if (oTable.fnIsOpen(nTr)) {
			        							this.innerHTML  = "Show QMRF";
			        							this.title='Click to show QMRF documents';
			        							oTable.fnClose(nTr);
			        						} else {
			        						    this.innerHTML  = "Hide QMRF";
			        							this.title='Click to close QMRF documents list';
			        							var id = 'v'+getID();
			        							oTable.fnOpen(nTr, fnStructureQMRFList(oTable,nTr,id,"${qmrf_root}"),	'details');
			        							
			        							var obj = oTable.fnGetData(nTr);
			        							var uri = encodeURIComponent(obj["compound"]["URI"]);
			        							var qmrf_query = "${qmrf_root}/protocol?structure=" + uri + "&media=application%2Fjson";
			        							var id = '#' + id + '_qmrf';
			        							var pTable = defineQMRFDocumentsTable("${qmrf_root}",qmrf_query,false,id,false,false);

			        													       
			        						}
			        				});			        			
			        		} catch (err) {}
			        	});
			         },
			         error: function(xhr, status, err) {
			        	 console.log(err);
			         },
			         complete: function(xhr, status) { }
			});	  	


} );
</script>

</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<p class='w_p'>
		
		<div>
		<table class='compoundtable' id='structures'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<tr>
		<th></th>
		<th>Structure</th>
		<th>Name</th>
		<th>CAS</th>
		<th>QMRF documents</th>
		<th>Similarity</th>
		<th><span id="datasetname">Dataset</span></th>
		<th>SMILES</th>
		<th>InChI</th>
		<th>InChI Key</th>
		</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
		</div>		
		</p>
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<div id='toTop'>
&Delta;<br>&Delta;<br>&Delta;
</div>
<#include "/scripts-welcome.ftl">
</body>
</html>