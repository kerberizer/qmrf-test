<#include "/html.ftl">
<head>
  <#include "/head.ftl">

</head>
<body>

<body>
	<div id='wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">	

		
		<div class='w_content'>
		<form method='post' action='${qmrf_root}/protected/signin?targetUri=${qmrf_root}/login' autocomplete='off'>
		

		<div class='ui-widget ' style='padding: 0 .7em;'>
		
		<table border='0' width='75%' style='margin:20px;'>		

		<tr><td colspan='3'>
		<div class='ui-widget-header ui-corner-top'><p>Sign In</p></div>
	    <div class='ui-widget-content ui-corner-bottom'>
	    </td></tr>
	    				

		<tr><th width='10%'><label for="username">User name</label></th>
		<td><input type="text" size='40' name='login' id='login' value=''></td>
		<td></td>
		</tr>
		
		<tr><th><label for="title">Password</label></th><td><input type='password' size='40' name='password' value=''></td>
		<td><a href="${qmrf_root}/forgotten" title='Click to request one time password reset'>Forgotten password?</a></td>
		</tr>
		
		<tr>
		<th></th>
		<td colspan='2'><input type="submit" value="Log in"><a href='#' class='chelp loginhelp'></a>
		<input type='hidden' size='40' name='targetURI' value='${qmrf_root}/login'>
		</td>
		</tr>

		</div>		
		
		</table>
		
		</form>
		</div>
		
		</div> <#-- content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">

<div id="readstatement" style="display:none" title="JRC Privacy Statement">
  <p>This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
</div>
</body>
</html>