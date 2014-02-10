<meta property="title" content="(Q)SAR Model Reporting Format Database">
<meta name="robots" content="index,follow"><META NAME="GOOGLEBOT" CONTENT="index,FOLLOW">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="/qmrf/style/ambit.css" rel="stylesheet" type="text/css">
<!--[if IE 7]><link rel="stylesheet" type="text/css" media="all" href="$/qmrf/style/ambit-msie7.css"><![endif]-->
<link href="/qmrf/style/jquery-ui-1.8.18.custom.css" rel="stylesheet" type="text/css">
<link href="/qmrf/style/jquery.dataTables.css" rel="stylesheet" type="text/css">
<link href="/qmrf/images/favicon.ico" rel="shortcut icon" type="image/ico">
<script type='text/javascript' src='/qmrf/jquery/jquery-1.7.2.min.js'></script>
<script type='text/javascript' src='/qmrf/jquery/jquery-ui-1.8.18.custom.min.js'></script>
<script type='text/javascript' charset='utf8' src='/qmrf/jquery/jquery.dataTables-1.9.0.min.js'></script>
<script type='text/javascript' src='/qmrf/jquery/jquery.cookies.2.2.0.min.js'></script>
<title>(Q)SAR Model Reporting Format Database</title>

<script type='text/javascript'>
	$(document).ready(function() {
		$(".external").click(function(event) {
			var msg = "You are leaving the JRC QSAR Model database website. The European Commission accepts no responsibility or liability whatsoever with regard to the information on the site you are moving to."; 		
		   <#if qmrf_disclaimer??>
		   		msg = "${qmrf_disclaimer}";
		   </#if>	
		   var confirmation = confirm(msg);
		   if (!confirmation) {
		     event.preventDefault();
		   }
		 });
	});
 </script>