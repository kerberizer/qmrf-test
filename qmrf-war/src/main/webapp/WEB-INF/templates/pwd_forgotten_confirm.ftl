<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='/qmrf/jquery/jquery.passstrength.min.js'></script>
<script type='text/javascript' src='${qmrf_root}/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='/qmrf/scripts/myprofile.js'></script>

<script type='text/javascript'>
$(document).ready(function() {
	$.ajaxSetup({cache:false});//while dev
	pwdStrengthValidatorSetup();
	 $('#pwd1').passStrengthify({minimum:12});
	 
	$("#pwdForm").validate({
		rules : {
			'pwd1': {
				required : true,
				minlength: 12
			},
			'pwd2': {
				required : true,
				minlength: 12,
				equalTo: "#pwd1"
			}
			
		},
		messages : {
			'pwd1'   : {
				required: "Please provide a new password",
				minlength: "Your password must be at least 12 characters long"
			},
			'pwd2'   : {
				required: "Please provide a new password",
				minlength: "Your password must be at least 12 characters long",
				equalTo: "Please enter the same password as above"
			}			
		}
	});	
	<#if qmrf_reg_confirmed??>
	    $.ajax({
	        dataType: "json",
	        async: false,
	        url: "${qmrf_root}/forgotten/confirm?code=${qmrf_reg_confirmed}&media=application%2Fjson",
	        success: function(data, status, xhr) {
	           	$.each(data["confirmation"],function(index, entry) {
	        		if ('confirmed' == entry["status"]) { 
	        			$("#failure").hide();
	        			$(".success").show();
	        		} else {
	        			$("#failure").hide();
	        			$(".success").show();	        			
	        		}
	        	});
	        },
	        statusCode: {
	            404: function() {
	        		$("#failure").show();
	        		$(".success").hide();
	            }
	        },
	        error: function(xhr, status, err) {
	       		$("#failure").show();
        		$(".success").hide();
        		$("#progress").hide();
	        },
	        complete: function(xhr, status) {
	        	$("#progress").hide();
	        }
	     });		
	</#if>
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
	    
		<#if qmrf_reg_confirmed??>
			
			<form action="${qmrf_root}/forgotten/confirm?code=${qmrf_reg_confirmed}&method=PUT" id="pwdForm"  method="POST" >
				<table class='success' width='80%%'>
				<tbody>	
				<tr>
				<th>				
					<label class='three columns alpha' for="pwd1">New password</label>
				</th>
				</tr>
				<tr>
				<td>
					<input class="five columns alpha half-bottom" type='password' size='40' id='pwd1' name='pwd1' value=''/>
				</td>
				</tr>		
				<tr>
				<th>		 	
					<label class='three columns alpha' for="pwd2">Confirm new password</label>
				</th>
				</tr>
				<tr>
				<td>				
					<input class="five columns alpha half-bottom"  type='password' size='40' id='pwd2' name='pwd2' value=''/>
				</td>
				</tr>	
				<tr>
				<td>
					<input class="three columns alpha half-bottom" id='updatepwd' name='updatepwd' type='submit' class='submit' value='Update'>
				</td>		
				<tr>
				</tbody>
				</table>
				</form>	
	
				<div id='failure' class='row' style='display:none'>
				<p>&nbsp</p>
				<p>
				<span class="ui-icon ui-icon-alert" style="display:inline-block"></span>
				Invalid confirmation code!
				</p>				
				</div>
				
				<img id='progress' src="${qmrf_root}/images/progress.gif" alt="Please wait..." title="Please wait...">
				</tbody>
				</table>
			</form>		
			<#else>
			
			<p>&nbsp</p>
				<p>
				<span class="ui-icon ui-icon-info" style="display:inline-block"></span>
				Please follow the instructions in the password reset e-mail.
				</p>
			</#if>


		</div>
		
	</div> <#-- w_content -->

	<#-- Prevents the footer from hiding the last line on smaller screens. -->
	<div class='pusher'></div>
	
	</div> <#-- w_wrap -->
	
<#include "/footer.ftl">
<#include "/scripts-welcome.ftl">
</body>

</html>

	