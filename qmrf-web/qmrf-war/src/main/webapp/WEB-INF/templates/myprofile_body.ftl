<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<#include "/users_head.ftl" >

<style type="text/css">
label { width: 10em; float: left; }
label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }
p { clear: both; }
.submit { margin-left: 12em; }
em { font-weight: bold; padding-right: 1em; vertical-align: top; }
</style>

<script type='text/javascript'>
$(document).ready(function() {
	$('#form_removeuser').validate({
	    submitHandler: function(form) {
	        if (confirm('Please confirm user removal!')) {
	            form.submit();
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
			
			<form action="/qmrf/myaccount/?method=put" id="form_myaccount" method="POST" >					
			<#if myprofile>
				<#assign ro=''>
			<#else>
				<#if admin>
					<#assign ro=''>
				<#else>
					<#assign ro='readonly'>
				</#if>	
			</#if>

			<h2><a href=''><span id='useruri'></span></a></h2>
			<p><label for="username">User name</label><b><span id='username'></span></b></p>
			<p><label for="title">Title</label><input type="text" ${ro} size='40' name='title' id='title' value=''></p>
			<p><label for="firstname">First name</label><input type="text" ${ro} size='40' name='firstname' id='firstname' value=''/><em>*</em></p>
			<p><label for="lastname">Last name</label><input type="text" ${ro} size='40' name='lastname' id='lastname' value=''/><em>*</em></p>
			<span id='organisations'></span>
			<p><label for="email">e-mail</label><input type="text" ${ro} size='40' name='email' id='email' value=''><em>*</em></p>
			
			<p><label for="homepage">WWW</label><input type="text" ${ro} size='40' name='homepage' id='homepage' value=''></p>
			<p><label for="keywords">Keywords</label><input type="text" ${ro} size='40' name='keywords' id='keywords' value=''></p>
			<p><label for="reviewer">Available as a reviewer</label><input type="checkbox" ${ro} name='reviewer' id='reviewer' value=''></p>
			<#if myprofile>
				<p><label for="update">&nbsp;</label><input id='update' name='update' type='submit' value='Update'></p>
			<#else>
				<#if admin>
					<p><label for="update">&nbsp;</label><input id='update' name='update' type='submit' value='Update'></p>
				</#if>	
			</#if>
			</form>
			
			<#if myprofile>
				<hr>
				<p><a href="/qmrf/myaccount/reset">Change password</a></p>			
			<#else>
				<#if admin>
					<hr>
					<form action="" id="form_removeuser" method="POST" >
					<p><input type="hidden" name='user' id='user_to_delete' value=''></p>					
					<p><label for="delete">&nbsp;</label><input id='delete' name='delete' type='submit' value='Remove this user'></p>
					</form>
				</#if>	
			</#if>
			
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