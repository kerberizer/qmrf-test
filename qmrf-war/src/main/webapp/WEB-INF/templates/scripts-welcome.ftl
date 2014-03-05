<script type="text/javascript">
	$(document).ready( function () {
		$('a.selectable').mouseover(function () { $(this).addClass('hovered');    } );
		$('a.selectable').mouseout(function  () { $(this).removeClass('hovered'); } );

		try {
			var uri = "${qmrf_root}/channel?uri=" + encodeURIComponent("${queryService}/admin/stats/dataset?media=text/plain");
			$('span#valueDatasets').load(uri);
			uri = "${qmrf_root}/channel?uri=" + encodeURIComponent("${queryService}/admin/stats/structures?media=text/plain" );
			$('span#valueStructures').load(uri);
		} catch (e) {
			
		}

	});

</script>