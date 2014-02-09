<script type="text/javascript">
	function toggleDiv(divId) {
		$('#'+divId).toggle();
		if ($('#'+divId+'_toggler').hasClass('togglerPlus')) {
			$('#'+divId+'_toggler').removeClass('togglerPlus');
			$('#'+divId+'_toggler').addClass('togglerMinus');
			} else if ($('#'+divId+'_toggler').hasClass('togglerMinus')) {
			$('#'+divId+'_toggler').removeClass('togglerMinus');
			$('#'+divId+'_toggler').addClass('togglerPlus');
		}
	}	
</script>
	<div class='w_left'>
		<div id="menu">
	
	 <!-- Front page -->
			<ul id='navmenu'>
			<li><a class='selectable enter_qmrf' title='Click here to enter the QMRF Database' href='/qmrf/protocol?pagesize=100'>
				<img class='w_logo_qmrfdb' src='/qmrf/images/logo_menu.png'>
				Access
			</a></li>
			<li><a class='selectable external' title='Click here to download the QMRF Editor application' href="${qmrf_editor}">Get QMRF Editor</a></li>
			<li><a class='selectable external' title='Click here to access the reviewers template' href="${qmrf_template}">Reviewers Template</a></li>
			<li><a class='selectable ' title='Click here to read about the OECD Principles' href="${qmrf_oecd}">OECD Principles</a></li>
			<li><a class='selectable external' title='Click here to access the QMRF user manual' href="${qmrf_manual}">User Manual</a></li>
			<li><a class='selectable external' title='Click here to read the FAQ' href="${qmrf_faq}">FAQ</a></li>
			<li><a class='selectable external' title='Click here to access the general help pages' href='http://qmrf.sf.net/'>User support</a></li>
	
		
		</ul>
	
			<div id='stats' class="w_stats">
			<table class='w_table_stats'>
				<tr class='w_tr'>
					<th class='w_th' colspan='2'>Statistics</th>
				</tr>
				<tr class='w_tr'>
					<td class='w_td_param'>
						Datasets
					</td>
					<td class='w_td_value'>
						<span id='valueDatasets'></span>
					</td>
				</tr>
				<tr class='w_tr'>
					<td class='w_td_param'>
						Chemical structures
					</td>
					<td class='w_td_value'>
	                	<span id='valueStructures'></span>
					</td>
				</tr>
			</table>
			</div>
	
		</div> <#-- menu -->
	</div> <#-- w_menu -->
