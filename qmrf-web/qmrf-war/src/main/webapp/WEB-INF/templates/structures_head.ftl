<script type='text/javascript' src='/qmrf/scripts/jcompound.js'></script>
<script type='text/javascript' src='/qmrf/scripts/qmrf_structures.js'></script>
<script type='text/javascript'>
$(document).ready(function() {
	$("#submit").button();
	$.ajaxSetup({cache:false});//while dev
	
	var url = "${qmrf_request_jsonp}";

  	var oTable = defineStructuresTable(url,"${queryService}","${query.option!''}" == 'similarity');
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
					oTable.fnOpen(nTr, fnStructureQMRFList(oTable,nTr,id),	'details');
											       
				}
		});
	
} );
</script>
