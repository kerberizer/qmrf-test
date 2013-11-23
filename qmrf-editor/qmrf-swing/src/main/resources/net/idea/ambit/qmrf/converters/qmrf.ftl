<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:ot="http://opentox.org/api/1.1/">

<script type="text/javascript" src="http://ambit.uni-plovdiv.bg:8080/qmrf/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="http://ambit.uni-plovdiv.bg:8080/qmrf/jquery/jquery-ui-1.8.18.custom.min.js"></script>
<link href="http://ambit.uni-plovdiv.bg:8080/qmrf/style/ambit.css" rel="stylesheet" type="text/css">
<link href="http://ambit.uni-plovdiv.bg:8080/qmrf/style/jquery-ui-1.8.18.custom.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<head>
  <title>Welcome!</title>
</head>
<body>
  <h1>QMRF ${title}!</h1>
  
  <p>Author ${author}</p>
   <p>Version ${version}</p>
	<p>Date ${date}</p>   
  <p>email ${email}</p>
  <p>Contact ${contact}</p>
  <p><a href="">${attributes.url}</a></p>
   
  
<#list chapters as chapter>
<h3>${chapter.chapter}.${chapter.title}</h3>

   	<#list chapter.subchapters.iterator as subchapter>

    <h4>${subchapter.chapter}.${subchapter.title}</h4>
	     <p>${subchapter.text}</p>
	     <#-- yes/no answers -->
	     <#if subchapter.attributes.answer??>
	     	${subchapter.attributes.answer}
	     </#if>
	     <#-- options -->
	     <#if subchapter.options??>
	     	<ul>
		     <#if subchapter.attributes.chemname??>
		     	<li>Chemical name&nbsp;${subchapter.attributes.chemname}
		     </#if>	     
		     <#if subchapter.attributes.cas??>
		     	<li>CAS&nbsp;${subchapter.attributes.cas}
		     </#if>	     
		     <#if subchapter.attributes.smiles??>
		     	<li>SMILES&nbsp;${subchapter.attributes.smiles}
		     </#if>	     
		     <#if subchapter.attributes.inchi??>
		     	<li>InChI&nbsp;${subchapter.attributes.inchi}
		     </#if>	     
		     <#if subchapter.attributes.mol??>
		     	<li>MOL file&nbsp;${subchapter.attributes.mol}
		     </#if>	     	
		     <#if subchapter.attributes.formula??>
		     	<li>Formula&nbsp;${subchapter.attributes.formula}
		     </#if>
	     		</ul>	   
	     </#if>
	     <#-- attachments -->
	     <#if subchapter.attachments??>
	     	<table>
	     	<#list subchapter.attachments as attachmentType>
	     		<#list attachmentType as attachment>
	     		   <tr>	
				   <th>${attachmentType.title}</th>
				   <td>${attachment.description}</td>
   				   <td><a href="${attachment.url}">${attachment.name}.${attachment.filetype}</a></td>				   	     		   
	     		   </tr>
	     		 </#list>
	     	</#list>
	     	</table>
	     </#if>	     
 		 <#if subchapter.catalogReference??>
 		 		<table width='100%'>
		    	<#list subchapter.catalogReference.iterator as entry>
		    		
		    		<tr>

		    		<!# -- Bibliography -->
					<#if entry.attributes.title??><td>${entry.attributes.title}</td></#if>
							    		
		    		<!# -- Endpoint -->
		    		<#if entry.attributes.group??><td>${entry.attributes.group}</td></#if>
		    		<#if entry.attributes.subgroup??><td>${entry.attributes.subgroup}</td></#if>
					<#if entry.attributes.name??><td>${entry.attributes.name}</td></#if>
				
		    		<!# -- Descriptors -->
		    		<#if entry.attributes.units??><td>${entry.attributes.units}</td></#if>
		    		<#if entry.attributes.description??><td>${entry.attributes.description}</td></#if>

					<!# -- Author -->
		    		<#if entry.attributes.affiliation??><td>${entry.attributes.affiliation}</td></#if>
		    		<#if entry.attributes.email??><td>${entry.attributes.email}</td></#if>
		    		
					<!# -- Algorithm -->
		    		<#if entry.attributes.definition??><td>${entry.attributes.definition}</td></#if>
		    		<#if entry.attributes.description??><td>${entry.attributes.description}</td></#if>
		    				    		
		    		<!# -- Software -->
		    		<#if entry.attributes.contact??><td>${entry.attributes.contact}</td></#if>
		    		<#if entry.attributes.url??><td><a href="${entry.attributes.url}">${entry.attributes.url}</a></td></#if>
		    				    		
					</tr>
   				</#list>
   				</table>
		 </#if>

 	 </#list> <!#-- subchapter -->
</#list>  <!#-- chapter -->

</body>
</html>  