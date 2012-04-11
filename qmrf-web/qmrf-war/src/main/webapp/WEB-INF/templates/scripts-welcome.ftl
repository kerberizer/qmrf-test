<script type="text/javascript">
	$(document).ready( function () {
		$('a.selectable').mouseover(function () { $(this).addClass('hovered');    } );
		$('a.selectable').mouseout(function  () { $(this).removeClass('hovered'); } );
<#-- XXX These are commented out until the cross-domain issue is solved.
		$('span#valueDatasets').load(  'http://ambit.uni-plovdiv.bg:8080/qmrfdata/admin/stats/dataset'   );
		$('span#valueStructures').load('http://ambit.uni-plovdiv.bg:8080/qmrfdata/admin/stats/structures');
-->
<#-- XXX Remove these two lines when the above issue is fixed. -->
		$('span#valueDatasets').replaceWith('47');
		$('span#valueStructures').replaceWith('12049');
	});
</script>