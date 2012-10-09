<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<#include "/protocols_head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<p class='w_p'>
		<div>
		<table id='protocols'  cellpadding='0' border='0' width='100%' cellspacing='0'>
		<thead>
		<th ></th>		
		<th class='contentTable'>QMRF Number</th>
		<th class='contentTable qmrfTitle'>Title</th>
		<th class='contentTable'>Endpoint</th>
		<th class='contentTable qmrfDate'>Last updated</th>
		<th class='contentTable qmrfDownloadLinks'>Download</th>
		<th class='contentTable qmrfOwner' id='ownerHeader'></th>
		<th class='contentTable' id='manageHeader'></th>
		</thead>
		<tbody></tbody>
		</table>
		</div>
		   		</p>
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>