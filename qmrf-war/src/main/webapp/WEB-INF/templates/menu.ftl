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
								
			<li><a class='selectable ${s["/protocol"]!""}' title='Search all published QMRF documents' href='/qmrf/protocol'>QMRF document search</a></li>
			<li><a class='selectable ${s["/chemical"]!""}' title='Search chemical structures by identifiers, similarity or substructure' href='/qmrf/chemical'>Structures search</a></li>
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
				<#assign freq_hint='Optional, if no alert frequency is selected, retrieve your saved search from the profile page.'>
				<#assign alert_hint='Save your search and configure the frequency of e-mail update alerts.'>
				
				<li><a class='selectable' title='${username}, ${alert_hint}' href='#' onClick="toggleDiv('saveSearch'); return false;">Save this search</a></li>
	
				<div class="ui-widget-content ui-corner-all" style='display: none;width:180px;' id='saveSearch'>
				<p>
				<form action='/qmrf/myaccount/alert' method='POST'>
					${alert_hint}
					<input type='hidden' name='name' value='/protocol'/>
					<input type='hidden' name='query' value='${qmrf_query}'/>
					<input type='hidden' name='qformat' value='FREETEXT'/>
					<input type='hidden' name='username' value='${username}'/>
					<br>
					<label for='rfrequency' title='${freq_hint}'>Frequency of e-mail alert</label>
					<select name='rfrequency'>;
					  <option value="monthly">Monthly</option>
					  <option value="weekly">Weekly</option>
					  <option value="daily">Daily</option>				
					  <option value="" title='${freq_hint}'>Never</option>
				    </select>
					<input type='submit' title='${alert_hint} ${freq_hint}' value='Save'/>
				</form>
				</p>
				</div>
			</#if></#if></#if>
			<!-- end saved search -->	
			
			<!-- search menu -->
			<#if s["/protocol"]??>
				
				<#assign search_title = "Search all published QMRF documents">
				<#assign s = { query.option!"default":"checked"} >
				
				<div class='search ui-widget'>
				<p title=''>${search_title}</p>
				<form method='GET' action='/qmrf/protocol'>
				<table width='200px'>
				<tr><td colspan='2'><input type='text' name='search' size='20' value='${query.search!""}' tabindex='0' title='Enter search query'></td></tr>
				<tr><td colspan='2'><input ${s["title"]!""} tabindex='1' type='radio' value='title' name='option' title='Title' size='20'>Title</td></tr>
				<tr><td colspan='2'><input ${s["text"]!""} tabindex='1' type='radio' value='text' name='option' title='Free text search' size='20'>Free text</td></tr>
				<tr><td><input ${s["endpoint"]!""} type='radio' tabindex='2' name='option' value='endpoint' title='Search by endpoint'>Endpoint</td>
				<tr><td colspan='2'><input ${s["author"]!""} tabindex='3' type='radio' value='author' name='option' title='Search by author' size='20'>Author</td></tr>
				<tr><td><input ${s["qmrfnumber"]!""} type='radio' tabindex='4' name='option' value='qmrfnumber' title='Search by QMRF number'>QMRF number</td>
				<tr><td>Number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value=''></td></tr>
				<input type='hidden' name='structure' value='${query.structure!""}'>
				<tr><td colspan='2' align='center'><input type='submit' id='submit' tabindex='4' value='Search'/></td></tr>
				</table>
				</form> 
				&nbsp;
				<div class='structureright'>
					<#if structure??>
						<img border='0' title='Showing QMRF documents for this chemical' width='150' height='150' src='${structure}?media=image%2Fpng&w=150&h=150'>
						<br>Showing QMRF documents
					</#if>
				</div>
				</div>
			</#if>
			
		<#else>  <!-- Front page -->
			<ul id='navmenu'>
			<li><a class='selectable enter_qmrf' title='Click here to enter the QMRF inventory' href='/qmrf/protocol'>
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