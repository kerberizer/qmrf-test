<script type='text/javascript' src='/qmrf/scripts/jcompound.js'></script>
<script type='text/javascript' src='/qmrf/scripts/qmrf_structures.js'></script>
<script type='text/javascript' src='/qmrf/scripts/qmrf_documents.js'></script>
<script type='text/javascript'>
$(document).ready(function() {
	$("#submit").button();
	$.ajaxSetup({cache:false});//while dev
	
	var url = "${qmrf_request_jsonp}";

  	var oTable = defineStructuresTable("${qmrf_root}",url,"${queryService}","${query.option!''}" == 'similarity');
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
	
} );
</script>
