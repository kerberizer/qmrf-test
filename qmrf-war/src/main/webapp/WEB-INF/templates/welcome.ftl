<#include "/head.ftl" >
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<div class='w_left'>
		<ul id='navmenu'>
		<li><a class='selectable enter_qmrf' title='Click here to enter the QMRF inventory' href='./protocol'>
			<img class='w_logo_inventory' src='./images/logo_menu.png'>
			Access
		</a></li>
		<li><a class='selectable' title='Click here to download the QMRF Editor application' href='http://qmrf.sf.net/editor'>Get QMRF Editor</a></li>
		<li><a class='selectable' title='Click here to access the reviewers template' href='http://qmrf.sf.net/template'>Reviewers Template</a></li>
		<li><a class='selectable' title='Click here to read about the OECD Principles' href='http://ihcp.jrc.ec.europa.eu/our_labs/computational_toxicology/background/oecd-principles'>OECD Principles</a></li>
		<li><a class='selectable' title='Click here to access the QMRF user manual' href='http://qmrf.sf.net/manual'>User Manual</a></li>
		<li><a class='selectable' title='Click here to read the FAQ' href='http://qmrf.sf.net/faq'>FAQ</a></li>
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

	</div> <#-- w_menu -->
	<div class='w_content'>
		<p class='w_p_header'>Welcome to the QMRF Inventory</p>
		<p class='w_p'>
			In the regulatory assessment of chemicals (e.g. under REACH), <b>(Q)SAR models</b> are playing an increasingly important role in predicting properties for hazard and risk assessment. This implies both a need to be able to identify relevant (Q)SARs and to use them to derive estimates and/or have access to their precalculated estimates. To help meet these needs, we are developing a database of (Q)SAR models (i.e. an inventory of information on the models). The JRC QSAR Model Database is freely accessible through this web site.
		</p>
		<p class='w_p'>
			The <b>(Q)SAR Model Reporting Format (QMRF)</b> is a harmonised template for summarising and reporting key information on (Q)SAR models, including the results of any validation studies. The information is structured according to the OECD (Q)SAR validation principles. 
		</p>
		<p class='w_p'>
			The <b>(Q)SAR Prediction Reporting Format (QPRF)</b> is a harmonised template for summarising and reporting substance-specific predictions generated by (Q)SAR models.
		</p>
		<p class='w_p'>
			All <b>substances</b>, available in the QMRF Database can be searched by exact or similar structure, or by a substructure.
		</p>
    	<p class='w_p_send'>Please send us your models to have them included in the database:
    		<a class='email' href='mailto:JRC-IHCP-COMPUTOX@ec.europa.eu'>JRC-IHCP-COMPUTOX@ec.europa.eu</a>	
    	</p>
	</div> <#-- w_content -->
	
	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
<#include "/footer.ftl">
</body>
</html>
