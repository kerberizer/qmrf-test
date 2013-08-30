<#escape x as x?html>
				<#assign s = { query.option!"default":"checked"} >
				<!-- different title if attachment -->
				<#assign search_title = "Structure search">
				<#assign hint = "Enter any chemical compound identifier (CAS, Name, EINECS, SMILES or InChI). The the input type is guessed automatically.">
				<#assign s = { query.option!"default":"checked"} >
				
				<div class='search ui-widget'>
				<p title="${hint}">${search_title}</p>
				<form method='GET' name='form'  action='/qmrf/chemical'>
				<input type='hidden' name='type' value='smiles'>
				<table width='200px'>
				<tr><td colspan='2' align='center'><input type='button' class='draw' tabindex='0' value='Draw (sub)structure' title='Launches structure diagram editor' onClick='startEditor("${qmrf_root}");'></td></tr>
		   
		   		<tr><td colspan='2' align='center'><input type='text' name='search' size='20' value="${query.search!''}" tabindex='1' title="${hint}"></td></tr>
				<tr><td colspan='2'><input input ${s["auto"]!""} type='radio' value='auto' name='option' title='Exact structure or search by identifier' size='20'>Auto</td></tr>
		   		<tr><td><input input ${s["similarity"]!""} type='radio' name='option' value='similarity' title='Enter SMILES or draw structure'>Similarity</td>
				<td align='left'>
		   		<select title ='Tanimoto similarity threshold' name='threshold'>
		   		<#assign seq = [0.95,0.9,0.85,0.8,0.75,0.7,0.65,0.6,0.55,0.5]>
				<#list seq as x>
					<#if query.threshold??>
						<#if query.threshold == x>
							<option value="${x}" selected>${x}</option>
						<#else>
							<option value="${x}">${x}</option>							
						</#if>
					<#else>
						<option value="${x}">${x}</option>
					</#if>
				</#list>  
		   		</select>
		   		</td></tr>
		   		<tr><td colspan='2'><input ${s["smarts"]!""} type='radio' name='option' value='smarts' title='Enter or draw a SMARTS query' size='20'>Substructure</td></tr>
		   		<tr><td>Max number of hits</td><td align='left'><input type='text' size='3' name='pagesize' value="${query.pagesize}"></td></tr>
		   		<tr><td colspan='2' align='center'><input tabindex='2' id='submit' type='submit' value='Search'/></td></tr>
		   		<tr><td colspan='2' align='center'><input type='hidden' name='structure' value="${query.structure!''}"></td></tr>	   
				
				</table>
				</form> 
				&nbsp;
				<div id='querypic' class='structureright'>
					<#if structure??>
						???
					</#if>
				</div>
				</div>		
</#escape>				