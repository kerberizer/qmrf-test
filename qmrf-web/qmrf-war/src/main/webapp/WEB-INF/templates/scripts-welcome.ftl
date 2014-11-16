<script type="text/javascript">
	$(document).ready( function () {
		$('a.selectable').mouseover(function () { $(this).addClass('hovered');    } );
		$('a.selectable').mouseout(function  () { $(this).removeClass('hovered'); } );


		try {
			var uri = "${qmrf_root}/proxy?uri=" + encodeURIComponent("${queryService}/admin/stats/dataset?media=application/json");
		      $.ajax({
		          dataType: "json",
		          url: uri,
		          success: function(data, status, xhr) {
		        	  $('span#valueDatasets').text(data.facet[0].count);
		          },
		          error: function(xhr, status, err) {
		          },
		          complete: function(xhr, status) {
		          }
		       });			
		} catch (e) {
			$('span#valueDatasets').text("");
		}
		try {
			uri = "${qmrf_root}/proxy?uri=" + encodeURIComponent("${queryService}/admin/stats/structures?media=application/json" );
		      $.ajax({
		          dataType: "json",
		          url: uri,
		          success: function(data, status, xhr) {
		        	  $('span#valueStructures').text(data.facet[0].count);
		          },
		          error: function(xhr, status, err) {
		          },
		          complete: function(xhr, status) {
		          }
		       });				
		} catch (e) {
			$('span#valueStructures').text("");
		}

	});

</script>