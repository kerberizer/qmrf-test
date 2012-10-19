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
		<!-- Download links for the entire list -->
		<div style='float:right; width:100%; align:center; margin:20px 0 0 0;'>
		<p>Download as&nbsp;
		<a href="${qmrf_request_csv}" id="downloadcsv"><img id="downloadimg" src="/qmrf/images/excel.png" alt="text/csv" title="Download as MS Excel" border="0"/></a>						
		</p></div>
		<!-- End download links -->
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