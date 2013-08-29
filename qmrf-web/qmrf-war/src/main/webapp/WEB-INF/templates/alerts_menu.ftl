			<#assign freq_hint='Optional, if no alert frequency is selected, retrieve your saved search from the profile page.'>
				<#assign alert_hint='Save your search and configure the frequency of e-mail update alerts.'>
				
				<li><a class='selectable' title="${username}, ${alert_hint}" href='#' onClick="toggleDiv('saveSearch'); return false;">Save this search</a></li>
	
				<div class="ui-widget-content ui-corner-all" style='display: none;width:180px;' id='saveSearch'>
				<p>
				<form action='/qmrf/myaccount/alert' method='POST'>
					${alert_hint}
					<input type='hidden' name='name' value='/protocol'/>
					<input type='hidden' name='query' value="${qmrf_query}"/>
					<input type='hidden' name='qformat' value='FREETEXT'/>
					<input type='hidden' name='username' value="${username}"/>
					<br>
					<label for='rfrequency' title="${freq_hint}">Frequency of e-mail alert</label>
					<select name='rfrequency'>;
					  <option value="monthly">Monthly</option>
					  <option value="weekly">Weekly</option>
					  <option value="daily">Daily</option>				
					  <option value="" title="${freq_hint}">Never</option>
				    </select>
					<input type='submit' title="${alert_hint} ${freq_hint}" value='Save'/>
				</form>
				</p>
				</div>