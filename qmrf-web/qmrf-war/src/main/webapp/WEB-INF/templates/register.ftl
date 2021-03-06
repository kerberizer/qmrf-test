<#include "/html.ftl" >
<head>
<#include "/head.ftl" >
<script type='text/javascript' src='/qmrf/jquery/jquery.validate.min.js'></script>
<script type='text/javascript' src='/qmrf/jquery/jquery.passstrength.min.js'></script>
<script type='text/javascript' src='/qmrf/scripts/myprofile.js'></script>

<script type='text/javascript'>

$().ready(function() {
    $('#pwd1').passStrengthify({minimum:12});
    
	$("#affiliation").autocomplete({
    		source: function (request, response) {
    		$.ajax({
                   url: "/qmrf/organisation?media=application/json&search="+request.term,
                   contentType: "application/json; charset=utf-8",
                   dataType: "json",
                   success: function (data) {
    	                response(
    	                    $.map(data.group, function (item) {
    	                       return {
    	                           label: item.title,
    	                           value: item.title
    	                       }
    	                   }));
    	               }
    	           })
    		},
    	    minLength: 2	            
    	   });
    				
	// validate the comment form when it is submitted
	
	pwdStrengthValidatorSetup();
	
	$("#registerForm").validate({
		rules : {
			'username': {
				required : true,
				minlength: 3,
				maxlength: 16
			},		
			'firstname': {
				required : true
			},
			'lastname': {
				minlength: 2,
				required : true
			},
			'email': {
				required : true,
				email: true
			},
			'homepage': {
				url: true
			},
			'affiliation': {
				minlength: 2,
				required : true
			},		
			'pwd1': {
				required : true,
				strength : true,
				minlength: 12
			},
			'pwd2': {
				required : true,
				minlength: 12,
				equalTo: "#pwd1"
			},
			'privacy' : {
				required : true
			}
			
		},
		messages : {
			'username'  : {
				required: "Please enter user name"
			},
			'firstname' : "Please provide your first name",
			'lastname'  : {
				required: "Please provide your last name"
			},
			'affiliation'  : {
				required: "Please provide your affiliation"
			},			
			'email'     : {
				required: "Please provide e-mail",
				email: "Please provide valid e-mail"
			},
			'homepage'  : {
				url: "Please provide valid web address"
			},
			'pwd1'      : {
				required: "",
				minlength: ""
			},
			'pwd2'      : {
				required: "Please confirm the password",
				minlength: "Your password must be at least 12 characters long",
				equalTo: "Please enter the same password as above"
			},
			'privacy' : {
				required : "Please read the privacy statement!"
			}		
		}
	});
	
	$(function() {
    	$( "#readstatement" ).dialog({
    	    autoOpen: false,
      		height:480,
      		width:640,
      		modal: true,
      		buttons: {
		        "I Accept": function() {
		          $('input:radio[name=privacy]:nth(0)').attr('checked',true);
		          $('input:radio[name=privacy]:nth(0)').attr('disabled',false);

		          $('#register').prop('disabled', false);
		          $( this ).dialog( "close" );
		        },
		        "I Reject": function() {
		          $('input:radio[name=privacy]:nth(1)').attr('checked',true);
		          $('input:radio[name=privacy]:nth(1)').attr('disabled',false);
		           $('#privacy').prop('disabled', false);
		          $('#register').prop('disabled', true);
		          $( this ).dialog( "close" );
		        }
      		}    	
    	});
    	
  	});
	$( "#open_statement" ).click(function() {
		var helpURI =  "${qmrf_root}/static/privacy.html?media=text/html";
		$( "#readstatement p" ).load( helpURI);
		$( "#readstatement" ).dialog( "open" );		
    });  	
});
	
</script>
<style type="text/css">
label { width: 10em; float: left; }
label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }
p { clear: both; }
.submit { margin-left: 12em; }
em { font-weight: bold; padding-right: 1em; vertical-align: top; }
</style>

</head>
<body>
	<div id='wrap'>
	<#include "/banner.ftl">
	<#include "/menu.ftl">

	<div class='w_content'>
		<div class='ui-widget ' style='padding: 0 .7em;'>
		<div class='ui-widget-header ui-corner-top'><p>Registration form</p></div>
	    <div class='ui-widget-content ui-corner-bottom'>				
	    	<p>Registration is only necessary if you wish to be informed about news, issues and updates. Once registered and logged in, you will also be able to save your searches for future use and subscribe for alerts.</p>
			<form action="/qmrf/register" id="registerForm"  method="POST" autocomplete='off' >		
			
			<p><label for="username">User name<em>*</em></label><input type="text" size='40' name='username' id='username' value=''></p>
			<p><label for="title">Title</label><input type="text" size='40' name='title' id='title' value=''></p>
			<p><label for="firstname">First name<em>*</em></label><input type="text" size='40' name='firstname' id='firstname' value=''></p>
			<p><label for="lastname">Last name<em>*</em></label><input type="text"  size='40' name='lastname' id='lastname' value=''></p>
			<p><label for="affiliation">Affiliation<em>*</em></label><input type="text"  size='40' name='affiliation' id='affiliation' value=''></p>
			<p><label for="email">e-mail<em>*</em></label><input type="text"size='40' name='email' id='email' value=''></p>
			
			<p><label for="homepage">WWW</label><input type="text"  size='40' name='homepage' id='homepage' value=''></p>
			<p><label for="keywords">Keywords</label><input type="text"  size='40' name='keywords' id='keywords' value=''></p>
			<p><label for="reviewer">Available as a reviewer</label><input type="checkbox" name='reviewer' id='reviewer' value=''></p>
					
			<p><label for='pwd1'>Password<em>*</em></label><input type='password' size='40' id='pwd1' name='pwd1' value=''></p>
			<p><label for='pwd2'>Confirm password<em>*</em></label><input type='password' size='40' id='pwd2' name='pwd2' value=''></p>
			<p><label for="privacy">&nbsp;</label>
			<a href="#" id="open_statement" >JRC PRIVACY STATEMENT</a>
			<input type="radio" name='privacy' disabled value='Accept'>I Accept
			<input type="radio" name='privacy' disabled value='Reject'>I Reject
			</p>
		
			<p><input id='register' name='register' type='submit' class='submit' disabled value='Register'></p>

			</form>		
		</div>
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
