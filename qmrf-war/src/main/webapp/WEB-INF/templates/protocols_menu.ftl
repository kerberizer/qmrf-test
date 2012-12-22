	<#assign search_title = "QMRF documents search">
				<#assign s = { query.option!"default":"checked"} >
				
				<div class='search ui-widget'>
				<p title=''>${search_title}</p>
				<form method='GET' action='/qmrf/protocol'>
				<table width='100%'>
				<tr><td colspan='2'><input type='text' name='search' size='20' value='${query.search!""}' tabindex='0' title='Enter search query'></td></tr>
				<tr><td colspan='2'><input ${s["title"]!""} tabindex='1' type='radio' value='title' name='option' title='Title' size='20'>Title</td></tr>
				<tr><td colspan='2'><input ${s["text"]!""} tabindex='2' type='radio' value='text' name='option' title='Free text - natural language search' size='20'>Free text</td></tr>
				<tr><td colspan='2'><input ${s["textboolean"]!""} tabindex='3' type='radio' value='textboolean' name='option' title='Free text - boolean search' size='20'>Free text (boolean)</td></tr>
				<tr><td><input ${s["endpoint"]!""} type='radio' tabindex='4' name='option' value='endpoint' title='Search by endpoint'>Endpoint</td>
				<tr><td colspan='2'><input ${s["author"]!""} tabindex='5' type='radio' value='author' name='option' title='Search by author' size='20'>Author</td></tr>
				<tr><td><input ${s["qmrfnumber"]!""} type='radio' tabindex='6' name='option' value='qmrfnumber' title='Search by QMRF number'>QMRF number</td>
				<tr><td>Max number of hits</td><td align='left'><input type='text' size='7' name='pagesize' value='${query.pagesize!"100"}'></td></tr>
				<input type='hidden' name='structure' value='${query.structure!""}'>
				<tr><td colspan='2' align='center'><input type='submit' id='submit' tabindex='8' value='Search'/></td></tr>
				</table>
				</form> 
				&nbsp;
				<div class='structureright'>
					<#if structure??>
						<img border='0' title='Showing QMRF documents for this chemical' width='150' height='150' src='${structure}?media=image%2Fpng&w=150&h=150'>
						<br>Showing QMRF documents
					</#if>
				</div>
				</div>