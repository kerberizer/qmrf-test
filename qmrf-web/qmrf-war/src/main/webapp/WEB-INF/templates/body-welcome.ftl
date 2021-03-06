<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/w_menu.ftl">
	<div class='w_content'>
		<p class='w_p_header'>Welcome to the JRC QSAR Model Database</p>
		<p class='w_p'>
			In the regulatory assessment of chemicals (e.g. under REACH), <b>QSAR models</b> are playing an increasingly important role in predicting properties for hazard and risk assessment. This implies both a need to be able to identify relevant QSARs and to use them to derive estimates and/or have access to their precalculated estimates. To help meet these needs, we are developing a database of QSAR models (i.e. an inventory of information on the models). The JRC QSAR Model Database is freely accessible through this web site.
		</p>
		<p class='w_p'>
			The <b>QSAR Model Reporting Format (QMRF)</b> is a harmonised template for summarising and reporting key information on QSAR models, including the results of any validation studies. The information is structured according to the OECD QSAR validation principles. 
		</p>
		<p class='w_p'>
			The <b>QSAR Prediction Reporting Format (QPRF)</b> is a harmonised template for summarising and reporting substance-specific predictions generated by QSAR models.
		</p>
		<p class='w_p'>
			All substances, available in the QMRF Database, can be searched by exact or similar structure, or by a substructure.
		</p>
    	<p class='w_p_send'>Please send us your models to have them included in the database:
    		<a class='email' href='mailto:${qmrf_email}'>${qmrf_email}</a>.	
    	</p>
    	<p class='w_p_em'>
 			Note: Registration is only necessary if you wish to be informed about news, issues and updates. Once registered and logged in, you will also be able to save your searches for future use and subscribe for alerts.
    	</p>
	</div> <#-- w_content -->
	
	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>
