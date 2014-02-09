<div id='header'>

			<ul class='topLinks'>
			<li class='topLinks'>
			<a class='topLinks external' href='http://qmrf.sf.net/editor'>Download QMRF Editor</a>
			</li>
			<li class='topLinks'>|</li>
			<li class='topLinks'>
			<a class='topLinks email' href='mailto:${qmrf_email}'>Submit QMRF by e-mail</a>
			</li>
			<li class='topLinks'>|</li>
			<li class='topLinks'>
			<a class='topLinks external' href='http://qmrf.sf.net/'>User support</a>
			</li>
			<li class='topLinks'>|</li>

			

			<#if username??>
				<li class='topLinks'>
				<a class='topLinks login' title='You are currently logged in as "${username}". Click here to log out.' href='#' onClick='document.forms["logoutForm"].submit(); return false;'>Log out [<b>${username}</b>]</a>			   
				<form id='logoutForm' action='/qmrf/protected/signout?targetUri=.' method='POST'></form>
				</li>
			<#else>
				<li class='topLinks'>
				<a class='topLinks login' title='Log in here to submit new documents (only required for editors)' href='/qmrf/login'>Log in</a>
				</li>
				<li class='topLinks'>|</li>
				<li class='topLinks'>
				<a class='topLinks register' title='Register for news, issues and updates, and to save your searches and subscribe for alerts.' href='/qmrf/register'>Register</a>
				</li>
			</#if>			
			
			</ul>

			<a href='http://ihcp.jrc.ec.europa.eu/'>
			<img class='logo_top-left' src='/qmrf/images/logo_jrc_ihcp.png' alt='JRC IHCP logo'>
			</a>
</div>
