<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<#include "/users_head.ftl" >
<script type='text/javascript'>
$(document).ready(function() {
	var oTable = defineUsersTable("${qmrf_root}","${qmrf_request_json}");
});
</script>
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<p class='w_p'>
		<div>
		
		<table id='users' cellpadding='0' border='0' width='100%' cellspacing='0' style="margin:0;padding:0;" >
		<thead>	
		<tr>
		<th>Name</th>
		<th>Affiliation</th>
		<th>e-mail</th>
		<th>Keywords</th>
		<th>Reviewer</th>
		</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
		
		<!-- Download links for the entire list -->
		<div style='float:right; width:100%; align:center; margin:20px 0 0 0;'>
		<p>Download as&nbsp;
		<a href="${qmrf_request_csv}" id="downloadcsv"><img id="downloadimg" src="/qmrf/images/excel.png" alt="text/csv" title="Download as MS Excel" border="0"></a>						
		</p></div>
		<!-- End download links -->
		</div>
		</p>
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<div id='toTop'>
&Delta;<br>&Delta;<br>&Delta;
</div>
<#include "/scripts-welcome.ftl">
</body>
</html>