<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='${qmrf_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='${qmrf_root}/scripts/myprofile.js'></script>
<script type='text/javascript' src='${qmrf_root}/scripts/jopentox.js'></script>
<script type='text/javascript'>

$().ready(function() {
	// validate the reset form when it is submitted
	$("#pwdresetForm").validate({
		rules : {
			'username': {
				required : true,
				minlength: 3,
				maxlength: 16
			},		
			'email': {
				required : true,
				email: true
			}
		},
		messages : {
			'username'  : {
				required: " Please enter user name"
			},
			'email'     : {
				required: " Please provide valid e-mail",
				email: " Please provide valid e-mail"
			}
		}
	});
});
	
</script>

</head>

<body>
	<div class='w_wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div class='ui-widget ' style='margin-top: 20px; padding: 0 .7em;'>
		<div class='ui-widget-header ui-corner-top'><p>QMRF password reset</p></div>
	    <div class='ui-widget-content ui-corner-bottom'>					
			<form action="${qmrf_root}/forgotten" id="pwdresetForm"  method="POST" >		
	    	<table width='80%%' style='margin-left:20px;'>
			<tbody>
			<tr>
				<th>
					<label class='three columns alpha' for="username">User name <em>*</em></label>
				</th>
			</tr>
			<tr>
				<th>
					<input class='three columns alpha half-bottom' type="text" size='40' name='username' id='username' value=''/>
				</th>
			</tr>
			<tr >
				<th>
					<label class='three columns alpha'  for="email">e-mail <em>*</em></label>
				</th>
			</tr>			
			<tr >
				<th>
					<input class='eight columns alpha half-bottom' type="text"size='40' name='email' id='email' value=''/>
				</th>
			</tr>
			<tr>
				<th>
					<input class='three columns alpha' id='register' name='register' type='submit' class='submit' value='Reset'>
				</th>
			</tr>
			</tbody>
			</table>
			</form>		
		</div>
		
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>
</html>