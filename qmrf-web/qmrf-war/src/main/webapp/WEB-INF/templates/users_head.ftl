<script type='text/javascript' src='/qmrf/scripts/myprofile.js'></script>
<script type='text/javascript' src='/qmrf/jquery/jquery.validate.min.js'></script>
<script type='text/javascript'>
$(document).ready(function() {

	<#assign admin = false>
	<#if managerRole??>
		<#if managerRole == 'true'>
			<#assign admin = true>
		</#if>					
	</#if>
					

	<#if myprofile>
		getMyAccount("${qmrf_request_json}",false,false);
	<#else>
		<#if admin>
			getMyAccount("${qmrf_request_json}",false,true);
		<#else>
			getMyAccount("${qmrf_request_json}",true,false);
		</#if>	
	</#if>
	
	
	$("#form_myaccount").validate({
		rules : {
			'firstname': {
				required : true
			},
			'lastname': {
				required : true
			},
			'email': {
				required : true,
				email: true
			},
			'homepage': {
				url: true
			}			
			
		},
		messages : {
			'firstname' : "Please provide your first name",
			'lastname'   : {
				required: "Please provide your last name"
			},
			'email'   : {
				required: "Please provide e-mail",
				email: "Please provide valid e-mail"
			},
			'homepage'   : {
				url: "Please provide valid web address"
			}
		}
	});
});
</script>