<#include "/html.ftl" >
<head>
<#include "/head.ftl" >

</head>
<body>

	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div class='ui-widget ' style='margin-top: 20px; padding: 0 .7em;'>
		<div class='ui-widget-header ui-corner-top'><p>QMRF password reset</p></div>
	    <div class='ui-widget-content ui-corner-bottom'>					
			<p>&nbsp</p>
			<p>
			<span class="ui-icon ui-icon-info" style="display:inline-block"></span>
			The user name or email you provided do not match the QMRF Database records.
			</p>
			<p>&nbsp</p>
			<p>
			<a href ='${qmrf_root}/forgotten'>Try resetting the password again</a>
			</p>	
		</div>
		
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>

</html>

