<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='/qmrf/jme/jme.js'></script>
<#include "/users_head.ftl" >
</head>
<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<p class='w_p'>

		
	    <div class='protocol'>					
		<div class='tabs'>
		<ul>
		<li><a href='#tabs-id'>User Profile</a></li>
		<li><a href='' id='protocoluri'  title='QMRF documents'>QMRF documents</a><span></span></li>
		<li><a href='' id='alerturi' title='QMRF alerts'>Saved searches</a><span></span></li>
		</ul>
		<!-- user details tab -->
			<div id='tabs-id'>
			<span class='summary'>
			<form action='/qmrf/myaccount/?method=put' id='form_myaccount' method='POST' >
			<table width='80%%'>
			<tbody>
			<tr><th colspan='2'><a href=''><h2><span id='useruri'></span></h2></a></th></tr>
			<tr><th colwidth='25%'>User name</th><th align='left' id='username'></th></tr>
			<tr><th>Title</th><td align='left'><input type='text' size='40' name='title' id='title' value=''></td></tr>
			<tr><th>First name</th><td align='left'><input type='text'  size='40' id='firstname' name='firstname' value=''></td></tr>
			<tr><th>Last name</th><td align='left'><input type='text'  size='40' id='lastname' name='lastname' value=''></td></tr>
			<tr><th>Affiliation</th>
			<td align='left' >
			<div>
			<table id='organisations'>
				<thead style="display:none;">
				<th></th>
				</thead>
				<tbody></tbody>
			</table>
			</div>
			</td></tr>				
			<tr><th>e-mail</th><td align='left'><input type='text'  size='40' id='email' name='email' value=''></td></tr>
			<tr><th>WWW</th><td align='left' ><input type='text' id='homepage' name='homepage' size='40' value=''></td></tr>
			<tr><th>keywords</th><td align='left' ><input type='text' id='keywords' name='keywords' size='40' value=''></td></tr>
			<tr><th>Available as a reviewer</th><td align='left' ><input id='reviewer' name='reviewer' type='checkbox'></td></tr>
		
			<tr><th></th><td align='left' ><input id='update' name='update' type='submit' value='Update'></td></tr>
			</tbody>								
			</table>
			
			</form>
			</span></div>
		<!-- protocols -->
			<div id='QMRF_documents'></div>
		<!-- alerts -->
			<div id='QMRF_alerts'></div>
		<!-- wrapping up -->
		</div>
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