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
		
		<#if searchURI??>
			
			<#assign s = {searchURI:"selected"}>

			<ul id='navmenu'>
			<li><a class='selectable' title='Go to the welcome page' href='/qmrf'>
				<img class='logo_home_menu' src='/qmrf/images/logo_menu.png'>
				Home
			</a></li>
								
			<li><a class='selectable ${s["/protocol"]!""}' title='Search all published QMRF documents' href='/qmrf/protocol?pagesize=100'>QMRF document search</a></li>
			<li><a class='selectable ${s["/chemical"]!""}' title='Search chemical structures by identifiers, similarity or substructure' href='/qmrf/chemical?pagesize=100'>Structures search</a></li>
			<li><a class='selectable ${s["/endpoint"]!""}' title='QMRF documents by endpoints' href='/qmrf/endpoint'>Endpoints</a></li>					
				
			<#if username??>
				<li><a class='selectable ${s["/myaccount"]!""}' href='/qmrf/myaccount' title='${username}'s profile and documents.'>My profile</a></li>
			</#if>
			
			<#assign admin = false>
			<#if managerRole??>
				<#if managerRole == 'true'>
					<#assign admin = true>
					<li><a class='selectable ${s["/user"]!""}' title='All registered users.' href='/qmrf/user'>Users</a></li>
					<li><a class='selectable ${s["/organisation"]!""}' title='All registered user affiliations.' href='/qmrf/organisation'>Organisations</a></li>
					<li><a class='selectable ${s["/admin"]!""}' title='System administration' href='/qmrf/admin'>Admin</a></li>
				</#if>					
			</#if>
			<#if editorRole??>
				<#if editorRole == 'true'>
					<#assign admin = true>
					<li><a class='selectable ${s["/editor"]!""}' title='Publishing new documents' href='/qmrf/editor'>New document</a></li>
				</#if>
			</#if>			
			<#if admin>
					<li><a class='selectable ${s["/unpublished"]!""}' title='All unpublished QMRF documents.' href='/qmrf/unpublished'>Unpublished Documents</a></li>
			</#if>
					
			<!-- save search widget only in case of query -->			
			<#if qmrf_query??><#if username??><#if s["/protocol"]??>
				<#include "/alerts_menu.ftl" >
			</#if></#if></#if>
			<!-- end saved search -->	
			
			<!-- search menu -->
			<#if s["/protocol"]??>
				<#include "/protocols_menu.ftl" >
			<#else> <#if s["/chemical"]??>
				<#include "/structures_menu.ftl" >
				</#if>
			</#if>
			
		<#else>  <!-- Front page -->
			<ul id='navmenu'>
			<li><a class='selectable enter_qmrf' title='Click here to enter the QMRF inventory' href='/qmrf/protocol?pagesize=100'>
				<img class='w_logo_inventory' src='/qmrf/images/logo_menu.png'>
				Access
			</a></li>
			<li><a class='selectable' title='Click here to download the QMRF Editor application' href='${qmrf_editor}'>Get QMRF Editor</a></li>
			<li><a class='selectable' title='Click here to access the reviewers template' href='${qmrf_template}'>Reviewers Template</a></li>
			<li><a class='selectable' title='Click here to read about the OECD Principles' href='${qmrf_oecd}'>OECD Principles</a></li>
			<li><a class='selectable' title='Click here to access the QMRF user manual' href='${qmrf_manual}'>User Manual</a></li>
			<li><a class='selectable' title='Click here to read the FAQ' href='${qmrf_faq}'>FAQ</a></li>
			<li><a class='selectable' title='Click here to access the general help pages' href='http://qmrf.sf.net/'>Help</a></li>
	
		
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
	
		</#if>
	</div> <#-- w_menu -->